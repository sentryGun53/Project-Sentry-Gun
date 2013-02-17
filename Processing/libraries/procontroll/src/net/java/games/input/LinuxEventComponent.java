/**
 * Copyright (C) 2003 Jeremy Booth (jeremy@newdawnsoftware.com)
 *
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this 
 * list of conditions and the following disclaimer. Redistributions in binary 
 * form must reproduce the above copyright notice, this list of conditions and 
 * the following disclaimer in the documentation and/or other materials provided
 * with the distribution. 
 * The name of the author may not be used to endorse or promote products derived
 * from this software without specific prior written permission. 
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF 
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO 
 * EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, 
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; 
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, 
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR 
 * OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF 
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE
 */
package net.java.games.input;

import java.io.IOException;

/**
 * @author elias
 * @author Jeremy Booth (jeremy@newdawnsoftware.com)
 */
final class LinuxEventComponent {
	private final LinuxEventDevice device;
	private final Component.Identifier identifier;
	private final Controller.Type button_trait;
	private final boolean is_relative;
	private final LinuxAxisDescriptor descriptor;
	private final int min;
	private final int max;
	private final int flat;

	
	public LinuxEventComponent(LinuxEventDevice device, Component.Identifier identifier, boolean is_relative, int native_type, int native_code) throws IOException {
		this.device = device;
		this.identifier = identifier;
		if (native_type == NativeDefinitions.EV_KEY)
			this.button_trait = LinuxNativeTypesMap.guessButtonTrait(native_code);
		else
			this.button_trait = Controller.Type.UNKNOWN;
		this.is_relative = is_relative;
		this.descriptor = new LinuxAxisDescriptor();
		descriptor.set(native_type, native_code);
		if (native_type == NativeDefinitions.EV_ABS) {
			LinuxAbsInfo abs_info = new LinuxAbsInfo();
			getAbsInfo(abs_info);
			this.min = abs_info.getMin();
			this.max = abs_info.getMax();
			this.flat = abs_info.getFlat();
		} else {
			this.min = Integer.MIN_VALUE;
			this.max = Integer.MAX_VALUE;
			this.flat = 0;
		}
	}

	public final LinuxEventDevice getDevice() {
		return device;
	}

	public final void getAbsInfo(LinuxAbsInfo abs_info) throws IOException {
		//assert descriptor.getType() == NativeDefinitions.EV_ABS;
		device.getAbsInfo(descriptor.getCode(), abs_info);
	}
	
	public final Controller.Type getButtonTrait() {
		return button_trait;
	}
	
	public final Component.Identifier getIdentifier() {
		return identifier;
	}

	public final LinuxAxisDescriptor getDescriptor() {
		return descriptor;
	}
	
	public final boolean isRelative() {
		return is_relative;
	}

	public final boolean isAnalog() {
		return identifier instanceof Component.Identifier.Axis && identifier != Component.Identifier.Axis.POV;
	}

	final float convertValue(float value) {
		if (identifier instanceof Component.Identifier.Axis && !is_relative) {
			// Some axes have min = max = 0
			if (min == max)
				return 0;
			if (value > max)
				value = max;
			else if (value < min)
				value = min;
			return 2*(value - min)/(max - min) - 1;
		} else {
			return value;
		}
	}

	final float getDeadZone() {
		return flat/(2f*(max - min));
	}
}

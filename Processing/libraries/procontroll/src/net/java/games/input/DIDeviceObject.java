/*
 * %W% %E%
 *
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
/*****************************************************************************
 * Copyright (c) 2003 Sun Microsystems, Inc.  All Rights Reserved.
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * - Redistribution of source code must retain the above copyright notice,
 *   this list of conditions and the following disclaimer.
 *
 * - Redistribution in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materails provided with the distribution.
 *
 * Neither the name Sun Microsystems, Inc. or the names of the contributors
 * may be used to endorse or promote products derived from this software
 * without specific prior written permission.
 *
 * This software is provided "AS IS," without a warranty of any kind.
 * ALL EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES, INCLUDING
 * ANY IMPLIED WARRANT OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR
 * NON-INFRINGEMEN, ARE HEREBY EXCLUDED.  SUN MICROSYSTEMS, INC. ("SUN") AND
 * ITS LICENSORS SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS
 * A RESULT OF USING, MODIFYING OR DESTRIBUTING THIS SOFTWARE OR ITS 
 * DERIVATIVES.  IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE FOR ANY LOST
 * REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT, SPECIAL, CONSEQUENTIAL,
 * INCIDENTAL OR PUNITIVE DAMAGES.  HOWEVER CAUSED AND REGARDLESS OF THE THEORY
 * OF LIABILITY, ARISING OUT OF THE USE OF OUR INABILITY TO USE THIS SOFTWARE,
 * EVEN IF SUN HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 *
 * You acknowledge that this software is not designed or intended for us in
 * the design, construction, operation or maintenance of any nuclear facility
 *
 *****************************************************************************/
package net.java.games.input;

import java.io.IOException;

/** Java wrapper for DIDEVICEOBJECTINSTANCE
 * @author elias
 * @version 1.0
 */
final class DIDeviceObject {
	//DirectInput scales wheel deltas by 120
	private final static int WHEEL_SCALE = 120;
	
	private final IDirectInputDevice device;
	private final byte[] guid;
	private final int identifier;
	private final int type;
	private final int instance;
	private final int guid_type;
	private final int flags;
	private final String name;
	private final Component.Identifier id;
	private final int format_offset;
	private final long min;
	private final long max;
	private final int deadzone;

	/* These are used for emulating relative axes */
	private int last_poll_value;
	private int last_event_value;
	
	public DIDeviceObject(IDirectInputDevice device, Component.Identifier id, byte[] guid, int guid_type, int identifier, int type, int instance, int flags, String name, int format_offset) throws IOException {
		this.device = device;
		this.id = id;
		this.guid = guid;
		this.identifier = identifier;
		this.type = type;
		this.instance = instance;
		this.guid_type = guid_type;
		this.flags = flags;
		this.name = name;
		this.format_offset = format_offset;
		if (isAxis() && !isRelative()) {
			long[] range = device.getRangeProperty(identifier);
			this.min = range[0];
			this.max = range[1];
			this.deadzone = device.getDeadzoneProperty(identifier);
		} else {
			this.min = IDirectInputDevice.DIPROPRANGE_NOMIN;
			this.max = IDirectInputDevice.DIPROPRANGE_NOMAX;
			this.deadzone = 0;
		}
	}

	public final synchronized int getRelativePollValue(int current_abs_value) {
		if (device.areAxesRelative())
			return current_abs_value;
		int rel_value = current_abs_value - last_poll_value;
		last_poll_value = current_abs_value;
		return rel_value;
	}

	public final synchronized int getRelativeEventValue(int current_abs_value) {
		if (device.areAxesRelative())
			return current_abs_value;
		int rel_value = current_abs_value - last_event_value;
		last_event_value = current_abs_value;
		return rel_value;
	}

	public final int getGUIDType() {
		return guid_type;
	}

	public final int getFormatOffset() {
		return format_offset;
	}

	public final IDirectInputDevice getDevice() {
		return device;
	}

	public final int getDIIdentifier() {
		return identifier;
	}

	public final Component.Identifier getIdentifier() {
		return id;
	}

	public final String getName() {
		return name;
	}

	public final int getInstance() {
		return instance;
	}

	public final int getType() {
		return type;
	}

	public final byte[] getGUID() {
		return guid;
	}

	public final int getFlags() {
		return flags;
	}

	public final long getMin() {
		return min;
	}

	public final long getMax() {
		return max;
	}

	public final float getDeadzone() {
		return deadzone;
	}

	public final boolean isButton() {
		return (type & IDirectInputDevice.DIDFT_BUTTON) != 0;
	}
	
	public final boolean isAxis() {
		return (type & IDirectInputDevice.DIDFT_AXIS) != 0;
	}
	
	public final boolean isRelative() {
		return isAxis() && (type & IDirectInputDevice.DIDFT_RELAXIS) != 0;
	}

	public final boolean isAnalog() {
		return isAxis() && id != Component.Identifier.Axis.POV;
	}

	public final float convertValue(float value) {
		if (getDevice().getType() == IDirectInputDevice.DI8DEVTYPE_MOUSE && id == Component.Identifier.Axis.Z) {
			return value/WHEEL_SCALE;
		} else if (isButton()) {
			return (((int)value) & 0x80) != 0 ? 1 : 0;
		} else if (id == Component.Identifier.Axis.POV) {
			int int_value = (int)value;
			if ((int_value & 0xFFFF) == 0xFFFF)
				return Component.POV.OFF;
			// DirectInput returns POV directions in hundredths of degree clockwise from north
			int slice = 360*100/16;
			if (int_value >= 0 && int_value < slice)
				return Component.POV.UP;
			else if (int_value < 3*slice)
				return Component.POV.UP_RIGHT;
			else if (int_value < 5*slice)
				return Component.POV.RIGHT;
			else if (int_value < 7*slice)
				return Component.POV.DOWN_RIGHT;
			else if (int_value < 9*slice)
				return Component.POV.DOWN;
			else if (int_value < 11*slice)
				return Component.POV.DOWN_LEFT;
			else if (int_value < 13*slice)
				return Component.POV.LEFT;
			else if (int_value < 15*slice)
				return Component.POV.UP_LEFT;
			else
				return Component.POV.UP;
		} else if (isAxis() && !isRelative()) {
			return 2*(value - min)/(float)(max - min) - 1;
		} else
			return value;
	}

}

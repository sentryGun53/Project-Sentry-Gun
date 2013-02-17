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
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

/**
 * @author elias
 */
final class LinuxEventDevice implements LinuxDevice {
	private final Map component_map = new HashMap();
	private final Rumbler[] rumblers;
	private final long fd;
	private final String name;
	private final LinuxInputID input_id;
	private final List components;
	private final Controller.Type type;

	/* Closed state variable that protects the validity of the file descriptor.
	 *  Access to the closed state must be synchronized
	 */
	private boolean closed;

	/* Access to the key_states array could be synchronized, but
	 * it doesn't hurt to have multiple threads read/write from/to it
	 */
	private final byte[] key_states = new byte[NativeDefinitions.KEY_MAX/8 + 1];
	
    public LinuxEventDevice(String filename) throws IOException {
		long fd;
		boolean detect_rumblers = true;
		try {
			fd = nOpen(filename, true);
		} catch (IOException e) {
			fd = nOpen(filename, false);
			detect_rumblers = false;
		}
		this.fd = fd;
		try {
			this.name = getDeviceName();
			this.input_id = getDeviceInputID();
			this.components = getDeviceComponents();
			if (detect_rumblers)
				this.rumblers = enumerateRumblers();
			else
				this.rumblers = new Rumbler[]{};
			this.type = guessType();
		} catch (IOException e) {
			close();
			throw e;
		}
    }
	private final static native long nOpen(String filename, boolean rw) throws IOException;

	public final Controller.Type getType() {
		return type;
	}

	private final static int countComponents(List components, Class id_type, boolean relative) {
		int count = 0;
		for (int i = 0; i < components.size(); i++) {
			LinuxEventComponent component = (LinuxEventComponent)components.get(i);
			if (id_type.isInstance(component.getIdentifier()) && relative == component.isRelative())
				count++;
		}
		return count;
	}

	private final Controller.Type guessType() throws IOException {
		Controller.Type type_from_usages = guessTypeFromUsages();
		if (type_from_usages == Controller.Type.UNKNOWN)
			return guessTypeFromComponents();
		else
			return type_from_usages;
	}
	
	private final Controller.Type guessTypeFromUsages() throws IOException {
		byte[] usage_bits = getDeviceUsageBits();
		if (isBitSet(usage_bits, NativeDefinitions.USAGE_MOUSE))
			return Controller.Type.MOUSE;
		else if (isBitSet(usage_bits, NativeDefinitions.USAGE_KEYBOARD))
			return Controller.Type.KEYBOARD;
		else if (isBitSet(usage_bits, NativeDefinitions.USAGE_GAMEPAD))
			return Controller.Type.GAMEPAD;
		else if (isBitSet(usage_bits, NativeDefinitions.USAGE_JOYSTICK))
			return Controller.Type.STICK;
		else
			return Controller.Type.UNKNOWN;
	}

	private final Controller.Type guessTypeFromComponents() throws IOException {
		List components = getComponents();
		if (components.size() == 0)
			return Controller.Type.UNKNOWN;
		int num_rel_axes = countComponents(components, Component.Identifier.Axis.class, true);
		int num_abs_axes = countComponents(components, Component.Identifier.Axis.class, false);
		int num_keys = countComponents(components, Component.Identifier.Key.class, false);
		int mouse_traits = 0;
		int keyboard_traits = 0;
		int joystick_traits = 0;
		int gamepad_traits = 0;
		if (name.toLowerCase().indexOf("mouse") != -1)
			mouse_traits++;
		if (name.toLowerCase().indexOf("keyboard") != -1)
			keyboard_traits++;
		if (name.toLowerCase().indexOf("joystick") != -1)
			joystick_traits++;
		if (name.toLowerCase().indexOf("gamepad") != -1)
			gamepad_traits++;
		int num_keyboard_button_traits = 0;
		int num_mouse_button_traits = 0;
		int num_joystick_button_traits = 0;
		int num_gamepad_button_traits = 0;
		// count button traits
		for (int i = 0; i < components.size(); i++) {
			LinuxEventComponent component = (LinuxEventComponent)components.get(i);
			if (component.getButtonTrait() == Controller.Type.MOUSE)
				num_mouse_button_traits++;
			else if (component.getButtonTrait() == Controller.Type.KEYBOARD)
				num_keyboard_button_traits++;
			else if (component.getButtonTrait() == Controller.Type.GAMEPAD)
				num_gamepad_button_traits++;
			else if (component.getButtonTrait() == Controller.Type.STICK)
				num_joystick_button_traits++;
		}
		if ((num_mouse_button_traits >= num_keyboard_button_traits) && (num_mouse_button_traits >= num_joystick_button_traits) && (num_mouse_button_traits >= num_gamepad_button_traits)) {
			mouse_traits++;
		} else if ((num_keyboard_button_traits >= num_mouse_button_traits) && (num_keyboard_button_traits >= num_joystick_button_traits) && (num_keyboard_button_traits >= num_gamepad_button_traits)) {
			keyboard_traits++;
		} else if ((num_joystick_button_traits >= num_keyboard_button_traits) && (num_joystick_button_traits >= num_mouse_button_traits) && (num_joystick_button_traits >= num_gamepad_button_traits)) {
			joystick_traits++;
		} else if ((num_gamepad_button_traits >= num_keyboard_button_traits) && (num_gamepad_button_traits >= num_mouse_button_traits) && (num_gamepad_button_traits >= num_joystick_button_traits)) {
			gamepad_traits++;
		}
		if (num_rel_axes >= 2) {
			mouse_traits++;
		}
		if (num_abs_axes >= 2) {
			joystick_traits++;
			gamepad_traits++;
		}

		if ((mouse_traits >= keyboard_traits) && (mouse_traits >= joystick_traits) && (mouse_traits >= gamepad_traits)) {
			return Controller.Type.MOUSE;
		} else if ((keyboard_traits >= mouse_traits) && (keyboard_traits >= joystick_traits) && (keyboard_traits >= gamepad_traits)) {
			return Controller.Type.KEYBOARD;
		} else if ((joystick_traits >= mouse_traits) && (joystick_traits >= keyboard_traits) && (joystick_traits >= gamepad_traits)) {
			return Controller.Type.STICK;
		} else if ((gamepad_traits >= mouse_traits) && (gamepad_traits >= keyboard_traits) && (gamepad_traits >= joystick_traits)) {
			return Controller.Type.GAMEPAD;
		} else
			return null;
	}

	private final Rumbler[] enumerateRumblers() {
		List rumblers = new ArrayList();
		try {
			int num_effects = getNumEffects();
			if (num_effects <= 0)
				return (Rumbler[])rumblers.toArray(new Rumbler[]{});
			byte[] ff_bits = getForceFeedbackBits();
			if (isBitSet(ff_bits, NativeDefinitions.FF_RUMBLE) && num_effects > rumblers.size()) {
				rumblers.add(new LinuxRumbleFF(this));
			}
		} catch (IOException e) {
			ControllerEnvironment.logln("Failed to enumerate rumblers: " + e.getMessage());
		}
		return (Rumbler[])rumblers.toArray(new Rumbler[]{});
	}

	public final Rumbler[] getRumblers() {
		return rumblers;
	}

	public final synchronized int uploadRumbleEffect(int id, int trigger_button, int direction, int trigger_interval, int replay_length, int replay_delay, int strong_magnitude, int weak_magnitude) throws IOException {
		checkClosed();
		return nUploadRumbleEffect(fd, id, direction, trigger_button, trigger_interval, replay_length, replay_delay, strong_magnitude, weak_magnitude);
	}
	private final static native int nUploadRumbleEffect(long fd, int id, int direction, int trigger_button, int trigger_interval, int replay_length, int replay_delay, int strong_magnitude, int weak_magnitude) throws IOException;

	public final synchronized int uploadConstantEffect(int id, int trigger_button, int direction, int trigger_interval, int replay_length, int replay_delay, int constant_level, int constant_env_attack_length, int constant_env_attack_level, int constant_env_fade_length, int constant_env_fade_level) throws IOException {
		checkClosed();
		return nUploadConstantEffect(fd, id, direction, trigger_button, trigger_interval, replay_length, replay_delay, constant_level, constant_env_attack_length, constant_env_attack_level, constant_env_fade_length, constant_env_fade_level);
	}
	private final static native int nUploadConstantEffect(long fd, int id, int direction, int trigger_button, int trigger_interval, int replay_length, int replay_delay, int constant_level, int constant_env_attack_length, int constant_env_attack_level, int constant_env_fade_length, int constant_env_fade_level) throws IOException;

	final void eraseEffect(int id) throws IOException {
		nEraseEffect(fd, id);
	}
	private final static native void nEraseEffect(long fd, int ff_id) throws IOException;
	
	public final synchronized void writeEvent(int type, int code, int value) throws IOException {
		checkClosed();
		nWriteEvent(fd, type, code, value);
	}
	private final static native void nWriteEvent(long fd, int type, int code, int value) throws IOException;

	public final void registerComponent(LinuxAxisDescriptor desc, LinuxComponent component) {
		component_map.put(desc, component);
	}

	public final LinuxComponent mapDescriptor(LinuxAxisDescriptor desc) {
		return (LinuxComponent)component_map.get(desc);
	}

	public final Controller.PortType getPortType() throws IOException {
		return input_id.getPortType();
	}
	
	public final LinuxInputID getInputID() {
		return input_id;
	}
	
	private final LinuxInputID getDeviceInputID() throws IOException {
		return nGetInputID(fd);
	}
	private final static native LinuxInputID nGetInputID(long fd) throws IOException;
	
	public final int getNumEffects() throws IOException {
		return nGetNumEffects(fd);
	}
	private final static native int nGetNumEffects(long fd) throws IOException;

	private final int getVersion() throws IOException {
		return nGetVersion(fd);
	}
	private final static native int nGetVersion(long fd) throws IOException;

	public final synchronized boolean getNextEvent(LinuxEvent linux_event) throws IOException {
		checkClosed();
		return nGetNextEvent(fd, linux_event);
	}
	private final static native boolean nGetNextEvent(long fd, LinuxEvent linux_event) throws IOException;
	
	public final synchronized void getAbsInfo(int abs_axis, LinuxAbsInfo abs_info) throws IOException {
		checkClosed();
		nGetAbsInfo(fd, abs_axis, abs_info);
	}
	private final static native void nGetAbsInfo(long fd, int abs_axis, LinuxAbsInfo abs_info) throws IOException;

	private final void addKeys(List components) throws IOException {
		byte[] bits = getKeysBits();
		for (int i = 0; i < bits.length*8; i++) {
			if (isBitSet(bits, i)) {
				Component.Identifier id = LinuxNativeTypesMap.getButtonID(i);
				components.add(new LinuxEventComponent(this, id, false, NativeDefinitions.EV_KEY, i));
			}
		}
	}
	
	private final void addAbsoluteAxes(List components) throws IOException {
		byte[] bits = getAbsoluteAxesBits();
		for (int i = 0; i < bits.length*8; i++) {
			if (isBitSet(bits, i)) {
				Component.Identifier id = LinuxNativeTypesMap.getAbsAxisID(i);
				components.add(new LinuxEventComponent(this, id, false, NativeDefinitions.EV_ABS, i));
			}
		}
	}

	private final void addRelativeAxes(List components) throws IOException {
		byte[] bits = getRelativeAxesBits();
		for (int i = 0; i < bits.length*8; i++) {
			if (isBitSet(bits, i)) {
				Component.Identifier id = LinuxNativeTypesMap.getRelAxisID(i);
				components.add(new LinuxEventComponent(this, id, true, NativeDefinitions.EV_REL, i));
			}
		}
	}

	public final List getComponents() {
		return components;
	}

	private final List getDeviceComponents() throws IOException {
		List components = new ArrayList();
		byte[] evtype_bits = getEventTypeBits();
		if (isBitSet(evtype_bits, NativeDefinitions.EV_KEY))
			addKeys(components);
		if (isBitSet(evtype_bits, NativeDefinitions.EV_ABS))
			addAbsoluteAxes(components);
		if (isBitSet(evtype_bits, NativeDefinitions.EV_REL))
			addRelativeAxes(components);
		return components;
	}
	
	private final byte[] getForceFeedbackBits() throws IOException {
		byte[] bits = new byte[NativeDefinitions.FF_MAX/8 + 1];
		nGetBits(fd, NativeDefinitions.EV_FF, bits);
		return bits;
	}
	
	private final byte[] getKeysBits() throws IOException {
		byte[] bits = new byte[NativeDefinitions.KEY_MAX/8 + 1];
		nGetBits(fd, NativeDefinitions.EV_KEY, bits);
		return bits;
	}
	
	private final byte[] getAbsoluteAxesBits() throws IOException {
		byte[] bits = new byte[NativeDefinitions.ABS_MAX/8 + 1];
		nGetBits(fd, NativeDefinitions.EV_ABS, bits);
		return bits;
	}
	
	private final byte[] getRelativeAxesBits() throws IOException {
		byte[] bits = new byte[NativeDefinitions.REL_MAX/8 + 1];
		nGetBits(fd, NativeDefinitions.EV_REL, bits);
		return bits;
	}
	
	private final byte[] getEventTypeBits() throws IOException {
		byte[] bits = new byte[NativeDefinitions.EV_MAX/8 + 1];
		nGetBits(fd, 0, bits);
		return bits;
	}
	private final static native void nGetBits(long fd, int ev_type, byte[] evtype_bits) throws IOException;

	private final byte[] getDeviceUsageBits() throws IOException {
		byte[] bits = new byte[NativeDefinitions.USAGE_MAX/8 + 1];
		if (getVersion() >= 0x010001) {
			nGetDeviceUsageBits(fd, bits);
		}
		return bits;
	}
	private final static native void nGetDeviceUsageBits(long fd, byte[] type_bits) throws IOException;

	public final synchronized void pollKeyStates() throws IOException {
		nGetKeyStates(fd, key_states);
	}
	private final static native void nGetKeyStates(long fd, byte[] states) throws IOException;

	public final boolean isKeySet(int bit) {
		return isBitSet(key_states, bit);
	}
	
	public final static boolean isBitSet(byte[] bits, int bit) {
		return (bits[bit/8] & (1<<(bit%8))) != 0;
	}

	public final String getName() {
		return name;
	}
	
	private final String getDeviceName() throws IOException {
		return nGetName(fd);
	}
	private final static native String nGetName(long fd) throws IOException;

	public synchronized final void close() throws IOException {
		if (closed)
			return;
		closed = true;
		LinuxEnvironmentPlugin.execute(new LinuxDeviceTask() {
			protected final Object execute() throws IOException {
				nClose(fd);
				return null;
			}
		});
	}
	private final static native void nClose(long fd) throws IOException;

	private final void checkClosed() throws IOException {
		if (closed)
			throw new IOException("Device is closed");
	}

	protected void finalize() throws IOException {
		close();
	}
}

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
final class LinuxJoystickDevice implements LinuxDevice {
	public final static int JS_EVENT_BUTTON     = 0x01;    /* button pressed/released */
	public final static int JS_EVENT_AXIS       = 0x02;    /* joystick moved */
	public final static int JS_EVENT_INIT       = 0x80;    /* initial state of device */

	public final static int AXIS_MAX_VALUE = 32767;

	private final long fd;
	private final String name;

	private final LinuxJoystickEvent joystick_event = new LinuxJoystickEvent();
	private final Event event = new Event();
	private final LinuxJoystickButton[] buttons;
	private final LinuxJoystickAxis[] axes;

	private EventQueue event_queue;

	/* Closed state variable that protects the validity of the file descriptor.
	 *  Access to the closed state must be synchronized
	 */
	private boolean closed;

    public LinuxJoystickDevice(String filename) throws IOException {
		this.fd = nOpen(filename);
		try {
			this.name = getDeviceName();
			setBufferSize(AbstractController.EVENT_QUEUE_DEPTH);
			buttons = new LinuxJoystickButton[getNumDeviceButtons()];
			axes = new LinuxJoystickAxis[getNumDeviceAxes()];
		} catch (IOException e) {
			close();
			throw e;
		}
    }
	private final static native long nOpen(String filename) throws IOException;

	public final synchronized void setBufferSize(int size) {
		event_queue = new EventQueue(size);
	}

	private final void processEvent(LinuxJoystickEvent joystick_event) {
		int index = joystick_event.getNumber();
		// Filter synthetic init event flag
		int type = joystick_event.getType() & ~JS_EVENT_INIT;
		switch (type) {
			case JS_EVENT_BUTTON:
				if (index < getNumButtons()) {
					LinuxJoystickButton button = buttons[index];
					if (button != null) {
						float value = joystick_event.getValue();
						button.setValue(value);
						event.set(button, value, joystick_event.getNanos());
						break;
					}
				}
				return;
			case JS_EVENT_AXIS:
				if (index < getNumAxes()) {
					LinuxJoystickAxis axis = axes[index];
					if (axis != null) {
						float value = (float)joystick_event.getValue()/AXIS_MAX_VALUE;
						axis.setValue(value);
						event.set(axis, value, joystick_event.getNanos());
						break;
					}
				}
				return;
			default:
				// Unknown component type
				return;
		}
		if (!event_queue.isFull()) {
			event_queue.add(event);
		}
	}

	public final void registerAxis(int index, LinuxJoystickAxis axis) {
		axes[index] = axis;
	}

	public final void registerButton(int index, LinuxJoystickButton button) {
		buttons[index] = button;
	}

	public final synchronized boolean getNextEvent(Event event) throws IOException {
		return event_queue.getNextEvent(event);
	}

	public final synchronized void poll() throws IOException {
		checkClosed();
		while (getNextDeviceEvent(joystick_event)) {
			processEvent(joystick_event);
		}
	}

	private final boolean getNextDeviceEvent(LinuxJoystickEvent joystick_event) throws IOException {
		return nGetNextEvent(fd, joystick_event);
	}
	private final static native boolean nGetNextEvent(long fd, LinuxJoystickEvent joystick_event) throws IOException;

	public final int getNumAxes() {
		return axes.length;
	}

	public final int getNumButtons() {
		return buttons.length;
	}

	private final int getNumDeviceButtons() throws IOException {
		return nGetNumButtons(fd);
	}
	private final static native int nGetNumButtons(long fd) throws IOException;

	private final int getNumDeviceAxes() throws IOException {
		return nGetNumAxes(fd);
	}
	private final static native int nGetNumAxes(long fd) throws IOException;

	private final int getVersion() throws IOException {
		return nGetVersion(fd);
	}
	private final static native int nGetVersion(long fd) throws IOException;

	public final String getName() {
		return name;
	}
	
	private final String getDeviceName() throws IOException {
		return nGetName(fd);
	}
	private final static native String nGetName(long fd) throws IOException;

	public final synchronized void close() throws IOException {
		if (!closed) {
			closed = true;
			nClose(fd);
		}
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

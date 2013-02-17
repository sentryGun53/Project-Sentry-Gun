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

/** Java wrapper of RAWDEVICELIST
 * @author elias
 * @version 1.0
 */
final class RawDevice {
	public final static int RI_MOUSE_LEFT_BUTTON_DOWN   = 0x0001;  // Left Button changed to down.
	public final static int RI_MOUSE_LEFT_BUTTON_UP	 = 0x0002;  // Left Button changed to up.
	public final static int RI_MOUSE_RIGHT_BUTTON_DOWN  = 0x0004;  // Right Button changed to down.
	public final static int RI_MOUSE_RIGHT_BUTTON_UP	= 0x0008;  // Right Button changed to up.
	public final static int RI_MOUSE_MIDDLE_BUTTON_DOWN = 0x0010;  // Middle Button changed to down.
	public final static int RI_MOUSE_MIDDLE_BUTTON_UP   = 0x0020;  // Middle Button changed to up.

	public final static int RI_MOUSE_BUTTON_1_DOWN	  = RI_MOUSE_LEFT_BUTTON_DOWN;
	public final static int RI_MOUSE_BUTTON_1_UP		= RI_MOUSE_LEFT_BUTTON_UP;
	public final static int RI_MOUSE_BUTTON_2_DOWN	  = RI_MOUSE_RIGHT_BUTTON_DOWN;
	public final static int RI_MOUSE_BUTTON_2_UP		= RI_MOUSE_RIGHT_BUTTON_UP;
	public final static int RI_MOUSE_BUTTON_3_DOWN	  = RI_MOUSE_MIDDLE_BUTTON_DOWN;
	public final static int RI_MOUSE_BUTTON_3_UP		= RI_MOUSE_MIDDLE_BUTTON_UP;

	public final static int RI_MOUSE_BUTTON_4_DOWN	  = 0x0040;
	public final static int RI_MOUSE_BUTTON_4_UP		= 0x0080;
	public final static int RI_MOUSE_BUTTON_5_DOWN	  = 0x0100;
	public final static int RI_MOUSE_BUTTON_5_UP		= 0x0200;

	/*
	 * If usButtonFlags has RI_MOUSE_WHEEL, the wheel delta is stored in usButtonData.
	 * Take it as a signed value.
	 */
	public final static int RI_MOUSE_WHEEL			  = 0x0400;

	public final static int MOUSE_MOVE_RELATIVE		 = 0;
	public final static int MOUSE_MOVE_ABSOLUTE		 = 1;
	public final static int MOUSE_VIRTUAL_DESKTOP	= 0x02;  // the coordinates are mapped to the virtual desktop
	public final static int MOUSE_ATTRIBUTES_CHANGED = 0x04;  // requery for mouse attributes

	public final static int RIM_TYPEHID = 2;
	public final static int RIM_TYPEKEYBOARD = 1;
	public final static int RIM_TYPEMOUSE = 0;

	public final static int WM_KEYDOWN					  = 0x0100;
	public final static int WM_KEYUP						= 0x0101;
	public final static int WM_SYSKEYDOWN				   = 0x0104;
	public final static int WM_SYSKEYUP					 = 0x0105;

	private final RawInputEventQueue queue;
	private final long handle;
	private final int type;

	/* Events from the event queue thread end here */
	private DataQueue keyboard_events;
	private DataQueue mouse_events;

	/* After processing in poll*(), the events are placed here */
	private DataQueue processed_keyboard_events;
	private DataQueue processed_mouse_events;

	/* mouse state */
	private final boolean[] button_states = new boolean[5];
	private int wheel;
	private int relative_x;
	private int relative_y;
	private int last_x;
	private int last_y;

	// Last x, y for converting absolute events to relative
	private int event_relative_x;
	private int event_relative_y;
	private int event_last_x;
	private int event_last_y;

	/* keyboard state */
	private final boolean[] key_states = new boolean[0xFF];

	public RawDevice(RawInputEventQueue queue, long handle, int type) {
		this.queue = queue;
		this.handle = handle;
		this.type = type;
		setBufferSize(AbstractController.EVENT_QUEUE_DEPTH);
	}

	/* Careful, this is called from the event queue thread */
	public final synchronized void addMouseEvent(long millis, int flags, int button_flags, int button_data, long raw_buttons, long last_x, long last_y, long extra_information) {
		if (mouse_events.hasRemaining()) {
			RawMouseEvent event = (RawMouseEvent)mouse_events.get();
			event.set(millis, flags, button_flags, button_data, raw_buttons,  last_x, last_y, extra_information);
		}
	}

	/* Careful, this is called from the event queue thread */
	public final synchronized void addKeyboardEvent(long millis, int make_code, int flags, int vkey, int message, long extra_information) {
		if (keyboard_events.hasRemaining()) {
			RawKeyboardEvent event = (RawKeyboardEvent)keyboard_events.get();
			event.set(millis, make_code, flags, vkey, message, extra_information);
		}
	}

	public final synchronized void pollMouse() {
		relative_x = relative_y = wheel = 0;
		mouse_events.flip();
		while (mouse_events.hasRemaining()) {
			RawMouseEvent event = (RawMouseEvent)mouse_events.get();
			boolean has_update = processMouseEvent(event);
			if (has_update && processed_mouse_events.hasRemaining()) {
				RawMouseEvent processed_event = (RawMouseEvent)processed_mouse_events.get();
				processed_event.set(event);
			}
		}
		mouse_events.compact();
	}

	public final synchronized void pollKeyboard() {
		keyboard_events.flip();
		while (keyboard_events.hasRemaining()) {
			RawKeyboardEvent event = (RawKeyboardEvent)keyboard_events.get();
			boolean has_update = processKeyboardEvent(event);
			if (has_update && processed_keyboard_events.hasRemaining()) {
				RawKeyboardEvent processed_event = (RawKeyboardEvent)processed_keyboard_events.get();
				processed_event.set(event);
			}
		}
		keyboard_events.compact();
	}

	private final boolean updateButtonState(int button_id, int button_flags, int down_flag, int up_flag) {
		if (button_id >= button_states.length)
			return false;
		if ((button_flags & down_flag) != 0) {
			button_states[button_id] = true;
			return true;
		} else if ((button_flags & up_flag) != 0) {
			button_states[button_id] = false;
			return true;
		} else
			return false;
	}
	
	private final boolean processKeyboardEvent(RawKeyboardEvent event) {
		int message = event.getMessage();
		int vkey = event.getVKey();
		if (vkey >= key_states.length)
			return false;
		if (message == WM_KEYDOWN || message == WM_SYSKEYDOWN) {
			key_states[vkey] = true;
			return true;
		} else if (message == WM_KEYUP || message == WM_SYSKEYUP) {
			key_states[vkey] = false;
			return true;
		} else
			return false;
	}

	public final boolean isKeyDown(int vkey) {
		return key_states[vkey];
	}

	private final boolean processMouseEvent(RawMouseEvent event) {
		boolean has_update = false;
		int button_flags = event.getButtonFlags();
		has_update = updateButtonState(0, button_flags, RI_MOUSE_BUTTON_1_DOWN, RI_MOUSE_BUTTON_1_UP) || has_update;
		has_update = updateButtonState(1, button_flags, RI_MOUSE_BUTTON_2_DOWN, RI_MOUSE_BUTTON_2_UP) || has_update;
		has_update = updateButtonState(2, button_flags, RI_MOUSE_BUTTON_3_DOWN, RI_MOUSE_BUTTON_3_UP) || has_update;
		has_update = updateButtonState(3, button_flags, RI_MOUSE_BUTTON_4_DOWN, RI_MOUSE_BUTTON_4_UP) || has_update;
		has_update = updateButtonState(4, button_flags, RI_MOUSE_BUTTON_5_DOWN, RI_MOUSE_BUTTON_5_UP) || has_update;
		int dx;
		int dy;
		if ((event.getFlags() & MOUSE_MOVE_ABSOLUTE) != 0) {
			dx = event.getLastX() - last_x;
			dy = event.getLastY() - last_y;
			last_x = event.getLastX();
			last_y = event.getLastY();
		} else {
			dx = event.getLastX();
			dy = event.getLastY();
		}
		int dwheel = 0;
		if ((button_flags & RI_MOUSE_WHEEL) != 0)
			dwheel = event.getWheelDelta();
		relative_x += dx;
		relative_y += dy;
		wheel += dwheel;
		has_update = dx != 0 || dy != 0 || dwheel != 0 || has_update;
		return has_update;
	}

	public final int getWheel() {
		return wheel;
	}

	public final int getEventRelativeX() {
		return event_relative_x;
	}
	
	public final int getEventRelativeY() {
		return event_relative_y;
	}
	
	public final int getRelativeX() {
		return relative_x;
	}
	
	public final int getRelativeY() {
		return relative_y;
	}
	
	public final synchronized boolean getNextKeyboardEvent(RawKeyboardEvent event) {
		processed_keyboard_events.flip();
		if (!processed_keyboard_events.hasRemaining()) {
			processed_keyboard_events.compact();
			return false;
		}
		RawKeyboardEvent next_event = (RawKeyboardEvent)processed_keyboard_events.get();
		event.set(next_event);
		processed_keyboard_events.compact();
		return true;
	}

	public final synchronized boolean getNextMouseEvent(RawMouseEvent event) {
		processed_mouse_events.flip();
		if (!processed_mouse_events.hasRemaining()) {
			processed_mouse_events.compact();
			return false;
		}
		RawMouseEvent next_event = (RawMouseEvent)processed_mouse_events.get();
		if ((next_event.getFlags() & MOUSE_MOVE_ABSOLUTE) != 0) {
			event_relative_x = next_event.getLastX() - event_last_x;
			event_relative_y = next_event.getLastY() - event_last_y;
			event_last_x = next_event.getLastX();
			event_last_y = next_event.getLastY();
		} else {
			event_relative_x = next_event.getLastX();
			event_relative_y = next_event.getLastY();
		}
		event.set(next_event);
		processed_mouse_events.compact();
		return true;
	}
	
	public final boolean getButtonState(int button_id) {
		if (button_id >= button_states.length)
			return false;
		return button_states[button_id];
	}

	public final void setBufferSize(int size) {
		keyboard_events = new DataQueue(size, RawKeyboardEvent.class);
		mouse_events = new DataQueue(size, RawMouseEvent.class);
		processed_keyboard_events = new DataQueue(size, RawKeyboardEvent.class);
		processed_mouse_events = new DataQueue(size, RawMouseEvent.class);
	}

	public final int getType() {
		return type;
	}

	public final long getHandle() {
		return handle;
	}

	public final String getName() throws IOException {
		return nGetName(handle);
	}
	private final static native String nGetName(long handle) throws IOException;

	public final RawDeviceInfo getInfo() throws IOException {
		return nGetInfo(this, handle);
	}
	private final static native RawDeviceInfo nGetInfo(RawDevice device, long handle) throws IOException;
}

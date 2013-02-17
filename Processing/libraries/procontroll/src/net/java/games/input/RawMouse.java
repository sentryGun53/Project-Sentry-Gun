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

/**
* @author elias
* @version 1.0
*/
final class RawMouse extends Mouse {
	/* Because one raw event can contain multiple
	 * changes, we'll make a simple state machine
	 * to keep track of which change to  report next
	 */
	
	// Another event should be read
	private final static int EVENT_DONE = 1;
	// The X axis should be reported next
	private final static int EVENT_X = 2;
	// The Y axis should be reported next
	private final static int EVENT_Y = 3;
	// The Z axis should be reported next
	private final static int EVENT_Z = 4;
	// Button 0 should be reported next
	private final static int EVENT_BUTTON_0 = 5;
	// Button 1 should be reported next
	private final static int EVENT_BUTTON_1 = 6;
	// Button 2 should be reported next
	private final static int EVENT_BUTTON_2 = 7;
	// Button 3 should be reported next
	private final static int EVENT_BUTTON_3 = 8;
	// Button 4 should be reported next
	private final static int EVENT_BUTTON_4 = 9;

	private final RawDevice device;

	private final RawMouseEvent current_event = new RawMouseEvent();
	private int event_state = EVENT_DONE;
	
	protected RawMouse(String name, RawDevice device, Component[] components, Controller[] children, Rumbler[] rumblers) throws IOException {
		super(name, components, children, rumblers);
		this.device = device;
	}

	public final void pollDevice() throws IOException {
		device.pollMouse();
	}

	private final static boolean makeButtonEvent(RawMouseEvent mouse_event, Event event, Component button_component, int down_flag, int up_flag) {
		if ((mouse_event.getButtonFlags() & down_flag) != 0) {
			event.set(button_component, 1, mouse_event.getNanos());
			return true;
		} else if ((mouse_event.getButtonFlags() & up_flag) != 0) {
			event.set(button_component, 0, mouse_event.getNanos());
			return true;
		} else
			return false;
	}

	protected final synchronized boolean getNextDeviceEvent(Event event) throws IOException {
		while (true) {
			switch (event_state) {
				case EVENT_DONE:
					if (!device.getNextMouseEvent(current_event))
						return false;
					event_state = EVENT_X;
					break;
				case EVENT_X:
					int rel_x = device.getEventRelativeX();
					event_state = EVENT_Y;
					if (rel_x != 0) {
						event.set(getX(), rel_x, current_event.getNanos());
						return true;
					}
					break;
				case EVENT_Y:
					int rel_y = device.getEventRelativeY();
					event_state = EVENT_Z;
					if (rel_y != 0) {
						event.set(getY(), rel_y, current_event.getNanos());
						return true;
					}
					break;
				case EVENT_Z:
					int wheel = current_event.getWheelDelta();
					event_state = EVENT_BUTTON_0;
					if (wheel != 0) {
						event.set(getWheel(), wheel, current_event.getNanos());
						return true;
					}
					break;
				case EVENT_BUTTON_0:
					event_state = EVENT_BUTTON_1;
					if (makeButtonEvent(current_event, event, getLeft(), RawDevice.RI_MOUSE_BUTTON_1_DOWN, RawDevice.RI_MOUSE_BUTTON_1_UP))
						return true;
					break;
				case EVENT_BUTTON_1:
					event_state = EVENT_BUTTON_2;
					if (makeButtonEvent(current_event, event, getRight(), RawDevice.RI_MOUSE_BUTTON_2_DOWN, RawDevice.RI_MOUSE_BUTTON_2_UP))
						return true;
					break;
				case EVENT_BUTTON_2:
					event_state = EVENT_BUTTON_3;
					if (makeButtonEvent(current_event, event, getMiddle(), RawDevice.RI_MOUSE_BUTTON_3_DOWN, RawDevice.RI_MOUSE_BUTTON_3_UP))
						return true;
					break;
				case EVENT_BUTTON_3:
					event_state = EVENT_BUTTON_4;
					if (makeButtonEvent(current_event, event, getSide(), RawDevice.RI_MOUSE_BUTTON_4_DOWN, RawDevice.RI_MOUSE_BUTTON_4_UP))
						return true;
					break;
				case EVENT_BUTTON_4:
					event_state = EVENT_DONE;
					if (makeButtonEvent(current_event, event, getExtra(), RawDevice.RI_MOUSE_BUTTON_5_DOWN, RawDevice.RI_MOUSE_BUTTON_5_UP))
						return true;
					break;
				default:
					throw new RuntimeException("Unknown event state: " + event_state);
			}
		}
	}
	
	protected final void setDeviceEventQueueSize(int size) throws IOException {
		device.setBufferSize(size);
	}

	final static class Axis extends AbstractComponent {
		private final RawDevice device;
		
		public Axis(RawDevice device, Component.Identifier.Axis axis) {
			super(axis.getName(), axis);
			this.device = device;
		}

		public final boolean isRelative() {
			return true;
		}

		public final boolean isAnalog() {
			return true;
		}

		protected final float poll() throws IOException {
			if (getIdentifier() == Component.Identifier.Axis.X) {
				return device.getRelativeX();
			} else if (getIdentifier() == Component.Identifier.Axis.Y) {
				return device.getRelativeY();
			} else if (getIdentifier() == Component.Identifier.Axis.Z) {
				return device.getWheel();
			} else
				throw new RuntimeException("Unknown raw axis: " + getIdentifier());
		}
	}
	
	final static class Button extends AbstractComponent {
		private final RawDevice device;
		private final int button_id;

		public Button(RawDevice device, Component.Identifier.Button id, int button_id) {
			super(id.getName(), id);
			this.device = device;
			this.button_id = button_id;
		}

		protected final float poll() throws IOException {
			return device.getButtonState(button_id) ? 1 : 0;
		}

		public final boolean isAnalog() {
			return false;
		}

		public final boolean isRelative() {
			return false;
		}
	}
}

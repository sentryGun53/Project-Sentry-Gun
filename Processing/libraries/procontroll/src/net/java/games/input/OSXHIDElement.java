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

/** Represents an OSX HID Element
* @author elias
* @author gregorypierce
* @version 1.0
*/
final class OSXHIDElement {
	private final OSXHIDDevice device;
	private final UsagePair usage_pair;
	private final long element_cookie;
	private final ElementType element_type;
	private final int min;
	private final int max;
	private final Component.Identifier identifier;
	private final boolean is_relative;
	
	public OSXHIDElement(OSXHIDDevice device, UsagePair usage_pair, long element_cookie, ElementType element_type, int min, int max, boolean is_relative) {
		this.device = device;
		this.usage_pair = usage_pair;
		this.element_cookie = element_cookie;
		this.element_type = element_type;
		this.min = min;
		this.max = max;
		this.identifier = computeIdentifier();
		this.is_relative = is_relative;
	}

	private final Component.Identifier computeIdentifier() {
		if (usage_pair.getUsagePage() == UsagePage.GENERIC_DESKTOP) {
			return ((GenericDesktopUsage)usage_pair.getUsage()).getIdentifier();
		} else if (usage_pair.getUsagePage() == UsagePage.BUTTON) { 
			return ((ButtonUsage)usage_pair.getUsage()).getIdentifier();
		} else if (usage_pair.getUsagePage() == UsagePage.KEYBOARD_OR_KEYPAD) {
			return ((KeyboardUsage)usage_pair.getUsage()).getIdentifier();
		} else
			return null;
	}

	final Component.Identifier getIdentifier() {
		return identifier;
	}

	final long getCookie() {
		return element_cookie;
	}

	final ElementType getType() {
		return element_type;
	}

	final boolean isRelative() {
		return is_relative && identifier instanceof Component.Identifier.Axis;
	}

	final boolean isAnalog() {
		return identifier instanceof Component.Identifier.Axis && identifier != Component.Identifier.Axis.POV;
	}

	private UsagePair getUsagePair() {
		return usage_pair;
	}
	
	final void getElementValue(OSXEvent event) throws IOException {
		device.getElementValue(element_cookie, event);
	}

	final float convertValue(float value) {
		if (identifier == Component.Identifier.Axis.POV) {
				switch ((int)value) {
					case 0:
						return Component.POV.UP;
					case 1:
						return Component.POV.UP_RIGHT;
					case 2:
						return Component.POV.RIGHT;
					case 3:
						return Component.POV.DOWN_RIGHT;
					case 4:
						return Component.POV.DOWN;
					case 5:
						return Component.POV.DOWN_LEFT;
					case 6:
						return Component.POV.LEFT;
					case 7:
						return Component.POV.UP_LEFT;
					case 8:
						return Component.POV.OFF;
					default:
						return Component.POV.OFF;
				}
		} else if (identifier instanceof Component.Identifier.Axis && !is_relative) {
			if (min == max)
				return 0;
			else if (value > max)
				value = max;
			else if (value < min)
				value = min;
			return 2*(value - min)/(max - min) - 1;
		} else
			return value;
	}
}

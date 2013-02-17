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

/** Generic Desktop Usages
* @author elias
* @version 1.0
*/
final class GenericDesktopUsage implements Usage {
	private final static GenericDesktopUsage[] map = new GenericDesktopUsage[0xFF];

	public final static GenericDesktopUsage POINTER                            = new GenericDesktopUsage(0x01); /* Physical Collection */
	public final static GenericDesktopUsage MOUSE                          = new GenericDesktopUsage(0x02); /* Application Collection */
	/* 0x03 Reserved */
	public final static GenericDesktopUsage JOYSTICK                           = new GenericDesktopUsage(0x04); /* Application Collection */
	public final static GenericDesktopUsage GAME_PAD                            = new GenericDesktopUsage(0x05); /* Application Collection */
	public final static GenericDesktopUsage KEYBOARD                           = new GenericDesktopUsage(0x06); /* Application Collection */
	public final static GenericDesktopUsage KEYPAD                         = new GenericDesktopUsage(0x07); /* Application Collection */
	public final static GenericDesktopUsage MULTI_AXIS_CONTROLLER                            = new GenericDesktopUsage(0x08); /* Application Collection */
	/* 0x09 - 0x2F Reserved */
	public final static GenericDesktopUsage X                          = new GenericDesktopUsage(0x30); /* Dynamic Value */
	public final static GenericDesktopUsage Y                          = new GenericDesktopUsage(0x31); /* Dynamic Value */
	public final static GenericDesktopUsage Z                          = new GenericDesktopUsage(0x32); /* Dynamic Value */
	public final static GenericDesktopUsage RX                         = new GenericDesktopUsage(0x33); /* Dynamic Value */
	public final static GenericDesktopUsage RY                         = new GenericDesktopUsage(0x34); /* Dynamic Value */
	public final static GenericDesktopUsage RZ                         = new GenericDesktopUsage(0x35); /* Dynamic Value */
	public final static GenericDesktopUsage SLIDER                         = new GenericDesktopUsage(0x36); /* Dynamic Value */
	public final static GenericDesktopUsage DIAL                           = new GenericDesktopUsage(0x37); /* Dynamic Value */
	public final static GenericDesktopUsage WHEEL                          = new GenericDesktopUsage(0x38); /* Dynamic Value */
	public final static GenericDesktopUsage HATSWITCH                          = new GenericDesktopUsage(0x39); /* Dynamic Value */
	public final static GenericDesktopUsage COUNTED_BUFFER                          = new GenericDesktopUsage(0x3A); /* Logical Collection */
	public final static GenericDesktopUsage BYTE_COUNT                          = new GenericDesktopUsage(0x3B); /* Dynamic Value */
	public final static GenericDesktopUsage MOTION_WAKEUP                           = new GenericDesktopUsage(0x3C); /* One-Shot Control */
	public final static GenericDesktopUsage START                          = new GenericDesktopUsage(0x3D); /* On/Off Control */
	public final static GenericDesktopUsage SELECT                         = new GenericDesktopUsage(0x3E); /* On/Off Control */
	/* 0x3F Reserved */
	public final static GenericDesktopUsage VX                         = new GenericDesktopUsage(0x40); /* Dynamic Value */
	public final static GenericDesktopUsage VY                         = new GenericDesktopUsage(0x41); /* Dynamic Value */
	public final static GenericDesktopUsage VZ                         = new GenericDesktopUsage(0x42); /* Dynamic Value */
	public final static GenericDesktopUsage VBRX                           = new GenericDesktopUsage(0x43); /* Dynamic Value */
	public final static GenericDesktopUsage VBRY                           = new GenericDesktopUsage(0x44); /* Dynamic Value */
	public final static GenericDesktopUsage VBRZ                           = new GenericDesktopUsage(0x45); /* Dynamic Value */
	public final static GenericDesktopUsage VNO                            = new GenericDesktopUsage(0x46); /* Dynamic Value */
	/* 0x47 - 0x7F Reserved */
	public final static GenericDesktopUsage SYSTEM_CONTROL                          = new GenericDesktopUsage(0x80); /* Application Collection */
	public final static GenericDesktopUsage SYSTEM_POWER_DOWN                            = new GenericDesktopUsage(0x81); /* One-Shot Control */
	public final static GenericDesktopUsage SYSTEM_SLEEP                            = new GenericDesktopUsage(0x82); /* One-Shot Control */
	public final static GenericDesktopUsage SYSTEM_WAKE_UP                           = new GenericDesktopUsage(0x83); /* One-Shot Control */
	public final static GenericDesktopUsage SYSTEM_CONTEXT_MENU                          = new GenericDesktopUsage(0x84); /* One-Shot Control */
	public final static GenericDesktopUsage SYSTEM_MAIN_MENU                         = new GenericDesktopUsage(0x85); /* One-Shot Control */
	public final static GenericDesktopUsage SYSTEM_APP_MENU                          = new GenericDesktopUsage(0x86); /* One-Shot Control */
	public final static GenericDesktopUsage SYSTEM_MENU_HELP                         = new GenericDesktopUsage(0x87); /* One-Shot Control */
	public final static GenericDesktopUsage SYSTEM_MENU_EXIT                         = new GenericDesktopUsage(0x88); /* One-Shot Control */
	public final static GenericDesktopUsage SYSTEM_MENU                         = new GenericDesktopUsage(0x89); /* Selector */
	public final static GenericDesktopUsage SYSTEM_MENU_RIGHT                            = new GenericDesktopUsage(0x8A); /* Re-Trigger Control */
	public final static GenericDesktopUsage SYSTEM_MENU_LEFT                         = new GenericDesktopUsage(0x8B); /* Re-Trigger Control */
	public final static GenericDesktopUsage SYSTEM_MENU_UP                           = new GenericDesktopUsage(0x8C); /* Re-Trigger Control */
	public final static GenericDesktopUsage SYSTEM_MENU_DOWN                         = new GenericDesktopUsage(0x8D); /* Re-Trigger Control */
	/* 0x8E - 0x8F Reserved */
	public final static GenericDesktopUsage DPAD_UP                         = new GenericDesktopUsage(0x90); /* On/Off Control */
	public final static GenericDesktopUsage DPAD_DOWN                           = new GenericDesktopUsage(0x91); /* On/Off Control */
	public final static GenericDesktopUsage DPAD_RIGHT                          = new GenericDesktopUsage(0x92); /* On/Off Control */
	public final static GenericDesktopUsage DPAD_LEFT                           = new GenericDesktopUsage(0x93); /* On/Off Control */
	/* 0x94 - 0xFFFF Reserved */

	private final int usage_id;
	
	public final static GenericDesktopUsage map(int usage_id) {
		if (usage_id < 0 || usage_id >= map.length)
			return null;
		return map[usage_id];
	}

	private GenericDesktopUsage(int usage_id) {
		map[usage_id] = this;
		this.usage_id = usage_id;
	}

	public final String toString() {
		return "GenericDesktopUsage (0x" + Integer.toHexString(usage_id) + ")";
	}

	public final Component.Identifier getIdentifier() {
		if (this == GenericDesktopUsage.X) {
			return Component.Identifier.Axis.X;
		} else if (this == GenericDesktopUsage.Y) {
			return Component.Identifier.Axis.Y;
		} else if (this == GenericDesktopUsage.Z) {
			return Component.Identifier.Axis.Z;
		} else if (this == GenericDesktopUsage.RX) {
			return Component.Identifier.Axis.RX;
		} else if (this == GenericDesktopUsage.RY) {
			return Component.Identifier.Axis.RY;
		} else if (this == GenericDesktopUsage.RZ) {
			return Component.Identifier.Axis.RZ;
		} else if (this == GenericDesktopUsage.SLIDER) {
			return Component.Identifier.Axis.SLIDER;
		} else if (this == GenericDesktopUsage.HATSWITCH) {
			return Component.Identifier.Axis.POV;
		} else if (this == GenericDesktopUsage.SELECT) {
			return Component.Identifier.Button.SELECT;
		} else
			return null;
	}

}

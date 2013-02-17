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

import java.lang.reflect.Method;

/** HID Usage pages
* @author elias
* @version 1.0
*/
final class UsagePage {
	private final static UsagePage[] map = new UsagePage[0xFF];

	public final static UsagePage UNDEFINED  = new UsagePage(0x00); 
	public final static UsagePage GENERIC_DESKTOP = new UsagePage(0x01, GenericDesktopUsage.class); 
	public final static UsagePage SIMULATION = new UsagePage(0x02); 
	public final static UsagePage VR = new UsagePage(0x03);
	public final static UsagePage SPORT  = new UsagePage(0x04);
	public final static UsagePage GAME   = new UsagePage(0x05);
	/* Reserved 0x06 */ 
	public final static UsagePage KEYBOARD_OR_KEYPAD   = new UsagePage(0x07, KeyboardUsage.class); /* USB Device Class Definition for Human Interface Devices (HID). Note: the usage type for all key codes is Selector (Sel). */
	public final static UsagePage LEDS   = new UsagePage(0x08); 
	public final static UsagePage BUTTON = new UsagePage(0x09, ButtonUsage.class); 
	public final static UsagePage ORDINAL    = new UsagePage(0x0A);
	public final static UsagePage TELEPHONY  = new UsagePage(0x0B); 
	public final static UsagePage CONSUMER   = new UsagePage(0x0C); 
	public final static UsagePage DIGITIZER  = new UsagePage(0x0D); 
	/* Reserved 0x0E */ 
	public final static UsagePage PID    = new UsagePage(0x0F); /* USB Physical Interface Device definitions for force feedback and related devices. */
	public final static UsagePage UNICODE    = new UsagePage(0x10); 
	/* Reserved 0x11 - 0x13 */
	public final static UsagePage ALPHANUMERIC_DISPLAY    = new UsagePage(0x14);
	/* Reserved 0x15 - 0x7F */  
	/* Monitor 0x80 - 0x83   USB Device Class Definition for Monitor Devices */
	/* Power 0x84 - 0x87     USB Device Class Definition for Power Devices */
	public final static UsagePage POWER_DEVICE = new UsagePage(0x84);                /* Power Device Page */
	public final static UsagePage BATTERY_SYSTEM = new UsagePage(0x85);              /* Battery System Page */
	/* Reserved 0x88 - 0x8B */
	public final static UsagePage BAR_CODE_SCANNER = new UsagePage(0x8C); /* (Point of Sale) USB Device Class Definition for Bar Code Scanner Devices */
	public final static UsagePage SCALE  = new UsagePage(0x8D); /* (Point of Sale) USB Device Class Definition for Scale Devices */
	/* ReservedPointofSalepages 0x8E - 0X8F */
	public final static UsagePage CAMERACONTROL= new UsagePage(0x90); /* USB Device Class Definition for Image Class Devices */
	public final static UsagePage ARCADE = new UsagePage(0x91); /* OAAF Definitions for arcade and coinop related Devices */                                                                                                                         
	private final Class usage_class;
	private final int usage_page_id;

	public final static UsagePage map(int page_id) {
		if (page_id < 0 || page_id >= map.length)
			return null;
		return map[page_id];
	}

	private UsagePage(int page_id, Class usage_class) {
		map[page_id] = this;
		this.usage_class = usage_class;
		this.usage_page_id = page_id;
	}

	private UsagePage(int page_id) {
		this(page_id, null);
	}
	
	public final String toString() {
		return "UsagePage (0x" + Integer.toHexString(usage_page_id) + ")";
	}

	public final Usage mapUsage(int usage_id) {
		if (usage_class == null)
			return null;
		try {
			Method map_method = usage_class.getMethod("map", new Class[]{int.class});
			Object result = map_method.invoke(null, new Object[]{new Integer(usage_id)});
			return (Usage)result;
		} catch (Exception e) {
			throw new Error(e);
		}
	}
}

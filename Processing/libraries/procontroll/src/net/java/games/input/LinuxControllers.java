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

/** helper methods for Linux specific Controllers
* @author elias
* @version 1.0
*/
final class LinuxControllers {
	private final static LinuxEvent linux_event = new LinuxEvent();

	/* Declared synchronized to protect linux_event */
	public final static synchronized boolean getNextDeviceEvent(Event event, LinuxEventDevice device) throws IOException {
		while (device.getNextEvent(linux_event)) {
			LinuxAxisDescriptor descriptor = linux_event.getDescriptor();
			LinuxComponent component = device.mapDescriptor(descriptor);
			if (component != null) {
				float value = component.convertValue(linux_event.getValue(), descriptor);
				event.set(component, value, linux_event.getNanos());
				return true;
			}
		}
		return false;
	}

	private final static LinuxAbsInfo abs_info = new LinuxAbsInfo();

	/* Declared synchronized to protect abs_info */
	public final static synchronized float poll(LinuxEventComponent event_component) throws IOException {
		int native_type = event_component.getDescriptor().getType();
		switch (native_type) {
			case NativeDefinitions.EV_KEY:
				int native_code = event_component.getDescriptor().getCode();
				float state = event_component.getDevice().isKeySet(native_code) ? 1f : 0f;
				return state;
			case NativeDefinitions.EV_ABS:
				event_component.getAbsInfo(abs_info);
				return abs_info.getValue();
			default:
				throw new RuntimeException("Unkown native_type: " + native_type);
		}
	}
}

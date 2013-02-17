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
final class DIControllers {
	private final static DIDeviceObjectData di_event = new DIDeviceObjectData();

	/* synchronized to protect di_event */
	public final static synchronized boolean getNextDeviceEvent(Event event, IDirectInputDevice device) throws IOException {
		if (!device.getNextEvent(di_event))
			return false;
		DIDeviceObject object = device.mapEvent(di_event);
		DIComponent component = device.mapObject(object);
		if (component == null)
			return false;
		int event_value;
		if (object.isRelative()) {
			event_value = object.getRelativeEventValue(di_event.getData());
		} else {
			event_value = di_event.getData();
		}
		event.set(component, component.getDeviceObject().convertValue(event_value), di_event.getNanos());
		return true;
	}

	public final static float poll(Component component, DIDeviceObject object) throws IOException {
		int poll_data = object.getDevice().getPollData(object);
		float result;
		if (object.isRelative()) {
			result = object.getRelativePollValue(poll_data);
		} else {
			result = poll_data;
		}
		return object.convertValue(result);
	}
}

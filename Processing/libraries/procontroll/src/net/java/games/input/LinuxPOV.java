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

/** Represents a linux button
* @author elias
* @version 1.0
*/
final class LinuxPOV extends LinuxComponent {
	private final LinuxEventComponent component_x;
	private final LinuxEventComponent component_y;

	private float last_x;
	private float last_y;
	
	public LinuxPOV(LinuxEventComponent component_x, LinuxEventComponent component_y) {
		super(component_x);
		this.component_x = component_x;
		this.component_y = component_y;
	}

	protected final float poll() throws IOException {
		last_x = LinuxControllers.poll(component_x);
		last_y = LinuxControllers.poll(component_y);
		return convertValue(0f, null);
	}

	public float convertValue(float value, LinuxAxisDescriptor descriptor) {
		if (descriptor == component_x.getDescriptor())
			last_x = value;
		if (descriptor == component_y.getDescriptor())
			last_y = value;

		if (last_x == -1 && last_y == -1)
			return Component.POV.UP_LEFT;
		else if (last_x == -1 && last_y == 0)
			return Component.POV.LEFT;
		else if (last_x == -1 && last_y == 1)
			return Component.POV.DOWN_LEFT;
		else if (last_x == 0 && last_y == -1)
			return Component.POV.UP;
		else if (last_x == 0 && last_y == 0)
			return Component.POV.OFF;
		else if (last_x == 0 && last_y == 1)
			return Component.POV.DOWN;
		else if (last_x == 1 && last_y == -1)
			return Component.POV.UP_RIGHT;
		else if (last_x == 1 && last_y == 0)
			return Component.POV.RIGHT;
		else if (last_x == 1 && last_y == 1)
			return Component.POV.DOWN_RIGHT;
		else {
			ControllerEnvironment.logln("Unknown values x = " + last_x + " | y = " + last_y);
			return Component.POV.OFF;
		}
	}
}

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

/** Java wrapper of RAWMOUSE
 * @author elias
 * @version 1.0
 */
final class RawMouseEvent {
	/* It seems that raw input scales wheel
	 * the same way as direcinput
	 */
	private final static int WHEEL_SCALE = 120;
	
	private long millis;
	private int flags;
	private int button_flags;
	private int button_data;
	private long raw_buttons;
	private long last_x;
	private long last_y;
	private long extra_information;

	public final void set(long millis, int flags, int button_flags, int button_data, long raw_buttons, long last_x, long last_y, long extra_information) {
		this.millis = millis;
		this.flags = flags;
		this.button_flags = button_flags;
		this.button_data = button_data;
		this.raw_buttons = raw_buttons;
		this.last_x = last_x;
		this.last_y = last_y;
		this.extra_information = extra_information;
	}

	public final void set(RawMouseEvent event) {
		set(event.millis, event.flags, event.button_flags, event.button_data, event.raw_buttons, event.last_x, event.last_y, event.extra_information);
	}

	public final int getWheelDelta() {
		return button_data/WHEEL_SCALE;
	}

	private final int getButtonData() {
		return button_data;
	}

	public final int getFlags() {
		return flags;
	}

	public final int getButtonFlags() {
		return button_flags;
	}

	public final int getLastX() {
		return (int)last_x;
	}

	public final int getLastY() {
		return (int)last_y;
	}

	public final long getRawButtons() {
		return raw_buttons;
	}

	public final long getNanos() {
		return millis*1000000L;
	}
}

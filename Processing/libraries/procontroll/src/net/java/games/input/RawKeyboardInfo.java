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

/** Java wrapper of RID_DEVICE_INFO_KEYBOARD
 * @author elias
 * @version 1.0
 */
class RawKeyboardInfo extends RawDeviceInfo {
	private final RawDevice device;
	private final int type;
	private final int sub_type;
	private final int keyboard_mode;
	private final int num_function_keys;
	private final int num_indicators;
	private final int num_keys_total;

	public RawKeyboardInfo(RawDevice device, int type, int sub_type, int keyboard_mode, int num_function_keys, int num_indicators, int num_keys_total) {
		this.device = device;
		this.type = type;
		this.sub_type = sub_type;
		this.keyboard_mode = keyboard_mode;
		this.num_function_keys = num_function_keys;
		this.num_indicators = num_indicators;
		this.num_keys_total = num_keys_total;
	}

	public final int getUsage() {
		return 6;
	}

	public final int getUsagePage() {
		return 1;
	}

	public final long getHandle() {
		return device.getHandle();
	}

	public final Controller createControllerFromDevice(RawDevice device, SetupAPIDevice setupapi_device) throws IOException {
		return new RawKeyboard(setupapi_device.getName(), device, new Controller[]{}, new Rumbler[]{});
	}
}

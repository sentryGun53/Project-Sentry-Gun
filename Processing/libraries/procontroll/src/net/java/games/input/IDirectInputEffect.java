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

/** Java wrapper for IDirectInputEffect
 * @author elias
 * @version 1.0
 */
final class IDirectInputEffect implements Rumbler {
	private final long address;
	private final DIEffectInfo info;
	private boolean released;

	public IDirectInputEffect(long address, DIEffectInfo info) {
		this.address = address;
		this.info = info;
	}

	public final synchronized void rumble(float intensity) {
		try {
			checkReleased();
			if (intensity > 0) {
				int int_gain = (int)Math.round(intensity*IDirectInputDevice.DI_FFNOMINALMAX);
				setGain(int_gain);
				start(1, 0);
			} else
				stop();
		} catch (IOException e) {
			ControllerEnvironment.logln("Failed to set rumbler gain: " + e.getMessage());
		}
	}

	public final Component.Identifier getAxisIdentifier() {
		return null;
	}

	public final String getAxisName() {
		return null;
	}

	public final synchronized void release() {
		if (!released) {
			released = true;
			nRelease(address);
		}
	}
	private final static native void nRelease(long address);

	private final void checkReleased() throws IOException {
		if (released)
			throw new IOException();
	}

	private final void setGain(int gain) throws IOException {
		int res = nSetGain(address, gain);
		if (res != IDirectInputDevice.DI_DOWNLOADSKIPPED &&
			res != IDirectInputDevice.DI_EFFECTRESTARTED &&
			res != IDirectInputDevice.DI_OK &&
			res != IDirectInputDevice.DI_TRUNCATED &&
			res != IDirectInputDevice.DI_TRUNCATEDANDRESTARTED) {
			throw new IOException("Failed to set effect gain (0x" + Integer.toHexString(res) + ")");
		}
	}
	private final static native int nSetGain(long address, int gain);

	private final void start(int iterations, int flags) throws IOException {
		int res = nStart(address, iterations, flags);
		if (res != IDirectInputDevice.DI_OK)
			throw new IOException("Failed to start effect (0x" + Integer.toHexString(res) + ")");
	}
	private final static native int nStart(long address, int iterations, int flags);
	
	private final void stop() throws IOException {
		int res = nStop(address);
		if (res != IDirectInputDevice.DI_OK)
			throw new IOException("Failed to stop effect (0x" + Integer.toHexString(res) + ")");
	}
	private final static native int nStop(long address);

	protected void finalize() {
		release();
	}
}

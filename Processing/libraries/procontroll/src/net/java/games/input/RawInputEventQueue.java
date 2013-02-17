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
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;

/** Java wrapper of RAWDEVICELIST
 * @author elias
 * @version 1.0
 */
final class RawInputEventQueue {
	private final Object monitor = new Object();

	private List devices;
	
	public final void start(List devices) throws IOException {
		this.devices = devices;
		QueueThread queue = new QueueThread();
		synchronized (monitor) {
			queue.start();
			// wait for initialization
			while (!queue.isInitialized()) {
				try {
					monitor.wait();
				} catch (InterruptedException e) {}
			}
		}
		if (queue.getException() != null)
			throw queue.getException();
	}

	private final RawDevice lookupDevice(long handle) {
		for (int i = 0; i < devices.size(); i++) {
			RawDevice device = (RawDevice)devices.get(i);
			if (device.getHandle() == handle)
				return device;
		}
		return null;
	}

	/* Event methods called back from native code in nPoll() */
	private final void addMouseEvent(long handle, long millis, int flags, int button_flags, int button_data, long raw_buttons, long last_x, long last_y, long extra_information) {
		RawDevice device = lookupDevice(handle);
		if (device == null)
			return;
		device.addMouseEvent(millis, flags, button_flags, button_data, raw_buttons, last_x, last_y, extra_information);
	}

	private final void addKeyboardEvent(long handle, long millis, int make_code, int flags, int vkey, int message, long extra_information) {
		RawDevice device = lookupDevice(handle);
		if (device == null)
			return;
		device.addKeyboardEvent(millis, make_code, flags, vkey, message, extra_information);
	}

	private final void poll(DummyWindow window) throws IOException {
		nPoll(window.getHwnd());
	}
	private final native void nPoll(long hwnd_handle) throws IOException;

	private final static void registerDevices(DummyWindow window, RawDeviceInfo[] devices) throws IOException {
		nRegisterDevices(0, window.getHwnd(), devices);
	}
	private final static native void nRegisterDevices(int flags, long hwnd_addr, RawDeviceInfo[] devices) throws IOException;

	private final class QueueThread extends Thread {
		private boolean initialized;
		private DummyWindow window;
		private IOException exception;

		public QueueThread() {
			setDaemon(true);
		}

		public final boolean isInitialized() {
			return initialized;
		}

		public final IOException getException() {
			return exception;
		}

		public final void run() {
			// We have to create the window in the (private) queue thread
			try {
				window = new DummyWindow();
			} catch (IOException e) {
				exception = e;
			}
			initialized = true;
			synchronized (monitor) {
				monitor.notify();
			}
			if (exception != null)
				return;
			Set active_infos = new HashSet();
			try {
				for (int i = 0; i < devices.size(); i++) {
					RawDevice device = (RawDevice)devices.get(i);
					active_infos.add(device.getInfo());
				}
				RawDeviceInfo[] active_infos_array = new RawDeviceInfo[active_infos.size()];
				active_infos.toArray(active_infos_array);
				try {
					registerDevices(window, active_infos_array);
					while (!isInterrupted()) {
						poll(window);
					}
				} finally {
					window.destroy();
				}
			} catch (IOException e) {
				exception = e;
			}
		}
	}
}

/**
 * Copyright (C) 2003 Jeremy Booth (jeremy@newdawnsoftware.com)
 *
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this 
 * list of conditions and the following disclaimer. Redistributions in binary 
 * form must reproduce the above copyright notice, this list of conditions and 
 * the following disclaimer in the documentation and/or other materials provided
 * with the distribution. 
 * The name of the author may not be used to endorse or promote products derived
 * from this software without specific prior written permission. 
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF 
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO 
 * EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, 
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; 
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, 
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR 
 * OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF 
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE
 */
package net.java.games.input;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

/**
 * Linux doesn't have proper support for force feedback
 * from different threads since it relies on PIDs
 * to determine ownership of a particular effect slot.
 * Therefore we have to hack around this by
 * making sure everything related to FF
 * (including the final device close that performs
 *  and implicit deletion of all the process' effects)
 * is run on a single thread.
 */
final class LinuxDeviceThread extends Thread {
	private final List tasks = new ArrayList();
	
	public LinuxDeviceThread() {
		setDaemon(true);
		start();
	}

	public synchronized final void run() {
		while (true) {
			if (!tasks.isEmpty()) {
				LinuxDeviceTask task = (LinuxDeviceTask)tasks.remove(0);
				task.doExecute();
				synchronized (task) {
					task.notify();
				}
			} else {
				try {
					wait();
				} catch (InterruptedException e) {
					// ignore
				}
			}
		}
	}

	public final Object execute(LinuxDeviceTask task) throws IOException {
		synchronized (this) {
			tasks.add(task);
			notify();
		}
		synchronized (task) {
			while (task.getState() == LinuxDeviceTask.INPROGRESS) {
				try {
					task.wait();
				} catch (InterruptedException e) {
					// ignore
				}
			}
		}
		switch (task.getState()) {
			case LinuxDeviceTask.COMPLETED:
				return task.getResult();
			case LinuxDeviceTask.FAILED:
				throw task.getException();
			default:
				throw new RuntimeException("Invalid task state: " + task.getState());
		}
	}
}

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
import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;

/** Java wrapper for IDirectInputDevice
 * @author martak
 * @author elias
 * @version 1.0
 */
final class IDirectInputDevice {
	public final static int GUID_XAxis = 1;
	public final static int GUID_YAxis = 2;
	public final static int GUID_ZAxis = 3;
	public final static int GUID_RxAxis = 4;
	public final static int GUID_RyAxis = 5;
	public final static int GUID_RzAxis = 6;
	public final static int GUID_Slider = 7;
	public final static int GUID_Button = 8;
	public final static int GUID_Key = 9;
	public final static int GUID_POV = 10;
	public final static int GUID_Unknown = 11;

	public final static int GUID_ConstantForce = 12;
	public final static int GUID_RampForce = 13;
	public final static int GUID_Square = 14;
	public final static int GUID_Sine = 15;
	public final static int GUID_Triangle = 16;
	public final static int GUID_SawtoothUp = 17;
	public final static int GUID_SawtoothDown = 18;
	public final static int GUID_Spring = 19;
	public final static int GUID_Damper = 20;
	public final static int GUID_Inertia = 21;
	public final static int GUID_Friction = 22;
	public final static int GUID_CustomForce = 23;

	public final static int DI8DEVTYPE_DEVICE		   = 0x11;
	public final static int DI8DEVTYPE_MOUSE			= 0x12;
	public final static int DI8DEVTYPE_KEYBOARD		 = 0x13;
	public final static int DI8DEVTYPE_JOYSTICK		 = 0x14;
	public final static int DI8DEVTYPE_GAMEPAD		  = 0x15;
	public final static int DI8DEVTYPE_DRIVING		  = 0x16;
	public final static int DI8DEVTYPE_FLIGHT		   = 0x17;
	public final static int DI8DEVTYPE_1STPERSON		= 0x18;
	public final static int DI8DEVTYPE_DEVICECTRL	   = 0x19;
	public final static int DI8DEVTYPE_SCREENPOINTER	= 0x1A;
	public final static int DI8DEVTYPE_REMOTE		   = 0x1B;
	public final static int DI8DEVTYPE_SUPPLEMENTAL	 = 0x1C;

	public final static int DISCL_EXCLUSIVE		= 0x00000001;
	public final static int DISCL_NONEXCLUSIVE  = 0x00000002;
	public final static int DISCL_FOREGROUND	= 0x00000004;
	public final static int DISCL_BACKGROUND	= 0x00000008;
	public final static int DISCL_NOWINKEY		= 0x00000010;

	public final static int DIDFT_ALL		   = 0x00000000;

	public final static int DIDFT_RELAXIS	   = 0x00000001;
	public final static int DIDFT_ABSAXIS	   = 0x00000002;
	public final static int DIDFT_AXIS		  = 0x00000003;

	public final static int DIDFT_PSHBUTTON	 = 0x00000004;
	public final static int DIDFT_TGLBUTTON	 = 0x00000008;
	public final static int DIDFT_BUTTON		= 0x0000000C;

	public final static int DIDFT_POV		   = 0x00000010;
	public final static int DIDFT_COLLECTION	= 0x00000040;
	public final static int DIDFT_NODATA		= 0x00000080;

	public final static int DIDFT_FFACTUATOR		= 0x01000000;
	public final static int DIDFT_FFEFFECTTRIGGER   = 0x02000000;
	public final static int DIDFT_OUTPUT			= 0x10000000;
	public final static int DIDFT_VENDORDEFINED	 = 0x04000000;
	public final static int DIDFT_ALIAS			 = 0x08000000;
	public final static int DIDFT_OPTIONAL		  = 0x80000000;

	public final static int DIDFT_NOCOLLECTION	  = 0x00FFFF00;

	public final static int DIDF_ABSAXIS			= 0x00000001;
	public final static int DIDF_RELAXIS			= 0x00000002;

	public final static int DI_OK					= 0x00000000;
	public final static int DI_NOEFFECT				= 0x00000001;
	public final static int DI_PROPNOEFFECT			= 0x00000001;
	public final static int DI_POLLEDDEVICE			= 0x00000002;

	public final static int DI_DOWNLOADSKIPPED			  = 0x00000003;
	public final static int DI_EFFECTRESTARTED			  = 0x00000004;
	public final static int DI_TRUNCATED					= 0x00000008;
	public final static int DI_SETTINGSNOTSAVED			 = 0x0000000B;
	public final static int DI_TRUNCATEDANDRESTARTED		= 0x0000000C;

	public final static int DI_BUFFEROVERFLOW		= 0x00000001;
	public final static int DIERR_INPUTLOST			= 0x8007001E;
	public final static int DIERR_NOTACQUIRED		= 0x8007001C;
	public final static int DIERR_OTHERAPPHASPRIO	= 0x80070005;

	public final static int DIDOI_FFACTUATOR		= 0x00000001;
	public final static int DIDOI_FFEFFECTTRIGGER   = 0x00000002;
	public final static int DIDOI_POLLED			= 0x00008000;
	public final static int DIDOI_ASPECTPOSITION	= 0x00000100;
	public final static int DIDOI_ASPECTVELOCITY	= 0x00000200;
	public final static int DIDOI_ASPECTACCEL	   = 0x00000300;
	public final static int DIDOI_ASPECTFORCE	   = 0x00000400;
	public final static int DIDOI_ASPECTMASK		= 0x00000F00;
	public final static int DIDOI_GUIDISUSAGE	   = 0x00010000;

	public final static int DIEFT_ALL						 = 0x00000000;

	public final static int DIEFT_CONSTANTFORCE			= 0x00000001;
	public final static int DIEFT_RAMPFORCE				 = 0x00000002;
	public final static int DIEFT_PERIODIC				  = 0x00000003;
	public final static int DIEFT_CONDITION				 = 0x00000004;
	public final static int DIEFT_CUSTOMFORCE			  = 0x00000005;
	public final static int DIEFT_HARDWARE				  = 0x000000FF;
	public final static int DIEFT_FFATTACK				  = 0x00000200;
	public final static int DIEFT_FFFADE					 = 0x00000400;
	public final static int DIEFT_SATURATION				= 0x00000800;
	public final static int DIEFT_POSNEGCOEFFICIENTS	 = 0x00001000;
	public final static int DIEFT_POSNEGSATURATION		= 0x00002000;
	public final static int DIEFT_DEADBAND				  = 0x00004000;
	public final static int DIEFT_STARTDELAY				= 0x00008000;

	public final static int DIEFF_OBJECTIDS			 = 0x00000001;
	public final static int DIEFF_OBJECTOFFSETS		 = 0x00000002;
	public final static int DIEFF_CARTESIAN			 = 0x00000010;
	public final static int DIEFF_POLAR				 = 0x00000020;
	public final static int DIEFF_SPHERICAL			 = 0x00000040;

	public final static int DIEP_DURATION			   = 0x00000001;
	public final static int DIEP_SAMPLEPERIOD		   = 0x00000002;
	public final static int DIEP_GAIN				   = 0x00000004;
	public final static int DIEP_TRIGGERBUTTON		  = 0x00000008;
	public final static int DIEP_TRIGGERREPEATINTERVAL  = 0x00000010;
	public final static int DIEP_AXES				   = 0x00000020;
	public final static int DIEP_DIRECTION			  = 0x00000040;
	public final static int DIEP_ENVELOPE			   = 0x00000080;
	public final static int DIEP_TYPESPECIFICPARAMS	 = 0x00000100;
	public final static int DIEP_STARTDELAY			 = 0x00000200;
	public final static int DIEP_ALLPARAMS_DX5		  = 0x000001FF;
	public final static int DIEP_ALLPARAMS			  = 0x000003FF;
	public final static int DIEP_START				  = 0x20000000;
	public final static int DIEP_NORESTART			  = 0x40000000;
	public final static int DIEP_NODOWNLOAD			 = 0x80000000;
	public final static int DIEB_NOTRIGGER			= 0xFFFFFFFF;
	
	public final static int INFINITE				= 0xFFFFFFFF;

	public final static int DI_DEGREES				  = 100;
	public final static int DI_FFNOMINALMAX			 = 10000;
	public final static int DI_SECONDS				  = 1000000;
	
	public final static int DIPROPRANGE_NOMIN = 0x80000000;
	public final static int DIPROPRANGE_NOMAX = 0x7FFFFFFF;

	private final DummyWindow window;
	private final long address;
	private final int dev_type;
	private final int dev_subtype;
	private final String instance_name;
	private final String product_name;
	private final List objects = new ArrayList();
	private final List effects = new ArrayList();
	private final List rumblers = new ArrayList();
	private final int[] device_state;
	private final Map object_to_component = new HashMap();
	private final boolean axes_in_relative_mode;


	private boolean released;
	private DataQueue queue;

	private int button_counter;
	private int current_format_offset;

	public IDirectInputDevice(DummyWindow window, long address, byte[] instance_guid, byte[] product_guid, int dev_type, int dev_subtype, String instance_name, String product_name) throws IOException {
		this.window = window;
		this.address = address;
		this.product_name = product_name;
		this.instance_name = instance_name;
		this.dev_type = dev_type;
		this.dev_subtype = dev_subtype;
		// Assume that the caller (native side) releases the device if setup fails
		enumObjects();
		try {
			enumEffects();
			createRumblers();
		} catch (IOException e) {
			ControllerEnvironment.logln("Failed to create rumblers: " + e.getMessage());
		}
		/* Some DirectInput lamer-designer made the device state
		 * axis mode be per-device not per-axis, so I'll just
		 * get all axes as absolute and compensate for relative axes.
		 *
		 * Unless, of course, all axes are relative like a mouse device,
		 * in which case setting the DIDF_ABSAXIS flag will result in
		 * incorrect axis values returned from GetDeviceData for some
		 * obscure reason.
		 */
		boolean all_relative = true;
		boolean has_axis = false;
		for (int i = 0; i < objects.size(); i++) {
			DIDeviceObject obj = (DIDeviceObject)objects.get(i);
			if (obj.isAxis()) {
				has_axis = true;
				if (!obj.isRelative()) {
					all_relative = false;
					break;
				}
			}
		}
		this.axes_in_relative_mode = all_relative && has_axis;
		int axis_mode = all_relative ? DIDF_RELAXIS : DIDF_ABSAXIS;
		setDataFormat(axis_mode);
		if (rumblers.size() > 0) {
			try {
				setCooperativeLevel(DISCL_BACKGROUND | DISCL_EXCLUSIVE);
			} catch (IOException e) {
				setCooperativeLevel(DISCL_BACKGROUND | DISCL_NONEXCLUSIVE);
			}
		} else
			setCooperativeLevel(DISCL_BACKGROUND | DISCL_NONEXCLUSIVE);
		setBufferSize(AbstractController.EVENT_QUEUE_DEPTH);
		acquire();
		this.device_state = new int[objects.size()];
	}

	public final boolean areAxesRelative() {
		return axes_in_relative_mode;
	}

	public final Rumbler[] getRumblers() {
		return (Rumbler[])rumblers.toArray(new Rumbler[]{});
	}

	private final List createRumblers() throws IOException {
		DIDeviceObject x_axis = lookupObjectByGUID(GUID_XAxis);
//		DIDeviceObject y_axis = lookupObjectByGUID(GUID_YAxis);
		if(x_axis == null/* || y_axis == null*/){
			return rumblers;
		}
		DIDeviceObject[] axes = {x_axis/*, y_axis*/};
		long[] directions = {0/*, 0*/};
		for (int i = 0; i < effects.size(); i++) {
			DIEffectInfo info = (DIEffectInfo)effects.get(i);
			if ((info.getEffectType() & 0xff) == DIEFT_PERIODIC &&
					(info.getDynamicParams() & DIEP_GAIN) != 0) {
				
				rumblers.add(createPeriodicRumbler(axes, directions, info));
			}
		}
		return rumblers;
	}

	private final Rumbler createPeriodicRumbler(DIDeviceObject[] axes, long[] directions, DIEffectInfo info) throws IOException {
		int[] axis_ids = new int[axes.length];
		for (int i = 0; i < axis_ids.length; i++) {
			axis_ids[i] = axes[i].getDIIdentifier();
		}
		long effect_address = nCreatePeriodicEffect(address, info.getGUID(), DIEFF_CARTESIAN | DIEFF_OBJECTIDS, INFINITE, 0, DI_FFNOMINALMAX, DIEB_NOTRIGGER, 0, axis_ids, directions, 0, 0, 0, 0, DI_FFNOMINALMAX, 0, 0, 50000, 0);
		return new IDirectInputEffect(effect_address, info);
	}
	private final static native long nCreatePeriodicEffect(long address, byte[] effect_guid, int flags, int duration, int sample_period, int gain, int trigger_button,  int trigger_repeat_interval, int[] axis_ids, long[] directions, int envelope_attack_level, int envelope_attack_time, int envelope_fade_level, int envelope_fade_time, int periodic_magnitude, int periodic_offset, int periodic_phase, int periodic_period, int start_delay) throws IOException;

	private final DIDeviceObject lookupObjectByGUID(int guid_id) {
		for (int i = 0; i < objects.size(); i++) {
			DIDeviceObject object = (DIDeviceObject)objects.get(i);
			if (guid_id == object.getGUIDType())
				return object;
		}
		return null;
	}

	public final int getPollData(DIDeviceObject object) {
		return device_state[object.getFormatOffset()];
	}

	public final DIDeviceObject mapEvent(DIDeviceObjectData event) {
		/* Raw event format offsets (dwOfs member) is in bytes,
		 * but we're indexing into ints so we have to compensate
		 * for the int size (4 bytes)
		 */
		int format_offset = event.getFormatOffset()/4;
		return (DIDeviceObject)objects.get(format_offset);
	}

	public final DIComponent mapObject(DIDeviceObject object) {
		return (DIComponent)object_to_component.get(object);
	}

	public final void registerComponent(DIDeviceObject object, DIComponent component) {
		object_to_component.put(object, component);
	}

	public final synchronized void pollAll() throws IOException {
		checkReleased();
		poll();
		getDeviceState(device_state);
		queue.compact();
		getDeviceData(queue);
		queue.flip();
	}

	public synchronized final boolean getNextEvent(DIDeviceObjectData data) {
		DIDeviceObjectData next_event = (DIDeviceObjectData)queue.get();
		if (next_event == null)
			return false;
		data.set(next_event);
		return true;
	}
	
	private final void poll() throws IOException {
		int res = nPoll(address);
		if (res != DI_OK && res != DI_NOEFFECT) {
			if (res == DIERR_NOTACQUIRED) {
				acquire();
				return;
			}
			throw new IOException("Failed to poll device (" + Integer.toHexString(res) + ")");
		}
	}
	private final static native int nPoll(long address) throws IOException;

	private final void acquire() throws IOException {
		int res = nAcquire(address);
		if (res != DI_OK && res != DIERR_OTHERAPPHASPRIO && res != DI_NOEFFECT)
			throw new IOException("Failed to acquire device (" + Integer.toHexString(res) + ")");
	}
	private final static native int nAcquire(long address);

	private final void unacquire() throws IOException {
		int res = nUnacquire(address);
		if (res != DI_OK && res != DI_NOEFFECT)
			throw new IOException("Failed to unAcquire device (" + Integer.toHexString(res) + ")");
	}
	private final static native int nUnacquire(long address);

	private final boolean getDeviceData(DataQueue queue) throws IOException {
		int res = nGetDeviceData(address, 0, queue, queue.getElements(), queue.position(), queue.remaining());
		if (res != DI_OK && res != DI_BUFFEROVERFLOW) {
			if (res == DIERR_NOTACQUIRED) {
				acquire();
				return false;
			}
			throw new IOException("Failed to get device data (" + Integer.toHexString(res) + ")");
		}
		return true;
	}
	private final static native int nGetDeviceData(long address, int flags, DataQueue queue, Object[] queue_elements, int position, int remaining);
	
	private final void getDeviceState(int[] device_state) throws IOException {
		int res = nGetDeviceState(address, device_state);
		if (res != DI_OK) {
			if (res == DIERR_NOTACQUIRED) {
				Arrays.fill(device_state, 0);
				acquire();
				return;
			}
			throw new IOException("Failed to get device state (" + Integer.toHexString(res) + ")");
		}
	}
	private final static native int nGetDeviceState(long address, int[] device_state);

	/* Set a custom data format that maps each object's data into an int[]
	   array with the same index as in the objects List */
	private final void setDataFormat(int flags) throws IOException {
		DIDeviceObject[] device_objects = new DIDeviceObject[objects.size()];
		objects.toArray(device_objects);
		int res = nSetDataFormat(address, flags, device_objects);
		if (res != DI_OK)
			throw new IOException("Failed to set data format (" + Integer.toHexString(res) + ")");
	}
	private final static native int nSetDataFormat(long address, int flags, DIDeviceObject[] device_objects);
	
	public final String getProductName() {
		return product_name;
	}

	public final int getType() {
		return dev_type;
	}

	public final List getObjects() {
		return objects;
	}

	private final void enumEffects() throws IOException {
		int res = nEnumEffects(address, DIEFT_ALL);
		if (res != DI_OK)
			throw new IOException("Failed to enumerate effects (" + Integer.toHexString(res) + ")");
	}
	private final native int nEnumEffects(long address, int flags);
	
	/* Called from native side from nEnumEffects */
	private final void addEffect(byte[] guid, int guid_id, int effect_type, int static_params, int dynamic_params, String name) {
		effects.add(new DIEffectInfo(this, guid, guid_id, effect_type, static_params, dynamic_params, name));
	}

	private final void enumObjects() throws IOException {
		int res = nEnumObjects(address, DIDFT_BUTTON | DIDFT_AXIS | DIDFT_POV);
		if (res != DI_OK)
			throw new IOException("Failed to enumerate objects (" + Integer.toHexString(res) + ")");
	}
	private final native int nEnumObjects(long address, int flags);

	public final synchronized long[] getRangeProperty(int object_identifier) throws IOException {
		checkReleased();
		long[] range = new long[2];
		int res = nGetRangeProperty(address, object_identifier, range);
		if (res != DI_OK)
			throw new IOException("Failed to get object range (" + res + ")");
		return range;
	}
	private final static native int nGetRangeProperty(long address, int object_id, long[] range);

	public final synchronized int getDeadzoneProperty(int object_identifier) throws IOException {
		checkReleased();
		return nGetDeadzoneProperty(address, object_identifier);
	}
	private final static native int nGetDeadzoneProperty(long address, int object_id) throws IOException;

	/* Called from native side from nEnumObjects */
	private final void addObject(byte[] guid, int guid_type, int identifier, int type, int instance, int flags, String name) throws IOException {
		Component.Identifier id = getIdentifier(guid_type, type, instance);
		int format_offset = current_format_offset++;
		DIDeviceObject obj = new DIDeviceObject(this, id, guid, guid_type, identifier, type, instance, flags, name, format_offset);
		objects.add(obj);
	}

	private final static Component.Identifier.Key getKeyIdentifier(int key_instance) {
		return DIIdentifierMap.getKeyIdentifier(key_instance);
	}

	private final Component.Identifier.Button getNextButtonIdentifier() {
		int button_id = button_counter++;
		return DIIdentifierMap.getButtonIdentifier(button_id);
	}
	
	private final Component.Identifier getIdentifier(int guid_type, int type, int instance) {
		switch (guid_type) {
			case IDirectInputDevice.GUID_XAxis:
				return Component.Identifier.Axis.X;
			case IDirectInputDevice.GUID_YAxis:
				return Component.Identifier.Axis.Y;
			case IDirectInputDevice.GUID_ZAxis:
				return Component.Identifier.Axis.Z;
			case IDirectInputDevice.GUID_RxAxis:
				return Component.Identifier.Axis.RX;
			case IDirectInputDevice.GUID_RyAxis:
				return Component.Identifier.Axis.RY;
			case IDirectInputDevice.GUID_RzAxis:
				return Component.Identifier.Axis.RZ;
			case IDirectInputDevice.GUID_Slider:
				return Component.Identifier.Axis.SLIDER;
			case IDirectInputDevice.GUID_POV:
				return Component.Identifier.Axis.POV;
			case IDirectInputDevice.GUID_Key:
				return getKeyIdentifier(instance);
			case IDirectInputDevice.GUID_Button:
				return getNextButtonIdentifier();
			default:
				return Component.Identifier.Axis.UNKNOWN;
		}
	}
	
	public final synchronized void setBufferSize(int size) throws IOException {
		checkReleased();
		unacquire();
		int res = nSetBufferSize(address, size);
		if (res != DI_OK && res != DI_PROPNOEFFECT && res != DI_POLLEDDEVICE)
			throw new IOException("Failed to set buffer size (" + Integer.toHexString(res) + ")");
		queue = new DataQueue(size, DIDeviceObjectData.class);
		queue.position(queue.limit());
		acquire();
	}
	private final static native int nSetBufferSize(long address, int size);

	public final synchronized void setCooperativeLevel(int flags) throws IOException {
		checkReleased();
		int res = nSetCooperativeLevel(address, window.getHwnd(), flags);
		if (res != DI_OK)
			throw new IOException("Failed to set cooperative level (" + Integer.toHexString(res) + ")");
	}
	private final static native int nSetCooperativeLevel(long address, long hwnd_address, int flags);

	public synchronized final void release() {
		if (!released) {
			released = true;
			for (int i = 0; i < rumblers.size(); i++) {
				IDirectInputEffect effect = (IDirectInputEffect)rumblers.get(i);
				effect.release();
			}
			nRelease(address);
		}
	}
	private final static native void nRelease(long address);

	private final void checkReleased() throws IOException {
		if (released)
			throw new IOException("Device is released");
	}

	protected void finalize() {
		release();
	}
}

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
import java.util.Iterator;

/** OSX HIDManager implementation
* @author elias
* @author gregorypierce
* @version 1.0
*/
final class OSXHIDDevice {
	private final static int AXIS_DEFAULT_MIN_VALUE = 0;
	private final static int AXIS_DEFAULT_MAX_VALUE = 64*1024;
	
	private final static String kIOHIDTransportKey                  = "Transport";
	private final static String kIOHIDVendorIDKey                   = "VendorID";
	private final static String kIOHIDVendorIDSourceKey             = "VendorIDSource";
	private final static String kIOHIDProductIDKey                  = "ProductID";
	private final static String kIOHIDVersionNumberKey              = "VersionNumber";
	private final static String kIOHIDManufacturerKey               = "Manufacturer";
	private final static String kIOHIDProductKey                    = "Product";
	private final static String kIOHIDSerialNumberKey               = "SerialNumber";
	private final static String kIOHIDCountryCodeKey                = "CountryCode";
	private final static String kIOHIDLocationIDKey                 = "LocationID";
	private final static String kIOHIDDeviceUsageKey                = "DeviceUsage";
	private final static String kIOHIDDeviceUsagePageKey            = "DeviceUsagePage";
	private final static String kIOHIDDeviceUsagePairsKey           = "DeviceUsagePairs";
	private final static String kIOHIDPrimaryUsageKey               = "PrimaryUsage";
	private final static String kIOHIDPrimaryUsagePageKey           = "PrimaryUsagePage";
	private final static String kIOHIDMaxInputReportSizeKey     = "MaxInputReportSize";
	private final static String kIOHIDMaxOutputReportSizeKey        = "MaxOutputReportSize";
	private final static String kIOHIDMaxFeatureReportSizeKey       = "MaxFeatureReportSize";

	private final static String kIOHIDElementKey                    = "Elements";

	private final static String kIOHIDElementCookieKey              = "ElementCookie";
	private final static String kIOHIDElementTypeKey                = "Type";
	private final static String kIOHIDElementCollectionTypeKey      = "CollectionType";
	private final static String kIOHIDElementUsageKey               = "Usage";
	private final static String kIOHIDElementUsagePageKey           = "UsagePage";
	private final static String kIOHIDElementMinKey                 = "Min";
	private final static String kIOHIDElementMaxKey                 = "Max";
	private final static String kIOHIDElementScaledMinKey           = "ScaledMin";
	private final static String kIOHIDElementScaledMaxKey           = "ScaledMax";
	private final static String kIOHIDElementSizeKey                = "Size";
	private final static String kIOHIDElementReportSizeKey          = "ReportSize";
	private final static String kIOHIDElementReportCountKey         = "ReportCount";
	private final static String kIOHIDElementReportIDKey            = "ReportID";
	private final static String kIOHIDElementIsArrayKey             = "IsArray";
	private final static String kIOHIDElementIsRelativeKey          = "IsRelative";
	private final static String kIOHIDElementIsWrappingKey          = "IsWrapping";
	private final static String kIOHIDElementIsNonLinearKey         = "IsNonLinear";
	private final static String kIOHIDElementHasPreferredStateKey   = "HasPreferredState";
	private final static String kIOHIDElementHasNullStateKey        = "HasNullState";
	private final static String kIOHIDElementUnitKey                = "Unit";
	private final static String kIOHIDElementUnitExponentKey        = "UnitExponent";
	private final static String kIOHIDElementNameKey                = "Name";
	private final static String kIOHIDElementValueLocationKey       = "ValueLocation";
	private final static String kIOHIDElementDuplicateIndexKey      = "DuplicateIndex";
	private final static String kIOHIDElementParentCollectionKey    = "ParentCollection";

	private final long device_address;
	private final long device_interface_address;
	private final Map properties;

	private boolean released;

	public OSXHIDDevice(long device_address, long device_interface_address) throws IOException {
		this.device_address = device_address;
		this.device_interface_address = device_interface_address;
		this.properties = getDeviceProperties();
		open();
	}

	public final Controller.PortType getPortType() {
		String transport = (String)properties.get(kIOHIDTransportKey);
		if (transport == null)
			return Controller.PortType.UNKNOWN;
		if (transport.equals("USB")) {
			return Controller.PortType.USB;
		} else {
			return Controller.PortType.UNKNOWN;
		}
	}

	public final String getProductName() {
		return (String)properties.get(kIOHIDProductKey);
	}

	private final OSXHIDElement createElementFromElementProperties(Map element_properties) {
	/*	long size = getLongFromProperties(element_properties, kIOHIDElementSizeKey);
		// ignore elements that can't fit into the 32 bit value field of a hid event
		if (size > 32)
			return null;*/
		long element_cookie = getLongFromProperties(element_properties, kIOHIDElementCookieKey);
		int element_type_id = getIntFromProperties(element_properties, kIOHIDElementTypeKey);
		ElementType element_type = ElementType.map(element_type_id);
		int min = (int)getLongFromProperties(element_properties, kIOHIDElementMinKey, AXIS_DEFAULT_MIN_VALUE);
		int max = (int)getLongFromProperties(element_properties, kIOHIDElementMaxKey, AXIS_DEFAULT_MAX_VALUE);
/*		long scaled_min = getLongFromProperties(element_properties, kIOHIDElementScaledMinKey, Long.MIN_VALUE);
		long scaled_max = getLongFromProperties(element_properties, kIOHIDElementScaledMaxKey, Long.MAX_VALUE);*/
		UsagePair device_usage_pair = getUsagePair();
		boolean default_relative = device_usage_pair != null && (device_usage_pair.getUsage() == GenericDesktopUsage.POINTER || device_usage_pair.getUsage() == GenericDesktopUsage.MOUSE);
		
		boolean is_relative = getBooleanFromProperties(element_properties, kIOHIDElementIsRelativeKey, default_relative);
/*		boolean is_wrapping = getBooleanFromProperties(element_properties, kIOHIDElementIsWrappingKey);
		boolean is_non_linear = getBooleanFromProperties(element_properties, kIOHIDElementIsNonLinearKey);
		boolean has_preferred_state = getBooleanFromProperties(element_properties, kIOHIDElementHasPreferredStateKey);
		boolean has_null_state = getBooleanFromProperties(element_properties, kIOHIDElementHasNullStateKey);*/
		int usage = getIntFromProperties(element_properties, kIOHIDElementUsageKey);
		int usage_page = getIntFromProperties(element_properties, kIOHIDElementUsagePageKey);
		UsagePair usage_pair = createUsagePair(usage_page, usage);
		if (usage_pair == null || (element_type != ElementType.INPUT_MISC && element_type != ElementType.INPUT_BUTTON && element_type != ElementType.INPUT_AXIS)) {
//System.out.println("element_type = 0x" + element_type + " | usage = " + usage + " | usage_page = " + usage_page);
			return null;
		} else {
			return new OSXHIDElement(this, usage_pair, element_cookie, element_type, min, max, is_relative);
		}
	}

	private final void addElements(List elements, Map properties) {
		Object[] elements_properties = (Object[])properties.get(kIOHIDElementKey);
		if (elements_properties == null)
			return;
		for (int i = 0; i < elements_properties.length; i++) {
			Map element_properties = (Map)elements_properties[i];
			OSXHIDElement element = createElementFromElementProperties(element_properties);
			if (element != null) {
				elements.add(element);
			}
			addElements(elements, element_properties);
		}
	}
	
	public final List getElements() {
		List elements = new ArrayList();
		addElements(elements, properties);
		return elements;
	}
	
	private final static long getLongFromProperties(Map properties, String key, long default_value) {
		Long long_obj = (Long)properties.get(key);
		if (long_obj == null)
			return default_value;
		return long_obj.longValue();
	}

	private final static boolean getBooleanFromProperties(Map properties, String key, boolean default_value) {
		return getLongFromProperties(properties, key, default_value ? 1 : 0) != 0;
	}

	private final static int getIntFromProperties(Map properties, String key) {
		return (int)getLongFromProperties(properties, key);
	}

	private final static long getLongFromProperties(Map properties, String key) {
		Long long_obj = (Long)properties.get(key);
		return long_obj.longValue();
	}

	private final static UsagePair createUsagePair(int usage_page_id, int usage_id) {
		UsagePage usage_page = UsagePage.map(usage_page_id);
		if (usage_page != null) {
			Usage usage = usage_page.mapUsage(usage_id);
			if (usage != null)
				return new UsagePair(usage_page, usage);
		}
		return null;
	}

	public final UsagePair getUsagePair() {
		int usage_page_id = getIntFromProperties(properties, kIOHIDPrimaryUsagePageKey);
		int usage_id = getIntFromProperties(properties, kIOHIDPrimaryUsageKey);
		return createUsagePair(usage_page_id, usage_id);
	}
	
/*
	public final List getUsagePairs() {
		List usage_pairs_list = new ArrayList();
		Object[] usage_pairs = (Object[])properties.get(kIOHIDDeviceUsagePairsKey);
		if (usage_pairs == null) {
			int usage_page_id = getIntFromProperties(properties, kIOHIDPrimaryUsagePageKey);
			int usage_id = getIntFromProperties(properties, kIOHIDPrimaryUsageKey);
			UsagePair pair = createUsagePair(usage_page_id, usage_id);
			if (pair != null)
				usage_pairs_list.add(pair);
		}
		for (int i = 0; i < usage_pairs.length; i++) {
			Map usage_pair = (Map)usage_pairs[i];
			int usage_page_id = getIntFromProperties(usage_pair, kIOHIDDeviceUsagePageKey);
			int usage_id = getIntFromProperties(usage_pair, kIOHIDDeviceUsageKey);
			UsagePair pair = createUsagePair(usage_page_id, usage_id);
			if (pair != null)
				usage_pairs_list.add(pair);
		}
		return usage_pairs_list;
	}
*/	
	private final void dumpProperties() {
		System.out.println(toString());
		dumpMap("", properties);
	}

	private final static void dumpArray(String prefix, Object[] array) {
		System.out.println(prefix + "{");
		for (int i = 0; i < array.length; i++) {
			dumpObject(prefix + "\t", array[i]);
			System.out.println(prefix + ",");
		}
		System.out.println(prefix + "}");
	}
	
	private final static void dumpMap(String prefix, Map map) {
		Iterator keys = map.keySet().iterator();
		while (keys.hasNext()) {
			Object key = keys.next();
			Object value = map.get(key);
			dumpObject(prefix, key);
			dumpObject(prefix + "\t", value);
		}
	}
	
	private final static void dumpObject(String prefix, Object obj) {
		if (obj instanceof Long) {
			Long l = (Long)obj;
			System.out.println(prefix + "0x" + Long.toHexString(l.longValue()));
		} else if (obj instanceof Map)
			dumpMap(prefix, (Map)obj);
		else if (obj.getClass().isArray())
			dumpArray(prefix, (Object[])obj);
		else
			System.out.println(prefix + obj);
	}

	private final Map getDeviceProperties() throws IOException {
		return nGetDeviceProperties(device_address);
	}
	private final static native Map nGetDeviceProperties(long device_address) throws IOException;

	public final synchronized void release() throws IOException {
		try {
			close();
		} finally {
			released = true;
			nReleaseDevice(device_address, device_interface_address);
		}
	}
	private final static native void nReleaseDevice(long device_address, long device_interface_address);

	public final synchronized void getElementValue(long element_cookie, OSXEvent event) throws IOException {
		checkReleased();
		nGetElementValue(device_interface_address, element_cookie, event);
	}
	private final static native void nGetElementValue(long device_interface_address, long element_cookie, OSXEvent event) throws IOException;

	public final synchronized OSXHIDQueue createQueue(int queue_depth) throws IOException {
		checkReleased();
		long queue_address = nCreateQueue(device_interface_address);
		return new OSXHIDQueue(queue_address, queue_depth);
	}
	private final static native long nCreateQueue(long device_interface_address) throws IOException;

	private final void open() throws IOException {
		nOpen(device_interface_address);
	}
	private final static native void nOpen(long device_interface_address) throws IOException;

	private final void close() throws IOException {
		nClose(device_interface_address);
	}
	private final static native void nClose(long device_interface_address) throws IOException;

	private final void checkReleased() throws IOException {
		if (released)
			throw new IOException();
	}
}

/*
Part of the proCONTROLL lib - http://texone.org/procontroll

Copyright (c) 2005 Christian Riekoff

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General
Public License along with this library; if not, write to the
Free Software Foundation, Inc., 59 Temple Place, Suite 330,
Boston, MA  02111-1307  USA
*/

package procontroll;

import java.util.ArrayList;
import java.util.List;

import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;

import processing.core.PApplet;

/**
 * <p>
 * ControllIO is the base class for using controllers in Processing.
 * It provides methods to retrieve information about the connected 
 * devices and to get the input data from them.<br>
 * To get a ControllIO object you to use the getInstance() Method. As
 * a startup you should use the printDevices() to see if all Controllers
 * are connected and correctly found.
 * </p>
 * <p>
 * To react on button events you can plug methods, that are called when
 * a button is pressed, released or while a button is pressed.
 * @example procontroll
 * @related ControllDevice
 * @usage application
 */
public class ControllIO implements Runnable{
	/**
	 * @invisible
	 */
	public static final int ON_PRESS = 0;
	/**
	 * @invisible
	 */
	public static final int ON_RELEASE = 1;
	/**
	 * @invisible
	 */
	public static final int WHILE_PRESS = 2;

	/**
	 * Ensures that there only exists one instance of ControllIO
	 */
	static private ControllIO instance;

	/**
	 * Holds the environment of JInput
	 */
	private final ControllerEnvironment environment;

	/**
	 * Instance to the PApplet where procontroll is running
	 */
	private final PApplet parent;

	/**
	 * List of the available devices
	 */
	private final List devices = new ArrayList();
	
	private final Thread thread;
	
	private boolean active = true;

	/**
	 * Initialise the ControllIO instance
	 * @param i_parent
	 */
	private ControllIO(final PApplet i_parent){
		environment = ControllerEnvironment.getEnvironment();
		parent = i_parent;
		parent.registerDispose(this);
		parent.registerPre(this);
		setupDevices();
		thread = new Thread(this);
		thread.start();
	}

	/**
	 * Puts the available devices into the devices List
	 *
	 */
	private void setupDevices(){
		final Controller[] controllers = environment.getControllers();
		for (int i = 0; i < controllers.length; i++){
			devices.add(new ControllDevice(controllers[i],parent));
		}
	}

	/**
	 * Use this method to get a ControllIO instance.
	 * @param i_parent PApplet, the application proCONTROLL is running in
	 * @return ControllIO, an instance of ControllIO
	 * @example procontroll
	 * @usage application
	 * @related ControllIO
	 */
	static public ControllIO getInstance(final PApplet i_parent){
		if (instance == null){
			instance = new ControllIO(i_parent);
		}
		return instance;
	}

	/**
	 * dispose method called by PApplet after closing. The update thread is deactivated here
	 * @invisible
	 */
	public void dispose(){
		active = false;
	}

	/**
	 * Lists the available Devices in the console window. This method
	 * is usefull at startup to see if all devices are proper connected
	 * and o get the name of the desired device.
	 * @shortdesc Lists the available Devices in the console window.
	 * @example procontroll_printDevices
	 * @usage application
	 * @related ControllIO
	 * @related ControllDevice
	 * @related getNumberOfDevices ( )
	 * @related getDevice ( )
	 */
	public void printDevices(){
		System.out.println("\n<<< available proCONTROLL devices: >>>\n");
		for (int i = 0; i < devices.size(); i++){
			System.out.print("     "+i+": ");
			System.out.println(((ControllDevice) devices.get(i)).getName());
		}
		System.out.println("\n<<< >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
	}

	/**
	 * Returns the number of available Devices
	 * @return int, the number of available devices
	 * @example procontroll_printDevices
	 * @related ControllIO
	 * @related ControllDevice
	 * @related getDevice ( )
	 * @usage application
	 */
	public int getNumberOfDevices(){
		return devices.size();
	}

	/**
	 * Use this method to get a Device. You can get a device by its name
	 * or its number. Use printDevices to see what devices are 
	 * available on your system.
	 * @param i_deviceNumber int, number of the device to open
	 * @return ControllDevice, the device coresponding to the given number or name
	 * @example procontroll
	 * @related ControllIO
	 * @related ControllDevice
	 * @related getNumberOfDevices ( )
	 * @related printDevices ( )
	 * @shortdesc Use this method to get a device.
	 * @usage application
	 */
	public ControllDevice getDevice(final int i_deviceNumber){
		if (i_deviceNumber >= getNumberOfDevices()){
			throw new RuntimeException("There is no device with the number " + i_deviceNumber + ".");
		}
		ControllDevice result = (ControllDevice) devices.get(i_deviceNumber);
		result.open();
		return result;
	}

	/**
	 * @param i_deviceName String, name of the device to open
	 */
	public ControllDevice getDevice(final String i_deviceName){
		for (int i = 0; i < getNumberOfDevices(); i++){
			ControllDevice device = (ControllDevice) devices.get(i);
			if (device.getName().equals(i_deviceName)){
				device.open();
				return device;
			}
		}
		throw new RuntimeException("There is no device with the name " + i_deviceName + ".");
	}

	/**
	 * Updates the devices, to get the actual data before a new
	 * frame is drawn
	 * @invisible
	 */
	public void pre(){
		for (int i = 0; i < devices.size(); i++)
			((ControllDevice) devices.get(i)).updateRelative();
	}

	/**
	 * Controllers are now polled in a seperate thread to get independent from
	 * the framerate of the sketch
	 * @invisible
	 */
	public void run(){
		while (active){
			for (int i = 0; i < devices.size(); i++)
				((ControllDevice) devices.get(i)).update();
			try {
			    Thread.sleep  ( 10 );
			} catch ( InterruptedException e ) { }
		}
	}
	
	/**
	 * <p>
	 * Plug is a handy method to handle incoming button events. To create a plug
	 * you have to implement a method that reacts on the events. To plug a method you
	 * need to give ControllIO the method name, the event type you want to react on and
	 * the device and button. If your method is inside a class you have to give ControllIO
	 * a reference to it.
	 * </p>
	 * <p>
	 * If you want to handle the events of a simple button, you only have to implement a
	 * method without parameters. To react on the events of a cooliehat you method needs to
	 * receive two float values, so that proCONTROLL can send you the x and y values of the
	 * cooliehat.
	 * </p>
	 * @param i_object Object: the object with the method to plug
	 * @param i_methodName String: the name of the method that has to be plugged
	 * @param i_eventType constant: can be ControllIO.ON_PRESS, ControllIO.ON_RELEASE or ControllIO.WHILE_PRESS
	 * @param i_intputDevice int: the number of the device that triggers the plug
	 * @param i_input int: the number of the button that triggers the plug
	 * @example procontroll
	 * @shortdesc Plugs a method to handle button events.
	 */
	public void plug(
		final Object i_object, 
		final String i_methodName, 
		final int i_eventType,
		final int i_intputDevice,
		final int i_input
	){
		ControllDevice device = getDevice(i_intputDevice);
		device.plug(i_object,i_methodName,i_eventType,i_input);
	}
	
	public void plug(
		final String i_methodName, 
		final int i_eventType,
		final int i_intputDevice,
		final int i_input
	){
		plug(parent,i_methodName,i_eventType,i_intputDevice,i_input);
	}
	
	/**
	 * 
	 * @param i_intputDevice String: the name of the device that triggers the plug
	 * @param i_input int: the name of the button that triggers the plug
	 */
	public void plug(
		final Object i_object, 
		final String i_methodName, 
		final int i_eventType,
		final String i_intputDevice,
		final String i_input
	){
		ControllDevice device = getDevice(i_intputDevice);
		device.plug(i_object,i_methodName,i_eventType,i_input);
	}
	
	public void plug(
		final String i_methodName, 
		final int i_eventType,
		final String i_intputDevice,
		final String i_input
	){
		plug(parent,i_methodName,i_eventType,i_intputDevice,i_input);
	}
}

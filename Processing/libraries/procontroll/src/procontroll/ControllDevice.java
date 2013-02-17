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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import processing.core.PApplet;

import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.java.games.input.Rumbler;

/**
 * <p>
 * The device class is for the communication with your input devices. A device
 * consists of buttons and sliders, sliders can be grouped to sticks.
 * </p>
 * <p>
 * To react on button events you can plug methods, that are called when
 * a button is pressed, released or while a button is pressed.
 * </p>
 * @example procontroll
 * @related ControllIO
 * @related ControllSlider
 * @related ControllButton
 * @related ControllStick
 */
public class ControllDevice{

	/**
	 * The JInput controller instance for this device
	 */
	private final Controller controller;
	
	/**
	 * list containing the sticks of the device
	 */
	private final List sticks = new ArrayList();

	/**
	 * list containing the sliders of the device
	 */
	private final List sliders = new ArrayList();
	
	/**
	 * list containing the buttons of the device
	 */
	private final List buttons = new ArrayList();
	
	/**
	 * list containing the rumblers of the device
	 */
	private Rumbler[] rumblers = new Rumbler[0];
	
	/**
	 * to map the inputnames and Controller inputs
	 */
	private final Map inputMap = new HashMap();
	
	/**
	 * A List with the buttons and Sliders available by the device
	 */
	private final List inputs = new ArrayList();

	/**
	 * true if the device has been opened. Only opened devices are updated before
	 * a frame.
	 */
	private boolean open = false;

	/**
	 * The name of the device.
	 */
	private final String name;
	
	/**
	 * Instance to the PApplet where procontroll is running
	 */
	private final PApplet parent;

	/**
	 * Initializes a new device by the given Controller
	 * 
	 * @param i_controller
	 */
	ControllDevice(final Controller i_controller, final PApplet i_parent){
		controller = i_controller;
		parent = i_parent;
		name = i_controller.getName();
		setupDevice();
	}

	/**
	 * Loads the available Sliders, Sticks and Buttons for a device
	 */
	private void setupDevice(){
		final Component[] components = controller.getComponents();
		for (int i = 0; i < components.length; i++){
			ControllInput input;
			if(components[i].isAnalog()){
				if(components[i].isRelative()){
					input = new ControllRelativeSlider(components[i]);
				}else{
					input = new ControllSlider(components[i]);
				}
				sliders.add(input);
			}else{
				if(components[i].getIdentifier() == Component.Identifier.Axis.POV){
					input = new ControllCoolieHat(components[i],parent);
				}else{
					input = new ControllButton(components[i],parent);
				}
				buttons.add(input);
			}
			inputMap.put(input.getName(), input);
		}

		inputs.addAll(sliders);
		inputs.addAll(buttons);
		
		if(sliders.size()%2 == 0){
			for(int i = 0; i < sliders.size(); i += 2){
				ControllSlider sliderY = (ControllSlider)sliders.get(i);
				ControllSlider sliderX = (ControllSlider)sliders.get(i+1);
				sticks.add(new ControllStick(sliderX,sliderY));
			}
		}
		
		rumblers = controller.getRumblers();
		System.out.println(rumblers.length);
	}

	/**
	 * Returns the name of the device.
	 * @return String, the name of a device
	 * @example procontroll_printDevices
	 * @related ControllDevice
	 */
	public String getName(){
		return name;
	}

	/**
	 * Returns the String representation of a device
	 * @return String, the String representation of a device
	 * @invisible
	 */
	public String toString(){
		return controller.getName();
	}

	/**
	 * This method is called on every thread loop 
	 */
	protected void update(){
		if(open){
			controller.poll();
			for (int i = 0; i < inputs.size(); i++)
				((ControllInput) inputs.get(i)).update();
		}
	}
	
	/**
	 * This method is called before each frame to update the controller values
	 */
	protected void updateRelative(){
		if(open){
			for (int i = 0; i < sliders.size(); i++)
				((ControllSlider) sliders.get(i)).updateRelative();
		}
	}
	
	/**
	 * Lists the available slider of a device in the console window. This method
	 * is usefull at startup to get the name of the different sliders.
	 * @shortdesc Lists the available sliders of a device.
	 * @example procontroll_printDevices
	 * @related ControllDevice
	 * @related ControllSlider
	 * @related printButtons ( )
	 * @related printSticks ( )
	 */
	public void printSliders(){
		if(sliders.size() > 0){
			System.out.println("\n<<< available "+name+" sliders: >>>\n");
			for (int i = 0; i < sliders.size(); i++){
				ControllSlider slider = (ControllSlider) sliders.get(i);
				System.out.print("     "+i+": ");
				System.out.print(slider.getName());
				if(slider.isRelative()){
					System.out.println(" relative");
				}else{
					System.out.println(" absolute");
				}
			}
			System.out.println("\n<<< >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		}
	}
	
	/**
	 * Lists the available button of a device in the console window. This method
	 * is usefull at startup to get the name of the different buttons.
	 * @shortdesc Lists the available buttons of a device.
	 * @example procontroll_printDevices
	 * @related ControllDevice
	 * @related ControllButton
	 * @related printSliders ( )
	 * @related printSticks ( )
	 */
	public void printButtons(){
		if(buttons.size() > 0){
			System.out.println("\n<<< available "+name+" buttons: >>>\n");
			for (int i = 0; i < buttons.size(); i++){
				System.out.print("     "+i+": ");
				System.out.println(((ControllInput) buttons.get(i)).getName());
			}
			System.out.println("\n<<< >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		}
	}
	
	/**
	 * Lists the available sticks of a device in the console window. This method
	 * is usefull at startup to get the name of the different sticks.
	 * @shortdesc Lists the available sticks of a device.
	 * @example procontroll_stick
	 * @related ControllDevice
	 * @related ControllStick
	 * @related printSliders ( )
	 * @related printButtons ( )
	 */
	public void printSticks(){
		if(sticks.size() > 0){
			System.out.println("\n<<< available "+name+" sticks: >>>\n");
			for (int i = 0; i < sticks.size(); i++){
				System.out.print("     "+i+": ");
				System.out.println(((ControllStick) sticks.get(i)).getName());
			}
			System.out.println("\n<<< >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		}
	}

	/**
	 * Returns the number of sliders of the device.
	 * @return int, the number of sliders available for a device
	 * @example procontroll_printDevices
	 * @related ControllDevice
	 * @related ControllSlider
	 * @related getNumberOfButtons ( )
	 * @related getNumberOfSticks ( )
	 * @related getSlider ( )
	 */
	public int getNumberOfSliders(){
		return sliders.size();
	}

	/**
	 * Use this method to get a Slider. You can get a slider by its name or its
	 * number. Use printSliders to see what sliders are available for a device.
	 * @param i_sliderNumb int, the number of the slider to return
	 * @return ControllSlider, the Slider coresponding to the given number or String
	 * @example procontroll
	 * @shortdesc Use this method to get a Slider.
	 * @related ControllDevice
	 * @related ControllSlider
	 * @related getNumberOfSliders ( )
	 * @related getButton ( )
	 * @related getStick ( )
	 */ 
	 public ControllSlider getSlider(final int i_sliderNumb){ 
		 return (ControllSlider)sliders.get(i_sliderNumb); 
	 } 
	 
	 /**
	 * @param i_sliderName String, name of the slider to return
	 */
	public ControllSlider getSlider(final String i_sliderName){
		try{
			return(ControllSlider)inputMap.get(i_sliderName);
		}catch (ClassCastException e){
		}
		
		throw new RuntimeException("There is no slider with the name " + i_sliderName + ".");
	}

	/**
	 * Tolerance is minimum under which the input is set to zero.
	 * Use this method to set the tolerance for all sliders of the device.
	 * @param i_tolerance float, the new tolerance for the device
	 * @example procontroll
	 * @shortdesc Use this method to set the tolerance for all sliders of the device.
	 * @related ControllDevice
	 * @related ControllSlider
	 */
	public void setTolerance(final float i_tolerance){
		for (int i = 0; i < sliders.size(); i++)
			((ControllSlider) sliders.get(i)).setTolerance(i_tolerance);
		;
	}
	
	/**
	 * Returns the number of buttons of the device.
	 * @return int, the number of buttons available for a device
	 * @example procontroll_printDevices
	 * @related ControllDevice
	 * @related ControllButton
	 * @related getNumberOfSliders ( )
	 * @related getNumberOfSticks ( )
	 * @related getButton ( )
	 */
	public int getNumberOfButtons(){
		return buttons.size();
	}

	/**
	 * Use this method to get a Button. You can get a Button by its name or its
	 * number. Use printButtons to see what buttons are available for a device.
	 * @param i_buttonNumb int, the number of the button to return
	 * @return ControllButton, the button coresponding to the given number or name
	 * @example procontroll
	 * @shortdesc Use this method to get a Button.
	 * @related ControllDevice
	 * @related ControllButton
	 * @related getSlider ( )
	 * @related getStick ( )
	 * @related getCoolieHat ( )
	 */ 
	 public ControllButton getButton(final int i_buttonNumb){ 
		 return (ControllButton)buttons.get(i_buttonNumb); 
	 } 
	 
	 /**
	 *  @param i_sliderName String, name of the button to return
	 */
	public ControllButton getButton(final String i_buttonName){
		try{
			return(ControllButton)inputMap.get(i_buttonName);
		}catch (ClassCastException e){
		}
		throw new RuntimeException("There is no button with the name " + i_buttonName + ".");
	}
	
	/**
	 * Returns the number of sticks of the device.
	 * @return int, the number of sticks available for a device
	 * @example procontroll_printDevices
	 * @related ControllDevice
	 * @related ControllStick
	 * @related getNumberOfSliders ( )
	 * @related getNumberOfButtons ( )
	 * @related getStick ( )
	 */
	public int getNumberOfSticks(){
		return sticks.size();
	}

	/**
	 * Use this method to get a stick. You can get a stick by its name or its
	 * number. Use printSticks to see what sticks are available for a device.
	 * @param i_stickNumb int, the number of the button to return
	 * @return ControllStick, the stick coresponding to the given number or name
	 * @shortdesc Use this method to get a Button.
	 * @example procontroll_getTotalValue_stick
	 * @related ControllDevice
	 * @related ControllStick
	 * @related getNumberOfButtons ( )
	 * @related getNumberOfSliders ( )
	 * @related getSlider ( )
	 * @related getButton ( )
	 * @related getCoolieHat ( )
	 */ 
	 public ControllStick getStick(final int i_stickNumb){ 
		 return (ControllStick)sticks.get(i_stickNumb); 
	 } 
	 
	 /**
	 *  @param i_stickName String, name of the button to return
	 */
	public ControllStick getStick(final String i_stickName){
		for (int i = 0; i < getNumberOfSticks(); i++){
			ControllStick stick = (ControllStick) sticks.get(i);
			if (stick.getName().equals(i_stickName)){
				return getStick(i);
			}
		}
		throw new RuntimeException("There is no stick with the name " + i_stickName + ".");
	}

	/**
	 * Use this method to get a cooliehat. You can get a cooliehat by its name or its
	 * number. Use printButtons to see what buttons are a cooliehat.
	 * @param i_coolieHatNumb int, the number of the cooliehat to return
	 * @return ControllCoolieHat, the cooliehat coresponding to the given number or name
	 * @example procontroll_cooliehat
	 * @shortdesc Use this method to get a Button.
	 * @related ControllDevice
	 * @related ControllCoolieHat
	 * @related getSlider ( )
	 * @related getStick ( )
	 * @related getButton ( )
	 */ 
	 public ControllCoolieHat getCoolieHat(final int i_coolieHatNumb){ 
		 return (ControllCoolieHat)buttons.get(i_coolieHatNumb); 
	 } 
	 
	 /**
	 *  @param i_sliderName String, name of the button to return
	 */
	public ControllCoolieHat getCoolieHat(final String i_buttonName){
		try{
			return(ControllCoolieHat)inputMap.get(i_buttonName);
		}catch (ClassCastException e){
		}
		throw new RuntimeException("There is no button with the name " + i_buttonName + ".");
	}
	
	/**
	 * Use this method to open a device. A device is automatically opened by
	 * default, so you only need to call this when you have closed it with the
	 * close method.
	 * @shortdesc Use this method to open a device.
	 * @related ControllDevice
	 */
	public void open(){
		open = true;
	}

	/**
	 * Use this method to close a device. A closed device does not to be updated
	 * to get values.
	 * @shortdesc Use this method to close a device.
	 * @related ControllDevice
	 */
	public void close(){
		open = false;
	}
	
	public void rumble(final float i_intensity, final int i_id){
		if(i_id >= rumblers.length)return;
		else rumblers[i_id].rumble(i_intensity);
	}
	
	public void rumble(final float i_intensity){
		rumble(i_intensity,0);
	}
	
	/**
	 * Plug is a handy method to handle incoming button events. To create a plug
	 * you have to implement a method that reacts on the events. To plug a method you
	 * need to give a device the method name, the event type you want to react on and
	 * the button. If your method is inside a class you have to give the plug
	 * a reference to it.
	 * @param i_object Object: the object with the method to plug
	 * @param i_methodName String: the name of the method that has to be plugged
	 * @param i_eventType constant: can be ControllIO.ON_PRESS, ControllIO.ON_RELEASE or ControllIO.WHILE_PRESS
	 * @param i_input int: the number of the button that triggers the plug
	 * @shortdesc Plugs a method to handle incoming button events.
	 */
	public void plug(
		final Object i_object, 
		final String i_methodName, 
		final int i_eventType,
		final int i_input
	){
		open();
		getButton(i_input).plug(i_object,i_methodName,i_eventType);
	}
	
	public void plug(
		final String i_methodName, 
		final int i_eventType,
		final int i_input
	){
		open();
		getButton(i_input).plug(parent,i_methodName,i_eventType);
	}
	
	/**
	 * 
	 * i_intputDevice String: the name of the device that triggers the plug
	 */
	public void plug(
		final Object i_object, 
		final String i_methodName, 
		final int i_eventType,
		final String i_input
	){
		open();
		getButton(i_input).plug(i_object,i_methodName,i_eventType);
	}
	
	public void plug(
		final String i_methodName, 
		final int i_eventType,
		final String i_input
	){
		open();
		getButton(i_input).plug(parent,i_methodName,i_eventType);
	}
}

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

import net.java.games.input.Component;
import processing.core.PApplet;

/**
 * The slider class is for analog input elements having a value
 * range. Normally this range goes from -1 to 1. You can set
 * a multiplier to increase this range, this is usefull so
 * that you do not have to change the values in your application.
 * You can get the actual value and the total value of a slider.
 * The actual value gives you the current state of the controller.
 * For the total value the actual values for each frame are add.
 * If you not want a slider to react upto a certain value you can set 
 * a tolerance value.
 * @example procontroll
 * @usage application
 * @related ControllDevice
 * @related ControllButton
 */
public class ControllSlider extends ControllInput{

	
	/**
	 * The total Value of the slider
	 */
	protected float totalValue = 0f;
	
	/**
	 * Tolerance is minimum under which the input is set to
	 * zero.
	 */
	protected float tolerance = 0f;
	
	/**
	 * The value of a slider is a relative value between
	 * -1.0f und 1.0f with the multiplier you can increase 
	 * and decrese this range.
	 */
	protected float multiplier = 1.f;
		
	/**
	 * Initializes a new Slider.
	 * @param i_component
	 */
	ControllSlider(final Component i_component){
		super(i_component);
	}
	
	/**
	 * For the total value the values for each frame are add.
	 * Use this method to get the total value of a slider.
	 * @return float, the total value of a slider
	 * @example procontroll_getTotalValue
	 * @usage application
	 * @shortdesc Use this method to get the total value of a slider.
	 * @related ControllSlider
	 * @related getValue ( )
	 * @related reset ( )
	 */
	public float getTotalValue(){
		return totalValue;
	}
	
	/**
	 * For the total value the actual values for each frame are add.
	 * Use this method to set the totalvalue to 0.
	 * @example procontroll_getTotalValue
	 * @usage application
	 * @shortdesc Use this method to set the totalvalue to 0.
	 * @related ControllSlider
	 * @related getTotalValue ( )
	 */
	public void reset(){
		totalValue = 0;
	}
	
	/**
	 * If you not want a slider to react upto a certain value you can set 
	 * a tolerance value. Use this method to retrieve the set tolerance.
	 * By default this value is set to 0.
	 * @return float, the tolerance of the slider
	 * @example procontroll_multiplier
	 * @usage application
	 * @shortdesc Use this method to get the actual tolerance
	 * @related ControllSlider
	 * @related setTolerance ( )
	 */
	public float getTolerance(){
		return tolerance;
	}
	
	/**
	 * If you not want a slider to react upto a certain value you can set 
	 * a tolerance value. Use this method to set the tolerance.
	 * By default this value is set to 0.
	 * @param i_tolerance float, the new tolerance for the slider
	 * @example procontroll
	 * @usage application
	 * @shortdesc Use this method to set the tolerance.
	 * @related ControllSlider
	 * @related getTolerance ( )
	 */
	public void setTolerance(final float i_tolerance){
		tolerance = i_tolerance;
	}
	
	/**
	 * The value of a slider is a relative value between
	 * -1.0f und 1.0f with the multiplier you can increase 
	 * and decrese this range. Use this method to get the
	 * actual multiplier. By default this value is 1.0.
	 * @return float, the actual multiplier for the slider
	 * @example procontroll_multiplier
	 * @usage application
	 * @shortdesc Use this method to get the actual multiplier.
	 * @related ControllSlider
	 * @related setMultiplier ( )
	 */
	public float getMultiplier(){
		return multiplier;
	}
	
	/**
	 * The value of a slider is a relative value between
	 * -1.0f und 1.0f with the multiplier you can increase 
	 * and decrese this range. Use this method to set the
	 * actual multiplier. By default this value is 1.0.
	 * @param i_multiplier float, the new multiplier for a Slider
	 * @example procontroll_multiplier
	 * @usage application
	 * @shortdesc Use this method to set the actual multiplier.
	 * @related ControllSlider
	 * @related getMultiplier ( )
	 */
	public void setMultiplier(final float i_multiplier){
		multiplier = i_multiplier;
	}
	
	/**
	 * Use this method to see if a slider is relative. A relative sliders
	 * value represents always the change between the current state and the last state.
	 * @return boolean, true if the slider is relative
	 */
	public boolean isRelative(){
		return component.isRelative();
	}
	
	/**
	 * This method is called before each frame to update the slider values.
	 */
	void update(){
		actualValue = component.getPollData();
		if(PApplet.abs(actualValue) < component.getDeadZone()+tolerance){
			actualValue = 0f;
		}else{
			actualValue = component.getPollData()*multiplier;
		}
		totalValue += actualValue;
	}
	
	void updateRelative(){
		
	}
}

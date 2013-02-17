/*
  Part of the GUI for Processing library 
  	http://www.lagers.org.uk/g4p/index.html
	http://gui4processing.googlecode.com/svn/trunk/
	
  Copyright (c) 2008-09 Peter Lager

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

package guicomponents;

import java.awt.event.MouseEvent;

import processing.core.PApplet;

/**
 * Abstract class to provide a slider - GHorzSlider and GVertSlider
 * inherit from this class.
 * 
 * @author Peter Lager
 *
 */
public abstract class GSlider extends GComponent {

	public static final int INTEGER = 0;
	public static final int DECIMAL = 1;
	public static final int EXPONENT = 2;
	
	/**
	 * These are the values that are supplied back to the user
	 */
	protected float init;
	protected float maxValue = 0;
	protected float minValue = 100;
	protected float value;

	// Indicates the type of value used in the display
	protected int _valueType = 0;

	/** 
	 * Pixel values relative to slider top left
	 */
	protected int thumbMin, thumbMax;
	// The position to display the thumb
	protected int thumbPos;
	// The final position for the thumb
	protected int thumbTargetPos;
	
	protected int thumbSize = 10;
	
	protected int thumbInertia = 1;
	
	protected int offset;
	
	protected boolean isValueChanging = false;
		
	/**
	 * Called by GHorzSlider and GVertSlider.
	 * 
	 * @param theApplet
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	public GSlider(PApplet theApplet, int x, int y, int width, int height){
		super(theApplet, x, y);
		this.width = width;
		this.height = height;
		z = Z_SLIPPY;
		registerAutos_DMPK(true, true, true, false);
		createEventHandler(G4P.mainWinApp, "handleSliderEvents", new Class[]{ GSlider.class });
	}

	public GSlider(PApplet theApplet, int x, int y) {
		super(theApplet, x, y);
	}

	/**
	 * The user can change the range and initial value of the 
	 * slider from the default values of range 0-100 and 
	 * initial value of 50.
	 * This method ignores inertia so the effect is immediate.
	 * 
	 * @param init
	 * @param min
	 * @param max
	 */
	public void setLimits(int init, int min, int max){
		minValue = Math.min(min, max);
		maxValue = Math.max(min, max);
		this.init = Math.round(PApplet.constrain((float)init, minValue, maxValue));
		
		if(thumbMax - thumbMin < maxValue - minValue && G4P.messages){
			System.out.println(getClass().getSimpleName()+".setLimits");
			System.out.println("  not all values in the range "+min+" - "+max+" can be returned");
			System.out.print("  either reduce the range or make the slider ");
			if(this.getClass().getSimpleName().equals("GHorzSlider")) 
				System.out.print("width");
			else
				System.out.print("height");
			System.out.println(" at least " + (max-min+thumbSize));
		}
		thumbTargetPos = thumbPos;
		// Set the value immediately ignoring inertia
		setValue(init, true);
		_valueType = INTEGER;
	}


	/**
	 * Sets the limits of the slider as float values. Converted to floats or integer depending
	 * on the type of the slider.
	 */
	public void setLimits(float init, float min, float max){
		minValue = Math.min(min, max);
		maxValue = Math.max(min, max);
		this.init = PApplet.constrain(init, minValue, maxValue);

		thumbTargetPos = thumbPos;
		// Set the value immediately ignoring inertia
		setValue(this.init, true);
		if(_valueType == INTEGER)
			_valueType = DECIMAL;
	}
	
	/**
	 * Override in child classes
	 */
	public void mouseEvent(MouseEvent event){
	}

	/**
	 * Override in child classes
	 *  
	 * @return always false
	 */
	public boolean isOver(int ax, int ay){
		return false;
	}
	
	/**
	 * Get the minimum slider value
	 * @return min value
	 */
	public int getMinValue() {
		return Math.round(minValue);
	}

	/**
	 * Get the maximum slider value
	 * @return max value
	 */
	public int getMaxValue() {
		return Math.round(maxValue);
	}

	/**
	 * Sets the type of slider that this should be. <br>
	 * INTEGER or DECIMAL or EXPONENT	 
	 */
	public void setValueType(int type){
		_valueType = type;
	}

	/**
	 * Get the type used for the slider value
	 * @return GSlider.INTEGER or GSlider.DECIMAL or GSlider.EXPONENT
	 */
	public int getValueType(){
		return _valueType;
	}
	
	/**
	 * Get the current value represented by the slider
	 * 
	 * @return current value
	 */
	public int getValue(){
		return Math.round(value);
	}
	
	/**
	 * Gets the current value of the slider. If the value type is integer
	 * then the value is rounded.
	 */
	public float getValuef(){
		if(_valueType == INTEGER)
			return Math.round(value);
		else
			return value;
	}


	/**
	 * Is the value changing as a result of the slider thumb being 
	 * dragged with the mouse.
	 * 
	 * @return true if value being changed at GUI
	 */
	public boolean isValueChanging() {
		return isValueChanging;
	}

	/**
	 * Sets the target value of the slider, if setInertia(x) has been used
	 * to implement inertia then the actual slider value will gradually
	 * change until it reaches the target value. The slider thumb is 
	 * always in the right position for the current slider value. <br>
	 * <b>Note</b> that events will continue to be generated so if this
	 * causes unexpected behaviour then use setValue(newValue, true)
	 * 
	 * @param newValue the value we wish the slider to become
	 */
	public void setValue(int newValue){
		value = PApplet.constrain(newValue, minValue, maxValue);
		thumbTargetPos = (int) PApplet.map(value, minValue, maxValue, thumbMin, thumbMax);
	}
	
	/**
	 * The same as setValue(newValue) except the second parameter determines 
	 * whether we should ignore any inertia value so the affect is immediate. <br>
	 * <b>Note</b> if false then events will continue to be generated so if this
	 * causes unexpected behaviour then use setValue(newValue, true)
	 * 
	 * @param newValue the value we wish the slider to become
	 * @param ignoreInteria if true change is immediate
	 */
	public void setValue(int newValue,  boolean ignoreInteria){
		setValue(newValue);
		if(ignoreInteria){
			thumbPos = thumbTargetPos;
		}
	}

	/**
	 * Sets the target value of the slider, if setInertia(x) has been 
	 * to implement inertia then the actual slider value will gradually
	 * change until it reaches the target value. The slider thumb is 
	 * always in the right position for the current slider value. <br>
	 * <b>Note</b> that events will continue to be generated so if this
	 * causes unexpected behaviour then use setValue(newValue, true)
	 * 
	 * @param newValue the value we wish the slider to become
	 */
	public void setValue(float newValue){
		value = PApplet.constrain(newValue, minValue, maxValue);
		thumbTargetPos = (int) PApplet.map(value, minValue, maxValue, thumbMin, thumbMax);
	}

	/**
	 * The same as setValue(newValue) except the second parameter determines 
	 * whether we should ignore any inertia value so the affect is immediate. <br>
	 * <b>Note</b> if false then events will continue to be generated so if this
	 * causes unexpected behaviour then use setValue(newValue, true)
	 * 
	 * @param newValue the value we wish the slider to become
	 * @param ignoreInteria if true change is immediate
	 */
	public void setValue(float newValue,  boolean ignoreInteria){
		setValue(newValue);
		if(ignoreInteria){
			thumbPos = thumbTargetPos;
		}
	}

	/**
	 * When dragging the slider thumb rapidly with the mouse a certain amount of 
	 * inertia will give a nice visual effect by trailing the thumb behind the
	 * mouse. A value of 1 (default) means the thumb is always in step with 
	 * the mouse. Increasing values will increase the amount of trailing and the
	 * length of time needed to reach the final value.
	 * I have found values around 10 give quite nice effect but much over 20 and
	 * you start to loose the gliding effect due to acceleration and deacceleration.
	 * 
	 * @param inertia values passed is constrained to the range 1-50.
	 */
	public void setInertia(int inertia){
		thumbInertia = PApplet.constrain(inertia, 1, 100);
	}

	/**
	 * Move thumb if not at desired position
	 */
	public void pre(){
		int change, inertia = thumbInertia;
		if(thumbPos == thumbTargetPos){
			isValueChanging = false;
		}
		else {
			// Make sure we get a change value by repeatedly decreasing the inertia value
			do {
				change = (thumbTargetPos - thumbPos)/inertia;
				inertia--;
			} while (change == 0 && inertia > 0);
			// If there is a change update the current value and generate an event
			if(change != 0){
				thumbPos += change;
				float newValue = PApplet.map(thumbPos, thumbMin, thumbMax, minValue, maxValue);
				boolean valueChanged = (newValue != value);
				value = newValue;
				if(valueChanged){
					eventType = CHANGED;
					fireEvent();
				}
			}
			else
				isValueChanging = false;
		}			
	}
		
	/**
	 * Override in child classes
	 */
	public void draw(){
	}
	
}

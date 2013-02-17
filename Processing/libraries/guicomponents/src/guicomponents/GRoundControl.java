/*
  Part of the GUI for Processing library 
  	http://www.lagers.org.uk/g4p/index.html
	http://gui4processing.googlecode.com/svn/trunk/

  Copyright (c) 2011 Peter Lager

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

import java.awt.Point;
import java.awt.event.MouseEvent;

import processing.core.PApplet;

/**
 * This is an abstract class that provides the core functionality including mouse event
 * handling for 'round controls' such as knobs. <br>
 * Round components include both circular and oval components.  <br><br>
 * 
 *  
 * @author Peter Lager
 *
 */
public abstract class GRoundControl extends GComponent {

	protected float cx,cy;

	protected float start,end;
	protected float sizeRadX, sizeRadY;

	protected boolean overRotArcOnly = false;

	/*	 
	 * These are the values of the start angle and end angle used to define
	 *  the limits of the clockwise rotation range. They are adjusted such 
	 *  that aLow < aHigh. If start angle > end angle then aLow is calculated as 
	 *  the equivalent negative rotation i.e. 
	 *  start angle = 110 and end angle = 70 then 
	 *  aLow = 360 - start angle = 360 - 110 = -250
	 */	 
	protected int aLow, aHigh;

	// Used to indicate if the arc goes over the 0 (east) position
	public boolean wrap0;

	// These angles are adjusted to be in range aLow to aHigh
	// this is updated in pre() and is used to calculate the current value
	// These angle can be in the range -360 - +360
	public int needleAngle, lastTargetNeedleAngle, targetNeedleAngle;
	protected int needleDir;

	// These angles are adjusted to be in the range 0-360
	protected int mouseAngle, lastMouseAngle;
	// When the mouse is pressed this measures the difference between
	// mouseAngle and targetNeedleAngle preventing discontinuous jumps
	// in the knob value. Offset is adjusted when the targetNeedleAngle
	// is stopped at either end of the slider again to prevent
	// discontinuous movement of the needle
	protected int offset;

	/*
	These represent the range of values that will be returned by the
	control. If start < end then the value will increase with
	clockwise rotation but if start>= end then the value increases 
	with counter-clockwise rotation.
	The control remembers which which the boolean.
	value = current value of the control
	lastValue = last value of the control
	the difference between these can be used to indicate the direction
	the user is attempting to rotate the control
	 */
	protected float valueStart = 20, valueEnd = 270;
	protected float value = 300;
	protected boolean isValueChanging;
	protected boolean clockwiseValues = (valueStart < valueEnd);

	// Provides inertia for the needle thereby smoothing the needle 
	// during rapid mouse movement. This value must be >= 1 although
	// there is no maximum value, values over 20 do not increase the
	// visual effect. A value of 1 means no inertia.s
	protected int inertia = 1;

	protected int mode = CTRL_HORIZONTAL;
	protected float sensitivity = 1.0f;
	protected int startMouseX, startMouseY;

	/**
	 * This constructor should be called by the appropriate child class constructor
	 */
	public GRoundControl(PApplet theApplet, int x, int y, int width, int height, int arcStart, int arcEnd) {
		super(theApplet, x, y);
		this.width = (width < 20) ? 20 : width;
		this.height = (height < 20) ? 20 : height;
		sizeRadX = cx =this.width/2;
		sizeRadY = cy = this.height/2;

		aLow = getValidArcAngle(arcStart);
		aHigh = getValidArcAngle(arcEnd);
		wrap0 = (arcStart > arcEnd);

		aLow = (aLow >= aHigh) ? aLow - 360 : aLow;

		start = PApplet.radians(aLow);
		end = PApplet.radians(aHigh);

		z = Z_SLIPPY;

		registerAutos_DMPK(true, true, true, false);
		setLimits(50,0,100);
	}

	/**
	 * This constructor should be called by the appropriate child class constructor
	 */
	public GRoundControl(PApplet theApplet, int x, int y, int size, int arcStart, int arcEnd) {
		this(theApplet, x, y, size, size, arcStart, arcEnd);
	}

	/**
	 * Used to implement inertia
	 */
	public void pre(){
		int change, nInertia = inertia;
		if(needleAngle == targetNeedleAngle){
			isValueChanging = false;
			needleDir = 0;
		}
		else {
			// Make sure we get a change value by repeatedly decreasing the inertia value
			do {
				change = (targetNeedleAngle - needleAngle)/nInertia;
				nInertia--;
			} while (change == 0 && nInertia > 0);
			// If there is a change update the current value and generate an event
			needleDir = signInt(change);
			if(change != 0){
				needleAngle += change;
				isValueChanging = true;
				fireEvent();
			}
			else
				isValueChanging = false;
		}			
	}

	/**
	 * Basic mouse event handler for all round components.
	 */
	public void mouseEvent(MouseEvent event){
		if(!visible  || !enabled) return;

		// Calculate absolute position of the centre of rotation
		Point p = new Point(0,0);
		calcAbsPosition(p);
		p.x += cx;
		p.y += cy;

		int degs = 0;

		boolean mouseOver;
		if(overRotArcOnly)
			mouseOver = isOverRotArc(winApp.mouseX, winApp.mouseY);
		else
			mouseOver = isOver(winApp.mouseX, winApp.mouseY);

		if(mouseOver || focusIsWith == this)
			cursorIsOver = this;
		else if(cursorIsOver == this)
			cursorIsOver = null;

		// Handle events
		switch(event.getID()){
		case MouseEvent.MOUSE_PRESSED:
			if(focusIsWith != this && mouseOver && z > focusObjectZ()){
				startMouseX = winApp.mouseX - p.x;
				startMouseY = winApp.mouseY - p.y;
				degs = getAngleFromUser(p);
				lastMouseAngle = mouseAngle = (degs < 0) ? degs + 360 : degs;
				offset = targetNeedleAngle - mouseAngle;
				takeFocus();
			}
			break;
		case MouseEvent.MOUSE_RELEASED:
			if(focusIsWith == this){
				loseFocus(null);
			}
			lastTargetNeedleAngle = targetNeedleAngle;	
			break;
		case MouseEvent.MOUSE_CLICKED:
			break;
		case MouseEvent.MOUSE_DRAGGED:
			if(focusIsWith == this){
				degs = getAngleFromUser(p);
				mouseAngle = (degs < 0) ? degs + 360 : degs;
				if(mouseAngle != lastMouseAngle){
					int deltaMangle = mouseAngle - lastMouseAngle;
					// correct when we go over zero degree position
					if(deltaMangle < -180)
						deltaMangle += 360;
					else if(deltaMangle > 180)
						deltaMangle -= 360;
					// Calculate and adjust new needle angle so it is in the range aLow >>> aHigh
					targetNeedleAngle = PApplet.constrain(targetNeedleAngle + deltaMangle, aLow, aHigh);
					// Update offset for use with angular mouse control
					offset += (targetNeedleAngle - lastTargetNeedleAngle - deltaMangle);
					// Remember target needle and mouse angles
					lastTargetNeedleAngle = targetNeedleAngle;
					lastMouseAngle = mouseAngle;
				}
				isValueChanging = true;
			}
			break;
		}
	}

	/**
	 * Override this method in child classes
	 * @param mouseX
	 * @param mouseY
	 * @return
	 */
	public abstract boolean isOverRotArc(int mouseX, int mouseY);

	/**
	 * Calculates the 'angle' from the current mouse position based on the type
	 * of 'controller' set.
	 * @param p the absolute pixel position for the control centre
	 * @return the unconstrained angle
	 */
	protected int getAngleFromUser(Point p){
		int degs = 0;
		switch(mode){
		case CTRL_ANGULAR:
			degs =  Math.round(PApplet.degrees(calcRealAngleFromXY(p,  winApp.mouseX, winApp.mouseY)));
			break;
		case CTRL_HORIZONTAL:
			degs = (int) (sensitivity * (winApp.mouseX - p.x - startMouseX));
			break;
		case CTRL_VERTICAL:
			degs = (int) (sensitivity * (winApp.mouseY - p.y - startMouseY));
			break;
		}
		return degs;
	}

	/**
	 * Get the real angle from an X/Y position
	 * @param p the absolute pixel position for the control centre
	 * @param x x coordinate
	 * @param y y coordinate
	 * @return real angle in radians (0 - 2 Pi)
	 */
	protected float calcRealAngleFromXY(Point p, float x, float y){
		float rads = (float)Math.atan2(y - p.y, x - p.x);
		rads = (rads < 0) ? rads + PApplet.TWO_PI : rads;
		return rads;
	}

	/**
	 * Is the value changing as a result of the knob being rotated  
	 * with the mouse.
	 * 
	 * @return true if value being changed at GUI
	 */
	public boolean isValueChanging(){
		return isValueChanging;
	}

	/**
	 * Get the current mouse controller mode possible values are <br>
	 * GKnob.CTRL_ANGULAR or GKnob.CTRL_HORIZONTAL) orGKnob.CTRL_VERTICAL
	 * @return the mode
	 */
	public int getControlMode() {
		return mode;
	}

	/**
	 * Set the mouse control mode to use, acceptable values are <br>
	 * GKnob.CTRL_ANGULAR or GKnob.CTRL_HORIZONTAL) orGKnob.CTRL_VERTICAL
	 * @param mode the mode to set
	 */
	public void setControlMode(int mode) {
		this.mode = mode;
	}

	/**
	 * This gets the sensitivity to be used in modes CTRL_HORIZONTAL and CTRL_VERTICAL
	 * @return the sensitivity
	 */
	public float getSensitivity() {
		return sensitivity;
	}

	/**
	 * This gets the sensitivity to be used in modes CTRL_HORIZONTAL and CTRL_VERTICAL <br>
	 * A value of 1 is 1 degree per pixel and a value of 2 is 2 degrees per pixel. <br>
	 * @param sensitivity the sensitivity to set
	 */
	public void setSensitivity(float sensitivity) {
		this.sensitivity = (sensitivity < 0.1f) ? 0.1f : sensitivity;
	}

	
	/**
	 * @return the inertia
	 */
	public int getInertia() {
		return inertia;
	}

	/**
	 * @param inertia the inertia to set
	 */
	public void setInertia(int inertia) {
		if(inertia >= 1)
			this.inertia = inertia;
	}

	/**
	 * Get the 'over rotation arc' option
	 */
	public boolean isMouseORA() {
		return overRotArcOnly;
	}

	/**
	 * If this is set to false (the default value) then the mouse button 
	 * can be pressed over any part of the knob and bzeel  to start rotating
	 * the knob. If it is true then only that portion of the knob within
	 * the rotation arc.
	 * 
	 * param overRotArcOnly true or false
	 */
	public void setMouseORA(boolean overRotArcOnly) {
		this.overRotArcOnly = overRotArcOnly;
	}

	/**
	 * Get the current value represented by the control as a floating point value.
	 * @return current float value
	 */
	public float getValuef(){
		if(clockwiseValues)
			return PApplet.map(needleAngle, aLow, aHigh, valueStart, valueEnd);
		else
			return -PApplet.map(needleAngle, aLow, aHigh, -valueStart, -valueEnd);
	}

	/**
	 * Get the current value represented by the control as an integer value.
	 * @return current integer value
	 */
	public int getValue(){
		return Math.round(getValuef());
	}

	/**
	 * Set the range of values that are to be returned by this control. <br>
	 * 
	 * 
	 * @param init initial value of control
	 * @param start value matching the start rotation
	 * @param end  value matching the start rotation (Values < start are acceptable)
	 */
	public void setLimits(float init, float start, float end)
	{
		valueStart = start;
		valueEnd = end;
		clockwiseValues = (start < end);
		setValue(init, true);
	}

	/**
	 * Set the current value of the control
	 * @param newValue the value to use will be constrained to legal vales.
	 */
	public void setValue(float newValue){
		if(clockwiseValues)
			newValue = PApplet.constrain(newValue, valueStart, valueEnd);
		else 
			newValue = PApplet.constrain(newValue, valueEnd, valueStart);
		targetNeedleAngle = getAngleFromValue(newValue);
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
		if(ignoreInteria)
			needleAngle = targetNeedleAngle;
	}

	/**
	 * Calculate the equivalent needle angle for a given value.
	 * 
	 * @param value a valid value for the knob range
	 * @return the angle that is equivalent to the given vale;
	 */
	protected int getAngleFromValue(float value){
		int angle;
		if(clockwiseValues)
			angle = (int) PApplet.map(value, valueStart, valueEnd, aLow, aHigh);
		else
			angle = (int) PApplet.map(-value, -valueStart, -valueEnd, aLow, aHigh);
		return angle;
	}

	/**
	 * Confirm if the angle is within the limits of rotation. <br>
	 * 
	 * @param angle must be in range 0-360 
	 * @return true if angle is within rotation angle range
	 */
	protected boolean isInValidArc(int angle){
		return (aLow < 0) ? (angle >= 360 + aLow || angle <= aHigh) : (angle >= aLow && angle <= aHigh);
	}

	/**
	 * Convert the angle to a positive value in the range 0 - 359
	 * without altering its 'slope' <br>
	 * Only used to validate initial values
	 * @param angle must be in range 0-360 
	 */
	protected int getValidArcAngle(int angle){
		while(angle < 0) angle+=360;
		while(angle > 360) angle-=360;
		return angle;
	}

	/**
	 * Get the sign of a number similar to signNum but returns zero when the number is 0
	 * rather than 1.
	 * 
	 * @param n
	 * @return 
	 */
	protected int signInt(int n){
		return (n == 0) ? 0 : (n < 0) ? -1 : +1;
	}

	/**
	 * Takes a real angle and calculates the angle to be used when
	 * drawing an arc so that they match up.
	 * @param ra the real world angle
	 * @return the angle for the arc method.
	 */
	protected float convertRealAngleToOvalAngle(double ra, float rX, float rY){
		double cosA = Math.cos(ra), sinA = Math.sin(ra);
		double h = Math.abs(rX - rY)/2.0;
		double eX = rX * cosA, eY = rY * sinA;

		if(rX > rY){
			eX -= h * cosA;
			eY += h * sinA;
		}
		else {
			eX += h * cosA;
			eY -= h * sinA;
		}
		float angle = (float) Math.atan2(eY, eX);
		while(ra - angle >= PI)
			angle += TWO_PI;
		while(angle - ra >= PI)
			angle -= TWO_PI;
		return angle;
	}

	/**
	 * Calculates the point of intersection between the circumference of an ellipse and a line from
	 * position xp, yp to the geometric centre of the ellipse.
	 * 
	 * @param circPos the returned intersection point
	 * @param xp x coordinate of point
	 * @param yp y coordinate of point
	 * @param rX half width of ellipse
	 * @param rY half height of ellipse
	 */
	protected void calcCircumferencePosition(Point circPos, float xp, float yp, float rX, float rY){
		double numer, denom;
		numer = rX * rY;
		denom = (float) Math.sqrt(rX*rX*yp*yp + rY*rY*xp*xp);
		circPos.x = (int) Math.round(xp * numer / denom);
		circPos.y = (int) Math.round(yp * numer / denom);
	}

}

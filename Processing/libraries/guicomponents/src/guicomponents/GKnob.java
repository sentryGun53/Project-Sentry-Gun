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

import processing.core.PApplet;

/**
 * The provides an extremely configurable GUI knob controller. GKnob
 * inherits from GRoundControl so you should read the documentation 
 * for that class as it also applies to GKnob. <br><br>
 * 
 * Configurable options <br>
 *  Knob size but it must be circular <br>
 *  Start and end of rotation arc. <br>
 *  Bezel width with tick marks <br>
 *  User defined value limits (i.e. the range of values returned <br>
 *  <br>
 *  Documentation for the following can be found in GRoundControl <br>
 *  Range of values associated with rotating the knob <br>
 *  Rotation is controlled by mouse movement -  3 modes available <br>
 *  (a) angular -  drag round knob center <br>
 *  (b) horizontal - drag left or right <br>
 *  (c) vertical - drag up or down <br>
 *  User can specify mouse sensitivity for modes (b) and (c)
 *  Use can specify level of inertia to give smoother rotation
 *  
 * 	<b>Note</b>: Angles are measured clockwise starting in the positive x direction i.e.
 * <pre>
 *         270
 *          |
 *    180 --+-- 0
 *          |
 *          90
 * </pre>
 * 
 * @author Peter Lager
 *
 */
public class GKnob extends GRoundControl {

	protected int nbrTickMarks = 2;
	protected Point[][] mark;
	protected int bezelWidth;
	protected int knobRadX, knobRadY, barRadX, barRadY;

	protected boolean valueTrackVisible = true;
	protected boolean rotArcOnly = false;

	/**
	 * Create a GKnob control <br><br>
	 * 
	 * Will ensure that width and height are >= 20 pixels <br>
	 * 
	 * The arcStart and arcEnd represent the limits of rotation expressed in 
	 * degrees as shown above. For instance if you want a knob that rotates
	 * from 7 o'clock to 5 o'clock (via 12 o'clock) then arcStart = 120 and
	 * arcEnd = 60 degrees.
	 * 
	 * @param theApplet
	 * @param x left position of knob
	 * @param y top position of knob
	 * @param width width of knob
	 * @param height height of knob (if different from width - oval knob)
	 * @param arcStart start of rotation arc (in degrees)
	 * @param arcEnd end of rotation arc (in degrees)
	 */
	protected GKnob(PApplet theApplet, int x, int y, int width, int height,
			int arcStart, int arcEnd) {
		super(theApplet, x, y, width, height, arcStart, arcEnd);

		// Calculate an acceptable bezel width based on initial size
		bezelWidth = (int) Math.max(Math.min(sizeRadX, sizeRadY)/3, 4);
		calculateSizes(bezelWidth);

		// Calculate ticks
		calcTickMarkerPositions(nbrTickMarks);
		createEventHandler(G4P.mainWinApp, "handleKnobEvents", new Class[]{ GKnob.class });
	}

	/**
	 * Create a GKnob control <br><br>
	 * 
	 * Will ensure that width and height are >= 20 pixels <br>
	 * 
	 * The arcStart and arcEnd represent the limits of rotation expressed in 
	 * degrees as shown above. For instance if you want a knob that rotates
	 * from 7 o'clock to 5 o'clock (via 12 o'clock) then arcStart = 120 and
	 * arcEnd = 60 degrees.
	 * 
	 * @param theApplet
	 * @param x left position of knob
	 * @param y top position of knob
	 * @param size size of knob (will be round)
	 * @param arcStart start of rotation arc (in degrees)
	 * @param arcEnd end of rotation arc (in degrees)
	 */
	public GKnob(PApplet theApplet, int x, int y, int size,
			int arcStart, int arcEnd) {
		this(theApplet, x, y, size, size, arcStart, arcEnd);
	}

	/**
	 * Used internally to calculate various radii based on the specified bezel width that
	 * will be used in drawing the knob. <br>
	 * 
	 * The supplied bezel width will be constrained between 0 and half the smaller of
	 * width and height. <br>
	 * 
	 * @param bw the width of the bezel
	 */
	protected void calculateSizes(int bw){
		bezelWidth = (int) PApplet.constrain(bw, 0, Math.min(sizeRadX, sizeRadY));
		knobRadX = (int) (sizeRadX - bezelWidth);
		knobRadY = (int) (sizeRadY - bezelWidth);
		if(knobRadX <=0 || knobRadY <= 0){
			knobRadX = knobRadY = 0;
			int inset = Math.min(Math.round(0.2f * bezelWidth), 10);
			barRadX = (int) (sizeRadX - inset);
			barRadY = (int) (sizeRadY - inset);
		}
		else {
			barRadX = Math.round(0.5f * (sizeRadX + knobRadX));
			barRadY = Math.round(0.5f * (sizeRadY + knobRadY));			
		}
	}

	/**
	 * Used to calculate the tick mark positions 
	 * @param nticks
	 */
	protected void calcTickMarkerPositions(int nticks){
		mark = new Point[nticks][2];
		for(int i = 0; i < nticks ; i++){
			mark[i][0] = new Point();
			mark[i][1] = new Point();
		}
		float deltaAng = (aHigh - aLow) / ((float)(nticks-1));
		float cosine, sine;
		for(int i = 0; i < nticks ; i++){
			cosine = (float) Math.cos(PApplet.radians(aLow + i * deltaAng));
			sine = (float) Math.sin(PApplet.radians(aLow + i * deltaAng));
			mark[i][0].x = (int) Math.round(knobRadX * cosine);
			mark[i][0].y = (int) Math.round(knobRadY * sine);
			if(i == 0 || i == nticks - 1){
				mark[i][1].x = (int) Math.round(sizeRadX * cosine);
				mark[i][1].y = (int) Math.round(sizeRadY * sine);
			}
			else {
				mark[i][1].x = (int) Math.round(barRadX * cosine);
				mark[i][1].y = (int) Math.round(barRadY * sine);
			}
		}
		nbrTickMarks = nticks;
	}

	/**
	 * Set the width of the bezel.
	 * @param bw desired bezel width in pixels.
	 */
	public void setBezelWidth(int bw){
		calculateSizes(bw);
		calcTickMarkerPositions(nbrTickMarks);
	}

	
	/**
	 * @return the bezelWidth
	 */
	public int getBezelWidth() {
		return bezelWidth;
	}

	/**
	 * Determines whether the value track is shown or not
	 * @param visible true or false
	 */
	public void setValueTrackVisible(boolean visible){
		valueTrackVisible = visible;
	}

	/**
	 * See if the value track is visible
	 */
	public boolean isValueTrackVisible(){
		return valueTrackVisible;
	}

	/**
	 * Set the number of tick spaces for the bezel. <br>
	 * 
	 * @param nbr_spaces number of spaces between ticks
	 */
	public void setNbrTickSpaces(int nbr_spaces){
		setNbrTickMarks(nbr_spaces + 1);
	}

	/**
	 * Set the number of tick markers
	 * @param nbr_ticks
	 */
	public void setNbrTickMarks(int nbr_ticks){
		nbr_ticks = (nbr_ticks < 2) ? 2 : nbr_ticks;
		calcTickMarkerPositions(nbr_ticks);
	}

	/**
	 * Determines whether the position ax, ay is over any part of the round control.
	 * 
	 * @param ax x coordinate
	 * @param ay y coordinate
	 * @return true if mouse is over the control else false
	 */
	public boolean isOver(int ax, int ay){
		Point p = new Point(0,0);
		calcAbsPosition(p);
		boolean inside;
		int dx = (int) (ax - p.x - cx);
		int dy = (int) (ay - p.y - cy);
		inside = (dx * dx  + dy * dy < sizeRadX * sizeRadY);
		return inside;
	}

	/**
	 * Determines if the position is over the round control and within the rotation range.
	 * @param ax x coordinate
	 * @param ay y coordinate
	 * @return true if mouse is over the rotation arc of the control else false
	 */
	public boolean isOverRotArc(int ax, int ay){
		Point p = new Point(0,0);
		calcAbsPosition(p);
		p.x += cx;
		p.y += cy;
		boolean inside = false;
		int dx = ax - p.x;
		int dy = ay - p.y;
		inside = (dx * dx  + dy * dy < sizeRadX * sizeRadY);
		if(inside){
			int degs = Math.round(PApplet.degrees(calcRealAngleFromXY(p, ax, ay)));
			inside = isInValidArc(degs);
		}
		return inside;
	}

	/**
	 * If this is set to false (the default value) then the mouse button 
	 * can be pressed over any part of the knob and bezel  to start rotating
	 * the knob. If it is true then only that portion of the knob within
	 * the rotation arc. <br>
	 * It can only be changed if 
	 * param strict the strictOver to set
	 */
	public void setMouseORA(boolean strict) {
		if(strict || (!strict && !rotArcOnly))
			overRotArcOnly = strict;
		//		else if(!rotArcOnly )
		//			strictOver = strict;
	}

	/**
	 * @return the rotArcOnly
	 */
	public boolean isRotArcOnly() {
		return rotArcOnly;
	}

	/**
	 * @param rotArcOnly the rotArcOnly to set
	 */
	public void setRotArcOnly(boolean rotArcOnly) {
		this.rotArcOnly = rotArcOnly;
		if(rotArcOnly)
			overRotArcOnly = true;
	}

	/**
	 * Draw the knob
	 */
	public void draw(){
		if(!visible) return;

		Point p = new Point(0,0);
		calcAbsPosition(p);
		p.x += cx;
		p.y += cy;

		float rad = PApplet.radians(needleAngle);

		winApp.pushMatrix();
		winApp.translate(p.x, p.y);
		winApp.pushStyle();
		winApp.style(G4P.g4pStyle);

		// Draw bezel
		if(bezelWidth > 0){
			winApp.noStroke();
			if(rotArcOnly)
				winApp.fill(winApp.color(128,128));
			else {
				winApp.fill(winApp.color(128,48));
				winApp.ellipse(0, 0, 2*sizeRadX, 2*sizeRadY);
				winApp.fill(winApp.color(128,80));
			}
			// draw darker arc for rotation range
			winApp.arc(0, 0, 2*sizeRadX, 2*sizeRadY, start, end);

			// Draw active track
			if(valueTrackVisible){
				winApp.fill(localColor.knobTrack);
				winApp.noStroke();
				winApp.arc(0, 0, 2*barRadX, 2*barRadY, start, rad);
			}

			// Draw ticks
			winApp.stroke(localColor.knobBorder);
			winApp.strokeWeight(1.2f);
			for(int i = 0; i < mark.length; i++){
				if(i == 0 || i == mark.length-1)
					winApp.strokeWeight(1.5f);
				else
					winApp.strokeWeight(1.2f);
				winApp.line(mark[i][0].x, mark[i][0].y,mark[i][1].x, mark[i][1].y);
			}
		}
		if(knobRadX > 0 ){
			// Draw knob centre
			winApp.stroke(localColor.knobBorder);
			winApp.strokeWeight(1.2f);
			winApp.fill(localColor.knobFill);
			if(rotArcOnly){
				winApp.arc(0, 0, 2*knobRadX, 2*knobRadY, start, end);
				winApp.stroke(localColor.knobBorder);
				winApp.strokeWeight(1.2f);
				winApp.line(0, 0, mark[0][0].x, mark[0][0].y);
				winApp.line(0, 0, mark[mark.length-1][0].x, mark[mark.length-1][0].y);			
			}
			else	
				winApp.ellipse(0, 0, 2*knobRadX, 2*knobRadY);

			// Draw needle
			winApp.stroke(localColor.knobNeedle);
			winApp.strokeWeight(1.5f);
			winApp.line(0, 0,
					Math.round((sizeRadX - bezelWidth) * Math.cos(rad)),
					Math.round((sizeRadY - bezelWidth) * Math.sin(rad)) );
		}
		winApp.popStyle();
		winApp.popMatrix();
	}

}

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
 * The provides an extremely configurable GUI knob controller. GKnobOval
 * inherits from GKnob which inherits from GRoundControl so you should 
 * read the documentation for those classes as it also applies to 
 * GKnobOval.<br>
 * <br>
 * 
 * For circular knobs use GKnob rather than this class. <br>
 * To avoid inaccuracies when drawing the bezel arcs it is recommended that
 * the length of the major axis should not exceed 1.5 x the minor axis and/or
 * the bezel width is kept small. <br><br>
 * 
 * Configurable options <br>
 *  Knob height and width (should be oval) <br>
 *  Start and end of rotation arc. <br>
 *  Bezel width with tick marks <br>
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
public class GKnobOval extends GKnob {

	protected Point p = new Point();

	/**
	 * Create a GKnobOval control <br><br>
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
	public GKnobOval(PApplet theApplet, int x, int y, int width, int height,
			int arcStart, int arcEnd) {
		super(theApplet, x, y, width, height, arcStart, arcEnd);

		// Calculate the display angle
		start = convertRealAngleToOvalAngle(PApplet.radians(aLow), sizeRadX, sizeRadY);
		end = convertRealAngleToOvalAngle(PApplet.radians(aHigh), sizeRadX, sizeRadY);
		// Calculate ticks
		calcTickMarkerPositions(nbrTickMarks);
	}

	public void draw(){
		if(!visible) return;

		p.move(0,0);
		calcAbsPosition(p);
		p.x += cx;
		p.y += cy;

		float rad = PApplet.radians(needleAngle), nrad;

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

			// Draw active value arc
			if(valueTrackVisible){
				winApp.fill(localColor.knobTrack);
				winApp.noStroke();
				nrad = convertRealAngleToOvalAngle(rad, sizeRadX, sizeRadY);
				winApp.arc(0, 0, 2*barRadX, 2*barRadY, start, nrad);
			}

			// Draw ticks
			winApp.stroke(localColor.knobBorder);
			winApp.stroke(1.2f);
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
			winApp.strokeWeight(2.0f);
			nrad = convertRealAngleToOvalAngle(rad, sizeRadX, sizeRadY);
			float ox = (float) (sizeRadX * Math.cos(nrad));
			float oy = (float) (sizeRadY * Math.sin(nrad));	
			calcCircumferencePosition(p, ox, oy, knobRadX, knobRadY);
			winApp.line(0, 0, p.x, p.y);
		}
		winApp.popStyle();
		winApp.popMatrix();
	}

	/**
	 * Determines whether the position ax, ay is over any part of the round control.
	 * 
	 * @param ax x coordinate
	 * @param ay y coordinate
	 * @return true if mouse is over the control else false
	 */
	public boolean isOver(int ax, int ay){
		//		Point p = new Point(0,0);
		p.move(0,0);
		calcAbsPosition(p);
		boolean inside;
		int dx = (int) (ax - p.x - cx);
		int dy = (int) (ay - p.y - cy);
		float ratioX = ((float)dx)/ sizeRadX;
		float ratioY = ((float)dy)/ sizeRadY;
		inside = (ratioX * ratioX + ratioY * ratioY < 1.0f);
		return inside;
	}

	/**
	 * Determines if the position is over the round control and within the rotation range.
	 * @param ax x coordinate
	 * @param ay y coordinate
	 * @return true if mouse is over the rotation arc of the control else false
	 */
	public boolean isOverRotArc(int ax, int ay){
		p.move(0,0);
		calcAbsPosition(p);
		boolean inside;
		int dx = (int) (ax - p.x - cx);
		int dy = (int) (ay - p.y - cy);
		float ratioX = ((float)dx)/ sizeRadX;
		float ratioY = ((float)dy)/ sizeRadY;
		inside = (ratioX * ratioX + ratioY * ratioY < 1.0f);
		if(inside){
			Point pm = new Point(Math.round(dx), Math.round(dy));
			float eX = pm.x, eY = pm.y;
			calcCircumferencePosition(pm, eX, eY, sizeRadX, sizeRadY);
			// Get real angle to knob centre
			double angle = Math.atan2(dy,dx);
			double cosA = Math.cos(angle), sinA = Math.sin(angle);

			double h = Math.abs(sizeRadX - sizeRadY)/2.0;
			if(width > height){
				eX -= h * cosA;
				eY += h * sinA;
			}
			else {
				eX += h * cosA;
				eY -= h * sinA;
			}
			int degs = Math.round(PApplet.degrees((float) Math.atan2(eY, eX)));
			degs = (degs < 0) ? degs + 360 : degs;
			inside = isInValidArc(degs);
		}
		return inside;
	}

	/**
	 * Used to calculate the tick mark positions 
	 * @param nticks the number of actual markers
	 */
	protected void calcTickMarkerPositions(int nticks){
		mark = new Point[nticks][2];
		float ox, oy;
		float ang = PApplet.radians(aLow), deltaAng = PApplet.radians(aHigh - aLow)/(nticks-1);
		for(int i = 0; i < nticks ; i++){
			mark[i][0] = new Point();
			mark[i][1] = new Point();
			float dang = convertRealAngleToOvalAngle(ang, sizeRadX, sizeRadY);
			ox = (float) (sizeRadX * Math.cos(dang));
			oy = (float) (sizeRadY * Math.sin(dang));	
			calcCircumferencePosition(mark[i][0], ox, oy, knobRadX, knobRadY);
			if(i == 0 || i == nticks - 1){
				mark[i][1].x = Math.round(ox);
				mark[i][1].y = Math.round(oy);
			}
			else 
				calcCircumferencePosition(mark[i][1], ox, oy, barRadX, barRadY);
			ang += deltaAng;
		}
		nbrTickMarks = nticks;
	}

}

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

import java.awt.Point;
import java.awt.event.MouseEvent;

import processing.core.PApplet;

/**
 * The horizontal slider component.
 * 
 * @author Peter Lager
 *
 */
public class GHorzSlider extends GSlider {

	/**
	 * Create a horizontal slider.
	 * Default values:
	 * 		Range 0-100
	 *      Initial value 50
	 * Use the setLimits method to customise these values.
	 * 
	 * @param theApplet
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	public GHorzSlider(PApplet theApplet, int x, int y, int width, int height){
		super(theApplet, x, y, width, height);
		initThumbDetails();
		z = Z_SLIPPY;
	}

	/**
	 * Initialises the thumb details
	 */
	protected void initThumbDetails(){
		thumbSize = (int) Math.max(20, width / 20);
		thumbMin = thumbSize/2;
		thumbMax = (int) (width - thumbSize/2);
		thumbTargetPos = thumbPos;
	}

	/**
	 * Draw the slider
	 */
	public void draw(){
		if(!visible) return;

		winApp.pushStyle();
		winApp.style(G4P.g4pStyle);
		Point pos = new Point(0,0);
		calcAbsPosition(pos);
		winApp.noStroke();
		winApp.fill(localColor.sdrTrack);
		winApp.rect(pos.x, pos.y, width, height);
		winApp.fill(localColor.sdrThumb);
		winApp.rect(pos.x + thumbPos - thumbSize/2, pos.y, thumbSize, height);
		if(border != 0){
			winApp.strokeWeight(border);
			winApp.noFill();
			winApp.stroke(localColor.sdrBorder);
			winApp.rect(pos.x, pos.y, width, height);
		}
		winApp.popStyle();

	}

	/**
	 * All GUI components are registered for mouseEvents
	 */
	public void mouseEvent(MouseEvent event){
		if(!visible  || !enabled) return;

		boolean mouseOver = isOver(winApp.mouseX, winApp.mouseY);
		if(mouseOver || focusIsWith == this)
			cursorIsOver = this;
		else if(cursorIsOver == this)
			cursorIsOver = null;

		switch(event.getID()){
		case MouseEvent.MOUSE_PRESSED:
			if(focusIsWith != this && mouseOver && z > focusObjectZ()){
				mdx = winApp.mouseX;
				mdy = winApp.mouseY;
				takeFocus();
			}
			break;
		case MouseEvent.MOUSE_CLICKED:
			if(focusIsWith == this){
				loseFocus(null);
				mdx = mdy = Integer.MAX_VALUE;
			}
			break;
		case MouseEvent.MOUSE_RELEASED:
			if(focusIsWith == this && mouseHasMoved(winApp.mouseX, winApp.mouseY)){
				loseFocus(null);
				mdx = mdy = Integer.MAX_VALUE;
			}
			break;
		case MouseEvent.MOUSE_DRAGGED:
			if(focusIsWith == this){
				isValueChanging = true;
				Point p = new Point(0,0);
				calcAbsPosition(p);
				thumbTargetPos = PApplet.constrain(winApp.mouseX - offset - p.x, thumbMin, thumbMax);
			}
			break;
		}
	}

	/**
	 * Determines whether the position ax, ay is over the thumb
	 * of this Slider.
	 * 
	 * @return true if mouse is over the slider thumb else false
	 */
	public boolean isOver(int ax, int ay){
		Point p = new Point(0,0);
		calcAbsPosition(p);
		if(ax >= p.x + thumbPos - thumbSize/2 && ax <= p.x + thumbPos + thumbSize/2 && ay >= p.y && ay <= p.y + height){
			offset = ax - (p.x + thumbPos);
			return true;
		}
		else 
			return false;
	}

} // end of class

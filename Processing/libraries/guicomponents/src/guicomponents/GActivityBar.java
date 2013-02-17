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

import processing.core.PApplet;

/**
 * A simple animated bar that can be used to show that the program is working
 * might be useful when loading large files etc.
 * 
 * The user can set a time limit for the bar to stay visible, or use the stop()
 * method to stop and halt the bar
 * 
 * @author Peter Lager
 *
 */
public class GActivityBar extends GComponent {

	final protected int NBR_THUMBS = 4;
	
	protected int[] thumbX = new int[NBR_THUMBS];
	protected int[] thumbCol = new int[NBR_THUMBS];
	protected int[] thumbDeltaX = new int[NBR_THUMBS];
	protected int thumbY, thumbDiameter;
	protected int trackHeight;
	protected GTimer timer;
	protected long duration; // millisecs
	
	/**
	 * Use this ctor to create the GActivityBar object.
	 * 
	 * The minimum height is 10 pixels and is rounded up to the nearest multiple of 2. <br>
	 * Next the width is checked as it must be >= 4 * height (the width is increased
	 * if necessary)
	 * 
	 * @param theApplet
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	public GActivityBar(PApplet theApplet, int x, int y, int width, int height){
		super(theApplet,x,y);
		this.height = Math.max(height,10);
		if(this.height % 2 != 0)
			this.height++;
		trackHeight = (int) (this.height - 2 * PADV);
		this.width = Math.max(this.height * 4, width);
		initThumbPos();
		initThumbColor();
		timer = new GTimer(winApp, this , "update", 5);
		timer.stop();
		visible = false;
		registerAutos_DMPK(true,false,false,false);
	}
	
	/**
	 * Initialise the thumb colors
	 */
	private void initThumbColor() {
		thumbCol[0] = localColor.acbFirst;
		thumbCol[NBR_THUMBS-1] = localColor.acbLast;
		for(int i = 1; i < NBR_THUMBS - 1; i++)
			thumbCol[i]= PApplet.lerpColor(thumbCol[0], thumbCol[NBR_THUMBS-1], ((float)i)/(NBR_THUMBS-1), PApplet.HSB);
	}

	/**
	 * Initialise the stating position for the thumbs
	 */
	private void initThumbPos(){
		thumbX[0] = (int) (width/2 + NBR_THUMBS * trackHeight /2);
		thumbDeltaX[0] = 1;
		for(int i=1; i<NBR_THUMBS; i++){
			thumbX[i] = thumbX[i-1]- 1*trackHeight/2;
			thumbDeltaX[i] = 1;
		}
	}
	
	/**
	 * Start the GActivityBar animation and let it run for a
	 * maximum duration of @aseconds
	 * @param seconds if <= 0.0 then never stop
	 */
	public void start(float seconds){
		if(seconds <= 0.0f)
			duration = Long.MAX_VALUE;
		else
			duration = (long)seconds * 1000;
		initThumbPos();
		visible = true;
		timer.start();
	}
	
	/**
	 * Stop the animation and make the GActivityBar invisible
	 */
	public void stop(){
		visible = false;
		timer.stop();
	}
	
	/**
	 * Update the position of the thumbs
	 */
	public void update(){
		if(duration > 0){
			duration -= timer.getInterval();
			for(int i=0; i < NBR_THUMBS; i++){
				if(thumbX[i] == 0 || thumbX[i] == width)
					thumbDeltaX[i] = -thumbDeltaX[i];
				thumbX[i] += this.thumbDeltaX[i];
			}
		}
		else {
			visible = false;
			timer.stop();
		}
	}

	/**
	 * Sets the local color scheme
	 * @param schemeNo
	 */
	public void setColorScheme(int schemeNo){
		localColor = GCScheme.getColor(winApp, schemeNo);
		this.initThumbColor();
	}

	/**
	 * Draw the GActivityBar
	 */
	public void draw(){
		if(!visible) return;
		winApp.pushStyle();
		winApp.style(G4P.g4pStyle);
		Point pos = new Point(0,0);
		calcAbsPosition(pos);
		winApp.ellipseMode(PApplet.CENTER);
		winApp.rectMode(PApplet.CORNER);
		winApp.noStroke();
		winApp.fill(localColor.acbBorder);
		winApp.ellipse(pos.x, pos.y + height/2, height, height);
		winApp.ellipse(pos.x + width, pos.y + height/2, height, height);
		winApp.rect(pos.x, pos.y, width, height);
		winApp.fill(localColor.acbTrack);
		winApp.ellipse(pos.x, pos.y + height/2, trackHeight, trackHeight);
		winApp.ellipse(pos.x + width, pos.y + height/2, trackHeight, trackHeight);
		winApp.rect(pos.x, pos.y + PADV, width, trackHeight);
		for(int i = NBR_THUMBS-1; i >= 0; i--){
			winApp.fill(thumbCol[i]);
			winApp.ellipse(pos.x + thumbX[i], pos.y + height/2, trackHeight, trackHeight);
		}
		winApp.popStyle();
	}

}

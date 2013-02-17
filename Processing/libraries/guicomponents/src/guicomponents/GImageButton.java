/*
  Part of the GUI for Processing library 
  	http://www.lagers.org.uk/g4p/index.html
	http://gui4processing.googlecode.com/svn/trunk/

  Copyright (c) 2010 Peter Lager

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
import processing.core.PImage;

/**
 * Buttons create from this class use a number of images to represent it's 
 * state. This means that buttons can have an irregular and/or discontinuous
 * shape. <br>
 * 
 * The image button needs 1 to 3 image files to represent the button states <br>
 * OFF mouse is not over button <br>
 * OVER mouse is over the button <br>
 * DOWN the mouse is over the button and a mouse button is being pressed. <br>
 * 
 * If you only provide one image then this will be used for all states, if you
 * provide two then the second image is used for both  OVER and DOWN states. <br><br>
 * 
 * Rather than separate files for the different image states you can provide a 
 * single image file which is a composite of 1-3 images (tiled horizontally)
 * which are used for the different button states OFF, OVER and DOWN <br><br>
 * 
 * 
 * If you don't provide a mask file then the button 'hotspot' is represented by any
 * non-transparent pixels in the OFF image. If you do provide a mask file then the 
 * hotspot is defined by any white pixels in the mask image. <br><br>
 * 
 * 
 * Three types of event can be generated :-  <br>
 * <b> PRESSED  RELEASED  CLICKED </b><br>
 * 
 * To simplify event handling the button only fires off CLICKED events 
 * if the mouse button is pressed and released over the button face 
 * (the default behaviour). <br>
 * 
 * Using <pre>button1.fireAllEvents(true);</pre> enables the other 2 events
 * for button <b>button1</b>. A PRESSED event is created if the mouse button
 * is pressed down over the button face, the CLICKED event is then generated 
 * if the mouse button is released over the button face. Releasing the 
 * button off the button face creates a RELEASED event. <br>
 * 
 * 
 * @author Peter Lager
 *
 */
public class GImageButton extends GComponent {

	// Button status values
	public static final int OFF		= 0;
	public static final int OVER	= 1;
	public static final int DOWN	= 2;

	protected static PImage noImage[] = null;
	
	protected int status;

	protected PImage[] bimage = new PImage[3];
	protected PImage mask;
	
	protected boolean reportAllButtonEvents = false;
	
	/**
	 * Create an image button based on a composite image for the button states.
	 * 
	 * @param theApplet
	 * @param maskFile null if none is to be provided
	 * @param imgFile composite image file for button sates
	 * @param nbrImages number of images in composite image
	 * @param x top-left horizontal distance for the buttun
	 * @param y top left vertical position for the buttun
	 */
	public GImageButton(PApplet theApplet, String maskFile, String imgFile, int nbrImages, int x, int y){
		super(theApplet, x, y);
		mask = getMask(maskFile);
		bimage = getImages(imgFile, nbrImages);
		width = bimage[0].width;
		height = bimage[0].height;
		z = Z_SLIPPY;
		createEventHandler(G4P.mainWinApp, "handleImageButtonEvents", new Class[]{ GImageButton.class });
		registerAutos_DMPK(true, true, false, false);
	}

	/**
	 * 
	 * @param theApplet
	 * @param maskFile null if none is to be provided
	 * @param imgFiles an array of filenames for button state images
	 * @param x top-left horizontal distance for the button
	 * @param y top left vertical position for the button
	 */
	public GImageButton(PApplet theApplet, String maskFile, String imgFiles[], int x, int y){
		super(theApplet, x, y);
		mask = getMask(maskFile);
		bimage = getImages(imgFiles);
		width = bimage[0].width;
		height = bimage[0].height;
		z = Z_SLIPPY;
		createEventHandler(G4P.mainWinApp, "handleImageButtonEvents", new Class[]{ GImageButton.class });
		registerAutos_DMPK(true, true, false, false);
	}
	
	/**
	 * Get the images from a composite image file.
	 * 
	 * @param imgFile
	 * @param nbrImages
	 * @return
	 */
	protected PImage[] getImages(String imgFile, int nbrImages){
		nbrImages = PApplet.constrain(nbrImages, 1, 3);

		PImage[] imgs = new PImage[3];
		PImage img = winApp.loadImage(imgFile);

		if(imgFile == null || img == null){
			missingFile(imgFile);
			imgs = getErrorImage();
		}
		else {
			int iw = img.width / nbrImages;
			for(int i = 0; i < nbrImages;  i++){
				imgs[i] = new PImage(iw, img.height, ARGB);
				imgs[i].copy(img, 
						i * iw, 0, iw, img.height,
						0, 0, iw, img.height);
			}
			// Re use images if less than 3 were provided
			for(int i = nbrImages; i < 3; i++){
				imgs[i] = imgs[nbrImages - 1];
			}
		}
		return imgs;
	}

	/**
	 * Get the images specified in the file list
	 * @param imgFiles
	 * @return
	 */
	protected PImage[] getImages(String[] imgFiles){
		PImage[] imgs = new PImage[3];
		int imgCount = 0;
		if(imgFiles == null || imgFiles.length < 1){
			if(G4P.messages)
				System.out.println("Error: you have not provided a list of image files for GImageButton");
		}
		else {
			for(imgCount = 0; imgCount < imgFiles.length;  imgCount++){
				imgs[imgCount] = winApp.loadImage(imgFiles[imgCount]);
				if(imgs[imgCount] == null)
					missingFile(imgFiles[imgCount]);
			}
			//Make sure we have 3 images to work with
			for(int j = imgCount; j < 3; j++)
				imgs[j] = imgs[j - 1];
		}
		if(imgs[0] == null)
			imgs = getErrorImage();
		return imgs;
	}
	
	/**
	 * Get the mask file. Report an error if the file cannot be found.
	 * @param mfile
	 * @return
	 */
	protected PImage getMask(String mfile){
		PImage img = null;
		if(mfile != null){			
			img = winApp.loadImage(mfile);
			if(img == null)
				missingFile(mfile);
		}
		return img;
	}
	
	/**
	 * Report a missing file
	 * @param fname
	 */
	protected void missingFile(String fname){
		if(G4P.messages)
			System.out.println("\nUnable to locate file '"+ fname+"' for GImageButton");
	}
	
	/**
	 * Get the the error button images
	 * @return
	 */
	protected PImage[] getErrorImage(){
		if(noImage == null)
			noImage = getImages("noimage3.png",3);
		return noImage;
	}

	/**
	 * Determines whether the position ax, ay is over this component.
	 * It will use a mask image if one has been provided otherwise
	 * it will look for non-transparent pixels. 
	 * @param ax mouse x position
	 * @param ay mouse y position
	 * @return true if mouse is over the component else false
	 */
	public boolean isOver(int ax, int ay){
		Point p = new Point(0,0);
		calcAbsPosition(p);
		if(ax >= p.x && ax <= p.x + width && ay >= p.y && ay <= p.y + height){
			int dx, dy, pixel;
			dx = ax - p.x;
			dy = ay - p.y;
			if(mask != null){	// we have a mask file
				pixel = mask.get(dx, dy) & 0x00ffffff;
				// test for white (ignoring alpha value)
				if(pixel == 0x00ffffff)
					return true;
			}
			else { // no mask use transparency of off image
				pixel = bimage[0].get(dx, dy);
				// Not transparent?
				if(winApp.alpha(pixel) != 0)
					return true;
			}
		}
		return false;
	}

	/**
	 * Draw the button
	 */
	public void draw(){
		if(!visible) return;
		
		winApp.pushStyle();
		winApp.imageMode(CORNER);
		Point pos = new Point(0,0);
		calcAbsPosition(pos);
		
		// Draw image
		if(bimage != null && bimage[status] != null){
			winApp.image(bimage[status], pos.x, pos.y);
		}
		winApp.popStyle();
	}

	/**
	 * If the parameter is true all 3 event types are generated, if false
	 * only CLICKED events are generated (default behaviour).
	 * @param all
	 */
	public void fireAllEvents(boolean all){
		reportAllButtonEvents = all;
	}

	/**
	 * All GUI components are registered for mouseEvents. <br>
	 * When a button is clicked on a GButton it generates 3 events (in this order) 
	 * mouse down, mouse up and mouse pressed. <br>
	 * If you only wish to respond to button click events then you should test the 
	 * event type e.g. <br>
	 * <pre>
	 * 	void handleButtonEvents(GButton button) {
	 *	  if(button == btnName && button.eventType == GButton.CLICKED){
	 *        // code for button click event
	 *    }
	 * </pre> <br>
	 * Where <pre><b>btnName</b></pre> is the GButton identifier (variable name)
	 * 
	 */
	public void mouseEvent(MouseEvent event){
		if(!visible || !enabled) return;

		boolean mouseOver = isOver(winApp.mouseX, winApp.mouseY);
		if(mouseOver) 
			cursorIsOver = this;
		else if(cursorIsOver == this)
			cursorIsOver = null;

		switch(event.getID()){
		case MouseEvent.MOUSE_PRESSED:
			if(focusIsWith != this && mouseOver && z > focusObjectZ()){
				mdx = winApp.mouseX;
				mdy = winApp.mouseY;
				status = DOWN;
				takeFocus();
				eventType = PRESSED;
				if(reportAllButtonEvents)
					fireEvent();
			}
			break;
		case MouseEvent.MOUSE_CLICKED:
			// No need to test for isOver() since if the component has focus
			// and the mouse has not moved since MOUSE_PRESSED otherwise we 
			// would not get the Java MouseEvent.MOUSE_CLICKED event
			if(focusIsWith == this){
				status = OFF;
				loseFocus(null);
				eventType = CLICKED;
				fireEvent();
			}
			break;
		case MouseEvent.MOUSE_RELEASED:	
			// if the mouse has moved then release focus otherwise
			// MOUSE_CLICKED will handle it
			if(focusIsWith == this && mouseHasMoved(winApp.mouseX, winApp.mouseY)){
				loseFocus(null);
				if(isOver(winApp.mouseX, winApp.mouseY)){
					eventType = CLICKED;
					fireEvent();
				}
				else {
					if(reportAllButtonEvents){
						eventType = RELEASED;
						fireEvent();
					}
				}
				status = OFF;
			}
			break;
		case MouseEvent.MOUSE_MOVED:
			// If dragged state will stay as DOWN
			if(isOver(winApp.mouseX, winApp.mouseY))
				status = OVER;
			else
				status = OFF;
		}
	}

}

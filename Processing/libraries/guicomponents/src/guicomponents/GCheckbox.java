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
import processing.core.PImage;

/**
 * The checkbox component
 * 
 * @author Peter Lager
 *
 */
public class GCheckbox extends GComponent {

	protected boolean selected;

	protected static PImage imgSelected;
	protected static PImage imgCleared;

	/**
	 * Create a check box.
	 * 
	 * The height will be calculated using the font height.
	 * Will use the default global font
	 * 
	 * @param theApplet
	 * @param text text to appear alongside checkbox
	 * @param x horz position
	 * @param y vert position
	 * @param width width of component
	 */
	public GCheckbox(PApplet theApplet, String text, int x, int y, int width){
		super(theApplet, x, y);
		checkboxCtorCore(text, width);
	}

	/**
	 * Core stuff to be done by all ctors
	 * @param text
	 * @param width
	 * @param height
	 */
	private void checkboxCtorCore(String text, int width){
		if(imgSelected == null)
			imgSelected = winApp.loadImage("check1.png");
		if(imgCleared == null)
			imgCleared = winApp.loadImage("check0.png");
		this.width = width;
		height = Math.max((int)localFont.getSize() + 2 * PADV, imgCleared.height);
		opaque = false;
		setText(text);
		z = Z_SLIPPY;
		createEventHandler(G4P.mainWinApp, "handleCheckboxEvents", new Class[]{ GCheckbox.class });
		registerAutos_DMPK(true, true, false, false);
	}

	/**
	 * Set the font & size for the checkbox changing the height (+/-) 
	 * and width(+) of the checkbox if necessary to display text. 
	 */
	public void setFont(String fontname, int fontsize){
		int tw = textWidth;
		int fs = (int) localFont.getSize();
		localFont = GFont.getFont(winApp, fontname, fontsize);
		if(fontsize != fs)
			height += (fontsize - fs);
		setText(text);
		if(textWidth > tw)
			width += (textWidth - tw);
	}

	/**
	 * Calculate text X position based on text alignment
	 */
	protected void calcAlignX(){
		switch(textAlignHorz){
		case GAlign.LEFT:
			alignX = imgSelected.width + 2 * border + PADH;
			break;
		case GAlign.RIGHT:
			alignX = (int) (width - textWidth - 2 * border - PADH);
			break;
		case GAlign.CENTER:
			alignX = (int) (imgSelected.width + (width - imgSelected.width - textWidth)/2);
			break;
		}
	}

	/**
	 * Draw the checkbox
	 */
	public void draw(){
		if(!visible) return;
		winApp.pushStyle();
		winApp.style(G4P.g4pStyle);
		Point pos = new Point(0,0);
		calcAbsPosition(pos);
		if (!text.equals("")){
			if(border != 0){
				winApp.strokeWeight(border);
				winApp.stroke(localColor.cbxBorder);
			}
			else
				winApp.noStroke();
			if(opaque)
				winApp.fill(localColor.cbxBack);
			else
				winApp.noFill();
			winApp.rect(pos.x, pos.y, width, height);
			// Draw text
			winApp.noStroke();
			winApp.fill(localColor.cbxFont);
			winApp.textFont(localFont, localFont.getSize());
			winApp.text(text, pos.x + alignX, pos.y + (height - localFont.getSize())/2 - PADV, textWidth, height);
		}
		winApp.fill(winApp.color(255,255));
		if(imgSelected != null && imgCleared != null){
			if(selected)
				winApp.image(imgSelected, pos.x, pos.y + (height - imgSelected.height)/2);
			else
				winApp.image(imgCleared, pos.x, pos.y + (height - imgSelected.height)/2);
		}
		winApp.popStyle();
	}

	/**
	 * All GUI components are registered for mouseEvents
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
				takeFocus();
			}
			break;
		case MouseEvent.MOUSE_CLICKED:
			if(focusIsWith == this /*&& isOver(app.mouseX, app.mouseY)*/){
				selected = !selected;
				if(selected)
					eventType = SELECTED;
				else
					eventType = DESELECTED;
				fireEvent();
				loseFocus(null);
				mdx = mdy = Integer.MAX_VALUE;
			}
			break;
		case MouseEvent.MOUSE_RELEASED:
			if(focusIsWith == this && mouseHasMoved(winApp.mouseX, winApp.mouseY)){
				this.loseFocus(null);
				mdx = mdy = Integer.MAX_VALUE;
			}
			break;
		}
	}

	public boolean isSelected(){
		return selected;
	}

	public void setSelected(boolean selected){
		this.selected = selected;
	}

}

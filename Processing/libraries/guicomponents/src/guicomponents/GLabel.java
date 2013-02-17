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
 * The label component.
 * 
 * @author Peter Lager
 *
 */
public class GLabel extends GComponent {

	/**
	 * 
	 * @param theApplet
	 * @param text
	 * @param x
	 * @param y
	 * @param width
	 */
	public GLabel(PApplet theApplet, String text, int x, int y, int width) {
		super(theApplet, x, y);
		labelCoreCtor(text, width, 0);
	}

	/**
	 * 
	 * @param theApplet
	 * @param text
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	public GLabel(PApplet theApplet, String text, int x, int y, int width, int height) {
		super(theApplet, x, y);
		labelCoreCtor(text, width, height);
	}

	/**
	 * 
	 * @param text
	 * @param width
	 * @param height
	 */
	private void labelCoreCtor(String text, int width, int height){
		this.width = width;
		this.height = localFont.getSize() + 2 * PADV;
		if(height > this.height)
			this.height = height;
		opaque = false;
		if(text != null)
			setText(text);
		registerAutos_DMPK(true, false, false, false);
	}

	/**
	 * Set the font & size for the label changing the height (+/-) 
	 * and width(+/-) of the label if necessary to display text.
	 */
	public void setFont(String fontname, int fontsize){
		int tw = textWidth;
		int fs = (int) localFont.getSize();
		localFont = GFont.getFont(winApp, fontname, fontsize);
		if(fontsize != fs)
			height += (fontsize - fs);
		setText(text);
		if(textWidth != tw)
			width += (textWidth - tw);
		calcAlignX();
		calcAlignY();
	}

	/**
	 * Draw the label
	 */
	public void draw(){
		if(!visible) return;

		winApp.pushStyle();
		winApp.style(G4P.g4pStyle);
		Point pos = new Point(0,0);
		calcAbsPosition(pos);
		if(border != 0){
			winApp.strokeWeight(border);
			winApp.stroke(localColor.lblBorder);
		}
		else
			winApp.noStroke();
		if(opaque)
			winApp.fill(localColor.lblBack);
		else
			winApp.noFill();
		winApp.rect(pos.x,pos.y, width, height);
		// Draw text
		winApp.noStroke();
		winApp.fill(localColor.lblFont);
		winApp.textFont(localFont, localFont.getSize());
		winApp.text(text, pos.x + alignX, pos.y + alignY, width, height);
//		winApp.text(text, pos.x + alignX, pos.y + (height - localFont.getFont().getSize())/2 - PADV, width - PADH - 2* border, height);
		winApp.popStyle();
	}
}

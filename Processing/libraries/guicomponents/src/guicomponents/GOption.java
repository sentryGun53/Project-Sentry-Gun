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
 * The option button class. This is used with the GOptionGroup
 * class to provide sets of options.
 * 
 * @author Peter Lager
 *
 */
public class GOption extends GComponent {
	/**
	 * All GOption objects should belong to a group
	 */
	protected GOptionGroup ownerGroup;

	// Images used for selected/deselected option
	protected static PImage imgSelected;
	protected static PImage imgCleared;


	/**
	 * Create an option button
	 * 
	 * @param theApplet
	 * @param text
	 * @param x
	 * @param y
	 * @param width
	 */
	public GOption(PApplet theApplet, String text, int x, int y, int width){
		super(theApplet, x, y);
		optionCtorCore(text, width, 0);
	}

	/**
	 * Code common to all ctors
	 * @param text
	 * @param width
	 * @param height
	 */
	private void optionCtorCore(String text, int width, int height){
		if(imgSelected == null)
			imgSelected = winApp.loadImage("radio1.png");
		if(imgCleared == null)
			imgCleared = winApp.loadImage("radio0.png");
		this.width = width;
		this.height = localFont.getSize() + 2 * PADV;
		if(height > this.height)
			this.height = height;
		opaque = false;
		setText(text);
		z = Z_SLIPPY;
		createEventHandler(G4P.mainWinApp, "handleOptionEvents", new Class[]{ GOption.class, GOption.class });
		registerAutos_DMPK(true, true, false, false);
	}

	/**
	 * Attempt to create the default event handler for the component class. 
	 * The default event handler is a method that returns void and has a single
	 * parameter of the same type as the componment class generating the
	 * event and a method name specific for that class. 
	 * 
	 * @param obj the object to handle the event
	 * @param methodName the method to execute in the object handler class
	 */
	public void addEventHandler(Object obj, String methodName){
		try{
			eventHandlerObject = obj;
			eventHandlerMethodName = methodName;
			eventHandlerMethod = obj.getClass().getMethod(methodName, new Class[] { GOption.class, GOption.class } );
		} catch (Exception e) {
			GMessenger.message(NONEXISTANT, this, new Object[] {methodName, new Class[] { this.getClass() } } );
			eventHandlerObject = null;
			eventHandlerMethodName = "";
		}
	}

	/**
	 * Fire an event for this component which has a reference to the
	 * option being de-selected as well as the option being selected.
	 * 
	 */
	protected void fireEvent(){
		if(eventHandlerMethod != null){
			if(eventHandlerMethod != null){
				try {
					eventHandlerMethod.invoke(eventHandlerObject,
							new Object[] { this, ownerGroup.deselectedOption() } );
				} catch (Exception e) {
					GMessenger.message(EXCP_IN_HANDLER, eventHandlerObject, 
							new Object[] {eventHandlerMethodName, e } );
				}
			}		
		}
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
	 * Set the font & size for the option button changing the height (+/-) 
	 * and width(+/-) of the button if necessary to display text.  
	 */
	public void setFont(String fontname, int fontsize){
		int tw = textWidth;
		int fs = (int) localFont.getSize();
		localFont = GFont.getFont(winApp, fontname, fontsize);
		if(fontsize != fs){
			height += (fontsize - fs);
			height = Math.max(height, imgSelected.height);
		}
		setText(text);
		if(textWidth != tw)
			width += (textWidth - tw);
	}

	/**
	 * draw the option
	 */
	public void draw(){
		if(!visible) return;

		winApp.pushStyle();
		winApp.style(G4P.g4pStyle);
		Point pos = new Point(0,0);
		calcAbsPosition(pos);
		if (!text.equals("")){
			if(border == 0){
				winApp.noStroke();					
			}
			else {
				winApp.stroke(localColor.btnBorder);
				winApp.strokeWeight(border);					
			}
			if(opaque)
				winApp.fill(localColor.txfBack);
			else
				winApp.noFill();
			winApp.rect(pos.x, pos.y, width, height);
			// Draw text
			winApp.noStroke();
			winApp.fill(localColor.optFont);
			winApp.textFont(localFont, localFont.getSize());
			winApp.text(text, pos.x + alignX, pos.y + (height - localFont.getSize())/2 - PADV, width - imgSelected.width, height);
		}
		winApp.fill(winApp.color(255,255));
		if(imgSelected != null && imgCleared != null){
			if(ownerGroup != null && ownerGroup.selectedOption() == this)
				winApp.image(imgSelected, pos.x + 1, pos.y + (height - imgSelected.height)/2);
			else
				winApp.image(imgCleared, pos.x + 1, pos.y + (height - imgSelected.height)/2);
		}
		winApp.popStyle();

	}


	/**
	 * All GUI components are registered for mouseEvents
	 */
	public void mouseEvent(MouseEvent event){
		// If this option does not belong to a group then ignore mouseEvents
		if(!visible || !enabled || ownerGroup == null) return;

		boolean mouseOver = isOver(winApp.mouseX, winApp.mouseY);
		if(mouseOver)
			cursorIsOver = this;
		else if(cursorIsOver == this)
			cursorIsOver = null;

		switch(event.getID()){
		case MouseEvent.MOUSE_PRESSED:
			if(focusIsWith != this && mouseOver && z >= focusObjectZ()){
				mdx = winApp.mouseX;
				mdy = winApp.mouseY;
				this.takeFocus();
			}
			break;
		case MouseEvent.MOUSE_CLICKED:
			if(focusIsWith == this){
				ownerGroup.setSelected(this);
				loseFocus(null);
				mdx = mdy = Integer.MAX_VALUE;
				eventType = SELECTED;
				fireEvent();
			}
			break;
		case MouseEvent.MOUSE_RELEASED:
			if(focusIsWith == this && mouseHasMoved(winApp.mouseX, winApp.mouseY)){
				mdx = mdy = Integer.MAX_VALUE;
				this.loseFocus(null);
			}
			break;
		}
	}

	/**
	 * Find out if this option is selected
	 * @return true if option is selected else false
	 */
	public boolean isSelected(){
		return (ownerGroup != null && ownerGroup.selectedOption() == this);
	}

	/**
	 * Find out if this object is deselected
	 * @return true if option is not selected else false
	 */
	public boolean isNotSelected(){
		return !(ownerGroup != null && ownerGroup.selectedOption() == this);		
	}

	/**
	 * User can make this option selected - this does not cause
	 * events being fired
	 * 
	 * @param selected
	 */
	public void setSelected(boolean selected){
		if(ownerGroup != null){
			ownerGroup.setSelected(this);
		}
	}

	/**
	 * Get the option group that owns this option
	 * 
	 * @return a reference to this option's GOptionGroup
	 */
	public GOptionGroup getGroup(){
		return ownerGroup;
	}

	/**
	 * Set the option group - at the present this method does not allow the option
	 * to be moved from one group to another.
	 * @param group
	 */
	public void setGroup(GOptionGroup group){
		this.ownerGroup = group;
		//group.addOption(this);
	}

}

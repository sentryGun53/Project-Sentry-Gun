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
import java.util.Iterator;
import java.util.LinkedList;

import processing.core.PApplet;

/**
 * A component that can be used to group GUI components that can be
 * dragged, collapsed (leaves title tab only) and un-collapsed.
 * 
 * When created the Panel is collapsed by default. To open the panel
 * use setCollapsed(true); after creating it.
 * 
 * @author Peter Lager
 *
 */
public class GPanel extends GComponent {

	/** Whether the panel is displayed in full or tab only */
	protected boolean tabOnly = true;

	/** The height of the tab calculated from font height + padding */
	protected int tabHeight;

	/** Used to restore position when closing panel */
	protected float dockX, dockY;

	/** true if the panel is being dragged */
	protected boolean beingDragged = false;

	/**
	 * Create a Panel that comprises of 2 parts the tab which is used to 
	 * select and move the panel and the container window below the tab which 
	 * is used to hold other components.
	 * The size of the container window will grow to fit components added
	 * provided that it does not exceed the width and height of the applet
	 * window.
	 *  
	 * @param theApplet the PApplet reference
	 * @param text to appear on tab
	 * @param x horizontal position
	 * @param y vertical position
	 * @param width width of the panel
	 * @param height height of the panel (excl. tab)
	 */
	public GPanel(PApplet theApplet, String text, int x, int y, int width, int height){
		super(theApplet, x, y);
		panelCtorCore(text, width, height);
	}

	/**
	 * Code common for all constructors.
	 * @param text to appear on the tab
	 * @param width
	 * @param height
	 */
	private void panelCtorCore(String text, int width, int height){
		children = new LinkedList<GComponent>();
		setText(text);
		tabHeight = (int) (1.2f * localFont.getSize() + 2 * PADV);
		constrainPanelPosition();
		opaque = true;
		dockX = x;
		dockY = y;
		this.width = width;
		this.height = height;
		z = Z_PANEL;
		createEventHandler(G4P.mainWinApp, "handlePanelEvents", new Class[]{ GPanel.class });
		registerAutos_DMPK(true, true, false, false);
	}

	/**
	 * Set the font & size for the tab text changing the height (+/-) 
	 * of the tab if necessary to display text.  
	 */
	public void setFont(String fontname, int fontsize){
		localFont = GFont.getFont(winApp, fontname, fontsize);

		tabHeight = (int) (1.2f * localFont.getSize() + 2 * PADV);
		setText(text);
	}

	/**
	 * Bring this panel to the front i.e. above other panels.
	 */
	public void bringToFront(){
		if(parent != null)
			parent.bringToFront();
		G4P.moveToFrontForDraw(this);
	}
	
	/**
	 * What to do when the GPanel loses focus.
	 */
	protected void loseFocus(GComponent grabber){
		focusIsWith = null;
		beingDragged = false;
	}

//	protected void takeFocus(){
//		super.takeFocus();
//		G4P.moveToFrontForDraw(this);
//	}
	
	/**
	 * Draw the panel.
	 * If tabOnly == true 
	 * 		then display the tab only
	 * else
	 * 		draw tab and all child (added) components
	 */
	public void draw(){
		if(!visible) return;

		Point pos = new Point(0,0);
		calcAbsPosition(pos);

		winApp.pushMatrix();
		winApp.translate(pos.x, pos.y);
		
		winApp.pushStyle();
		winApp.style(G4P.g4pStyle);
		
		winApp.noStroke();
		if(border > 0){
			winApp.strokeWeight(border);
			winApp.stroke(localColor.pnlBorder);
		}
		winApp.fill(localColor.pnlTabBack);
		// Display tab (length depends on whether panel is open or closed
		int w = (int) ((tabOnly)? textWidth + PADH * 4 : width);
		winApp.rect(0, - tabHeight, w, tabHeight);
		// Display tab text
		winApp.fill(localColor.pnlFont);
		winApp.textFont(localFont, localFont.getSize());
		winApp.text(text, PADH, - (tabHeight + localFont.getSize())/2 - PADV , textWidth, tabHeight);
		if(!tabOnly){
			if(opaque){
				winApp.fill(localColor.pnlBack);
				winApp.rect(0, 0, width, height);
			}
		}
		winApp.popStyle();
		winApp.popMatrix();
		if(!tabOnly){
			Iterator<GComponent> iter = children.iterator();
			while(iter.hasNext()){
				iter.next().draw();
			}
		}
	}


	/**
	 * All GUI components are registered for mouseEvents
	 */
	public void mouseEvent(MouseEvent event){
		if(!visible || !enabled) return;
		
		boolean mouseOver = isOver(winApp.mouseX, winApp.mouseY);
//		boolean mouseOverPanel = isOverPanel(winApp.mouseX, winApp.mouseY);
		if(mouseOver) 
			cursorIsOver = this;
		else if(cursorIsOver == this)
				cursorIsOver = null;

		switch(event.getID()){
		case MouseEvent.MOUSE_PRESSED:
			if(focusIsWith != this && mouseOver && z >= focusObjectZ()){
				mdx = winApp.mouseX;
				mdy = winApp.mouseY;
				takeFocus();
				// May become true but will soon be set to false when
				// we lose focus
				beingDragged = true;
			}
			// If focus is with some other control with the same depth and the mouse is over the panel
			// Used to ensure that GTextField controls on GPanels release focus
			if(focusIsWith != null && focusIsWith != this && z == focusObjectZ() && isOverPanel(winApp.mouseX, winApp.mouseY) )
				focusIsWith.loseFocus(null);
			break;
		case MouseEvent.MOUSE_CLICKED:
			if(focusIsWith == this){
				tabOnly = !tabOnly;
				// Perform appropriate action depending on collase state
				setCollapsed(tabOnly);
				if(tabOnly)
					eventType = COLLAPSED;
				else
					eventType = EXPANDED;
				// fire an event
				fireEvent();
				if(tabOnly){
					x = dockX;
					y = dockY;					
				}
				else {
					dockX = x;
					dockY = y;
					// Open panel move on screen if needed
					if(y + height > winApp.getHeight())
						y = winApp.getHeight() - height;
					if(x + width > winApp.getWidth())
						x = winApp.getWidth() - width;
				}
				// This component does not keep the focus when clicked
				loseFocus(null);
				mdx = mdy = Integer.MAX_VALUE;
			}
			break;
		case MouseEvent.MOUSE_RELEASED:
			if(focusIsWith == this){
				if(mouseHasMoved(winApp.mouseX, winApp.mouseY)){
					mdx = mdy = Integer.MAX_VALUE;
					loseFocus(null);
				}
			}
			break;
		case MouseEvent.MOUSE_DRAGGED:
			if(focusIsWith == this && parent == null){
				x += (winApp.mouseX - winApp.pmouseX);
				y += (winApp.mouseY - winApp.pmouseY);
				beingDragged = true;
				eventType = DRAGGED;
				fireEvent();
				constrainPanelPosition();
				if(!tabOnly){
					dockX = x;
					dockY = y;
				}
			}
			break;
		}
	}

	/**
	 * This method is used to discover whether the panel is being 
	 * dragged to a new position on the screen.
	 * @return true if being dragged to a new position
	 */
	public boolean isDragging(){
		return beingDragged;
	}

	/**
	 * Ensures that the panel tab and panel body if open does not
	 * extend off the screen.
	 */
	private void constrainPanelPosition(){
		int w = (int) ((tabOnly)? textWidth + PADH * 2 : width);
		int h = (int) ((tabOnly)? 0 : height);
		// Constrain horizontally
		if(x < 0) 
			x = 0;
		else if(x + w > winApp.getWidth()) 
			x = (int) (winApp.getWidth() - w);
		// Constrain vertically
		if(y - tabHeight  < 0) 
			y = tabHeight;
		else if(y + h > winApp.getHeight()) 
			y = winApp.getHeight() - h;
	}

	/**
	 * Determines whether the position ax, ay is over the tab
	 * of this GPanel.
	 * @param ax x position
	 * @param ay y position
	 * @return true if mouse is over the panel tab else false
	 */
	public boolean isOver(int ax, int ay){
		Point p = new Point(0,0);
		calcAbsPosition(p);
		int w = (int) ((tabOnly)? textWidth + PADH * 2 : width);
		if(ax >= p.x && ax <= p.x + w && ay >= p.y - tabHeight && ay <= p.y)
			return true;
		else
			return false;
	}
	
	/**
	 * Determines whether the position ax, ay is over the panel, takimg
	 * into account whether the panel is collapsed or not. <br>
	 * 
	 * @param ax x position
	 * @param ay y position
	 * @return true if mouse is over the panel surface else false
	 */
	public boolean isOverPanel(int ax, int ay){
		if(tabOnly)
			return isOver(ax,ay);
		else {
			Point p = new Point(0,0);
			calcAbsPosition(p);
			if(ax >= p.x && ax <= p.x + width && ay >= p.y - tabHeight && ay <= p.y + height)
				return true;
			else 
				return false;
		}
	}

	public void setControlsEnabled(boolean enable){
		if(children != null){
			for(GComponent c : children)
				c.setEnabled(enable);
		}
	}

	/**
	 * If the panel is made invisible then disable the controls
	 * @param visible the visible to set
	 */
	public void setVisible(boolean visible) {
		this.visible = visible;
		if(!visible)
			setControlsEnabled(false);
	}

	/**
	 * Controls the transparency of this panel and all the
	 * components on it.
	 * 0 = fully transparent
	 * 255 = fully opaque
	 * 
	 * @param alpha
	 */
	public void setAlpha(int alpha){
		localColor.setAlpha(alpha);
		if(!children.isEmpty()){
			Iterator<GComponent> iter = children.iterator();
			while(iter.hasNext())
				iter.next().setAlpha(alpha);
		}
	}

	/**
	 * Collapse or open the panel
	 * @param collapse
	 */
	public void setCollapsed(boolean collapse){
		tabOnly = collapse;
		// If we open the panel make sure it fits on the screen but if we
		// collapse the panel disable the panel controls
		if(!tabOnly)
			constrainPanelPosition();
		else
			setControlsEnabled(false);
	}

	/**
	 * Find out if the panel is collapsed
	 * @return true if collapsed
	 */
	public boolean isCollapsed(){
		return tabOnly;
	}
	
	public int getTabHeight(){
		return tabHeight;
	}

} // end of class

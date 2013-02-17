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
import java.util.ArrayList;
import java.util.LinkedList;

import processing.core.PApplet;
import processing.core.PImage;

/**
 * Combo Box (drop down list) component.
 * 
 * @author Peter Lager
 *
 */
public class GCombo extends GComponent {

	protected static PImage imgArrow;

	protected int startRow;
	protected int maxRows = 5; 		// max height of list
	protected int nbrRowsToShow;	// number of rows to show

	protected GOptionGroup optGroup;
	protected GVertSlider slider;

	protected boolean expanded = false;

	/**
	 * Create the combo using the string array for the options
	 * the first option in the list is made the selected one.
	 * If you want another then use setSelected()
	 * 
	 * @param theApplet
	 * @param options
	 * @param maxRows
	 * @param x
	 * @param y
	 * @param width
	 */
	public GCombo(PApplet theApplet, String[] options, int maxRows, int x, int y, int width){
		super(theApplet, x, y);
		this.maxRows = PApplet.constrain(maxRows, 1, 25);
		comboCtorCore(width);
		createOptions(options);
		createSlider();
	}

	private void comboCtorCore(int width) {
		children = new LinkedList<GComponent>();
		if(imgArrow == null)
			imgArrow = winApp.loadImage("combo0.png");
		this.width = width;
		height = Math.max((int)localFont.getSize() + 2 * PADV, imgArrow.height);
		opaque = true;
		border = 1;
		z = Z_SLIPPY;
		createEventHandler(G4P.mainWinApp, "handleComboEvents", new Class[]{ GCombo.class });
		registerAutos_DMPK(true, true, false, false);
	}

	/**
	 * Create the vertical slider for the drop down list
	 */
	private void createSlider(){
		slider = new GVertSlider(winApp, (int)width - 10, (int)height, 10, maxRows * (int)height);
		slider.setBorder(1);
		slider.setVisible(false);
		slider.setLimits(0, 0, maxRows - 1);
		slider.addEventHandler(this, "processSliderMotion", new Class[] { GVertSlider.class } );
		add(slider);
	}

	/**
	 * Create initial options based on string array.
	 * 
	 * @param optTexts
	 */
	protected void createOptions(String[] optTexts){
		optGroup = new GOptionGroup();
		GOption option;
		for(int i = 0; i < optTexts.length; i++){
			option = makeOption(optTexts[i]);
			if(option != null){
				optGroup.addOption(option);
				add(option);
			}
		}
		optGroup.setSelected(0);
		text = optGroup.selectedText();
		nbrRowsToShow = Math.min(optTexts.length, maxRows);
	}

	/**
	 * Make a new option for the text optText
	 * 
	 * @param optText the text to appear on the option.
	 * @return reference to option created and added else null
	 */
	public GOption makeOption(String optText){
		GOption opt = null;
		if(optText != null && !optText.equals("")){
			opt = new GOption(winApp, optText, 0, 0, (int)width - 10);
			opt.addEventHandler(this, "processOptionSelection");
			opt.setVisible(false);
			opt.setOpaque(true);
			opt.setBorder(0);
		}
		return opt;
	}
	
	/**
	 * Set the font & size for the combo changing height and
	 * width of the button if necessary to display text. <br>
	 * It will not shrink if the font size is decreased.  
	 */
	public void setFont(String fontname, int fontsize){
		int tw = textWidth;
		int fs = (int) localFont.getSize();
		localFont = GFont.getFont(winApp, fontname, fontsize);
		if(fontsize > fs)
			height += (fontsize - fs);
		setText(text);
		if(textWidth > tw)
			width += (textWidth - tw);
		ArrayList<GOption> options = optGroup.getOptions();
		for(int i = 0; i < options.size(); i++)
			options.get(i).setWidth((int)width - 10);
		slider.setX((int)width - 10);
	}

	/**
	 * Sets the local color scheme
	 * @param schemeNo
	 */
	public void setColorScheme(int schemeNo){
		localColor = GCScheme.getColor(winApp, schemeNo);
		slider.localColor = GCScheme.getColor(winApp, schemeNo);
		ArrayList<GOption> options = optGroup.getOptions();
		for(int i = 0; i < options.size(); i++)
			options.get(i).localColor = GCScheme.getColor(winApp, schemeNo);
	}


	/**
	 * INTERNAL USE ONLY <br>
	 * This will handle the vertical slider events by changing the starting 
	 * value of thefirst option to be shown. <br>
	 * 
	 * @param vertslider 
	 */
	public void processSliderMotion(GVertSlider vertslider){
		startRow = slider.getValue();
	}

	/**
	 * This method is called when an option is selected from the 
	 * drop down list.
	 * 
	 * @param sOpt selected option
	 * @param dOpt deselected option
	 */
	public void processOptionSelection(GOption sOpt, GOption dOpt){
		text = optGroup.selectedText();
		if(sOpt != dOpt)
			fireEvent();
		shrink();
		loseFocus(null);
	}

	/**
	 * Set the selected option by its index value. (Starts at 0)
	 * @param index
	 */
	public void setSelected(int index){
		optGroup.setSelected(index);
		setText(optGroup.selectedText());
	}
	
	/**
	 * Set the selected option by its text value
	 * @param optText
	 */
	public void setSelected(String optText){
		optGroup.setSelected(optText);
		setText(optGroup.selectedText());
	}

	/**
	 * Add an option to the end of the list
	 * 
	 * @param optText
	 * @return true if option successfully added
	 */
	public boolean addOption(String optText){
		GOption option = makeOption(optText);
		boolean ok = optGroup.addOption(option);
		if(ok)
			add(option);
		return ok;
	}

	/**
	 * Add an option in the given position
	 * 
	 * @param pos
	 * @param optText
	 * @return true if option successfully added
	 */
	public boolean addOption(int pos, String optText){
		GOption option = makeOption(optText);
		boolean ok = optGroup.addOption(pos, option);
		if(ok)
			add(option);
		return ok;
	}

	/**
	 * Remove an option based on its index value in the list
	 * 
	 * @param index
	 */
	public void removeOption(int index){
		GOption option = optGroup.removeOption(index);
		if(option != null)
			remove(option);
	}
	
	/**
	 * Remove a value based on its text value
	 * 
	 * @param optText
	 */
	public void removeOption(String optText){
		GOption option = optGroup.removeOption(optText);
		if(option != null)
			remove(option);
	}

	/**
	 * Get the number of options
	 * @return
	 */
	public int getNbrOptions() {
		return optGroup.getOptions().size();
	}

	/**
	 * Removes all the current options from the combo box
	 * 
	 */
	public void removeAllOptions() {
		optGroup.getOptions().clear();
		setText("");
	}

	/**
	 * This will replace all options with the new options specified.
	 * 
	 * @param optTexts the new options
	 * @param selected the initial selection (uses 0 if invalid)
	 */
	public void setOptions(String[] optTexts, int selected) {
		createOptions(optTexts);
		setSelected(selected);
	}

	/**
	 * Determines whether the position ax, ay is over the expand arrow 
	 * or over the expanded combo box, depending on whether the box 
	 * is expanded or not.
	 * 
	 * @return true if mouse is over the appropriate part
	 */
	public boolean isOver(int ax, int ay){
		Point p = new Point(0,0);
		calcAbsPosition(p);
		int x1, x2, y1, y2;
		if(expanded){
			x1 = p.x;
			y1 = p.y;
			x2 = x1 + (int)width;
			y2 = y1 + (nbrRowsToShow + 1)*(int)height;
		}
		else {
			x1 = p.x + (int)width - imgArrow.width - PADH;
			y1 = p.y + ((int)height - imgArrow.height)/2 + PADV;
			x2 = x1 + imgArrow.width;
			y2 = y1 + imgArrow.height;
		}
		return (ax >= x1 && ax <= x2 && ay >= y1 && ay <= y2); 
	}

	/**
	 * Close the drop down list
	 */
	public void shrink(){
		expanded = false;
		for(int i = 0; i < optGroup.size(); i++)
			optGroup.get(i).visible = false;
		slider.visible = false;
	}

	/**
	 * Open the drop down list
	 */
	public void expand(){
		expanded = true;
		startRow = 0;
		slider.setLimits(0, 0, optGroup.size() - maxRows);
		takeFocus();
	}

	/**
	 * Is the drop down list visible
	 * @return true if the combo list is visible (ie down) else false
	 */
	public boolean isExpanded(){
		return expanded;
	}

	/**
	 * If we loose the focus to another GUI component
	 * that is not a child of this, then shrink the drop
	 * down list and release focus
	 */
	public void loseFocus(GComponent grabber){
		if(!children.contains(grabber)){
			shrink();
			focusIsWith = null;
		}
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
			if(focusIsWith != this && mouseOver && z >= focusObjectZ())
				takeFocus();
			else if(focusIsWith == this && !mouseOver)
//			else if(focusIsWith == this && !isOver(winApp.mouseX, winApp.mouseY))
				loseFocus(null);
			break;
		case MouseEvent.MOUSE_CLICKED:
//			if(focusIsWith == this && isOver(winApp.mouseX, winApp.mouseY)){
			if(focusIsWith == this && mouseOver){
				if(expanded)
					shrink();
				else
					expand();
				mdx = mdy = Integer.MAX_VALUE;
			}
			break;
		}
	}

	/**
	 * Draw the combo box
	 */
	public void draw(){
		if(!visible) return;

		winApp.pushStyle();
		winApp.style(G4P.g4pStyle);
		Point pos = new Point(0,0);
		calcAbsPosition(pos);

		// Draw selected option area
		if(border == 0)
			winApp.noStroke();
		else {
			winApp.strokeWeight(border);
			winApp.stroke(localColor.txfBorder);
		}
		if(opaque)
			winApp.fill(localColor.txfBack);
		else
			winApp.noFill();
		winApp.rect(pos.x, pos.y, width, height);
		
		// Draw selected text
		winApp.noStroke();
		winApp.fill(localColor.txfFont);
		winApp.textFont(localFont, localFont.getSize());
		winApp.text(text, pos.x + PADH, pos.y -PADV +(height - localFont.getSize())/2, width - 16, height);

		// draw drop down list
		winApp.fill(winApp.color(255,255));
		if(imgArrow != null)
			winApp.image(imgArrow, pos.x + width - imgArrow.width - 1, pos.y + (height - imgArrow.height)/2);
		if(expanded == true){
			GOption opt;
			winApp.noStroke();
			winApp.fill(localColor.txfBack);
			winApp.rect(pos.x,pos.y+height,width,nbrRowsToShow*height);

			for(int i = 0; i < optGroup.size(); i++){
				opt = optGroup.get(i);
				if(i >= startRow && i < startRow + nbrRowsToShow){
					opt.visible = true;
					opt.y = height * (i - startRow + 1);
					opt.draw();
				}
				else {
					opt.visible = false;					
				}
			}
			// Draw box round list
			if(border != 0){
				winApp.strokeWeight(border);
				winApp.stroke(localColor.txfBorder);
				winApp.noFill();
				winApp.rect(pos.x,pos.y+height,width,nbrRowsToShow*height);
			}
			if(optGroup.size() > maxRows){
				slider.setVisible(true);
				slider.draw();
			}
		}
		winApp.popStyle();
	}

	public int selectedIndex(){
		return optGroup.selectedIndex();
	}

	public String selectedText(){
		return optGroup.selectedText();
	}

	public int deselectedIndex(){
		return optGroup.deselectedIndex();
	}

	public String deselectedText(){
		return optGroup.deselectedText();
	}

	public String toString(){
		StringBuilder s = new StringBuilder(tag + "   ("+z+")\n");
		if(children != null)
			for(GComponent c : children)
				s.append("\t" + c + "\n");
		return new String(s);
	}
}

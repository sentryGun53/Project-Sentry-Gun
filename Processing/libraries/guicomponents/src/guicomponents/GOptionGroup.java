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

import java.util.ArrayList;

import processing.core.PApplet;

/**
 * This is used to group options together to provide single-selection
 * from 2 or more GOption buttons.
 * 
 * @author Peter Lager
 *
 */
public class GOptionGroup {

	protected GOption selected = null;
	protected GOption deselected = null;

	protected ArrayList<GOption> options = new ArrayList<GOption>();;

	/**
	 * Use this constructor to create an option group.
	 */
	public GOptionGroup(){
	}

	/**
	 * This class does not need a reference to the applet class
	 * @deprecated
	 * @param theApplet
	 */
	public GOptionGroup(PApplet theApplet){
	}

	public ArrayList<GOption> getOptions(){
		return options;
	}

	/**
	 * Enable or disable all the options in this group
	 * @param enable
	 */
	public void setEnabled(boolean enable){
		for(int i = 0; i < options.size(); i++){
			options.get(i).setEnabled(enable);
		}
	}

	/**
	 * INTERNAL USE ONLY
	 * @param index
	 * @return reference to an option based on it's index.
	 */
	public GOption get(int index){
		return options.get(index);
	}

	/**
	 * INTERNAL USE ONLY
	 * @param option
	 * @return true if option successfully added else false
	 */
	public boolean addOption(GOption option){
		if(option != null){
			options.add(option);
			option.setGroup(this);
			return true;
		}
		else
			return false;
	}

	/**
	 * Add a new Option at the given position
	 * @param pos
	 * @param option
	 * @return true if option successfully added else false
	 */
	public boolean addOption(int pos, GOption option){
		if(option != null && pos >= 0 && pos <= options.size()){
			options.add(pos, option);
			option.setGroup(this);
			return true;			
		}
		return false;
	}

	/**
	 * Remove an existing option
	 * @param option
	 * @return a reference to the option removed
	 */
	public GOption removeOption(GOption option){
		options.remove(option);
		return option;
	}

	/**
	 * Remove an option at the given position
	 * @param index
	 * @return a reference to the option removed
	 */
	public GOption removeOption(int index){
		GOption option = null;
		if(index >= 0 && index < options.size()){
			option = options.remove(index);
		}
		return option;
	}

	/**
	 * Remove an option based on the option's text (this ignores case)
	 * @param optText
	 * @return a reference to the option removed
	 */
	public GOption removeOption(String optText){
		System.out.println("REMOVE");
		GOption option = null;
		int i = options.size() - 1;
		while(i >= 0){
			if(options.get(i).getText().compareToIgnoreCase(optText) == 0){
				option = options.get(i);
				break;
			}
			i--;
		}
		if(option != null)
			options.remove(option);
		return option;
	}

	/**
	 * Make this option the selected one
	 * 
	 * @param option
	 */
	public void setSelected(GOption option){
		deselected = selected;
		selected = option;
	}

	/**
	 * If index is in range make this one selected
	 *  
	 * @param index
	 */
	public void setSelected(int index){
		if(index >= 0 && index < options.size()){
			deselected = selected;
			options.get(index).setSelected(true);
		}
	}

	/**
	 * Set option selected based on the option text (this case insensitive)
	 * @param optText the text of the option to select
	 */
	public void setSelected(String optText){
		int i = options.size();
		while(i-- >= 0){
			if(options.get(i).getText().compareToIgnoreCase(optText) == 0)
				break;
		}
		if(i > 0)
			setSelected(options.get(i));
	}

	/**
	 * INTERNAL USE ONLY
	 * Return the option that has just been selected
	 * @return the option that has been selected
	 */
	public GOption selectedOption(){
		return selected;
	}

	/**
	 * INTERNAL USE ONLY
	 * Return the option that has just been deselected
	 * @return the option that has been deselected
	 */
	public GOption deselectedOption(){
		return deselected;
	}

	/**
	 * INTERNAL USE ONLY
	 * @return the index of the currently selected option
	 */
	public int selectedIndex(){
		return options.indexOf(selected);
	}

	/**
	 * INTERNAL USE ONLY
	 * Return the index of the option that has just been deselected
	 * @return the index of the de-selected option
	 */
	public int deselectedIndex(){
		return options.indexOf(deselected);
	}

	/**
	 * INTERNAL USE ONLY
	 * @return the text of the currently selected option
	 */
	public String selectedText(){
		return selected.text;
	}

	/**
	 * INTERNAL USE ONLY
	 * Return the text of the option that has just been deselected
	 * @return the text of the de-selected option
	 */
	public String deselectedText(){
		return deselected.text;
	}

	/**
	 * Get the number of options in this GOptionGroup
	 */
	public int size(){
		return options.size();
	}

}

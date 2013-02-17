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

import java.io.IOException;
import java.io.InputStream;

import processing.core.PApplet;
import processing.core.PImage;

/**
 * Stores all the colour information for the GUI components into a scheme.
 * 
 * Defines a set of predefined schemes covering the primary colours
 * 
 * @author Peter Lager
 *
 */
public class GCScheme implements GConstants {

	// Color scheme constants
	public static final int BLUE_SCHEME 	= 0;
	public static final int GREEN_SCHEME 	= 1;
	public static final int RED_SCHEME 		= 2;
	public static final int PURPLE_SCHEME	= 3;
	public static final int YELLOW_SCHEME	= 4;
	public static final int CYAN_SCHEME 	= 5;
	public static final int GREY_SCHEME 	= 6;

	protected static PApplet app;

	protected static PImage image = null;

	// Mask to get RGB
	public static final int COL_MASK 		= 0x00ffffff;
	// Mask to get alpha
	public static final int ALPHA_MASK 		= 0xff000000;

	public static int setAlpha(int col, int alpha){
		alpha = (alpha & 0xff) << 24;
		col = (col & COL_MASK) | alpha;
		return col;
	}

	/**
	 * Set the default color scheme
	 * 
	 * @param theApplet
	 * @return the applets color scheme
	 */
	public static GCScheme getColor(PApplet theApplet){
		return getColor(theApplet, 0);
	}

	/**
	 * Set the color scheme to one of the preset schemes
	 * BLUE / GREEN / RED /  PURPLE / YELLOW / CYAN / GREY
	 * or if you have created your own schemes following the instructions
	 * at gui4processing.lagers.org.uk/colorscheme.html then you can enter
	 * the appropriate numeric value of the scheme.
	 * 
	 * @param theApplet
	 * @param schemeNo
	 * @return the color scheme based on the scheme number
	 */
	public static GCScheme getColor(PApplet theApplet, int schemeNo){
		app = theApplet;
		if(image == null){
			InputStream is = app.createInput("user_col_schema.png");
			if(is != null){
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				image = app.loadImage("user_col_schema.png");
				GMessenger.message(USER_COL_SCHEME, null, null);
			}
			else {
				// User image not provided
				image = app.loadImage("default_col_schema.png");
			}
		}
		GCScheme scheme = new GCScheme(schemeNo);
		populateScheme(scheme, schemeNo);

		return scheme;
	}

	protected static void populateScheme(GCScheme s, int schemeNo){
		// Force the scheme number to be valid depending on size of image
		schemeNo = Math.abs(schemeNo) % image.height;
		s.pnlFont = image.get(0, schemeNo) | ALPHA_MASK;
		s.pnlTabBack = image.get(1, schemeNo) | ALPHA_MASK;
		s.pnlBack = image.get(2, schemeNo) | ALPHA_MASK;
		s.pnlBorder = image.get(3, schemeNo) | ALPHA_MASK;

		s.btnFont = image.get(5, schemeNo) | ALPHA_MASK;
		s.btnOff = image.get(6, schemeNo) | ALPHA_MASK;
		s.btnOver = image.get(7, schemeNo) | ALPHA_MASK;
		s.btnDown = image.get(8, schemeNo) | ALPHA_MASK;

		s.sdrTrack = image.get(10, schemeNo) | ALPHA_MASK;
		s.sdrThumb = image.get(11, schemeNo) | ALPHA_MASK;
		s.sdrBorder = image.get(12, schemeNo) | ALPHA_MASK;

		s.txfFont = image.get(15, schemeNo) | ALPHA_MASK;
		s.txfBack = image.get(16, schemeNo) | ALPHA_MASK;
		s.txfSelBack = image.get(17, schemeNo)  | ALPHA_MASK;
		s.txfBorder = image.get(18, schemeNo)  | ALPHA_MASK;
		s.txfCursor = s.txfBorder;
		
		s.lblFont = image.get(20, schemeNo) | ALPHA_MASK;
		s.lblBack = image.get(21, schemeNo) | ALPHA_MASK;
		s.lblBorder = image.get(22, schemeNo) | ALPHA_MASK;

		s.optFont = image.get(25, schemeNo) | ALPHA_MASK;
		s.optBack = image.get(26, schemeNo) | ALPHA_MASK;
		s.optBorder = image.get(27, schemeNo) | ALPHA_MASK;

		s.cbxFont = image.get(30, schemeNo) | ALPHA_MASK;
		s.cbxBack = image.get(31, schemeNo) | ALPHA_MASK;
		s.cbxBorder = image.get(32, schemeNo) | ALPHA_MASK;
		
		s.acbBorder = image.get(35, schemeNo) | ALPHA_MASK;
		s.acbTrack = image.get(36, schemeNo) | ALPHA_MASK;
		s.acbLast = image.get(37, schemeNo) | ALPHA_MASK;
		s.acbFirst = image.get(38, schemeNo) | ALPHA_MASK;

		s.knobBorder = image.get(40, schemeNo) | ALPHA_MASK;
		s.knobFill = image.get(41, schemeNo) | ALPHA_MASK;
		s.knobTrack = image.get(42, schemeNo) | ALPHA_MASK;
		s.knobTicks = image.get(43, schemeNo) | ALPHA_MASK;
		s.knobNeedle = image.get(44, schemeNo) | ALPHA_MASK;
	}

	// Class attributes and methods start here

	// Scheme number
	public int schemeNo = 0;
	// Panels
	public int pnlFont, pnlTabBack, pnlBack, pnlBorder;
	// Buttons
	public int btnFont, btnOff, btnOver, btnDown, btnBorder;
	// Sliders
	public int sdrTrack, sdrThumb, sdrBorder;
	// TextFields
	public int txfFont, txfBack, txfSelBack, txfBorder, txfCursor;
	// Label
	public int lblFont, lblBack, lblBorder;
	// Option
	public int optFont, optBack, optBorder;
	// Checkbox
	public int cbxFont, cbxBack, cbxBorder;
	// ActivityBar
	public int acbBorder, acbTrack, acbFirst, acbLast;
	// Knobs
	public int knobBorder, knobFill, knobTrack, knobTicks, knobNeedle;
	
	// Transparency level
	private int alpha = 255;
	/**
	 * Create a default (blue) scheme
	 */
	public GCScheme(){
		schemeNo = 0;
		populateScheme(this, schemeNo);		
	}

	/**
	 * Create a scheme for a given scheme number
	 * @param csn
	 */
	public GCScheme (int csn){
		schemeNo = csn;
		populateScheme(this, schemeNo);
	}

	/**
	 *  Copy ctor
	 * @param gcScheme scheme to copy
	 */
	public GCScheme(GCScheme gcScheme){
		schemeNo = gcScheme.schemeNo;
		populateScheme(this, schemeNo);		
	}

	/**
	 * Changes the alpha level for all elements of the scheme.
	 * 
	 * @param alpha in the range 0 (fully transparent) to 255 (fully opaque)
	 */
	public void setAlpha(int alpha){
		this.alpha = (alpha & 0xff);
		int a = this.alpha << 24;
		pnlFont = (pnlFont & 0x00ffffff) | a;
		pnlTabBack = (pnlTabBack & 0x00ffffff) | a;
		pnlBack = (pnlBack & 0x00ffffff) | a;
		pnlBorder = (pnlBorder & 0x00ffffff) | a;
		btnFont = (btnFont & 0x00ffffff) | a;
		btnOff = (btnOff & 0x00ffffff) | a;
		btnOver = (btnOver & 0x00ffffff) | a;
		btnDown = (btnDown & 0x00ffffff) | a;
		btnBorder = (btnBorder & 0x00ffffff) | a;
		sdrTrack = (sdrTrack & 0x00ffffff) | a;
		sdrThumb = (sdrThumb & 0x00ffffff) | a;
		sdrBorder = (sdrBorder & 0x00ffffff) | a;
		txfFont = (txfFont & 0x00ffffff) | a;
		txfBack = (txfBack & 0x00ffffff) | a;
		txfSelBack = (txfSelBack & 0x00ffffff) | a;
		txfBorder = (txfBorder & 0x00ffffff) | a;
		txfCursor = (txfCursor & 0x00ffffff) | a;
		lblFont = (lblFont & 0x00ffffff) | a;
		lblBack = (lblBack & 0x00ffffff) | a;
		lblBorder = (lblBorder & 0x00ffffff) | a;
		optFont = (optFont & 0x00ffffff) | a;
		optBack = (optBack & 0x00ffffff) | a;
		optBorder = (optBorder & 0x00ffffff) | a;
		cbxFont = (cbxFont & 0x00ffffff) | a;
		cbxBack = (cbxBack & 0x00ffffff) | a;
		cbxBorder = (cbxBorder & 0x00ffffff) | a;
		acbBorder = (acbBorder & 0x00ffffff | a);
		acbTrack = (acbTrack & 0x00ffffff | a);
		acbLast = (acbLast & 0x00ffffff | a);
		acbFirst = (acbFirst & 0x00ffffff | a);
		knobBorder = (knobBorder & 0x00ffffff | a);
		knobFill = (knobFill & 0x00ffffff | a);
		knobTrack = (knobTrack & 0x00ffffff | a);
		knobTicks = (knobTicks & 0x00ffffff | a);
		knobNeedle = (knobNeedle & 0x00ffffff | a);
	}

	/**
	 * Get the transparency level
	 * @return 0 - transparent;  255 - opaque
	 */
	public int getAlpha(){
		return alpha;
	}

} // end of class

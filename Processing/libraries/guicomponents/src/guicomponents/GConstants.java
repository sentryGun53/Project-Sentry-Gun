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

import java.awt.font.TextAttribute;

/**
 * @author Peter Lager
 *
 */
public interface GConstants {

	// 0x0???????  user can disable these messages 
	// 0x8???????  always display these messages


	// ### Event constants ###
	// TextField component (GSlider also uses CHANGED)
	public final static int CHANGED = 			0x00000101;	// Text has changed
	public final static int ENTERED = 			0x00000102;	// Enter key pressed
	public final static int SET = 				0x00000103;	// setText() was used

	// GPanel component
	public final static int COLLAPSED = 		0x00000201;	// Panel has been collapsed
	public final static int EXPANDED = 			0x00000202;	// Panel has been expanded
	public final static int DRAGGED = 			0x00000203;	// Panel has been dragged

	// GButton
	public final static int CLICKED = 			0x00000301;
	public final static int PRESSED = 			0x00000302;
	public final static int RELEASED = 			0x00000303;

	// GCheckbox & GOption
	public final static int SELECTED = 			0x00000401;
	public final static int DESELECTED = 		0x00000402;

	// GRoundControl
	public final static int CTRL_ANGULAR = 		0x00000501;
	public final static int CTRL_HORIZONTAL = 	0x00000502;
	public final static int CTRL_VERTICAL = 	0x00000503;

	// GWindow
	public final static int EXIT_APP = 			0x00000f01;
	public final static int CLOSE_WINDOW = 		0x00000f02;
	public final static int KEEP_OPEN = 		0x00000f03;


	// ### GUI build constants ###
	public final static int ADD_DUPLICATE = 	0x00010101;
	public final static int USER_COL_SCHEME = 	0x00010102;
	public final static int DISABLE_AUTO_DRAW =	0x00010103;

	// ### Scroll bar policy constants ###
	/** Do not create or display any scrollbars for the text area. */
	public static final int SCROLLBARS_NONE = 				0x0000;
	/** Create and display vertical scrollbar only. */
	public static final int SCROLLBARS_VERTICAL_ONLY = 		0x0001;
	/** Create and display horizontal scrollbar only. */
	public static final int SCROLLBARS_HORIZONTAL_ONLY = 	0x0002;
	/** Create and display both vertical and horizontal scrollbars. */
	public static final int SCROLLBARS_BOTH = 				0x0003;

	public static final int SCROLLBARS_AUTOHIDE =			0x1000;

	// ### Scroll bar type constants ###
	/** Create and display vertical scrollbar only. */
	public static final int SCROLLBAR_VERTICAL = 1;
	/** Create and display horizontal scrollbar only. */
	public static final int SCROLLBAR_HORIZONTAL = 2;


	// ### Error MessageTypes ###
	public final static int RUNTIME_ERROR = 	0xf0000000;
	// Event method handler errors
	public final static int MISSING = 			0x01000001;	// Can't find standard handler
	public final static int NONEXISTANT = 		0x01000002;
	public final static int EXCP_IN_HANDLER =	0x81000003;	// Exception in event handler
	// PeasyCam errors
	public final static int NOT_PEASYCAM =		0x82000001; // Not a PeasyCam object
	public final static int HUD_UNSUPPORTED = 	0x82000002; // HUD not supported
	public final static int INVALID_STATUS = 	0x82000003; // HUD not supported

	// GTextField text scroll constants
	public final static int SCROLL_UP =			0x00000111;	// Scroll text up
	public final static int SCROLL_DOWN =		0x00000112;	// Scroll text down
	public final static int SCROLL_LEFT =		0x00000113;	// Scroll text left
	public final static int SCROLL_RIGHT =		0x00000114;	// Scroll text right


	public static final TextAttribute FAMILY = TextAttribute.FAMILY;
	//	public static final TextAttribute FAMILY = new TextAttribute("family");

	//	public static final TextAttribute WEIGHT = new TextAttribute("weight");
	public static final TextAttribute WEIGHT = TextAttribute.WEIGHT;

	public static final Float WEIGHT_EXTRA_LIGHT = new Float(0.5f);
	public static final Float WEIGHT_LIGHT = new Float(0.75f);
	public static final Float WEIGHT_DEMILIGHT = new Float(0.875f);
	public static final Float WEIGHT_REGULAR = new Float(1.0f);
	public static final Float WEIGHT_SEMIBOLD = new Float(1.25f);
	public static final Float WEIGHT_MEDIUM = new Float(1.5f);
	public static final Float WEIGHT_DEMIBOLD = new Float(1.75f);
	public static final Float WEIGHT_BOLD = new Float(2.0f);
	public static final Float WEIGHT_HEAVY = new Float(2.25f);
	public static final Float WEIGHT_EXTRABOLD = new Float(2.5f);
	public static final Float WEIGHT_ULTRABOLD = new Float(2.75f);


	//	public static final TextAttribute WIDTH = new TextAttribute("width");
	public static final TextAttribute WIDTH = TextAttribute.WIDTH;

	public static final Float WIDTH_CONDENSED = new Float(0.75f);
	public static final Float WIDTH_SEMI_CONDENSED = new Float(0.875f);
	public static final Float WIDTH_REGULAR = new Float(1.0f);
	public static final Float WIDTH_SEMI_EXTENDED = new Float(1.25f);
	public static final Float WIDTH_EXTENDED = new Float(1.5f);


	public static final TextAttribute POSTURE = TextAttribute.POSTURE;

	public static final Float POSTURE_REGULAR = new Float(0.0f);
	public static final Float POSTURE_OBLIQUE = new Float(0.20f);

	// Acceptable values - float
	public static final TextAttribute SIZE = TextAttribute.SIZE;


	public static final TextAttribute SUPERSCRIPT = TextAttribute.SUPERSCRIPT;

	public static final Integer SUPERSCRIPT_SUPER = new Integer(1);
	public static final Integer SUPERSCRIPT_SUB = new Integer(-1);


	// Any java.awt.Color object
	public static final TextAttribute FOREGROUND = TextAttribute.FOREGROUND;
	public static final TextAttribute BACKGROUND = TextAttribute.BACKGROUND;

	public static final TextAttribute STRIKETHROUGH = TextAttribute.STRIKETHROUGH;

	public static final Boolean STRIKETHROUGH_ON = new Boolean(true);
	public static final Boolean STRIKETHROUGH_OFF = new Boolean(false);


	public static final TextAttribute JUSTIFICATION = TextAttribute.JUSTIFICATION;
	public static final Float JUSTIFICATION_FULL = new Float(1.0f);
	public static final Float JUSTIFICATION_NONE = new Float(0.0f);

}

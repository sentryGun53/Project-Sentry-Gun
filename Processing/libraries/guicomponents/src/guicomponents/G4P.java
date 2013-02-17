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

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PFont;
import processing.core.PGraphics;
import processing.core.PMatrix;
import processing.core.PMatrix2D;
import processing.core.PMatrix3D;
import processing.core.PStyle;

/**
 * This class has only static methods. It keeps track of all GComponents created.
 * Its primary role is encapsulated in the draw() method which can override
 * the default Processing drawing mechanism.
 * 
 * @author Peter Lager
 *
 */
public class G4P implements PConstants, GConstants {

	// Keeps info about all applets and their controls
	private static HashMap<PApplet, AppletInfo> applets = new HashMap<PApplet, AppletInfo>();

	// Will be set when and first window is added
	public static PApplet mainWinApp = null;

	public static PStyle g4pStyle = null;

	public static boolean messages = true;

	/**
	 * An array of names representing all the available fonts.
	 */
	final public static String[] fontFamilies = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();

	
	/** INTERNAL USE ONLY  Cursor over changer */
	private static GCursorImageChanger mcd = new GCursorImageChanger();
	public static boolean overControl = false;
	public static boolean cursorChangeEnabled = false;
	public static int mouseOff = ARROW;
	public static int mouseOver = HAND;

	/**
	 * See if this font family is available on this user's computer system
	 * and return the family name if found. If the font family is unavailable
	 * then it returns the name "Dialog"
	 * @param family
	 * @return
	 */
	public static String getFamily(String family){
		for(String f : fontFamilies)
			if(f.equalsIgnoreCase(family))
				return f;
		return "Dialog";
	}


	/**
	 * Enables or disables cursor over component change. <br>
	 * This method is ignore if an applet or window has not been
	 * registered with G4P or the mouse over state is not being changed.
	 * 
	 * This is ignored if no G4P components have been created yet
	 * @param enable
	 */
	public static void setMouseOverEnabled(boolean enable){
		if(cursorChangeEnabled != enable && mainWinApp != null){
			if(enable == false){
				Set<PApplet> apps = applets.keySet();
				for(PApplet pa : apps)
					pa.cursor(mouseOff);				
			}
			cursorChangeEnabled = enable;
		}
	}

	public static void refresh(){
		Set<PApplet> apps = applets.keySet();
		for(PApplet pa : apps)
			pa.repaint();				
	}

	/**
	 * Inform G4P which cursor shapes will be used.
	 * Initial values are ARROW (off) and HAND (over)
	 * 
	 * @param cursorOff
	 * @param cursorOver
	 */
	public static void cursor(int cursorOff, int cursorOver){
		mouseOff = cursorOff;
		mouseOver = cursorOver;
	}

	/**
	 * Inform G4P which cursor to use for mouse over.
	 * 
	 * @param cursorOver
	 */
	public static void cursor(int cursorOver){
		mouseOver = cursorOver;
	}

	/**
	 * INTERNAL USE ONLY <br>
	 * This should be called by all ctors in GComponent and since all GUI 
	 * components inherit from GComponent and are required to call a 
	 * GComponent ctor then all GUI components will automatically be 
	 * registered in the set.
	 * @param theApplet 
	 * 
	 * @param c the component that has been created.
	 */
	public static void addComponent(PApplet app, GComponent c){
		if(g4pStyle == null)
			getStyle();
		// info exists for all GWindow objects
		AppletInfo info = applets.get(app);
		// If info == null then this must be the main Processing window and
		// since the matrix may have already been changed e.g. creating 
		// a PeaseyCam object will do this, so recalculate the original matrix.
		if(info == null){
			// We have the main applet window so create the AppletInfo for it
			info = addWindow(app);
			if(app.g.is2D())
				info.orgMatrix = new PMatrix2D();
			else
				info.orgMatrix = 
					new PMatrix3D( 
							1, 0, 0, -0.5f * app.width,
							0, 1, 0, -0.5f * app.height,
							0, 0, 1, -0.5f * app.height / (float)Math.tan(Math.PI / 6.0),
							0, 0, 0, 1
					);
		}
		info.addComponent(c);
	}

	/**
	 * INTERNAL USE ONLY <br>
	 * Remove a component so that it is permanently unavailable. <br>
	 * Do not call this method directly otherwise your program will crash. <br>
	 * Use the components dispose method instead.
	 * 
	 * @param c the component to remove
	 */
	public static void dumpComponent(GComponent c){
		AppletInfo info = applets.get(c.getPApplet());
		if(info != null)
			info.removeControl(c);
	}

	/**
	 * Add the window's PApplet object to G4P if not already there. <br>
	 * @param window
	 */
	public static void addWindow(GWindow window){
		if(window != null && window.papplet != null)
			addWindow(window.papplet);
	}

	/**
	 * Add a PApplet object to G4P if not already there. <br>
	 * The only time you might want to use this is when you are mixing
	 * G4P and OpenGL. In which case call this method from setup() immediately
	 * after the call to size() and before any statements that might alter
	 * the initial drawing matrix e.g. <br>
	 * <pre>void setup(){</pre> <br>
	 * <pre>  size(480, 320);</pre> <br> 
	 * <pre>  G4P.addWindow(this);</pre> <br>
	 * @param app the PApplet object to add
	 */
	public static AppletInfo addWindow(PApplet app){
		AppletInfo info = applets.get(app); // avoid duplicates
		if(info == null){
			// If this is the first time then initialise mouse over ability
			if(applets.isEmpty()){
				mainWinApp = app;
				mainWinApp.registerPost(mcd);
			}
			info = new AppletInfo(app);
			applets.put(app, info);
		}
		return info;
	}

	/**
	 * INTERNAL USE ONLY <br>
	 * Remove control window - called when a ControlWindow is closed
	 * for good.
	 *  
	 * @param window
	 */
	public static void removeWindow(GWindow window){
		if(window != null && window.papplet != null)
			applets.remove(window.papplet);
	}

	/**
	 * Determines whether a window is still open or has been closed
	 * @param window
	 * @return true if the window is still open
	 */
	public static boolean isWindowActive(GWindow window){
		if(window != null && window.papplet != null)
			return applets.containsKey(window.papplet);
		else
			return false;
	}

	/**
	 * INTERNAL USE ONLY
	 */
	private static void getStyle(){
		PGraphics temp = new PGraphics();

		g4pStyle = temp.getStyle();

		g4pStyle.rectMode = CORNER;
		g4pStyle.ellipseMode = DIAMETER;
		g4pStyle.imageMode = CORNER;
		g4pStyle.shapeMode = CORNER;

		g4pStyle.colorMode = RGB;
		g4pStyle.colorModeA = 255.0f;
		g4pStyle.colorModeX = 255.0f;
		g4pStyle.colorModeY = 255.0f;
		g4pStyle.colorModeZ = 255.0f;
	}

	/**
	 * Set the color scheme to be used by G4P<br>
	 * Only reqd if different from the default blue scheme to be
	 * global specify before creating GUI components
	 * 
	 * Available schemes:
	 * BLUE_SCHEME, GREEN_SCHEME, RED_SCHEME, PURPLE_SCHEME
	 * YELLOW_SCHEME, CYAN_SCHEME, GREY_SCHEME
	 * 
	 * @param theApplet
	 * @param schemeNo GCScheme.GREEN_SCHEME
	 */
	public static void setColorScheme(PApplet theApplet, int schemeNo){
		if(theApplet != null)
			GComponent.globalColor = GCScheme.getColor(theApplet,  schemeNo);
	}

	/**
	 * Set the font type and size to be used by G4P<br>
	 * Only reqd if different from the default "Serif" 11 <br>
	 * to be global specify before creating GUI components
	 * 
	 * @param theApplet
	 * @param fontName name of font
	 * @param fontSize font size
	 * 
	 * @deprecated
	 */
	public static void setFont(PApplet theApplet, String fontName, int fontSize){
		if(theApplet != null)
			GComponent.globalFont = GFont.getFont(theApplet, fontName, fontSize);
	}

	/**
	 * Set the global font to be used by all controls. <br>
	 * You can override the font used by individual controls with
	 * the equivalent setFont method for the control.
	 * 
	 * @param fontName font family name
	 * @param fontSize font size
	 */
	public static void setFont(String fontName, int fontSize){
		setFont(fontName, fontSize, Font.PLAIN);
	}

	/**
	 * Set the global font to be used by all controls. <br>
	 * You can override the font used by individual controls with
	 * the equivalent setFont method for the control. <br>
	 * 
     * The style argument is an integer bitmask that may
     * be PLAIN, or a bitwise union of BOLD and/or ITALIC
     * (for example, ITALIC or BOLD|ITALIC). If the style is
     * unrecognised it will default to PLAIN.
	 * 
	 * @param fontName font family name
	 * @param fontSize font size
	 * @param style style e.g. Font.PLAIN
	 */
	public static void setFont(String fontName, int fontSize, int style){
		GComponent.fGlobalFont = new Font(fontName, fontSize, style);
	}
	
	/**
	 * Set the global font to be used by all controls. <br>
	 * You can override the font used by individual controls with
	 * the equivalent setFont method for the control. <br>
	 * 
	 * @param font the java.awt.Font to use
	 */
	public static void setFont(Font font){
		GComponent.fGlobalFont = font;
	}
	
	/**
	 * Set the global font to be used by all controls. <br>
	 * You can override the font used by individual controls with
	 * the equivalent setFont method for the control. <br>
	 * 
	 * @param pfont the Processing font to use
	 */
//	public static void setFont(PFont pfont){
//		GComponent.fGlobalFont = pfont.getFont();
//	}
	
	
	
	/**
	 * When you first use G4P(app) it switches off auto draw for the 
	 * PApplet app.
	 * 
	 */
	public static void draw(PApplet app){
		AppletInfo info = applets.get(app);
		if(info != null && info.paControls.size() > 0){
			// If this method has been called and this applet is still using autodraw
			// then it is time to take over the responsibility for drawing from
			// the applet.
			if(info.autoDrawOn){
				for(GComponent comp : info.paControls){
					if(comp.getParent() == null){
						comp.regDraw = false;
						comp.getPApplet().unregisterDraw(comp);
					}
				}
				info.autoDrawOn = false;
			}
			// Draw the components on the mainWinApp only.
			// Note that GPanels will call the appropriate
			// draw methods for the components on them
			// Now setup for 2D display
			app.noLights();
			app.pushMatrix();
			app.hint(PConstants.DISABLE_DEPTH_TEST);
			app.resetMatrix();
			app.applyMatrix(info.orgMatrix);
			// Draw components
			for(GComponent comp : info.paControls){
				if(comp.getParent() == null && comp.getPApplet() == app)
					comp.draw();
			}
			// Done with drawing components 
			app.hint(PConstants.ENABLE_DEPTH_TEST);
			app.popMatrix();
		}
	}

	/**
	 * Unregister all GComponents that have no parent from PApplets auto draw
	 * mechanism. The G4P.draw() will now be responsible for drawing the 
	 * components.
	 * 
	 * It is called once on the first call to G4P.draw()
	 * 
	 */
	private static void unregisterFromPAppletDraw(PApplet app) {
		AppletInfo info = applets.get(app);
		if(info != null){
			//		List<GComponent> paControls = info.paControls);
			for(GComponent comp : info.paControls){
				if(comp.getParent() == null){
					comp.regDraw = false;
					comp.getPApplet().unregisterDraw(comp);
				}
			}
			info.autoDrawOn = false;
		}
	}

	/**
	 * Is autodraw on for the PApplet app?
	 */
	public static boolean isAutoDrawOn(PApplet app){
		AppletInfo info = applets.get(app);
		if(info != null)
			return info.autoDrawOn;
		else 
			return false;
	}

	/**
	 * G4P has a range of support messages eg <br>if you create a GUI component 
	 * without an event handler or, <br>a slider where the visible size of the
	 * slider is less than the difference between min and max values.
	 * 
	 * This method allows the user to enable (default) or disable this option. If
	 * disable then it should be called before any GUI components are created.
	 * 
	 * @param enable
	 */
	public static void messagesEnabled(boolean enable){
		messages = enable;
	}

	/**
	 * This will sort the GUI controls in a secondary window.
	 * @param window the GWindow object
	 */
	public static void setDrawOrder(GWindow window){
		PApplet app = window.papplet;
		setDrawOrder(app);
	}

	/**
	 * If you are using GPanel or GCombo it would be useful to call this method in setup
	 * or customGUI (if using GUI builder tool). <br>
	 * DO NOT CALL this method from inside the draw() method. <br>
	 * This will sort the draw order for GUI controls based on their z attribute - note that
	 * a control will be drawn before a control with a higher z. If two controls have the 
	 * same z value, then the controls are ordered by their vertical screen position. This means
	 * that controls near the bottom of the display will be drawn before those nearer the top.
	 * @param windowApp the PApplet object
	 */
	public static void setDrawOrder(PApplet app){
		AppletInfo info = applets.get(app);
		if(info != null && info.autoDrawOn ){
			Collections.sort(info.paControls, new GComponent.Z_Order());
			// Change physical order in PApplet by removing then adding the components
			if(info.autoDrawOn){
				for(GComponent comp : info.paControls){
					if(comp.getParent() == null)
						app.unregisterDraw(comp);
				}
				for(GComponent comp : info.paControls){
					if(comp.getParent() == null)
						app.registerDraw(comp);
				}	
			}
		}
	}

	/**
	 * INTERNAL USE ONLY <br>
	 * Used to bring a panel to the front of the display. <br>
	 * Do not use this method directly. 
	 */
	public static void moveToFrontForDraw(GComponent comp){
		PApplet app = comp.getPApplet();
		AppletInfo info = applets.get(app);
		if(info != null && info.paControls.remove(comp)){
			info.paControls.add(comp);
			if(comp.parent == null && app != null && info.autoDrawOn){
				app.unregisterDraw(comp);
				app.registerDraw(comp);
			}
		}
	}

	/**
	 * INTERNAL USE ONLY <br>
	 * Used to ensure the panel is the last component to be tested for mouse events. <br>
	 * Do not use this method. 
	 */
	public static void moveToFrontForMouse(GComponent comp){
		PApplet app = comp.getPApplet();
		AppletInfo info = applets.get(app);
		if(info != null && info.paControls.remove(comp)){
			info.paControls.add(comp);
			app.unregisterMouseEvent(comp);
			app.registerMouseEvent(comp);
		}
	}

	// Only used inside G4P to keep track of windows and associated applets
	private static class AppletInfo {
		public PMatrix orgMatrix;
		public List<GComponent> paControls;
		public boolean autoDrawOn = true;

		/**
		 * @param papplet
		 */
		public AppletInfo(PApplet papplet) {
			if(papplet.g.is3D())
				orgMatrix = papplet.getMatrix((PMatrix3D)null);
			else
				orgMatrix = papplet.getMatrix((PMatrix2D)null);
			paControls = new LinkedList<GComponent>();
		}

		/**
		 * If the component is not null and has not already been added then
		 * add it to the list and return true. Otherwise the operation is
		 * ignored and the method returns false;
		 * @param comp the component to add
		 * @return true if successfully added else false
		 */
		public boolean addComponent(GComponent comp){
			if(comp == null)
				return false;
			if(paControls.contains(comp)){
				GMessenger.message(ADD_DUPLICATE, comp ,null);
				return false;
			}
			paControls.add(comp);
			return true;
		}

		/**
		 * Removes the component from the 
		 * @param c
		 * @return
		 */
		public boolean removeControl(GComponent c){
			return paControls.remove(c);
		}

	}

}

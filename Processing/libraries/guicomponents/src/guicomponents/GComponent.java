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

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PFont;
import processing.core.PGraphicsJava2D;


/**
 * CLASS FOR INTERNAL USE ONLY
 * 
 * Abstract base class for all GUI components
 * 
 * @author Peter Lager
 *
 */
abstract public class GComponent implements PConstants, GConstants, Comparable<Object> {

	// Determines how position and size parameters are interpreted when
	// a control is created
	// Introduced V3.0
	private static int control_mode = PApplet.CORNER;

	/**
	 * Change the way position and size parameters are interpreted when a control is created. 
	 * There are 3 modes. <br><pre>
	 * PApplet.CORNER	 (x, y, w, h)
	 * PApplet.CORNERS	 (x0, y0, x1, y1)
	 * PApplet.CENTER	 (cx, cy, w, h) </pre><br>
	 * 
	 * @param mode illegal values are ignored leaving the mode unchanged
	 */
	public static void ctrlMode(int mode){
		switch(mode){
		case PApplet.CORNER:	// (x, y, w, h)
		case PApplet.CORNERS:	// (x0, y0, x1, y1)
		case PApplet.CENTER:	// (cx, cy, w, h)
			control_mode = mode;
		}
	}

	/**
	 * Get the control creation mode @see ctrlMode(int mode)
	 * @return
	 */
	public static int getCtrlMode(){
		return control_mode;
	}

	/**
	 * INTERNAL USE ONLY
	 * This holds a reference to the GComponent that currently has the
	 * focus.
	 * A component loses focus when another component takes focus with the
	 * takeFocus() method. The takeFocus method should use focusIsWith.loseFocus()
	 * before setting its value to the new component 
	 */
	protected static GComponent focusIsWith = null; // READ ONLY

	protected static GComponent keyFocusIsWith = null;

	/**
	 * INTERNAL USE ONLY
	 * Keeps track of the component the mouse is over so the mouse
	 * cursor can be changed if we wish.
	 */
	protected static GComponent cursorIsOver;

	/*
	 * INTERNAL USE ONLY
	 * Used to track mouse required by GButton, GCheckbox, GHorzSlider
	 * GVertSlider, GPanel classes
	 */
	protected int mdx = Integer.MAX_VALUE, mdy = Integer.MAX_VALUE;

	/** the font to be used for a control */
	protected Font fLocalFont = fGlobalFont;
	
	public static GCScheme globalColor;
	public GCScheme localColor;
	// Replacements for global colour scheme above V3
	public static int globalColorScheme = FCScheme.BLUE_SCHEME;
	protected int localColorScheme = globalColorScheme;
	protected int[] palette = null;
	protected Color[] jpalette = null;

	// Change local scheme v3
	public void setLocalColorScheme(int cs){
		cs = Math.abs(cs) % 16; // Force into valid range
		if(localColorScheme != cs || palette == null){
			localColorScheme = cs;
			palette = FCScheme.getColor(winApp, localColorScheme);
			jpalette = FCScheme.getJavaColor(winApp, localColorScheme);
			bufferInvalid = true;
		}
	}

	// This is the new global font
	public static Font fGlobalFont = new Font("Dialog", Font.PLAIN, 11);
	
	public static PFont globalFont;
	public PFont localFont;

	protected static int componentNo = 0;

	/*
	 * Padding around fonts
	 */
	protected final static int PADH = 4;
	protected final static int PADV = 2;

	// Increment to be used if on a GPanel
	protected final static int Z_PANEL = 1024;

	// Components that don't release focus automatically
	// i.e. GTextField
	protected final static int Z_STICKY = 0;

	// Components that automatically releases focus when appropriate
	// e.g. GButton
	protected final static int Z_SLIPPY = 24;

	/** 
	 * This is a reference the the PApplet that was used to create the 
	 * component - in all cases this should be launching or main PApplet.
	 *  
	 * It must be set by the constructor 
	 */
	protected PApplet winApp;

	/** Link to the parent panel (if null then it is on main window) */
	protected GComponent parent = null;

	/**
	 * A list of child GComponents added to this component
	 * Created and used by GPanel and GCombo classes
	 */
	protected LinkedList<GComponent> children = null;
	
	protected HotSpot[] hotspots = null;
	protected int currSpot = -1;
	
	protected int whichHotSpot(float px, float py){
		if(hotspots == null) return -1;
		int hs = -1;
		for(int i = 0; i < hotspots.length; i++){
			if(hotspots[i].contains(px, py)){
				hs = hotspots[i].id;
				break;
			}
		}
		return hs;
	}
	
	protected int getCurrHotSpot(){
		return currSpot;
	}
	
	/** The object to handle the event */
	protected Object eventHandlerObject = null;
	/** The method in eventHandlerObject to execute */
	protected Method eventHandlerMethod = null;
	/** the name of the method to handle the event */ 
	protected String eventHandlerMethodName;
	
	/** Text value associated with component */
	protected String text = "";
	// The styled version of text
	protected StyledString stext = null;
	
	// These next variable will be abandoned in next version
	protected int textWidth;
	protected int textAlignHorz = GAlign.LEFT;
	protected int textAlignVert = GAlign.MIDDLE;
	protected int alignX = 0;
	protected int alignY = 0;



	/** Top left position of component in pixels (relative to parent or absolute if parent is null) 
	 * (changed form int data type in V3*/
	protected float x, y;
	/** Width and height of component in pixels for drawing background (changed form int data type in V3*/
	protected float width, height;
	/** Half sizes reduces programming complexity later */
	protected float halfWidth, halfHeight;
	/** The cenre of the control */
	protected float cx, cy;
	/** The angle to control is rotated (radians) */
	protected float rotAngle;
	/** Introduced V3 to speed up AffineTransform operations */
	protected double[] temp = new double[2];
	
	/** 
	 * Position over control corrected for any transformation. <br>
	 * [0,0] is top left corner
	 */
	protected float ox, oy;

	/** Used to when components overlap */
	public int z = 0;

	/** Simple tag that can be used by the user */
	public String tag;

	/** Allows user to specify a number for this component */
	public int tagNo;

	/** Is the component visible or not */
	protected boolean visible = true;

	/** Is the component enabled to generate mouse and keyboard events */
	protected boolean enabled = true;

	/** 
	 * Is the component available for mouse and keyboard events.
	 * This is on;y used internally to prevent user input being
	 * processed during animation. new to V3
	 */
	protected boolean available = true;

	/** The border width for this component : default value is 0 */
	protected int border = 0;

	/** Whether to show background or not */
	protected boolean opaque = true;

	// The event type use READ ONLY
	public int eventType = 0;

	// New to V3 components have an image buffer which is only redrawn if 
	// it has been invalidated
	protected PGraphicsJava2D buffer = null;
	protected boolean bufferInvalid = true;
	
	
	/**
	 * Remember what we have registered for.
	 */
	protected boolean regDraw = false;
	protected boolean regMouse = false;
	protected boolean regPre = false;
	protected boolean regKey = false;

	/**
	 * Prevent uninitialised instantiation
	 */
	@SuppressWarnings("unused")
	private GComponent() { }

	/**
	 * INTERNAL USE ONLY
	 * This constructor MUST be called by all constructors  
	 * of any child class e.g. GPanel, GLabel etc.
	 * 
	 * Each component registers itself with G4P, the first component 
	 * register the mainWinApp for later use.
	 * 
	 * Only create the GScheme the first time it is called.
	 * 
	 * @param theApplet
	 * @param x
	 * @param y
	 */
	public GComponent(PApplet theApplet, int x, int y){
		winApp = theApplet;
		tag = "#" + PApplet.nf(componentNo++, 5) + " " + this.getClass().getSimpleName();
		z = 0;
		if(globalColor == null)
			globalColor = GCScheme.getColor(theApplet);
		localColor = new GCScheme(globalColor);
		if(globalFont == null)
			globalFont = GFont.getDefaultFont(theApplet);
		localFont = globalFont;
		this.x = x;
		this.y = y;
		G4P.addComponent(winApp, this);
	}

	public GComponent(PApplet theApplet, float p0, float p1, float p2, float p3) {
		this.winApp = theApplet;
		setPositionAndSize(p0, p1, p2, p3);
		rotAngle = 0;
		z = 0;
		palette = FCScheme.getColor(winApp, localColorScheme);
		jpalette = FCScheme.getJavaColor(winApp, localColorScheme);
		fLocalFont = fGlobalFont;
		G4P.addComponent(winApp, this);
	}

	/**
	 * Calculate all the variables that determine the position and size of the
	 * control. This depends on <pre>control_mode</pre>
	 * 
	 */
	protected void setPositionAndSize(float n0, float n1, float n2, float n3){
		switch(control_mode){
		case PApplet.CORNER:	// (x,y,w,h)
			x = n0; y = n1; width = n2; height = n3;
			halfWidth = width/2; halfHeight = height/2;
			cx = x + halfWidth; cy = y + halfHeight;
			break;			
		case PApplet.CORNERS:	// (x0,y0,x1,y1)
			x = n0; y = n1; width = n2 - n0; height = n3 - n1;
			halfWidth = width/2; halfHeight = height/2;
			cx = x + halfWidth; cy = y + halfHeight;
			break;
		case PApplet.CENTER:	// (cx,cy,w,h)
			cx = n0; cy = n1; width = n2; height = n3;
			halfWidth = width/2; halfHeight = height/2;
			x = cx - halfWidth; y = cy + halfHeight;
			break;
		}
	}

	/**
	 * The control to be added has absolute screen coordinates.
	 * 
	 * @param c
	 */
	public void addAbsoluteControl(GComponent c){
		// @TODO really need to test this
		c.x -= x; c.y -= y;
		c.cx -= x; c.cy -= y;
		c.parent = this;
		children.addLast(c);
	}

	/**
	 * The control to be added has screen coordinates relative to this control
	 * @param c
	 */
	public void addRelativeControl(GComponent c){
		// @TODO really need to test this
		c.cx = c.x + c.halfWidth - halfWidth;
		c.cy = c.y + c.halfHeight - halfHeight;

		c.parent = this;
		children.addLast(c);
	}

	/**
	 * This will set the rotation of the control to angle overwriting
	 * any previous rotation set. Then it calculates the centre position
	 * so that the original top left corner of the control will be the 
	 * position indicated by x,y with respect to the top left corner of
	 * parent
	 * 
	 * @param c
	 * @param x
	 * @param y
	 * @param angle
	 */
	public void addCompoundControl(GComponent c, float x, float y, float angle){
		if(angle == 0)
			angle = c.rotAngle;
		// In child control reset the control so it centred about the origin
		c.x = x; c.y = y;
		
		switch(control_mode){
		case PApplet.CORNER:
		case PApplet.CORNERS:
			// Rotate about top corner
			c.temp[0] = c.halfWidth;
			c.temp[1] = c.halfHeight;
			AffineTransform aff = new AffineTransform();
			aff.setToRotation(angle);
			aff.transform(c.temp, 0, c.temp, 0, 1);
			c.cx = (float)c.temp[0] + x - halfWidth;
			c.cy = (float)c.temp[1] + y - halfHeight;
			break;
		case PApplet.CENTER:
			// Rotate about centre
			// @TODO rotate about center
			break;
		}
		
		
		c.rotAngle = angle;
		// Add to parent
		c.parent = this;
		// The parent will take over drawing the child
		winApp.unregisterDraw(c);
		c.regDraw = false;
		c.setZ(z);
		if(children == null)
			children = new LinkedList<GComponent>();
		children.addLast(c);
		Collections.sort(children, new Z_Order());
	}
	

	public boolean addXXX(GComponent c){
		if(c == null || children.contains(c)){
			if(G4P.messages)
				System.out.println("Either the child component doesn't exist or has already been added to this component");
			return false;
		} else {
			c.parent = this;
			children.add(c);
			// The parent will take over drawing the child
			winApp.unregisterDraw(c);
			c.regDraw = false;
			if(localColor.getAlpha() < 255)
				c.setAlpha(localColor.getAlpha());
			Collections.sort(children, new Z_Order());
			return true;
		}
	}

	// This is for visualisation
	public void setRotation(float rot){
		this.rotAngle = rot;
	}

	/**
	 * Get an affine transformation that is the compound of all 
	 * transformations including parents
	 * @param aff
	 * @return
	 */
	public AffineTransform getTransform(AffineTransform aff){
		if(parent != null)
			aff = parent.getTransform(aff);
		aff.translate(cx, cy);
		aff.rotate(rotAngle);
		return aff;
	}

	/**
	 * This method takes a position px, py and calulates the equivalent
	 * position [ox,oy] as is no transformations have taken place and
	 * the origin is the top-left corner of the control.
	 * @param px
	 * @param py
	 */
	protected void calcTransformedOrigin(float px, float py){
		AffineTransform aff = new AffineTransform();
		aff = getTransform(aff);
		temp[0] = px; temp[1] = py;
		try {
			aff.inverseTransform(temp, 0, temp, 0, 1);
			ox = (float) temp[0] + halfWidth;
			oy = (float) temp[1] + halfHeight;
		} catch (NoninvertibleTransformException e) {
		}
	}
	
	
	/**
	 * NOT REQD
	 * Remove this method from the release version performs the same task as 
	 * calcTransformedOrigin(px,py) but also tests to see if it is over
	 * the control.
	 * 
	 * @param px
	 * @param py
	 * @return
	 */
	public boolean contains(float px, float py){
		AffineTransform aff = new AffineTransform();
		aff = getTransform(aff);
		temp[0] = px; temp[1] = py;
		try {
			aff.inverseTransform(temp, 0, temp, 0, 1);
		} catch (NoninvertibleTransformException e) {
			e.printStackTrace();
			return false;
		}
		ox = (float) temp[0] + halfWidth;
		oy = (float) temp[1] + halfHeight;
		boolean over = (ox >= 0 && ox <= width && oy >= 0 && oy <= height);
		return over;
	}


	/**
	 * Attempt to create the default event handler for the component class. 
	 * The default event handler is a method that returns void and has a single
	 * parameter of the same type as the component class generating the
	 * event and a method name specific for that class. 
	 * 
	 * @param handlerObj the object to handle the event
	 * @param methodName the method to execute in the object handler class
	 * @param parameters the parameter classes.
	 */
	@SuppressWarnings("rawtypes")
	protected void createEventHandler(Object handlerObj, String methodName, Class[] parameters){
		try{
			eventHandlerMethod = handlerObj.getClass().getMethod(methodName, parameters );
			eventHandlerObject = handlerObj;
			eventHandlerMethodName = methodName;
		} catch (Exception e) {
			GMessenger.message(MISSING, this, new Object[] {methodName, parameters});
			eventHandlerObject = null;
		}
	}

	/**
	 * Attempt to create the default event handler for the component class. 
	 * The default event handler is a method that returns void and has a single
	 * parameter of the same type as the component class generating the
	 * event and a method name specific for that class. 
	 * 
	 * @param obj the object to handle the event
	 * @param methodName the method to execute in the object handler class
	 */
	public void addEventHandler(Object obj, String methodName){
		try{
			eventHandlerObject = obj;
			eventHandlerMethodName = methodName;
			eventHandlerMethod = obj.getClass().getMethod(methodName, new Class[] {this.getClass() } );
		} catch (Exception e) {
			GMessenger.message(NONEXISTANT, this, new Object[] {methodName, new Class[] { this.getClass() } } );
			eventHandlerObject = null;
			eventHandlerMethodName = "";
		}
	}

	/**
	 * Attempt to create the default event handler for the component class. 
	 * The default event handler is a method that returns void and has a single
	 * parameter of the same type as the component class generating the
	 * event and a method name specific for that class. 
	 * 
	 * @param obj the object to handle the event
	 * @param methodName the method to execute in the object handler class
	 * @param parameters the parameter classes.
	 */
	@SuppressWarnings("rawtypes")
	public void addEventHandler(Object obj, String methodName, Class[] parameters){
		if(parameters == null)
			parameters = new Class[0];
		try{
			eventHandlerObject = obj;
			eventHandlerMethodName = methodName;
			eventHandlerMethod = obj.getClass().getMethod(methodName, parameters );
		} catch (Exception e) {
			GMessenger.message(NONEXISTANT, eventHandlerObject, new Object[] {methodName, parameters } );
			eventHandlerObject = null;
			eventHandlerMethodName = "";
		}
	}

	/**
	 * Attempt to fire an event for this component.
	 * 
	 * The method called must have a single parameter which is the object 
	 * firing the event.
	 * If the method to be called is to have different parameters then it should
	 * be overridden in the child class
	 * The method 
	 */
	protected void fireEvent(){
		if(eventHandlerMethod != null){
			try {
				eventHandlerMethod.invoke(eventHandlerObject, new Object[] { this });
			} catch (Exception e) {
				GMessenger.message(EXCP_IN_HANDLER, eventHandlerObject, 
						new Object[] {eventHandlerMethodName, e } );
			}
		}		
	}

	/**
	 * Get the PApplet object
	 * @return the PApplet this component is drawn on
	 */
	public PApplet getPApplet(){
		return winApp;
	}

	/*
	 * The following methods are related to handling focus.
	 * Most components can loose focus without affecting their state
	 * but TextComponents that support mouse text selection need to 
	 * clear this selection when they loose focus. Also components
	 * like GCombo that comprise other G4P components need additional
	 * work
	 */

	/**
	 * This method stub is overridden in GPanel
	 */
	protected void bringToFront(){
		if(parent != null)
			parent.bringToFront();
	}

	/**
	 * Give the focus to this component but only after allowing the 
	 * current component with focus to release it gracefully. <br>
	 * Always cancel the keyFocusIsWith irrespective of the component
	 * type. If the component needs to retain keyFocus then override this
	 * method in that class e.g. GCombo
	 */
	protected void takeFocus(){
		if(focusIsWith != null && focusIsWith != this)
			focusIsWith.loseFocus(this);
		focusIsWith = this;
		keyFocusIsWith = null;
		this.bringToFront();
	}

	/**
	 * For most components there is nothing to do when they loose focus.
	 * Override this method in classes that need to do something when
	 * they loose focus eg TextField
	 */
	protected void loseFocus(GComponent grabber){
		if(cursorIsOver == this)
			cursorIsOver = null;
		focusIsWith = grabber;
	}

	/**
	 * Determines whether this component is to have focus or not
	 * @param focus
	 */
	public void setFocus(boolean focus){
		if(focus)
			takeFocus();
		else
			loseFocus(null);
	}

	/**
	 * Does this component have focus
	 * @return true if this component has focus else false
	 */
	public boolean hasFocus(){
		return (this == focusIsWith);
	}

	protected boolean hasParentFocus(){
		if(this == focusIsWith)
			return true;
		else if(parent != null)
			parent.hasParentFocus();
		return false;
	}

	protected boolean hasChildFocus(){
		if(this == focusIsWith)
			return true;
		else if(children != null){
			boolean hf = false;
			for(GComponent comp : children){
				hf |= comp.hasChildFocus();
			}
			return hf;
		}
		return false;
	}

	/**
	 * Does this component have key focus
	 * @return true if this component has key focus else false
	 */
	public boolean hasKeyFocus(){
		return (this == keyFocusIsWith);
	}

	/**
	 * Get a the object (if any) that currently has focus
	 * @return a reference to the object with focus (maybe null!)
	 */
	public static GComponent getFocusObject(){
		return focusIsWith;
	}


	protected static int focusObjectZ(){
		return (focusIsWith == null) ? -1 : focusIsWith.z;
	}


	/**
	 * See if this component is a child of another the component
	 * @param p possible parent
	 * @return true if p is a parent of this component
	 */
	protected boolean isChildOf(GComponent p){
		if(this == p)
			return true;
		else
			return (parent == null) ? false : parent.isChildOf(p);
	}

	/**
	 * This can be used to detect the type of event
	 * @return the eventType
	 */
	public int getEventType() {
		return eventType;
	}

	/**
	 * Used by some components on the MOUSE_RELEASED event 
	 * @param x
	 * @param y
	 * @return
	 */
	protected boolean mouseHasMoved(int x, int y){
		return (mdx != x || mdy != y);
	}

	/**
	 * Add a GUI component to this GComponent at the position specified by
	 * component being added. If transparency has been applied to this 
	 * GComponent then the same level will be applied to the component
	 * to be added.
	 * Unregister the component for drawing this is managed by the 
	 * GComponent draw method to preserve z-ordering
	 * 
	 * @return always true
	 */
	public boolean add(GComponent child){
		if(child == null || children.contains(child)){
			if(G4P.messages)
				System.out.println("Either the child component doesn't exist or has already been added to this component");
			return false;
		} else {
			child.parent = this;
			children.add(child);
			child.setZ(z);
			// The parent will take over drawing the child
			winApp.unregisterDraw(child);
			child.regDraw = false;
			if(localColor.getAlpha() < 255)
				child.setAlpha(localColor.getAlpha());
			Collections.sort(children, new Z_Order());
			return true;
		}
	}

	/**
	 * Recursive function to set the priority of a component. This
	 * is used to determine who gets focus when components overlap
	 * on the screen e.g. when a combobo expands it might cover a button. <br>
	 * It is used where components have childen e.g. GCombo and
	 * GPaneln
	 * It is used when a child component is added.
	 * @param component
	 * @param parentZ
	 */
	protected void setZ(int parentZ){
		z += parentZ;
		if(children != null){
			for(GComponent c : children){
				c.setZ(parentZ);
			}
		}
	}

	/**
	 * Remove a GUI component from this component
	 * 
	 * @param component
	 */
	public void remove(GComponent component){
		children.remove(component);
		component.unsetZ(z);
	}

	/**
	 * Recursive function to set the priority of a component. This
	 * is used to determine who gets focus when components overlap
	 * on the screen e.g. when a combobo expands it might cover a button. <br>
	 * It is used where components have childen e.g. GCombo and
	 * GPaneln <br>
	 * It is used when a child component is removed.
	 * @param component
	 * @param parentZ
	 */
	protected void unsetZ(int parentZ){
		z -= parentZ;
		if(children != null){
			for(GComponent c : children){
				c.unsetZ(parentZ);
			}
		}
	}

	/**
	 * Override in child classes
	 */
	public void pre(){ }

	/**
	 * Override in child classes
	 */
	public void draw(){ }

	/**
	 * Override in child classes.
	 * Every object will execute this method when an event
	 * is to be processed.
	 */
	public void mouseEvent(MouseEvent event){ }

	/**
	 * Override in child classes
	 * @param event
	 */
	public void keyPressed(KeyEvent event){ }

	/**
	 * This method is used to register this object with PApplet so it can process
	 * events appropriate for that class.
	 * It should be called from all child class ctors.
	 * 
	 * @param draw
	 * @param mouse
	 * @param pre
	 * @param key
	 */
	protected void registerAutos_DMPK(boolean draw, boolean mouse, boolean pre, boolean key){
		// if auto draw has been disabled then do not register for draw()
		if(draw && G4P.isAutoDrawOn(winApp)){
			winApp.registerDraw(this);
			regDraw = true;
		}
		if(mouse){
			winApp.registerMouseEvent(this);
			regMouse = true;
		}
		if(pre){
			winApp.registerPre(this);
			regPre = true;
		}
		if(key){
			winApp.registerKeyEvent(this);
			regKey = true;
		}
	}

	/**
	 * Completely dispose of this component. This operation cannot be undone.
	 */
	public void dispose(){
		if(regDraw) winApp.unregisterDraw(this);
		if(regMouse) winApp.unregisterMouseEvent(this);
		if(regPre) winApp.unregisterPre(this);
		if(regKey) winApp.unregisterKeyEvent(this);
		G4P.dumpComponent(this);
	}

	/**
	 * Called when we add a component to another window. Transfers autos
	 * to new window for this component and all it's children.
	 * 
	 * @param newWindowApp
	 */
	public void changeWindow(PApplet newWindowApp){
		if(regDraw){
			winApp.unregisterDraw(this);
			newWindowApp.registerDraw(this);
		}
		if(regPre){
			winApp.unregisterPre(this);
			newWindowApp.registerPre(this);
		}
		if(regMouse){
			winApp.unregisterMouseEvent(this);
			newWindowApp.registerMouseEvent(this);
		}
		if(regKey){
			winApp.unregisterKeyEvent(this);
			newWindowApp.registerKeyEvent(this);
		}
		winApp = newWindowApp;

		if(children != null && !children.isEmpty()){
			Iterator<GComponent> iter = children.iterator();
			while(iter.hasNext())
				iter.next().changeWindow(newWindowApp);
		}
	}


	/**
	 * Determines whether the position ax, ay is over this component.
	 * This is the default implementation and assumes the component
	 * is a rectangle where x & y is the top-left corner and the size
	 * is defined by width and height.
	 * Override this method where necessary in child classes e.g. GPanel 
	 * 
	 * @param ax mouse x position
	 * @param ay mouse y position
	 * @return true if mouse is over the component else false
	 */
	public boolean isOver(int ax, int ay){
		Point p = new Point(0,0);
		calcAbsPosition(p);
		if(ax >= p.x && ax <= p.x + width && ay >= p.y && ay <= p.y + height)
			return true;
		else 
			return false;
	}

	/** 
	 * This method will calculate the absolute top left position of this 
	 * component taking into account any ancestors. 
	 * 
	 * @param d
	 */
	public void calcAbsPosition(Point d){
		if(parent != null)
			parent.calcAbsPosition(d);
		d.x += x;
		d.y += y;
	}

	/**
	 * @return the parent
	 */
	public GComponent getParent() {
		return parent;
	}

	/**
	 * Sets the local color scheme
	 * @param schemeNo
	 */
	public void setColorScheme(int schemeNo){
		localColor = GCScheme.getColor(winApp, schemeNo);
	}

	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * Get the StyledString used for display
	 * @return
	 */
	public StyledString getStyledText() {
		return stext;
	}

	/**
	 * Set the text to be displayed.
	 * NEW version for FPanel etc.
	 * @param text
	 */
	public void setTextNew(String text){
		if(text == null || text.length() == 0 )
			text = "";
		stext = new StyledString(text);
	}
	
	public void setTextNew(String text, int wrapWidth){
		if(text == null || text.length() == 0 )
			text = "";
		stext = new StyledString(text);
	}
	
	/**
	 * @param text use this function to set the text so that the
	 * text length is calculated
	 */
	public void setText(String text) {
		this.text = text;
		winApp.textFont(localFont, localFont.getSize());
		textWidth = (int) winApp.textWidth(text); 
		calcAlignX();
		calcAlignY();
	}

	/**
	 * @param text the text to set with alignment
	 */
	public void setText(String text, int align) {
		this.text = text;
		winApp.textFont(localFont, localFont.getSize());
		textWidth = (int) winApp.textWidth(text);
		setTextAlign(align);
	}

	/**
	 * Set the text alignment inside the box. <br>
	 * Horizontal must be one of the following
	 * GAlign.LEFT or GAlign.CENTER or GAlign.RIGHT <br>
	 * Vertical must be one of the following
	 * GAlign.TOP or GAlign.MIDDLE or GAlign.BOTTOM <br>
	 * Both horizontal and vertical alignment can be set in one call using
	 * bitwise OR e.g. GAlign.BOTTOM | GAlign.CENTER 
	 * @param align the alignment flag
	 */
	public void setTextAlign(int align){
		int ha = align & GAlign.H_ALIGN;
		int va = align & GAlign.V_ALIGN;
		if(ha == GAlign.LEFT || ha == GAlign.CENTER || ha == GAlign.RIGHT){
			textAlignHorz = ha;
			calcAlignX();
		}
		if(va == GAlign.TOP || va == GAlign.MIDDLE || va == GAlign.BOTTOM){
			textAlignVert = va;
			calcAlignY();
		}
	}

	/**
	 * Override in child classes
	 * @param fontname
	 * @param fontsize
	 */
	public void setFont(String fontname, int fontsize){
	}

	/**
	 * Set the global font to be used by all controls. <br>
	 * You can override the font used by individual controls with
	 * the equivalent setFont method for the control. <br>
	 * 
	 * @param font the java.awt.Font to use
	 */
	public void setFontNew(Font font){
		fLocalFont = font;
	}
	
	/**
	 * Set the global font to be used by all controls. <br>
	 * You can override the font used by individual controls with
	 * the equivalent setFont method for the control. <br>
	 * 
	 * @param pfont the Processing font to use
	 */
	public void setFontNew(PFont pfont){
//		Font font = pfont.getNative();
//		if(font == null){
//			
//		}
//		
		fLocalFont = (Font) pfont.getNative();
	}
	
	
	/**
	 * Calculate text X & Y position based on text alignment
	 */
	protected void calcAlignX(){
		switch(textAlignHorz){
		case GAlign.LEFT:
			alignX = border + PADH;
			break;
		case GAlign.RIGHT:
			alignX = (int) (width - textWidth - border - PADH);
			break;
		case GAlign.CENTER:
			alignX = (int) ((width - textWidth)/2);
			break;
		}
	}

	protected void calcAlignY(){
		switch(textAlignVert){
		case GAlign.TOP:
			alignY = border + PADV;
			break;
		case GAlign.BOTTOM:
			alignY = (int) (height - localFont.getSize() - border - PADV);
			break;
		case GAlign.MIDDLE:
			alignY = (int) ((height - localFont.getSize() - border - PADV)/2);
			break;
		}
	}

	/**
	 * Sets the position of a component
	 * @param x
	 * @param y
	 */
	public void setXY(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Sets the x position of a component
	 * @param x
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * Sets the x position of a component
	 * @param y
	 */
	public void setY(int y) {
		this.y = y;
	}

	/**
	 * @return the x
	 */
	public float getX() {
		return x;
	}

	/**
	 * @return the y
	 */
	public float getY() {
		return y;
	}

	/**
	 * @return the width
	 */
	public float getWidth() {
		return width;
	}

	/**
	 * @param width the width to set
	 */
	public void setWidth(float width) {
		this.width = width;
	}

	/**
	 * @return the height
	 */
	public float getHeight() {
		return height;
	}

	/**
	 * @param height the height to set
	 */
	public void setHeight(int height) {
		this.height = height;
	}

	/**
	 * @return the component's visibility
	 */
	public boolean isVisible() {
		return visible;
	}

	/**
	 * Enable or disable the ability of the component to generate mouse events.<br>
	 * GTextField - it also controls key press events <br>
	 * GPanel - controls whether the panel can be moved/collapsed/expanded <br>
	 * @param enable true to enable else false
	 */
	public void setEnabled(boolean enable){
		enabled = enable;
		if(children != null){
			for(GComponent c : children)
				c.setEnabled(enable);
		}
	}

	/**
	 * Is this component enabled
	 * @return true if the component is enabled
	 */
	public boolean isEnabled(){
		return enabled;
	}
	/**
	 * @param visible the visibility to set
	 */
	public void setVisible(boolean visible) {
		// If we are making it invisible and it has focus give up the focus
		if(!visible && focusIsWith == this)
			loseFocus(null);
		this.visible = visible;
	}

	/**
	 * The user can add a border by specifying it's thickness
	 * a value of 0 means no border (this is the default)
	 * @param border width in pixels
	 */
	public void setBorder(int border){
		this.border = border;
		calcAlignX();
	}

	/**
	 * Get the border width
	 * @return the border width
	 */
	public int getBorder(){
		return border;
	}

	/**
	 * Determines wheher to show tha back color or not.
	 * Only applies to some components
	 * @param opaque
	 */
	public void setOpaque(boolean opaque){
		this.opaque = opaque;
	}

	/**
	 * Find out if the component is opaque
	 * @return true if the background is visible
	 */
	public boolean getOpaque(){
		return opaque;
	}

	/**
	 * Controls the transparency of this component
	 * 0 = fully transparent
	 * 255 = fully opaque
	 * 
	 * @param alpha
	 */
	public void setAlpha(int alpha){
		localColor.setAlpha(alpha);
	}

	/**
	 * How transparent / opaque is this component
	 * @return 0 (transparent) 255 (opaque)
	 */
	public int getAlpha(){
		return localColor.getAlpha();
	}

	public String toString(){
		return tag + "   ("+z+")";
	}

	public int compareTo(Object o) {
		return new Integer(this.hashCode()).compareTo(new Integer(o.hashCode()));
	}

	/**
	 * Comparator used for controlling the order components are drawn
	 * @author Peter Lager
	 */
	public static class Z_Order implements Comparator<GComponent> {

		public int compare(GComponent c1, GComponent c2) {
			if(c1.z != c2.z)
				return  new Integer(c1.z).compareTo( new Integer(c2.z));
			else
				return new Integer((int) -c1.y).compareTo(new Integer((int) -c2.y));
		}

	} // end of comparator class

} // end of class

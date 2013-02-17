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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.reflect.Method;

import processing.core.PApplet;
import processing.core.PImage;

/**
 * Objects of this class are separate windows which can be used to hold
 * G4P GUI components or used for drawing or both combined.
 * <br><br>
 * A number of examples are included in the library and can be found
 * at www.lagers.org.uk
 * 
 * 
 * @author Peter Lager
 *
 */
@SuppressWarnings("serial")
public class GWindow extends Frame implements GConstants {

	protected PApplet app;
	
	/**
	 * Gives direct access to the PApplet object inside the frame
	 * 
	 */
	public GWinApplet papplet;

	protected String winName;

	public GWinData data;
	
	protected WindowAdapter winAdapt = null;
	
	protected int actionOnClose = KEEP_OPEN;
	
	
	/**
	 * Remember what we have registered for.
	 */
	protected boolean regDraw = false;
	protected boolean regMouse = false;
	protected boolean regPre = false;
	protected boolean regKey = false;
	protected boolean regPost = false;

	/** The object to handle the pre event */
	protected Object preHandlerObject = null;
	/** The method in preHandlerObject to execute */
	protected Method preHandlerMethod = null;
	/** the name of the method to handle the event */ 
	protected String preHandlerMethodName;

	/** The object to handle the draw event */
	protected Object drawHandlerObject = null;
	/** The method in drawHandlerObject to execute */
	protected Method drawHandlerMethod = null;
	/** the name of the method to handle the event */ 
	protected String drawHandlerMethodName;

	/** The object to handle the mouse event */
	public Object mouseHandlerObject = null;
	/** The method in mouseHandlerObject to execute */
	public Method mouseHandlerMethod = null;
	/** the name of the method to handle the event */ 
	protected String mouseHandlerMethodName;

	/** The object to handle the post event */
	protected Object postHandlerObject = null;
	/** The method in postHandlerObject to execute */
	protected Method postHandlerMethod = null;
	/** the name of the method to handle the event */ 
	protected String postHandlerMethodName;

	/**
	 * Create a window that can be used to hold G4P components or used
	 * for drawing or both together.
	 * 
	 * @param theApplet
	 * @param name
	 * @param x initial position on the screen
	 * @param y initial position on the screen
	 * @param w width of the drawing area (the frame will be bigger to accommodate border)
	 * @param h height of the drawing area (the frame will be bigger to accommodate border and title bar)
	 * @param noFrame if true then the frame has no border
	 * @param mode JAVA2D / P2D / P3D / OPENGL
	 */
	public GWindow(PApplet theApplet, String name, int x, int y, int w, int h, boolean noFrame, String mode) {
		super(name);
		app = theApplet;
		winName = name;

		if(mode == null || mode.equals(""))
			mode = PApplet.JAVA2D;
		
		papplet = new GWinApplet(mode);
		papplet.owner = this;
		papplet.frame = this;
		papplet.frame.setResizable(true);

		papplet.appWidth = w;
		papplet.appHeight = h;

		windowCtorCore(theApplet, x, y, w, h, noFrame, mode);
		super.setResizable(true);
	}

	/**
	 * 
	 * @param theApplet
	 * @param name
	 * @param x initial position on the screen
	 * @param y initial position on the screen
	 * @param image background image (used to size window)
	 * @param noFrame if true then the frame has no border
	 * @param mode JAVA2D / P2D / P3D / OPENGL
	 */
	public GWindow(PApplet theApplet, String name, int x, int y, PImage image, boolean noFrame, String mode) {
		super(name);
		app = theApplet;
		winName = name;
				
		if(mode == null || mode.equals(""))
			mode = PApplet.JAVA2D;
		
		papplet = new GWinApplet(mode);
		papplet.owner = this;
		papplet.frame = this;
		papplet.frame.setResizable(true);
		

	    /// Get image details to set size
		papplet.bkImage = image;
		papplet.appWidth = image.width;
		papplet.appHeight = image.height;

		windowCtorCore(theApplet, x, y, image.width, image.height, noFrame, mode);
		
		super.setResizable(false);
	}

	/**
	 * Core stuff for GWindows ctor
	 * 
	 * @param x
	 * @param y
	 * @param w
	 * @param h
	 * @param noFrame
	 * @param mode
	 */
	private void windowCtorCore(PApplet theApplet, int x, int y, int w, int h, boolean noFrame, String mode){
		papplet.bkColor = papplet.color(180);
		
		papplet.resize(papplet.appWidth, papplet.appHeight);
		papplet.setPreferredSize(new Dimension(papplet.appWidth, papplet.appHeight));
		papplet.setMinimumSize(new Dimension(papplet.appWidth, papplet.appHeight));

		// add the PApplet to the Frame
		setLayout(new BorderLayout());
		add(papplet, BorderLayout.CENTER);

		// ensures that the animation thread is started and
		// that other internal variables are properly set.
		papplet.init();

		// Set the sketch path to the same as the main PApplet object
	    papplet.sketchPath = theApplet.sketchPath;

		// Pack the window, position it and make visible
		setUndecorated(noFrame);
		pack();
		setLocation(x,y);
		setVisible(true);
		
		// At least get a blank screen
		papplet.registerDraw(papplet);
		regDraw = true;
		
		// Make the window always on top
		setOnTop(true);
		
		// Make sure we have some data even if not used
		data = new GWinData();
		data.owner = this;
		
		// Make sure G4P knows about this window
		G4P.addWindow(this);
	}
	
	
	/**
	 * Add a G4P component onto the window.
	 * 
	 * @param component
	 */
	public void add(GComponent component){
		component.changeWindow(papplet);
	}

	/**
	 * Add an object that holds the data this window needs to use.
	 * 
	 * Note: the object can be of any class that extends GWinData.
	 * 
	 * @param data
	 */
	public void addData(GWinData data){
		this.data = data;
		this.data.owner = this;
	}
	
	/**
	 * Always make this window appear on top of other windows (or not). <br>
	 * This will not work when run from a remote server (ie Applet over the web)
	 * for security reasons. In this situation a call to this method is ignored
	 * and a warning is generated. 
	 * 
	 * @param onTop
	 */
	public void setOnTop(boolean onTop){
		try{
			setAlwaysOnTop(onTop);
		} catch (Exception e){
			if(G4P.messages)
				System.out.println("Warning: setOnTop() method will not work when the sketch is run from a remote location.");
		}
	}
	
	/**
	 * Sets the location of the window. <br>
	 * (Already available from the Frame class - helps visibility 
	 * of method in G4P reference)
	 */
	public void setLocation(int x, int y){
		super.setLocation(x,y);
	}
	
	/**
	 * Sets the visibility of the window <br>
	 * (Already available from the Frame class - helps visibility 
	 * of method in G4P reference)
	 */
	public void setVisible(boolean visible){
		super.setVisible(visible);
	}
	
	/**
	 * Determines whether the window is resizabale or not. <br>
	 * This cannot be set to true if a background image is used.
	 */
	public void setResizable(boolean resizable){
		if(resizable == false)
			super.setResizable(false);
		else {
			if(papplet.bkImage == null)
				super.setResizable(true);
		}
	}
	
	/**
	 * Set the background image to be used instead of a plain color background <br>
	 * The window will resize to accommodate the image.
	 * @param image
	 */
	public void setBackground(PImage image){
		papplet.noLoop();
		papplet.bkImage = null;
		super.setResizable(true);
		papplet.resize(image.width, image.height);
		papplet.bkImage = image;
		papplet.appWidth = image.width;
		papplet.appHeight = image.height;
		papplet.setPreferredSize(new Dimension(papplet.appWidth, papplet.appHeight));
		papplet.setMinimumSize(new Dimension(papplet.appWidth, papplet.appHeight));
		pack();
		super.setResizable(false);
		papplet.loop();
	}
	
	/**
	 * Set the background color for the window.
	 * 
	 * @param col
	 */
	public void setBackground(int col){
		papplet.bkColor = col;
	}

	/**
	 * By default the background() method is called to set the background image/colour
	 * every frame. You can switch this off by calling this method with a parametr 
	 * value = false.
	 * @param auto_clear whether to call the background method() or not
	 */
	public void setAutoClear(boolean auto_clear){
		papplet.autoClear = auto_clear;
	}
	
	/**
	 * This sets what happens when the users attempts to close the window. <br>
	 * There are 3 possible actions depending on the value passed. <br>
	 * GWindow.KEEP_OPEN - ignore attempt to close window (default action)
	 * GWindow.CLOSE_WINDOW - close this window, if it is the main window it causes the app to exit <br>
	 * GWindow.EXIT_APP - exit the app, this will cause all windows to close. <br>
	 * @param action
	 */
	public void setActionOnClose(int action){
		switch(action){
		case KEEP_OPEN:
			removeWindowListener(winAdapt);
			winAdapt = null;
			actionOnClose = action;
			break;
		case CLOSE_WINDOW:
		case EXIT_APP:
			if(winAdapt == null){
				winAdapt = new WindowAdapter() {
					public void windowClosing(WindowEvent evt) {
						switch(actionOnClose){
						case CLOSE_WINDOW:
							removeFromG4P();
							dispose();	// close this frame
							break;
						case EXIT_APP:
							System.exit(0);
							break;
							}
					}
				};
				addWindowListener(winAdapt);
			} // end if
			actionOnClose = action;
			break;
		} // end switch
	}
	
	/**
	 * Get the action to be performed when the user attempts to close
	 * the window.
	 * @return actionOnClose
	 */
	public int getActionOnClose(){
		return actionOnClose;
	}
	
	/**
	 * Allows the user to close this window and release its resources.
	 */
	public void close(){
		removeFromG4P();
		dispose();	// close this frame
	}
	
	/**
	 * Used to remove from G4P when the Frame is disposed.
	 */
	private void removeFromG4P(){
		papplet.noLoop();
		if(regPost)
			papplet.unregisterPost(papplet);
		if(regDraw)
			papplet.unregisterDraw(papplet);
		if(regPre)
			papplet.unregisterPre(papplet);
		if(regMouse)
			papplet.unregisterMouseEvent(papplet);
		regPost = regDraw = regPre = regMouse = false;
		G4P.removeWindow(this);
	}

	/**
	 * Attempt to add the 'draw' handler method. 
	 * The default event handler is a method that returns void and has two
	 * parameters Papplet and GWinData
	 * 
	 * @param obj the object to handle the event
	 * @param methodName the method to execute in the object handler class
	 */
	public void addDrawHandler(Object obj, String methodName){
		try{
			drawHandlerMethod = obj.getClass().getMethod(methodName, new Class[] {GWinApplet.class, GWinData.class } );
			drawHandlerObject = obj;
			drawHandlerMethodName = methodName;
		} catch (Exception e) {
			GMessenger.message(NONEXISTANT, this, new Object[] {methodName, new Class[] { GWinApplet.class, GWinData.class } } );
		}
	}

	/**
	 * Attempt to add the 'pre' handler method. 
	 * The default event handler is a method that returns void and has two
	 * parameters Papplet and GWinData
	 * 
	 * @param obj the object to handle the event
	 * @param methodName the method to execute in the object handler class
	 */
	public void addPreHandler(Object obj, String methodName){
		try{
			preHandlerMethod = obj.getClass().getMethod(methodName, new Class[] {GWinApplet.class, GWinData.class } );
			preHandlerObject = obj;
			preHandlerMethodName = methodName;
			papplet.registerPre(papplet);
			regPre = true;
		} catch (Exception e) {
			GMessenger.message(NONEXISTANT, this, new Object[] {methodName, new Class[] { GWinApplet.class, GWinData.class } } );
		}
	}

	/**
	 * Attempt to add the 'mouse' handler method. 
	 * The default event handler is a method that returns void and has two
	 * parameters Papplet and GWinData
	 * 
	 * @param obj the object to handle the event
	 * @param methodName the method to execute in the object handler class
	 */
	public void addMouseHandler(Object obj, String methodName){
		try{
			mouseHandlerMethod = obj.getClass().getMethod(methodName, 
					new Class[] {GWinApplet.class, GWinData.class, MouseEvent.class } );
			mouseHandlerObject = obj;
			mouseHandlerMethodName = methodName;
			papplet.registerMouseEvent(papplet);
			regMouse = true;
		} catch (Exception e) {
			GMessenger.message(NONEXISTANT, this, new Object[] {methodName, new Class[] { GWinApplet.class, GWinData.class, MouseEvent.class } } );
		}
	}

	/**
	 * Attempt to add the 'post' handler method. 
	 * The default event handler is a method that returns void and has two
	 * parameters Papplet and GWinData
	 * 
	 * @param obj the object to handle the event
	 * @param methodName the method to execute in the object handler class
	 */
	public void addPostHandler(Object obj, String methodName){
		try{
			postHandlerMethod = obj.getClass().getMethod(methodName, 
					new Class[] {GWinApplet.class, GWinData.class } );
			postHandlerObject = obj;
			postHandlerMethodName = methodName;
			regPost = true;
		} catch (Exception e) {
			GMessenger.message(NONEXISTANT, this, new Object[] {methodName, new Class[] { GWinApplet.class, GWinData.class } } );
		}
	}

}

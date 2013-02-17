/*
 *  Copyright notice
 *  
 *  This file was created as part of the Processing library `gwoptics' 
 *  http://www.gwoptics.org/processing/gwoptics_p5lib/
 *  
 *  Copyright (C) 2009 onwards Daniel Brown and Andreas Freise
 *  
 *  
 *  It has been re-factored by Peter Lager to integrate it into the GUI 
 *  for Processing (G4P) library 
 * 	http://www.lagers.org.uk/g4p/index.html
 *	http://gui4processing.googlecode.com/svn/trunk/
 *
 *  Copyright (C) 2010 onwards Peter Lager
 *	
 *  This library is free software; you can redistribute it and/or modify it under 
 *  the terms of the GNU Lesser General Public License version 2.1 as published 
 *  by the Free Software Foundation.
 *  
 *  This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
 *  without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 *  See the GNU Lesser General Public License for more details.
 *  
 *  You should have received a copy of the GNU Lesser General Public License along with 
 *  this library; if not, write to the Free Software Foundation, Inc., 59 Temple Place, 
 *  Suite 330, Boston, MA 02111-1307 USA 
 */

package guicomponents;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PImage;

/**
 * <p>
 * GWSlider is a wrapper class that extends the functionality of the gui4processing(g4p) slider. It adds a lot of
 * functionality that is found in many other main stream sliders in other frameworks.
 * </p>
 * 
 * <p>
 * The main change to the g4p slider was to introduce a more flexible and graphical interface to render the slider.
 * The method used to render the slider is combining several images that relate to various segments of the slider
 * in the form of a skin. The slider is broken down into 4 segments, each having a specific image file that relates
 * to them:
 * </p>
 * 
 * <ul>
 * <li>Left end cap of the slider(end_left.png)</li>
 * <li>Right end cap of the slider(end_right.png)</li>
 * <li>An extendible centre segment(centre.png)</li>
 * <li>Draggable thumb (handle.png and handle_mouseover.png)</li>
 * </ul>
 * 
 * <p>
 * The five images stated above define the skin that is applied to slider. A default skin is included in the library
 * and applied when no other alternative is provided. To generate a skin all these images must be included into a 
 * folder in the sketches data directory, where the name of the folder is the name of the skin. When creating a new
 * slider, there is a constructor available that allows you to specify the skin to use. Eg, if you have a folder name
 * 'ShinyRedSkin' in your data directory that has the above images in, then pass a string with "ShinyRedSkin" to the
 * constructor.
 * </p>
 * 
 * <p>
 * The images need to related. The end_left, end_right and centre png's must all be the same height. The height can be
 * whatever is required, though values round 20 is recommended. The end segments can both be different lengths and the
 * length of the centre images must be 1 pixel. The centre image is stretched depending on the length of the slider to 
 * fill in the middle section. The thumb/handle can be any height and width but should be an odd number of pixels. An
 * odd number allows a perfect centre to be found as fractional pixels are not possible. Alpha channel use is recommended
 * to generate interesting skins.
 * </p>
 * 
 * <p>
 * Also added to the slider is tick marks. The number of ticks shown is customisable and there is the option to stick the 
 * thumb to each tick. This only allows the thumb to take on certain values and positions. The ticks by default have only
 * 2 labels, one showing the minimum and one the maximum. The other options are to have no tick labels or to specify a 
 * string array containing the labels.
 * </p>
 * 
 * <p>
 * One of the more trickier features of the slider to understand is setting the type of the slider. By default the slider
 * is set up to display and use integer values. On occasion it is useful to be able to specify a floating value range.
 * When the slider is set to ValueType.Integer, any limits or values passed to the slider will be rounded to the nearest
 * integer. If you later then switch to ValueType.Decimal or Exponential the limits and value will still be rounded.
 * This can cause issues as the initial setting is ValueType.Integer, if you first set your floating limits and value then 
 * specify the type of the slider to be decimal, the decimal parts of the inputs will be lost. <b>Always specify the 
 * ValueType of the slider as soon as it is created</b>, this will save many headaches. 
 * </p>
 * 
 * <p>
 * Another confusing feature to the slider is the methods to retrieve the value of the slider. Initially the g4p slider
 * only allowed an integer value. To get around this a new value member is defined as a float, where all the relevant
 * methods are overridden to use this new value but to also update the old integer value. Unfortunately the getValue method
 * is already predefined to return and integer, so a new getValuef() method is added to allow you to access the floating 
 * member. Either can be used but remember getValue() will always return a rounded value.
 * </p>
 * 
 * <p>
 * The slider can also be controlled via the arrow keys on the keyboard for a finer adjustment. The slider must first
 * have been given focus with the mouse before hand, before it will work. 
 * </p>
 * 
 * <p>
 * <b>NOTE: Handle/Thumb image should be an odd number of pixels wide so that it can correctly centre on tick marks.</b>
 * </p>
 * 
 * <p>
 * History<br /> 
 * Version 0.3.3 overhaul to how ticks are calculated and rendered, firstly calculating the difference in value
 * between tick and then relating that into a distance in pixels, rather than dividing length of slider by tick
 * number.
 * </p>
 * 
 * @author Daniel Brown 21/6/09
 *
 */
public class GWSlider extends GSlider {

	// Unit of measurement e.g. amp, metre etc.
	public String unit;

	protected PImage _leftEnd;
	protected PImage _thumb;
	protected PImage _thumb_mouseover;
	protected PImage _rightEnd;
	protected PImage _centre;
	protected String _skin;
	protected int _numTicks;
	protected int _tickLength;
	protected int _tickOffset;	
	protected int _precision;
	protected boolean _renderMaxMinLabel;
	protected boolean _renderValueLabel;
	protected boolean _stickToTicks;
	protected String[] _tickLabels;
	protected boolean _isMouseOverThumb;
	protected boolean _mousePressedOverThumb;
	protected int[] _tickPositions;
	protected float[] _tickValues;
	protected int _tickColour;	
	protected int _fontColour;	

	// Mod made by Daniel 12/3/10
	protected int _currTickStuck; //Index of current index stuck to 

	//setters
	/**
	 * Sets the number of decimal places to print in the value labels
	 */
	public void setPrecision(int acc){
		_precision = PApplet.constrain(acc, 0, 6);
	}

	/**
	 * Sets the target value of the slider, if setInertia(x) has been used
	 * to implement inertia then the actual slider value will gradually
	 * change until it reaches the target value. The slider thumb is 
	 * always in the right position for the current slider value. <br>
	 * <b>Note</b> that events will continue to be generated so if this
	 * causes unexpected behaviour then use setValue(newValue, true)
	 * 
	 * @param newValue the value we wish the slider to become
	 */
	public void setValue(int newValue){
		value = PApplet.constrain(newValue, minValue, maxValue);
		thumbTargetPos = (int) PApplet.map(value, minValue, maxValue, thumbMin, thumbMax);
		if(_stickToTicks)
			_stickToTickByValue(value);
	}

	/**
	 * Sets the target value of the slider, if setInertia(x) has been 
	 * to implement inertia then the actual slider value will gradually
	 * change until it reaches the target value. The slider thumb is 
	 * always in the right position for the current slider value. <br>
	 * <b>Note</b> that events will continue to be generated so if this
	 * causes unexpected behaviour then use setValue(newValue, true)
	 * 
	 * @param newValue the value we wish the slider to become
	 */
	public void setValue(float newValue){
		value = PApplet.constrain(newValue, minValue, maxValue);
		thumbTargetPos = (int) PApplet.map(value, minValue, maxValue, thumbMin, thumbMax);
		if(_stickToTicks)
			_stickToTickByValue(value);
	}

	/**
	 * Sets the target value of the slider according to the tick number.
	 * If setInertia(x) has been to implement inertia then the actual 
	 * slider value will gradually change until it reaches the target
	 * value. The slider thumb is always in the right position for the 
	 * current slider value. <br>
	 * <b>Note</b> that events will continue to be generated so if this
	 * causes unexpected behaviour then use setValue(newValue, true)
	 * 
	 * @param tickNo >=0 and < number of ticks
	 */
	public void setValueToTickNumber(int tickNo){
		if(_stickToTicks){
			tickNo = PApplet.constrain(tickNo, 0, _numTicks);
			float newValue = PApplet.map((float)tickNo, 0.0f, (float)_numTicks, minValue, maxValue);
			_stickToTickByValue(newValue);
		}		
	}

	/**
	 * Sets the number of ticks shown on the slider. This will cancel any labels 
	 * previously set.
	 */
	public void setTickCount(int nbrTicks){
		_numTicks = nbrTicks;
		_tickLabels = null;
		_calcTickPositions();
		// ***********************************************************
		if(_stickToTicks)
			_stickToTickByValue(value);
	}

	/**
	 * Accepts an array of strings that then determines the number of ticks shown and the label 
	 * underneath each of them. This overrides any previous setting of the tick count and value
	 * label rendering.
	 * @param lbls
	 */
	public void setTickLabels(String[] lbls){
		_tickLabels = lbls;
		_numTicks = lbls.length - 1;
		_calcTickPositions();
		// **********************************************************************
		if(_stickToTicks)
			_stickToTickByValue(value);
	}

	/**
	 * Setting to true limits the thumb to only take values that each tick represents and no 
	 * value in between them
	 */
	public void setStickToTicks(boolean stick){
		_stickToTicks = stick;
		if(stick){
			_stickToTickByValue(value);
		}
		_calcTickPositions();
	}

	/**
	 * Adjusts the length of the ticks
	 * @param l
	 */
	public void setTickLength(int l){
		_tickLength = PApplet.constrain(l, 1, 10);
		_calcControlWidthHeight();
	}

	/**
	 * set to false to not render the min/max labels for a more minamalistic look.
	 */
	public void setRenderMaxMinLabel(boolean showMinMax){
		_renderMaxMinLabel = showMinMax;
		_calcControlWidthHeight();
	}

	/**
	 * set to false to not render the value label for a more minamalistic look.
	 */
	public void setRenderValueLabel(boolean showValue){
		_renderValueLabel = showValue;
	}

	/**
	 * Set the colour of the ticks
	 * 
	 * @param R red (0-255)
	 * @param G green (0-255)
	 * @param B blue (0-255)
	 */
	public void setTickColour(int R,int G, int B){
		setTickColour(winApp.color(R,G,B));
	}

	/**
	 * Set the colour of the ticks
	 * 
	 * @param c the colour value as calculated by the PApplet color() method.
	 */
	public void setTickColour(int c){
		_tickColour = c;
	}

	/**
	 * Set the colour of the font
	 * 
	 * @param R red (0-255)
	 * @param G green (0-255)
	 * @param B blue (0-255)
	 */
	public void setFontColour(int R, int G, int B){
		setFontColour(winApp.color(R,G,B));
	}

	/**
	 * Set the colour of the font
	 * 
	 * @param c the colour value as calculated by the PApplet color() method.
	 */
	public void setFontColour(int c){
		_fontColour = c;
	}

	/**
	 * basic constructor that applies the default library skin to the slider. Accepts  
	 * the x and y position of the slider, the PApplet theApplet where the slider is  
	 * rendered and the length of the slider.
	 * 
	 * @param theApplet
	 * @param x
	 * @param y
	 * @param length
	 */
	public GWSlider(PApplet theApplet, int x, int y, int length) {
		this(theApplet,"gwSlider",x,y,length);
	}

	/**
	 * Alternative constructor that applies a given skin to the slider. Accepts the x and y 
	 * position of the slider, the PApplet theApplet where the slider is rendered and the length 
	 * of the slider. Throws GUIException if the necessary skin images are not present.
	 * 
	 * @param theApplet
	 * @param x
	 * @param y
	 * @param length
	 */
	public GWSlider(PApplet theApplet, String skin, int x, int y, int length) {
		super(theApplet, x, y);
	//	super(theApplet, x, y, length, 1); //we reset the height later when we get the image heights

		this.width = length;
		this.height = 1;
		z = Z_SLIPPY;

//		if(length < 1){throw new RuntimeException("Length of slider must be greater than 0.");}

		//Here we set a bunch of default values for everything
		if(skin == null)
			_skin = "gwSlider";
		else
			_skin = skin;

		_numTicks = 5;
		_tickLength = 5;
		_tickOffset = 3;
		_renderMaxMinLabel = true;
		_renderValueLabel = true;
		_stickToTicks = false;
		_precision = 2;
		_valueType = INTEGER;
		_tickColour = winApp.color(0);
		_fontColour = winApp.color(0);

		unit = "";

		// Look for the skin images, if these don't exist the variable come out null
		// no exceptions are thrown
		_leftEnd = winApp.loadImage(_skin + "/end_left.png");
		_rightEnd = winApp.loadImage(_skin + "/end_right.png");
		_thumb = winApp.loadImage(_skin +"/handle.png");
		_thumb_mouseover = winApp.loadImage(_skin +"/handle_mouseover.png");
		//load the centre image up temporarily as we will generate the final stretched
		//image to use shortly
		PImage cTemp = winApp.loadImage(_skin + "/centre.png");

		String files = "";

		//generate a list of files that aren't there
		if(_leftEnd == null)
			files += "end_left.png\n";
		if(_rightEnd == null)
			files += "end_right.png\n";
		if(_thumb == null)
			files += "handle.png\n";
		if(_thumb_mouseover == null)
			files += "handle_mouseover.png\n";
		if(cTemp == null)
			files += "centre.png\n";

		// See if we have problems with the skin files
		if(files != ""){
			PApplet.println("The following files could not be found for the skin " + _skin + ": \n" + files
					+ "\nCheck that these files are correctly placed in the data directory under a folder with"
					+ " the same name as the skin used.\n");
		}
		if(cTemp.width != 1){
			PApplet.println("The supplied centre image for this skin is not of width 1px.");
		}
		if(cTemp.height != _leftEnd.height || cTemp.height != _rightEnd.height){
			PApplet.println("The image components of the slider are not all the same height.");
		}

		this.height = cTemp.height;
		int cWidth = length - _leftEnd.width - _rightEnd.width;
		if(cWidth < 0){cWidth = 1;}

		_centre = new PImage(cWidth,cTemp.height);

		//now copy over the data from cTemp to main centre image
		//the standard PImage stretch method is no good in this case 
		//appears better to do it manually.
		cTemp.loadPixels();
		_centre.loadPixels();

		for (int i = 0; i < _centre.height; i++) {
			for (int j = 0; j < _centre.width; j++) {
				_centre.pixels[i*_centre.width +j] = cTemp.pixels[i];
			}
		}
		_centre.updatePixels();
		cTemp.updatePixels();
		
		//the thumb only moves along the centre section
		thumbMin = _leftEnd.width;
		thumbMax = _leftEnd.width + _centre.width;
		this.setLimits(50.0f, 0.0f, 100.0f);

		localFont = globalFont;

		_calcControlWidthHeight();
		_calcTickPositions();
		z = Z_SLIPPY;
		
		//signs up to the g4p events system
		createEventHandler(G4P.mainWinApp, "handleSliderEvents", new Class[]{ GSlider.class });
	
		registerAutos_DMPK(true, true, true, false);
	}

	/**
	 * the width of the control and height, is determined by the length and also 
	 * the various images used in the skins. This function deals with that. It is
	 * important as the width and height values define the bounding box around the
	 * slider. This then helps determine if mouse clicks are relevant to this slider
	 * or not.
	 */
	protected void _calcControlWidthHeight(){
		//width is determined by image sizes
		width = _leftEnd.width + _centre.width + _rightEnd.width;
		height =  _centre.height + _tickLength + _tickOffset;

		if(_renderMaxMinLabel){height += localFont.getSize();}
	}

	/**
	 * The tick positions are stored in an array and referenced on each draw call.
	 * It is done this way to provide a more accurate way of lining up the ticks and
	 * the thumb when stick to ticks is enabled
	 */
	protected void _calcTickPositions(){
		Point p = new Point();
		calcAbsPosition(p);

		float sliderRange = maxValue - minValue;
		float dTick = sliderRange / _numTicks; //distance in terms of value

		_tickPositions = new int[_numTicks + 1];
		_tickValues = new float[_numTicks + 1];

		for(int i = 0; i <= _numTicks;i++){
			_tickPositions[i] = Math.round(PApplet.map(minValue + i * dTick, minValue, maxValue, thumbMin, thumbMax));
			_tickValues[i] = minValue + i * dTick;
		}
	}

	/**
	 * This method takes a value that is to represented on the slider and determines which
	 * tick it is closet to and sets the thumb at the relevant position
	 * @param v
	 */
	protected void _stickToTickByValue(float v){
		float sliderRange = maxValue - minValue;
		float dTick = sliderRange / _numTicks; //distance in terms of value

		int index = Math.round(PApplet.constrain(v - minValue, 0, sliderRange) / dTick);

		_currTickStuck = PApplet.constrain(index, 0,  _numTicks);
		thumbTargetPos = _tickPositions[_currTickStuck];

		value = minValue +_currTickStuck * dTick;
	}


	/**
	 * This method accepts an input value that states a screen position(usually from a 
	 * mouse click) and determines the nearest tick marks to stick the thumb to
	 * @param pos
	 */
	protected void _stickToTickByPosition(float pos){
		Point p = new Point();
		calcAbsPosition(p);

		float sliderRange = maxValue - minValue;
		float dTick = sliderRange / _numTicks; //distance in terms of value
		float v = PApplet.map(pos, thumbMin, thumbMax, minValue, maxValue);

		int index = Math.round((v - minValue) / dTick);

		_currTickStuck = PApplet.constrain(index, 0, _tickPositions.length-1);
		thumbTargetPos = _tickPositions[_currTickStuck];
	}


	/**
	 * handles all the mouse events that may effect the slider. depending on mouse position and action
	 * the focus is set to the slider and the relevant function and members set.
	 */
	@Override
	public void mouseEvent(MouseEvent event){
		if(!visible || !enabled) return;

		boolean isMouseOver = false;
		boolean isMouseOverThumb = false;
		// Is mouse over the slider?
		isMouseOver = this.isOver(event.getX(), event.getY());
		// If it is over slider then check to see if it is over thumb
		if(isMouseOver){
			isMouseOverThumb = isOverThumb(event.getX(), event.getY());
			if(isMouseOverThumb || focusIsWith == this) 
				cursorIsOver = this;
			else if(cursorIsOver == this)
				cursorIsOver = null;
		}
		else {
			cursorIsOver = (cursorIsOver == this) ? null : cursorIsOver;
		}

		Point p = new Point();
		calcAbsPosition(p);

		switch (event.getID()) {
		case MouseEvent.MOUSE_PRESSED:
			if(focusIsWith != this && isMouseOver && z > focusObjectZ()) { // && isMouseOverThumb){
				this.takeFocus();
				_mousePressedOverThumb = isMouseOverThumb;
			}
			break;
		case MouseEvent.MOUSE_RELEASED: // OK as long as we have focus
			if(focusIsWith == this){  // && isMouseOverThumb || (_mousePressedOverThumb == true)){
				if(_stickToTicks){
					_stickToTickByPosition(winApp.mouseX - p.x);
				}
				else {
					thumbTargetPos  = PApplet.constrain(winApp.mouseX - p.x, thumbMin, thumbMax);
				}
				loseFocus(null);
				eventType = RELEASED;
				fireEvent();
			}
			_isMouseOverThumb = false;
			_mousePressedOverThumb = false;
			break;
		case MouseEvent.MOUSE_DRAGGED:
			if(focusIsWith == this && _mousePressedOverThumb){
				thumbTargetPos  = PApplet.constrain(winApp.mouseX - p.x , thumbMin, thumbMax);
				isValueChanging = true;
			}
			break;
		case MouseEvent.MOUSE_MOVED:
			if(isOverThumb(event.getX(), event.getY())){
				_isMouseOverThumb = true;
			}
			else
				_isMouseOverThumb = false;
			break;
		} // end of switch
	}

	/**
	 * Registered key event of parent object, checks if arrow keys are being pressed and has focus
	 * if so the thumb is moved one pixel.
	 */
	public void keyEvent(KeyEvent e){
		if(e.getID() == KeyEvent.KEY_PRESSED && this.hasKeyFocus()){
			if(e.getKeyCode()== 37){ //left arrow
				if(_stickToTicks){			
					_currTickStuck = PApplet.constrain(_currTickStuck - 1, 0, _tickPositions.length-1);
					thumbTargetPos = _tickPositions[_currTickStuck];
				}else
					thumbTargetPos = PApplet.constrain(thumbTargetPos - 1,thumbMin,thumbMax);
			}else if(e.getKeyCode()== 39){ //right arrow
				if(_stickToTicks){
					_currTickStuck = PApplet.constrain(_currTickStuck + 1, 0, _tickPositions.length-1);
					thumbTargetPos = _tickPositions[_currTickStuck];
				}else
					thumbTargetPos = PApplet.constrain(thumbTargetPos + 1,thumbMin,thumbMax);			
			}
		}
	}


	/**
	 * returns whether the positions supplied is over the mouse or not
	 */
	@Override
	public boolean isOver(int ax, int ay){
		Point p = new Point(0,0);
		calcAbsPosition(p);
		float val = (float) (_centre.height * 0.5 - _thumb.height * 0.5); //takes into account if the thumbs height
		//is greater than the central bar image
		if(ax >= p.x && ax <= p.x + width && ay >= (p.y + val) && ay <= (p.y + height - val))
			return true;
		else 
			return false;
	}

	/**
	 * return whether input position is over the thumb, used to determine whether to show the
	 * handle_mouseover image
	 * @param ax
	 * @param ay
	 * @return true if the position ax,ay is over the thumb
	 */
	public boolean isOverThumb(int ax, int ay){		
		Point p = new Point(0,0);
		calcAbsPosition(p);
		Rectangle r = new Rectangle((int)(p.x + thumbPos  - 0.5*_thumb.width - 1),
				(int)(p.y + 0.5*_centre.height -  0.5*_thumb.height -1),
				(int)(_thumb.width + 1),
				(int)(_thumb.height + 1));

		if(r.contains(ax, ay))
			return true;
		else 
			return false;
	}

	/**
	 * Sets font of labels
	 */
	public void setFont(String fontname, int fontsize){
		localFont = GFont.getFont(winApp, fontname, fontsize);
	}

	@Override
	public void draw(){		
		if(!visible) return;
		String format = null;

		//depending on slider type we want to format the 
		//labels as necessary
		switch(_valueType){
		case INTEGER:
			format = "%d%s";
			break;
		case DECIMAL:
			format = "%." + _precision + "f%s";
			break;
		case EXPONENT:
			format = "%." + _precision + "E%s";
			break;
		}

		winApp.pushStyle();

		//calculates the absolute position, this is 
		//for when the slider is embedded in other panels and controls
		Point p = new Point(); 
		calcAbsPosition(p);
		
		//draw each of the slider skin images
		winApp.imageMode(CORNER);

		if(_leftEnd != null) 
			winApp.image(_leftEnd, p.x, p.y);
		if(_centre != null) 
			winApp.image(_centre, p.x + _leftEnd.width, p.y);
		if(_rightEnd != null) 
			winApp.image(_rightEnd, p.x + _leftEnd.width + _centre.width, p.y);

		winApp.textFont(localFont);
		winApp.textAlign(PConstants.CENTER);

		//calc a few tick variables for drawing
		float tickDist =  (_centre.width )/(float)_numTicks;
		winApp.stroke(_tickColour);
		winApp.strokeWeight(1);
		winApp.fill(_fontColour);
		//draw ticks
		float tickYPos = p.y + _centre.height + _tickOffset + _tickLength + localFont.getSize();

		for(int i = 0;i < _tickPositions.length;i++){
			if(_tickLabels != null){
				Point pos = new Point(_tickPositions[i],(int) tickYPos);
				winApp.text(_tickLabels[i],p.x + pos.x,pos.y);
			}else if(i == 0 && _renderMaxMinLabel){
				//Draw in the min value
				Point pos = new Point(p.x + _leftEnd.width + Math.round(i * tickDist),p.y + _centre.height + _tickOffset + _tickLength + localFont.getSize()) ;
				if(_valueType == INTEGER)
					winApp.text(String.format(format,Math.round(minValue),unit),pos.x,pos.y);
				else
					winApp.text(String.format(format,minValue,unit),pos.x,pos.y);

			}else if(i == _numTicks && _renderMaxMinLabel){	
				//Draw in the max value			
				Point pos = new Point(p.x + _leftEnd.width + Math.round(i * tickDist),p.y + _centre.height + _tickOffset + _tickLength + localFont.getSize()) ;
				if(_valueType == INTEGER)
					winApp.text(String.format(format,Math.round(maxValue),unit),pos.x,pos.y);
				else
					winApp.text(String.format(format,maxValue,unit),pos.x,pos.y);
			}

			winApp.beginShape(LINE);
			winApp.vertex(p.x + _tickPositions[i], p.y + _centre.height + _tickOffset);
			winApp.vertex(p.x + _tickPositions[i], p.y + _centre.height + _tickOffset + _tickLength);
			winApp.endShape();

			//draws a highlight line next to the black line, gives a more
			//3d feel to the control
			winApp.pushStyle();
			winApp.stroke(230);
			winApp.beginShape(LINE);
			winApp.vertex(p.x + _tickPositions[i] + 1, p.y + _centre.height + _tickOffset);
			winApp.vertex(p.x + _tickPositions[i] + 1, p.y + _centre.height + _tickOffset + _tickLength);
			winApp.endShape();
			winApp.popStyle();
			if(_thumb != null){
				if(!_isMouseOverThumb)
					winApp.image(_thumb, p.x + thumbPos - Math.round(_thumb.width*0.5) + 1, (float) (p.y + 0.5*_centre.height - 0.5*_thumb.height));
				else
					winApp.image(_thumb_mouseover, p.x + thumbPos - Math.round(_thumb_mouseover.width*0.5) + 1, (float) (p.y + 0.5*_centre.height - 0.5*_thumb_mouseover.height));
			}
			if(_renderValueLabel){
				if(_valueType == INTEGER)
					winApp.text(String.format(format,Math.round(value),unit),p.x + thumbPos, p.y - _thumb.height*0.5f + 0.5f*_centre.height - 4 );
				else 
					winApp.text(String.format(format,value,unit),p.x + thumbPos, p.y - _thumb.height*0.5f + 0.5f*_centre.height - 4 );
			}
		}			
		winApp.popStyle();
	}

}

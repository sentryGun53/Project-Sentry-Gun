package guicomponents;

import guicomponents.HotSpot.HSrect;
import guicomponents.StyledString.TextLayoutHitInfo;
import guicomponents.StyledString.TextLayoutInfo;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.font.TextHitInfo;
import java.awt.font.TextLayout;
import java.awt.geom.GeneralPath;
import java.util.LinkedList;

import processing.core.PApplet;
import processing.core.PGraphicsJava2D;


public class FTextArea extends FTextComponent {

	private static float pad = 6;



	public FTextArea(PApplet theApplet, float p0, float p1, float p2, float p3) {
		this(theApplet, p0, p1, p2, p3, SCROLLBARS_NONE);
	}

	public FTextArea(PApplet theApplet, float p0, float p1, float p2, float p3, int scrollbars) {
		super(theApplet, p0, p1, p2, p3, scrollbars);
		tx = ty = pad;
		tw = width - 2 * pad - ((sbPolicy & SCROLLBAR_VERTICAL) != 0 ? 18 : 0);
		th = height - 2 * pad - ((sbPolicy & SCROLLBAR_HORIZONTAL) != 0 ? 18 : 0);
		gpTextDisplayArea = new GeneralPath();
		gpTextDisplayArea.moveTo( 0,  0);
		gpTextDisplayArea.lineTo( 0, th);
		gpTextDisplayArea.lineTo(tw, th);
		gpTextDisplayArea.lineTo(tw,  0);
		gpTextDisplayArea.closePath();

		// The image buffer is just for the typing area
		buffer = (PGraphicsJava2D) winApp.createGraphics((int)width, (int)height, PApplet.JAVA2D);
		buffer.rectMode(PApplet.CORNER);
		buffer.g2.setFont(fLocalFont);
		hotspots = new HotSpot[]{
				new HSrect(1, tx, ty, tw, th),			// typing area
				new HSrect(9, 0, 0, width, height),		// control surface
		};
		if((sbPolicy & SCROLLBAR_HORIZONTAL) != 0){
			hsb = new FScrollbar(theApplet, 0, 0, tw, 16);
			addCompoundControl(hsb, tx, ty + th + 2, 0);
			hsb.addEventHandler(this, "hsbEventHandler");
			hsb.setAutoHide(autoHide);
		}
		if((sbPolicy & SCROLLBAR_VERTICAL) != 0){
			vsb = new FScrollbar(theApplet, 0, 0, th, 16);
			addCompoundControl(vsb, tx + tw + 18, ty, PI/2);
			vsb.addEventHandler(this, "vsbEventHandler");
			vsb.setAutoHide(autoHide);
		}
		setTextNew(" ", (int)tw);
		z = Z_STICKY;
		registerAutos_DMPK(true, true, false, true);
	}

	/**
	 * Set the text to be used. The wrap width is determined by the size of the component.
	 * @param text
	 */
	public void setTextNew(String text){
		setText(text, (int)tw);
	}

	/**
	 * Set the text to display and adjust any scrollbars
	 * @param text
	 * @param wrapWidth
	 */
	public void setTextNew(String text, int wrapWidth){
		this.text = text;
		stext = new StyledString(buffer.g2, text, wrapWidth);
		float sTextHeight;
		if(vsb != null){
			sTextHeight = stext.getTextAreaHeight();
			ptx = pty = 0;
			if(sTextHeight < th)
				vsb.setValue(0.0f, 1.0f);
			else 
				vsb.setValue(0, th/sTextHeight);
		}
		// If needed update the horizontal scrollbar
		if(hsb != null){
			if(stext.getMaxLineLength() < tw)
				hsb.setValue(0,1);
			else
				hsb.setValue(0, tw/stext.getMaxLineLength());
		}
	}

	/**
	 * If the buffer is invalid then redraw it.
	 */
	protected void updateBuffer(){
		if(bufferInvalid) {
			Graphics2D g2d = buffer.g2;
			TextLayoutHitInfo startSelTLHI = null, endSelTLHI = null;

			buffer.beginDraw();
			// Whole control surface if opaque
			if(opaque)
				buffer.background(palette[6]);
			else
				buffer.background(buffer.color(255,0));

			// Now move to top left corner of text display area
			buffer.translate(tx,ty); 

			// Typing area surface
			buffer.noStroke();
			buffer.fill(palette[7]);
			buffer.rect(-1,-1,tw+2,th+2);

			g2d.setClip(gpTextDisplayArea);
			buffer.translate(-ptx, -pty);
			// Translate in preparation for display selection and text
//			buffer.strokeWeight(1.5f);

			// Get latest version of styled text layouts
			LinkedList<TextLayoutInfo> lines = stext.getLines(g2d);

			boolean hasSelection = hasSelection();
			if(hasSelection){
				if(endTLHI.compareTo(startTLHI) == -1){
					startSelTLHI = endTLHI;
					endSelTLHI = startTLHI;
				}
				else {
					startSelTLHI = startTLHI;
					endSelTLHI = endTLHI;
				}
			}	

			// Display selection and text
			for(TextLayoutInfo lineInfo : lines){
				TextLayout layout = lineInfo.layout;
				buffer.translate(0, layout.getAscent());
				// Draw selection if any
				if(hasSelection && lineInfo.compareTo(startSelTLHI.tli) >= 0 && lineInfo.compareTo(endSelTLHI.tli) <= 0 ){				
					int ss = 0;
					ss = (lineInfo.compareTo(startSelTLHI.tli) == 0) ? startSelTLHI.thi.getInsertionIndex()  : 0;
					int ee = endSelTLHI.thi.getInsertionIndex();
					ee = (lineInfo.compareTo(endSelTLHI.tli) == 0) ? endSelTLHI.thi.getInsertionIndex() : lineInfo.nbrChars-1;
					g2d.setColor(jpalette[14]);
					Shape selShape = layout.getLogicalHighlightShape(ss, ee);
					g2d.fill(selShape);
				}
				// display text
				g2d.setColor(jpalette[2]);
				lineInfo.layout.draw(g2d, 0, 0);
				buffer.translate(0, layout.getDescent() + layout.getLeading());
			}
			g2d.setClip(null);
			buffer.endDraw();
			bufferInvalid = false;
		}
	}


	/**
	 * See if the cursor is off screen if so pan the display
	 * @return
	 */
	protected boolean keepCursorInDisplay(){
		boolean horzScroll = false, vertScroll = false;
		if(endTLHI != null){
			if(caretX < ptx ){ 										// LEFT?
				ptx--;
				if(ptx < 0) ptx = 0;
				horzScroll = true;
			}
			else if(caretX > ptx + tw - 2){ 						// RIGHT?
				ptx++;
				horzScroll = true;
			}
			if(caretY < pty){										// UP?
				pty--;
				if(pty < 0) pty = 0;
				vertScroll = true;
			}
			else if(caretY > pty + th  + stext.getMaxLineHeight()){	// DOWN?
				pty++;
				vertScroll = true;
			}
			if(horzScroll && hsb != null)
				hsb.setValue(ptx / (stext.getMaxLineLength() + 4));
			if(vertScroll && vsb != null)
				vsb.setValue(pty / (stext.getTextAreaHeight() + 1.5f * stext.getMaxLineHeight()));
		}
		// If we have scrolled invalidate the buffer otherwise forget it
		if(horzScroll || vertScroll)
			bufferInvalid = true;
		// Let the user know we have scrolled
		return horzScroll | vertScroll;
	}

	public void draw(){
		if(!visible) return;

		// Uodate buffer if invalid
		updateBuffer();
		winApp.pushStyle();

		winApp.pushMatrix();
		// Perform the rotation
		winApp.translate(cx, cy);
		winApp.rotate(rotAngle);

		winApp.pushMatrix();
		// Move matrix to line up with top-left corner
		winApp.translate(-halfWidth, -halfHeight);
		// Draw buffer
		winApp.imageMode(PApplet.CORNER);
		winApp.image(buffer, 0, 0);
		
		// Draw caret if text display area
		if(showCaret && endTLHI != null){
			float[] cinfo = endTLHI.tli.layout.getCaretInfo(endTLHI.thi);
			float x_left =  - ptx + cinfo[0];
			float y_top = - pty + endTLHI.tli.yPosInPara; 
			float y_bot = y_top - cinfo[3] + cinfo[5];
			if(x_left >= 0 && x_left <= tw && y_top >= 0 && y_bot <= th){
				winApp.strokeWeight(1.9f);
				winApp.stroke(palette[15]);
				winApp.line(tx+x_left, ty+Math.max(0, y_top), tx+x_left, ty+Math.min(th, y_bot));
			}
		}
		
		winApp.popMatrix();

		if(children != null){
			for(GComponent c : children)
				c.draw();
		}
		winApp.popMatrix();
		winApp.popStyle();
	}

	protected boolean processKeyPressed(KeyEvent e, boolean shiftDown, boolean ctrlDown){
		int keyCode = e.getKeyCode();
		boolean caretMoved = false;

		switch(keyCode){
		case KeyEvent.VK_LEFT:
			caretMoved = moveCaretLeft(endTLHI);
			break;
		case KeyEvent.VK_RIGHT:
			caretMoved = moveCaretRight(endTLHI);
			break;
		case KeyEvent.VK_UP:
			caretMoved = moveCaretUp(endTLHI);
			break;
		case KeyEvent.VK_DOWN:
			caretMoved = moveCaretDown(endTLHI);
			break;
		case KeyEvent.VK_HOME:
			if(ctrlDown){		// move to start of text
				caretMoved = moveCaretStartOfText(endTLHI);
			}
			else 	// Move to start of line
				caretMoved = moveCaretStartOfLine(endTLHI);
			break;
		case KeyEvent.VK_END:
			if(ctrlDown){		// move to end of text
				caretMoved = moveCaretEndOfText(endTLHI);
			}
			else 	// Move to end of line
				caretMoved = moveCaretEndOfLine(endTLHI);
			break;
		}
		calculateCaretPos(endTLHI);
		if(caretX > stext.getWrapWidth()){
			switch(keyCode){
			case KeyEvent.VK_LEFT:
			case KeyEvent.VK_UP:
			case KeyEvent.VK_DOWN:
			case KeyEvent.VK_END:
				moveCaretLeft(endTLHI);
				caretMoved = true;
				break;
			case KeyEvent.VK_RIGHT:
				if(!moveCaretRight(endTLHI))
					moveCaretLeft(endTLHI);
				caretMoved = true;
			}
			// Calculate new caret position
			calculateCaretPos(endTLHI);
		}
		// After testing for cursor moving keys
		if(caretMoved){
			if(!shiftDown)				// Not extending selection
				startTLHI.copyFrom(endTLHI);
			else
				bufferInvalid = true;		// Selection changed
		}
		return caretMoved;
	}

	public void keyEvent(KeyEvent e) {
		if(!visible  || !enabled || !available) return;
		if(focusIsWith == this && endTLHI != null){
			boolean shiftDown = ((e.getModifiersEx() & KeyEvent.SHIFT_DOWN_MASK) == KeyEvent.SHIFT_DOWN_MASK);
			boolean ctrlDown = ((e.getModifiersEx() & KeyEvent.CTRL_DOWN_MASK) == KeyEvent.CTRL_DOWN_MASK);

			if(e.getID() == KeyEvent.KEY_PRESSED) {
				processKeyPressed(e, shiftDown, ctrlDown);
				setScrollbarValues(ptx, pty);
				while(keepCursorInDisplay());
			}
			else if(e.getID() == KeyEvent.KEY_TYPED && e.getKeyChar() != KeyEvent.CHAR_UNDEFINED){
				processKeyTyped(e, shiftDown, ctrlDown);
				setScrollbarValues(ptx, pty);
				while(keepCursorInDisplay());
			}
		}
	}

	/**
	 * Move caret to home position
	 * @return true if caret moved else false
	 */
	protected boolean moveCaretStartOfLine(TextLayoutHitInfo currPos){
		if(currPos.thi.getCharIndex() == 0)
			return false; // already at start of line
		currPos.thi = currPos.tli.layout.getNextLeftHit(1);
		return true;
	}

	protected boolean moveCaretEndOfLine(TextLayoutHitInfo currPos){
		if(currPos.thi.getCharIndex() == currPos.tli.nbrChars - 1)
			return false; // already at end of line
		currPos.thi = currPos.tli.layout.getNextRightHit(currPos.tli.nbrChars - 1);
		return true;
	}

	protected boolean moveCaretStartOfText(TextLayoutHitInfo currPos){
		if(currPos.tli.lineNo == 0 && currPos.thi.getCharIndex() == 0)
			return false; // already at start of text
		currPos.tli = stext.getTLIforLineNo(0);
		currPos.thi = currPos.tli.layout.getNextLeftHit(1);
		return true;
	}

	protected boolean moveCaretEndOfText(TextLayoutHitInfo currPos){
		if(currPos.tli.lineNo == stext.getNbrLines() - 1 && currPos.thi.getCharIndex() == currPos.tli.nbrChars - 1)
			return false; // already at end of text
		currPos.tli = stext.getTLIforLineNo(stext.getNbrLines() - 1);		
		currPos.thi = currPos.tli.layout.getNextRightHit(currPos.tli.nbrChars - 1);
		return true;
	}


	protected boolean moveCaretUp(TextLayoutHitInfo currPos){
		if(currPos.tli.lineNo == 0)
			return false;
		TextLayoutInfo ntli = stext.getTLIforLineNo(currPos.tli.lineNo - 1);	
		TextHitInfo nthi = ntli.layout.hitTestChar(caretX, 0);
		currPos.tli = ntli;
		currPos.thi = nthi;
		return true;
	}

	protected boolean moveCaretDown(TextLayoutHitInfo currPos){
		if(currPos.tli.lineNo == stext.getNbrLines() - 1)
			return false;
		TextLayoutInfo ntli = stext.getTLIforLineNo(currPos.tli.lineNo + 1);	
		TextHitInfo nthi = ntli.layout.hitTestChar(caretX, 0);
		currPos.tli = ntli;
		currPos.thi = nthi;
		return true;
	}

	/**
	 * Move caret left by one character. If necessary move to the end of the line above
	 * @return true if caret was moved else false
	 */
	protected boolean moveCaretLeft(TextLayoutHitInfo currPos){
		TextLayoutInfo ntli;
		TextHitInfo nthi = currPos.tli.layout.getNextLeftHit(currPos.thi);
		if(nthi == null){ 
			// Move the caret to the end of the previous line 
			if(currPos.tli.lineNo == 0)
				// Can't goto previous line because this is the first line
				return false;
			else {
				// Move to end of previous line
				ntli = stext.getTLIforLineNo(currPos.tli.lineNo - 1);
				nthi = ntli.layout.getNextRightHit(ntli.nbrChars-1);
				currPos.tli = ntli;
				currPos.thi = nthi;
			}
		}
		else {
			// Move the caret to the left of current position
			currPos.thi = nthi;
		}
		return true;
	}

	/**
	 * Move caret left by one character. If necessary move to the end of the line above
	 * @return true if caret was moved else false
	 */
	protected boolean moveCaretRight(TextLayoutHitInfo currPos){
		TextLayoutInfo ntli;
		TextHitInfo nthi = currPos.tli.layout.getNextRightHit(currPos.thi);
		if(nthi == null){ 
			// Move the caret to the end of the previous line 
			if(currPos.tli.lineNo >= stext.getNbrLines() - 1)
				// Can't goto next line because this is the last line
				return false;
			else {
				// Move to start of next line
				ntli = stext.getTLIforLineNo(currPos.tli.lineNo + 1);
				nthi = ntli.layout.getNextLeftHit(1);
				currPos.tli = ntli;
				currPos.thi = nthi;
			}
		}
		else {
			// Move the caret to the right of current position
			currPos.thi = nthi;
		}
		return true;
	}

	public void mouseEvent(MouseEvent event){
		if(!visible  || !enabled || !available) return;

		calcTransformedOrigin(winApp.mouseX, winApp.mouseY);
		ox -= tx; oy -= ty; // Remove translation

		currSpot = whichHotSpot(ox, oy);

		if(currSpot == 1 || focusIsWith == this)
			cursorIsOver = this;
		else if(cursorIsOver == this)
			cursorIsOver = null;

		switch(event.getID()){
		case MouseEvent.MOUSE_PRESSED:
			if(currSpot == 1){
				if(focusIsWith != this && z > focusObjectZ()){
					takeFocus();
				}
				mdx = winApp.mouseX;
				mdy = winApp.mouseY;
				endTLHI = stext.calculateFromXY(buffer.g2, ox + ptx, oy + pty);
				startTLHI = new TextLayoutHitInfo(endTLHI);
				calculateCaretPos(endTLHI);
				bufferInvalid = true;
			}
			break;
		case MouseEvent.MOUSE_RELEASED:
			dragging = false;
			break;
		case MouseEvent.MOUSE_DRAGGED:
			if(focusIsWith == this){
				dragging = true;
				endTLHI = stext.calculateFromXY(buffer.g2, ox + ptx, oy + pty);
				calculateCaretPos(endTLHI);
				bufferInvalid = true;
				keepCursorInDisplay();
			}
			break;
		}
	}

	protected void calculateCaretPos(TextLayoutHitInfo tlhi){
		float temp[] = tlhi.tli.layout.getCaretInfo(tlhi.thi);
		caretX = temp[0];		
		caretY = tlhi.tli.yPosInPara;
	}

}

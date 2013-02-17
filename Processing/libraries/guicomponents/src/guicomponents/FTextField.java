package guicomponents;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.font.TextHitInfo;
import java.awt.font.TextLayout;
import java.awt.geom.GeneralPath;
import java.util.LinkedList;

import guicomponents.HotSpot.HSrect;
import guicomponents.StyledString.TextLayoutHitInfo;
import guicomponents.StyledString.TextLayoutInfo;
import processing.core.PApplet;
import processing.core.PGraphicsJava2D;

public class FTextField extends FTextComponent {

	int pad = 2;
	
	public FTextField(PApplet theApplet, float p0, float p1, float p2, float p3) {
		this(theApplet, p0, p1, p2, p3, SCROLLBARS_NONE);
	}
	
	public FTextField(PApplet theApplet, float p0, float p1, float p2, float p3, int scrollbars) {
		super(theApplet, p0, p1, p2, p3, scrollbars);
		tx = ty = pad;
		tw = width - 2 * pad;
		th = height - ((sbPolicy & SCROLLBAR_HORIZONTAL) != 0 ? 11 : 0);
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
			hsb = new FScrollbar(theApplet, 0, 0, tw, 10);
			addCompoundControl(hsb, tx, ty + th + 2, 0);
			hsb.addEventHandler(this, "hsbEventHandler");
			hsb.setAutoHide(autoHide);
		}
		setTextNew(" ");
		z = Z_STICKY;
		registerAutos_DMPK(true, true, false, true);
	}

	public void setTextNew(String text){
		if(text == null || text.length() == 0)
			text = " ";
		this.text = text;
		stext = new StyledString(buffer.g2, text);
		if(hsb != null){
			if(stext.getMaxLineLength() < tw)
				hsb.setValue(0,1);
			else
				hsb.setValue(0, tw/stext.getMaxLineLength());
		}
	}

	
	/**
	 * If the buffer is invalid then redraw it.
	 * @TODO need to use palette for colours
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
			if(stext.length() > 0){
				buffer.pushMatrix();
				for(TextLayoutInfo lineInfo : lines){
					TextLayout layout = lineInfo.layout;
					buffer.translate(0, layout.getAscent());
					// Draw selection if any
					if(hasSelection && lineInfo.compareTo(startSelTLHI.tli) >= 0 && lineInfo.compareTo(endSelTLHI.tli) <= 0 ){				
						int ss = startSelTLHI.thi.getInsertionIndex();
						int ee = endSelTLHI.thi.getInsertionIndex();
						g2d.setColor(jpalette[14]);
						Shape selShape = layout.getLogicalHighlightShape(ss, ee);
						g2d.fill(selShape);
					}
					// Draw text
					g2d.setColor(jpalette[2]);
					lineInfo.layout.draw(g2d, 0, 0);
					buffer.translate(0, layout.getDescent() + layout.getLeading());
				}
				buffer.popMatrix();
			}
			g2d.setClip(null);
//			// Draw caret
//			if(showCaret && endTLHI != null){
//				buffer.pushMatrix();
//				buffer.translate(0, endTLHI.tli.layout.getAscent() );
//				Shape[] caret = endTLHI.tli.layout.getCaretShapes(endTLHI.thi.getInsertionIndex());
//				g2d.setColor(jpalette[15]);
//				g2d.draw(caret[0]);
//				buffer.popMatrix();
//			}
			buffer.endDraw();
			bufferInvalid = false;
		}
	}

	
	/**
	 * See if the cursor is off screen if so pan the display
	 * @return
	 */
	protected boolean keepCursorInDisplay(){
		boolean horzScroll = false;
		if(endTLHI != null){
			if(caretX < ptx ){ 										// LEFT?
				ptx--;
				if(ptx < 0) ptx = 0;
				horzScroll = true;
			}
			else if(caretX > ptx + tw - 4){ 						// RIGHT?
				ptx++;
				horzScroll = true;
			}
			if(horzScroll && hsb != null)
				hsb.setValue(ptx / (stext.getMaxLineLength() + 4));
		}
		// If we have scrolled invalidate the buffer otherwise forget it
		if(horzScroll)
			bufferInvalid = true;
		// Let the user know we have scrolled
		return horzScroll;
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
			bufferInvalid = true;
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
		case KeyEvent.VK_HOME:
			caretMoved = moveCaretStartOfLine(endTLHI);
			break;
		case KeyEvent.VK_END:
			caretMoved = moveCaretEndOfLine(endTLHI);
			break;
		}
		calculateCaretPos(endTLHI);
		if(caretMoved){
			if(!shiftDown)				// Not extending selection
				startTLHI.copyFrom(endTLHI);
			else
				bufferInvalid = true;	
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

	public void draw(){
		if(!visible) return;
		updateBuffer();

		winApp.pushStyle();
		winApp.pushMatrix();

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


}

package guicomponents;

import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.font.TextHitInfo;
import java.awt.geom.GeneralPath;

import guicomponents.StyledString.TextLayoutHitInfo;
import guicomponents.StyledString.TextLayoutInfo;
import processing.core.PApplet;

public class FTextComponent extends GComponent {

	
	// The typing area
	protected float tx,ty,th,tw;
	// Offset to display area
	protected float ptx, pty;
	// Caret position
	protected float caretX, caretY;
	
	protected GeneralPath gpTextDisplayArea;
	
	
	protected TextLayoutHitInfo startTLHI = null, endTLHI = null;

	// Set to true when mouse is dragging : set false on button released
	protected boolean dragging = false;

	// The scrollbar policy
	protected final int sbPolicy;
	protected boolean autoHide = true;
	protected FScrollbar hsb, vsb;

	protected GTimer caretFlasher;
	protected boolean showCaret = false;


	public FTextComponent(PApplet theApplet, float p0, float p1, float p2, float p3, int scrollbars) {
		super(theApplet, p0, p1, p2, p3);
		sbPolicy = scrollbars;
		autoHide = ((scrollbars & SCROLLBARS_AUTOHIDE) == SCROLLBARS_AUTOHIDE);
		caretFlasher = new GTimer(theApplet, this, "flashCaret", 400);
		caretFlasher.start();
		opaque = true;
	}

//	void setScrollbarValues(){
//		float sTextHeight;
//		if(vsb != null){
//			sTextHeight = stext.getTextAreaHeight();
//			ptx = pty = 0;
//			if(sTextHeight < th)
//				vsb.setValue(0.0f, 1.0f);
//			else 
//				vsb.setValue(0, th/sTextHeight);
//		}
//		// If needed update the horizontal scrollbar
//		if(hsb != null){
//			if(stext.getMaxLineLength() < tw)
//				hsb.setValue(0,1);
//			else
//				hsb.setValue(0, tw/stext.getMaxLineLength());
//		}
//	}
	
	void setScrollbarValues(float sx, float sy){
		if(vsb != null){
			float sTextHeight = stext.getTextAreaHeight();
			ptx = pty = 0;
			if(sTextHeight < th)
				vsb.setValue(0.0f, 1.0f);
			else 
				vsb.setValue(sy/sTextHeight, th/sTextHeight);
		}
		// If needed update the horizontal scrollbar
		if(hsb != null){
			float sTextWidth = stext.getMaxLineLength();
			if(stext.getMaxLineLength() < tw)
				hsb.setValue(0,1);
			else
				hsb.setValue(sy/sTextWidth, tw/sTextWidth);
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

	/**
	 * Move caret left by one character.
	 * @return true if caret was moved else false
	 */
	protected boolean moveCaretLeft(TextLayoutHitInfo currPos){
		TextHitInfo nthi = currPos.tli.layout.getNextLeftHit(currPos.thi);
		if(nthi == null){ 
			return false;
		}
		else {
			// Move the caret to the left of current position
			currPos.thi = nthi;
		}
		return true;
	}

	/**
	 * Move caret right by one character.
	 * @return true if caret was moved else false
	 */
	protected boolean moveCaretRight(TextLayoutHitInfo currPos){
		TextHitInfo nthi = currPos.tli.layout.getNextRightHit(currPos.thi);
		if(nthi == null){ 
			return false;
		}
		else {
			currPos.thi = nthi;
		}
		return true;
	}
	
	
	public void setJustify(boolean justify){
		stext.setJustify(justify);
		bufferInvalid = true;
	}
	
	public void setLocalColorScheme(int cs){
		super.setLocalColorScheme(cs);
		if(hsb != null)
			hsb.setLocalColorScheme(localColorScheme);
		if(vsb != null)
			vsb.setLocalColorScheme(localColorScheme);
	}

	public void setFontNew(Font font){
		if(font != null && font != fLocalFont){
			fLocalFont = font;
			buffer.g2.setFont(fLocalFont);
		}
	}

	public boolean hasSelection(){
		return (startTLHI != null && endTLHI != null && startTLHI.compareTo(endTLHI) != 0);	
	}

	
	protected void calculateCaretPos(TextLayoutHitInfo tlhi){
		float temp[] = tlhi.tli.layout.getCaretInfo(tlhi.thi);
		caretX = temp[0];		
		caretY = tlhi.tli.yPosInPara;
	}

	protected void processKeyTyped(KeyEvent e, boolean shiftDown, boolean ctrlDown){
		int endChar = endTLHI.tli.startCharIndex + endTLHI.thi.getInsertionIndex();
		int startChar = (startTLHI != null) ? startTLHI.tli.startCharIndex + startTLHI.thi.getInsertionIndex() : endChar;
		int pos = endChar, nbr = 0, adjust = 0;
		boolean hasSelection = (startTLHI.compareTo(endTLHI) != 0);

		if(hasSelection){ // Have we some text selected?
			if(startChar < endChar){ // Forward selection
				pos = startChar; nbr = endChar - pos;
			}
			else if(startChar > endChar){ // Backward selection
				pos = endChar;	nbr = startChar - pos;
			}
		}
		
		char keyChar = e.getKeyChar();
		int ascii = (int)keyChar;
		boolean textChanged = false;
		// If we have a selection then any key typed will delete it
		if(hasSelection){
			stext.deleteCharacters(pos, nbr);
			adjust = 0; textChanged = true;
		}
		else {	// Only process back_space and delete if there was no selection
			if(keyChar == KeyEvent.VK_BACK_SPACE){
				if(stext.deleteCharacters(pos - 1, 1)){
					adjust = -1; textChanged = true;
				}
			}
			else if(keyChar == KeyEvent.VK_DELETE){
				if(stext.deleteCharacters(pos, 1)){
					adjust = 0; textChanged = true;
				}
			}
		}
		// Now we have got rid of any selection be can process other keys
		if(ascii >= 32 && ascii < 127){
			if(stext.insertCharacters(pos, "" + e.getKeyChar())){
				adjust = 1; textChanged = true;
			}
		}
		if(textChanged){
			// Get new caret character position
			pos += adjust;
			// Force update of lines since they have changed
			stext.getLines(buffer.g2);
			
			TextLayoutInfo tli;
			TextHitInfo thi = null, thiRight;

			tli = stext.getTLIforCharNo(pos);

			int posInLine = pos - tli.startCharIndex;

			// Get some hit info so we can see what is happening
			try{
			}
			catch(Exception excp){
			}
			try{
				thiRight = tli.layout.getNextRightHit(posInLine);
			}
			catch(Exception excp){
				thiRight = null;
			}
			
			if(posInLine <= 0){					// At start of line
				thi = tli.layout.getNextLeftHit(thiRight);				
			}
			else if(posInLine >= tli.nbrChars){	// End of line
				thi = tli.layout.getNextRightHit(tli.nbrChars - 1);					
			}
			else {								// Character in line;
				thi = tli.layout.getNextLeftHit(thiRight);	
			}

			endTLHI.setInfo(tli, thi);
			// Cursor at end of paragraph graphic
			calculateCaretPos(endTLHI);
			
			// Finish off by ensuring no selection, invalidate buffer etc.
			startTLHI.copyFrom(endTLHI);
			bufferInvalid = true;
		} // End of text changed == true
	}
	
	public void flashCaret(){
		showCaret = !showCaret;
	}
	
	public void hsbEventHandler(FScrollbar scrollbar){
		ptx = hsb.getValue() * (stext.getMaxLineLength() + 4);
		bufferInvalid = true;
	}

	public void vsbEventHandler(FScrollbar scrollbar){
		pty = vsb.getValue() * (stext.getTextAreaHeight() + 1.5f * stext.getMaxLineHeight());
		bufferInvalid = true;
	}

}

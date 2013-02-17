package guicomponents;

import guicomponents.HotSpot.HSrect;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.util.Arrays;

import processing.core.PApplet;
import processing.core.PGraphicsJava2D;

public class FScrollbar extends GComponent {

	private static final int OFF_FILL = 3;
	private static final int OFF_STROKE = 0;
	private static final int OVER_FILL = 1;
	private static final int OVER_STROKE = 3;
	private static final int TRACK = 5;

	protected RoundRectangle2D lowCap, highCap;
	private BasicStroke pen = new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);

	protected float value = 0.2f;
	protected float filler = .5f;
	protected boolean autoHide = true;
	protected boolean currOverThumb = false;
	protected boolean isValueChanging = false;

	protected float last_ox, last_oy;


	public FScrollbar(PApplet theApplet, float p0, float p1, float p2, float p3) {
		super(theApplet, p0, p1, p2, p3);
		buffer = (PGraphicsJava2D) winApp.createGraphics((int)width, (int)height, PApplet.JAVA2D);
		buffer.rectMode(PApplet.CORNER);
		hotspots = new HotSpot[]{
				new HSrect(1, 0, 0, 16, height),			// low cap
				new HSrect(2, width - 16, 0, 16, height),	// high cap
				new HSrect(9, 17, 0, width - 17, height)	// thumb track
		};
		Arrays.sort(hotspots); // belt and braces

		lowCap = new RoundRectangle2D.Float(1, 1, 15, height-2, 6, 6);
		highCap = new RoundRectangle2D.Float(width - 15, 1, 14.5f, height-2, 6, 6);

		opaque = false;
		
		z = Z_SLIPPY;
		registerAutos_DMPK(true, true, false, false);
		createEventHandler(G4P.mainWinApp, "handleScrollbarEvents", new Class[]{ FScrollbar.class });
	}

	public void setAutoHide(boolean autoHide){
		if(this.autoHide != autoHide){
			this.autoHide = autoHide;
			if(this.autoHide && filler > 0.99999f)
				visible = false;
			bufferInvalid = true;
		}
	}
	public void setValue(float value){
		if(value + filler > 1)
			filler = 1 - value;
		this.value = value;
		if(autoHide && filler > 0.99999f)
			visible = false;
		else
			visible = true;
		bufferInvalid = true;
	}

	public void setValue(float value, float filler){
		if(value + filler > 1)
			value = 1 - filler;
		this.value = value;
		this.filler = filler;
		if(autoHide && this.filler > 0.99999f)
			visible = false;
		else
			visible = true;
		bufferInvalid = true;
	}

	public float getValue(){
		return value;
	}

	/**
	 * All GUI components are registered for mouseEvents
	 */
	public void mouseEvent(MouseEvent event){
		if(!visible  || !enabled || !available) return;

		calcTransformedOrigin(winApp.mouseX, winApp.mouseY);

		int spot = whichHotSpot(ox, oy);
		// If over the track then see if we are over the thumb
		if(spot >= 9){
			if(isOverThumb(ox, oy))
				spot = 10;
			else
				spot = -1; // Over empty track so ignore
		}
		if(spot != currSpot){
			currSpot = spot;
			bufferInvalid = true;
		}

		if(currSpot>= 0 || focusIsWith == this)
			cursorIsOver = this;
		else if(cursorIsOver == this)
			cursorIsOver = null;

		switch(event.getID()){
		case MouseEvent.MOUSE_PRESSED:
			if(focusIsWith != this && currSpot>= 0 && z > focusObjectZ()){
				mdx = winApp.mouseX;
				mdy = winApp.mouseY;
				last_ox = ox; last_oy = oy;
				takeFocus();
			}
			break;
		case MouseEvent.MOUSE_CLICKED:
			if(focusIsWith == this){
				switch(currSpot){
				case 1:
					value -= 0.1f;
					if(value < 0)
						value = 0;
					bufferInvalid = true;
					eventType = CHANGED;
					fireEvent();
					break;
				case 2:
					value += 0.1f;
					if(value + filler > 1.0)
						value = 1 - filler;
					bufferInvalid = true;
					eventType = CHANGED;
					fireEvent();
					break;
				}
				loseFocus(null);
				mdx = mdy = Integer.MAX_VALUE;
			}
			break;
		case MouseEvent.MOUSE_RELEASED:
			if(focusIsWith == this && mouseHasMoved(winApp.mouseX, winApp.mouseY)){
				loseFocus(parent);
				mdx = mdy = Integer.MAX_VALUE;
				isValueChanging = false;
				bufferInvalid = true;
			}
			break;
		case MouseEvent.MOUSE_DRAGGED:
			if(focusIsWith == this){
				float movement = ox - last_ox;
				last_ox = ox;
				float deltaV = movement / (width - 32);
				value += deltaV;
				value = PApplet.constrain(value, 0, 1.0f - filler);
				isValueChanging = true;
				bufferInvalid = true;
				eventType = CHANGED;
				fireEvent();
			}
			break;
		}
		if(focusIsWith != this){
			currSpot = 0;
			bufferInvalid = true;
		}
	}

	protected boolean isOverThumb(float px, float py){
		float p = (px - 16) / (width - 32);
		boolean over =( p >= value && p < value + filler);
		return over;
	}

	protected void updateBuffer(){
		Graphics2D g2d = buffer.g2;
		buffer.beginDraw();
		if(opaque) {
			buffer.background(buffer.color(255,0));
			buffer.fill(palette[6]);
			buffer.noStroke();
			buffer.rect(8,0,width-16,height);
		}
		else
			buffer.background(buffer.color(255,0));
		// Draw the track
		buffer.fill(palette[TRACK]);
		buffer.noStroke();
		buffer.rect(8,3,width-8,height-5);
		g2d.setStroke(pen);

		// Draw the low cap
		buffer.strokeWeight(1.2f);
		if(currSpot == 1){
			g2d.setColor(jpalette[OVER_FILL]);
			g2d.fill(lowCap);
			g2d.setColor(jpalette[OVER_STROKE]);
			g2d.draw(lowCap);
		}
		else {
			g2d.setColor(jpalette[OFF_FILL]);
			g2d.fill(lowCap);
			g2d.setColor(jpalette[OFF_STROKE]);
			g2d.draw(lowCap);
		}
		// Draw the high cap
		if(currSpot == 2){
			g2d.setColor(jpalette[OVER_FILL]);
			g2d.fill(highCap);
			g2d.setColor(jpalette[OVER_STROKE]);
			g2d.draw(highCap);
		}
		else {
			g2d.setColor(jpalette[OFF_FILL]);
			g2d.fill(highCap);
			g2d.setColor(jpalette[OFF_STROKE]);
			g2d.draw(highCap);
		}
		// draw thumb
		float thumbWidth = (width - 32) * filler;
		RoundRectangle2D thumb = new RoundRectangle2D.Float(1,1,thumbWidth-1, height-2,6,6);
		buffer.translate((width - 32) * value + 16, 0);
		if(currSpot == 10 || isValueChanging){
			g2d.setColor(jpalette[OVER_FILL]);
			g2d.fill(thumb);
			g2d.setColor(jpalette[OVER_STROKE]);
			g2d.draw(thumb);
		}
		else {
			g2d.setColor(jpalette[OFF_FILL]);
			g2d.fill(thumb);
			g2d.setColor(jpalette[OFF_STROKE]);
			g2d.draw(thumb);
		}
		buffer.endDraw();
		bufferInvalid = false;
	}

	public void draw(){
		if(!visible) return;
		if(bufferInvalid)
			updateBuffer();

		winApp.pushStyle();
		winApp.pushMatrix();

		winApp.translate(cx, cy);
		winApp.rotate(rotAngle);
		winApp.imageMode(PApplet.CENTER);
		winApp.image(buffer, 0, 0);

		if(children != null){
			for(GComponent c : children)
				c.draw();
		}
		winApp.popMatrix();
		winApp.popStyle();

	}	

}

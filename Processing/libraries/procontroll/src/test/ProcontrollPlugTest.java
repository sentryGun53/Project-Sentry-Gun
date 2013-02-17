package test;

import processing.core.PApplet;
import procontroll.ControllDevice;
import procontroll.ControllIO;
import procontroll.ControllStick;

public class ProcontrollPlugTest extends PApplet{
	
	ControllIO controllIO;
	ControllDevice joypad;
	ControllStick stick1;
	ControllStick stick2;
	
	float transX;
	float transY;

	public void setup(){
		size(600,600,OPENGL);
		
		transX = width/2;
		transY = height/2;
		
		controllIO = ControllIO.getInstance(this);
		
		joypad = controllIO.getDevice("Logitech RumblePad 2 USB");
		joypad.plug(this, "handleButton1Press", ControllIO.ON_PRESS, 1);
		joypad.plug(this, "handleButton1Release", ControllIO.ON_RELEASE, 1);
		joypad.plug(this, "handleMovement", ControllIO.WHILE_PRESS, 0);
		
		stick1 = joypad.getStick(0);
		stick1.setMultiplier(PI);
		
		stick2 = joypad.getStick(1);
		stick2.setTolerance(0.06f);
		stick2.setMultiplier(0.05f);
	}
	
	public void handleButton1Press(){
		fill(255,0,0);
		joypad.rumble(1);
	}
	
	public void handleButton1Release(){
		fill(255);
	}
	
	public void handleMovement(final float i_x,final float i_y){
		transX += i_x;
		transY += i_y;
	}
	
	public void draw(){
		background(0);
		lights();
		translate(transX,transY,0);
		rotateX(stick2.getTotalY());
		rotateY(stick2.getTotalX());
		box(200);
	}
	
	

	public static void main(String[] args){
		PApplet.main(new String[] {ProcontrollPlugTest.class.getName()});
	}
}


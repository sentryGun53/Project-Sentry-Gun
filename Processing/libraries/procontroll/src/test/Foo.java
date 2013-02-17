package test;

import processing.core.PApplet;
import processing.core.PFont;
import procontroll.ControllIO;
import procontroll.ControllDevice;
import procontroll.ControllSlider;

public class Foo extends PApplet{
	
	public PFont font; 
		
	ControllIO controll;
	ControllDevice device;
	ControllSlider slider1;
	ControllSlider slider2;
	ControllSlider slider3;
	ControllSlider slider4;
	
	
	
	public void setup(){
		size(800, 600);
		frameRate(24);
		controll = ControllIO.getInstance(this);
		controll.printDevices();
		
		for(int i = 0; i < controll.getNumberOfDevices();i++){
			println(controll.getDevice(i).getNumberOfButtons());
			println(controll.getDevice(i).getNumberOfSliders());
			controll.getDevice(i).printSliders();
			controll.getDevice(i).printButtons();
		}
		device = controll.getDevice(0);
		device.open();
		device.setTolerance(0.06f);
		/**device = controll.getDevice(2);
		device.open();
		slider1 = device.getSlider(0); 
		slider1.relation(20);
		slider2 = device.getSlider(1); 
		slider2.relation(0.05f);
		slider3 = device.getSlider(2);
		slider3.relation(0.05f);
		slider4 = device.getSlider(3); 
		slider4.relation(0.05f);
		println("devices:"+controll.getNumberOfDevices());*/
		
	}


	public void draw(){
		background(255);
		println(">>>>>>>>>>>>>>>>>>"+frameRate);
		for(int i = 0; i < device.getNumberOfButtons();i++){
			println(device.getButton(i).pressed());
		}
		
		for(int i = 0; i < device.getNumberOfSliders();i++){
			println(device.getSlider(i).getValue());
		}
	}

	static public void main(String[] args){
		PApplet.main(new String[] {Foo.class.getName()});
	}
}
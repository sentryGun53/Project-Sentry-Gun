package procontroll.test;


import processing.core.PApplet;
import procontroll.ControllButton;
import procontroll.ControllDevice;
import procontroll.ControllIO;
import procontroll.ControllSlider;

public class ProControll extends PApplet{

	ControllIO controll;
	ControllDevice device;
	ControllSlider sliderX;
	ControllSlider sliderY;
	ControllButton button;

	float totalX;
	float totalY;

	public void setup(){
	  size(400,400);
	  
	  controll = ControllIO.getInstance(this);

	  device = controll.getDevice("Logitech RumblePad 2 USB");
	  //device.setTolerance(0.05f);
	  
	  sliderX = device.getSlider("X-Achse");
	  sliderY = device.getSlider("Y-Achse");
	  
	  button = device.getButton("Taste 0");
	  
	  totalX = width/2;
	  totalY = height/2;
	  
	  fill(0);
	  rectMode(CENTER);
	}


	public void draw(){
	  background(255);
	  
	  if(button.pressed()){
	    fill(255,0,0);
	  }else{
	    fill(0);
	  }
	  
	  println(sliderX.getTotalValue());
	  totalX = constrain(totalX + sliderX.getValue(),10,width-10);
	  totalY = constrain(totalY + sliderY.getValue(),10,height-10);
	  
	  rect(totalX,totalY,20,20);
	}


	public static void main(String[] args){
		PApplet.main(new String[] {ProControll.class.getName()});
	}
}


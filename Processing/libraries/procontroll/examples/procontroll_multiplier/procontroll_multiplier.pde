import procontroll.*;
import java.io.*;

ControllIO controll;
ControllDevice device;
ControllSlider sliderX;
ControllSlider sliderY;
ControllButton button;

void setup(){
  size(400,400);
  
  controll = ControllIO.getInstance(this);

  device = controll.getDevice("Logitech RumblePad 2 USB");
  device.setTolerance(0.05f);
  
  sliderX = device.getSlider("X-Achse");
  sliderY = device.getSlider("Y-Achse");
  
  println("tolerance: " + sliderX.getTolerance());
  
  button = device.getButton("Taste 0");
  
  fill(0);
  rectMode(CENTER);
}

float totalX = width/2;
float totalY = height/2;
float multiplier = 1;

void draw(){
  background(255);
  
  if(button.pressed()){
    fill(255,0,0);
    multiplier += 0.05;
  }else{
    fill(0);
    multiplier -= 0.1;
    multiplier = max(1,multiplier);
  }
  
  sliderX.setMultiplier(multiplier);
  sliderY.setMultiplier(multiplier);
  
  println("multiplier: " + sliderX.getMultiplier());
  
  totalX = constrain(totalX + sliderX.getValue(),10,width-10);
  totalY = constrain(totalY + sliderY.getValue(),10,height-10);
  
  rect(totalX,totalY,20,20);
}

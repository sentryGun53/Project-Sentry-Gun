import procontroll.*;
import java.io.*;

ControllIO controll;
ControllDevice device;
ControllStick stick;
ControllButton button;

void setup(){
  size(400,400);
  
  controll = ControllIO.getInstance(this);

  device = controll.getDevice("Logitech RumblePad 2 USB");
  device.printSticks();
  
  stick = device.getStick("Z-Achse Z-Rotation");
  stick.setTolerance(0.05f);
  
  println("tolerance: " + stick.getTolerance());
  
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
  
  stick.setMultiplier(multiplier);
  
  println("multiplier: " + stick.getMultiplier());
  
  totalX = constrain(totalX + stick.getX(),10,width-10);
  totalY = constrain(totalY + stick.getY(),10,height-10);
  
  rect(totalX,totalY,20,20);
}

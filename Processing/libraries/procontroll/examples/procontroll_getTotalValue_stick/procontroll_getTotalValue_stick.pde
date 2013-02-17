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
  
  button = device.getButton("Taste 0");
  
  fill(0);
  rectMode(CENTER);
}

void draw(){
  background(255);
  
  if(button.pressed()){
    fill(255,0,0);
  }else{
    fill(0);
  }
  
  float x = stick.getTotalX() + width/2;
  float y = stick.getTotalY() + height/2;
  
  if(x > width + 20 || x < - 20 || y > height + 20 || y < - 20){
    stick.reset();
  }
  
  rect(x,y,20,20);
}

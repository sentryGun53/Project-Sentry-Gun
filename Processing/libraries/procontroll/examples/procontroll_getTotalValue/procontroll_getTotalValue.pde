import procontroll.*;
import java.io.*;

ControllIO controlIO;
ControllDevice inputDevice;
ControllSlider sliderX;
ControllSlider sliderY;
ControllButton button;

void setup(){
  size(400,400);
  
  controlIO = ControllIO.getInstance(this);

  inputDevice = controlIO.getDevice(7);
  inputDevice.setTolerance(0.05f);
  
  sliderX = inputDevice.getSlider(7);
  sliderY = inputDevice.getSlider(6);
  
  button = inputDevice.getButton(7);
  
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
  
  float x = sliderX.getTotalValue() + width/2;
  float y = sliderY.getTotalValue() + height/2;
  
  if(x > width + 20 || x < - 20 || y > height + 20 || y < - 20){
    sliderX.reset();
    sliderY.reset();
  }
  
  rect(x,y,20,20);
}

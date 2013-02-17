import procontroll.*;
import java.io.*;

ControllIO controll;

void setup(){
  size(400,400);
  
  controll = ControllIO.getInstance(this);
  controll.printDevices();
  
  for(int i = 0; i < controll.getNumberOfDevices(); i++){
    ControllDevice device = controll.getDevice(i);
    
    println(device.getName()+" has:");
    println(" " + device.getNumberOfSliders() + " sliders");
    println(" " + device.getNumberOfButtons() + " buttons");
    println(" " + device.getNumberOfSticks() + " sticks");
    
    device.printSliders();
    device.printButtons();
    device.printSticks();
  }
}

import processing.opengl.*;

import procontroll.*;
import net.java.games.input.*;

ControllIO controllIO;
ControllDevice joypad;
ControllCoolieHat cooliehat;

float transX;
float transY;

void setup(){
  size(600,600,OPENGL);

  transX = width/2;
  transY = height/2;

  controllIO = ControllIO.getInstance(this);

  joypad = controllIO.getDevice("Logitech RumblePad 2 USB");
  joypad.printButtons();

  cooliehat = joypad.getCoolieHat(0);
  cooliehat.setMultiplier(4);
}

void handleButton1Press(){
  fill(255,0,0);
  joypad.rumble(1);
}

void draw(){
  transX += cooliehat.getX();
  transY += cooliehat.getY();
  
  background(0);
  lights();
  translate(transX,transY,0);
  box(200);
}

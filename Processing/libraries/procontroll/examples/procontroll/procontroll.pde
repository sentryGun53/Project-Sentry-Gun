import processing.opengl.*;

import procontroll.*;
import net.java.games.input.*;

ControllIO controllIO;
ControllDevice joypad;
ControllStick stick1;
ControllStick stick2;

float transX;
float transY;

void setup(){
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

void handleButton1Press(){
  fill(255,0,0);
  joypad.rumble(1);
}

void handleButton1Release(){
  fill(255);
}

void handleMovement(final float i_x,final float i_y){
  transX += i_x;
  transY += i_y;
}

void draw(){
  background(0);
  lights();
  translate(transX,transY,0);
  rotateX(stick2.getTotalY());
  rotateY(stick2.getTotalX());
  box(200);
}

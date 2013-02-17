/*
 -------- Project Sentry Gun:  MINIMAL TURRET EDITION ---------
 ==============================================================
 ------ An Open-Source Project, initiated by Bob Rudolph ------
 
 This is a stripped-down version of the Processing code, designed for the Raspberry Pi or other low-power platforms.
 Expect this minimal version to use about half the amount of memory the standard version uses.
 
 
 This version of the Processing code has no control panel, so you need to adjust your settings using this method:
 1) Run the standard version of the code (with control panel), and get all the settings just how you want them.
 2) Press 'Save Settings" in the standard version of the code, then close it (or press "Save and Exit")
 3) With the standard version open in the Processing IDE, click Sketch > Show Sketch Folder. You should see a /data/ folder.
 4) Copy "settings.txt" and "settings_inputDevice.txt" from the /data/ folder for the standard code to the /data/ folder for the minimal code.
 5) Run the minimal code. Your settings should be updated with the ones you set in the standard code.
 
 
 Press SPACE to toggle Autonomous/Manual modes.
 This code eliminates the following features:
    - Control panel and UI elements
    - Sound effects
 This code retains the following major features:
    - Compatible with the corresponding version of the Arduino code
    - Can use a USB joystick or game controller as an input device
 
 
 */


static final private boolean SHOW_WEBCAM = true;     // change this to false to keep the visuals from running

boolean PRINT_FRAMERATE = false;     // set to true to print the framerate at the bottom of the IDE window


public int camWidth;                   //   camera width (pixels),   usually 160*n
public int camHeight;                  //   camera height (pixels),  usually 120*n


public boolean mirrorCam;            //   set true to mirror camera image

public float xMin;                   //  0.0      used for calibration
public float xMax;                    //  180.0    
public float yMin;                    //  180.0
public float yMax;                    //  0.0

import JMyron.*;
import blobDetection.*;
import processing.serial.*;
import procontroll.*;
import net.java.games.input.*;

public int minBlobArea;                    //   minimum target size (pixels)
public int tolerance;                      //   sensitivity to motion

public boolean runWithoutArduino = false;
public boolean connecting = false;


public Serial arduinoPort;
JMyron camInput;
BlobDetection target;
Blob blob;
Blob biggestBlob;


int[] Background;
int[] rawImage;
int[] rawBackground;
int[] currFrame;

public int targetX = camWidth/2;
public int targetY = camHeight/2;
int fire = 0;
int[] prevFire = {
  0, 0, 0, 0, 0, 0, 0, 0, 0, 0
};

float xRatio;
float yRatio;

int possibleX = camWidth/2;
int possibleY = camHeight/2;

int displayX = camWidth/2;
int displayY = camHeight/2;

int oldX = camWidth/2; // smoothing (contributed by Adam S.)
int oldY = camHeight/2; // smoothing
int xdiff; // smoothing
int ydiff; // smoothing
public float smoothingFactor = 0.8; // smoothing
public boolean activeSmoothing = true;

String strTargetx;
String strTargety;
String fireSelector;
String scanSelector;

public boolean firingMode;             // true = semi,        false = auto
public boolean safety;
public boolean controlMode;           // true = autonomous,  false = manual
public boolean scanWhenIdle;
public boolean trackingMotion;

int idleTime = 10000;          // how many milliseconds to wait until scanning (when in scan mode)
int idleBeginTime = 0;
boolean scan = false;

public String serPortUsed;

boolean trackingColor;
int trackColorTolerance;
int trackColorRed;
int trackColorGreen;
int trackColorBlue;

boolean safeColor;
int safeColorMinSize;
int safeColorTolerance;
int safeColorRed;
int safeColorGreen;
int safeColorBlue;


public boolean useInputDevice;  // use a joystick or game controller as input (in manual mode)
public boolean inputDeviceIsSetup = false;

public ControllIO controlIO;         // more stuff for using a joystick or game controller for input
public ControllDevice inputDevice;

public ControllButton[] buttons = new ControllButton[30];
public ControllSlider[] sliders = new ControllSlider[10];

public ControllButton[] fire_buttons = new ControllButton[0];
public ControllButton[] preciseAim_buttons = new ControllButton[0];
public ControllButton[] centerGun_buttons = new ControllButton[0];
public ControllButton[] autoOn_buttons = new ControllButton[0];
public ControllButton[] autoOff_buttons = new ControllButton[0];
public ControllButton[] inputToggle_buttons = new ControllButton[0];
public ControllButton[] randomSound_buttons = new ControllButton[0];

public ControllSlider[] pan_sliders = new ControllSlider[0];
public ControllSlider[] tilt_sliders = new ControllSlider[0];
public ControllSlider[] panInvert_sliders = new ControllSlider[0];
public ControllSlider[] tiltInvert_sliders = new ControllSlider[0];


public float xPosition = camWidth/2;
public float yPosition = camHeight/2;


int effect = 0;
int[] diffPixelsColor = {
  255, 255, 0
};  // Red, green, blue values (0-255)  to show pixel as marked as target
int[] screenPixels; 
public boolean showDifferentPixels;
public boolean showTargetBox;
public boolean showCameraView;


void setup() {

  loadSettings();

  size(camWidth, camHeight);

  camInput = new JMyron();
  camInput.start(camWidth, camHeight);
  camInput.findGlobs(0);
  camInput.adaptivity(1.01);
  camInput.update();
  currFrame = camInput.image();
  rawImage = camInput.image();
  Background = camInput.image();
  rawBackground = camInput.image();

  screenPixels = camInput.image();

  target = new BlobDetection(camWidth, camHeight);
  target.setThreshold(0.9);
  target.setPosDiscrimination(true);

  retryArduinoConnect();

  xRatio = (camWidth / (xMax - xMin));                         // used to allign sights with crosshairs on PC
  yRatio = (camHeight/ (yMax - yMin));                         //
}



void draw() {
  if (PRINT_FRAMERATE) {
    println(frameRate);
  }

  if (controlMode) {              // autonomous mode
    autonomousMode();            //
  }
  else if (!controlMode) {        // manual mode
    manualMode();                //
  }

  if (fire == 1) {
    idleBeginTime = millis();
    scan = false;
  }
  else {
    if (millis() > idleBeginTime + idleTime && controlMode && scanWhenIdle) {
      scan = true;
    }
    else {
      scan = false;
    }
  }

  if (!safety) {
    fire = 0;
  }

  strTargetx = "000" + str(targetX);                   // make into 3-digit numbers
  strTargetx = strTargetx.substring(strTargetx.length()-3);
  strTargety = "000" + str(targetY);
  strTargety = strTargety.substring(strTargety.length()-3);
  fireSelector = str(0);
  if (firingMode) {
    fireSelector = str(1);
  }
  else {
    fireSelector = str(3);
  }
  if (scan) {
    scanSelector = str(1);
  }
  else {
    scanSelector = str(0);
  }
  //println('a' + strTargetx + strTargety  + str(fire) + fireSelector + scanSelector);
  if (!runWithoutArduino && !connecting) {
    arduinoPort.write('a' + strTargetx + strTargety + str(fire) + fireSelector + scanSelector);   // send to arduino
  }


  if (SHOW_WEBCAM) {
    if (fire == 1)
      strokeWeight(3);
    if (fire == 0)
      strokeWeight(1);
    stroke(255, 0, 0);                     //draw crosshairs
    noFill();                            // 
    line(displayX, 0, displayX, camHeight);  //
    line(0, displayY, camWidth, displayY);   //
    ellipse(displayX, displayY, 20, 20);     //
    ellipse(displayX, displayY, 28, 22);     //
    ellipse(displayX, displayY, 36, 24);     //
  }


  prevTargetX = targetX;
  prevTargetY = targetY;
}

void autonomousMode() {
  if (inputDeviceIsSetup) {
    checkInputDevice();
  }

  camInput.update();
  rawBackground = camInput.retinaImage();
  rawImage = camInput.image();

  if (mirrorCam) {
    for (int i = 0; i < camWidth*camHeight; i++) {
      int y = floor(i/camWidth);
      int x = i - (y*camWidth);
      x = camWidth-x;
      currFrame[i] = rawImage[(y*camWidth) + x-1];
      Background[i] = rawBackground[(y*camWidth) + x-1];
    }
  }
  else {
    currFrame = rawImage;
    Background = rawBackground;
  }

  int safeColorPixelsCounter = 0;

  loadPixels();                    // ??

  for (int i = 0; i < camWidth*camHeight; i++) {
    if (SHOW_WEBCAM) {
      pixels[i] = currFrame[i];                     // ??
    }

    boolean motion = (((abs(red(currFrame[i])-red(Background[i])) + abs(green(currFrame[i])-green(Background[i])) + abs(blue(currFrame[i])-blue(Background[i]))) > (200-tolerance)) && trackingMotion);
    boolean isTrackedColor = (((abs(red(currFrame[i])-trackColorRed) + abs(green(currFrame[i])-trackColorGreen) + abs(blue(currFrame[i])-trackColorBlue)) < trackColorTolerance) && trackingColor);

    boolean isSafeColor = (((abs(red(currFrame[i])-safeColorRed) + abs(green(currFrame[i])-safeColorGreen) + abs(blue(currFrame[i])-safeColorBlue)) < safeColorTolerance) && safeColor);

    if (motion || isTrackedColor) {
      screenPixels[i] = color(255, 255, 255);
      if (SHOW_WEBCAM) {
        if (showDifferentPixels) {
          if (effect == 0) {
            pixels[i] = color(diffPixelsColor[0], diffPixelsColor[1], diffPixelsColor[2]);                     // ??
          }
          else if (effect == 1) {
            pixels[i] = color((diffPixelsColor[0] + red(currFrame[i]))/2, (diffPixelsColor[1] + green(currFrame[i]))/2, (diffPixelsColor[2] + blue(currFrame[i]))/2);                     // ??
          }
          else if (effect == 2) {
            pixels[i] = color(255-red(currFrame[i]), 255-green(currFrame[i]), 255-blue(currFrame[i]));                     // ??
          }
          else if (effect == 3) {
            pixels[i] = color((diffPixelsColor[0] + (255-red(currFrame[i])))/2, (diffPixelsColor[1] + (255-green(currFrame[i])))/2, (diffPixelsColor[2] + (255-blue(currFrame[i])))/2);                     // ??
          }
        }
      }
    }
    else {
      screenPixels[i] = color(0, 0, 0);
    }

    if (isSafeColor) {
      safeColorPixelsCounter++;
      if (SHOW_WEBCAM) {
        pixels[i] = color(0, 255, 0);                     // ??
      }
      screenPixels[i] = color(0, 0, 0);
    }
  }

  if (SHOW_WEBCAM) {
    updatePixels();                        // ??
  }

  int biggestBlobArea = 0;
  target.computeBlobs(screenPixels);
  for (int i = 0; i < target.getBlobNb()-1; i++) {
    blob = target.getBlob(i);
    int blobWidth = int(blob.w*camWidth);
    int blobHeight = int(blob.h*camHeight);
    if (blobWidth*blobHeight >= biggestBlobArea) {
      biggestBlob = target.getBlob(i);
      biggestBlobArea = int(biggestBlob.w*camWidth)*int(biggestBlob.h*camHeight);
    }
  }
  possibleX = 0;
  possibleY = 0;

  if (biggestBlobArea >= minBlobArea) {
    possibleX = int(biggestBlob.x * camWidth);
    possibleY = int(biggestBlob.y * camHeight);
  }


  if ((biggestBlobArea >= minBlobArea)) {
    fire = 1;
    if (SHOW_WEBCAM) {
      if (showTargetBox) {
        stroke(255, 50, 50);
        strokeWeight(3);
        fill(255, 50, 50, 150);
        rect(int(biggestBlob.xMin*camWidth), int(biggestBlob.yMin*camHeight), int((biggestBlob.xMax-biggestBlob.xMin)*camWidth), int((biggestBlob.yMax-biggestBlob.yMin)*camHeight));
      }
    }
    anticipation();

    if (activeSmoothing) {
      xdiff = possibleX - oldX; // smoothing
      ydiff = possibleY - oldY; // smoothing
      possibleX = int(oldX + xdiff*(1.0-smoothingFactor)); // smoothing
      possibleY = int(oldY + ydiff*(1.0-smoothingFactor)); // smoothing
    }

    displayX = possibleX;
    displayY = possibleY;
    if (displayX < 0)
      displayX = 0;
    if (displayX > camWidth)
      displayX = camWidth;
    if (displayY < 0)
      displayY = 0;
    if (displayY > camHeight)
      displayY = 0;  
    targetX = int((possibleX/xRatio)+xMin);         
    targetY = int(((camHeight-possibleY)/yRatio)+yMin);
    oldX = possibleX; // smoothing
    oldY = possibleY; // smoothing
  }
  else {
    fire = 0;
  }


  if (safeColorPixelsCounter > safeColorMinSize && safeColor) {
    fire = 0;
    if (SHOW_WEBCAM) {
      noStroke();
      fill(0, 255, 0, 150);
      rect(0, 0, width, height);
      targetX = int((xMin+xMax)/2.0);
      targetY = int(yMin);
      displayX = camWidth/2;
      displayY = camHeight;
    }
  }
}

void manualMode() {

  camInput.update();
  rawBackground = camInput.retinaImage();
  rawImage = camInput.image();
  if (mirrorCam) {
    for (int i = 0; i < camWidth*camHeight; i++) {
      int y = floor(i/camWidth);
      int x = i - (y*camWidth);
      x = camWidth-x;
      currFrame[i] = rawImage[(y*camWidth) + x-1];
      Background[i] = rawBackground[(y*camWidth) + x-1];
    }
  }
  else {
    currFrame = rawImage;
    Background = rawBackground;
  }
  if (SHOW_WEBCAM) {
    loadPixels();                                 //draw camera view to screen                       // ??
    for (int i = 0; i < camWidth*camHeight; i++) {  //
      pixels[i] = currFrame[i];                   //                      // ??
    }                                             //
    updatePixels();                               //                        // ??
  }

  if (inputDeviceIsSetup) {
    checkInputDevice();
  }
  if (useInputDevice) {
    updateInputDevice();                        // determine control values using the input device (see declaration in Input_Device tab)
  }
  else {  
    if (SHOW_WEBCAM) {
      targetX = int((mouseX/xRatio)+xMin);                 // calculate position to go to based on mouse position
      targetY = int(((camHeight-mouseY)/yRatio)+yMin);     //                        // ??
      displayX = mouseX;
      displayY = mouseY;
      if (mousePressed) {
        fire = 1;
      }
      else {
        fire = 0;
      }
    }
  }
}


void keyReleased() {

  if (key == ' ') {
    controlMode = !controlMode;
  }

  if (key == 'b') {
    camInput.adapt();
  }

  if (key == 'a') {
    xMin = float(targetX);
    xRatio = (camWidth / (xMax - xMin));                         // used to allign sights with crosshairs on PC
  }
  if (key == 'd') {
    xMax = float(targetX);
    xRatio = (camWidth / (xMax - xMin));                         // used to allign sights with crosshairs on PC
  }
  if (key == 's') {
    yMin = float(targetY);
    yRatio = (camHeight/ (yMax - yMin));                         //
  }
  if (key == 'w') {
    yMax = float(targetY);
    yRatio = (camHeight/ (yMax - yMin));                         //
  }
}


public void setBackground() {
  camInput.adapt();
}



public void stop() {
  if (!runWithoutArduino) {
    arduinoPort.write("z0000000");
    delay(500);
    arduinoPort.stop();
  }
  camInput.stop();
  super.stop();
}


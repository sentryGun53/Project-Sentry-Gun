/* this section allows you to use a game controller to control your sentry.
Setup using "InputDeviceSetupTool.pde"
 
*/



void setupInputDevice() {
  if (!inputDeviceIsSetup) {

    String[] loadData = new String[49];
    loadData = loadStrings("settings_inputDevice.txt");

    controlIO = ControllIO.getInstance(this);

    boolean error = false;
    try {
      inputDevice = controlIO.getDevice(loadData[2]);
      // println("Device loaded successfully!");
    }
    catch (Exception e) {
      println("ERROR: Specified input device is not connected!");
      useInputDevice = false;
      return;
    }

    inputDevice.setTolerance(0.025f);

    println("Device Selected = " + inputDevice.getName());

    int numButtons = inputDevice.getNumberOfButtons();
    for (int i = 0; i < numButtons; i++) {
      if (i < buttons.length) {
        buttons[i] = inputDevice.getButton(i);
      }
    }
    // println("numButtons = " + numButtons);

    int numSliders = inputDevice.getNumberOfSliders();
    for (int i = 0; i < numSliders; i++) {
      if (i < sliders.length) {
        sliders[i] = inputDevice.getSlider(i);
      }
    }  
    // println("numSliders = " + numSliders);


    for (int i = 0; i <= 29; i++) {
            
      if (loadData[i+6].equals("Fire")) {
        fire_buttons = (ControllButton[]) append(fire_buttons, buttons[i]);
      }
      else if (loadData[i+6].equals("Precise Aim")) {
        preciseAim_buttons = (ControllButton[]) append(preciseAim_buttons, buttons[i]);
      }
      else if (loadData[i+6].equals("Center Gun")) {
        centerGun_buttons = (ControllButton[]) append(centerGun_buttons, buttons[i]);
      }
      else if (loadData[i+6].equals("Auto Aim On")) {
        autoOn_buttons = (ControllButton[]) append(autoOn_buttons, buttons[i]);
      }
      else if (loadData[i+6].equals("Auto Aim Off")) {
        autoOff_buttons = (ControllButton[]) append(autoOff_buttons, buttons[i]);
      }
      else if (loadData[i+6].equals("Input Dev On/Off")) {
        inputToggle_buttons = (ControllButton[]) append(inputToggle_buttons, buttons[i]);
      }
      else if (loadData[i+6].equals("Random Sound")) {
        randomSound_buttons = (ControllButton[]) append(randomSound_buttons, buttons[i]);
      }
    }


    for (int i = 0; i <= 9; i++) {
      if (loadData[i+39].equals("Pan")) {
        pan_sliders = (ControllSlider[]) append(pan_sliders, sliders[i]);
      }
      else if (loadData[i+39].equals("Tilt")) {
        tilt_sliders = (ControllSlider[]) append(tilt_sliders, sliders[i]);
      }
      else if (loadData[i+39].equals("Pan (Invert)")) {
        panInvert_sliders = (ControllSlider[]) append(panInvert_sliders, sliders[i]);
      }
      else if (loadData[i+39].equals("Tilt (Invert)")) {
        tiltInvert_sliders = (ControllSlider[]) append(tiltInvert_sliders, sliders[i]);
      }
    }

    inputDeviceIsSetup = true;
  }
}

void updateInputDevice() {
  if (!inputDeviceIsSetup) {
    setupInputDevice();
  }
  else {
    float xMotionValue = 0;
    for (int i = 0; i < pan_sliders.length; i++) {
      xMotionValue = xMotionValue + pan_sliders[i].getValue();
    }
    for (int i = 0; i < panInvert_sliders.length; i++) {
      xMotionValue = xMotionValue - panInvert_sliders[i].getValue();
    }

    float yMotionValue = 0;
    for (int i = 0; i < tilt_sliders.length; i++) {
      yMotionValue = yMotionValue + tilt_sliders[i].getValue();
    }
    for (int i = 0; i < tiltInvert_sliders.length; i++) {
      yMotionValue = yMotionValue - tiltInvert_sliders[i].getValue();
    }




    boolean triggerButtonValue = false;
    for (int i = 0; i < fire_buttons.length; i++) {
      if (fire_buttons[i].pressed()) {
        triggerButtonValue = true;
      }
    }

    boolean centerButtonValue = false;
    for (int i = 0; i < centerGun_buttons.length; i++) {
      if (centerGun_buttons[i].pressed()) {
        centerButtonValue = true;
      }
    }

    boolean precisionButtonValue = false;
    for (int i = 0; i < preciseAim_buttons.length; i++) {
      if (preciseAim_buttons[i].pressed()) {
        precisionButtonValue = true;
      }
    }


    float aimSensitivityX = map(pow(abs(xMotionValue), 2), 0.0, 1.0, 1.0, camWidth/10);    // set the sensitivity coeficcient for horizontal axis. Based on a quadratic correlation.
    float aimSensitivityY = map(pow(abs(yMotionValue), 2), 0.0, 1.0, 1.0, camWidth/10);    // set the sensitivity coeficcient for vertical axis. Based on a quadratic correlation.

    if (precisionButtonValue) {         // aim precisely if appropriate button is pressed
      aimSensitivityX *= 0.25;
      aimSensitivityY *= 0.25;
    }

    xPosition += aimSensitivityX * xMotionValue;   // update the position of the crosshairs
    yPosition += aimSensitivityY * yMotionValue;

    xPosition = constrain(xPosition, 0, camWidth);   // don't let the crosshairs leave the camera view
    yPosition = constrain(yPosition, 0, camHeight);  

    if (centerButtonValue) {       // center the crosshairs if appropriate button is pressed
      xPosition = camWidth/2;
      yPosition = camHeight/2;
    }

    if (triggerButtonValue) {   // fire if appropriate button is pressed
      fire = 1;
    }
    else {
      fire = 0;
    }

    targetX = int((xPosition/xRatio)+xMin);                 // calculate position to go to based on mouse position
    targetY = int(((camHeight-yPosition)/yRatio)+yMin);     //
    displayX = int(xPosition);
    displayY = int(yPosition);
  }
}

void checkInputDevice() {
  if (!inputDeviceIsSetup) {
    setupInputDevice();
  }
  else {
    boolean manualModeButtonValue = false;
    for (int i = 0; i < autoOff_buttons.length; i++) {
      if (autoOff_buttons[i].pressed()) {
        manualModeButtonValue = true;
      }
    }

    boolean autonomousModeButtonValue = false;
    for (int i = 0; i < autoOn_buttons.length; i++) {
      if (autoOn_buttons[i].pressed()) {
        autonomousModeButtonValue = true;
      }
    }

    boolean soundEffectButtonValue = false;
    for (int i = 0; i < randomSound_buttons.length; i++) {
      if (randomSound_buttons[i].pressed()) {
        soundEffectButtonValue = true;
      }
    }

    boolean activeButtonValue = false;
    for (int i = 0; i < inputToggle_buttons.length; i++) {
      if (inputToggle_buttons[i].pressed()) {
        activeButtonValue = true;
      }
    }

    if (manualModeButtonValue) { // go to manual mode if appropriate button is pressed
      controlMode = false;
    }
    if (autonomousModeButtonValue) { // go to autonomous mode if appropriate button is pressed
      controlMode = true;
    }
   
    if (activeButtonValue) {
      useInputDevice = !useInputDevice;
      while (activeButtonValue) {
        activeButtonValue = false;
        for (int i = 0; i < inputToggle_buttons.length; i++) {
          if (inputToggle_buttons[i].pressed()) {
            activeButtonValue = true;
          }
        }
      }
    }
  }
}


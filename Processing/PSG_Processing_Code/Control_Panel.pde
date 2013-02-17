// current issues: cannot show the frame around control panel while using a picture background. to see areas of code related to this, do a search/find for "(tag frame)"

public int controlPanelWindowX = 50;         // x position on screen of upper-left corner of control panel
public int controlPanelWindowY = 100;        // y position on screen of upper-left corner of control panel

import guicomponents.*;

GPanel panel_main; // control panel
PImage panelBackgroundImg;

GLabel label_serialOut, label_targetX, label_targetY, label_fire, label_fireSelector, label_scanSelector, label_runWithoutArduino, label_xMin, label_xMax, label_yMin, label_yMax, label_setxMin, label_setxMax, label_setyMin, label_setyMax;   // text labels on control panel
GCheckbox checkbox_leadTarget, checkbox_showRestrictedZones, checkbox_trackingColor, checkbox_safeColor, checkbox_trackingMotion, checkbox_showDifferentPixels, checkbox_showTargetBox, checkbox_mirrorCam, checkbox_controlMode, checkbox_safety, checkbox_showCameraView, checkbox_scanWhenIdle, checkbox_soundEffects, checkbox_activeSmoothing, checkbox_useInputDevice, checkbox_useArrowKeys;// checkboxes
GButton button_viewCameraSettings, button_setBackground, button_selectColor, button_selectSafeColor, button_openWebsite, button_playRandomSound, button_saveSettings, button_loadSettings, button_retryArduinoConnect, button_saveAndExit, button_configJoystick, button_resetCalibration, button_flipX, button_flipY;	// buttons
GWSlider slider_tolerance, slider_trackColorTolerance, slider_safeColorTolerance, slider_safeColorMinSize, slider_minBlobArea, slider_nbDot, slider_antSens, slider_propX, slider_propY, slider_smoothingFactor; //sliders
GLabel label_slider_tolerance, label_slider_trackColorTolerance, label_slider_safeColorTolerance, label_slider_safeColorMinSize, label_slider_minBlobArea, label_slider_nbDot, label_slider_antSens, label_slider_propX, label_slider_propY, label_smoothingFactor; // value readouts for sliders
// GTextField txfSomeText;   // textfield
GCombo dropdown_effect, dropdown_firingMode, dropdown_comPort;   // dropdown menus
//GActivityBar acyBar;   // activity bar
//GTimer tmrTimer;       // timer

GOptionGroup opgMouseOver;
GOption optHand, optXhair, optMove, optText, optWait;

// G4P components for second windowl
GWindow window_main;

int sliderInertia = 3;

void drawControlPanel() {

  G4P.setColorScheme(this, GCScheme.GREY_SCHEME);
  G4P.messagesEnabled(false);

  // create Panels
  panel_main = new GPanel(this, "Main", 0, 0, 600, 600);
  panel_main.setOpaque(false);
  panel_main.setCollapsed(false);

  // create labels
  label_serialOut = new GLabel(this, "Serial Out:           ", 300, 475, 150, 20);
  label_serialOut.setBorder(0);
  label_serialOut.setOpaque(false);
  label_serialOut.setColorScheme(GCScheme.GREY_SCHEME);
  panel_main.add(label_serialOut);

  label_targetX = new GLabel(this, "Pan Servo Position:    ", 300, 495, 150, 20);
  label_targetX.setBorder(0);
  label_targetX.setOpaque(false);
  label_targetX.setColorScheme(GCScheme.GREY_SCHEME);
  panel_main.add(label_targetX);

  label_targetY = new GLabel(this, "Tilt Servo Position:    ", 300, 515, 150, 20);
  label_targetY.setBorder(0);
  label_targetY.setOpaque(false);
  label_targetY.setColorScheme(GCScheme.GREY_SCHEME);
  panel_main.add(label_targetY);	

  label_fire = new GLabel(this, "Not Firing", 300, 535, 150, 20);
  label_fire.setBorder(0);
  label_fire.setOpaque(false); 
  label_fire.setColorScheme(GCScheme.RED_SCHEME);
  panel_main.add(label_fire);	

  label_fireSelector = new GLabel(this, "Automatic", 300, 555, 150, 20);
  label_fireSelector.setBorder(0);
  label_fireSelector.setOpaque(false);
  label_fireSelector.setColorScheme(GCScheme.GREY_SCHEME);
  panel_main.add(label_fireSelector);	

  label_scanSelector = new GLabel(this, "Scan When Idle", 300, 575, 150, 20);
  label_scanSelector.setBorder(0);
  label_scanSelector.setOpaque(false);
  label_scanSelector.setColorScheme(GCScheme.GREY_SCHEME);
  panel_main.add(label_scanSelector);	

  label_runWithoutArduino = new GLabel(this, "No Controller", 460, 475, 120, 20);
  label_runWithoutArduino.setBorder(0);
  label_runWithoutArduino.setOpaque(false);
  label_runWithoutArduino.setColorScheme(GCScheme.YELLOW_SCHEME);
  panel_main.add(label_runWithoutArduino);	

  label_xMin = new GLabel(this, "xMin: 000", 35, 362, 150, 20);
  label_xMin.setBorder(0);
  label_xMin.setOpaque(false);
  label_xMin.setColorScheme(GCScheme.YELLOW_SCHEME);
  panel_main.add(label_xMin);	

  label_xMax = new GLabel(this, "xMax: 180", 145, 362, 150, 20);
  label_xMax.setBorder(0);
  label_xMax.setOpaque(false);
  label_xMax.setColorScheme(GCScheme.YELLOW_SCHEME);
  panel_main.add(label_xMax);	

  label_yMin = new GLabel(this, "yMin: 000", 35, 392, 150, 20);
  label_yMin.setBorder(0);
  label_yMin.setOpaque(false);
  label_yMin.setColorScheme(GCScheme.YELLOW_SCHEME);
  panel_main.add(label_yMin);	

  label_yMax = new GLabel(this, "yMax: 180", 145, 392, 150, 20);
  label_yMax.setBorder(0);
  label_yMax.setOpaque(false);
  label_yMax.setColorScheme(GCScheme.YELLOW_SCHEME);
  panel_main.add(label_yMax);	

  label_setxMin = new GLabel(this, "to set xMin press A", 10, 375, 120, 10);
  panel_main.add(label_setxMin);

  label_setxMax = new GLabel(this, "to set xMax press D", 120, 375, 120, 10);
  panel_main.add(label_setxMax);

  label_setyMin = new GLabel(this, "to set yMin press S", 10, 405, 120, 10);
  panel_main.add(label_setyMin);

  label_setyMax = new GLabel(this, "to set yMax press W", 120, 405, 120, 10);
  panel_main.add(label_setyMax);

  // create checkboxes
  checkbox_leadTarget = new GCheckbox(this, "Enable Target Anticipation", 310, 325, 10);
  checkbox_leadTarget.setSelected(leadTarget);
  checkbox_leadTarget.setBorder(0);
  panel_main.add(checkbox_leadTarget);

  checkbox_showRestrictedZones = new GCheckbox(this, "Show Restricted Zones  (to set, hold R and click+drag)", 10, 480, 10);
  checkbox_showRestrictedZones.setSelected(showRestrictedZones);
  checkbox_showRestrictedZones.setBorder(0);
  panel_main.add(checkbox_showRestrictedZones);

  checkbox_trackingColor = new GCheckbox(this, "Track A Color", 10, 255, 10);
  checkbox_trackingColor.setSelected(trackingColor);
  checkbox_trackingColor.setBorder(0);
  panel_main.add(checkbox_trackingColor);

  checkbox_safeColor = new GCheckbox(this, "Enable Safe Color", 310, 175, 10);
  checkbox_safeColor.setSelected(safeColor);
  checkbox_safeColor.setBorder(0);
  panel_main.add(checkbox_safeColor);

  checkbox_trackingMotion = new GCheckbox(this, "Track Motion", 10, 205, 10);
  checkbox_trackingMotion.setSelected(trackingMotion);
  checkbox_trackingMotion.setBorder(0);
  panel_main.add(checkbox_trackingMotion);

  checkbox_showDifferentPixels = new GCheckbox(this, "Show Different Pixels", 10, 500, 10);
  checkbox_showDifferentPixels.setSelected(showDifferentPixels);
  checkbox_showDifferentPixels.setBorder(0);
  panel_main.add(checkbox_showDifferentPixels);

  checkbox_showTargetBox = new GCheckbox(this, "Show Target Box", 10, 520, 10);
  checkbox_showTargetBox.setSelected(showTargetBox);
  checkbox_showTargetBox.setBorder(0);
  panel_main.add(checkbox_showTargetBox);

  checkbox_mirrorCam = new GCheckbox(this, "Mirror Webcam", 10, 540, 10);
  checkbox_mirrorCam.setSelected(mirrorCam);
  checkbox_mirrorCam.setBorder(0);
  panel_main.add(checkbox_mirrorCam);

  checkbox_controlMode = new GCheckbox(this, "Enable Autonomous Mode (press SPACE to toggle)", 10, 25, 10);
  checkbox_controlMode.setSelected(controlMode);
  checkbox_controlMode.setBorder(0);
  panel_main.add(checkbox_controlMode);

  checkbox_safety = new GCheckbox(this, "Enable Weapon", 10, 325, 10);
  checkbox_safety.setSelected(safety);
  checkbox_safety.setBorder(0);
  panel_main.add(checkbox_safety);

  checkbox_showCameraView = new GCheckbox(this, "Show Camera View", 10, 560, 10);
  checkbox_showCameraView.setSelected(showCameraView);
  checkbox_showCameraView.setBorder(0);
  panel_main.add(checkbox_showCameraView);

  checkbox_scanWhenIdle = new GCheckbox(this, "Scan When Idle", 10, 345, 10);
  checkbox_scanWhenIdle.setSelected(scanWhenIdle);
  checkbox_scanWhenIdle.setBorder(0);
  panel_main.add(checkbox_scanWhenIdle);

  checkbox_soundEffects = new GCheckbox(this, "Enable Sounds Effects", 315, 25, 10);
  checkbox_soundEffects.setSelected(soundEffects);
  checkbox_soundEffects.setBorder(0);
  panel_main.add(checkbox_soundEffects);

  checkbox_activeSmoothing = new GCheckbox(this, "Smoothing", 460, 25, 10);
  checkbox_activeSmoothing.setSelected(activeSmoothing);
  checkbox_activeSmoothing.setBorder(0);
  panel_main.add(checkbox_activeSmoothing);

  checkbox_useInputDevice = new GCheckbox(this, "Use Joystick/Game Controller Input", 10, 45, 10);
  checkbox_useInputDevice.setSelected(useInputDevice);
  checkbox_useInputDevice.setBorder(0);
  panel_main.add(checkbox_useInputDevice);
  
  checkbox_useArrowKeys = new GCheckbox(this, "Use Arrow Keys to Fine Adjust (press SHIFT to toggle)", 10, 65, 10);
  checkbox_useArrowKeys.setSelected(useArrowKeys);
  checkbox_useArrowKeys.setBorder(0);
  panel_main.add(checkbox_useArrowKeys);


  // create buttons
  button_viewCameraSettings = new GButton(this, "Webcam Settings", 460, 75, 120, 10);
  panel_main.add(button_viewCameraSettings);

  button_setBackground = new GButton(this, "Save Current Image as Background", 110, 205, 185, 10);
  panel_main.add(button_setBackground);

  button_selectColor = new GButton(this, "Select Color to Track", 110, 255, 185, 10);
  panel_main.add(button_selectColor);

  button_selectSafeColor = new GButton(this, "Select Safe Color  ", 310, 200, 20, 10);
  panel_main.add(button_selectSafeColor);

  // with image
  button_openWebsite = new GButton(this, "", "Sentry_Logo_Tiny.png", 1, 545, 560, 36, 24);
  panel_main.add(button_openWebsite);

  button_playRandomSound = new GButton(this, "Play a Random Sound", 320, 75, 120, 10);
  panel_main.add(button_playRandomSound);

  button_saveSettings = new GButton(this, "Save Settings", 320, 110, 120, 10);
  panel_main.add(button_saveSettings);

  button_loadSettings = new GButton(this, "Re-Load Settings", 460, 110, 120, 10);
  panel_main.add(button_loadSettings);

  button_saveAndExit = new GButton(this, "Save Settings & Exit", 10, 95, 280, 40);
  panel_main.add(button_saveAndExit);

  button_retryArduinoConnect = new GButton(this, "Retry/Connect", 460, 500, 120, 10);
  panel_main.add(button_retryArduinoConnect);

  button_configJoystick  = new GButton(this, "Configure", 200, 45, 70, 10);
  panel_main.add(button_configJoystick);
  
  button_resetCalibration = new GButton(this, "Reset Calibration", 10, 425, 220, 10);
  panel_main.add(button_resetCalibration);
  
  button_flipX = new GButton(this, "Flip X", 240, 370, 40, 10);
  panel_main.add(button_flipX);
  
  button_flipY = new GButton(this, "Flip Y", 240, 400, 40, 10);
  panel_main.add(button_flipY);

  // create sliders
  slider_tolerance = new GWSlider(this, 10, 225, 200);
  slider_tolerance.setLimits(tolerance, 0, 200);
  slider_tolerance.setRenderMaxMinLabel(false);
  slider_tolerance.setRenderValueLabel(false);
  slider_tolerance.setTickCount(10);
  slider_tolerance.setInertia(sliderInertia);
  panel_main.add(slider_tolerance);
  label_slider_tolerance = new GLabel(this, "Tolerance: ", 210, 225, 150, 20);
  label_slider_tolerance.setBorder(0);
  label_slider_tolerance.setOpaque(false);
  label_slider_tolerance.setColorScheme(GCScheme.YELLOW_SCHEME);
  panel_main.add(label_slider_tolerance);

  slider_trackColorTolerance = new GWSlider(this, 10, 275, 200);
  slider_trackColorTolerance.setLimits(trackColorTolerance, 0, 300);
  slider_trackColorTolerance.setRenderMaxMinLabel(false);
  slider_trackColorTolerance.setRenderValueLabel(false);
  slider_trackColorTolerance.setTickCount(12);
  slider_trackColorTolerance.setInertia(sliderInertia);
  panel_main.add(slider_trackColorTolerance);
  label_slider_trackColorTolerance = new GLabel(this, "Tolerance: ", 210, 275, 150, 20);
  label_slider_trackColorTolerance.setBorder(0);
  label_slider_trackColorTolerance.setOpaque(false);
  label_slider_trackColorTolerance.setColorScheme(GCScheme.YELLOW_SCHEME);
  panel_main.add(label_slider_trackColorTolerance);

  slider_safeColorTolerance = new GWSlider(this, 310, 230, 200);
  slider_safeColorTolerance.setLimits(safeColorTolerance, 0, 300);
  slider_safeColorTolerance.setRenderMaxMinLabel(false);
  slider_safeColorTolerance.setRenderValueLabel(false);
  slider_safeColorTolerance.setTickCount(12);
  slider_safeColorTolerance.setInertia(sliderInertia);
  panel_main.add(slider_safeColorTolerance);
  label_slider_safeColorTolerance = new GLabel(this, "Tolerance: ", 510, 230, 150, 20);
  label_slider_safeColorTolerance.setBorder(0);
  label_slider_safeColorTolerance.setOpaque(false);
  label_slider_safeColorTolerance.setColorScheme(GCScheme.YELLOW_SCHEME);
  panel_main.add(label_slider_safeColorTolerance);

  slider_safeColorMinSize = new GWSlider(this, 310, 260, 200);
  slider_safeColorMinSize.setLimits(safeColorMinSize, 0, 5000);
  slider_safeColorMinSize.setRenderMaxMinLabel(false);
  slider_safeColorMinSize.setRenderValueLabel(false);
  slider_safeColorMinSize.setTickCount(10);
  slider_safeColorMinSize.setInertia(sliderInertia);
  panel_main.add(slider_safeColorMinSize);
  label_slider_safeColorMinSize = new GLabel(this, "Min Area: ", 510, 260, 150, 20);
  label_slider_safeColorMinSize.setBorder(0);
  label_slider_safeColorMinSize.setOpaque(false);
  label_slider_safeColorMinSize.setColorScheme(GCScheme.YELLOW_SCHEME);
  panel_main.add(label_slider_safeColorMinSize);

  slider_minBlobArea = new GWSlider(this, 10, 175, 200);
  slider_minBlobArea.setLimits(minBlobArea, 0, 10000);
  slider_minBlobArea.setRenderMaxMinLabel(false);
  slider_minBlobArea.setRenderValueLabel(false);
  slider_minBlobArea.setTickCount(10);
  slider_minBlobArea.setInertia(sliderInertia);
  panel_main.add(slider_minBlobArea);
  label_slider_minBlobArea = new GLabel(this, "Min Size: ", 210, 175, 150, 20);
  label_slider_minBlobArea.setBorder(0);
  label_slider_minBlobArea.setOpaque(false);
  label_slider_minBlobArea.setColorScheme(GCScheme.YELLOW_SCHEME);
  panel_main.add(label_slider_minBlobArea);

  slider_nbDot = new GWSlider(this, 310, 350, 200);
  slider_nbDot.setLimits(nbDot, 2, 22);
  slider_nbDot.setRenderMaxMinLabel(false);
  slider_nbDot.setRenderValueLabel(false);
  slider_nbDot.setTickCount(10);
  slider_nbDot.setInertia(sliderInertia);
  panel_main.add(slider_nbDot);
  label_slider_nbDot = new GLabel(this, "Memory: ", 510, 350, 150, 20);
  label_slider_nbDot.setBorder(0);
  label_slider_nbDot.setOpaque(false);
  label_slider_nbDot.setColorScheme(GCScheme.YELLOW_SCHEME);
  panel_main.add(label_slider_nbDot);

  slider_antSens = new GWSlider(this, 310, 375, 200);
  slider_antSens.setLimits(antSens, 1, 100);
  slider_antSens.setRenderMaxMinLabel(false);
  slider_antSens.setRenderValueLabel(false);
  slider_antSens.setTickCount(10);
  slider_antSens.setInertia(sliderInertia);
  panel_main.add(slider_antSens);
  label_slider_antSens = new GLabel(this, "Sensitivity: ", 510, 375, 150, 20);
  label_slider_antSens.setBorder(0);
  label_slider_antSens.setOpaque(false);
  label_slider_antSens.setColorScheme(GCScheme.YELLOW_SCHEME);
  panel_main.add(label_slider_antSens);

  slider_propX = new GWSlider(this, 310, 400, 120);
  slider_propX.setLimits(propX, 0.00, 3.00);
  slider_propX.setValueType(GWSlider.DECIMAL);
  slider_propX.setRenderMaxMinLabel(false);
  slider_propX.setRenderValueLabel(false);
  slider_propX.setTickCount(7);
  slider_propX.setInertia(sliderInertia);
  panel_main.add(slider_propX);
  label_slider_propX = new GLabel(this, "X Degree of Anticipation: ", 430, 400, 300, 20);
  label_slider_propX.setBorder(0);
  label_slider_propX.setOpaque(false);
  label_slider_propX.setColorScheme(GCScheme.YELLOW_SCHEME);
  panel_main.add(label_slider_propX);

  slider_propY = new GWSlider(this, 310, 425, 120);
  slider_propY.setLimits(propY, 0.00, 3.00);
  slider_propY.setValueType(GWSlider.DECIMAL);
  slider_propY.setRenderMaxMinLabel(false);
  slider_propY.setRenderValueLabel(false);
  slider_propY.setTickCount(7);
  slider_propY.setInertia(sliderInertia);
  panel_main.add(slider_propY);
  label_slider_propY = new GLabel(this, "Y Degree of Anticipation: ", 430, 425, 300, 20);
  label_slider_propY.setBorder(0);
  label_slider_propY.setOpaque(false);
  label_slider_propY.setColorScheme(GCScheme.YELLOW_SCHEME);
  panel_main.add(label_slider_propY); 

  slider_smoothingFactor = new GWSlider(this, 310, 50, 180);
  slider_smoothingFactor.setLimits(smoothingFactor, 0.00, 1.00);
  slider_smoothingFactor.setValueType(GWSlider.DECIMAL);
  slider_smoothingFactor.setRenderMaxMinLabel(false);
  slider_smoothingFactor.setRenderValueLabel(false);
  slider_smoothingFactor.setTickCount(10);
  slider_smoothingFactor.setInertia(sliderInertia);
  panel_main.add(slider_smoothingFactor);
  label_smoothingFactor = new GLabel(this, "Smoothing Factor", 490, 50, 100, 20);
  label_smoothingFactor.setBorder(0);
  label_smoothingFactor.setOpaque(false);
  label_smoothingFactor.setColorScheme(GCScheme.YELLOW_SCHEME);
  panel_main.add(label_smoothingFactor); 


  // createCombos (dropdown boxes)
  String[] entries_1 = new String[] {
    "Opaque", "Transparent", "Negative", "Negative & Transparent"
  };
  dropdown_effect = new GCombo(this, entries_1, entries_1.length, 140, 500, 140);        // this, String[] of entries, dropdown # of enteries shown at once, xPosition, yPosition, width
  dropdown_effect.setSelected(effect);                                                  // which entry to show as selected (first entry is 0, second is 1, etc.)
  panel_main.add(dropdown_effect);

  String[] entries_2 = new String[] {
    "Automatic", "Semi-Auto"
  };
  dropdown_firingMode = new GCombo(this, entries_2, entries_2.length, 140, 320, 100);        // this, String[] of entries, dropdown # of enteries shown at once, xPosition, yPosition, width
  dropdown_firingMode.setSelected(int(firingMode));                                                  // which entry to show as selected (first entry is 0, second is 1, etc.)
  panel_main.add(dropdown_firingMode);


  if (Serial.list().length > 0) {
    String[] entries_3 = append(Serial.list(), "Select to Override");
    dropdown_comPort = new GCombo(this, entries_3, entries_3.length, 460, 520, 120);  
    dropdown_comPort.setSelected(entries_3.length-1);
    panel_main.add(dropdown_comPort);
  }

  // Enable mouse over image changes
  G4P.setMouseOverEnabled(true);


  panelBackgroundImg = loadImage("Panel_Background.png");
  // create new window  (tag frame)
  window_main = new GWindow(this, "Control Panel", controlPanelWindowX, controlPanelWindowY, panelBackgroundImg, true, null);
//  window_main.setBackground(180);
  window_main.setOnTop(false);
  window_main.add(panel_main);
  window_main.addDrawHandler(this, "drawController");
  panel_main.setXY(0, 0);
}


public void updateControlPanels() {
  setLabelText(label_serialOut, "Serial Out: " + 'a' + strTargetx + strTargety + str(fire) + fireSelector + scanSelector);
  setLabelText(label_targetX, "Pan Servo Position: " + strTargetx);
  setLabelText(label_slider_tolerance, "Tolerance: " + str(tolerance));
  setLabelText(label_slider_trackColorTolerance, "Tolerance: " + str(trackColorTolerance));
  setLabelText(label_slider_safeColorTolerance, "Tolerance: " + str(safeColorTolerance));
  setLabelText(label_slider_safeColorMinSize, "Min Area: " + str(safeColorMinSize)); 
  setLabelText(label_slider_minBlobArea, "Min Size: " + str(minBlobArea));
  setLabelText(label_slider_nbDot, "Memory: " + str(nbDot));
  setLabelText(label_slider_antSens, "Sensitivity: " + str(antSens));
  setLabelText(label_slider_propX, "X Degree of Anitcipation: " + str(propX));
  setLabelText(label_slider_propY, "Y Degree of Anitcipation: " + str(propY));
  setLabelText(label_xMin, "xMin: " + str(xMin));
  setLabelText(label_xMax, "xMax: " + str(xMax));
  setLabelText(label_yMin, "yMin: " + str(yMin));
  setLabelText(label_yMax, "yMax: " + str(yMax));

  if (prevTargetX != targetX) {
    label_targetX.setOpaque(true);
  }
  else {
    label_targetX.setOpaque(false);
  }
  if (prevTargetY != targetY) {
    label_targetY.setOpaque(true);
  }
  else {
    label_targetY.setOpaque(false);
  }
  setLabelText(label_targetY, "Tilt Servo Position: " + strTargety);
  if (boolean(fire)) {
    label_fire.setOpaque(true);
    setLabelText(label_fire, "Firing");
  }
  else {
    label_fire.setOpaque(false);
    setLabelText(label_fire, "Not Firing");
  }
  if (firingMode) {
    setLabelText(label_fireSelector, "Semi-Automatic");
  }
  else {
    setLabelText(label_fireSelector, "Automatic");
  }
  if (!runWithoutArduino) {
    label_runWithoutArduino.setOpaque(true);
    setLabelText(label_runWithoutArduino, "Controller on " + serPortUsed);
  }
  else {
    label_runWithoutArduino.setOpaque(false);
    setLabelText(label_runWithoutArduino, "No Controller");
  }
  if (connecting) {
    label_runWithoutArduino.setOpaque(true);
    setLabelText(label_runWithoutArduino, "connecting...");
  }
  if (scanWhenIdle) {
    setLabelText(label_scanSelector, "Scan When Idle");
  }
  else{
    setLabelText(label_scanSelector, "Don't Scan When Idle");
  }
  checkbox_controlMode.setSelected(controlMode);
  checkbox_useInputDevice.setSelected(useInputDevice);
  checkbox_useArrowKeys.setSelected(useArrowKeys);
}


public void handleComboEvents(GCombo combo) {
  if (combo == dropdown_effect) {
    effect = dropdown_effect.selectedIndex();
  }
  if (combo == dropdown_firingMode) {
    firingMode = boolean(dropdown_firingMode.selectedIndex());
  }
  if (combo == dropdown_comPort) {
    if (dropdown_comPort.selectedIndex() < Serial.list().length) {
      if (!runWithoutArduino) {
        connecting = true;
        println("Manual override. Stopping old serial connection...");
        arduinoPort.stop();
        println("Stopped old serial connection.");
      }
      println("New COM port selected manually: " + Serial.list()[dropdown_comPort.selectedIndex()]);
      arduinoPort = new Serial(this, Serial.list()[dropdown_comPort.selectedIndex()], 4800);
      println("Serial Port used = " + Serial.list()[dropdown_comPort.selectedIndex()]);
      serPortUsed = Serial.list()[dropdown_comPort.selectedIndex()];
      runWithoutArduino = false;
      connecting = false;
    }
  }
}

public void handleSliderEvents(GSlider slider) {
  if (slider == slider_tolerance) {
    tolerance = slider_tolerance.getValue();
  }
  if (slider == slider_trackColorTolerance) {
    trackColorTolerance = slider_trackColorTolerance.getValue();
  }
  if (slider == slider_safeColorTolerance) {
    safeColorTolerance = slider_safeColorTolerance.getValue();
  }
  if (slider == slider_minBlobArea) {
    minBlobArea = slider_minBlobArea.getValue();
  }
  if (slider == slider_safeColorMinSize) {
    safeColorMinSize = slider_safeColorMinSize.getValue();
  }
  if (slider == slider_nbDot) {
    nbDot = slider_nbDot.getValue();
  }
  if (slider == slider_antSens) {
    antSens = slider_antSens.getValue();
  }
  if (slider == slider_propX) {
    propX = slider_propX.getValuef();
  }
  if (slider == slider_propY) {
    propY = slider_propY.getValuef();
  }
  if (slider == slider_smoothingFactor) {
    smoothingFactor = slider_smoothingFactor.getValuef();
  }
}


public void handleButtonEvents(GButton button) {
  if (button == button_viewCameraSettings && button.eventType == GButton.CLICKED) {
    viewCameraSettings();
  }
  if (button == button_setBackground && button.eventType == GButton.CLICKED) {
    setBackground();
  }
  if (button == button_selectColor && button.eventType == GButton.CLICKED) {
    selectColor();
  }
  if (button == button_selectSafeColor && button.eventType == GButton.CLICKED) {
    selectSafeColor();
  }
  if (button == button_openWebsite && button.eventType == GButton.CLICKED) {
    openWebsite();
  }
  if (button == button_playRandomSound && button.eventType == GButton.CLICKED) {
    playRandomSound();
  }
  if (button == button_saveSettings && button.eventType == GButton.CLICKED) {
    saveSettings();
  }
  if (button == button_loadSettings && button.eventType == GButton.CLICKED) {
    loadSettings();
  }
  if (button == button_saveAndExit && button.eventType == GButton.CLICKED) {
    saveSettings();
    delay(100);
    exit();
  }
  if (button == button_retryArduinoConnect && button.eventType == GButton.CLICKED) {
    if (!runWithoutArduino) {
      arduinoPort.stop();
    }
    runWithoutArduino = false;
    retryArduinoConnect();
  }
  if (button == button_configJoystick  && button.eventType == GButton.CLICKED) {
    configJoystick();
  }
  if(button == button_resetCalibration && button.eventType == GButton.CLICKED) {
    xMin = 0.0;
    xMax = 180.0;
    yMin = 0.0;
    yMax = 180;
    xRatio = (camWidth / (xMax - xMin));   
    yRatio = (camHeight/ (yMax - yMin));      
  }
  if(button == button_flipX && button.eventType == GButton.CLICKED) {
    float oldxMin = xMin;
    float oldxMax = xMax;
    xMin = oldxMax;
    xMax = oldxMin;
    xRatio = (camWidth / (xMax - xMin));     
  }
  if(button == button_flipY && button.eventType == GButton.CLICKED) {
    float oldyMin = yMin;
    float oldyMax = yMax;
    yMin = oldyMax;
    yMax = oldyMin;
    yRatio = (camHeight/ (yMax - yMin));                         
  }
}

public void handleCheckboxEvents(GCheckbox cbox) {
  if (cbox == checkbox_leadTarget) {
    leadTarget = checkbox_leadTarget.isSelected();
  }
  if (cbox == checkbox_showRestrictedZones) {
    showRestrictedZones = checkbox_showRestrictedZones.isSelected();
  }
  if (cbox == checkbox_trackingColor) {
    trackingColor = checkbox_trackingColor.isSelected();
  }
  if (cbox == checkbox_safeColor) {
    safeColor = checkbox_safeColor.isSelected();
  }
  if (cbox == checkbox_trackingMotion) {
    trackingMotion = checkbox_trackingMotion.isSelected();
  }
  if (cbox == checkbox_showDifferentPixels) {
    showDifferentPixels = checkbox_showDifferentPixels.isSelected();
  }
  if (cbox == checkbox_showTargetBox) {
    showTargetBox = checkbox_showTargetBox.isSelected();
  }
  if (cbox == checkbox_mirrorCam) {
    mirrorCam = checkbox_mirrorCam.isSelected();
  }
  if (cbox == checkbox_controlMode) {
    controlMode = checkbox_controlMode.isSelected();
  }
  if (cbox == checkbox_safety) {
    safety = checkbox_safety.isSelected();
  }
  if (cbox == checkbox_showCameraView) {
    showCameraView = checkbox_showCameraView.isSelected();
  }
  if (cbox == checkbox_scanWhenIdle) {
    scanWhenIdle = checkbox_scanWhenIdle.isSelected();
  }
  if (cbox == checkbox_soundEffects) {
    soundEffects = checkbox_soundEffects.isSelected();
  }
  if (cbox == checkbox_activeSmoothing) {
    activeSmoothing = checkbox_activeSmoothing.isSelected();
  }
  if (cbox == checkbox_useInputDevice) {
    useInputDevice = checkbox_useInputDevice.isSelected();
  }
  if (cbox == checkbox_useArrowKeys) {
    useArrowKeys = checkbox_useArrowKeys.isSelected();
  }
}

public void drawController(GWinApplet appc, GWinData data) {
  //  (tag frame)
}

public void setLabelText(GLabel label, String text) {
  try {
    label.setText(text);
  }
  catch (NullPointerException e) { // ignore
  }
}


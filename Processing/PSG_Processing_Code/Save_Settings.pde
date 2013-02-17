public void saveSettings() {
  
  String[] saveData = new String[40];
  
  saveData[0] = str(camWidth);
  saveData[1] = str(camHeight);
  saveData[2] = str(xMin);
  saveData[3] = str(xMax);
  saveData[4] = str(yMin);
  saveData[5] = str(yMax);
  saveData[6] = str(effect);
  saveData[7] = str(mirrorCam);
  saveData[8] = str(minBlobArea);
  saveData[9] = str(tolerance); 
  saveData[10] = str(runWithoutArduino);
  saveData[11] = str(smoothingFactor);
  saveData[12] = str(activeSmoothing);
  saveData[13] = str(showDifferentPixels);
  saveData[14] = str(showTargetBox);
  saveData[15] = str(showCameraView);
  saveData[16] = str(firingMode);
  saveData[17] = str(safety);
  saveData[18] = str(controlMode);
  saveData[19] = str(soundEffects);
  saveData[20] = str(scanWhenIdle);
  saveData[21] = str(trackingMotion);
  saveData[22] = str(showRestrictedZones);
  saveData[23] = str(trackingColor);
  saveData[24] = str(trackColorTolerance);
  saveData[25] = str(trackColorRed);
  saveData[26] = str(trackColorGreen);
  saveData[27] = str(trackColorBlue);
  saveData[28] = str(safeColor);
  saveData[29] = str(safeColorMinSize);
  saveData[30] = str(safeColorTolerance);
  saveData[31] = str(safeColorRed);
  saveData[32] = str(safeColorGreen);
  saveData[33] = str(safeColorBlue);
  saveData[34] = str(useInputDevice);
  saveData[35] = str(leadTarget);
  saveData[36] = str(nbDot);
  saveData[37] = str(antSens);
  saveData[38] = str(propX);
  saveData[39] = str(propY);
  
  saveStrings("data/settings.txt", saveData);
  println("Successfully saved settings to \"settings.txt\"");
  
}

public void loadSettings() {
  
  String[] loadData = new String[40];
  loadData = loadStrings("settings.txt");
  
//  camWidth = int(loadData[0]);
//  camHeight = int(loadData[1]);

  xMin = float(loadData[2]);
  xMax = float(loadData[3]);
  yMin = float(loadData[4]);
  yMax = float(loadData[5]);
  
  xRatio = (camWidth / (xMax - xMin)); 
  yRatio = (camHeight/ (yMax - yMin));  
  
  effect = int(loadData[6]);
  mirrorCam = boolean(loadData[7]);
  minBlobArea = int(loadData[8]);
  tolerance =  int(loadData[9]);
  runWithoutArduino = boolean(loadData[10]);
  smoothingFactor = float(loadData[11]);
  activeSmoothing = boolean(loadData[12]);
  showDifferentPixels = boolean(loadData[13]);
  showTargetBox = boolean(loadData[14]);
  showCameraView = boolean(loadData[15]);
  firingMode = boolean(loadData[16]);
  safety = boolean(loadData[17]);
  controlMode = boolean(loadData[18]);
  soundEffects = boolean(loadData[19]);
  scanWhenIdle = boolean(loadData[20]);
  trackingMotion = boolean(loadData[21]);
  showRestrictedZones = boolean(loadData[22]);
  trackingColor = boolean(loadData[23]);
  trackColorTolerance = int(loadData[24]);
  trackColorRed = int(loadData[25]);
  trackColorGreen = int(loadData[26]);
  trackColorBlue = int(loadData[27]);
  safeColor = boolean(loadData[28]);
  safeColorMinSize = int(loadData[29]);
  safeColorTolerance = int(loadData[30]);
  safeColorRed = int(loadData[31]);
  safeColorGreen = int(loadData[32]);
  safeColorBlue = int(loadData[33]);
  useInputDevice = boolean(loadData[34]);
  leadTarget = boolean(loadData[35]);
  nbDot = int(loadData[36]);
  antSens = int(loadData[37]);
  propX = float(loadData[38]);
  propY = float(loadData[39]);
  
  println("Successfully loaded settings from \"settings.txt\"");
}


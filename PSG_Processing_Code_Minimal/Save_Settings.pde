public void loadSettings() {
  
  String[] loadData = new String[40];
  loadData = loadStrings("settings.txt");
  
  
  camWidth = int(loadData[0]);
  camHeight = int(loadData[1]);

  xMin = float(loadData[2]);
  xMax = float(loadData[3]);
  yMin = float(loadData[4]);
  yMax = float(loadData[5]);
  
  effect = int(loadData[6]);

  mirrorCam = boolean(loadData[7]);
  minBlobArea = int(loadData[8]);
  tolerance =  int(loadData[9]);
  
//  runWithoutArduino = boolean(loadData[10]);

  smoothingFactor = float(loadData[11]);
  activeSmoothing = boolean(loadData[12]);
  
  showDifferentPixels = boolean(loadData[13]);
  showTargetBox = boolean(loadData[14]);
  showCameraView = boolean(loadData[15]);

  firingMode = boolean(loadData[16]);
  safety = boolean(loadData[17]);
  controlMode = boolean(loadData[18]);
  
//  soundEffects = boolean(loadData[19]);

  scanWhenIdle = boolean(loadData[20]);
  trackingMotion = boolean(loadData[21]);
  
//  showRestrictedZones = boolean(loadData[22]);

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


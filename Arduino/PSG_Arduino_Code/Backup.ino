//  contributed by Hugo K.

#include <EEPROM.h>
#include "EEPROMAnything.h"

/*
// Booleans but int
    int controlMode;
    int safety;
    int firingMode;
    int scanWhenIdle;
    int trackingMotion;
    int trackingColor;
    int leadTarget;
    int safeColor;
    int showRestrictedZones;
    int showDifferentPixels;
    int showTargetBox;
    int showCameraView;
    int mirrorCam;
    int soundEffects;
    
    // Integers
    int camWidth;
    int camHeight;
    int nbDot;
    int antSens;
    int minBlobArea;
    int tolerance;
    int effect;
    int trackColorTolerance;
    int trackColorRed;
    int trackColorGreen;
    int trackColorBlue;
    int safeColorMinSize;
    int safeColorTolerance;
    int safeColorRed;
    int safeColorGreen;
    int safeColorBlue;
    int idleTime;
    
    // Floats
    double propX;
    double propY;
    double xRatio;
    double yRatio;
    double xMin;
    double xMax;
    double yMin;
    double yMax;
*/

void backup(){
  
  char* Parameter;
  char* i;
  
  char BufferSerie[200];  
  for (int z=0;z<=200;z++) BufferSerie[z]='\0';
  
  // Save the String Send 
  
  byte bufferPos=0;        
  char ch; 
  boolean endOfString = false;
  
  while( !endOfString){ 
    if(Serial.available()){
      ch = Serial.read();
    
      if(ch != '!'){
     
        BufferSerie[bufferPos++] = ch;
      
      }else{
      
        endOfString = true;
      }  
    }
  }
  
  // Split the received String and update the struct configuration."Value"
  
// Booleans
  Parameter = strtok_r(BufferSerie, ";", &i);
  configuration1.controlMode = atoi(Parameter);
  
  Parameter = strtok_r(NULL, ";", &i);
  configuration1.safety = atoi(Parameter);

  Parameter = strtok_r(NULL, ";", &i);
  configuration1.firingMode = atoi(Parameter);

  Parameter = strtok_r(NULL, ";", &i);
  configuration1.scanWhenIdle = atoi(Parameter);

  Parameter = strtok_r(NULL, ";", &i);
  configuration1.trackingMotion = atoi(Parameter);

  Parameter = strtok_r(NULL, ";", &i);
  configuration1.trackingColor = atoi(Parameter);

  Parameter = strtok_r(NULL, ";", &i);
  configuration1.leadTarget = atoi(Parameter);

  Parameter = strtok_r(NULL, ";", &i);
  configuration1.safeColor = atoi(Parameter);

  Parameter = strtok_r(NULL, ";", &i);
  configuration1.showRestrictedZones = atoi(Parameter);

  Parameter = strtok_r(NULL, ";", &i);
  configuration1.showDifferentPixels = atoi(Parameter);
  
  Parameter = strtok_r(NULL, ";", &i);
  configuration1.showTargetBox = atoi(Parameter);

  Parameter = strtok_r(NULL, ";", &i);
  configuration1.showCameraView = atoi(Parameter);

  Parameter = strtok_r(NULL, ";", &i);
  configuration1.mirrorCam = atoi(Parameter);

  Parameter = strtok_r(NULL, ";", &i);
  configuration1.soundEffects = atoi(Parameter);
  
// Integers  
  Parameter = strtok_r(NULL, ";", &i);
  configuration1.camWidth = atoi(Parameter);

  Parameter = strtok_r(NULL, ";", &i);
  configuration1.camHeight = atoi(Parameter);

  Parameter = strtok_r(NULL, ";", &i);
  configuration1.nbDot = atoi(Parameter);

  Parameter = strtok_r(NULL, ";", &i);
  configuration1.antSens = atoi(Parameter);
  
  Parameter = strtok_r(NULL, ";", &i);
  configuration1.minBlobArea = atoi(Parameter);

  Parameter = strtok_r(NULL, ";", &i);
  configuration1.tolerance = atoi(Parameter);

  Parameter = strtok_r(NULL, ";", &i);
  configuration1.effect = atoi(Parameter);
  
  Parameter = strtok_r(NULL, ";", &i);
  configuration1.trackColorTolerance = atoi(Parameter);
    
  Parameter = strtok_r(NULL, ";", &i);
  configuration1.trackColorRed = atoi(Parameter);
  
  Parameter = strtok_r(NULL, ";", &i);
  configuration1.trackColorGreen = atoi(Parameter);
  
  Parameter = strtok_r(NULL, ";", &i);
  configuration1.trackColorBlue = atoi(Parameter);
  
  Parameter = strtok_r(NULL, ";", &i);
  configuration1.safeColorMinSize = atoi(Parameter);
    
  Parameter = strtok_r(NULL, ";", &i);
  configuration1.safeColorTolerance = atoi(Parameter);
  
  Parameter = strtok_r(NULL, ";", &i);
  configuration1.safeColorRed = atoi(Parameter);
    
  Parameter = strtok_r(NULL, ";", &i);
  configuration1.safeColorGreen = atoi(Parameter);
  
  Parameter = strtok_r(NULL, ";", &i);
  configuration1.safeColorBlue = atoi(Parameter);
    
  Parameter = strtok_r(NULL, ";", &i);
  configuration1.idleTime = atoi(Parameter);
  
// floats
  Parameter = strtok_r(NULL, ";", &i);
  configuration1.propX = strtod(Parameter,NULL);
  
  Parameter = strtok_r(NULL, ";", &i);
  configuration1.propY = strtod(Parameter,NULL);
  
  Parameter = strtok_r(NULL, ";", &i);
  configuration1.xRatio = strtod(Parameter,NULL);
  
  Parameter = strtok_r(NULL, ";", &i);
  configuration1.yRatio = strtod(Parameter,NULL);
  
  Parameter = strtok_r(NULL, ";", &i);
  configuration1.xMin = strtod(Parameter,NULL);
  
  Parameter = strtok_r(NULL, ";", &i);
  configuration1.xMax = strtod(Parameter,NULL);
  
  Parameter = strtok_r(NULL, ";", &i);
  configuration1.yMin = strtod(Parameter,NULL);
  
  Parameter = strtok_r(NULL, ";", &i);
  configuration1.yMax = strtod(Parameter,NULL);
  
  delay(20);
 
  // Backup on EEPROM 92 bytes, Start on 0
  EEPROM_writeAnything(0, configuration1);  //take more than 92*3.3ms  

}

void restore(){
  
  EEPROM_readAnything(0, configuration1); // Relode Struct configuration from EEPROM
  
  delay(20);
    
  Serial.println('R');  // Tel Processing Arduino Ready for Sending Values
  
  //Booleans
  Serial.print(configuration1.controlMode);
  Serial.print(";");
      
  Serial.print(configuration1.safety);
  Serial.print(";");
      
  Serial.print(configuration1.firingMode);
  Serial.print(";");
      
  Serial.print(configuration1.scanWhenIdle);
  Serial.print(";");
  
  Serial.print(configuration1.trackingMotion);
  Serial.print(";");
      
  Serial.print(configuration1.trackingColor);
  Serial.print(";");
      
  Serial.print(configuration1.leadTarget);
  Serial.print(";");
  
  Serial.print(configuration1.safeColor);
  Serial.print(";");
      
  Serial.print(configuration1.showRestrictedZones);
  Serial.print(";");
  
  Serial.print(configuration1.showDifferentPixels);
  Serial.print(";");
  
  Serial.print(configuration1.showTargetBox);
  Serial.print(";");
  
  Serial.print(configuration1.showCameraView);
  Serial.print(";");
      
  Serial.print(configuration1.mirrorCam);
  Serial.print(";");
      
  Serial.print(configuration1.soundEffects);
  Serial.print(";");
     
  //Integers    
  Serial.print(configuration1.camWidth);
  Serial.print(";");
      
  Serial.print(configuration1.camHeight);
  Serial.print(";");
      
  Serial.print(configuration1.nbDot);
  Serial.print(";");
  
  Serial.print(configuration1.antSens);
  Serial.print(";");
      
  Serial.print(configuration1.minBlobArea);
  Serial.print(";");
      
  Serial.print(configuration1.tolerance);
  Serial.print(";");
  
  Serial.print(configuration1.effect);
  Serial.print(";");
  
  Serial.print(configuration1.trackColorTolerance);
  Serial.print(";");
      
  Serial.print(configuration1.trackColorRed);
  Serial.print(";");
      
  Serial.print(configuration1.trackColorGreen);
  Serial.print(";");
  
  Serial.print(configuration1.trackColorBlue);
  Serial.print(";");
  
  Serial.print(configuration1.safeColorMinSize);
  Serial.print(";");
  
  Serial.print(configuration1.safeColorTolerance);
  Serial.print(";");
      
  Serial.print(configuration1.safeColorRed);
  Serial.print(";");
      
  Serial.print(configuration1.safeColorGreen);
  Serial.print(";");
  
  Serial.print(configuration1.safeColorBlue);
  Serial.print(";");
      
  Serial.print(configuration1.idleTime);
  Serial.print(";");

  //Floats    
  Serial.print(configuration1.propX);
  Serial.print(";");
      
  Serial.print(configuration1.propY);
  Serial.print(";");
  
  Serial.print(configuration1.xRatio);
  Serial.print(";");
  
  Serial.print(configuration1.yRatio);
  Serial.print(";");
  
  Serial.print(configuration1.xMin);
  Serial.print(";");
  
  Serial.print(configuration1.xMax);
  Serial.print(";");
  
  Serial.print(configuration1.yMin);
  Serial.print(";");
  
  Serial.print(configuration1.yMax);
  Serial.print("!");
  
}

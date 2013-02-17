//  contributed by Hugo K.


boolean leadTarget = true;

int nbDot = 10;                      // nomber of dot for anticipation minimum 2
int antSens = 10;                   // sensitivity of anticipation
float propX = 0.67;                  // proportionality of anticipation 
float propY = 0.11;                    // 1 is Hight / more is Less

int[] oldPossibleX = new int[nbDot+1];  // 0 is actual position
int[] oldPossibleY = new int[nbDot+1];

int[] accX = new int[nbDot -1];  
int[] accY = new int[nbDot -1];

int[] travelX = new int[nbDot -1];  
int[] travelY = new int[nbDot -1];

float antX = 0;
float antY = 0;

int prevTargetX = targetX;
int prevTargetY = targetY;


void anticipation() {

  if (leadTarget) {

    if (oldPossibleX.length != nbDot+1) {
      oldPossibleX = expand(oldPossibleX, nbDot+1);
    }
    if (oldPossibleY.length != nbDot+1) {
      oldPossibleY = expand(oldPossibleY, nbDot+1);
    }
    if (accX.length != nbDot-1) {
      accX = expand(accX, nbDot-1);
    }
    if (accY.length != nbDot-1) {
      accY = expand(accY, nbDot-1);
    }
    if (travelX.length != nbDot-1) {
      travelX = expand(travelX, nbDot-1);
    }
    if (travelY.length != nbDot-1) {
      travelY = expand(travelY, nbDot-1);
    }

    oldPossibleX[0] = possibleX;
    oldPossibleY[0] = possibleY;

    // Acceleration between oldPossibleX and old possibleX-1  
    for (int i=0; i<=nbDot-2; i++) {
      if ( abs(oldPossibleX[i] - oldPossibleX[i+1]) < camWidth/antSens && abs(oldPossibleX[i+1] - oldPossibleX[i+2]) < camWidth/antSens) {
        accX[i] = (oldPossibleX[i] - oldPossibleX[i+1]) - (oldPossibleX[i+1] - oldPossibleX[i+2]);
      }
      if ( abs(oldPossibleY[i] - oldPossibleY[i+1]) < camHeight/antSens && abs(oldPossibleY[i+1] - oldPossibleY[i+2]) < camHeight/antSens) {
        accY[i] = (oldPossibleY[i] - oldPossibleY[i+1]) - (oldPossibleY[i+1] - oldPossibleY[i+2]);
      }
    }

    // Travel between oldPossibleX and old possibleX-1  
    for (int i=0; i<= nbDot-2; i++) {
      if ( abs(oldPossibleX[i] - oldPossibleX[i+1]) < camWidth/antSens ) {
        travelX[i] = oldPossibleX[i] - oldPossibleX[i+1];
      }
      else {
        travelX[i] = 0;
      }
      if ( abs(oldPossibleY[i] - oldPossibleY[i+1]) < camHeight/antSens ) {
        travelY[i] = oldPossibleY[i] - oldPossibleY[i+1];
      }
      else {
        travelY[i] = 0;
      }
    }

    // addition of speed and acceleration
    // Each term can be weighted to have an improved algorithm

    antX = 0;
    antY = 0;

    for (int i=0; i<=nbDot-2; i++) {
      antX = antX + travelX[i] + accX[i];
      antY = antY + travelY[i] + accY[i];
    }

    antX = antX * propX;
    antY = antY * propY;

    // Updating  positions

    for (int i = nbDot; i>=1; i--) {
      oldPossibleX[i] = oldPossibleX[i-1];
      oldPossibleY[i] = oldPossibleY[i-1];
    }

    possibleX = int(possibleX + antX);
    possibleY = int(possibleY + antY);
  }
}

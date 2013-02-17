public void retryArduinoConnect() {
  connecting = true;
  if (!runWithoutArduino) {
//    try{
//      arduinoPort.stop();
//    }catch(Exception e) {
//      delay(10);
//    }
    // Find Serial Port that the arduino is on
    // The arduino is sending out a 'T' every 100 millisecs. Contributed by Don K.
    long millisStart;
    int i = 0;
    int len = Serial.list().length;    //get number of ports available
    println(Serial.list());      //print list of ports to screen

    println("Serial Port Count = " + len);  //print count of ports to screen
    if (len == 0) {
      runWithoutArduino = true;
      println("no Arduino detected. Will run without Arduino. Cheers");
    }
    for (i = 0; i < len; i++) {
      println("Testing port " + Serial.list()[i]);
      arduinoPort = new Serial(this, Serial.list()[i], 4800);      // Open 1st port in list
      millisStart = millis();
      while ( (millis () - millisStart) < 2000) ;  //wait for USB port reset (Guessed at 3 secs)
      // can't use delay() call in setup()
      arduinoPort.clear();        // empty buffer(incase of trash)
      arduinoPort.bufferUntil('T');                   //buffer until there is a 'T'
      millisStart = millis();
      while ( (millis () - millisStart) < 100) ;  //collect some chars
      if (arduinoPort.available() > 0)      //if we have a character
      {
        char c = arduinoPort.readChar();  //get the character
        if (c == 'T')        //if we got a 'T'
        {
          break;        //leave for loop
        }
      }
      else 
        arduinoPort.stop();      //if no 'T', stop port
      if (i == len - 1) {
        runWithoutArduino = true;
        println("no Arduino detected. Will run without Arduino. Cheers");
      }
    }
    if (!runWithoutArduino) {
      println("Serial Port used = " + Serial.list()[i]);
      serPortUsed = Serial.list()[i];
      millisStart = millis();
      while ( (millis () - millisStart) < 5000) ;
    }
  }
  connecting = false;
}

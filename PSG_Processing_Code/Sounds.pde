Minim minim;

AudioSnippet s1;
AudioSnippet s2;
AudioSnippet s3;
AudioSnippet s4;
AudioSnippet s5;
AudioSnippet s6;
AudioSnippet s7;
AudioSnippet s8;
AudioSnippet s9;
AudioSnippet s10;
AudioSnippet s11;
AudioSnippet s12;
AudioSnippet s13;
AudioSnippet s14;
AudioSnippet s15;
AudioSnippet s16;
AudioSnippet s17;
AudioSnippet s18;
AudioSnippet s19;
AudioSnippet s20;
AudioSnippet s21;

int soundTimer = 0;
int soundInterval = 1000;


void loadSounds() {
  s1 = minim.loadSnippet("data/your business is appreciated.wav");
  s2 = minim.loadSnippet("data/who's there.wav");
  s3 = minim.loadSnippet("data/there you are.wav");
  s4 = minim.loadSnippet("data/there you are(2).wav");
  s5 = minim.loadSnippet("data/target lost.wav");
  s6 = minim.loadSnippet("data/target aquired.wav");
  s7 = minim.loadSnippet("data/sleep mode activated.wav");
  s8 = minim.loadSnippet("data/sentry mode activated.wav");
  s9 = minim.loadSnippet("data/no hard feelings.wav");
  s10 = minim.loadSnippet("data/is anyone there.wav");
  s11 = minim.loadSnippet("data/i see you.wav");
  s12 = minim.loadSnippet("data/i dont hate you.wav");
  s13 = minim.loadSnippet("data/i dont blame you.wav");
  s14 = minim.loadSnippet("data/hey its me.wav");
  s15 = minim.loadSnippet("data/hello.wav");
  s16 = minim.loadSnippet("data/gotcha.wav");
  s17 = minim.loadSnippet("data/dispensing product.wav");
  s18 = minim.loadSnippet("data/deploying.wav");
  s19 = minim.loadSnippet("data/could you come over here.wav");
  s20 = minim.loadSnippet("data/are you still there.wav");
  s21 = minim.loadSnippet("data/activated.wav");
}

void playSound(int sound) {
  if(soundEffects) {
    if(sound == 1) {
      s1.rewind();
      s1.play();
    }
    if(sound == 2) {
      s2.rewind();
      s2.play();
    }
    if(sound == 3) {
      s3.rewind();
      s3.play();
    }
    if(sound == 4) {
      s4.rewind();
      s4.play();
    }
    if(sound == 5) {
      s5.rewind();
      s5.play();
    }
    if(sound == 6) {
      s6.rewind();
      s6.play();
    }
    if(sound == 7) {
      s7.rewind();
      s7.play();
    }
    if(sound == 8) {
      s8.rewind();
      s8.play();
    }
    if(sound == 9) {
      s9.rewind();
      s9.play();
    }
    if(sound == 10) {
      s10.rewind();
      s10.play();
    }
    if(sound == 11) {
      s11.rewind();
      s11.play();
    }
    if(sound == 12) {
      s12.rewind();
      s12.play();
    }
    if(sound == 13) {
      s13.rewind();
      s13.play();
    }
    if(sound == 14) {
      s14.rewind();
      s14.play();
    }
    if(sound == 15) {
      s15.rewind();
      s15.play();
    }
    if(sound == 16) {
      s16.rewind();
      s16.play();
    }
    if(sound == 17) {
      s17.rewind();
      s17.play();
    }
    if(sound == 18) {
      s18.rewind();
      s18.play();
    }
    if(sound == 19) {
      s19.rewind();
      s19.play();
    }
    if(sound == 20) {
      s20.rewind();
      s20.play();
    }
    if(sound == 21) {
      s21.rewind();
      s21.play();
    }
  }
}

void randomIdleSound() {
  if(soundEffects) {
    int sound = int(random(1, 11));
    if(sound == 1) {
      s2.rewind();
      s2.play();
    }
    if(sound == 2) {
      s7.rewind();
      s7.play();
    }
    if(sound == 3) {
      s9.rewind();
      s9.play();
    }
    if(sound == 4) {
      s10.rewind();
      s10.play();
    }
    if(sound == 5) {
      s11.rewind();
      s11.play();
    }
    if(sound == 6) {
      s12.rewind();
      s12.play();
    }
    if(sound == 7) {
      s13.rewind();
      s13.play();
    }
    if(sound == 8) {
      s14.rewind();
      s14.play();
    }
    if(sound == 9) {
      s19.rewind();
      s19.play();
    }
    if(sound == 10) {
      s20.rewind();
      s20.play();
    }
  }
}

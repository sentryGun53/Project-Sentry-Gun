/**
 * Copyright (C) 2003 Jeremy Booth (jeremy@newdawnsoftware.com)
 *
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this 
 * list of conditions and the following disclaimer. Redistributions in binary 
 * form must reproduce the above copyright notice, this list of conditions and 
 * the following disclaimer in the documentation and/or other materials provided
 * with the distribution. 
 * The name of the author may not be used to endorse or promote products derived
 * from this software without specific prior written permission. 
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF 
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO 
 * EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, 
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; 
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, 
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR 
 * OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF 
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE
 */
package net.java.games.input;

/**
 * Mapping utility class between native type ints and string names or
 * Key.Identifiers
 * @author Jeremy Booth (jeremy@newdawnsoftware.com)
 */
class LinuxNativeTypesMap {
    private static LinuxNativeTypesMap INSTANCE = new LinuxNativeTypesMap();
    
    private final Component.Identifier relAxesIDs[];
    private final Component.Identifier absAxesIDs[];
    private final Component.Identifier buttonIDs[];
    
    /** create an empty, uninitialsed map
     */    
    private LinuxNativeTypesMap() {
        buttonIDs = new Component.Identifier[NativeDefinitions.KEY_MAX];
        relAxesIDs = new Component.Identifier[NativeDefinitions.REL_MAX];
        absAxesIDs = new Component.Identifier[NativeDefinitions.ABS_MAX];
		reInit();
    }
    
    /** Do the work.
     */    
    private void reInit() {
        buttonIDs[NativeDefinitions.KEY_ESC] = Component.Identifier.Key.ESCAPE;
        buttonIDs[NativeDefinitions.KEY_1] = Component.Identifier.Key._1;
        buttonIDs[NativeDefinitions.KEY_2] = Component.Identifier.Key._2;
        buttonIDs[NativeDefinitions.KEY_3] = Component.Identifier.Key._3;
        buttonIDs[NativeDefinitions.KEY_4] = Component.Identifier.Key._4;
        buttonIDs[NativeDefinitions.KEY_5] = Component.Identifier.Key._5;
        buttonIDs[NativeDefinitions.KEY_6] = Component.Identifier.Key._6;
        buttonIDs[NativeDefinitions.KEY_7] = Component.Identifier.Key._7;
        buttonIDs[NativeDefinitions.KEY_8] = Component.Identifier.Key._8;
        buttonIDs[NativeDefinitions.KEY_9] = Component.Identifier.Key._9;
        buttonIDs[NativeDefinitions.KEY_0] = Component.Identifier.Key._0;
        buttonIDs[NativeDefinitions.KEY_MINUS] = Component.Identifier.Key.MINUS;
        buttonIDs[NativeDefinitions.KEY_EQUAL] = Component.Identifier.Key.EQUALS;
        buttonIDs[NativeDefinitions.KEY_BACKSPACE] = Component.Identifier.Key.BACK;
        buttonIDs[NativeDefinitions.KEY_TAB] = Component.Identifier.Key.TAB;
        buttonIDs[NativeDefinitions.KEY_Q] = Component.Identifier.Key.Q;
        buttonIDs[NativeDefinitions.KEY_W] = Component.Identifier.Key.W;
        buttonIDs[NativeDefinitions.KEY_E] = Component.Identifier.Key.E;
        buttonIDs[NativeDefinitions.KEY_R] = Component.Identifier.Key.R;
        buttonIDs[NativeDefinitions.KEY_T] = Component.Identifier.Key.T;
        buttonIDs[NativeDefinitions.KEY_Y] = Component.Identifier.Key.Y;
        buttonIDs[NativeDefinitions.KEY_U] = Component.Identifier.Key.U;
        buttonIDs[NativeDefinitions.KEY_I] = Component.Identifier.Key.I;
        buttonIDs[NativeDefinitions.KEY_O] = Component.Identifier.Key.O;
        buttonIDs[NativeDefinitions.KEY_P] = Component.Identifier.Key.P;
        buttonIDs[NativeDefinitions.KEY_LEFTBRACE] = Component.Identifier.Key.LBRACKET;
        buttonIDs[NativeDefinitions.KEY_RIGHTBRACE] = Component.Identifier.Key.RBRACKET;
        buttonIDs[NativeDefinitions.KEY_ENTER] = Component.Identifier.Key.RETURN;
        buttonIDs[NativeDefinitions.KEY_LEFTCTRL] = Component.Identifier.Key.LCONTROL;
        buttonIDs[NativeDefinitions.KEY_A] = Component.Identifier.Key.A;
        buttonIDs[NativeDefinitions.KEY_S] = Component.Identifier.Key.S;
        buttonIDs[NativeDefinitions.KEY_D] = Component.Identifier.Key.D;
        buttonIDs[NativeDefinitions.KEY_F] = Component.Identifier.Key.F;
        buttonIDs[NativeDefinitions.KEY_G] = Component.Identifier.Key.G;
        buttonIDs[NativeDefinitions.KEY_H] = Component.Identifier.Key.H;
        buttonIDs[NativeDefinitions.KEY_J] = Component.Identifier.Key.J;
        buttonIDs[NativeDefinitions.KEY_K] = Component.Identifier.Key.K;
        buttonIDs[NativeDefinitions.KEY_L] = Component.Identifier.Key.L;
        buttonIDs[NativeDefinitions.KEY_SEMICOLON] = Component.Identifier.Key.SEMICOLON;
        buttonIDs[NativeDefinitions.KEY_APOSTROPHE] = Component.Identifier.Key.APOSTROPHE;
        buttonIDs[NativeDefinitions.KEY_GRAVE] = Component.Identifier.Key.GRAVE;
        buttonIDs[NativeDefinitions.KEY_LEFTSHIFT] = Component.Identifier.Key.LSHIFT;
        buttonIDs[NativeDefinitions.KEY_BACKSLASH] = Component.Identifier.Key.BACKSLASH;
        buttonIDs[NativeDefinitions.KEY_Z] = Component.Identifier.Key.Z;
        buttonIDs[NativeDefinitions.KEY_X] = Component.Identifier.Key.X;
        buttonIDs[NativeDefinitions.KEY_C] = Component.Identifier.Key.C;
        buttonIDs[NativeDefinitions.KEY_V] = Component.Identifier.Key.V;
        buttonIDs[NativeDefinitions.KEY_B] = Component.Identifier.Key.B;
        buttonIDs[NativeDefinitions.KEY_N] = Component.Identifier.Key.N;
        buttonIDs[NativeDefinitions.KEY_M] = Component.Identifier.Key.M;
        buttonIDs[NativeDefinitions.KEY_COMMA] = Component.Identifier.Key.COMMA;
        buttonIDs[NativeDefinitions.KEY_DOT] = Component.Identifier.Key.PERIOD;
        buttonIDs[NativeDefinitions.KEY_SLASH] = Component.Identifier.Key.SLASH;
        buttonIDs[NativeDefinitions.KEY_RIGHTSHIFT] = Component.Identifier.Key.RSHIFT;
        buttonIDs[NativeDefinitions.KEY_KPASTERISK] = Component.Identifier.Key.MULTIPLY;
        buttonIDs[NativeDefinitions.KEY_LEFTALT] = Component.Identifier.Key.LALT;
        buttonIDs[NativeDefinitions.KEY_SPACE] = Component.Identifier.Key.SPACE;
        buttonIDs[NativeDefinitions.KEY_CAPSLOCK] = Component.Identifier.Key.CAPITAL;
        buttonIDs[NativeDefinitions.KEY_F1] = Component.Identifier.Key.F1;
        buttonIDs[NativeDefinitions.KEY_F2] = Component.Identifier.Key.F2;
        buttonIDs[NativeDefinitions.KEY_F3] = Component.Identifier.Key.F3;
        buttonIDs[NativeDefinitions.KEY_F4] = Component.Identifier.Key.F4;
        buttonIDs[NativeDefinitions.KEY_F5] = Component.Identifier.Key.F5;
        buttonIDs[NativeDefinitions.KEY_F6] = Component.Identifier.Key.F6;
        buttonIDs[NativeDefinitions.KEY_F7] = Component.Identifier.Key.F7;
        buttonIDs[NativeDefinitions.KEY_F8] = Component.Identifier.Key.F8;
        buttonIDs[NativeDefinitions.KEY_F9] = Component.Identifier.Key.F9;
        buttonIDs[NativeDefinitions.KEY_F10] = Component.Identifier.Key.F10;
        buttonIDs[NativeDefinitions.KEY_NUMLOCK] = Component.Identifier.Key.NUMLOCK;
        buttonIDs[NativeDefinitions.KEY_SCROLLLOCK] = Component.Identifier.Key.SCROLL;
        buttonIDs[NativeDefinitions.KEY_KP7] = Component.Identifier.Key.NUMPAD7;
        buttonIDs[NativeDefinitions.KEY_KP8] = Component.Identifier.Key.NUMPAD8;
        buttonIDs[NativeDefinitions.KEY_KP9] = Component.Identifier.Key.NUMPAD9;
        buttonIDs[NativeDefinitions.KEY_KPMINUS] = Component.Identifier.Key.SUBTRACT;
        buttonIDs[NativeDefinitions.KEY_KP4] = Component.Identifier.Key.NUMPAD4;
        buttonIDs[NativeDefinitions.KEY_KP5] = Component.Identifier.Key.NUMPAD5;
        buttonIDs[NativeDefinitions.KEY_KP6] = Component.Identifier.Key.NUMPAD6;
        buttonIDs[NativeDefinitions.KEY_KPPLUS] = Component.Identifier.Key.ADD;
        buttonIDs[NativeDefinitions.KEY_KP1] = Component.Identifier.Key.NUMPAD1;
        buttonIDs[NativeDefinitions.KEY_KP2] = Component.Identifier.Key.NUMPAD2;
        buttonIDs[NativeDefinitions.KEY_KP3] = Component.Identifier.Key.NUMPAD3;
        buttonIDs[NativeDefinitions.KEY_KP0] = Component.Identifier.Key.NUMPAD0;
        buttonIDs[NativeDefinitions.KEY_KPDOT] = Component.Identifier.Key.DECIMAL;
//        buttonIDs[NativeDefinitions.KEY_103RD] = null;
        buttonIDs[NativeDefinitions.KEY_F13] = Component.Identifier.Key.F13;
        buttonIDs[NativeDefinitions.KEY_102ND] = null;
        buttonIDs[NativeDefinitions.KEY_F11] = Component.Identifier.Key.F11;
        buttonIDs[NativeDefinitions.KEY_F12] = Component.Identifier.Key.F12;
        buttonIDs[NativeDefinitions.KEY_F14] = Component.Identifier.Key.F14;
        buttonIDs[NativeDefinitions.KEY_F15] = Component.Identifier.Key.F15;
        buttonIDs[NativeDefinitions.KEY_F16] = null;
        buttonIDs[NativeDefinitions.KEY_F17] = null;
        buttonIDs[NativeDefinitions.KEY_F18] = null;
        buttonIDs[NativeDefinitions.KEY_F19] = null;
        buttonIDs[NativeDefinitions.KEY_F20] = null;
        buttonIDs[NativeDefinitions.KEY_KPENTER] = Component.Identifier.Key.NUMPADENTER;
        buttonIDs[NativeDefinitions.KEY_RIGHTCTRL] = Component.Identifier.Key.RCONTROL;
        buttonIDs[NativeDefinitions.KEY_KPSLASH] = Component.Identifier.Key.DIVIDE;
        buttonIDs[NativeDefinitions.KEY_SYSRQ] = Component.Identifier.Key.SYSRQ;
        buttonIDs[NativeDefinitions.KEY_RIGHTALT] = Component.Identifier.Key.RALT;
        buttonIDs[NativeDefinitions.KEY_LINEFEED] = null;
        buttonIDs[NativeDefinitions.KEY_HOME] = Component.Identifier.Key.HOME;
        buttonIDs[NativeDefinitions.KEY_UP] = Component.Identifier.Key.UP;
        buttonIDs[NativeDefinitions.KEY_PAGEUP] = Component.Identifier.Key.PAGEUP;
        buttonIDs[NativeDefinitions.KEY_LEFT] = Component.Identifier.Key.LEFT;
        buttonIDs[NativeDefinitions.KEY_RIGHT] = Component.Identifier.Key.RIGHT;
        buttonIDs[NativeDefinitions.KEY_END] = Component.Identifier.Key.END;
        buttonIDs[NativeDefinitions.KEY_DOWN] = Component.Identifier.Key.DOWN;
        buttonIDs[NativeDefinitions.KEY_PAGEDOWN] = Component.Identifier.Key.PAGEDOWN;
        buttonIDs[NativeDefinitions.KEY_INSERT] = Component.Identifier.Key.INSERT;
        buttonIDs[NativeDefinitions.KEY_DELETE] = Component.Identifier.Key.DELETE;
        buttonIDs[NativeDefinitions.KEY_PAUSE] = Component.Identifier.Key.PAUSE;
/*        buttonIDs[NativeDefinitions.KEY_MACRO] = "Macro";
        buttonIDs[NativeDefinitions.KEY_MUTE] = "Mute";
        buttonIDs[NativeDefinitions.KEY_VOLUMEDOWN] = "Volume Down";
        buttonIDs[NativeDefinitions.KEY_VOLUMEUP] = "Volume Up";
        buttonIDs[NativeDefinitions.KEY_POWER] = "Power";*/
        buttonIDs[NativeDefinitions.KEY_KPEQUAL] = Component.Identifier.Key.NUMPADEQUAL;
        //buttonIDs[NativeDefinitions.KEY_KPPLUSMINUS] = "KeyPad +/-";
/*        buttonIDs[NativeDefinitions.KEY_F21] = "F21";
        buttonIDs[NativeDefinitions.KEY_F22] = "F22";
        buttonIDs[NativeDefinitions.KEY_F23] = "F23";
        buttonIDs[NativeDefinitions.KEY_F24] = "F24";
        buttonIDs[NativeDefinitions.KEY_KPCOMMA] = "KeyPad comma";
        buttonIDs[NativeDefinitions.KEY_LEFTMETA] = "LH Meta";
        buttonIDs[NativeDefinitions.KEY_RIGHTMETA] = "RH Meta";
        buttonIDs[NativeDefinitions.KEY_COMPOSE] = "Compose";
        buttonIDs[NativeDefinitions.KEY_STOP] = "Stop";
        buttonIDs[NativeDefinitions.KEY_AGAIN] = "Again";
        buttonIDs[NativeDefinitions.KEY_PROPS] = "Properties";
        buttonIDs[NativeDefinitions.KEY_UNDO] = "Undo";
        buttonIDs[NativeDefinitions.KEY_FRONT] = "Front";
        buttonIDs[NativeDefinitions.KEY_COPY] = "Copy";
        buttonIDs[NativeDefinitions.KEY_OPEN] = "Open";
        buttonIDs[NativeDefinitions.KEY_PASTE] = "Paste";
        buttonIDs[NativeDefinitions.KEY_FIND] = "Find";
        buttonIDs[NativeDefinitions.KEY_CUT] = "Cut";
        buttonIDs[NativeDefinitions.KEY_HELP] = "Help";
        buttonIDs[NativeDefinitions.KEY_MENU] = "Menu";
        buttonIDs[NativeDefinitions.KEY_CALC] = "Calculator";
        buttonIDs[NativeDefinitions.KEY_SETUP] = "Setup";*/
        buttonIDs[NativeDefinitions.KEY_SLEEP] = Component.Identifier.Key.SLEEP;
        /*buttonIDs[NativeDefinitions.KEY_WAKEUP] = "Wakeup";
        buttonIDs[NativeDefinitions.KEY_FILE] = "File";
        buttonIDs[NativeDefinitions.KEY_SENDFILE] = "Send File";
        buttonIDs[NativeDefinitions.KEY_DELETEFILE] = "Delete File";
        buttonIDs[NativeDefinitions.KEY_XFER] = "Transfer";
        buttonIDs[NativeDefinitions.KEY_PROG1] = "Program 1";
        buttonIDs[NativeDefinitions.KEY_PROG2] = "Program 2";
        buttonIDs[NativeDefinitions.KEY_WWW] = "Web Browser";
        buttonIDs[NativeDefinitions.KEY_MSDOS] = "DOS mode";
        buttonIDs[NativeDefinitions.KEY_COFFEE] = "Coffee";
        buttonIDs[NativeDefinitions.KEY_DIRECTION] = "Direction";
        buttonIDs[NativeDefinitions.KEY_CYCLEWINDOWS] = "Window cycle";
        buttonIDs[NativeDefinitions.KEY_MAIL] = "Mail";
        buttonIDs[NativeDefinitions.KEY_BOOKMARKS] = "Book Marks";
        buttonIDs[NativeDefinitions.KEY_COMPUTER] = "Computer";
        buttonIDs[NativeDefinitions.KEY_BACK] = "Back";
        buttonIDs[NativeDefinitions.KEY_FORWARD] = "Forward";
        buttonIDs[NativeDefinitions.KEY_CLOSECD] = "Close CD";
        buttonIDs[NativeDefinitions.KEY_EJECTCD] = "Eject CD";
        buttonIDs[NativeDefinitions.KEY_EJECTCLOSECD] = "Eject / Close CD";
        buttonIDs[NativeDefinitions.KEY_NEXTSONG] = "Next Song";
        buttonIDs[NativeDefinitions.KEY_PLAYPAUSE] = "Play and Pause";
        buttonIDs[NativeDefinitions.KEY_PREVIOUSSONG] = "Previous Song";
        buttonIDs[NativeDefinitions.KEY_STOPCD] = "Stop CD";
        buttonIDs[NativeDefinitions.KEY_RECORD] = "Record";
        buttonIDs[NativeDefinitions.KEY_REWIND] = "Rewind";
        buttonIDs[NativeDefinitions.KEY_PHONE] = "Phone";
        buttonIDs[NativeDefinitions.KEY_ISO] = "ISO";
        buttonIDs[NativeDefinitions.KEY_CONFIG] = "Config";
        buttonIDs[NativeDefinitions.KEY_HOMEPAGE] = "Home";
        buttonIDs[NativeDefinitions.KEY_REFRESH] = "Refresh";
        buttonIDs[NativeDefinitions.KEY_EXIT] = "Exit";
        buttonIDs[NativeDefinitions.KEY_MOVE] = "Move";
        buttonIDs[NativeDefinitions.KEY_EDIT] = "Edit";
        buttonIDs[NativeDefinitions.KEY_SCROLLUP] = "Scroll Up";
        buttonIDs[NativeDefinitions.KEY_SCROLLDOWN] = "Scroll Down";
        buttonIDs[NativeDefinitions.KEY_KPLEFTPAREN] = "KeyPad LH parenthesis";
        buttonIDs[NativeDefinitions.KEY_KPRIGHTPAREN] = "KeyPad RH parenthesis";
        buttonIDs[NativeDefinitions.KEY_INTL1] = "Intl 1";
        buttonIDs[NativeDefinitions.KEY_INTL2] = "Intl 2";
        buttonIDs[NativeDefinitions.KEY_INTL3] = "Intl 3";
        buttonIDs[NativeDefinitions.KEY_INTL4] = "Intl 4";
        buttonIDs[NativeDefinitions.KEY_INTL5] = "Intl 5";
        buttonIDs[NativeDefinitions.KEY_INTL6] = "Intl 6";
        buttonIDs[NativeDefinitions.KEY_INTL7] = "Intl 7";
        buttonIDs[NativeDefinitions.KEY_INTL8] = "Intl 8";
        buttonIDs[NativeDefinitions.KEY_INTL9] = "Intl 9";
        buttonIDs[NativeDefinitions.KEY_LANG1] = "Language 1";
        buttonIDs[NativeDefinitions.KEY_LANG2] = "Language 2";
        buttonIDs[NativeDefinitions.KEY_LANG3] = "Language 3";
        buttonIDs[NativeDefinitions.KEY_LANG4] = "Language 4";
        buttonIDs[NativeDefinitions.KEY_LANG5] = "Language 5";
        buttonIDs[NativeDefinitions.KEY_LANG6] = "Language 6";
        buttonIDs[NativeDefinitions.KEY_LANG7] = "Language 7";
        buttonIDs[NativeDefinitions.KEY_LANG8] = "Language 8";
        buttonIDs[NativeDefinitions.KEY_LANG9] = "Language 9";
        buttonIDs[NativeDefinitions.KEY_PLAYCD] = "Play CD";
        buttonIDs[NativeDefinitions.KEY_PAUSECD] = "Pause CD";
        buttonIDs[NativeDefinitions.KEY_PROG3] = "Program 3";
        buttonIDs[NativeDefinitions.KEY_PROG4] = "Program 4";
        buttonIDs[NativeDefinitions.KEY_SUSPEND] = "Suspend";
        buttonIDs[NativeDefinitions.KEY_CLOSE] = "Close";*/
        buttonIDs[NativeDefinitions.KEY_UNKNOWN] = Component.Identifier.Key.UNLABELED;
        /*buttonIDs[NativeDefinitions.KEY_BRIGHTNESSDOWN] = "Brightness Down";
        buttonIDs[NativeDefinitions.KEY_BRIGHTNESSUP] = "Brightness Up";*/
        
        //Misc keys
        buttonIDs[NativeDefinitions.BTN_0] = Component.Identifier.Button._0;
        buttonIDs[NativeDefinitions.BTN_1] = Component.Identifier.Button._1;
        buttonIDs[NativeDefinitions.BTN_2] = Component.Identifier.Button._2;
        buttonIDs[NativeDefinitions.BTN_3] = Component.Identifier.Button._3;
        buttonIDs[NativeDefinitions.BTN_4] = Component.Identifier.Button._4;
        buttonIDs[NativeDefinitions.BTN_5] = Component.Identifier.Button._5;
        buttonIDs[NativeDefinitions.BTN_6] = Component.Identifier.Button._6;
        buttonIDs[NativeDefinitions.BTN_7] = Component.Identifier.Button._7;
        buttonIDs[NativeDefinitions.BTN_8] = Component.Identifier.Button._8;
        buttonIDs[NativeDefinitions.BTN_9] = Component.Identifier.Button._9;
        
        // Mouse
        buttonIDs[NativeDefinitions.BTN_LEFT] = Component.Identifier.Button.LEFT;
        buttonIDs[NativeDefinitions.BTN_RIGHT] = Component.Identifier.Button.RIGHT;
        buttonIDs[NativeDefinitions.BTN_MIDDLE] = Component.Identifier.Button.MIDDLE;
        buttonIDs[NativeDefinitions.BTN_SIDE] = Component.Identifier.Button.SIDE;
        buttonIDs[NativeDefinitions.BTN_EXTRA] = Component.Identifier.Button.EXTRA;
        buttonIDs[NativeDefinitions.BTN_FORWARD] = Component.Identifier.Button.FORWARD;
        buttonIDs[NativeDefinitions.BTN_BACK] = Component.Identifier.Button.BACK;
        
        // Joystick
        buttonIDs[NativeDefinitions.BTN_TRIGGER] = Component.Identifier.Button.TRIGGER;
        buttonIDs[NativeDefinitions.BTN_THUMB] = Component.Identifier.Button.THUMB;
        buttonIDs[NativeDefinitions.BTN_THUMB2] = Component.Identifier.Button.THUMB2;
        buttonIDs[NativeDefinitions.BTN_TOP] = Component.Identifier.Button.TOP;
        buttonIDs[NativeDefinitions.BTN_TOP2] = Component.Identifier.Button.TOP2;
        buttonIDs[NativeDefinitions.BTN_PINKIE] = Component.Identifier.Button.PINKIE;
        buttonIDs[NativeDefinitions.BTN_BASE] = Component.Identifier.Button.BASE;
        buttonIDs[NativeDefinitions.BTN_BASE2] = Component.Identifier.Button.BASE2;
        buttonIDs[NativeDefinitions.BTN_BASE3] = Component.Identifier.Button.BASE3;
        buttonIDs[NativeDefinitions.BTN_BASE4] = Component.Identifier.Button.BASE4;
        buttonIDs[NativeDefinitions.BTN_BASE5] = Component.Identifier.Button.BASE5;
        buttonIDs[NativeDefinitions.BTN_BASE6] = Component.Identifier.Button.BASE6;
        buttonIDs[NativeDefinitions.BTN_DEAD] = Component.Identifier.Button.DEAD;
        
        // Gamepad
        buttonIDs[NativeDefinitions.BTN_A] = Component.Identifier.Button.A;
        buttonIDs[NativeDefinitions.BTN_B] = Component.Identifier.Button.B;
        buttonIDs[NativeDefinitions.BTN_C] = Component.Identifier.Button.C;
        buttonIDs[NativeDefinitions.BTN_X] = Component.Identifier.Button.X;
        buttonIDs[NativeDefinitions.BTN_Y] = Component.Identifier.Button.Y;
        buttonIDs[NativeDefinitions.BTN_Z] = Component.Identifier.Button.Z;
        buttonIDs[NativeDefinitions.BTN_TL] = Component.Identifier.Button.LEFT_THUMB;
        buttonIDs[NativeDefinitions.BTN_TR] = Component.Identifier.Button.RIGHT_THUMB;
        buttonIDs[NativeDefinitions.BTN_TL2] = Component.Identifier.Button.LEFT_THUMB2;
        buttonIDs[NativeDefinitions.BTN_TR2] = Component.Identifier.Button.RIGHT_THUMB2;
        buttonIDs[NativeDefinitions.BTN_SELECT] = Component.Identifier.Button.SELECT;
        buttonIDs[NativeDefinitions.BTN_MODE] = Component.Identifier.Button.MODE;
        buttonIDs[NativeDefinitions.BTN_THUMBL] = Component.Identifier.Button.LEFT_THUMB3;
        buttonIDs[NativeDefinitions.BTN_THUMBR] = Component.Identifier.Button.RIGHT_THUMB3;
        
        // Digitiser
        buttonIDs[NativeDefinitions.BTN_TOOL_PEN] = Component.Identifier.Button.TOOL_PEN;
        buttonIDs[NativeDefinitions.BTN_TOOL_RUBBER] = Component.Identifier.Button.TOOL_RUBBER;
        buttonIDs[NativeDefinitions.BTN_TOOL_BRUSH] = Component.Identifier.Button.TOOL_BRUSH;
        buttonIDs[NativeDefinitions.BTN_TOOL_PENCIL] = Component.Identifier.Button.TOOL_PENCIL;
        buttonIDs[NativeDefinitions.BTN_TOOL_AIRBRUSH] = Component.Identifier.Button.TOOL_AIRBRUSH;
        buttonIDs[NativeDefinitions.BTN_TOOL_FINGER] = Component.Identifier.Button.TOOL_FINGER;
        buttonIDs[NativeDefinitions.BTN_TOOL_MOUSE] = Component.Identifier.Button.TOOL_MOUSE;
        buttonIDs[NativeDefinitions.BTN_TOOL_LENS] = Component.Identifier.Button.TOOL_LENS;
        buttonIDs[NativeDefinitions.BTN_TOUCH] = Component.Identifier.Button.TOUCH;
        buttonIDs[NativeDefinitions.BTN_STYLUS] = Component.Identifier.Button.STYLUS;
        buttonIDs[NativeDefinitions.BTN_STYLUS2] = Component.Identifier.Button.STYLUS2;
        
        relAxesIDs[NativeDefinitions.REL_X] = Component.Identifier.Axis.X;
        relAxesIDs[NativeDefinitions.REL_Y] = Component.Identifier.Axis.Y;
        relAxesIDs[NativeDefinitions.REL_Z] = Component.Identifier.Axis.Z;
        relAxesIDs[NativeDefinitions.REL_WHEEL] = Component.Identifier.Axis.Z;
        // There are guesses as I have no idea what they would be used for
        relAxesIDs[NativeDefinitions.REL_HWHEEL] = Component.Identifier.Axis.SLIDER;
        relAxesIDs[NativeDefinitions.REL_DIAL] = Component.Identifier.Axis.SLIDER;
        relAxesIDs[NativeDefinitions.REL_MISC] = Component.Identifier.Axis.SLIDER;
        
        absAxesIDs[NativeDefinitions.ABS_X] = Component.Identifier.Axis.X;
        absAxesIDs[NativeDefinitions.ABS_Y] = Component.Identifier.Axis.Y;
        absAxesIDs[NativeDefinitions.ABS_Z] = Component.Identifier.Axis.Z;
        absAxesIDs[NativeDefinitions.ABS_RX] = Component.Identifier.Axis.RX;
        absAxesIDs[NativeDefinitions.ABS_RY] = Component.Identifier.Axis.RY;
        absAxesIDs[NativeDefinitions.ABS_RZ] = Component.Identifier.Axis.RZ;
        absAxesIDs[NativeDefinitions.ABS_THROTTLE] = Component.Identifier.Axis.SLIDER;
        absAxesIDs[NativeDefinitions.ABS_RUDDER] = Component.Identifier.Axis.RZ;
        absAxesIDs[NativeDefinitions.ABS_WHEEL] = Component.Identifier.Axis.Y;
        absAxesIDs[NativeDefinitions.ABS_GAS] = Component.Identifier.Axis.SLIDER;
        absAxesIDs[NativeDefinitions.ABS_BRAKE] = Component.Identifier.Axis.SLIDER;
        // Hats are done this way as they are mapped from two axis down to one
        absAxesIDs[NativeDefinitions.ABS_HAT0X] = Component.Identifier.Axis.POV;
        absAxesIDs[NativeDefinitions.ABS_HAT0Y] = Component.Identifier.Axis.POV;
        absAxesIDs[NativeDefinitions.ABS_HAT1X] = Component.Identifier.Axis.POV;
        absAxesIDs[NativeDefinitions.ABS_HAT1Y] = Component.Identifier.Axis.POV;
        absAxesIDs[NativeDefinitions.ABS_HAT2X] = Component.Identifier.Axis.POV;
        absAxesIDs[NativeDefinitions.ABS_HAT2Y] = Component.Identifier.Axis.POV;
        absAxesIDs[NativeDefinitions.ABS_HAT3X] = Component.Identifier.Axis.POV;
        absAxesIDs[NativeDefinitions.ABS_HAT3Y] = Component.Identifier.Axis.POV;
        // erm, yeah
        absAxesIDs[NativeDefinitions.ABS_PRESSURE] = null;
        absAxesIDs[NativeDefinitions.ABS_DISTANCE] = null;
        absAxesIDs[NativeDefinitions.ABS_TILT_X] = null;
        absAxesIDs[NativeDefinitions.ABS_TILT_Y] = null;
        absAxesIDs[NativeDefinitions.ABS_MISC] = null;
    }

	public final static Controller.Type guessButtonTrait(int button_code) {
		switch (button_code) {
			case NativeDefinitions.BTN_TRIGGER : 
			case NativeDefinitions.BTN_THUMB : 
			case NativeDefinitions.BTN_THUMB2 : 
			case NativeDefinitions.BTN_TOP : 
			case NativeDefinitions.BTN_TOP2 : 
			case NativeDefinitions.BTN_PINKIE : 
			case NativeDefinitions.BTN_BASE : 
			case NativeDefinitions.BTN_BASE2 : 
			case NativeDefinitions.BTN_BASE3 : 
			case NativeDefinitions.BTN_BASE4 : 
			case NativeDefinitions.BTN_BASE5 : 
			case NativeDefinitions.BTN_BASE6 : 
			case NativeDefinitions.BTN_DEAD : 
				return Controller.Type.STICK;
			case NativeDefinitions.BTN_A : 
			case NativeDefinitions.BTN_B : 
			case NativeDefinitions.BTN_C : 
			case NativeDefinitions.BTN_X : 
			case NativeDefinitions.BTN_Y : 
			case NativeDefinitions.BTN_Z : 
			case NativeDefinitions.BTN_TL : 
			case NativeDefinitions.BTN_TR : 
			case NativeDefinitions.BTN_TL2 : 
			case NativeDefinitions.BTN_TR2 : 
			case NativeDefinitions.BTN_SELECT : 
			case NativeDefinitions.BTN_MODE : 
			case NativeDefinitions.BTN_THUMBL : 
			case NativeDefinitions.BTN_THUMBR :
				return Controller.Type.GAMEPAD;
			case NativeDefinitions.BTN_0 : 
			case NativeDefinitions.BTN_1 : 
			case NativeDefinitions.BTN_2 : 
			case NativeDefinitions.BTN_3 : 
			case NativeDefinitions.BTN_4 : 
			case NativeDefinitions.BTN_5 : 
			case NativeDefinitions.BTN_6 : 
			case NativeDefinitions.BTN_7 : 
			case NativeDefinitions.BTN_8 : 
			case NativeDefinitions.BTN_9 : 
				return Controller.Type.UNKNOWN;
			case NativeDefinitions.BTN_LEFT : 
			case NativeDefinitions.BTN_RIGHT : 
			case NativeDefinitions.BTN_MIDDLE : 
			case NativeDefinitions.BTN_SIDE : 
			case NativeDefinitions.BTN_EXTRA : 
				return Controller.Type.MOUSE;
				//				case NativeDefinitions.KEY_RESERVED:
			case NativeDefinitions.KEY_ESC:
			case NativeDefinitions.KEY_1:
			case NativeDefinitions.KEY_2:
			case NativeDefinitions.KEY_3:
			case NativeDefinitions.KEY_4:
			case NativeDefinitions.KEY_5:
			case NativeDefinitions.KEY_6:
			case NativeDefinitions.KEY_7:
			case NativeDefinitions.KEY_8:
			case NativeDefinitions.KEY_9:
			case NativeDefinitions.KEY_0:
			case NativeDefinitions.KEY_MINUS:
			case NativeDefinitions.KEY_EQUAL:
			case NativeDefinitions.KEY_BACKSPACE:
			case NativeDefinitions.KEY_TAB:
			case NativeDefinitions.KEY_Q:
			case NativeDefinitions.KEY_W:
			case NativeDefinitions.KEY_E:
			case NativeDefinitions.KEY_R:
			case NativeDefinitions.KEY_T:
			case NativeDefinitions.KEY_Y:
			case NativeDefinitions.KEY_U:
			case NativeDefinitions.KEY_I:
			case NativeDefinitions.KEY_O:
			case NativeDefinitions.KEY_P:
			case NativeDefinitions.KEY_LEFTBRACE:
			case NativeDefinitions.KEY_RIGHTBRACE:
			case NativeDefinitions.KEY_ENTER:
			case NativeDefinitions.KEY_LEFTCTRL:
			case NativeDefinitions.KEY_A:
			case NativeDefinitions.KEY_S:
			case NativeDefinitions.KEY_D:
			case NativeDefinitions.KEY_F:
			case NativeDefinitions.KEY_G:
			case NativeDefinitions.KEY_H:
			case NativeDefinitions.KEY_J:
			case NativeDefinitions.KEY_K:
			case NativeDefinitions.KEY_L:
			case NativeDefinitions.KEY_SEMICOLON:
			case NativeDefinitions.KEY_APOSTROPHE:
			case NativeDefinitions.KEY_GRAVE:
			case NativeDefinitions.KEY_LEFTSHIFT:
			case NativeDefinitions.KEY_BACKSLASH:
			case NativeDefinitions.KEY_Z:
			case NativeDefinitions.KEY_X:
			case NativeDefinitions.KEY_C:
			case NativeDefinitions.KEY_V:
			case NativeDefinitions.KEY_B:
			case NativeDefinitions.KEY_N:
			case NativeDefinitions.KEY_M:
			case NativeDefinitions.KEY_COMMA:
			case NativeDefinitions.KEY_DOT:
			case NativeDefinitions.KEY_SLASH:
			case NativeDefinitions.KEY_RIGHTSHIFT:
			case NativeDefinitions.KEY_KPASTERISK:
			case NativeDefinitions.KEY_LEFTALT:
			case NativeDefinitions.KEY_SPACE:
			case NativeDefinitions.KEY_CAPSLOCK:
			case NativeDefinitions.KEY_F1:
			case NativeDefinitions.KEY_F2:
			case NativeDefinitions.KEY_F3:
			case NativeDefinitions.KEY_F4:
			case NativeDefinitions.KEY_F5:
			case NativeDefinitions.KEY_F6:
			case NativeDefinitions.KEY_F7:
			case NativeDefinitions.KEY_F8:
			case NativeDefinitions.KEY_F9:
			case NativeDefinitions.KEY_F10:
			case NativeDefinitions.KEY_NUMLOCK:
			case NativeDefinitions.KEY_SCROLLLOCK:
			case NativeDefinitions.KEY_KP7:
			case NativeDefinitions.KEY_KP8:
			case NativeDefinitions.KEY_KP9:
			case NativeDefinitions.KEY_KPMINUS:
			case NativeDefinitions.KEY_KP4:
			case NativeDefinitions.KEY_KP5:
			case NativeDefinitions.KEY_KP6:
			case NativeDefinitions.KEY_KPPLUS:
			case NativeDefinitions.KEY_KP1:
			case NativeDefinitions.KEY_KP2:
			case NativeDefinitions.KEY_KP3:
			case NativeDefinitions.KEY_KP0:
			case NativeDefinitions.KEY_KPDOT:
			case NativeDefinitions.KEY_ZENKAKUHANKAKU:
			case NativeDefinitions.KEY_102ND:
			case NativeDefinitions.KEY_F11:
			case NativeDefinitions.KEY_F12:
			case NativeDefinitions.KEY_RO:
			case NativeDefinitions.KEY_KATAKANA:
			case NativeDefinitions.KEY_HIRAGANA:
			case NativeDefinitions.KEY_HENKAN:
			case NativeDefinitions.KEY_KATAKANAHIRAGANA:
			case NativeDefinitions.KEY_MUHENKAN:
			case NativeDefinitions.KEY_KPJPCOMMA:
			case NativeDefinitions.KEY_KPENTER:
			case NativeDefinitions.KEY_RIGHTCTRL:
			case NativeDefinitions.KEY_KPSLASH:
			case NativeDefinitions.KEY_SYSRQ:
			case NativeDefinitions.KEY_RIGHTALT:
			case NativeDefinitions.KEY_LINEFEED:
			case NativeDefinitions.KEY_HOME:
			case NativeDefinitions.KEY_UP:
			case NativeDefinitions.KEY_PAGEUP:
			case NativeDefinitions.KEY_LEFT:
			case NativeDefinitions.KEY_RIGHT:
			case NativeDefinitions.KEY_END:
			case NativeDefinitions.KEY_DOWN:
			case NativeDefinitions.KEY_PAGEDOWN:
			case NativeDefinitions.KEY_INSERT:
			case NativeDefinitions.KEY_DELETE:
			case NativeDefinitions.KEY_MACRO:
			case NativeDefinitions.KEY_MUTE:
			case NativeDefinitions.KEY_VOLUMEDOWN:
			case NativeDefinitions.KEY_VOLUMEUP:
			case NativeDefinitions.KEY_POWER:
			case NativeDefinitions.KEY_KPEQUAL:
			case NativeDefinitions.KEY_KPPLUSMINUS:
			case NativeDefinitions.KEY_PAUSE:
			case NativeDefinitions.KEY_KPCOMMA:
			case NativeDefinitions.KEY_HANGUEL:
			case NativeDefinitions.KEY_HANJA:
			case NativeDefinitions.KEY_YEN:
			case NativeDefinitions.KEY_LEFTMETA:
			case NativeDefinitions.KEY_RIGHTMETA:
			case NativeDefinitions.KEY_COMPOSE:
			case NativeDefinitions.KEY_STOP:
			case NativeDefinitions.KEY_AGAIN:
			case NativeDefinitions.KEY_PROPS:
			case NativeDefinitions.KEY_UNDO:
			case NativeDefinitions.KEY_FRONT:
			case NativeDefinitions.KEY_COPY:
			case NativeDefinitions.KEY_OPEN:
			case NativeDefinitions.KEY_PASTE:
			case NativeDefinitions.KEY_FIND:
			case NativeDefinitions.KEY_CUT:
			case NativeDefinitions.KEY_HELP:
			case NativeDefinitions.KEY_MENU:
			case NativeDefinitions.KEY_CALC:
			case NativeDefinitions.KEY_SETUP:
			case NativeDefinitions.KEY_SLEEP:
			case NativeDefinitions.KEY_WAKEUP:
			case NativeDefinitions.KEY_FILE:
			case NativeDefinitions.KEY_SENDFILE:
			case NativeDefinitions.KEY_DELETEFILE:
			case NativeDefinitions.KEY_XFER:
			case NativeDefinitions.KEY_PROG1:
			case NativeDefinitions.KEY_PROG2:
			case NativeDefinitions.KEY_WWW:
			case NativeDefinitions.KEY_MSDOS:
			case NativeDefinitions.KEY_COFFEE:
			case NativeDefinitions.KEY_DIRECTION:
			case NativeDefinitions.KEY_CYCLEWINDOWS:
			case NativeDefinitions.KEY_MAIL:
			case NativeDefinitions.KEY_BOOKMARKS:
			case NativeDefinitions.KEY_COMPUTER:
			case NativeDefinitions.KEY_BACK:
			case NativeDefinitions.KEY_FORWARD:
			case NativeDefinitions.KEY_CLOSECD:
			case NativeDefinitions.KEY_EJECTCD:
			case NativeDefinitions.KEY_EJECTCLOSECD:
			case NativeDefinitions.KEY_NEXTSONG:
			case NativeDefinitions.KEY_PLAYPAUSE:
			case NativeDefinitions.KEY_PREVIOUSSONG:
			case NativeDefinitions.KEY_STOPCD:
			case NativeDefinitions.KEY_RECORD:
			case NativeDefinitions.KEY_REWIND:
			case NativeDefinitions.KEY_PHONE:
			case NativeDefinitions.KEY_ISO:
			case NativeDefinitions.KEY_CONFIG:
			case NativeDefinitions.KEY_HOMEPAGE:
			case NativeDefinitions.KEY_REFRESH:
			case NativeDefinitions.KEY_EXIT:
			case NativeDefinitions.KEY_MOVE:
			case NativeDefinitions.KEY_EDIT:
			case NativeDefinitions.KEY_SCROLLUP:
			case NativeDefinitions.KEY_SCROLLDOWN:
			case NativeDefinitions.KEY_KPLEFTPAREN:
			case NativeDefinitions.KEY_KPRIGHTPAREN:
			case NativeDefinitions.KEY_F13:
			case NativeDefinitions.KEY_F14:
			case NativeDefinitions.KEY_F15:
			case NativeDefinitions.KEY_F16:
			case NativeDefinitions.KEY_F17:
			case NativeDefinitions.KEY_F18:
			case NativeDefinitions.KEY_F19:
			case NativeDefinitions.KEY_F20:
			case NativeDefinitions.KEY_F21:
			case NativeDefinitions.KEY_F22:
			case NativeDefinitions.KEY_F23:
			case NativeDefinitions.KEY_F24:
			case NativeDefinitions.KEY_PLAYCD:
			case NativeDefinitions.KEY_PAUSECD:
			case NativeDefinitions.KEY_PROG3:
			case NativeDefinitions.KEY_PROG4:
			case NativeDefinitions.KEY_SUSPEND:
			case NativeDefinitions.KEY_CLOSE:
			case NativeDefinitions.KEY_PLAY:
			case NativeDefinitions.KEY_FASTFORWARD:
			case NativeDefinitions.KEY_BASSBOOST:
			case NativeDefinitions.KEY_PRINT:
			case NativeDefinitions.KEY_HP:
			case NativeDefinitions.KEY_CAMERA:
			case NativeDefinitions.KEY_SOUND:
			case NativeDefinitions.KEY_QUESTION:
			case NativeDefinitions.KEY_EMAIL:
			case NativeDefinitions.KEY_CHAT:
			case NativeDefinitions.KEY_SEARCH:
			case NativeDefinitions.KEY_CONNECT:
			case NativeDefinitions.KEY_FINANCE:
			case NativeDefinitions.KEY_SPORT:
			case NativeDefinitions.KEY_SHOP:
			case NativeDefinitions.KEY_ALTERASE:
			case NativeDefinitions.KEY_CANCEL:
			case NativeDefinitions.KEY_BRIGHTNESSDOWN:
			case NativeDefinitions.KEY_BRIGHTNESSUP:
			case NativeDefinitions.KEY_MEDIA:
			case NativeDefinitions.KEY_SWITCHVIDEOMODE:
			case NativeDefinitions.KEY_KBDILLUMTOGGLE:
			case NativeDefinitions.KEY_KBDILLUMDOWN:
			case NativeDefinitions.KEY_KBDILLUMUP:
				//    			case NativeDefinitions.KEY_UNKNOWN:
			case NativeDefinitions.KEY_OK:
			case NativeDefinitions.KEY_SELECT:
			case NativeDefinitions.KEY_GOTO:
			case NativeDefinitions.KEY_CLEAR:
			case NativeDefinitions.KEY_POWER2:
			case NativeDefinitions.KEY_OPTION:
			case NativeDefinitions.KEY_INFO:
			case NativeDefinitions.KEY_TIME:
			case NativeDefinitions.KEY_VENDOR:
			case NativeDefinitions.KEY_ARCHIVE:
			case NativeDefinitions.KEY_PROGRAM:
			case NativeDefinitions.KEY_CHANNEL:
			case NativeDefinitions.KEY_FAVORITES:
			case NativeDefinitions.KEY_EPG:
			case NativeDefinitions.KEY_PVR:
			case NativeDefinitions.KEY_MHP:
			case NativeDefinitions.KEY_LANGUAGE:
			case NativeDefinitions.KEY_TITLE:
			case NativeDefinitions.KEY_SUBTITLE:
			case NativeDefinitions.KEY_ANGLE:
			case NativeDefinitions.KEY_ZOOM:
			case NativeDefinitions.KEY_MODE:
			case NativeDefinitions.KEY_KEYBOARD:
			case NativeDefinitions.KEY_SCREEN:
			case NativeDefinitions.KEY_PC:
			case NativeDefinitions.KEY_TV:
			case NativeDefinitions.KEY_TV2:
			case NativeDefinitions.KEY_VCR:
			case NativeDefinitions.KEY_VCR2:
			case NativeDefinitions.KEY_SAT:
			case NativeDefinitions.KEY_SAT2:
			case NativeDefinitions.KEY_CD:
			case NativeDefinitions.KEY_TAPE:
			case NativeDefinitions.KEY_RADIO:
			case NativeDefinitions.KEY_TUNER:
			case NativeDefinitions.KEY_PLAYER:
			case NativeDefinitions.KEY_TEXT:
			case NativeDefinitions.KEY_DVD:
			case NativeDefinitions.KEY_AUX:
			case NativeDefinitions.KEY_MP3:
			case NativeDefinitions.KEY_AUDIO:
			case NativeDefinitions.KEY_VIDEO:
			case NativeDefinitions.KEY_DIRECTORY:
			case NativeDefinitions.KEY_LIST:
			case NativeDefinitions.KEY_MEMO:
			case NativeDefinitions.KEY_CALENDAR:
			case NativeDefinitions.KEY_RED:
			case NativeDefinitions.KEY_GREEN:
			case NativeDefinitions.KEY_YELLOW:
			case NativeDefinitions.KEY_BLUE:
			case NativeDefinitions.KEY_CHANNELUP:
			case NativeDefinitions.KEY_CHANNELDOWN:
			case NativeDefinitions.KEY_FIRST:
			case NativeDefinitions.KEY_LAST:
			case NativeDefinitions.KEY_AB:
			case NativeDefinitions.KEY_NEXT:
			case NativeDefinitions.KEY_RESTART:
			case NativeDefinitions.KEY_SLOW:
			case NativeDefinitions.KEY_SHUFFLE:
			case NativeDefinitions.KEY_BREAK:
			case NativeDefinitions.KEY_PREVIOUS:
			case NativeDefinitions.KEY_DIGITS:
			case NativeDefinitions.KEY_TEEN:
			case NativeDefinitions.KEY_TWEN:
			case NativeDefinitions.KEY_DEL_EOL:
			case NativeDefinitions.KEY_DEL_EOS:
			case NativeDefinitions.KEY_INS_LINE:
			case NativeDefinitions.KEY_DEL_LINE:
			case NativeDefinitions.KEY_FN:
			case NativeDefinitions.KEY_FN_ESC:
			case NativeDefinitions.KEY_FN_F1:
			case NativeDefinitions.KEY_FN_F2:
			case NativeDefinitions.KEY_FN_F3:
			case NativeDefinitions.KEY_FN_F4:
			case NativeDefinitions.KEY_FN_F5:
			case NativeDefinitions.KEY_FN_F6:
			case NativeDefinitions.KEY_FN_F7:
			case NativeDefinitions.KEY_FN_F8:
			case NativeDefinitions.KEY_FN_F9:
			case NativeDefinitions.KEY_FN_F10:
			case NativeDefinitions.KEY_FN_F11:
			case NativeDefinitions.KEY_FN_F12:
			case NativeDefinitions.KEY_FN_1:
			case NativeDefinitions.KEY_FN_2:
			case NativeDefinitions.KEY_FN_D:
			case NativeDefinitions.KEY_FN_E:
			case NativeDefinitions.KEY_FN_F:
			case NativeDefinitions.KEY_FN_S:
			case NativeDefinitions.KEY_FN_B:
				return Controller.Type.KEYBOARD;
			default:
				return Controller.Type.UNKNOWN;
		}
	}
			
    /** Return port type from a native port type int id
     * @param nativeid The native port type
     * @return The jinput port type
     */    
    public static Controller.PortType getPortType(int nativeid) {
        // Have to do this one this way as there is no BUS_MAX
        switch (nativeid) {
            case NativeDefinitions.BUS_GAMEPORT :
                return Controller.PortType.GAME;
            case NativeDefinitions.BUS_I8042 :
                return Controller.PortType.I8042;
            case NativeDefinitions.BUS_PARPORT :
                return Controller.PortType.PARALLEL;
            case NativeDefinitions.BUS_RS232 :
                return Controller.PortType.SERIAL;
            case NativeDefinitions.BUS_USB :
                return Controller.PortType.USB;
            default:
                return Controller.PortType.UNKNOWN;
        }
    }
    
    /** Gets the identifier for a relative axis
     * @param nativeID The axis type ID
     * @return The jinput id
     */    
    public static Component.Identifier getRelAxisID(int nativeID) {
        Component.Identifier retval = INSTANCE.relAxesIDs[nativeID];
        if(retval == null) {
            retval = Component.Identifier.Axis.SLIDER_VELOCITY;
            INSTANCE.relAxesIDs[nativeID] = retval;
        }
        return retval;
    }
    
    /** Gets the identifier for a absolute axis
     * @param nativeID The native axis type id
     * @return The jinput id
     */    
    public static Component.Identifier getAbsAxisID(int nativeID) {
		Component.Identifier retval = null;
		try {
			retval = INSTANCE.absAxesIDs[nativeID];
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("INSTANCE.absAxesIDs is only " + INSTANCE.absAxesIDs.length + " long, so " + nativeID + " not contained");
			//ignore, pretend it was null
		}
        if(retval == null) {
            retval = Component.Identifier.Axis.SLIDER;
            INSTANCE.absAxesIDs[nativeID] = retval;
        }
        return retval;
    }
    
    /** Gets the identifier for a button
     * @param nativeID The native button type id
     * @return The jinput id
     */    
    public static Component.Identifier getButtonID(int nativeID) {
        Component.Identifier retval = INSTANCE.buttonIDs[nativeID];
        if(retval == null) {
            retval = Component.Identifier.Key.UNKNOWN;
            INSTANCE.buttonIDs[nativeID] = retval;
        }
        return retval;
    }
}

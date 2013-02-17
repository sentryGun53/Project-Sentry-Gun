/*
 * %W% %E%
 *
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
/*****************************************************************************
 * Copyright (c) 2003 Sun Microsystems, Inc.  All Rights Reserved.
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * - Redistribution of source code must retain the above copyright notice,
 *   this list of conditions and the following disclaimer.
 *
 * - Redistribution in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materails provided with the distribution.
 *
 * Neither the name Sun Microsystems, Inc. or the names of the contributors
 * may be used to endorse or promote products derived from this software
 * without specific prior written permission.
 *
 * This software is provided "AS IS," without a warranty of any kind.
 * ALL EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES, INCLUDING
 * ANY IMPLIED WARRANT OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR
 * NON-INFRINGEMEN, ARE HEREBY EXCLUDED.  SUN MICROSYSTEMS, INC. ("SUN") AND
 * ITS LICENSORS SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS
 * A RESULT OF USING, MODIFYING OR DESTRIBUTING THIS SOFTWARE OR ITS 
 * DERIVATIVES.  IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE FOR ANY LOST
 * REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT, SPECIAL, CONSEQUENTIAL,
 * INCIDENTAL OR PUNITIVE DAMAGES.  HOWEVER CAUSED AND REGARDLESS OF THE THEORY
 * OF LIABILITY, ARISING OUT OF THE USE OF OUR INABILITY TO USE THIS SOFTWARE,
 * EVEN IF SUN HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 *
 * You acknowledge that this software is not designed or intended for us in
 * the design, construction, operation or maintenance of any nuclear facility
 *
 *****************************************************************************/
package net.java.games.input;

/**
 * @author elias
 * @version 1.0
 */
final class DIIdentifierMap {
	public final static int DIK_ESCAPE		  = 0x01;
	public final static int DIK_1			   = 0x02;
	public final static int DIK_2			   = 0x03;
	public final static int DIK_3			   = 0x04;
	public final static int DIK_4			   = 0x05;
	public final static int DIK_5			   = 0x06;
	public final static int DIK_6			   = 0x07;
	public final static int DIK_7			   = 0x08;
	public final static int DIK_8			   = 0x09;
	public final static int DIK_9			   = 0x0A;
	public final static int DIK_0			   = 0x0B;
	public final static int DIK_MINUS		   = 0x0C;	/* - on main keyboard */
	public final static int DIK_EQUALS		  = 0x0D;
	public final static int DIK_BACK			= 0x0E;	/* backspace */
	public final static int DIK_TAB			 = 0x0F;
	public final static int DIK_Q			   = 0x10;
	public final static int DIK_W			   = 0x11;
	public final static int DIK_E			   = 0x12;
	public final static int DIK_R			   = 0x13;
	public final static int DIK_T			   = 0x14;
	public final static int DIK_Y			   = 0x15;
	public final static int DIK_U			   = 0x16;
	public final static int DIK_I			   = 0x17;
	public final static int DIK_O			   = 0x18;
	public final static int DIK_P			   = 0x19;
	public final static int DIK_LBRACKET		= 0x1A;
	public final static int DIK_RBRACKET		= 0x1B;
	public final static int DIK_RETURN		  = 0x1C;	/* Enter on main keyboard */
	public final static int DIK_LCONTROL		= 0x1D;
	public final static int DIK_A			   = 0x1E;
	public final static int DIK_S			   = 0x1F;
	public final static int DIK_D			   = 0x20;
	public final static int DIK_F			   = 0x21;
	public final static int DIK_G			   = 0x22;
	public final static int DIK_H			   = 0x23;
	public final static int DIK_J			   = 0x24;
	public final static int DIK_K			   = 0x25;
	public final static int DIK_L			   = 0x26;
	public final static int DIK_SEMICOLON	   = 0x27;
	public final static int DIK_APOSTROPHE	  = 0x28;
	public final static int DIK_GRAVE		   = 0x29;	/* accent grave */
	public final static int DIK_LSHIFT		  = 0x2A;
	public final static int DIK_BACKSLASH	   = 0x2B;
	public final static int DIK_Z			   = 0x2C;
	public final static int DIK_X			   = 0x2D;
	public final static int DIK_C			   = 0x2E;
	public final static int DIK_V			   = 0x2F;
	public final static int DIK_B			   = 0x30;
	public final static int DIK_N			   = 0x31;
	public final static int DIK_M			   = 0x32;
	public final static int DIK_COMMA		   = 0x33;
	public final static int DIK_PERIOD		  = 0x34;	/* . on main keyboard */
	public final static int DIK_SLASH		   = 0x35;	/* / on main keyboard */
	public final static int DIK_RSHIFT		  = 0x36;
	public final static int DIK_MULTIPLY		= 0x37;	/* * on numeric keypad */
	public final static int DIK_LMENU		   = 0x38;	/* left Alt */
	public final static int DIK_SPACE		   = 0x39;
	public final static int DIK_CAPITAL		 = 0x3A;
	public final static int DIK_F1			  = 0x3B;
	public final static int DIK_F2			  = 0x3C;
	public final static int DIK_F3			  = 0x3D;
	public final static int DIK_F4			  = 0x3E;
	public final static int DIK_F5			  = 0x3F;
	public final static int DIK_F6			  = 0x40;
	public final static int DIK_F7			  = 0x41;
	public final static int DIK_F8			  = 0x42;
	public final static int DIK_F9			  = 0x43;
	public final static int DIK_F10			 = 0x44;
	public final static int DIK_NUMLOCK		 = 0x45;
	public final static int DIK_SCROLL		  = 0x46;	/* Scroll Lock */
	public final static int DIK_NUMPAD7		 = 0x47;
	public final static int DIK_NUMPAD8		 = 0x48;
	public final static int DIK_NUMPAD9		 = 0x49;
	public final static int DIK_SUBTRACT		= 0x4A;	/* - on numeric keypad */
	public final static int DIK_NUMPAD4		 = 0x4B;
	public final static int DIK_NUMPAD5		 = 0x4C;
	public final static int DIK_NUMPAD6		 = 0x4D;
	public final static int DIK_ADD			 = 0x4E;	/* + on numeric keypad */
	public final static int DIK_NUMPAD1		 = 0x4F;
	public final static int DIK_NUMPAD2		 = 0x50;
	public final static int DIK_NUMPAD3		 = 0x51;
	public final static int DIK_NUMPAD0		 = 0x52;
	public final static int DIK_DECIMAL		 = 0x53;	/* . on numeric keypad */
	public final static int DIK_OEM_102		 = 0x56;	/* <> or \| on RT 102-key keyboard (Non-U.S.) */
	public final static int DIK_F11			 = 0x57;
	public final static int DIK_F12			 = 0x58;
	public final static int DIK_F13			 = 0x64;	/*					 (NEC PC98) */
	public final static int DIK_F14			 = 0x65;	/*					 (NEC PC98) */
	public final static int DIK_F15			 = 0x66;	/*					 (NEC PC98) */
	public final static int DIK_KANA			= 0x70;	/* (Japanese keyboard)			*/
	public final static int DIK_ABNT_C1		 = 0x73;	/* /? on Brazilian keyboard */
	public final static int DIK_CONVERT		 = 0x79;	/* (Japanese keyboard)			*/
	public final static int DIK_NOCONVERT	   = 0x7B;	/* (Japanese keyboard)			*/
	public final static int DIK_YEN			 = 0x7D;	/* (Japanese keyboard)			*/
	public final static int DIK_ABNT_C2		 = 0x7E;	/* Numpad . on Brazilian keyboard */
	public final static int DIK_NUMPADEQUALS	= 0x8D;	/* = on numeric keypad (NEC PC98) */
	public final static int DIK_PREVTRACK	   = 0x90;	/* Previous Track (DIK_CIRCUMFLEX on Japanese keyboard) */
	public final static int DIK_AT			  = 0x91;	/*					 (NEC PC98) */
	public final static int DIK_COLON		   = 0x92;	/*					 (NEC PC98) */
	public final static int DIK_UNDERLINE	   = 0x93;	/*					 (NEC PC98) */
	public final static int DIK_KANJI		   = 0x94;	/* (Japanese keyboard)			*/
	public final static int DIK_STOP			= 0x95;	/*					 (NEC PC98) */
	public final static int DIK_AX			  = 0x96;	/*					 (Japan AX) */
	public final static int DIK_UNLABELED	   = 0x97;	/*						(J3100) */
	public final static int DIK_NEXTTRACK	   = 0x99;	/* Next Track */
	public final static int DIK_NUMPADENTER	 = 0x9C;	/* Enter on numeric keypad */
	public final static int DIK_RCONTROL		= 0x9D;
	public final static int DIK_MUTE			= 0xA0;	/* Mute */
	public final static int DIK_CALCULATOR	  = 0xA1;	/* Calculator */
	public final static int DIK_PLAYPAUSE	   = 0xA2;	/* Play / Pause */
	public final static int DIK_MEDIASTOP	   = 0xA4;	/* Media Stop */
	public final static int DIK_VOLUMEDOWN	  = 0xAE;	/* Volume - */
	public final static int DIK_VOLUMEUP		= 0xB0;	/* Volume + */
	public final static int DIK_WEBHOME		 = 0xB2;	/* Web home */
	public final static int DIK_NUMPADCOMMA	 = 0xB3;	/* , on numeric keypad (NEC PC98) */
	public final static int DIK_DIVIDE		  = 0xB5;	/* / on numeric keypad */
	public final static int DIK_SYSRQ		   = 0xB7;
	public final static int DIK_RMENU		   = 0xB8;	/* right Alt */
	public final static int DIK_PAUSE		   = 0xC5;	/* Pause */
	public final static int DIK_HOME			= 0xC7;	/* Home on arrow keypad */
	public final static int DIK_UP			  = 0xC8;	/* UpArrow on arrow keypad */
	public final static int DIK_PRIOR		   = 0xC9;	/* PgUp on arrow keypad */
	public final static int DIK_LEFT			= 0xCB;	/* LeftArrow on arrow keypad */
	public final static int DIK_RIGHT		   = 0xCD;	/* RightArrow on arrow keypad */
	public final static int DIK_END			 = 0xCF;	/* End on arrow keypad */
	public final static int DIK_DOWN			= 0xD0;	/* DownArrow on arrow keypad */
	public final static int DIK_NEXT			= 0xD1;	/* PgDn on arrow keypad */
	public final static int DIK_INSERT		  = 0xD2;	/* Insert on arrow keypad */
	public final static int DIK_DELETE		  = 0xD3;	/* Delete on arrow keypad */
	public final static int DIK_LWIN			= 0xDB;	/* Left Windows key */
	public final static int DIK_RWIN			= 0xDC;	/* Right Windows key */
	public final static int DIK_APPS			= 0xDD;	/* AppMenu key */
	public final static int DIK_POWER		   = 0xDE;	/* System Power */
	public final static int DIK_SLEEP		   = 0xDF;	/* System Sleep */
	public final static int DIK_WAKE			= 0xE3;	/* System Wake */
	public final static int DIK_WEBSEARCH	   = 0xE5;	/* Web Search */
	public final static int DIK_WEBFAVORITES	= 0xE6;	/* Web Favorites */
	public final static int DIK_WEBREFRESH	  = 0xE7;	/* Web Refresh */
	public final static int DIK_WEBSTOP		 = 0xE8;	/* Web Stop */
	public final static int DIK_WEBFORWARD	  = 0xE9;	/* Web Forward */
	public final static int DIK_WEBBACK		 = 0xEA;	/* Web Back */
	public final static int DIK_MYCOMPUTER	  = 0xEB;	/* My Computer */
	public final static int DIK_MAIL			= 0xEC;	/* Mail */
	public final static int DIK_MEDIASELECT	 = 0xED;	/* Media Select */

	public final static Component.Identifier.Key getKeyIdentifier(int key_code) {
		switch (key_code) {
			case DIK_ESCAPE:
				return Component.Identifier.Key.ESCAPE;
			case DIK_1:
				return Component.Identifier.Key._1;
			case DIK_2:
				return Component.Identifier.Key._2;
			case DIK_3:
				return Component.Identifier.Key._3;
			case DIK_4:
				return Component.Identifier.Key._4;
			case DIK_5:
				return Component.Identifier.Key._5;
			case DIK_6:
				return Component.Identifier.Key._6;
			case DIK_7:
				return Component.Identifier.Key._7;
			case DIK_8:
				return Component.Identifier.Key._8;
			case DIK_9:
				return Component.Identifier.Key._9;
			case DIK_0:
				return Component.Identifier.Key._0;
			case DIK_MINUS:
				return Component.Identifier.Key.MINUS;
			case DIK_EQUALS:
				return Component.Identifier.Key.EQUALS;
			case DIK_BACK:
				return Component.Identifier.Key.BACK;
			case DIK_TAB:
				return Component.Identifier.Key.TAB;
			case DIK_Q:
				return Component.Identifier.Key.Q;
			case DIK_W:
				return Component.Identifier.Key.W;
			case DIK_E:
				return Component.Identifier.Key.E;
			case DIK_R:
				return Component.Identifier.Key.R;
			case DIK_T:
				return Component.Identifier.Key.T;
			case DIK_Y:
				return Component.Identifier.Key.Y;
			case DIK_U:
				return Component.Identifier.Key.U;
			case DIK_I:
				return Component.Identifier.Key.I;
			case DIK_O:
				return Component.Identifier.Key.O;
			case DIK_P:
				return Component.Identifier.Key.P;
			case DIK_LBRACKET:
				return Component.Identifier.Key.LBRACKET;
			case DIK_RBRACKET:
				return Component.Identifier.Key.RBRACKET;
			case DIK_RETURN:
				return Component.Identifier.Key.RETURN;
			case DIK_LCONTROL:
				return Component.Identifier.Key.LCONTROL;
			case DIK_A:
				return Component.Identifier.Key.A;
			case DIK_S:
				return Component.Identifier.Key.S;
			case DIK_D:
				return Component.Identifier.Key.D;
			case DIK_F:
				return Component.Identifier.Key.F;
			case DIK_G:
				return Component.Identifier.Key.G;
			case DIK_H:
				return Component.Identifier.Key.H;
			case DIK_J:
				return Component.Identifier.Key.J;
			case DIK_K:
				return Component.Identifier.Key.K;
			case DIK_L:
				return Component.Identifier.Key.L;
			case DIK_SEMICOLON:
				return Component.Identifier.Key.SEMICOLON;
			case DIK_APOSTROPHE:
				return Component.Identifier.Key.APOSTROPHE;
			case DIK_GRAVE:
				return Component.Identifier.Key.GRAVE;
			case DIK_LSHIFT:
				return Component.Identifier.Key.LSHIFT;
			case DIK_BACKSLASH:
				return Component.Identifier.Key.BACKSLASH;
			case DIK_Z:
				return Component.Identifier.Key.Z;
			case DIK_X:
				return Component.Identifier.Key.X;
			case DIK_C:
				return Component.Identifier.Key.C;
			case DIK_V:
				return Component.Identifier.Key.V;
			case DIK_B:
				return Component.Identifier.Key.B;
			case DIK_N:
				return Component.Identifier.Key.N;
			case DIK_M:
				return Component.Identifier.Key.M;
			case DIK_COMMA:
				return Component.Identifier.Key.COMMA;
			case DIK_PERIOD:
				return Component.Identifier.Key.PERIOD;
			case DIK_SLASH:
				return Component.Identifier.Key.SLASH;
			case DIK_RSHIFT:
				return Component.Identifier.Key.RSHIFT;
			case DIK_MULTIPLY:
				return Component.Identifier.Key.MULTIPLY;
			case DIK_LMENU:
				return Component.Identifier.Key.LALT;
			case DIK_SPACE:
				return Component.Identifier.Key.SPACE;
			case DIK_CAPITAL:
				return Component.Identifier.Key.CAPITAL;
			case DIK_F1:
				return Component.Identifier.Key.F1;
			case DIK_F2:
				return Component.Identifier.Key.F2;
			case DIK_F3:
				return Component.Identifier.Key.F3;
			case DIK_F4:
				return Component.Identifier.Key.F4;
			case DIK_F5:
				return Component.Identifier.Key.F5;
			case DIK_F6:
				return Component.Identifier.Key.F6;
			case DIK_F7:
				return Component.Identifier.Key.F7;
			case DIK_F8:
				return Component.Identifier.Key.F8;
			case DIK_F9:
				return Component.Identifier.Key.F9;
			case DIK_F10:
				return Component.Identifier.Key.F10;
			case DIK_NUMLOCK:
				return Component.Identifier.Key.NUMLOCK;
			case DIK_SCROLL:
				return Component.Identifier.Key.SCROLL;
			case DIK_NUMPAD7:
				return Component.Identifier.Key.NUMPAD7;
			case DIK_NUMPAD8:
				return Component.Identifier.Key.NUMPAD8;
			case DIK_NUMPAD9:
				return Component.Identifier.Key.NUMPAD9;
			case DIK_SUBTRACT:
				return Component.Identifier.Key.SUBTRACT;
			case DIK_NUMPAD4:
				return Component.Identifier.Key.NUMPAD4;
			case DIK_NUMPAD5:
				return Component.Identifier.Key.NUMPAD5;
			case DIK_NUMPAD6:
				return Component.Identifier.Key.NUMPAD6;
			case DIK_ADD:
				return Component.Identifier.Key.ADD;
			case DIK_NUMPAD1:
				return Component.Identifier.Key.NUMPAD1;
			case DIK_NUMPAD2:
				return Component.Identifier.Key.NUMPAD2;
			case DIK_NUMPAD3:
				return Component.Identifier.Key.NUMPAD3;
			case DIK_NUMPAD0:
				return Component.Identifier.Key.NUMPAD0;
			case DIK_DECIMAL:
				return Component.Identifier.Key.DECIMAL;
			case DIK_F11:
				return Component.Identifier.Key.F11;
			case DIK_F12:
				return Component.Identifier.Key.F12;
			case DIK_F13:
				return Component.Identifier.Key.F13;
			case DIK_F14:
				return Component.Identifier.Key.F14;
			case DIK_F15:
				return Component.Identifier.Key.F15;
			case DIK_KANA:
				return Component.Identifier.Key.KANA;
			case DIK_CONVERT:
				return Component.Identifier.Key.CONVERT;
			case DIK_NOCONVERT:
				return Component.Identifier.Key.NOCONVERT;
			case DIK_YEN:
				return Component.Identifier.Key.YEN;
			case DIK_NUMPADEQUALS:
				return Component.Identifier.Key.NUMPADEQUAL;
			case DIK_AT:
				return Component.Identifier.Key.AT;
			case DIK_COLON:
				return Component.Identifier.Key.COLON;
			case DIK_UNDERLINE:
				return Component.Identifier.Key.UNDERLINE;
			case DIK_KANJI:
				return Component.Identifier.Key.KANJI;
			case DIK_STOP:
				return Component.Identifier.Key.STOP;
			case DIK_AX:
				return Component.Identifier.Key.AX;
			case DIK_UNLABELED:
				return Component.Identifier.Key.UNLABELED;
			case DIK_NUMPADENTER:
				return Component.Identifier.Key.NUMPADENTER;
			case DIK_RCONTROL:
				return Component.Identifier.Key.RCONTROL;
			case DIK_NUMPADCOMMA:
				return Component.Identifier.Key.NUMPADCOMMA;
			case DIK_DIVIDE:
				return Component.Identifier.Key.DIVIDE;
			case DIK_SYSRQ:
				return Component.Identifier.Key.SYSRQ;
			case DIK_RMENU:
				return Component.Identifier.Key.RALT;
			case DIK_PAUSE:
				return Component.Identifier.Key.PAUSE;
			case DIK_HOME:
				return Component.Identifier.Key.HOME;
			case DIK_UP:
				return Component.Identifier.Key.UP;
			case DIK_PRIOR:
				return Component.Identifier.Key.PAGEUP;
			case DIK_LEFT:
				return Component.Identifier.Key.LEFT;
			case DIK_RIGHT:
				return Component.Identifier.Key.RIGHT;
			case DIK_END:
				return Component.Identifier.Key.END;
			case DIK_DOWN:
				return Component.Identifier.Key.DOWN;
			case DIK_NEXT:
				return Component.Identifier.Key.PAGEDOWN;
			case DIK_INSERT:
				return Component.Identifier.Key.INSERT;
			case DIK_DELETE:
				return Component.Identifier.Key.DELETE;
			case DIK_LWIN:
				return Component.Identifier.Key.LWIN;
			case DIK_RWIN:
				return Component.Identifier.Key.RWIN;
			case DIK_APPS:
				return Component.Identifier.Key.APPS;
			case DIK_POWER:
				return Component.Identifier.Key.POWER;
			case DIK_SLEEP:
				return Component.Identifier.Key.SLEEP;
			/* Unassigned keys */
			case DIK_ABNT_C1:
			case DIK_ABNT_C2:
			case DIK_PREVTRACK:
			case DIK_PLAYPAUSE:
			case DIK_NEXTTRACK:
			case DIK_MUTE:
			case DIK_CALCULATOR:
			case DIK_MEDIASTOP:
			case DIK_VOLUMEDOWN:
			case DIK_VOLUMEUP:
			case DIK_WEBHOME:
			case DIK_WAKE:
			case DIK_WEBSEARCH:
			case DIK_WEBFAVORITES:
			case DIK_WEBREFRESH:
			case DIK_WEBSTOP:
			case DIK_WEBFORWARD:
			case DIK_WEBBACK:
			case DIK_MYCOMPUTER:
			case DIK_MAIL:
			case DIK_MEDIASELECT:
			case DIK_OEM_102:
			default:
				return Component.Identifier.Key.UNKNOWN;
		}
	}

	public final static Component.Identifier.Button getButtonIdentifier(int id) {
		switch (id) {
			case 0:
				return Component.Identifier.Button._0;
			case 1:
				return Component.Identifier.Button._1;
			case 2:
				return Component.Identifier.Button._2;
			case 3:
				return Component.Identifier.Button._3;
			case 4:
				return Component.Identifier.Button._4;
			case 5:
				return Component.Identifier.Button._5;
			case 6:
				return Component.Identifier.Button._6;
			case 7:
				return Component.Identifier.Button._7;
			case 8:
				return Component.Identifier.Button._8;
			case 9:
				return Component.Identifier.Button._9;
			case 10:
				return Component.Identifier.Button._10;
			case 11:
				return Component.Identifier.Button._11;
			case 12:
				return Component.Identifier.Button._12;
			case 13:
				return Component.Identifier.Button._13;
			case 14:
				return Component.Identifier.Button._14;
			case 15:
				return Component.Identifier.Button._15;
			case 16:
				return Component.Identifier.Button._16;
			case 17:
				return Component.Identifier.Button._17;
			case 18:
				return Component.Identifier.Button._18;
			case 19:
				return Component.Identifier.Button._19;
			case 20:
				return Component.Identifier.Button._20;
			case 21:
				return Component.Identifier.Button._21;
			case 22:
				return Component.Identifier.Button._23;
			case 23:
				return Component.Identifier.Button._24;
			case 24:
				return Component.Identifier.Button._25;
			case 25:
				return Component.Identifier.Button._26;
			case 26:
				return Component.Identifier.Button._27;
			case 27:
				return Component.Identifier.Button._28;
			case 28:
				return Component.Identifier.Button._29;
			case 29:
				return Component.Identifier.Button._30;
			case 30:
				return Component.Identifier.Button._31;
			default:
				return null;
		}
	}

	public final static Component.Identifier.Button mapMouseButtonIdentifier(Component.Identifier.Button button_id) {
		if (button_id == Component.Identifier.Button._0) {
			return Component.Identifier.Button.LEFT;
		} else if (button_id == Component.Identifier.Button._1) {
			return Component.Identifier.Button.RIGHT;
		} else if (button_id == Component.Identifier.Button._2) {
			return Component.Identifier.Button.MIDDLE;
		} else
			return button_id;
	}
}

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

import java.util.Map;
import java.util.HashMap;

/** Button Usages
* @author elias
* @version 1.0
*/
final class ButtonUsage implements Usage {
	private final static Map map = new HashMap();
	
	private final int button_id;

	public final static ButtonUsage map(int button_id) {
		Integer button_id_obj = new Integer(button_id);
		ButtonUsage existing = (ButtonUsage)map.get(button_id_obj);
		if (existing != null)
			return existing;
		ButtonUsage new_button = new ButtonUsage(button_id);
		map.put(button_id_obj, new_button);
		return new_button;
	}
	
	private ButtonUsage(int button_id) {
		this.button_id = button_id;
	}

	public final Component.Identifier.Button getIdentifier() {
		switch (button_id) {
			case 1:
				return Component.Identifier.Button._0;
			case 2:
				return Component.Identifier.Button._1;
			case 3:
				return Component.Identifier.Button._2;
			case 4:
				return Component.Identifier.Button._3;
			case 5:
				return Component.Identifier.Button._4;
			case 6:
				return Component.Identifier.Button._5;
			case 7:
				return Component.Identifier.Button._6;
			case 8:
				return Component.Identifier.Button._7;
			case 9:
				return Component.Identifier.Button._8;
			case 10:
				return Component.Identifier.Button._9;
			case 11:
				return Component.Identifier.Button._10;
			case 12:
				return Component.Identifier.Button._11;
			case 13:
				return Component.Identifier.Button._12;
			case 14:
				return Component.Identifier.Button._13;
			case 15:
				return Component.Identifier.Button._14;
			case 16:
				return Component.Identifier.Button._15;
			case 17:
				return Component.Identifier.Button._16;
			case 18:
				return Component.Identifier.Button._17;
			case 19:
				return Component.Identifier.Button._18;
			case 20:
				return Component.Identifier.Button._19;
			case 21:
				return Component.Identifier.Button._20;
			case 22:
				return Component.Identifier.Button._21;
			case 23:
				return Component.Identifier.Button._22;
			case 24:
				return Component.Identifier.Button._23;
			case 25:
				return Component.Identifier.Button._24;
			case 26:
				return Component.Identifier.Button._25;
			case 27:
				return Component.Identifier.Button._26;
			case 28:
				return Component.Identifier.Button._27;
			case 29:
				return Component.Identifier.Button._28;
			case 30:
				return Component.Identifier.Button._29;
			case 31:
				return Component.Identifier.Button._30;
			case 32:
				return Component.Identifier.Button._31;
			default:
				return null;
		}
	}
	
	public final String toString() {
		return "ButtonUsage (" + button_id + ")";
	}
}

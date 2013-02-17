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

import java.util.List;
import java.util.ArrayList;

/**
 * A Mouse is a type of controller consisting of two child controllers,
 * a ball and a button pad.  This includes devices such as touch pads,
 * trackballs, and fingersticks.
 */
public abstract class Mouse extends AbstractController {
    protected Mouse(String name, Component[] components, Controller[] children, Rumbler[] rumblers) {
        super(name, components, children, rumblers);
    }

    /**
     * Returns the type of the Controller.
     */
    public Type getType() {
        return Type.MOUSE;
    }

	/**
	 * Returns the x-axis for the mouse ball, never null.
	 */
	public Component getX() {
		return getComponent(Component.Identifier.Axis.X);
	}

	/**
	 * Returns the y-axis for the mouse ball, never null.
	 */
	public Component getY() {
		return getComponent(Component.Identifier.Axis.Y);
	}

	/**
	 * Returns the mouse wheel, or null if no mouse wheel is present.
	 */
	public Component getWheel() {
		return getComponent(Component.Identifier.Axis.Z);
	}

	/**
	 * Returns the left or primary mouse button, never null.
	 */
	public Component getLeft() {
		return getComponent(Component.Identifier.Button.LEFT);
	}

	/**
	 * Returns the right or secondary mouse button, null if the mouse is
	 * a single-button mouse.
	 */
	public Component getRight() {
		return getComponent(Component.Identifier.Button.RIGHT);
	}

	/**
	 * Returns the middle or tertiary mouse button, null if the mouse has
	 * fewer than three buttons.
	 */
	public Component getMiddle() {
		return getComponent(Component.Identifier.Button.MIDDLE);
	}

	/**
	 * Returns the side or 4th mouse button, null if the mouse has
	 * fewer than 4 buttons.
	 */
	public Component getSide() {
		return getComponent(Component.Identifier.Button.SIDE);
	}

	/**
	 * Returns the extra or 5th mouse button, null if the mouse has
	 * fewer than 5 buttons.
	 */
	public Component getExtra() {
		return getComponent(Component.Identifier.Button.EXTRA);
	}

	/**
	 * Returns the forward mouse button, null if the mouse hasn't
	 * got one.
	 */
	public Component getForward() {
		return getComponent(Component.Identifier.Button.FORWARD);
	}

	/**
	 * Returns the back mouse button, null if the mouse hasn't
	 * got one.
	 */
	public Component getBack() {
		return getComponent(Component.Identifier.Button.BACK);
	}
} // class Mouse

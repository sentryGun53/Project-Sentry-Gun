/*
  Part of the GUI for Processing library 
  	http://www.lagers.org.uk/g4p/index.html
	http://gui4processing.googlecode.com/svn/trunk/

  Copyright (c) 2008-09 Peter Lager

  This library is free software; you can redistribute it and/or
  modify it under the terms of the GNU Lesser General Public
  License as published by the Free Software Foundation; either
  version 2.1 of the License, or (at your option) any later version.

  This library is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
  Lesser General Public License for more details.

  You should have received a copy of the GNU Lesser General
  Public License along with this library; if not, write to the
  Free Software Foundation, Inc., 59 Temple Place, Suite 330,
  Boston, MA  02111-1307  USA
 */

package guicomponents;

/**
 * A list of text alignment options.
 * 
 * @author Peter Lager
 *
 */
public interface GAlign {
	// Alignment
	public static final int LEFT	= 0x00000001;
	public static final int RIGHT	= 0x00000002;
	public static final int CENTER	= 0x00000004;
	public static final int H_ALIGN	= 0x00000007;

	public static final int TOP		= 0x00000010;
	public static final int BOTTOM	= 0x00000020;
	public static final int MIDDLE	= 0x00000040;
	public static final int V_ALIGN	= 0x00000070;
	
}

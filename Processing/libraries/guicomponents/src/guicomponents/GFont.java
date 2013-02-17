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

import java.util.HashMap;

import processing.core.PApplet;
import processing.core.PFont;

/**
 * This class only has static methods and are used to create and return
 * PFont objects for use by the GUI components.<br>
 * 
 * 
 * @author Peter Lager
 *
 */
public class GFont {

	// Keep track of all fonts made and prevent duplicates
	private static final HashMap<GFontKey, PFont> fontmap = new HashMap<GFontKey, PFont>();

	
	/**
	 * Create a font
	 * 
	 * @param theApplet
	 * @param fontname system name for font
	 * @param fsize size of font to create (8 - 144 incl)
	 * @return the matching PFont object
	 */
	public static PFont getFont(PApplet theApplet, String fontname, int fsize){
		fsize = PApplet.constrain(fsize, 8, 144);
		GFontKey fkey = new GFontKey(fontname, fsize);
		PFont pfont = null;
		// See if the font has already been created
		// if so return it else make it
		if(fontmap.containsKey(fkey))
			pfont = fontmap.get(fkey);
		else {
			// Attempt to make the this font
			pfont = theApplet.createFont(fontname, fsize, true);
			// if no such system font then make one using default sans-serif font of same size
			if(pfont != null){
				fontmap.put(fkey, pfont);	// remember it
			} else {
				// unable to make this font so make sans-serif font
				// at this size if not already done
				fkey = new GFontKey("SansSerif", fsize);
				if(fontmap.containsKey(fkey))
					pfont = fontmap.get(fkey);
				else {
					pfont = theApplet.createFont("SansSerif", fsize, true);
					fontmap.put(fkey, pfont);
				}
			}
		}
		return pfont;
	}


	/**
	 * A quick way to get the default Sans Serif font (11pt)
	 * @param theApplet
	 * @return the applet's default color scheme
	 */
	public static PFont getDefaultFont(PApplet theApplet){
		return getFont(theApplet, "SansSerif", 11);
	}

	
	/**
	 * This is a private class and is only used by the GFont class.
	 * It defines a key to uniquely identify fonts created based on
	 * their system font name and size. The purpose is to prevent
	 * multiple PFont objects that represent the same font/size.
	 * 
	 * @author Peter Lager
	 *
	 */
	private static class GFontKey implements Comparable<GFontKey>{

		private final String fontKey;

		public GFontKey(String fontname, int fontsize){
			fontKey = fontname + "-" + fontsize;
		}

		public boolean equals(Object o){
			GFontKey fkey = (GFontKey) o;
			if(fkey == null)
				return false;
			return fontKey.equals(fkey.fontKey);
		}
	
		public int hashCode(){
			return fontKey.hashCode();
		}
		
		public int compareTo(GFontKey obj) {
			GFontKey fkey = (GFontKey) obj;
			if(fkey == null)
				return 1;
			return fontKey.compareTo(fkey.fontKey );
		}

	}
	
//	/**
//	 * Get the system default Serif font
//	 * @param theApplet
//	 * @param fsize the font size wanted
//	 * @return
//	 */
//	public static PFont getSerifFont(PApplet theApplet, int fsize){
//		fsize = PApplet.constrain(fsize, 8, 72);
//		GFontKey fkey = new GFontKey("Serif", fsize);
//		PFont pfont;
//		if(fontmap.containsKey(fkey))
//			return fontmap.get(fkey);
//		else {
//			pfont = theApplet.createFont("Serif", fsize, true);
//			fontmap.put(fkey, pfont);
//			return pfont;
//		}
//	}
//
//	/**
//	 * Get the system Sans Serif font
//	 * @param theApplet
//	 * @param fsize the font size wanted
//	 * @return
//	 */
//	public static PFont getSansSerifFont(PApplet theApplet, int fsize){
//		fsize = PApplet.constrain(fsize, 8, 72);
//		GFontKey fkey = new GFontKey("SansSerif", fsize);
//		PFont pfont;
//		if(fontmap.containsKey(fkey))
//			pfont = fontmap.get(fkey);
//		else {
//			pfont = theApplet.createFont("SansSerif", fsize, true);
//			fontmap.put(fkey, pfont);
//		}
//		return pfont;
//	}
//
//	/**
//	 * Get the system mono-spaced font
//	 * @param theApplet
//	 * @param fsize the font size wanted
//	 * @return
//	 */
//	public static PFont getMonospacedFont(PApplet theApplet, int fsize){
//		fsize = PApplet.constrain(fsize, 8, 72);
//		GFontKey fkey = new GFontKey("Monospaced", fsize);
//		PFont pfont;
//		if(fontmap.containsKey(fkey))
//			pfont = fontmap.get(fkey);
//		else {
//			pfont = theApplet.createFont("Monospaced", fsize, true);
//			fontmap.put(fkey, pfont);
//		}
//		return pfont;
//	}
	
}

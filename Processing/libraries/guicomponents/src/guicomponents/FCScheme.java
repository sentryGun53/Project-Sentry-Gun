package guicomponents;

import java.awt.Color;
import java.io.IOException;
import java.io.InputStream;

import processing.core.PApplet;
import processing.core.PImage;

public class FCScheme implements GConstants {

	protected static PApplet app;

	public static final int RED_SCHEME 		= 0;
	public static final int GREEN_SCHEME 	= 1;
	public static final int YELLOW_SCHEME	= 2;
	public static final int PURPLE_SCHEME	= 3;
	public static final int ORANGE_SCHEME 	= 4;
	public static final int CYAN_SCHEME 	= 5;
	public static final int BLUE_SCHEME 	= 6;
	public static final int GREY_SCHEME 	= 7;
	public static final int SCHEME_8		= 8;
	public static final int SCHEME_9		= 9;
	public static final int SCHEME_10		= 10;
	public static final int SCHEME_11		= 11;
	public static final int SCHEME_12		= 12;
	public static final int SCHEME_13		= 13;
	public static final int SCHEME_14		= 14;
	public static final int SCHEME_15		= 15;


	private static int[][] palettes = null;
	private static Color[][] jpalettes = null;

	/**
	 * Set the color scheme to one of the preset schemes
	 * BLUE / GREEN / RED /  PURPLE / YELLOW / CYAN / GREY
	 * or if you have created your own schemes following the instructions
	 * at gui4processing.lagers.org.uk/colorscheme.html then you can enter
	 * the appropriate numeric value of the scheme.
	 * 
	 * @param theApplet
	 * @param schemeNo
	 * @return the color scheme based on the scheme number
	 */
	public static int[] getColor(PApplet theApplet, int schemeNo){
		app = theApplet;
		schemeNo = Math.abs(schemeNo) % 16;
		if(palettes == null)
			makePalettes();
		return palettes[schemeNo];
	}

	/**
	 * Set the color scheme to one of the preset schemes
	 * BLUE / GREEN / RED /  PURPLE / YELLOW / CYAN / GREY
	 * or if you have created your own schemes following the instructions
	 * at gui4processing.lagers.org.uk/colorscheme.html then you can enter
	 * the appropriate numeric value of the scheme.
	 * 
	 * @param theApplet
	 * @param schemeNo
	 * @return the color scheme based on the scheme number
	 */
	public static Color[] getJavaColor(PApplet theApplet, int schemeNo){
		app = theApplet;
		schemeNo = Math.abs(schemeNo) % 16;
		if(palettes == null)
			makePalettes();
		return jpalettes[schemeNo];
	}

	
	private static void makePalettes(){
		// Load the image
		PImage image = null;;
		InputStream is = app.createInput("user_gui_palette.png");
		if(is != null){
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			image = app.loadImage("user_gui_palette.png");
			GMessenger.message(USER_COL_SCHEME, null, null);
		}
		else {
			// User image not provided
			image = app.loadImage("default_gui_palette.png");
		}
		// Now make the palletes
		palettes = new int[16][16];
		jpalettes = new Color[16][16];
		for(int p = 0; p <16; p++)
			for(int c = 0; c < 16; c++){
				int col =  image.get(c * 16 + 8, p * 16 + 8);
				palettes[p][c] = col;
				jpalettes[p][c] = new Color((col >> 16) & 0xff, (col >> 8) & 0xff, col & 0xff);
			}
	}
}

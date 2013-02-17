package procontroll;

import processing.core.PApplet;
import net.java.games.input.Component;


class ControllRelativeSlider extends ControllSlider{

	ControllRelativeSlider(Component i_component){
		super(i_component);
		// TODO Auto-generated constructor stub
	}
	
	private float pollValue = 0;

	/**
	 * This method is called before each frame to update the slider values.
	 */
	void update(){
		if(PApplet.abs(actualValue) < component.getDeadZone()){
		}else{
			pollValue += component.getPollData()*multiplier;
		}
	}
	
	void updateRelative(){
		actualValue = pollValue;
		pollValue = 0;
	}
}

package procontroll.test;


import java.util.ArrayList;

import processing.core.PApplet;
import procontroll.ControllDevice;
import procontroll.ControllIO;


public class MultipleMouse extends PApplet {

    private ControllIO controll;

    private ArrayList devices;

    private float[] _myDeltas;

    public void setup() {
        size(1024, 768);
        smooth();
        noFill();

        controll = ControllIO.getInstance(this);
        controll.printDevices();

        devices = new ArrayList ();

        for (int i = 0; i < controll.getNumberOfDevices(); i++) {
            controll.getDevice(i).getName();
            if (controll.getDevice(i).getName().contains("Mouse")) {
                try {
                    devices.add(controll.getDevice(i));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }

        _myDeltas = new float[devices.size()];

        System.out.println("FOUND " + devices.size() + " DEVICES.");
    }


    public void draw() {
        background(255);

        for (int i = 0; i < devices.size(); i++) {
            _myDeltas[i] = ((ControllDevice)devices.get(i)).getSlider(0).getValue();
        }

        for (int i = 0; i < devices.size(); i++) {
            final float myOffset = (float) (i + 1) / (float) (devices.size() + 1);
            ellipse(width * myOffset, height / 2, _myDeltas[i] * 10, _myDeltas[i] * 10);
        }
    }


    public static void main(String[] args) {
        PApplet.main(new String[] {MultipleMouse.class.getName()});
    }
}



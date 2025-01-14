package org.example;

import org.example.Graphics.GUIWithGraphics;
import org.example.MoveRectangle.MoveARectangle;

public class Main {

    static GUI gui = new GUI();
    static GUIWithGraphics Ggui = new GUIWithGraphics();
    static MoveARectangle Mgui = new MoveARectangle();

    public static void main(String[] args) {
        //gui.theMainWindow();
        //Ggui.graphicsWindow();
        Mgui.rectangleOnMove();
    }
}
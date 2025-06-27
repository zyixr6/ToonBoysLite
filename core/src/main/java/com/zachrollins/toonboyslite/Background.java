package com.zachrollins.toonboyslite;

import com.badlogic.gdx.scenes.scene2d.Stage;

// background image for scene
public class Background extends BaseActor {

    // load and size background
    public Background(Stage stage) {
        super(0, 0, stage);
        loadTexture("background.png");
        setSize(1280, 720);
    }
}

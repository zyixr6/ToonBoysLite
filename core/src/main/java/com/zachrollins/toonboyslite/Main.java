package com.zachrollins.toonboyslite;

import com.badlogic.gdx.Game; // <- this is the correct import
import com.badlogic.gdx.Screen;

// main application class
public class Main extends Game {

    // set up initial screen
    @Override
    public void create() {
        setScreen(new MenuScreen());
    }

    // ensure previous screen is disposed before switching
    @Override
    public void setScreen(Screen screen) {
        if (getScreen() != null) {
            getScreen().dispose();
        }
        super.setScreen(screen);
    }
}

package com.zachrollins.toonboyslite;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;

// po enemy unit
public class Po extends Enemy {

    // initialize po stats and sound
    public Po(Stage stage, Array<Friendly> friendlies) {
        super(stage, friendlies);
        health = maxHealth = 2000;
        moveSpeed = 50;
        loadAnimations();
        setAttackSound(Gdx.audio.newSound(Gdx.files.internal("sfx/skadoosh.mp3")));
    }

    // setup po animations
    private void loadAnimations() {
        int verticalPadding = 16;
        float scale = 1f;
        moveAnimation   = loadAnimation("po/move.png",   1, 0.1f, scale, verticalPadding);
        attackAnimation = loadAnimation("po/attack.png", 1, 0.1f, scale, verticalPadding);
    }
}

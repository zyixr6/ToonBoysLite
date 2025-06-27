package com.zachrollins.toonboyslite;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;

// demon enemy unit
public class Demon extends Enemy {

    // initialize demon stats and sound
    public Demon(Stage stage, Array<Friendly> friendlies) {
        super(stage, friendlies);
        health = maxHealth = 500;
        moveSpeed = 100;
        loadAnimations();
        setAttackSound(Gdx.audio.newSound(Gdx.files.internal("sfx/demon.mp3")));
    }

    // setup demon animations
    private void loadAnimations() {
        int verticalPadding = 0;
        float scale = 2.5f;
        idleAnimation   = loadAnimation("demon/IDLE.png",   4, 0.1f, scale, verticalPadding);
        moveAnimation   = loadAnimation("demon/FLYING.png", 4, 0.1f, scale, verticalPadding);
        attackAnimation = loadAnimation("demon/ATTACK.png", 8, 0.1f, scale, verticalPadding);
    }
}

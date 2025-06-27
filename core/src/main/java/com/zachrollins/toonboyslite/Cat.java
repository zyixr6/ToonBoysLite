package com.zachrollins.toonboyslite;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;

// cat enemy unit
public class Cat extends Enemy {

    // initialize cat stats and sound
    public Cat(Stage stage, Array<Friendly> friendlies) {
        super(stage, friendlies);
        health = maxHealth = 250;
        moveSpeed = 100;
        loadAnimations();
        setAttackSound(Gdx.audio.newSound(Gdx.files.internal("sfx/cat.mp3")));
    }

    // load cat animations
    private void loadAnimations() {
        int verticalPadding = 16;
        float scale = 5f;
        idleAnimation   = loadAnimation("cat/IDLE.png",   8, 0.1f, scale, verticalPadding);
        moveAnimation   = loadAnimation("cat/RUN.png",    8, 0.1f, scale, verticalPadding);
        attackAnimation = loadAnimation("cat/ATTACK.png", 8, 0.1f, scale, verticalPadding);
    }
}

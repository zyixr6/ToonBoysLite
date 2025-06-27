package com.zachrollins.toonboyslite;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;

// warrior unit class
public class Warrior extends Friendly {

    // mana cost for spawning warrior
    public static final int MANA_COST = 100;

    // initialize warrior stats and sound
    public Warrior(Stage stage, Array<Enemy> enemies) {
        super(stage, enemies);
        health = maxHealth = 100;
        moveSpeed = 150;
        loadAnimations();
        setAttackSound(Gdx.audio.newSound(Gdx.files.internal("sfx/sword.mp3")));
    }

    // load warrior animations
    private void loadAnimations() {
        int verticalPadding = 7;
        float scale = 1.75f;
        idleAnimation   = loadAnimation("warrior/IDLE.png",   6, 0.1f, scale, verticalPadding);
        moveAnimation   = loadAnimation("warrior/RUN.png",    8, 0.1f, scale, verticalPadding);
        attackAnimation = loadAnimation("warrior/ATTACK.png", 5, 0.1f, scale, verticalPadding);
    }
}

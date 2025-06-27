package com.zachrollins.toonboyslite;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;

// knight unit class
public class Knight extends Friendly {

    // mana cost for spawning knight
    public static final int MANA_COST = 200;

    // initialize knight stats, animations, and sound
    public Knight(Stage stage, Array<Enemy> enemies) {
        super(stage, enemies);
        health         = maxHealth      = 100;
        moveSpeed      = 150f;
        attackDamage   = 30;
        attackCooldown = 1.5f;
        loadAnimations();
        setAttackSound(Gdx.audio.newSound(Gdx.files.internal("sfx/sword.mp3")));
    }

    // load idle, move, and attack animations
    private void loadAnimations() {
        int verticalPadding = 22;
        float scale = 2.5f;
        idleAnimation   = loadAnimation("knight/IDLE.png",   7, 0.1f, scale, verticalPadding);
        moveAnimation   = loadAnimation("knight/RUN.png",    8, 0.1f, scale, verticalPadding);
        attackAnimation = loadAnimation("knight/ATTACK.png", 6, 0.1f, scale, verticalPadding);
    }
}

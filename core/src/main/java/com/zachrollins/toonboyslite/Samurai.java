package com.zachrollins.toonboyslite;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;

// samurai unit class
public class Samurai extends Friendly {

    // mana cost for spawning samurai
    public static final int MANA_COST = 300;

    // initialize samurai stats, animations, and sound
    public Samurai(Stage stage, Array<Enemy> enemies) {
        super(stage, enemies);
        health = maxHealth = 50;
        moveSpeed = 500;
        attackDamage = 10;
        attackCooldown = 0.1f;
        loadAnimations();
        setAttackSound(Gdx.audio.newSound(Gdx.files.internal("sfx/sword.mp3")));
    }

    // load idle, move, and attack animations
    private void loadAnimations() {
        int verticalPadding = 15;
        float scale = 2.5f;
        idleAnimation   = loadAnimation("samurai/IDLE.png",   10, 0.1f, scale, verticalPadding);
        moveAnimation   = loadAnimation("samurai/RUN.png",    16, 0.1f, scale, verticalPadding);
        attackAnimation = loadAnimation("samurai/ATTACK.png", 7, 0.1f, scale, verticalPadding);
    }
}

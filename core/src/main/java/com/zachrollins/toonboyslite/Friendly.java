package com.zachrollins.toonboyslite;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;

// base class for friendly units
public abstract class Friendly extends Unit {

    // mana cost to spawn this unit
    protected int manaCost;

    // list of enemies this unit will attack
    protected Array<Enemy> enemies;

    // initialize with stage and enemy list
    public Friendly(Stage stage, Array<Enemy> enemies) {
        super(stage);
        this.enemies = enemies;
    }

    // move direction for friendly units (to the right)
    @Override
    protected int moveDirection() {
        return +1;
    }

    // opposing targets are enemies
    @Override
    protected Array<BaseActor> getTargets() {
        return (Array<BaseActor>)(Array<?>) enemies;
    }
}

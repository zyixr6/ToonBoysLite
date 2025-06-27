package com.zachrollins.toonboyslite;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;

// base class for enemy units
public abstract class Enemy extends Unit {

    // list of friendly units this enemy will attack
    protected Array<Friendly> friendlies;

    // initialize with stage and friendly list
    public Enemy(Stage stage, Array<Friendly> friendlies) {
        super(stage);
        this.friendlies = friendlies;
    }

    // move direction for enemies (to the left)
    @Override
    protected int moveDirection() {
        return -1;
    }

    // opposing targets are friendlies
    @Override
    protected Array<BaseActor> getTargets() {
        return (Array<BaseActor>) (Array<?>) friendlies;
    }
}

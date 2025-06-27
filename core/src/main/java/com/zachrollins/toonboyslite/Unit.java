package com.zachrollins.toonboyslite;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;

// base class for all units
public abstract class Unit extends BaseActor {

    // per-unit stats and timers
    protected int    health, maxHealth;
    protected float  moveSpeed;
    protected int    attackDamage = 10;
    protected float  attackCooldown = 1.0f;
    protected float  attackTimer = 0f;

    // movement and attack state
    protected enum State { MOVING, ATTACKING }
    protected State state = State.MOVING;

    // current attack target
    protected BaseActor currentTarget;

    // animations for different states
    protected Animation<TextureRegion> idleAnimation;
    protected Animation<TextureRegion> moveAnimation;
    protected Animation<TextureRegion> attackAnimation;
    protected float stateTime = 0f;

    // sound effect for attack
    protected Sound attackSound;

    // constructor for manual setup without stats
    public Unit(Stage stage) {
        super(0, 0, stage);
        this.attackSound = null;
    }

    // constructor for quick stats-based setup
    public Unit(Stage stage,
                int health,
                float moveSpeed,
                int attackDamage,
                float attackCooldown) {
        super(0, 0, stage);
        this.health         = this.maxHealth      = health;
        this.moveSpeed      = moveSpeed;
        this.attackDamage   = attackDamage;
        this.attackCooldown = attackCooldown;
        this.attackSound    = null;
    }

    // get current health value
    public int getHealth() {
        return health;
    }

    // apply damage to this unit
    public void takeDamage(int amount) {
        health -= amount;
        if (health <= 0) {
            remove();
            currentTarget = null;
        }
    }

    // update logic each frame
    @Override
    public void act(float delta) {
        super.act(delta);
        stateTime += delta;

        if (currentTarget == null
            || currentTarget.getParent() == null
            || !getBounds().overlaps(currentTarget.getBounds())) {
            currentTarget = findTarget();
        }

        Array<BaseActor> hits = findTargets();
        if (!hits.isEmpty()) {
            state = State.ATTACKING;
            attackTimer += delta;
            if (attackTimer >= attackCooldown) {
                attackTimer = 0;
                attack();
            }
        } else {
            state = State.MOVING;
            moveBy(moveDirection() * moveSpeed * delta, 0);
        }
    }

    // find all overlapping opposing actors
    protected Array<BaseActor> findTargets() {
        Array<BaseActor> hits = new Array<>();
        Rectangle myBounds = getBounds();
        for (BaseActor target : getTargets()) {
            if (target.getParent() != null
                && myBounds.overlaps(target.getBounds())) {
                hits.add(target);
            }
        }
        return hits;
    }

    // find any one overlapping opposing actor
    protected BaseActor findTarget() {
        for (BaseActor target : getTargets()) {
            if (target.getParent() != null
                && getBounds().overlaps(target.getBounds())) {
                return target;
            }
        }
        return null;
    }

    // deal damage and play sound on overlapping targets
    protected void attack() {
        if (attackSound != null) {
            attackSound.play(MuteManager.isMuted() ? 0f : 1f);
        }
        for (BaseActor b : findTargets()) {
            if (b instanceof Friendly f) {
                f.takeDamage(attackDamage);
            } else if (b instanceof Enemy e) {
                e.takeDamage(attackDamage);
            }
        }
        if (findTargets().isEmpty()) {
            state = State.MOVING;
        }
    }

    // render animation frame based on state
    @Override
    public void draw(Batch batch, float parentAlpha) {
        TextureRegion frame;
        switch (state) {
            case MOVING:
                frame = moveAnimation.getKeyFrame(stateTime, true);
                break;
            case ATTACKING:
                frame = attackAnimation.getKeyFrame(stateTime, true);
                break;
            default:
                frame = idleAnimation.getKeyFrame(stateTime, true);
        }
        batch.draw(frame, getX(), getY(), getWidth(), getHeight());
    }

    // draw health bar above unit
    public void drawHealthBar(ShapeRenderer sr, int indexOffset) {
        float barW = getWidth() * 0.7f;
        float barH = 8f;
        float ratio = (float) health / maxHealth;

        float x = getX() + (getWidth() - barW) / 2f;
        float y = getY() + getHeight() + 8f + (indexOffset * 4f);

        sr.setColor(Color.BLACK);
        sr.rect(x - 1, y - 1, barW + 2, barH + 2);

        sr.setColor(Color.DARK_GRAY);
        sr.rect(x, y, barW, barH);

        sr.setColor(this instanceof Enemy ? Color.RED : Color.GREEN);
        sr.rect(x, y, barW * ratio, barH);
    }

    // get bounding rectangle for collision
    public Rectangle getBounds() {
        return new Rectangle(getX(), getY(), getWidth(), getHeight());
    }

    // subclasses define movement direction (-1 or +1)
    protected abstract int moveDirection();

    // subclasses provide list of opposing targets
    protected abstract Array<BaseActor> getTargets();

    // allow subclasses to set custom attack sound
    protected void setAttackSound(Sound sound) {
        this.attackSound = sound;
    }
}

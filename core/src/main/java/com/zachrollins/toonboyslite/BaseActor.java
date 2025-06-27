package com.zachrollins.toonboyslite;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;

// base class for drawable and animated game objects
public class BaseActor extends Actor {

    // static texture for basic drawing
    protected Texture texture;

    // constructor adds actor to stage at position
    public BaseActor(float x, float y, Stage s) {
        setPosition(x, y);
        s.addActor(this);
    }

    // load a texture and resize actor
    public void loadTexture(String file) {
        texture = new Texture(file);
        setSize(texture.getWidth(), texture.getHeight());
    }

    // load animation from sprite sheet with optional vertical crop and scale
    protected Animation<TextureRegion> loadAnimation(
        String path,
        int numFrames,
        float frameDuration,
        float scale,
        int verticalPadding
    ) {
        Texture sheet = new Texture(path);
        int frameWidth = sheet.getWidth() / numFrames;
        int frameHeight = sheet.getHeight();
        int croppedHeight = frameHeight - (2 * verticalPadding);

        TextureRegion[][] tmp = TextureRegion.split(sheet, frameWidth, frameHeight);
        TextureRegion[] framesArray = new TextureRegion[numFrames];

        for (int i = 0; i < numFrames; i++) {
            TextureRegion fullFrame = tmp[0][i];
            TextureRegion cropped = new TextureRegion(
                fullFrame.getTexture(),
                fullFrame.getRegionX(),
                fullFrame.getRegionY() + verticalPadding,
                fullFrame.getRegionWidth(),
                croppedHeight
            );
            framesArray[i] = cropped;
        }

        setSize(frameWidth * scale, croppedHeight * scale);
        return new Animation<>(frameDuration, framesArray);
    }

    // get bounding rectangle for collisions
    public Rectangle getBounds() {
        return new Rectangle(getX(), getY(), getWidth(), getHeight());
    }

    // draw static texture if present
    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (texture != null) {
            batch.draw(texture, getX(), getY(), getWidth(), getHeight());
        }
    }

    // update actor each frame
    @Override
    public void act(float delta) {
        super.act(delta);
    }

    // dispose of texture if loaded
    public void dispose() {
        if (texture != null) {
            texture.dispose();
        }
    }
}

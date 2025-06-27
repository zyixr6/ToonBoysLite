package com.zachrollins.toonboyslite;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Stage;

// ground platform for units
public class Platform extends BaseActor {

    // total width of platform
    public static final float PLATFORM_WIDTH = 1280 * 2f;

    // texture for underground tiles
    private final Texture dirtTexture;

    // create platform and load textures
    public Platform(Stage stage) {
        super(0, 120, stage);
        loadTexture("tile_grass.png");
        dirtTexture = new Texture("tile.png");
        setWidth(PLATFORM_WIDTH);
    }

    // draw grass on top and dirt below
    @Override
    public void draw(Batch batch, float parentAlpha) {
        float tileWidth = texture.getWidth();
        int numTiles = (int)(getWidth() / tileWidth);

        float dirtHeight = dirtTexture.getHeight();
        int numDown = (int)(720 / dirtHeight);

        for (int i = 0; i < numTiles; i++) {
            float x = getX() + i * tileWidth;
            batch.draw(texture, x, getY());
            for (int j = 1; j <= numDown; j++) {
                batch.draw(dirtTexture, x, getY() - j * dirtHeight);
            }
        }
    }

    // dispose of dirt texture
    @Override
    public void dispose() {
        dirtTexture.dispose();
    }
}

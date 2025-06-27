package com.zachrollins.toonboyslite;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class MenuScreen implements Screen {

    // scene2d stage for ui elements
    private Stage stage;

    // textures for background and title
    private Texture backgroundTex;
    private Texture titleTex;
    private Image titleImage;

    // music for menu
    private Music menuMusic;

    // textures for mute button states
    private Texture muteTexture;
    private Texture mutedTexture;
    private ImageButton muteButton;

    public MenuScreen() {
        // initialize stage with viewport
        stage = new Stage(new ScreenViewport());

        // load background and title images
        backgroundTex = new Texture(Gdx.files.internal("menu-background.jpg"));
        titleTex = new Texture(Gdx.files.internal("title.png"));

        // create title image and center it
        titleImage = new Image(titleTex);
        titleImage.setPosition(
            (stage.getWidth() - titleImage.getWidth()) / 2f,
            stage.getHeight() * 0.6f
        );
        stage.addActor(titleImage);

        // load and position play button
        Texture playTex = new Texture(Gdx.files.internal("start.png"));
        ImageButton playButton = new ImageButton(
            new TextureRegionDrawable(new TextureRegion(playTex))
        );
        playButton.setPosition(
            (stage.getWidth() - playButton.getWidth()) / 2f,
            stage.getHeight() * 0.15f
        );
        playButton.addListener(event -> {
            if (playButton.isPressed()) {
                ((Main) Gdx.app.getApplicationListener()).setScreen(
                    new FirstBattleScreen((Main) Gdx.app.getApplicationListener())
                );
                return true;
            }
            return false;
        });
        stage.addActor(playButton);

        // load mute button textures
        muteTexture = new Texture(Gdx.files.internal("mute.png"));
        mutedTexture = new Texture(Gdx.files.internal("muted.png"));

        // create and size mute button
        muteButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(muteTexture)));
        float buttonSize = 100;
        muteButton.getImage().setScaling(Scaling.fill);
        muteButton.setSize(buttonSize, buttonSize);

        // position mute button at top-right
        muteButton.setPosition(
            stage.getWidth() - muteButton.getWidth() - 10,
            stage.getHeight() - muteButton.getHeight() - 10
        );
        muteButton.addListener(event -> {
            if (event instanceof InputEvent && ((InputEvent) event).getType() == InputEvent.Type.touchDown) {
                MuteManager.toggleMute(menuMusic);
                updateMuteButtonTexture();
                return true;
            }
            return false;
        });
        stage.addActor(muteButton);

        // set input processor to stage
        Gdx.input.setInputProcessor(stage);

        // load and play menu music if not muted
        menuMusic = Gdx.audio.newMusic(Gdx.files.internal("music/menu.mp3"));
        if (!MuteManager.isMuted()) {
            menuMusic.setLooping(true);
            menuMusic.play();
        }
    }

    // update the mute button icon based on mute state
    private void updateMuteButtonTexture() {
        muteButton.getStyle().imageUp = new TextureRegionDrawable(
            new TextureRegion(MuteManager.isMuted() ? mutedTexture : muteTexture)
        );
        muteButton.invalidate();
    }

    @Override
    public void show() {
        // called when this screen is set
    }

    @Override
    public void render(float delta) {
        // clear screen and draw background
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(Gdx.gl20.GL_COLOR_BUFFER_BIT);
        stage.getBatch().begin();
        stage.getBatch().draw(backgroundTex, 0, 0, stage.getWidth(), stage.getHeight());
        stage.getBatch().end();

        // update and draw stage actors
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        // update viewport on resize
        if (width <= 0 || height <= 0) return;
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {
        // called on pause
    }

    @Override
    public void resume() {
        // called on resume
    }

    @Override
    public void hide() {
        // called when screen is hidden
    }

    @Override
    public void dispose() {
        // dispose all resources
        stage.dispose();
        backgroundTex.dispose();
        titleTex.dispose();
        muteTexture.dispose();
        mutedTexture.dispose();
        menuMusic.dispose();
    }
}

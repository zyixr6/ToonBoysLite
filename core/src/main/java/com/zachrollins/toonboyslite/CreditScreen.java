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

public class CreditScreen implements Screen {

    // scene2d stage for ui elements
    private Stage stage;

    // textures for background, title, and thanks message
    private Texture backgroundTex, titleTex, thanksTex;

    // textures for replay and exit buttons
    private Texture playTex, exitTex;

    // images for title and thanks
    private Image titleImage, thanksImage;

    // buttons for replay and exit
    private ImageButton playAgainButton, exitButton;

    // music for credit screen
    private Music creditMusic;

    // textures for mute button on/off
    private Texture muteTexture, mutedTexture;

    // button to toggle mute
    private ImageButton muteButton;

    // setup ui and audio
    public CreditScreen() {
        // create stage with viewport
        stage = new Stage(new ScreenViewport());

        // load all textures
        backgroundTex = new Texture(Gdx.files.internal("credit-background.png"));
        titleTex      = new Texture(Gdx.files.internal("win.png"));
        thanksTex     = new Texture(Gdx.files.internal("thanks.png"));
        playTex       = new Texture(Gdx.files.internal("replay.png"));
        exitTex       = new Texture(Gdx.files.internal("exit.png"));

        // create and position title image
        titleImage = new Image(titleTex);
        titleImage.setPosition(
            (stage.getWidth() - titleImage.getWidth()) / 2f,
            stage.getHeight() * 0.6f
        );
        stage.addActor(titleImage);

        // create and position thanks image below title
        thanksImage = new Image(thanksTex);
        float thanksY = titleImage.getY() - thanksImage.getHeight() - 10f;
        thanksImage.setPosition(
            (stage.getWidth() - thanksImage.getWidth()) / 2f,
            thanksY
        );
        stage.addActor(thanksImage);

        // create replay and exit buttons
        playAgainButton = new ImageButton(
            new TextureRegionDrawable(new TextureRegion(playTex))
        );
        exitButton = new ImageButton(
            new TextureRegionDrawable(new TextureRegion(exitTex))
        );

        // layout buttons side by side at bottom
        float btnY       = stage.getHeight() * 0.15f;
        float btnSpacing = 20f;
        float totalWidth = playAgainButton.getWidth() + btnSpacing + exitButton.getWidth();
        float startX     = (stage.getWidth() - totalWidth) / 2f;
        playAgainButton.setPosition(startX, btnY);
        exitButton.setPosition(startX + playAgainButton.getWidth() + btnSpacing, btnY);
        stage.addActor(playAgainButton);
        stage.addActor(exitButton);

        // add listeners to buttons
        playAgainButton.addListener(evt -> {
            if (playAgainButton.isPressed()) {
                ((Main) Gdx.app.getApplicationListener()).setScreen(new MenuScreen());
                return true;
            }
            return false;
        });
        exitButton.addListener(evt -> {
            if (exitButton.isPressed()) {
                Gdx.app.exit();
                return true;
            }
            return false;
        });

        // set input to stage
        Gdx.input.setInputProcessor(stage);

        // load and play credit music if unmuted
        creditMusic = Gdx.audio.newMusic(Gdx.files.internal("music/win.mp3"));
        if (!MuteManager.isMuted()) {
            creditMusic.setLooping(true);
            creditMusic.play();
        }

        // load mute button icons
        muteTexture  = new Texture(Gdx.files.internal("mute.png"));
        mutedTexture = new Texture(Gdx.files.internal("muted.png"));

        // create and size mute button
        muteButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(muteTexture)));
        float buttonSize = 100;
        muteButton.getImage().setScaling(Scaling.fill);
        muteButton.setSize(buttonSize, buttonSize);
        muteButton.setPosition(
            stage.getWidth() - buttonSize - 10,
            stage.getHeight() - buttonSize - 10
        );
        stage.addActor(muteButton);

        // toggle mute on click
        muteButton.addListener(event -> {
            if (event instanceof InputEvent && ((InputEvent) event).getType() == InputEvent.Type.touchDown) {
                MuteManager.toggleMute(creditMusic);
                updateMuteButtonTexture();
                return true;
            }
            return false;
        });
    }

    // update mute button icon
    private void updateMuteButtonTexture() {
        muteButton.getStyle().imageUp = new TextureRegionDrawable(
            new TextureRegion(MuteManager.isMuted() ? mutedTexture : muteTexture)
        );
        muteButton.invalidate();
    }

    @Override
    public void show() {
        // called when screen is displayed
    }

    @Override
    public void render(float delta) {
        // clear screen and draw background
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(Gdx.gl20.GL_COLOR_BUFFER_BIT);
        stage.getBatch().begin();
        stage.getBatch().draw(backgroundTex, 0, 0, stage.getWidth(), stage.getHeight());
        stage.getBatch().end();

        // update and draw stage
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
        // called when hidden
    }

    @Override
    public void dispose() {
        // dispose all resources
        stage.dispose();
        backgroundTex.dispose();
        titleTex.dispose();
        thanksTex.dispose();
        playTex.dispose();
        exitTex.dispose();
        muteTexture.dispose();
        mutedTexture.dispose();
        creditMusic.dispose();
    }
}

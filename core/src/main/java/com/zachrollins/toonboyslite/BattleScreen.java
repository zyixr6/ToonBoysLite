package com.zachrollins.toonboyslite;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

// base class for battle screens
public abstract class BattleScreen implements Screen {

    // reference to main game
    protected Game game;

    // camera for world view
    protected OrthographicCamera camera;

    // stage for game actors
    protected Stage mainStage;

    // background renderer
    protected Background background;

    // ground platform
    protected Platform platform;

    // renderer for health bars
    private ShapeRenderer shapeRenderer = new ShapeRenderer();

    // stage for ui elements
    private Stage uiStage;

    // label showing mana
    private Label manaLabel;

    // current mana amount
    private int mana = 0;

    // timer for mana regeneration
    private float manaTimer = 0f;

    // time since last camera movement
    private float idleTime = 0f;

    // delay before camera auto-follow
    private float idleThreshold = 2f;

    // speed of manual camera pan
    private float cameraSpeed = 500f;

    // textures and images for castles
    private Texture friendlyCastleTex, enemyCastleTex;
    private Image friendlyCastleImg, enemyCastleImg;

    // banner texture and image for stage name
    protected abstract String getStageBannerFilename();
    private Texture stageTex;
    private Image stageImg;

    // background music for battle
    private Music backgroundMusic;

    // textures for mute button states
    private Texture muteTexture, mutedTexture;

    // ui button to toggle mute
    private ImageButton muteButton;

    // game end state flags and banners
    private boolean gameEnded = false;
    private boolean playerWon = false;
    private Texture endBanner, promptBanner;

    // constructor sets up camera and stages
    public BattleScreen(Game game) {
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1280, 720);
        mainStage = new Stage();
        uiStage = new Stage(new ScreenViewport());
    }

    // subclasses must init enemies
    public abstract void initEnemies();

    // game logic update called each frame
    public abstract void update(float dt);

    // access current enemies
    protected abstract Array<Enemy> getEnemies();

    // access current friendly units
    protected abstract Array<Friendly> getFriendlies();

    @Override
    public void show() {
        // setup background and platform
        background = new Background(mainStage);
        platform   = new Platform(mainStage);

        // play background music
        if (backgroundMusic != null) {
            backgroundMusic.setLooping(true);
            backgroundMusic.play();
        }

        // load castle textures
        friendlyCastleTex = new Texture(Gdx.files.internal("castle.png"));
        enemyCastleTex    = new Texture(Gdx.files.internal("haunted-castle.png"));

        // create castle images
        friendlyCastleImg = new Image(friendlyCastleTex);
        enemyCastleImg    = new Image(enemyCastleTex);

        // scale castle images to height 400
        float targetH = 400f;
        float fScale  = targetH / friendlyCastleTex.getHeight();
        float eScale  = targetH / enemyCastleTex.getHeight();
        friendlyCastleImg.setSize(friendlyCastleTex.getWidth() * fScale, targetH);
        enemyCastleImg.setSize(enemyCastleTex.getWidth()   * eScale, targetH);

        // position castles on platform
        float groundY = platform.getY() + platform.getHeight();
        float inset   = 50f;
        float yOffset = groundY - 35f;
        friendlyCastleImg.setPosition(platform.getX() + inset, yOffset);
        enemyCastleImg.setPosition(
            platform.getX() + platform.getWidth() - enemyCastleImg.getWidth() - inset,
            yOffset
        );

        // add castles behind other actors
        mainStage.addActor(friendlyCastleImg);
        mainStage.addActor(enemyCastleImg);

        // setup user interface
        setupUI();

        // setup input processors
        setupInput();
    }

    // build ui components
    private void setupUI() {
        // create font for labels and buttons
        BitmapFont font = new BitmapFont();
        font.getData().setScale(2f);

        // create mana label
        LabelStyle ls = new LabelStyle(font, Color.WHITE);
        manaLabel = new Label("mana: 0", ls);
        manaLabel.setPosition(10, uiStage.getHeight() - 60);
        uiStage.addActor(manaLabel);

        // create pixmap for button backgrounds
        Pixmap pix = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pix.setColor(Color.DARK_GRAY); pix.fill();
        Drawable up = new TextureRegionDrawable(new TextureRegion(new Texture(pix)));
        pix.setColor(Color.LIGHT_GRAY); pix.fill();
        Drawable down = new TextureRegionDrawable(new TextureRegion(new Texture(pix)));
        pix.dispose();

        // style for spawn buttons
        TextButtonStyle btnStyle = new TextButtonStyle();
        btnStyle.up            = up;
        btnStyle.down          = down;
        btnStyle.font          = font;
        btnStyle.fontColor     = Color.WHITE;
        btnStyle.downFontColor = Color.DARK_GRAY;

        // mana costs for units
        int warriorCost = Warrior.MANA_COST;
        int knightCost  = Knight.MANA_COST;
        int samuraiCost = Samurai.MANA_COST;

        // layout spawn buttons
        float btnW    = 300f, btnH = 80f, spacing = 20f;
        float cx      = uiStage.getWidth() / 2f;
        float totalW  = 3 * btnW + 2 * spacing;
        float startX  = cx - totalW / 2f;
        float yPos    = 20f;

        // warrior spawn button
        TextButton warriorBtn = new TextButton("warrior (" + warriorCost + ")", btnStyle);
        warriorBtn.getLabel().setFontScale(2f);
        warriorBtn.setSize(btnW, btnH);
        warriorBtn.setPosition(startX, yPos);
        warriorBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                if (!gameEnded && mana >= warriorCost) {
                    mana -= warriorCost;
                    manaLabel.setText("mana: " + mana);
                    spawnWarrior();
                }
            }
        });
        uiStage.addActor(warriorBtn);

        // knight spawn button
        TextButton knightBtn = new TextButton("knight (" + knightCost + ")", btnStyle);
        knightBtn.getLabel().setFontScale(2f);
        knightBtn.setSize(btnW, btnH);
        knightBtn.setPosition(startX + (btnW + spacing), yPos);
        knightBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                if (!gameEnded && mana >= knightCost) {
                    mana -= knightCost;
                    manaLabel.setText("mana: " + mana);
                    spawnKnight();
                }
            }
        });
        uiStage.addActor(knightBtn);

        // samurai spawn button
        TextButton samuraiBtn = new TextButton("samurai (" + samuraiCost + ")", btnStyle);
        samuraiBtn.getLabel().setFontScale(2f);
        samuraiBtn.setSize(btnW, btnH);
        samuraiBtn.setPosition(startX + 2 * (btnW + spacing), yPos);
        samuraiBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                if (!gameEnded && mana >= samuraiCost) {
                    mana -= samuraiCost;
                    manaLabel.setText("mana: " + mana);
                    spawnSamurai();
                }
            }
        });
        uiStage.addActor(samuraiBtn);

        // load and show stage banner
        stageTex = new Texture(Gdx.files.internal(getStageBannerFilename()));
        stageImg = new Image(stageTex);
        float x = (uiStage.getWidth() - stageImg.getWidth()) / 2f;
        float y = uiStage.getHeight() - stageImg.getHeight() - 10f;
        stageImg.setPosition(x, y);
        uiStage.addActor(stageImg);

        // load mute button textures
        muteTexture  = new Texture(Gdx.files.internal("mute.png"));
        mutedTexture = new Texture(Gdx.files.internal("muted.png"));
        TextureRegionDrawable drawable = new TextureRegionDrawable(
            new TextureRegion(MuteManager.isMuted() ? mutedTexture : muteTexture)
        );
        muteButton = new ImageButton(drawable);
        float btnSize = 100;
        muteButton.setSize(btnSize, btnSize);
        muteButton.setPosition(
            uiStage.getWidth() - btnSize - 10f,
            uiStage.getHeight() - btnSize - 10f
        );
        muteButton.addListener(new com.badlogic.gdx.scenes.scene2d.utils.ClickListener() {
            @Override public void clicked(InputEvent event, float x, float y) {
                MuteManager.toggleMute(backgroundMusic);
                updateMuteButtonTexture();
            }
        });
        uiStage.addActor(muteButton);
    }

    // update mute button icon
    private void updateMuteButtonTexture() {
        TextureRegionDrawable d = new TextureRegionDrawable(
            new TextureRegion(MuteManager.isMuted() ? mutedTexture : muteTexture)
        );
        muteButton.getStyle().imageUp = d;
        muteButton.invalidate();
    }

    // spawn a warrior unit
    protected void spawnWarrior() {
        float groundY = platform.getY() + platform.getHeight();
        Warrior w = new Warrior(mainStage, getEnemies());
        w.setPosition(50, groundY);
        getFriendlies().add(w);
    }

    // spawn a knight unit
    protected void spawnKnight() {
        float groundY = platform.getY() + platform.getHeight();
        Knight k = new Knight(mainStage, getEnemies());
        k.setPosition(50, groundY);
        getFriendlies().add(k);
    }

    // spawn a samurai unit
    protected void spawnSamurai() {
        float groundY = platform.getY() + platform.getHeight();
        Samurai s = new Samurai(mainStage, getEnemies());
        s.setPosition(50, groundY);
        getFriendlies().add(s);
    }

    // setup input processors for ui and game
    private void setupInput() {
        InputMultiplexer im = new InputMultiplexer();
        im.addProcessor(uiStage);
        im.addProcessor(mainStage);
        Gdx.input.setInputProcessor(im);
    }

    @Override
    public void render(float delta) {
        // update game logic
        update(delta);

        // regenerate mana if game ongoing
        if (!gameEnded) {
            regenMana(delta);
        }

        // handle camera movement and idle follow
        boolean moved = false;
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            camera.position.x -= cameraSpeed * delta;
            idleTime = 0; moved = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            camera.position.x += cameraSpeed * delta;
            idleTime = 0; moved = true;
        }
        if (!moved) idleTime += delta;

        if (idleTime >= idleThreshold) {
            float tx;
            Array<Friendly> fr = getFriendlies();
            if (fr != null && fr.size > 0) {
                float maxX = 0;
                for (Friendly f : fr) if (f.getX() > maxX) maxX = f.getX();
                tx = maxX + 50;
            } else {
                Array<Enemy> en = getEnemies();
                if (en != null && en.size > 0) {
                    float minX = Float.MAX_VALUE;
                    for (Enemy e : en) if (e.getX() < minX) minX = e.getX();
                    tx = minX - 50;
                } else {
                    tx = platform.getWidth() / 2f;
                }
            }
            camera.position.x += (tx - camera.position.x) * 0.1f;
        }

        // clamp camera position
        camera.position.y = camera.viewportHeight / 2f;
        float minX = camera.viewportWidth / 2f;
        float maxX = platform.getWidth() - camera.viewportWidth / 2f;
        camera.position.x = Math.max(minX, Math.min(maxX, camera.position.x));

        // update background position
        background.setPosition(
            camera.position.x - camera.viewportWidth / 2f,
            camera.position.y - camera.viewportHeight / 2f
        );
        camera.update();
        mainStage.getViewport().setCamera(camera);

        // advance actors
        mainStage.act(delta);

        // check for loss or win
        if (!gameEnded) {
            for (Enemy e : getEnemies()) {
                if (e.getX() + e.getWidth() <= platform.getX()) {
                    onLose();
                    break;
                }
            }
            if (!gameEnded) {
                float right = platform.getX() + platform.getWidth();
                for (Friendly f : getFriendlies()) {
                    if (f.getX() + f.getWidth() >= right) {
                        onWin();
                        break;
                    }
                }
            }
        }

        // draw world and ui
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        mainStage.draw();

        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        int barIdx = 0;
        for (Actor a : mainStage.getActors()) {
            if (a instanceof Unit u) u.drawHealthBar(shapeRenderer, barIdx++);
        }
        shapeRenderer.end();

        uiStage.act(delta);
        uiStage.draw();

        // show end banners and handle restart/continue
        if (gameEnded) {
            SpriteBatch sb = (SpriteBatch) mainStage.getBatch();
            sb.setProjectionMatrix(camera.combined);
            sb.begin();
            float bw = endBanner.getWidth(), bh = endBanner.getHeight();
            float bx = camera.position.x - bw / 2f;
            float by = camera.viewportHeight / 2f + 50;
            sb.draw(endBanner, bx, by);
            float pw = promptBanner.getWidth(), ph = promptBanner.getHeight();
            float px = camera.position.x - pw / 2f;
            float py = by - ph - 20;
            sb.draw(promptBanner, px, py);
            sb.end();

            if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                if (playerWon) continueGame();
                else retryGame();
            }
        }
    }

    // regenerate mana over time
    private void regenMana(float delta) {
        manaTimer += delta;
        while (manaTimer >= 0.05f) {
            mana++;
            manaTimer -= 0.05f;
            manaLabel.setText("mana: " + mana);
        }
    }

    // handle loss state
    protected void onLose() {
        playerWon    = false;
        gameEnded    = true;
        endBanner    = new Texture("stage-failed.png");
        promptBanner = new Texture("retry.png");
        mainStage.getRoot().setTouchable(Touchable.disabled);
        uiStage.getRoot().setTouchable(Touchable.disabled);
    }

    // handle win state
    protected void onWin() {
        playerWon    = true;
        gameEnded    = true;
        endBanner    = new Texture("stage-complete.png");
        promptBanner = new Texture("continue.png");
        mainStage.getRoot().setTouchable(Touchable.disabled);
        uiStage.getRoot().setTouchable(Touchable.disabled);
    }

    // go to next level or screen
    protected void continueGame() {}

    // retry current level
    protected void retryGame() {}

    // assign music before showing
    public void setBackgroundMusic(Music music) {
        this.backgroundMusic = music;
    }

    @Override public void resize(int w, int h) { uiStage.getViewport().update(w, h, true); }
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        // free all resources
        mainStage.dispose();
        uiStage.dispose();
        shapeRenderer.dispose();
        if (endBanner    != null) endBanner.dispose();
        if (promptBanner != null) promptBanner.dispose();
        if (friendlyCastleTex != null) friendlyCastleTex.dispose();
        if (enemyCastleTex    != null) enemyCastleTex.dispose();
        if (stageTex != null) stageTex.dispose();
        if (backgroundMusic != null) backgroundMusic.stop();
        if (muteTexture  != null) muteTexture.dispose();
        if (mutedTexture != null) mutedTexture.dispose();
    }
}

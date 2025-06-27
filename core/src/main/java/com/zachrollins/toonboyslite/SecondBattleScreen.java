package com.zachrollins.toonboyslite;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;

// extends battle screen for second level
public class SecondBattleScreen extends BattleScreen {

    // list of friendly units
    private Array<Friendly> friendlies;
    // list of enemy units
    private Array<Enemy> enemies;

    // initialize arrays and set music for stage two
    public SecondBattleScreen(Game game) {
        super(game);
        friendlies = new Array<>();
        enemies    = new Array<>();
        setBackgroundMusic(Gdx.audio.newMusic(Gdx.files.internal("music/stage-two.mp3")));
    }

    // called when this screen becomes active
    @Override
    public void show() {
        Gdx.input.setInputProcessor(mainStage);
        super.show();
        initEnemies();
    }

    // place demons on the platform at start
    @Override
    public void initEnemies() {
        float groundY = platform.getY() + platform.getHeight();
        Demon demon = new Demon(mainStage, friendlies);
        demon.setPosition(
            platform.getWidth() - demon.getWidth() - 50,
            groundY
        );
        enemies.add(demon);
    }

    // game-specific update logic
    @Override
    public void update(float dt) {
        // add update code here
    }

    // return current enemies
    @Override
    public Array<Enemy> getEnemies() {
        return enemies;
    }

    // return current friendlies
    @Override
    public Array<Friendly> getFriendlies() {
        return friendlies;
    }

    // proceed to final battle on win
    @Override
    protected void continueGame() {
        game.setScreen(new FinalBattleScreen(game));
    }

    // retry this stage on loss
    @Override
    protected void retryGame() {
        game.setScreen(new SecondBattleScreen(game));
    }

    // banner image for this stage
    @Override
    protected String getStageBannerFilename() {
        return "stage-two.png";
    }
}

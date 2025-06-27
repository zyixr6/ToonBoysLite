package com.zachrollins.toonboyslite;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;

// extends battle screen for first level
public class FirstBattleScreen extends BattleScreen {

    // lists for friendly and enemy units
    private Array<Friendly> friendlies;
    private Array<Enemy> enemies;

    // initialize arrays and set music for stage one
    public FirstBattleScreen(Game game) {
        super(game);
        friendlies = new Array<>();
        enemies    = new Array<>();
        setBackgroundMusic(Gdx.audio.newMusic(Gdx.files.internal("music/stage-one.mp3")));
    }

    // called when this screen is shown
    @Override
    public void show() {
        Gdx.input.setInputProcessor(mainStage);
        super.show();
        initEnemies();
    }

    // place initial enemy units
    @Override
    public void initEnemies() {
        float groundY = platform.getY() + platform.getHeight();
        Cat cat = new Cat(mainStage, friendlies);
        cat.setPosition(
            platform.getWidth() - cat.getWidth() - 50,
            groundY
        );
        enemies.add(cat);
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

    // proceed to second battle on win
    @Override
    protected void continueGame() {
        game.setScreen(new SecondBattleScreen(game));
    }

    // retry this battle on loss
    @Override
    protected void retryGame() {
        game.setScreen(new FirstBattleScreen(game));
    }

    // banner image for this stage
    @Override
    protected String getStageBannerFilename() {
        return "stage-one.png";
    }
}

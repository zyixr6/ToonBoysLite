package com.zachrollins.toonboyslite;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.utils.Array;

public class FinalBattleScreen extends BattleScreen {

    private Array<Friendly> friendlies;
    private Array<Enemy> enemies;

    private Music battleHorn;
    private Music thunderSfx;
    private Music battleMusic;

    public FinalBattleScreen(Game game) {
        super(game);
        friendlies = new Array<>();
        enemies = new Array<>();

        // Load all the music tracks
        battleHorn = Gdx.audio.newMusic(Gdx.files.internal("sfx/battle-horn.mp3"));
        thunderSfx = Gdx.audio.newMusic(Gdx.files.internal("sfx/thunder.mp3"));
        battleMusic = Gdx.audio.newMusic(Gdx.files.internal("music/final-stage.mp3"));

        battleHorn.setVolume(MuteManager.isMuted() ? 0f : 1f);
        thunderSfx.setVolume(MuteManager.isMuted() ? 0f : 1f);
        battleMusic.setVolume(MuteManager.isMuted() ? 0f : 1f);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(mainStage);
        super.show();
        initEnemies();

        // Start the battle horn music
        startBattleHorn();
    }

    private void startBattleHorn() {
        // Play the battle horn
        battleHorn.setOnCompletionListener(new Music.OnCompletionListener() {
            @Override
            public void onCompletion(Music music) {
                // Once battle horn music finishes, play thunder music
                playBattleMusic();
            }
        });
        battleHorn.play();
    }

    private void playBattleMusic() {
        // Finally, start the main battle music and play character voice line
        thunderSfx.play();
        setBackgroundMusic(battleMusic);
        battleMusic.setLooping(true);
        battleMusic.play();
    }

    @Override
    public void initEnemies() {
        float groundY = platform.getY() + platform.getHeight();

        Po greatestEver = new Po(mainStage, friendlies);
        greatestEver.setPosition(
            platform.getWidth() - greatestEver.getWidth() - 50,
            groundY
        );

        enemies.add(greatestEver);
    }

    @Override
    public void update(float dt) {
        // Game-specific update logic
    }

    @Override
    public Array<Enemy> getEnemies() {
        return enemies;
    }

    @Override
    public Array<Friendly> getFriendlies() {
        return friendlies;
    }

    @Override
    protected void continueGame() {
        game.setScreen(new CreditScreen());
    }

    @Override
    protected void retryGame() {
        game.setScreen(new FinalBattleScreen(game));
    }

    @Override
    protected String getStageBannerFilename() {
        return "final-stage.png";
    }

    @Override
    public void dispose() {
        super.dispose();
        // Dispose of music files
        battleHorn.dispose();
        thunderSfx.dispose();
        battleMusic.dispose();
    }
}

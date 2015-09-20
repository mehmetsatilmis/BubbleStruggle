package com.interview.game;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.interview.game.Manager.FileManager;
import com.interview.game.Manager.InputManager;
import com.interview.game.State.PlayState;

public class MyGdxGame implements ApplicationListener {
    private PlayState playState;
    private InputManager inputManager;
    private SpriteBatch spriteBatch;
    private Texture  backgroundImage;

    public static final float STEP = 1 / 60f;
    private float accum;

    @Override
    public void create () {

        playState = new PlayState();
        inputManager = new InputManager();

        spriteBatch = new SpriteBatch();
        backgroundImage = FileManager.getManager().getTexture(FileManager.BACKGROUND_IMAGE,"background");
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void render () {
        accum += Gdx.graphics.getDeltaTime();
        while(accum >= STEP) {
            accum -= STEP;
            inputManager.update();
            playState.update(STEP);
            playState.render(spriteBatch);
        }
    }

    @Override
    public void pause() {
        playState.pause();
    }

    @Override
    public void resume() {
        playState.resume();
    }

    @Override
    public void dispose() {

    }
}

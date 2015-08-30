package com.interview.game;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.interview.game.Model.InputManager;
import com.interview.game.State.PlayState;

public class MyGdxGame implements ApplicationListener {
    private PlayState playState;
    private InputManager inputManager;

    public static final float STEP = 1 / 60f;
    private float accum;

	@Override
	public void create () {

        playState = new PlayState();
        inputManager = new InputManager();
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
            playState.render();
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

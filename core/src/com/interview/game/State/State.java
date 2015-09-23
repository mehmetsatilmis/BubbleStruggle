package com.interview.game.State;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.interview.game.Screen.GameScreenManager;

/**
 * Created by msatilmis on 29.08.2015.
 */
public interface State {

    public void setGameScreenManager(GameScreenManager gameScreenManager);
    public void checkStateRules(); //This function using to control for each scenes rules

    public void pause();
    public void resume();
    public void update(float dt);
    public void render(SpriteBatch spriteBatch);
    public void dispose();
}

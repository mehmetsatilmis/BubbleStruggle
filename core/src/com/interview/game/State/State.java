package com.interview.game.State;

import com.interview.game.Screen.GameScreenManager;

/**
 * Created by msatilmis on 29.08.2015.
 */
public interface State {

    public void setGameScreenManager(GameScreenManager gameScreenManager);
    public void checkStateRules();

    public void pause();
    public void resume();
    public void update(float dt);
    public void render();
    public void dispose();
}

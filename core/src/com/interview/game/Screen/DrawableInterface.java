package com.interview.game.Screen;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by msatilmis on 29.08.2015.
 *
 *  Todo : GameScreenManager implements this interface, create PauseScreenManager
 */
public interface DrawableInterface {
    public void create();
    public void update();
    public void render(SpriteBatch spriteBatch);
    public void resize(int width, int height);
    public void pause();
    public void resume();
    public void dispose();

}

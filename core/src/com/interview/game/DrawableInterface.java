package com.interview.game;

/**
 * Created by msatilmis on 29.08.2015.
 */
public interface DrawableInterface {
    public void create();
    public void update();
    public void render();
    public void resize(int width,int height);
    public void pause();
    public void resume();
    public void dispose();

}

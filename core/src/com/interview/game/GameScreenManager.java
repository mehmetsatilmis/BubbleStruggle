package com.interview.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by msatilmis on 29.08.2015.
 */
public class GameScreenManager implements DrawableInterface {

    private World world;
    private Box2DDebugRenderer box2DDebugRenderer;
    private OrthographicCamera orthoCam;

    public static int WIDTH;
    public static int HEIGHT;
    public static float PPM_W = 5;
    public static float PPM_H = 5;
    public float dt;

    private int refereansWidth = 640;
    private int refereansHeight = 480;

    public GameScreenManager(float step, World world){
        this.world = world;
        dt = step;

        WIDTH  = Gdx.graphics.getWidth();
        HEIGHT = Gdx.graphics.getHeight();

        float rate = (float) ((double) refereansWidth / (double) WIDTH);
        PPM_W = rate;
        PPM_H = (float) ((double) refereansHeight / (double) HEIGHT);
        // set up box2d cam
        orthoCam = new OrthographicCamera();
        orthoCam.setToOrtho(false, WIDTH  , HEIGHT );
        box2DDebugRenderer = new Box2DDebugRenderer();
    }

    @Override
    public void create() {

    }

    @Override
    public void update() {
        orthoCam.update();
        world.step(dt, 6, 2);
    }

    @Override
    public void render() {
        // clear screen
        Gdx.gl.glClear(Gdx.gl.GL_COLOR_BUFFER_BIT);

        // draw box2d world
        box2DDebugRenderer.render(world, orthoCam.combined);
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }
}

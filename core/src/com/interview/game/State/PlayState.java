package com.interview.game.State;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.interview.game.Model.AnimationManager;
import com.interview.game.Model.InputManager;
import com.interview.game.Operation.MyContantListener;
import com.interview.game.Screen.GameScreenManager;
import com.interview.game.Model.Ball;
import com.interview.game.Operation.CallBack.BallCallBack;
import com.interview.game.Operation.CreateBall;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.graphics.g2d.Animation;

/**
 * Created by msatilmis on 29.08.2015.
 * <p/>
 * Simulated Play state (game screen, balls vs)
 */
public class PlayState implements State {
    private GameScreenManager gameScreenManager;
    private World world;

    private float passedTime = 0f;

    public static ArrayList<Ball> balls;


    public PlayState() {
        balls = new ArrayList<Ball>();
        world = new World(new Vector2(0, 0), true);
        gameScreenManager = new GameScreenManager(Gdx.graphics.getDeltaTime(), world);
        gameScreenManager.create();
        world.setContactListener(new MyContantListener());

    }


    @Override
    public void setGameScreenManager(GameScreenManager gameScreenManager) {
        this.gameScreenManager = gameScreenManager;
    }

    @Override
    public void checkStateRules() {
        Animation anim = null;
        Animation weaponAnim = null;
        int type = InputManager.walking_type;
        if (type == 0) {
            anim = AnimationManager.getInstance().getAnimation(AnimationManager.SPRITE_WALKING_LEFT, "walkleft");
            gameScreenManager.getPlayer().moveLeft();
        } else if (type == 1) {
            anim = AnimationManager.getInstance().getAnimation(AnimationManager.SPRITE_WALKING_FORWARD, "walk");
        } else {
            anim = AnimationManager.getInstance().getAnimation(AnimationManager.SPRITE_WALKING_RIGHT, "walkright");
            gameScreenManager.getPlayer().moveRight();
        }

        if (InputManager.isTouched) {
            weaponAnim = AnimationManager.getInstance().getAnimation(AnimationManager.WEAPON, "weapon");
        }

        gameScreenManager.setPlayerAnim(anim);


    }

    @Override
    public void pause() {
        gameScreenManager.pause();
    }

    @Override
    public void resume() {
        gameScreenManager.resume();
    }

    @Override
    public void update(float dt) {
        passedTime += Gdx.graphics.getDeltaTime();
        checkStateRules();

        gameScreenManager.dt = dt;
        gameScreenManager.passedTime = passedTime;
        gameScreenManager.update();
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        gameScreenManager.render(spriteBatch);
    }

    @Override
    public void dispose() {
        for (int i = 0; i < balls.size(); ++i) {
            balls.get(i).dispose();
        }
    }

    private class CollisionVar {
        // category bits
        public static final short BIT_SCREEN = 2;
        public static final short BIT_BALL = 4;
    }
}

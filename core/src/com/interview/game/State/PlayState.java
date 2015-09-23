package com.interview.game.State;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.interview.game.Manager.AnimationManager;
import com.interview.game.Manager.InputManager;
import com.interview.game.Model.Ball;
import com.interview.game.Model.Player;
import com.interview.game.Model.Weapon;
import com.interview.game.Operation.CallBack.PlayerActionCallBackAbstract;
import com.interview.game.Operation.CallBack.WeaponActionCallBackAbstract;
import com.interview.game.Operation.MyContantListener;
import com.interview.game.Screen.GameScreenManager;

import java.util.ArrayList;
import java.util.LinkedHashMap;

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
    public static LinkedHashMap<Integer, Ball>  ballLinkedHashMap = new LinkedHashMap<Integer, Ball>();


    public PlayState() {
        world = new World(new Vector2(0, 0), true);
        gameScreenManager = new GameScreenManager(Gdx.graphics.getDeltaTime(), world);
        gameScreenManager.create();
        world.setContactListener(new MyContantListener(new WeaponActionCallBackAbstract() {
            @Override
            public void onResponse(final int index) {
                checkWeaponAction(index);
            }
        }, new PlayerActionCallBackAbstract() {
            @Override
            public void onResponse(int state) {
                checkPlayerAction(state);
            }
        }));

    }


    @Override
    public void setGameScreenManager(GameScreenManager gameScreenManager) {
        this.gameScreenManager = gameScreenManager;
    }

    @Override
    public void checkStateRules() {
        com.badlogic.gdx.graphics.g2d.Animation anim = null;
        com.badlogic.gdx.graphics.g2d.Animation weaponAnim = null;
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

        if (Weapon.getWeapon().isActive) {
            Weapon.getWeapon().shoot();
        }

        gameScreenManager.setPlayerAnim(anim);

    }

    private void checkWeaponAction(int index) {
        if (index < 0)
            return;
        System.out.println(index);
        Ball ball;
        final ArrayList<Ball> list;
        synchronized (ballLinkedHashMap) {
            ball = ballLinkedHashMap.get(index);
            Player.getPlayer().score += ball.getLevel() * 10;
            ball.body.setActive(false);
            list = ball.createChildBall();
            ballLinkedHashMap.remove(ball);
        }

        System.out.println(list.size());
        for (int i = 0; i < list.size(); ++i) {
            final int finalI = i;
            Gdx.app.postRunnable(new Runnable() {
                @Override
                public void run() {
                   GameScreenManager.createBall(list.get(finalI));
                }
            });
        }

    }

    private void checkPlayerAction(int state){
        if(state == 0){
            Gdx.app.postRunnable(new Runnable() {
                @Override
                public void run() {
                    GameScreenManager.createPlayer();
                }
            });
        }
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
        for (int i = 0; i < ballLinkedHashMap.size(); ++i) {
            ballLinkedHashMap.get(i).dispose();
        }
    }

    private class CollisionVar {
        // category bits
        public static final short BIT_SCREEN = 2;
        public static final short BIT_BALL = 4;
    }
}

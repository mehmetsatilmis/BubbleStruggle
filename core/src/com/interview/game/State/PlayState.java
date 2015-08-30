package com.interview.game.State;

import com.badlogic.gdx.Gdx;
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

import javafx.animation.Animation;

/**
 * Created by msatilmis on 29.08.2015.
 * <p/>
 * Simulated Play state (game screen, balls vs)
 */
public class PlayState implements State {
    private GameScreenManager gameScreenManager;
    private World world;
    private CreateBall createBallClass;

    private Body playerBody = null;
    private float passedTime = 0f;

    public static ArrayList<Ball> balls;


    public PlayState() {
        balls = new ArrayList<Ball>();
        world = new World(new Vector2(0, 0), true);
        gameScreenManager = new GameScreenManager(Gdx.graphics.getDeltaTime(), world);
        world.setContactListener(new MyContantListener());
        createLayout();
        createModel();
    }

    /*Boundaries game screen*/
    private void createLayout() {
        //down bound
        BodyDef bdef = new BodyDef();
        bdef.position.set(0 / GameScreenManager.PPM_W, 80 / GameScreenManager.PPM_H);
        bdef.type = BodyDef.BodyType.StaticBody;
        Body body = world.createBody(bdef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(GameScreenManager.WIDTH, 1);
        FixtureDef fdef = new FixtureDef();
        fdef.shape = shape;
        fdef.filter.categoryBits = CollisionVar.BIT_SCREEN;
        fdef.filter.maskBits = CollisionVar.BIT_SCREEN | CollisionVar.BIT_BALL;

        body.createFixture(fdef).setUserData("down");
        //top bound
        bdef.position.set(0 / GameScreenManager.PPM_W, 400 / GameScreenManager.PPM_H);
        bdef.type = BodyDef.BodyType.StaticBody;
        body = world.createBody(bdef);

        shape.setAsBox(GameScreenManager.WIDTH, 1);
        fdef.shape = shape;
        fdef.filter.categoryBits = CollisionVar.BIT_SCREEN;
        fdef.filter.maskBits = CollisionVar.BIT_SCREEN | CollisionVar.BIT_BALL;

        body.createFixture(fdef).setUserData("top");


        //left bound
        bdef.position.set(0 / GameScreenManager.PPM_W, 80 / GameScreenManager.PPM_H);
        bdef.type = BodyDef.BodyType.StaticBody;
        body = world.createBody(bdef);

        shape.setAsBox(0, GameScreenManager.HEIGHT);
        fdef.shape = shape;
        fdef.filter.categoryBits = CollisionVar.BIT_SCREEN;
        fdef.filter.maskBits = CollisionVar.BIT_SCREEN | CollisionVar.BIT_BALL;
        body.createFixture(fdef).setUserData("left");

        //right bound
        bdef.position.set(GameScreenManager.WIDTH + 1, 0 / GameScreenManager.PPM_H);
        bdef.type = BodyDef.BodyType.StaticBody;
        body = world.createBody(bdef);
        fdef.filter.categoryBits = CollisionVar.BIT_SCREEN;
        fdef.filter.maskBits = CollisionVar.BIT_SCREEN | CollisionVar.BIT_BALL;

        shape.setAsBox(0, GameScreenManager.HEIGHT);
        fdef.shape = shape;
        body.createFixture(fdef).setUserData("right");


    }

    /*create balls and player*/
    private void createModel() {
        createBallClass = new CreateBall(8, new BallCallBack() {
            @Override
            public void onBallCreate(Ball ball) {
                synchronized (world) {
                    int index = 0;

                    BodyDef bdef = new BodyDef();
                    bdef.position.set(ball.position);
                    bdef.type = BodyDef.BodyType.DynamicBody;
                    Body body = world.createBody(bdef);
                    CircleShape shape = new CircleShape();
                    shape.setRadius(10 / GameScreenManager.PPM_H);

                    synchronized (balls) {
                        balls.add(ball);
                        index = balls.size();
                        FixtureDef fdef = new FixtureDef();
                        fdef.shape = shape;
                        fdef.restitution = (float) 1.1;
                        fdef.filter.categoryBits = CollisionVar.BIT_BALL;
                        fdef.filter.maskBits = CollisionVar.BIT_SCREEN;
                        body.createFixture(fdef).setUserData("" + index);
                    }

                    float angle = (float) (Math.atan2(ball.direction.y, ball.direction.x));
                    body.setLinearVelocity(new Vector2(45 * MathUtils.cos(angle), 45 * MathUtils.sin(angle)));

                }

            }
        });

        createPlayer();
        createBallClass.runCreateBall();
    }

    private void createPlayer() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(320 / GameScreenManager.PPM_W, 101 / GameScreenManager.PPM_H);
        bdef.type = BodyDef.BodyType.StaticBody;
        Body body = world.createBody(bdef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(0 / GameScreenManager.PPM_W, 0 / GameScreenManager.PPM_H);
        FixtureDef fdef = new FixtureDef();
        fdef.shape = shape;
        fdef.filter.categoryBits = CollisionVar.BIT_SCREEN;
        fdef.filter.maskBits = CollisionVar.BIT_SCREEN | CollisionVar.BIT_BALL;
        body.createFixture(fdef).setUserData("player");
        playerBody = body;
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
            AnimationManager.getManager().getAnimation("walkleft");
            if (anim == null)
                anim = AnimationManager.getManager().loadAtlasAnimation(AnimationManager.SPRITE_WALKING_LEFT, "walkleft");
        } else if (type == 1) {
            anim = AnimationManager.getManager().getAnimation("walk");
            if (anim == null)
                anim = AnimationManager.getManager().loadAtlasAnimation(AnimationManager.SPRITE_WALKING_FORWARD, "walk");
        } else {
            anim = AnimationManager.getManager().getAnimation("walkright");
            if (anim == null)
                anim = AnimationManager.getManager().loadAtlasAnimation(AnimationManager.SPRITE_WALKING_RIGHT, "walkright");
        }

        if(InputManager.isTouched){
            anim = AnimationManager.getManager().getAnimation("weapon");
            if (anim == null)
                anim = AnimationManager.getManager().loadAtlasAnimation(AnimationManager.WEAPON, "weapon");
        }

    }

    @Override
    public void pause() {
        createBallClass.endOfTimer();
    }

    @Override
    public void resume() {
        createModel();
    }

    @Override
    public void update(float dt) {
        passedTime += Gdx.graphics.getDeltaTime();

        gameScreenManager.dt = dt;
        gameScreenManager.update();
    }

    @Override
    public void render() {
        gameScreenManager.render();
    }

    @Override
    public void dispose() {

    }

    private class CollisionVar {
        // category bits
        public static final short BIT_SCREEN = 2;
        public static final short BIT_BALL = 4;
    }
}

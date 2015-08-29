package com.interview.game.State;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.interview.game.GameScreenManager;
import com.interview.game.Model.Ball;
import com.interview.game.Operation.CallBack.BallCallBack;
import com.interview.game.Operation.CreateBall;

import java.util.ArrayList;

/**
 * Created by msatilmis on 29.08.2015.
 */
public class PlayState implements State {
    private GameScreenManager gameScreenManager;
    private World world;
    private ArrayList<Ball> balls;

    public static Integer MAX_LEVEL_NUMBER = 4;

    public PlayState(){
        balls = new ArrayList<Ball>();
        world = new World(new Vector2(0,-9.81f),true);
        gameScreenManager = new GameScreenManager(Gdx.graphics.getDeltaTime(),world);
        createLayout();
        createModel();
    }

    private void createLayout(){
        // create platform
        BodyDef bdef = new BodyDef();
        bdef.position.set(0 / GameScreenManager.PPM_W, 80 / GameScreenManager.PPM_H);
        bdef.type = BodyDef.BodyType.StaticBody;
        Body body = world.createBody(bdef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(GameScreenManager.WIDTH, 5);
        FixtureDef fdef = new FixtureDef();
        fdef.shape = shape;
        body.createFixture(fdef);

        // create falling box
        bdef.position.set(0 / GameScreenManager.PPM_W, 400 / GameScreenManager.PPM_H);
        bdef.type = BodyDef.BodyType.StaticBody;
        body = world.createBody(bdef);

        shape.setAsBox(GameScreenManager.WIDTH, 5);
        fdef.shape = shape;
        body.createFixture(fdef);


    }

    private void createModel(){
        CreateBall createBallClass = new CreateBall(5,new BallCallBack(){
            @Override
            public void onBallCreate(Ball ball) {
                synchronized (world){
                    balls.add(ball);

                    BodyDef bdef = new BodyDef();
                    bdef.position.set(ball.position);
                    bdef.type = BodyDef.BodyType.DynamicBody;
                    Body body = world.createBody(bdef);

                    CircleShape shape = new CircleShape();
                    shape.setRadius(10 / GameScreenManager.PPM_H);

                    FixtureDef fdef = new FixtureDef();
                    fdef.shape = shape;
                    body.createFixture(fdef);
                }
            }
        });

        createBallClass.runCreateBall();
    }

    @Override
    public void setGameScreenManager(GameScreenManager gameScreenManager) {
        this.gameScreenManager = gameScreenManager;
    }

    @Override
    public void checkStateRules() {

    }

    @Override
    public void update(float dt) {
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
}

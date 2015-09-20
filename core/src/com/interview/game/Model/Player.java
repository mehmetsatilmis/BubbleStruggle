package com.interview.game.Model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Transform;
import com.badlogic.gdx.physics.box2d.World;
import com.interview.game.Screen.GameScreenManager;
import com.interview.game.State.PlayState;

/**
 * Created by airties on 15/09/15.
 */
public class Player {

    public Integer score;
    public Integer lifeCount;
    public Body playerBody;
    public Vector2 preposition;
    public Animation anim;
    public int width;
    public int height;

    public boolean is_enable_walking_right = true;
    public boolean isIs_enable_walking_left = true;

    private int counter_left = 0;
    private int counter_right = 0;

    private static Player player = null;

    public static Player getPlayer(){
        if(player == null)
            player = new Player();
        return player;
    }

    public Player(){
        score = 0;
        lifeCount = 3;
    }

    public void setPlayerBody(Body body){
        playerBody = body;
        preposition = playerBody.getPosition();
    }

    public void moveRight() {
        if (is_enable_walking_right) {
            float delta = Gdx.graphics.getDeltaTime() * 100;
            Vector2 direction = new Vector2(delta, 0);
            Vector2 newPosition = new Vector2(playerBody.getPosition());
            preposition = new Vector2(playerBody.getPosition());
            newPosition.add(direction);
            playerBody.setTransform(newPosition.x, newPosition.y, playerBody.getAngle());

            if(!isIs_enable_walking_left && counter_left > 2) {
                isIs_enable_walking_left = true;
                counter_left = 0;
            }else if(!isIs_enable_walking_left){
                ++ counter_left;
            }
        }

        //System.out.println("pos x : " + playerBody.getPosition().x);
    }

    public void moveLeft() {
        if(isIs_enable_walking_left) {
            float delta = Gdx.graphics.getDeltaTime() * 100;
            Vector2 direction = new Vector2(-delta, 0);
            Vector2 newPosition = new Vector2(playerBody.getPosition());
            preposition = new Vector2(playerBody.getPosition());
            newPosition.add(direction);
            playerBody.setTransform(newPosition, playerBody.getAngle());
            //playerBody.setTransform(newPosition.x,newPosition.y,playerBody.getAngle());
            if(!is_enable_walking_right &&  counter_right > 2) {
                is_enable_walking_right = true;
                counter_right = 0;

            }else if(!is_enable_walking_right){
                counter_right += 1;
            }
        }
      //  System.out.println("pos x : " + playerBody.getPosition().x);

    }

    public void createPlayer(World world){
        BodyDef bdef = new BodyDef();
        bdef.position.set(320 / GameScreenManager.PPM_W, 110  / GameScreenManager.PPM_H );
        bdef.type = BodyDef.BodyType.DynamicBody;
        Body body = world.createBody(bdef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(20 / GameScreenManager.PPM_W, 20 / GameScreenManager.PPM_H);
        FixtureDef fdef = new FixtureDef();
        fdef.shape = shape;
        fdef.filter.categoryBits = GameScreenManager.CollisionVar.BIT_PLAYER;
        fdef.filter.maskBits = GameScreenManager.CollisionVar.BIT_SCREEN | GameScreenManager.CollisionVar.BIT_BALL;
        body.createFixture(fdef).setUserData("player");
        Player.getPlayer().playerBody = body;
        Player.getPlayer().setPlayerBody(body);
        Player.getPlayer().width = (int) (64 / GameScreenManager.PPM_W);
        Player.getPlayer().height = (int) (64 / GameScreenManager.PPM_H);
        System.out.println("body pos : " + Player.getPlayer().playerBody.getPosition().x + " y : " + Player.getPlayer().playerBody.getPosition().y);
    }
}

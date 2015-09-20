package com.interview.game.Model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Transform;
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

            if(!isIs_enable_walking_left)
                isIs_enable_walking_left = true;
        }

        System.out.println("pos x : " + playerBody.getPosition().x);
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
            if(!is_enable_walking_right)
                is_enable_walking_right = true;
        }
        System.out.println("pos x : " + playerBody.getPosition().x);

    }
}

package com.interview.game.Model;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import java.util.ArrayList;

/**
 * Created by msatilmis on 30.08.2015.
 */
public class Ball {
    public Vector2 position;
    public Vector2 direction;
    private BallType ballType;

    public static Integer MAX_LEVEL_NUMBER = 4; /*4 different level ball*/


    public Ball(Vector2 pos, int level, Vector2 direction){
        ballType = new BallType(level,level*10);
        position = pos;
        this.direction = direction;
    }

    private Ball(Vector2 vector2,BallType ballType, Vector2 direction){
        this.ballType = ballType;
        position = vector2;
        this.direction = direction;
    }
    // create 2 balls  after break
    public ArrayList<Ball> createChildBall(){
        ArrayList<BallType> ballTypes = ballType.createChildBall();
        ArrayList<Ball> balls = new ArrayList<Ball>();
        balls.add(new Ball(position,ballTypes.get(1),new Vector2(-1,-1)));
        balls.add(new Ball(position,ballTypes.get(2),new Vector2(1,-1)));

        return balls;
    }

    public void dispose(){
        ballType.dispose();
    }

    /*
        Simulated ball level
     */
    private class BallType{
        public int level;
        public int score;
        public Texture texture;

        public BallType(int level, int score){
            this.level = level;
            this.score = score;
        }
        // create 2 balls type after break
        public ArrayList<BallType> createChildBall(){
            if(this.level == 1)
                return null;

            ArrayList<BallType> ballList = new ArrayList<BallType>();
            ballList.add(new BallType(level-1,score/2));
            ballList.add(new BallType(level-1,score/2));
            return ballList;
        }

        public void dispose(){
            texture.dispose();
        }
    }
}

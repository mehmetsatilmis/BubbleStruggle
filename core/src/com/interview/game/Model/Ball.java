package com.interview.game.Model;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.interview.game.Screen.GameScreenManager;

import java.util.ArrayList;

/**
 * Created by msatilmis on 30.08.2015.
 */
public class Ball {
    public Vector2 position;
    public Vector2 direction;
    public Vector2 length;
    public Body body;
    public Texture texture;
    private BallType ballType;


    public static Integer MAX_LEVEL_NUMBER = 4; /*4 different level ball*/


    public Ball(Vector2 pos, int level, Vector2 direction,Body body){
        ballType = new BallType(level,level*10);
        position = pos;
        this.direction = direction;
        //texture = FileManager.getManager().getTexture(ballType.texturePath,ballType.ballName);
    }
    public Ball(Vector2 vector2,Vector2 len,BallType ballType, Vector2 direction){
        this.ballType = ballType;
        position = vector2;
        this.direction = direction;
        length = len;
        //texture = FileManager.getManager().getTexture(ballType.texturePath,ballType.ballName);
    }

    private Ball(Vector2 vector2,BallType ballType, Vector2 direction){
        this.ballType = ballType;
        position = vector2;
        this.direction = direction;
    }
    // create 2 balls  after break
    public ArrayList<Ball> createChildBall(){
        ArrayList<BallType> ballTypes = ballType.createChildBall();
        ArrayList<Ball> balls = new ArrayList<Ball>(3);
        if(ballTypes != null) {
            balls.add(new Ball(new Vector2(body.getPosition()), new Vector2(length),ballTypes.get(0), new Vector2(-1, -1)));
            balls.add(new Ball(new Vector2(body.getPosition()), new Vector2(length) ,ballTypes.get(1), new Vector2(1, -1)));
        }
        return balls;
    }

    public void dispose(){
        ballType.dispose();
    }

    public int getLevel(){
        return ballType.level;
    }

    public String getBallName(){
        return ballType.ballName;
    }

    public String getTexturePath(){
        return ballType.texturePath;
    }
    /*
        Simulated ball level
     */
    private class BallType{
        public int level;
        public int score;
        public String texturePath;
        public String ballName;

        public BallType(int level, int score){
            this.level = level;
            this.score = score;
            Vector2 length_local;

            //using same length for each child ball
            if(level == 1){
                texturePath = com.interview.game.Manager.FileManager.BLUE_BALL;
                ballName = "blueball";
                length = new Vector2(4 / GameScreenManager.PPM_W,4 / GameScreenManager.PPM_H);
            }else if(level == 2){
                texturePath = com.interview.game.Manager.FileManager.GREEN_BALL;
                ballName = "greenball";
                length = new Vector2(8 / GameScreenManager.PPM_W,8 / GameScreenManager.PPM_H);

            }else if(level == 3){
                texturePath = com.interview.game.Manager.FileManager.PINK_BALL;
                ballName = "pinkball";
                length = new Vector2(16 / GameScreenManager.PPM_W,16 / GameScreenManager.PPM_H);

            }else{
                texturePath = com.interview.game.Manager.FileManager.BLUE_BALL;
                ballName = "blueball";
                length = new Vector2(8 / GameScreenManager.PPM_W,8 /GameScreenManager.PPM_H);
            }
        }
        // create 2 balls type after break
        public ArrayList<BallType> createChildBall(){
            if(this.level == 1)
                return null;

            ArrayList<BallType> ballList = new ArrayList<BallType>(3);
            ballList.add(new BallType(level-1,score/2));
            ballList.add(new BallType(level-1,score/2));
            return ballList;
        }

        public void dispose(){

        }
    }
}

package com.interview.game.Operation;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.math.Vector2;
import com.interview.game.GameScreenManager;
import com.interview.game.Model.Ball;
import com.interview.game.Operation.CallBack.CreateBallCallBackAbstract;
import com.interview.game.State.PlayState;

import java.util.Random;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by msatilmis on 30.08.2015.
 */
public class CreateBall implements Runnable {

    private CreateBallCallBackAbstract callBack;
    private long delay;
    private ScheduledFuture<?> timer;
    private Vector2 default_position = new Vector2(320/ GameScreenManager.PPM_W, 380/GameScreenManager.PPM_H);

    public void runCreateBall(){
        ScheduledThreadPoolExecutor exec = new ScheduledThreadPoolExecutor(1);
        timer = exec.scheduleAtFixedRate(this,0,delay, TimeUnit.SECONDS);
    }

    public void endOfTimer(){
        timer.cancel(true);
    }

    public CreateBall(long delay,CreateBallCallBackAbstract callBack){
        this.callBack = callBack;
        this.delay = delay;
    }

    @Override
    public void run() {
       createBall();
    }

    private void createBall(){
        Random random = new Random();

        callBack.onBallCreate(new Ball(default_position,random.nextInt(PlayState.MAX_LEVEL_NUMBER-1)+1));
    }
}

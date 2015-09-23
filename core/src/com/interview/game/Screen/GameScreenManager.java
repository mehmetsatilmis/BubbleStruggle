package com.interview.game.Screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.interview.game.Manager.AnimationManager;
import com.interview.game.Manager.FileManager;
import com.interview.game.Model.Ball;
import com.interview.game.Model.Player;
import com.interview.game.Model.Weapon;
import com.interview.game.Operation.CallBack.BallCallBack;
import com.interview.game.Operation.CreateBall;
import com.interview.game.State.PlayState;

import java.util.ArrayList;


/**
 * Created by msatilmis on 29.08.2015.
 *
 * Created for running game screen management, render and create player, weapon and balls
 */
public class GameScreenManager implements DrawableInterface {

    public static World world;
    private Box2DDebugRenderer box2DDebugRenderer;
    private OrthographicCamera orthoCam;

    public static int WIDTH;
    public static int HEIGHT;
    public static float PPM_W = 5; //just initialize , to resize coordination for each devices (width)
    public static float PPM_H = 5; //just initialize , to resize coordination for each devices (height)
    public float dt;
    public float passedTime;
    public Player player;
    public Weapon weapon;

    public CreateBall createBallClass;

    private int refereansWidth = 640; // each position about screen elements, calculated for 640x480
    private int refereansHeight = 480;
    private int referansTop = 400;  // game screen end y = 400 ,
    private int referansDown = 80;  // game screen begin y = 80,


    private boolean is_draw_background = false;

    public GameScreenManager(float step, World world) {
        this.world = world;
        dt = step;

        WIDTH = Gdx.graphics.getWidth();
        HEIGHT = Gdx.graphics.getHeight();

        float rate = (float) ((double) refereansWidth / (double) WIDTH);
        PPM_W = rate;
        PPM_H = (float) ((double) refereansHeight / (double) HEIGHT);
        // set up box2d cam
        orthoCam = new OrthographicCamera();
        orthoCam.setToOrtho(false, WIDTH, HEIGHT);
        box2DDebugRenderer = new Box2DDebugRenderer();
        player = Player.getPlayer();
        player.anim = AnimationManager.getInstance().getAnimation(AnimationManager.SPRITE_WALKING_RIGHT, "walkright");
        Weapon.getWeapon().anim = AnimationManager.getInstance().getAnimation(AnimationManager.WEAPON, "weapon");
    }

    public void setPlayerAnim(Animation anim) {
        Player.getPlayer().anim = anim;
    }

    public Player getPlayer() {
        return Player.getPlayer();
    }

    @Override
    public void create() {
        createLayout();
        createModel();
    }

    @Override
    public void update() {
        orthoCam.update();
        synchronized (world) {
            if (world == null)
                System.out.println("null");
            world.step(dt, 6, 2);
        }
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        // clear screen
        Gdx.gl.glClear(Gdx.gl.GL_COLOR_BUFFER_BIT);
        Texture texture = FileManager.getManager().getTexture(null, "background");
        spriteBatch.begin();

        //Background
        spriteBatch.draw(texture, startScreenX(), startScreenY(),
                getScreenWidth(),
                getScreenHeight());

        renderBalls(spriteBatch);
        renderPlayerInfo(spriteBatch);

        //player
        player = Player.getPlayer();
        TextureRegion textureRegion = player.anim.getKeyFrame(passedTime, true);
        TextureRegion textureRegion1 = Weapon.getWeapon().anim.getKeyFrame(passedTime, true);
        weapon = Weapon.getWeapon();

        spriteBatch.draw(textureRegion, player.playerBody.getPosition().x - player.width / 2,
                player.playerBody.getPosition().y - player.height / 2,
                player.width,
                player.height);

        //Weapon draw while actived by touch
        if (Weapon.getWeapon().isActive) {
            spriteBatch.draw(textureRegion1, weapon.weaponBody.getPosition().x - weapon.width / 2,
                    weapon.weaponBody.getPosition().y - weapon.height / 2,
                    weapon.width,
                    weapon.height);
        }else{
            if(Weapon.getWeapon().weaponBody != null)
                Weapon.getWeapon().weaponBody.setActive(false);
        }
        spriteBatch.end();
        // draw box2d world
        box2DDebugRenderer.render(world, orthoCam.combined);
    }

    private void renderBalls(SpriteBatch spriteBatch){
        ArrayList<Texture> textureList = new ArrayList<Texture>();
        ArrayList<Vector2> vector2ArrayList = new ArrayList<Vector2>();
        ArrayList<Vector2> levelList = new ArrayList<Vector2>();

        synchronized (PlayState.ballLinkedHashMap) {
            for (int i = 0; i < PlayState.ballLinkedHashMap.size(); ++i) {
                Ball ball = PlayState.ballLinkedHashMap.get(i);
                if(ball.body.isActive()) {
                    textureList.add(FileManager.getManager().getTexture(ball.getTexturePath(), ball.getBallName()));
                    vector2ArrayList.add(ball.body.getPosition());
                    levelList.add(ball.length);
                }
            }
            //balls
            for (int i = 0; i < textureList.size(); ++i) {
                float height = levelList.get(i).y / GameScreenManager.PPM_H;

                spriteBatch.draw(textureList.get(i), vector2ArrayList.get(i).x - height / 2, vector2ArrayList.get(i).y - height / 2,
                       height, height);
            }
        }
    }

    private void renderPlayerInfo(SpriteBatch spriteBatch){
        Texture texture = FileManager.getManager().getTexture(FileManager.RETRY_COUNT_IMAGE,"count");
        for(int i=0; i< Player.getPlayer().lifeCount; ++i){
            spriteBatch.draw(texture,getScreenWidth()- (30 * (i+1))/ GameScreenManager.PPM_W,20 / GameScreenManager.PPM_H,
                    30/ GameScreenManager.PPM_W,30/ GameScreenManager.PPM_H);
        }

        BitmapFont font = FileManager.getManager().getFont();
        font.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        font.draw(spriteBatch, "SCORE : " + Player.getPlayer().score ,50 / GameScreenManager.PPM_W,
                50 / GameScreenManager.PPM_H,(int) 30 / GameScreenManager.PPM_H,10,false);
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {
        createBallClass.endOfTimer();
    }

    @Override
    public void resume() {
        createBallClass.runCreateBall();
    }

    @Override
    public void dispose() {

    }

    private int startScreenX() {
        return 0;
    }

    private int startScreenY() {
        return (int) (referansDown / GameScreenManager.PPM_H);
    }

    private int getScreenWidth() {
        return (int) (refereansWidth / GameScreenManager.PPM_W);
    }

    private int getScreenHeight() {
        return (int) ((referansTop - referansDown) / GameScreenManager.PPM_H);
    }

    /*Boundaries game screen*/
    private void createLayout() {
        //down bound
        BodyDef bdef = new BodyDef();
        bdef.position.set(0 / GameScreenManager.PPM_W, referansDown / GameScreenManager.PPM_H);
        bdef.type = BodyDef.BodyType.StaticBody;
        Body body = world.createBody(bdef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(GameScreenManager.WIDTH, 1);
        FixtureDef fdef = new FixtureDef();
        fdef.shape = shape;
        fdef.filter.categoryBits = CollisionVar.BIT_SCREEN;
        fdef.filter.maskBits = CollisionVar.BIT_SCREEN | CollisionVar.BIT_BALL | CollisionVar.BIT_PLAYER;

        body.createFixture(fdef).setUserData("down");
        //top bound
        bdef.position.set(0 / GameScreenManager.PPM_W, referansTop / GameScreenManager.PPM_H);
        bdef.type = BodyDef.BodyType.StaticBody;
        body = world.createBody(bdef);

        shape.setAsBox(GameScreenManager.WIDTH, 1);
        fdef.shape = shape;
        fdef.filter.categoryBits = CollisionVar.BIT_SCREEN;
        fdef.filter.maskBits = CollisionVar.BIT_SCREEN | CollisionVar.BIT_BALL | CollisionVar.BIT_PLAYER;

        body.createFixture(fdef).setUserData("top");


        //left bound
        bdef.position.set(0 / GameScreenManager.PPM_W, 80 / GameScreenManager.PPM_H);
        bdef.type = BodyDef.BodyType.StaticBody;
        body = world.createBody(bdef);

        shape.setAsBox(0, GameScreenManager.HEIGHT);
        fdef.shape = shape;
        fdef.filter.categoryBits = CollisionVar.BIT_SCREEN;
        fdef.filter.maskBits = CollisionVar.BIT_SCREEN | CollisionVar.BIT_BALL | CollisionVar.BIT_PLAYER;
        body.createFixture(fdef).setUserData("left");

        //right bound
        bdef.position.set(GameScreenManager.WIDTH + 1, 0 / GameScreenManager.PPM_H);
        bdef.type = BodyDef.BodyType.StaticBody;
        body = world.createBody(bdef);
        fdef.filter.categoryBits = CollisionVar.BIT_SCREEN;
        fdef.filter.maskBits = CollisionVar.BIT_SCREEN | CollisionVar.BIT_BALL | CollisionVar.BIT_PLAYER;

        shape.setAsBox(0, GameScreenManager.HEIGHT);
        fdef.shape = shape;
        body.createFixture(fdef).setUserData("right");


    }

    /*create balls and player*/
    private void createModel() {
        createBallClass = new CreateBall(10, new BallCallBack() {
            @Override
            public void onBallCreate(Ball ball) {
                createBall(ball);
            }
        });

        createPlayer();
        createBallClass.runCreateBall();
        createWeapon();
    }

    public static void createBall(Ball ball) {
        int index = 0;

        BodyDef bdef = new BodyDef();
        Vector2 ballPosition = new Vector2(ball.position.x + ball.length.x / 2,
                ball.position.y + ball.length.y / 2);
        bdef.position.set(ball.position);
        bdef.type = BodyDef.BodyType.DynamicBody;
        Body body;
        synchronized (world) {
             body = world.createBody(bdef);
        }
        CircleShape shape = new CircleShape();
        System.out.println("length : " + ball.length.y);
        shape.setRadius(ball.length.y);

        synchronized (PlayState.ballLinkedHashMap) {
            ball.body = body;
            PlayState.ballLinkedHashMap.put(PlayState.ballLinkedHashMap.size(), ball);
            index = PlayState.ballLinkedHashMap.size();

            FixtureDef fdef = new FixtureDef();
            fdef.shape = shape;
            fdef.restitution = (float) 1.1;
            fdef.filter.categoryBits = CollisionVar.BIT_BALL;
            fdef.filter.maskBits = CollisionVar.BIT_SCREEN | CollisionVar.BIT_PLAYER | CollisionVar.BIT_WEAPON;

            ball.body.createFixture(fdef).setUserData("ball" + (index - 1));
        }

        float angle = (float) (Math.atan2(ball.direction.y, ball.direction.x));
        ball.body.setLinearVelocity(new Vector2(45 * MathUtils.cos(angle), 45 * MathUtils.sin(angle)));


    }

    public static void createPlayer() {
        Player.getPlayer().createPlayer(world);
    }

    private void createWeapon() {
        if (Weapon.getWeapon().isActive)
            Weapon.getWeapon().createWeapon(world);
    }

    public static class CollisionVar {
        // category bits
        public static final short BIT_SCREEN = 2;
        public static final short BIT_BALL = 4;
        public static final short BIT_WEAPON = 8;
        public static final short BIT_PLAYER = 16;
    }
}

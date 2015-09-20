package com.interview.game.Screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
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
import com.interview.game.Model.Ball;
import com.interview.game.Manager.FileManager;
import com.interview.game.Model.Player;
import com.interview.game.Model.Weapon;
import com.interview.game.Operation.CallBack.BallCallBack;
import com.interview.game.Operation.CreateBall;
import com.interview.game.State.PlayState;

import java.util.ArrayList;


/**
 * Created by msatilmis on 29.08.2015.
 */
public class GameScreenManager implements DrawableInterface {

    private World world;
    private Box2DDebugRenderer box2DDebugRenderer;
    private OrthographicCamera orthoCam;

    public static int WIDTH;
    public static int HEIGHT;
    public static float PPM_W = 5;
    public static float PPM_H = 5;
    public float dt;
    public float passedTime;
    public Player player;
    public Weapon weapon;

    public CreateBall createBallClass;

    private int refereansWidth = 640;
    private int refereansHeight = 480;
    private int referansTop = 400;
    private int referansDown = 80;

    private boolean is_draw_background = false;

    public GameScreenManager(float step, World world){
        this.world = world;
        dt = step;

        WIDTH  = Gdx.graphics.getWidth();
        HEIGHT = Gdx.graphics.getHeight();

        float rate = (float) ((double) refereansWidth / (double) WIDTH);
        PPM_W = rate;
        PPM_H = (float) ((double) refereansHeight / (double) HEIGHT);
        // set up box2d cam
        orthoCam = new OrthographicCamera();
        orthoCam.setToOrtho(false, WIDTH  , HEIGHT );
        box2DDebugRenderer = new Box2DDebugRenderer();
        player = Player.getPlayer();
        player.anim = AnimationManager.getInstance().getAnimation(AnimationManager.SPRITE_WALKING_RIGHT, "walkright");
        Weapon.getWeapon().anim = AnimationManager.getInstance().getAnimation(AnimationManager.WEAPON,"weapon");
    }

    public void setPlayerAnim(Animation anim){
        Player.getPlayer().anim = anim;
    }

    public void WeaponAnim(Animation anim){
        Player.getPlayer().anim = anim;
    }

    public Player getPlayer(){
        return Player.getPlayer();
    }

    public Weapon getWeapon(){
        return Weapon.getWeapon();
    }

    @Override
    public void create() {
        createLayout();
        createModel();
    }

    @Override
    public void update() {
        orthoCam.update();
        world.step(dt, 6, 2);
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        // clear screen
        Gdx.gl.glClear(Gdx.gl.GL_COLOR_BUFFER_BIT);
        Texture texture = FileManager.getManager().getTexture(null, "background");
        ArrayList<Texture> textureList = new ArrayList<Texture>();
        ArrayList<Vector2> vector2ArrayList = new ArrayList<Vector2>();
        ArrayList<Vector2> levelList = new ArrayList<Vector2>();

        for(int i= 0; i<PlayState.balls.size(); ++i){
            Ball ball = PlayState.balls.get(i);
            textureList.add(FileManager.getManager().getTexture(ball.getTexturePath(),ball.getBallName()));
            vector2ArrayList.add(ball.body.getPosition());
            levelList.add(ball.length);
        }


        spriteBatch.begin();
        //Background
        spriteBatch.draw(texture, startScreenX(), startScreenY(),
                getScreenWidth(),
                getScreenHeight());
        //balls
        for(int i = 0; i<textureList.size();++i){
            float height = levelList.get(i).y /GameScreenManager.PPM_H;
            spriteBatch.draw(textureList.get(i),vector2ArrayList.get(i).x,vector2ArrayList.get(i).y - height /2,
                    levelList.get(i).x / GameScreenManager.PPM_W,height);
        }

        //player
        player = Player.getPlayer();
        TextureRegion textureRegion = player.anim.getKeyFrame(passedTime, true);
        TextureRegion textureRegion1 = Weapon.getWeapon().anim.getKeyFrame(passedTime,true);
        weapon = Weapon.getWeapon();

        spriteBatch.draw(textureRegion, player.playerBody.getPosition().x, player.playerBody.getPosition().y, player.width,player.height);
        spriteBatch.draw(textureRegion1, weapon.weaponBody.getPosition().x, weapon.weaponBody.getPosition().y, weapon.width,weapon.height);

        spriteBatch.end();
        // draw box2d world
        box2DDebugRenderer.render(world, orthoCam.combined);
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

    private int startScreenX(){
        return 0;
    }

    private int startScreenY(){
        return (int) (referansDown / GameScreenManager.PPM_H);
    }

    private int getScreenWidth(){
        return (int) (refereansWidth / GameScreenManager.PPM_W);
    }

    private int getScreenHeight(){
        return (int) ((referansTop-referansDown) / GameScreenManager.PPM_H);
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
        fdef.filter.maskBits = CollisionVar.BIT_SCREEN | CollisionVar.BIT_BALL| CollisionVar.BIT_PLAYER;

        body.createFixture(fdef).setUserData("top");


        //left bound
        bdef.position.set(0 / GameScreenManager.PPM_W, 80 / GameScreenManager.PPM_H);
        bdef.type = BodyDef.BodyType.StaticBody;
        body = world.createBody(bdef);

        shape.setAsBox(0, GameScreenManager.HEIGHT);
        fdef.shape = shape;
        fdef.filter.categoryBits = CollisionVar.BIT_SCREEN;
        fdef.filter.maskBits = CollisionVar.BIT_SCREEN | CollisionVar.BIT_BALL| CollisionVar.BIT_PLAYER;
        body.createFixture(fdef).setUserData("left");

        //right bound
        bdef.position.set(GameScreenManager.WIDTH + 1, 0 / GameScreenManager.PPM_H);
        bdef.type = BodyDef.BodyType.StaticBody;
        body = world.createBody(bdef);
        fdef.filter.categoryBits = CollisionVar.BIT_SCREEN;
        fdef.filter.maskBits = CollisionVar.BIT_SCREEN | CollisionVar.BIT_BALL| CollisionVar.BIT_PLAYER;

        shape.setAsBox(0, GameScreenManager.HEIGHT);
        fdef.shape = shape;
        body.createFixture(fdef).setUserData("right");


    }

    /*create balls and player*/
    private void createModel() {
        createBallClass = new CreateBall(10, new BallCallBack() {
            @Override
            public void onBallCreate(Ball ball) {
                synchronized (world) {
                    int index = 0;

                    BodyDef bdef = new BodyDef();
                    bdef.position.set(ball.position);
                    bdef.type = BodyDef.BodyType.DynamicBody;
                    Body body = world.createBody(bdef);
                    CircleShape shape = new CircleShape();
                    shape.setRadius(10 * ball.getLevel() / GameScreenManager.PPM_H);

                    synchronized (PlayState.balls) {
                        ball.body = body;
                        PlayState.balls.add(ball);
                        index = PlayState.balls.size();
                        FixtureDef fdef = new FixtureDef();
                        fdef.shape = shape;
                        fdef.restitution = (float) 1.1;
                        fdef.filter.categoryBits = CollisionVar.BIT_BALL;
                        fdef.filter.maskBits = CollisionVar.BIT_SCREEN | CollisionVar.BIT_PLAYER | CollisionVar.BIT_WEAPON;
                        body.createFixture(fdef).setUserData("" + index);
                    }

                    float angle = (float) (Math.atan2(ball.direction.y, ball.direction.x));
                    body.setLinearVelocity(new Vector2(45 * MathUtils.cos(angle), 45 * MathUtils.sin(angle)));

                }

            }
        });

        createPlayer();
        createBallClass.runCreateBall();
        createWeapon();
    }


    private void createPlayer() {
        BodyDef bdef = new BodyDef();
        bdef.position.set((320 - (64 /2)) / GameScreenManager.PPM_W, (118 - 64/2) / GameScreenManager.PPM_H );
        bdef.type = BodyDef.BodyType.DynamicBody;
        Body body = world.createBody(bdef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(5 / GameScreenManager.PPM_W, 5 / GameScreenManager.PPM_H);
        FixtureDef fdef = new FixtureDef();
        fdef.shape = shape;
        fdef.filter.categoryBits = CollisionVar.BIT_PLAYER;
        fdef.filter.maskBits = CollisionVar.BIT_SCREEN | CollisionVar.BIT_BALL;
        body.createFixture(fdef).setUserData("player");
        Player.getPlayer().playerBody = body;
        Player.getPlayer().setPlayerBody(body);
        Player.getPlayer().width = (int) (64 / GameScreenManager.PPM_W);
        Player.getPlayer().height = (int) (64 / GameScreenManager.PPM_H);
        System.out.println("body pos : " + Player.getPlayer().playerBody.getPosition().x + " y : " + Player.getPlayer().playerBody.getPosition().y);
    }

    private void createWeapon(){
        BodyDef bdef = new BodyDef();
        bdef.position.set((320 - (64 /2)) / GameScreenManager.PPM_W, (118 - 64/2) / GameScreenManager.PPM_H );
        bdef.type = BodyDef.BodyType.DynamicBody;
        Body body = world.createBody(bdef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(5 / GameScreenManager.PPM_W, 5 / GameScreenManager.PPM_H);
        FixtureDef fdef = new FixtureDef();
        fdef.shape = shape;
        fdef.filter.categoryBits = CollisionVar.BIT_WEAPON;
        fdef.filter.maskBits = CollisionVar.BIT_SCREEN | CollisionVar.BIT_BALL;
        body.createFixture(fdef).setUserData("weapon");
        Weapon.getWeapon().weaponBody = body;
        Weapon.getWeapon().width = (int) (64 / GameScreenManager.PPM_W);
        Weapon.getWeapon().height = (int) (64 / GameScreenManager.PPM_H);

    }

    private class CollisionVar {
        // category bits
        public static final short BIT_SCREEN = 2;
        public static final short BIT_BALL = 4;
        public static final short BIT_WEAPON = 8;
        public static final short BIT_PLAYER = 16;
    }
}

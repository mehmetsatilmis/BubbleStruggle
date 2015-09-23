package com.interview.game.Model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.interview.game.Screen.GameScreenManager;

/**
 * Created by msatilmis on 18/09/15.
 */
public class Weapon {
    public Animation anim;
    public Body weaponBody;     //position tracking and collision listener
    public Vector2 preposition;
    public int width;
    public int height;
    public boolean isActive = false; // control flag for just one created weapon for each scene

    private static Weapon weapon = null;

    public static Weapon getWeapon() {
        if (weapon == null)
            weapon = new Weapon();
        return weapon;
    }

    /*
        Shoot action and control for
     */
    public void shoot() {

        if(!isActive) {
            isActive = true;
            createWeapon(GameScreenManager.world);
        }
        float delta = Gdx.graphics.getDeltaTime() * 100;
        Vector2 direction = new Vector2(0, delta);
        //height += delta;
        Vector2 newPosition = new Vector2(weaponBody.getPosition());
        preposition = new Vector2(weaponBody.getPosition());
        newPosition.add(direction);
        weaponBody.setTransform(newPosition.x, newPosition.y, weaponBody.getAngle());

        //destroyFixture for getting taller about weapon's shape
        Array<Fixture> arr = weaponBody.getFixtureList();
        if(arr.size > 0)
            weaponBody.destroyFixture(arr.first());

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(15, 30); //TODO hard codded for shape , weapon don't be getting taller
        FixtureDef fdef = new FixtureDef();
        fdef.shape = shape;
        fdef.filter.categoryBits = GameScreenManager.CollisionVar.BIT_PLAYER;
        fdef.filter.maskBits = GameScreenManager.CollisionVar.BIT_SCREEN | GameScreenManager.CollisionVar.BIT_BALL;
        weaponBody.createFixture(fdef).setUserData("weapon");
    }

    /*
       Create weapon with player position
     */
    public void createWeapon(World world){
        BodyDef bdef = new BodyDef();
        bdef.position.set(Player.getPlayer().playerBody.getPosition().x,
                Player.getPlayer().playerBody.getPosition().y + 10);
        bdef.type = BodyDef.BodyType.DynamicBody;
        synchronized (world){
            System.out.println("**************************");
            weaponBody = world.createBody(bdef);
        }

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(15 / GameScreenManager.PPM_W, 25 / GameScreenManager.PPM_H);
        FixtureDef fdef = new FixtureDef();
        fdef.shape = shape;
        fdef.filter.categoryBits = GameScreenManager.CollisionVar.BIT_PLAYER;
        fdef.filter.maskBits = GameScreenManager.CollisionVar.BIT_SCREEN | GameScreenManager.CollisionVar.BIT_BALL;
        weaponBody.createFixture(fdef).setUserData("weapon");
        Weapon.getWeapon().width = (int) (20 / GameScreenManager.PPM_W);
        Weapon.getWeapon().height = (int) (35 / GameScreenManager.PPM_H);

    }

}

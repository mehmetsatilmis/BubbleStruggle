package com.interview.game.Model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

/**
 * Created by airties on 18/09/15.
 */
public class Weapon {
    public Animation anim;
    public Body weaponBody;
    public Vector2 preposition;
    public int width;
    public int height;
    public boolean isActive = false;

    private static Weapon weapon = null;

    public static Weapon getWeapon() {
        if (weapon == null)
            weapon = new Weapon();
        return weapon;
    }

    public void shoot() {

        isActive = true;

        float delta = Gdx.graphics.getDeltaTime() * 100;
        Vector2 direction = new Vector2(delta, 0);
        Vector2 newPosition = new Vector2(weaponBody.getPosition());
        preposition = new Vector2(weaponBody.getPosition());
        newPosition.add(direction);
        weaponBody.setTransform(newPosition.x, newPosition.y, weaponBody.getAngle());

    }


}

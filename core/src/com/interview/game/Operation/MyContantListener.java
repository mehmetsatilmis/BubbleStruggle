package com.interview.game.Operation;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.interview.game.Model.Player;
import com.interview.game.Model.Weapon;
import com.interview.game.Operation.CallBack.WeaponActionCallBackAbstract;

/**
 * Created by msatilmis on 30.08.2015.
 */
public class MyContantListener implements ContactListener {

    private WeaponActionCallBackAbstract callBack;

    public MyContantListener(WeaponActionCallBackAbstract callBack) {
        this.callBack = callBack;
    }

    @Override
    public void beginContact(Contact contact) {
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();
        System.out.println("fa " + fa.getUserData() + " fb : " + fb.getUserData());
        if (fa.getUserData().equals("player") || fb.getUserData().equals("player")) {
            Body playerBody = null;
            boolean is_first = false;

            if (fa.getUserData().equals("player")) {
                playerBody = fa.getBody();
                is_first = true;
            } else {
                playerBody = fb.getBody();
            }

            if (is_first) {
                if (fb.getUserData().equals("left") || fb.getUserData().equals("right")) {
                    if (fb.getUserData().equals("left")) {
                        synchronized (Player.getPlayer()) {
                            Player.getPlayer().is_enable_walking_right = true;
                            Player.getPlayer().isIs_enable_walking_left = false;
                        }
                    } else {
                        synchronized (Player.getPlayer()) {
                            Player.getPlayer().is_enable_walking_right = false;
                            Player.getPlayer().isIs_enable_walking_left = true;
                        }
                    }
                }
            } else {
                System.out.println(" fb : " + fb.getUserData());
                if (fa.getUserData().equals("left") || fa.getUserData().equals("right")) {
                    if (fa.getUserData().equals("left")) {
                        synchronized (Player.getPlayer()) {
                            Player.getPlayer().is_enable_walking_right = true;
                            Player.getPlayer().isIs_enable_walking_left = false;
                        }
                    } else {
                        synchronized (Player.getPlayer()) {
                            Player.getPlayer().is_enable_walking_right = false;
                            Player.getPlayer().isIs_enable_walking_left = true;
                        }
                    }

                }
            }


        } else if (fa.getUserData().equals("weapon") || fb.getUserData().equals("weapon")) {
            boolean is_first = false;
            if (fa.getUserData().equals("weapon")) {
                is_first = true;
            }

            if (is_first) {
                if (fb.getUserData().equals("top")) {
                    synchronized (Weapon.getWeapon()) {
                        Weapon.getWeapon().isActive = false;
                    }
                } else if (fb.getUserData().toString().contains("ball")) {
                    int index = fb.getUserData().toString().indexOf("ball");
                    index = Integer.parseInt(fb.getUserData().toString().substring(index + 4));
                    callBack.onResponse(index);
                    synchronized (Weapon.getWeapon()) {
                        Weapon.getWeapon().isActive = false;
                    }
                }

            } else {
                if (fa.getUserData().equals("top")) {
                    synchronized (Weapon.getWeapon()) {
                        Weapon.getWeapon().isActive = false;
                    }
                } else if (fa.getUserData().toString().contains("ball")) {
                    int index = fa.getUserData().toString().indexOf("ball");
                    index = Integer.parseInt(fa.getUserData().toString().substring(index + 4));
                    callBack.onResponse(index);
                    synchronized (Weapon.getWeapon()) {
                        Weapon.getWeapon().isActive = false;
                    }
                }
            }

        } else {
            float angle = 0;
            Body body;

            if (fa.getUserData().equals("down") || fa.getUserData().equals("top") || fa.getUserData().equals("left") || fa.getUserData().equals("right")) {
                body = fb.getBody();
            } else {
                body = fa.getBody();
            }

            /*change direction about balls*/
            if (fa.getUserData().equals("down") || fb.getUserData().equals("down")) {
                body.getLinearVelocity().y *= -1;
                angle = (float) (Math.atan2(body.getLinearVelocity().y, body.getLinearVelocity().x));
            } else if (fa.getUserData().equals("top") || fb.getUserData().equals("top")) {
                body.getLinearVelocity().y *= -1;
                angle = (float) (Math.atan2(body.getLinearVelocity().y, body.getLinearVelocity().x));
            } else if (fa.getUserData().equals("left") || fb.getUserData().equals("left")) {
                body.getLinearVelocity().x *= -1;
                angle = (float) (Math.atan2(body.getLinearVelocity().y, body.getLinearVelocity().x));
            } else if (fa.getUserData().equals("right") || fb.getUserData().equals("right")) {
                body.getLinearVelocity().x *= -1;
                angle = (float) (Math.atan2(body.getLinearVelocity().y, body.getLinearVelocity().x));
            }
            //changing  velocity value
            body.setLinearVelocity(new Vector2(45 * MathUtils.cos(angle), 45 * MathUtils.sin(angle)));
        }

    }


    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}

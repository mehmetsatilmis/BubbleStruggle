package com.interview.game.Operation;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.interview.game.Model.Ball;
import com.interview.game.State.PlayState;

/**
 * Created by msatilmis on 30.08.2015.
 */
public class MyContantListener implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();

        if (fa.getUserData().equals("player") || fb.getUserData().equals("player")) {

        } else {
            synchronized (contact) {
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
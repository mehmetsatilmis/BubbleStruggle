package com.interview.game.Manager;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.interview.game.Model.Weapon;

/**
 * Created by msatilmis on 30.08.2015.
 */
public class InputManager {
    /*0 : left , 1: stay, 2: right*/
    public static int walking_type = 0;
    public static boolean isTouched = false;

    public void update() {
        Application.ApplicationType appType = Gdx.app.getType();
        switch (appType) {
            case Android:
            case iOS:
                mobileDevicesControl();
                break;
            default:
                otherDevicesControl();
                break;
        }
    }

    /**
     * Ios,Android *
     */
    private void mobileDevicesControl() {

        float newXPos = 0;

        Input.Orientation nativeOrientation = Gdx.input.getNativeOrientation();

        switch (nativeOrientation) {
            case Landscape:
                newXPos = Gdx.input.getAccelerometerX();
                break;

            default:

                newXPos = Gdx.input.getAccelerometerY();
                break;
        }


        if (newXPos > 0) {
            walking_type = 2;

        } else if (newXPos < 0) {
            walking_type = 0;

        } else
            walking_type = 1;

        /** listen for bullet **/
        if (Gdx.input.isTouched()) {
            isTouched = true;
            if(!Weapon.getWeapon().isActive)
                synchronized (Weapon.getWeapon()){
                    Weapon.getWeapon().shoot();
                }
        } else
            isTouched = false;



    }

    /**
     * Desktop,Web Browser ...
     */
    private void otherDevicesControl() {


        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            walking_type = 2;

        } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            walking_type = 0;
        } else
            walking_type = 1;


        /** listen for bullet **/
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            isTouched = true;
        }else{
            isTouched = false;
        }

    }
}

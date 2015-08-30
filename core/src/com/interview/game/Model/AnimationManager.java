package com.interview.game.Model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;

/**
 * Created by msatilmis on 30.08.2015.
 */
public class AnimationManager {
    private static AnimationManager animationManager = null;
    private LinkedHashMap<String ,Animation> animList;

    /** Animations paths **/
    public static final String SPRITE_WALKING_RIGHT = "atlases/right-walking.atlas";
    public static final String SPRITE_WALKING_LEFT = "atlases/left-walking.atlas";
    public static final String SPRITE_WALKING_FORWARD = "atlases/forward-walking.atlas";
    public static final String WEAPON = "atlases/weapon.atlas";

    private AnimationManager(){
        animList = new LinkedHashMap<String,Animation>();
    }

    public static AnimationManager getManager(){
        if(animationManager == null){
            animationManager = new AnimationManager();
        }
        return  animationManager;
    }

    public Animation loadAtlasAnimation(String path,String key) {

        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal(path));

        Array<TextureAtlas.AtlasRegion> regions = atlas.getRegions();
        if (regions == null)
            return null;
        float duration = 1 / (float) regions.size;
        Animation animation = new Animation(duration, regions);

        return animation;
    }

    public Animation getAnimation(String key){
        return animList.get(key);
    }


    public void dispose(){
        Set<String> key = animList.keySet();
        Iterator<String> itr = key.iterator();

        while (itr.hasNext()){
            animList.get(itr.next());
        }

    }

}

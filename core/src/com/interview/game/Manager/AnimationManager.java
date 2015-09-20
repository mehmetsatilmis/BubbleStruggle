
package com.interview.game.Manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.Array;

import java.util.LinkedHashMap;


public class AnimationManager {

	/** Animations paths **/
	public static final String SPRITE_WALKING_RIGHT = "atlases/right-walking.atlas";
	public static final String SPRITE_WALKING_LEFT = "atlases/left-walking.atlas";
	public static final String SPRITE_WALKING_FORWARD = "atlases/forward-walking.atlas";
	public static final String WEAPON = "atlases/weapon.atlas";

	private static AnimationManager animationManager;

	private LinkedHashMap<String, Animation> animationMap;

	private AnimationManager() {

		animationMap = new LinkedHashMap<String, Animation>();
	}

	public static AnimationManager getInstance() {

		if (animationManager == null)
			animationManager = new AnimationManager();

		return animationManager;
	}

	public Animation getAnimation(String path,String key) {

		if(path != null && animationMap.get(key) == null) {
			animationMap.put(key,loadAtlasAnimation(path));
		}

		return animationMap.get(key);
	}


	private Animation loadAtlasAnimation(String path) {

		TextureAtlas atlas = new TextureAtlas(Gdx.files.internal(path));

		Array<AtlasRegion> regions = atlas.getRegions();
		if (regions == null)
			return null;
		float duration = 1 / (float) regions.size;
		Animation animation = new Animation(duration, regions);

		return animation;
	}


}

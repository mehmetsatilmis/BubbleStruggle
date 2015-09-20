package com.interview.game.Manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;

/**
 * Created by msatilmis on 30.08.2015.
 */
public class FileManager {

    private LinkedHashMap<String ,Texture> textureList;
    private static FileManager fileManager = null;

    private FileManager(){
        textureList = new LinkedHashMap<String,Texture>();
    }

    /** Image Paths **/
    public static final String GREEN_BALL = "green_ball.png";
    public static final String PINK_BALL = "pink-ball.png";
    public static final String BLUE_BALL = "blue-ball.png";
    public static final String RETRY_COUNT_IMAGE = "retry-count.png";
    public static final String BACKGROUND_IMAGE = "background.jpg";

    /** Font Paths **/
    public static final String FONT_PATH = "fonts/font.fnt";
    public static final String FONT_IMAGE = "fonts/font.png";


    public static FileManager getManager(){
        if(fileManager == null){
            fileManager = new FileManager();
        }
        return  fileManager;
    }

    public Texture getTexture(String texturePath,String key) {

        if (texturePath != null && textureList.get(key) == null){
            Texture texture = new Texture(Gdx.files.internal(texturePath));
            textureList.put(key,texture);
            return texture;
        }

        if (textureList.get(key) != null)
            return textureList.get(key);
        else
            return null;

    }

    public void dispose(){
        Set<String> key = textureList.keySet();
        Iterator<String> itr = key.iterator();

        while (itr.hasNext()){
            textureList.get(itr.next()).dispose();
        }

    }
}

package com.interview.game.Model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import java.util.LinkedHashMap;

/**
 * Created by msatilmis on 30.08.2015.
 */
public class FileManager {

    private LinkedHashMap<String ,Texture> textureList;
    private FileManager fileManager = null;

    private FileManager(){
        textureList = new LinkedHashMap<String,Texture>();
    }

    public FileManager getManager(){
        if(fileManager == null){
            fileManager = new FileManager();
        }
        return  fileManager;
    }

    public Texture getTexture(String texturePath,String key) {

        if (texturePath == null)
            return null;

        if (textureList.get(key) != null)
            return textureList.get(key);

        Texture texture = new Texture(Gdx.files.internal(texturePath));
        textureList.put(key,texture);
        return texture;
    }
}

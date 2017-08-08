package com.eggheadgames.siren;

/**
 * Created by sriharshathulluri on 5/17/17.
 */

public class MySingleton {
    private static MySingleton instance;

    public static String path;

    private MySingleton() {}

    public static MySingleton instance()
    {
        //if no instance is initialized yet then create new instance
        //else return stored instance
        if (instance == null)
        {
            instance = new MySingleton();
        }
        return instance;
    }
}

package com.rajan.eliteeditor.storage;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

/**
 * Created by rajan.kali on 8/31/2017.
 * Database class
 */

@Database(entities = {Cache.class},version = 1)
public abstract class EliteDatabase extends RoomDatabase {
    private static EliteDatabase INSTANCE;
    public abstract EditorDao editorData();
    public static EliteDatabase getInstance(Context context){
        if(INSTANCE == null){
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),EliteDatabase.class,"elitedb")
                        .allowMainThreadQueries()
                        .build();
        }
        return INSTANCE;
    }
}

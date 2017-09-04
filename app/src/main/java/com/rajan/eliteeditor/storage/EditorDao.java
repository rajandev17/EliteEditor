package com.rajan.eliteeditor.storage;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

/**
 * Created by rajan.kali on 8/31/2017.
 * Data Access Object class for editor
 */

@Dao
public interface EditorDao {
    @Query("select * from Cache")
    Cache getCachedData();

    @Update
    void insertOrUpdateCache(Cache cache);
}

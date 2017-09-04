package com.rajan.eliteeditor.storage;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by rajan.kali on 8/31/2017.
 * Room Entity for saving editor data
 */

@Entity(tableName = "Cache")
public class Cache {

    @PrimaryKey(autoGenerate = true)
    private int uid;

    @ColumnInfo(name = "content")
    private String content;

    int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

package com.example.notes;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class NotesDBHelper extends SQLiteOpenHelper { // This class controls Version, Update Of DataBase, Creation of DataBase

    private static final String DB_NAME = "notes.db";
    private static final int DB_VERSION = 2; // Static in OOP will save its previous value. For example, ID should be static.
    // Because when we create new object it will just (id++) static will add 1 to previous value
    // Final is Constant Value we can not change its value in Code. We can just declare it and assign Value

    public NotesDBHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(NotesContract.NotesEntry.CREATE_COMMAND); // Execute SQL Code
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { // Upgrade DataBase when we change Version
        db.execSQL(NotesContract.NotesEntry.DROP_COMMAND);
        onCreate(db);
    }
}

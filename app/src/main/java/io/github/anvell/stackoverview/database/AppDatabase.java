package io.github.anvell.stackoverview.database;

import android.app.Application;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.support.annotation.NonNull;

import io.github.anvell.stackoverview.model.Question;

@Database(entities = {Question.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {

    private static final String DB_NAME = "StackOverview.db";
    private static AppDatabase instance;

    public abstract QuestionDao questionDao();

    public static AppDatabase get(@NonNull Application application) {
        if (instance == null) {
            synchronized (AppDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(application, AppDatabase.class, DB_NAME).build();
                }
            }
        }
        return instance;
    }

}
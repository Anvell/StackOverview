package io.github.anvell.stackoverview.database;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.ArrayList;
import java.util.List;

import io.github.anvell.stackoverview.model.Question;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface QuestionDao {

    @Insert(onConflict = REPLACE)
    void store(Question q);

    @Query("SELECT * FROM question WHERE questionId = :id")
    Question load(int id);

    @Query("SELECT * FROM question")
    LiveData<List<Question>> loadAll();

    @Query("DELETE FROM question WHERE questionId = :id")
    void delete(int id);

    @Delete
    void delete(Question q);

    @Query("DELETE FROM question")
    void deleteAll();
}

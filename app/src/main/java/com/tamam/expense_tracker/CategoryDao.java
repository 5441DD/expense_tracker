package com.tamam.expense_tracker;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface CategoryDao {

    @Query("SELECT * FROM Category")
    List<Category> listAll();

    @Insert
    void insertAll(List<Category> categories);

    @Insert
    void insertOne(Category category);

}



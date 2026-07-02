package com.tamam.expense_tracker;

import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

public interface CategoryDao {

    @Query("SELECT * FROM category")
    List<Category> listAll();

    @Insert
    void insertAll(List<Category> categories);

    @Insert
    void insertOne(Category category);

}



package com.tamam.expense_tracker;

import androidx.room.Query;

import java.util.List;

public interface CategoryDao {

    @Query("SELECT * FROM expense")
    List<Expense> listAll();
}

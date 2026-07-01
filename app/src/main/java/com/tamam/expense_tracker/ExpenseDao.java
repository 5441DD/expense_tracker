package com.tamam.expense_tracker;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ExpenseDao {

    @Query("SELECT * FROM expense")
    List<Expense> listAll();

    @Query("SELECT * FROM expense WHERE uid IN (:userIds)")
    List<Expense> loadAllByIds(int[] userIds);

    @Query("SELECT * FROM expense WHERE type LIKE type")
    Expense findByName(String type);

    @Insert
    void insertAll(List<Expense> expenses);
    @Delete
    void delete(Expense expense);

}

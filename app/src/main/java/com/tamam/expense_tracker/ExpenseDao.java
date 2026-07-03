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

    @Insert
    void insertAll(List<Expense> expenses);
    @Delete
    void delete(Expense expense);

    @Query("SELECT SUM(value)  FROM expense WHERE typeId = :categoryId")
    Integer sumExpensesbyId(int categoryId);

    @Insert
    void insertOne(Expense expense);

}

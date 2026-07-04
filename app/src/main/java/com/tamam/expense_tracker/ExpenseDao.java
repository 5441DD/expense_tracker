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

    // Sadece giderler
    @Query("SELECT SUM(value) FROM expense WHERE typeId = :categoryId AND isIncome = 0")
    Integer sumGiderById(int categoryId);

    // Sadece gelirler
    @Query("SELECT SUM(value) FROM expense WHERE typeId = :categoryId AND isIncome = 1")
    Integer sumGelirById(int categoryId);

    @Insert
    void insertOne(Expense expense);

}

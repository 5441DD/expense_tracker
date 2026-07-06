package com.tamam.expense_tracker;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.Date;
import java.util.List;

@Dao
public interface ExpenseDao {

    @Query("SELECT * FROM expense")
    List<Expense> listAll();

    @Insert
    void insertAll(List<Expense> expenses);
    @Delete
    void delete(Expense expense);

    @Query("SELECT * FROM expense WHERE isIncome = 0")
    List<Expense> listAllByGider();

    @Query("SELECT * FROM expense WHERE isIncome = 1")
    List<Expense> listAllByGelir();

    // Sadece giderler
    @Query("SELECT SUM(value) FROM expense WHERE typeId = :categoryId AND isIncome = 0")
    Integer sumGiderById(int categoryId);

    // Sadece gelirler
    @Query("SELECT SUM(value) FROM expense WHERE typeId = :categoryId AND isIncome = 1")
    Integer sumGelirById(int categoryId);

    @Insert
    void insertOne(Expense expense);

    @Query("SELECT * FROM expense WHERE date BETWEEN :startDate AND :endDate AND isIncome = :isIncome")
    List<Expense> listAllByDate(String startDate, String endDate, boolean isIncome);

    @Query("SELECT SUM(value) FROM expense WHERE typeId = :categoryId AND isIncome = :isIncome AND date BETWEEN :startDate AND :endDate")
    Integer sumByCategoryAndDateRange(int categoryId, boolean isIncome, String startDate, String endDate);

    @Query("SELECT * FROM expense WHERE isIncome = :isIncome AND date BETWEEN :startDate AND :endDate")
    List<Expense> listByDateRange(boolean isIncome, String startDate, String endDate);

}

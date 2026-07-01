package com.tamam.expense_tracker;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Expense.class},version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ExpenseDao expenseDao();
    public abstract CategoryDao categoryDao();
}

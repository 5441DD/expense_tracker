package com.tamam.expense_tracker;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Expense {

    @PrimaryKey(autoGenerate = true)
    public int id;


    @ColumnInfo(name = "typeId")
    public int typeId;


    @ColumnInfo(name = "value")
    public int value;


    @ColumnInfo(name = "date")
    public String date;


    @ColumnInfo(name = "note")
    public String note;

}

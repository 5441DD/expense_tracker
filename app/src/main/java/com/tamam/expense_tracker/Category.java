package com.tamam.expense_tracker;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Category {

    @PrimaryKey(autoGenerate = true)
    int id;

    @ColumnInfo(name = "type")
    String type;


}

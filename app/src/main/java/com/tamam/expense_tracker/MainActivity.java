package com.tamam.expense_tracker; // Eğer paket adın tam olarak buysa aynen kalsın

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {



    ExpenseDao expenseDao;
    CategoryDao categoryDao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /// ///
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /// ///

        /// ///
        /// ///

        //Database Baglantisi//

        AppDatabase db = Room.databaseBuilder(
                getApplicationContext(),
                AppDatabase.class,
                "expense_db"
        ).allowMainThreadQueries().build();

        expenseDao = db.expenseDao();
        categoryDao = db.categoryDao();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new ChartFragment())
                .commit();

        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);
        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selected;
            if (item.getItemId() == R.id.nav_chart) {
                selected = new ChartFragment();
            } else {
                selected = new ChartFragment(); // ListFragment henüz yok, şimdilik aynı
            }
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, selected)
                    .commit();
            return true;
        });

    }
}
package com.tamam.expense_tracker; // Eğer paket adın tam olarak buysa aynen kalsın

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.chip.ChipGroup;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {



    ExpenseDao expenseDao;
    CategoryDao categoryDao;

    public String startDate;
    public String endDate;

     void refreshFragment(){
         Fragment currentFragment= getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (currentFragment instanceof ChartFragment){
            ((ChartFragment)currentFragment).refresh();
        } else if (currentFragment instanceof ListFragment) {
            ((ListFragment)currentFragment).refresh();
        }


     }

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
        ///fragment ilk calistiginda filtre calismasi icin
        Calendar calendar1 = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", new Locale("tr"));

        calendar1.set(Calendar.DAY_OF_MONTH, 1);
        calendar1.set(Calendar.HOUR_OF_DAY, 0);
        calendar1.set(Calendar.MINUTE, 0);
        startDate = sdf.format(calendar1.getTime());

        calendar1.set(Calendar.DAY_OF_MONTH, calendar1.getActualMaximum(Calendar.DAY_OF_MONTH));
        calendar1.set(Calendar.HOUR_OF_DAY, 23);
        calendar1.set(Calendar.MINUTE, 59);
        endDate = sdf.format(calendar1.getTime());
        ///

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new ChartFragment())
                .commit();

        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);
        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selected;
            if (item.getItemId() == R.id.nav_chart) {
                selected = new ChartFragment();
            } else {
                selected = new ListFragment(); // ListFragment henüz yok, şimdilik aynı
            }
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, selected)
                    .commit();
            return true;
        });

        ChipGroup chipGroup= new ChipGroup(MainActivity.this);
        chipGroup=findViewById(R.id.chipGroup);

        chipGroup.setOnCheckedStateChangeListener((chipGroup1, list) ->{
            if (list.isEmpty())
                return;
            Calendar calendar = Calendar.getInstance();
            TextView textView = findViewById(R.id.dateRange);



            SimpleDateFormat dateFormatWHour = new SimpleDateFormat("yyyy-MM-dd HH:mm", new Locale("tr"));
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM", new Locale("tr"));

            Date startDateObj=null;
            Date endDateObj=null;


            int id=list.get(0);
            if (id==R.id.chipDay){
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                startDateObj = calendar.getTime();

                calendar.set(Calendar.HOUR_OF_DAY, 23);
                calendar.set(Calendar.MINUTE, 59);
                endDateObj = calendar.getTime();


            }else if (id==R.id.chipMonth){
                calendar.set(Calendar.DAY_OF_MONTH, 1);
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                startDateObj = calendar.getTime();

                calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
                calendar.set(Calendar.HOUR_OF_DAY, 23);
                calendar.set(Calendar.MINUTE, 59);
                endDateObj = calendar.getTime();

            }else if (id==R.id.chipYear){
                calendar.set(Calendar.DAY_OF_YEAR, 1);
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                startDateObj = calendar.getTime();

                calendar.set(Calendar.DAY_OF_YEAR, calendar.getActualMaximum(Calendar.DAY_OF_YEAR));
                calendar.set(Calendar.HOUR_OF_DAY, 23);
                calendar.set(Calendar.MINUTE, 59);
                endDateObj = calendar.getTime();

            }
            startDate = dateFormatWHour.format(startDateObj);
            endDate = dateFormatWHour.format(endDateObj);

            textView.setText("📅 " + dateFormat.format(startDateObj) + " - " + dateFormat.format(endDateObj));

            refreshFragment();

        });

    }
}
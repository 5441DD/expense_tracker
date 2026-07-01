package com.tamam.expense_tracker; // Eğer paket adın tam olarak buysa aynen kalsın

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    Integer[] total = {0, 0, 0, 0};
    ArrayList <Integer> expenses_total_arraylist= new ArrayList<>(Arrays.asList(total));
    String[] expenses={"Ulasim","Fatura","Eglence","Market"};
    ArrayList <String> expenses_arraylist= new ArrayList<>(Arrays.asList(expenses));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /// ///
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /// ///

        //Database Baglantisi
        AppDatabase db = Room.databaseBuilder(
                getApplicationContext(),
                AppDatabase.class,
                "expense_db"
        ).allowMainThreadQueries().build();
        ExpenseDao expenseDao = db.expenseDao();
        CategoryDao categoryDao = db.categoryDao();
        


        FrameLayout frame_layout = findViewById(R.id.frame_layout);

        Button button_add = findViewById(R.id.button_add);

        PieChart piechart = findViewById(R.id.pieChart);
        ArrayList<PieEntry> entries = new ArrayList<>();
        PieDataSet pieDataSet= new PieDataSet(entries,"Harcamalar");
        PieData pieData =new PieData(pieDataSet);



        button_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder= new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Harcama Ekle");

                LinearLayout linearLayout=new LinearLayout(MainActivity.this);
                linearLayout.setOrientation(LinearLayout.VERTICAL);

                EditText input = new EditText(MainActivity.this);
                input.setHint("Miktar?");

                linearLayout.addView(input);

                RadioGroup radioGroup= new RadioGroup(MainActivity.this);

                for(int i=0;i<expenses.length;i++){
                    RadioButton rb = new RadioButton(MainActivity.this);
                    rb.setText(expenses[i]);
                    rb.setId(i);
                    radioGroup.addView(rb);

                }
                linearLayout.addView(radioGroup);


                builder.setView(linearLayout);





                builder.setPositiveButton("Kaydet", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String miktar = input.getText().toString();
                        int intMiktar=Integer.parseInt(miktar);

                        int checked_radiobuttonId = radioGroup.getCheckedRadioButtonId();
                        if (checked_radiobuttonId == -1) {
                            Toast.makeText(MainActivity.this, "Kategori seç", Toast.LENGTH_SHORT).show();
                            return;
                            }
                        Integer old_value = expenses_total_arraylist.get(checked_radiobuttonId);
                        if (old_value==null)
                            old_value=0;
                        expenses_total_arraylist.set(checked_radiobuttonId,old_value + intMiktar);

                        entries.clear();
                        for (int i=0;i<expenses.length;i++){
                            if (expenses_total_arraylist.get(i)!=0&&expenses_total_arraylist.get(i)!=null){
                               PieEntry pieEntry = new PieEntry(expenses_total_arraylist.get(i),expenses_arraylist.get(i));
                               entries.add(pieEntry);
                            }
                        }

                        Toast.makeText(MainActivity.this, "Girilen Miktar: " + miktar +" Kaydedildi", Toast.LENGTH_LONG).show();

                        PieDataSet newDataSet = new PieDataSet(entries, "Harcamalar");

                        piechart.invalidate();
                        PieData newData = new PieData(newDataSet);
                        animateChart(newDataSet);
                        piechart.setData(newData);
                        piechart.invalidate();



                    }
                });

                builder.setNegativeButton("Iptal", null);
                builder.show();

            }

            public void animateChart( PieDataSet newDataSet){
                // Renkler
                newDataSet.setColors(
                        Color.parseColor("#E24B4A"),  // Ulaşım
                        Color.parseColor("#378ADD"),  // Fatura
                        Color.parseColor("#BA7517"),  // Eğlence
                        Color.parseColor("#1D9E75")   // Market
                );

                // Dilim üzerindeki yazı boyutu
                newDataSet.setValueTextSize(14f);
                newDataSet.setValueTextColor(Color.WHITE);

                PieData yeniData = new PieData(newDataSet);
                piechart.setData(yeniData);

                // Ortadaki delik — modern görünüm için
                piechart.setDrawHoleEnabled(true);
                piechart.setHoleRadius(40f);
                piechart.setHoleColor(Color.TRANSPARENT);

                // Legend
                piechart.getLegend().setEnabled(true);
                piechart.getLegend().setTextSize(12f);

                // Başlığı kaldır
                piechart.getDescription().setEnabled(false);

                // Animasyon
                piechart.animateY(1000);
            }
        });

    }
}
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

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.android.flexbox.FlexboxLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    Integer[] total = {0, 0, 0, 0};
    ArrayList <Integer> expenses_total_arraylist= new ArrayList<>(Arrays.asList(total));
    String[] expenses={"Ulasim","Fatura","Eglence","Market"};
    ArrayList <String> expenses_arraylist= new ArrayList<>(Arrays.asList(expenses));

    public void addCategory(CategoryDao categoryDao, String name, String hexColor){
        Category c1 = new Category();
        c1.name=name;
        c1.hexColor=hexColor;
        categoryDao.insertOne(c1);
    }

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

        List<Category> categoryList=categoryDao.listAll();
        if(categoryList.isEmpty()){
            addCategory(categoryDao,"Ulasim","F6B42C");
            addCategory(categoryDao,"Fatura","AB6345");
            addCategory(categoryDao,"Eglence","FF4444");
            addCategory(categoryDao,"Market","EACB19");
        }

        //Database Baglantisi//


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

                List<Category> categoryList1 = categoryDao.listAll();
                //Categoryleri dbden cekip secenek olarak ekle
                for(int i=0;i<categoryList1.size();i++){
                    RadioButton rb = new RadioButton(MainActivity.this);
                    rb.setText(categoryList1.get(i).name);
                    rb.setId(categoryList1.get(i).id);
                    radioGroup.addView(rb);
                }

                Button button_category_add=new Button(MainActivity.this);
                button_category_add.setText("Add Category");
                button_category_add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder= new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle("Kategori Ekle");

                        LinearLayout linearLayout=new LinearLayout(MainActivity.this);
                        linearLayout.setOrientation(LinearLayout.VERTICAL);

                        EditText editText = new EditText(MainActivity.this);
                        editText.setHint("Kategori adi giriniz");


                        TextView textView = new TextView(MainActivity.this);
                        textView.setText("Renk secin");

                        TextView textView1 = new TextView(MainActivity.this);
                        textView1.setText("Onizleme");


                        FlexboxLayout flexboxLayout= new FlexboxLayout(MainActivity.this);
                        String[] colors = {"#E24B4A", "#378ADD", "#1D9E75", "#BA7517",
                                "#7F77DD", "#D85A30", "#D4537E", "#639922"};

                        final String[] selectedColor = {null};

                        for (int i=0;i<colors.length;i++) {

                            String color = colors[i];

                            View daire = new View(MainActivity.this);

                            FlexboxLayout.LayoutParams params = new FlexboxLayout.LayoutParams(80, 80);
                            params.setMargins(10, 10, 10, 10);
                            daire.setLayoutParams(params);

                            daire.setBackgroundColor(Color.parseColor(color));

                            //yuvarlak yapmak icin
                            GradientDrawable shape = new GradientDrawable();
                            shape.setShape(GradientDrawable.OVAL);
                            shape.setColor(Color.parseColor(color));
                            daire.setBackground(shape);
                            daire.setOnClickListener(view -> {
                                selectedColor[0] = color;
                                textView1.setText("Seçilen renk: " + selectedColor);
                            });

                            flexboxLayout.addView(daire);


                        }


                        linearLayout.addView(editText);
                        linearLayout.addView(textView);
                        linearLayout.addView(flexboxLayout);
                        linearLayout.addView(textView1);
                        builder.setView(linearLayout);

                        builder.setPositiveButton("Kaydet", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                if (editText.getText().length()!=-1 && selectedColor[0]!=null){

                                    String categoryName=editText.getText();
                                    String selectedColor=selectedColor[0];

                                }

                            }
                        });




                    }
                });

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
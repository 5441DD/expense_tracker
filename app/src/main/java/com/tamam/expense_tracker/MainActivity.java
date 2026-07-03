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

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    public void addCategory(CategoryDao categoryDao, String name, String hexColor){
        Category c1 = new Category();
        c1.name=name;
        c1.hexColor=hexColor;
        categoryDao.insertOne(c1);
    }
    public void addExpense(ExpenseDao expenseDao, int typeId, int value, String date, String note,boolean isIncome){
        Expense expense=new Expense();
        expense.typeId=typeId;
        expense.value=value;
        expense.date=date;
        expense.note=note;
        expense.isIncome=isIncome;
        expenseDao.insertOne(expense);
    }


    PieChart piechart;
    ExpenseDao expenseDao;
    CategoryDao categoryDao;

    boolean isIncome = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /// ///
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /// ///

        //Database Baglantisi//
        piechart = findViewById(R.id.pieChart);
        AppDatabase db = Room.databaseBuilder(
                getApplicationContext(),
                AppDatabase.class,
                "expense_db"
        ).allowMainThreadQueries().build();
        expenseDao = db.expenseDao();
        categoryDao = db.categoryDao();
        updateChart(expenseDao,categoryDao);


        List<Category> categoryList=categoryDao.listAll();
        if(categoryList.isEmpty()){
            addCategory(categoryDao,"Ulasim","#F6B42C");
            addCategory(categoryDao,"Fatura","#AB6345");
            addCategory(categoryDao,"Eglence","#FF4444");

        }

        FrameLayout frame_layout = findViewById(R.id.frame_layout);

        Button button_add = findViewById(R.id.button_add);

        button_add.setOnClickListener(v -> showAddExpenseDialog());

    }


    void showAddExpenseDialog() {
        AlertDialog.Builder builder1= new AlertDialog.Builder(MainActivity.this);
        builder1.setTitle("Harcama Ekle");

        LinearLayout linearLayout=new LinearLayout(MainActivity.this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        MaterialButton materialButton= new MaterialButton(MainActivity.this);
        MaterialButton materialButton1= new MaterialButton(MainActivity.this);
        MaterialButtonToggleGroup materialButtonToggleGroup = new MaterialButtonToggleGroup(MainActivity.this);
        materialButtonToggleGroup.addView(materialButton);
        materialButtonToggleGroup.addView(materialButton1);
        materialButtonToggleGroup.setSingleSelection(true);
        materialButton.setText("Gider");
        materialButton1.setText("Gelir");
        materialButtonToggleGroup.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if (isChecked) {
                if (checkedId == materialButton.getId()) {
                    isIncome = false;
                } else {
                    isIncome = true;;
                }
            }
        });

        EditText input = new EditText(MainActivity.this);
        input.setHint("Miktar?");



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


        button_category_add.setOnClickListener(v -> showAddCategoryDialog());


        linearLayout.addView(materialButtonToggleGroup);
        linearLayout.addView(input);
        linearLayout.addView(button_category_add);
        linearLayout.addView(radioGroup);


        builder1.setView(linearLayout);





        builder1.setPositiveButton("Kaydet", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String miktar = input.getText().toString();
                if (miktar.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Miktar gir", Toast.LENGTH_SHORT).show();
                    return;
                }
                int intMiktar;
                try {
                    intMiktar = Integer.parseInt(miktar);
                } catch (NumberFormatException e) {
                    Toast.makeText(MainActivity.this, "Geçerli bir sayı gir", Toast.LENGTH_SHORT).show();
                    return;
                }

                int checked_radiobuttonId = radioGroup.getCheckedRadioButtonId();
                if (checked_radiobuttonId == -1) {
                    Toast.makeText(MainActivity.this, "Kategori seç", Toast.LENGTH_SHORT).show();
                    return;
                }
                addExpense(expenseDao, checked_radiobuttonId, intMiktar, "2026-07-03", "",isIncome);
                updateChart(expenseDao, categoryDao);

                Toast.makeText(MainActivity.this, "Girilen Miktar: " + miktar +" Kaydedildi", Toast.LENGTH_LONG).show();
            }
        });

        builder1.setNegativeButton("Iptal", null);
        updateChart(expenseDao, categoryDao);
        builder1.show();

    }



    void showAddCategoryDialog() {
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
                textView1.setText("Seçilen renk: " + selectedColor[0]);
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

                if (!editText.getText().toString().trim().isEmpty() && selectedColor[0] != null){

                    String categoryName=editText.getText().toString();
                    String selectedColor1=selectedColor[0];
                    if (!categoryName.isEmpty() && selectedColor1 != null) {
                        addCategory(categoryDao,categoryName,selectedColor1);
                        Toast.makeText(MainActivity.this, "Kategori basariyla kaydedildi", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "İsim ve renk seçiniz", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
        builder.setNegativeButton("Iptal", null);
        updateChart(expenseDao, categoryDao);
        builder.show();
    }

    public void updateChart(ExpenseDao expenseDao, CategoryDao categoryDao) {

        List<PieEntry> entries = new ArrayList<>();

        List<Category> categories = categoryDao.listAll();
        ArrayList<Integer> colors = new ArrayList<>();
        for(int i = 0; i < categories.size(); i++){
            Integer sum = expenseDao.sumExpensesbyId(categories.get(i).id);
            if (sum == null) sum = 0;
            if (sum > 0) {
                entries.add(new PieEntry((float) sum, categories.get(i).name));
                colors.add(Color.parseColor(categories.get(i).hexColor));
            }
        }

        PieDataSet dataSet = new PieDataSet(entries, "Harcamalar");
        dataSet.setColors(colors);
        dataSet.setValueTextSize(14f);
        dataSet.setValueTextColor(Color.WHITE);

        piechart.setData(new PieData(dataSet));
        piechart.setDrawHoleEnabled(true);
        piechart.setHoleRadius(40f);
        piechart.getDescription().setEnabled(false);
        piechart.animateY(1000);
        piechart.invalidate();


    }
}
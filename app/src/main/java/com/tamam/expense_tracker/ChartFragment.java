package com.tamam.expense_tracker;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChartFragment extends Fragment {

    PieChart piechart;
    ExpenseDao expenseDao;
    CategoryDao categoryDao;
    String lastChartMode = "gider";
    boolean isIncome = false;


    public void refresh() {
        MainActivity activity = (MainActivity) getActivity();
        if (activity == null) return;

        updateChart(expenseDao,categoryDao,lastChartMode,activity.startDate, activity.endDate);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_chart, container, false);

        piechart = view.findViewById(R.id.pieChart);
        MainActivity activity = (MainActivity) getActivity();
        expenseDao = activity.expenseDao;
        categoryDao = activity.categoryDao;
        List<Category> categoryList=categoryDao.listAll();
        if(categoryList.isEmpty()){
            addCategory(categoryDao,"Ulasim","#F6B42C");
            addCategory(categoryDao,"Fatura","#AB6345");
            addCategory(categoryDao,"Eglence","#FF4444");

        }

        Button button_add = view.findViewById(R.id.button_add);
        MaterialButton button_income=view.findViewById(R.id.btnIncome);
        MaterialButton button_expense=view.findViewById(R.id.btnExpense);
        updateChart(expenseDao, categoryDao,lastChartMode,activity.startDate,activity.endDate);;
        button_add.setOnClickListener(v -> showAddExpenseDialog());
        button_income.setOnClickListener(v -> updateChart(expenseDao, categoryDao,"gelir",activity.startDate,activity.endDate));
        button_expense.setOnClickListener(v -> updateChart(expenseDao, categoryDao,"gider",activity.startDate,activity.endDate));

        return view;

    }

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
    void showAddExpenseDialog() {
        AlertDialog.Builder builder1= new AlertDialog.Builder(requireContext());
        builder1.setTitle("Harcama Ekle");

        LinearLayout linearLayout=new LinearLayout(requireContext());
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        TextView textView= new TextView(requireContext());
        if (isIncome)
            textView.setText("Gelir");
        else
            textView.setText("Gider");

        MainActivity activity = (MainActivity) getActivity();

        MaterialButton materialButton= new MaterialButton(requireContext());
        MaterialButton materialButton1= new MaterialButton(requireContext());
        MaterialButtonToggleGroup materialButtonToggleGroup = new MaterialButtonToggleGroup(requireContext());
        materialButtonToggleGroup.addView(materialButton);
        materialButtonToggleGroup.addView(materialButton1);
        materialButtonToggleGroup.setSingleSelection(true);
        materialButton.setText("Gider");
        materialButton1.setText("Gelir");
        materialButtonToggleGroup.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if (isChecked) {
                if (checkedId == materialButton.getId()) {
                    isIncome = false;
                    textView.setText("Gider");
                } else {
                    isIncome = true;;
                    textView.setText("Gelir");
                }
            }
        });

        EditText input = new EditText(requireContext());
        input.setHint("Miktar?");



        RadioGroup radioGroup= new RadioGroup(requireContext());

        List<Category> categoryList1 = categoryDao.listAll();
        //Categoryleri dbden cekip secenek olarak ekle
        for(int i=0;i<categoryList1.size();i++){
            RadioButton rb = new RadioButton(requireContext());
            rb.setText(categoryList1.get(i).name);
            rb.setId(categoryList1.get(i).id);
            radioGroup.addView(rb);
        }

        Button button_category_add=new Button(requireContext());
        button_category_add.setText("Add Category");


        button_category_add.setOnClickListener(v -> showAddCategoryDialog());

        linearLayout.addView(materialButtonToggleGroup);
        linearLayout.addView(textView);
        linearLayout.addView(input);
        linearLayout.addView(button_category_add);
        linearLayout.addView(radioGroup);


        builder1.setView(linearLayout);





        builder1.setPositiveButton("Kaydet", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                String miktar = input.getText().toString();
                if (miktar.isEmpty()) {
                    Toast.makeText(requireContext(), "Miktar gir", Toast.LENGTH_SHORT).show();
                    return;
                }
                int intMiktar;
                try {
                    intMiktar = Integer.parseInt(miktar);
                } catch (NumberFormatException e) {
                    Toast.makeText(requireContext(), "Geçerli bir sayı gir", Toast.LENGTH_SHORT).show();
                    return;
                }

                int checked_radiobuttonId = radioGroup.getCheckedRadioButtonId();
                if (checked_radiobuttonId == -1) {
                    Toast.makeText(requireContext(), "Kategori seç", Toast.LENGTH_SHORT).show();
                    return;
                }
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                String simdikiTarih = dateFormat.format(new Date());
                addExpense(expenseDao, checked_radiobuttonId, intMiktar, simdikiTarih, "",isIncome);

                updateChart(expenseDao, categoryDao,lastChartMode,activity.startDate,activity.endDate);

                Toast.makeText(requireContext(), "Girilen Miktar: " + miktar +" Kaydedildi", Toast.LENGTH_LONG).show();
            }
        });

        builder1.setNegativeButton("Iptal", null);

        updateChart(expenseDao, categoryDao,lastChartMode,activity.startDate,activity.endDate);
        builder1.show();

    }

    void showAddCategoryDialog() {
        MainActivity activity = (MainActivity) getActivity();

        AlertDialog.Builder builder= new AlertDialog.Builder(requireContext());
        builder.setTitle("Kategori Ekle");

        LinearLayout linearLayout=new LinearLayout(requireContext());
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        EditText editText = new EditText(requireContext());
        editText.setHint("Kategori adi giriniz");


        TextView textView = new TextView(requireContext());
        textView.setText("Renk secin");

        TextView textView1 = new TextView(requireContext());
        textView1.setText("Onizleme");


        FlexboxLayout flexboxLayout= new FlexboxLayout(requireContext());
        String[] colors = {"#E24B4A", "#378ADD", "#1D9E75", "#BA7517",
                "#7F77DD", "#D85A30", "#D4537E", "#639922"};

        final String[] selectedColor = {null};

        for (int i=0;i<colors.length;i++) {

            String color = colors[i];

            View daire = new View(requireContext());

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
                        Toast.makeText(requireContext(), "Kategori basariyla kaydedildi", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(requireContext(), "İsim ve renk seçiniz", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
        builder.setNegativeButton("Iptal", null);
        updateChart(expenseDao, categoryDao,lastChartMode,activity.startDate,activity.endDate);
        builder.show();
    }
    public void updateChart(ExpenseDao expenseDao, CategoryDao categoryDao, String chartMode, String startDate,String endDate) {

        List<PieEntry> entries = new ArrayList<>();

        List<Category> categories = categoryDao.listAll();
        ArrayList<Integer> colors = new ArrayList<>();
        for(int i = 0; i < categories.size(); i++){
            Integer sum;
            if (chartMode.equals("gider")){
                sum = expenseDao.sumByCategoryAndDateRange(categories.get(i).id, false, startDate, endDate);
            } else if (chartMode.equals("gelir")) {
                sum = expenseDao.sumByCategoryAndDateRange(categories.get(i).id, true, startDate, endDate);
            }else {
                Integer gider = expenseDao.sumByCategoryAndDateRange(categories.get(i).id, false, startDate, endDate);
                Integer gelir = expenseDao.sumByCategoryAndDateRange(categories.get(i).id, true, startDate, endDate);
                if (gider==null) gider=0;
                if (gelir==null) gelir=0;
                sum=gelir-gider;

            }
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

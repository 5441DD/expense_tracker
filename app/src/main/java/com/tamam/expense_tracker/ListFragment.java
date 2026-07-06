package com.tamam.expense_tracker;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButtonToggleGroup;

import java.util.ArrayList;
import java.util.List;

public class ListFragment extends Fragment {

    ExpenseDao expenseDao;
    CategoryDao categoryDao;

    List<Expense> currentListviewList;
    List<Category> currentCategoryList;

    ListView listview_expenses1;

    String lastListMode="gider";

    public void refresh() {
        MainActivity activity = (MainActivity) getActivity();
        if (activity == null) return;

        listViewSet(lastListMode, activity.startDate, activity.endDate);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {



        View view = inflater.inflate(R.layout.fragment_list, container, false);

        MainActivity activity = (MainActivity) getActivity();
        expenseDao = activity.expenseDao;
        categoryDao = activity.categoryDao;
        listview_expenses1= view.findViewById(R.id.listview_expenses);
        MaterialButtonToggleGroup materialButtonToggleGroup=view.findViewById(R.id.toggleGroup);
        materialButtonToggleGroup.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
            @Override
            public void onButtonChecked(MaterialButtonToggleGroup materialButtonToggleGroup, int i, boolean b) {
                if (b){
                    if (i==R.id.btnIncome){
                        listViewSet("gelir",activity.startDate,activity.endDate);
                    }
                    else {
                        listViewSet("gider",activity.startDate,activity.endDate);
                    }
                }
            }
        });

        listViewSet(lastListMode,activity.startDate,activity.endDate);
        return view;
    }
    void listViewSet(String listMode,String startDate,String endDate){
        currentCategoryList=categoryDao.listAll();
        List<String> stringList = new ArrayList<>();

        if (listMode.equals("gelir")){
            currentListviewList=expenseDao.listByDateRange(true, startDate, endDate);
        } else if (listMode.equals("gider")) {
            currentListviewList=expenseDao.listByDateRange(false, startDate, endDate);
        }else {
            currentListviewList=expenseDao.listAll();
        }
        for (int i = 0; i < currentListviewList.size(); i++) {
            int expense_typeId = currentListviewList.get(i).typeId;
            String expense_name="Kategori bilinmiyor";
            String expense_hexcolor;
            for (int j = 0; j <currentCategoryList.size(); j++) {
                if (currentCategoryList.get(j).id==expense_typeId) {
                    expense_name = currentCategoryList.get(j).name;
                    expense_hexcolor = currentCategoryList.get(j).hexColor;
                }
            }
            String expense_date=currentListviewList.get(i).date;
            int expense_value = currentListviewList.get(i).value;
            String expense_note=currentListviewList.get(i).note;


            String text = expense_name + " - " + expense_value + "₺ - " + expense_date;
            stringList.add(text);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_list_item_1,
                stringList
        );
        listview_expenses1.setAdapter(adapter);
    }


}

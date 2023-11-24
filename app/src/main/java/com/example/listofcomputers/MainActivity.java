package com.example.listofcomputers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    static ArrayAdapter adapter;
    static ArrayList<Computer> dataItems;
    private Fragment_info infoFragment;
    private Fragment_add addFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);
        dataItems = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, dataItems);
        listView.setAdapter(adapter);

        // Инициализация фрагментов
        infoFragment = new Fragment_info();
        addFragment = new Fragment_add();

        FragmentManager fragmentManager = getSupportFragmentManager();

        // Инициализация данных из базы данных
        DataBaseAccessor databaseAccessor = new DataBaseAccessor(this);
        dataItems.addAll(databaseAccessor.getAllData());

        adapter.notifyDataSetChanged();

        // Обработчик кнопки "Добавить"
        Button addButton = findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.fragment_container, addFragment);
                transaction.commit();
            }
        });

        // Обработчик нажатия на элемент списка
        listView.setOnItemClickListener((parent, view, position, id) -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable("computer", dataItems.get(position)); // Передаем выбранный объект Computer

            int idComp = dataItems.get(position).getId();
            String nameComp = dataItems.get(position).getName();
            String statusComp = dataItems.get(position).getStatus();
            String locationComp = dataItems.get(position).getLocation();
            String onlineComp = dataItems.get(position).getLastOnline();
            bundle.putInt("id", idComp);
            bundle.putString("name", nameComp);
            bundle.putString("status", statusComp);
            bundle.putString("location", locationComp);
            bundle.putString("online", onlineComp);
            bundle.putInt("number", position);                           // Передаем номер элемента в списке

            infoFragment.setArguments(bundle);

            // Создаем объект FragmentTransaction для управления транзакциями фрагментов
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            // Заменяем текущий фрагмент в контейнере с идентификатором R.id.fragment_container на новый фрагмент (infoFragment)
            transaction.replace(R.id.fragment_container, infoFragment);
            // Применение изменений
            transaction.commit();
        });
    }

    // Метод для определения, является ли устройство планшетом
    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }
}
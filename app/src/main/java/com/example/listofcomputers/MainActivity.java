package com.example.listofcomputers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ListView listView;
    static ArrayList<Computer> dataItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DataBaseAccessor databaseAccessor = new DataBaseAccessor(this); // Замените 'this' на ваш контекст

        ListView listView = findViewById(R.id.listView);

        dataItems = databaseAccessor.getAllData(); // Реализуйте этот метод в DatabaseHelper

        ArrayAdapter<Computer> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, dataItems);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // получаем выбранный элемент
                Computer selectedComputer = dataItems.get(i);
                // переходим к второй активности и передаем данные
                Intent intent = new Intent(MainActivity.this, InfoActivity.class);
                intent.putExtra("computer", i);
                startActivity(intent);
                //mStartForResult.launch(intent);
            }
        });
    }

    

}

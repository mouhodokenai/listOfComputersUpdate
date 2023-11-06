package com.example.listofcomputers;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
public class MainActivity extends AppCompatActivity {
    private ListView listView;
    private ArrayAdapter adapter;
    static ArrayList<Computer> dataItems;

    // Запуск активности для получения результата
    ActivityResultLauncher<Intent> mStartForResult2 = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        int updatedNum = data.getIntExtra("num", -1);
                        Computer updatedComputer = (Computer) data.getSerializableExtra("updatedComputer");

                        dataItems.add(updatedComputer);

                        // Обновление адаптера
                        adapter.notifyDataSetChanged();
                    }
                }
            });
    ActivityResultLauncher<Intent> mStartForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        int updatedNum = data.getIntExtra("num", -1);
                        Computer updatedComputer = (Computer) data.getSerializableExtra("updatedComputer");

                        dataItems.set(updatedNum, updatedComputer);

                        // Обновление адаптера
                        adapter.notifyDataSetChanged();
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Инициализация dataItems здесь
        dataItems = new ArrayList<>();
        DataBaseAccessor databaseAccessor = new DataBaseAccessor(this);
        dataItems.addAll(databaseAccessor.getAllData());

        // Инициализация адаптера
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, dataItems);

        listView = findViewById(R.id.listView);
        listView.setAdapter(adapter);

        // Остальной код активности
        Button addButton = findViewById(R.id.addButton);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Computer selectedComputer = dataItems.get(i);
                Intent intent = new Intent(MainActivity.this, InfoActivity.class);
                intent.putExtra("number", i);
                intent.putExtra("computer", selectedComputer);
                mStartForResult.launch(intent);
            }
        });
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                int num = dataItems.size() + 1;
                intent.putExtra("num", num);
                mStartForResult2.launch(intent);
            }
        });
    }
}

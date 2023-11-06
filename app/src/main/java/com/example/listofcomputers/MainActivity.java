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
    ActivityResultLauncher<Intent> mStartForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {

                    listView = findViewById(R.id.listView);

                    if(result.getResultCode() == Activity.RESULT_OK){
                        Intent data = result.getData();
                        int updatedNum = data.getIntExtra("num", -1);
                        Computer updatedComputer = (Computer) data.getSerializableExtra("updatedComputer");


                        dataItems.set(updatedNum, updatedComputer);

                        // Обновление адаптера

                        listView.setAdapter(adapter);
                    }

                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, dataItems);

        DataBaseAccessor databaseAccessor = new DataBaseAccessor(this);

        listView = findViewById(R.id.listView);

        Button addButton = findViewById(R.id.addButton);

        dataItems = databaseAccessor.getAllData(); //

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // переходим к второй активности и передаем данные

                Computer selectedComputer = dataItems.get(i);
                Intent intent = new Intent(MainActivity.this, InfoActivity.class);
                intent.putExtra("number", i);
                intent.putExtra("computer", selectedComputer);
               // startActivity(intent);
                mStartForResult.launch(intent);
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                startActivity(intent);
            }
        });

    }

}

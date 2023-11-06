package com.example.listofcomputers;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        DataBaseAccessor databaseAccessor = new DataBaseAccessor(this);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        int numberComputer = bundle.getInt("num");

        // Настройка полей для редактирования
        EditText name = findViewById(R.id.name);
        EditText status = findViewById(R.id.status);
        EditText location = findViewById(R.id.location);
        EditText online = findViewById(R.id.online);


        // Обработчики событий для полей редактирования
        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name.setFocusableInTouchMode(true);
                name.setFocusable(true);
                name.setCursorVisible(true);
                name.requestFocus();
            }
        });
        status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                status.setFocusableInTouchMode(true);
                status.setFocusable(true);
                status.setCursorVisible(true);
                status.requestFocus();
            }
        });
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                location.setFocusableInTouchMode(true);
                location.setFocusable(true);
                location.setCursorVisible(true);
                location.requestFocus();
            }
        });
        online.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                online.setFocusableInTouchMode(true);
                online.setFocusable(true);
                online.setCursorVisible(true);
                online.requestFocus();
            }
        });

        Button saveButton = findViewById(R.id.saveButton);
        // Обработчик события для кнопки сохранения
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Получаем отредактированные данные из полей ввода
                String editedName = name.getText().toString();
                String editedStatus = status.getText().toString();
                String editedLocation = location.getText().toString();
                String editedLastOnline = online.getText().toString();

                Computer newComputer = new Computer(editedName, editedStatus, editedLocation, editedLastOnline);

                // Обновляем данные в объекте
                databaseAccessor.addComputer(editedName, editedStatus, editedLocation, editedLastOnline);

                Intent resultIntent = new Intent();
                resultIntent.putExtra("updatedComputer", newComputer); // Обновленный объект Note

                // Устанавливаем результат и передаем Intent обратно в MainActivity
                setResult(Activity.RESULT_OK, resultIntent);

                // Завершаем Activity
                finish();

            }
        });
    }
}
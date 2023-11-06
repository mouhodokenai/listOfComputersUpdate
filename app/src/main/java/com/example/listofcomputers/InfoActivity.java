package com.example.listofcomputers;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class InfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        DataBaseAccessor databaseAccessor = new DataBaseAccessor(this);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        Computer CurrentComputer = (Computer) bundle.get("computer");
        int numberComputer = bundle.getInt("number");

        // Настройка полей для редактирования
        EditText name = findViewById(R.id.name);
        EditText status = findViewById(R.id.status);
        EditText location = findViewById(R.id.location);
        EditText online = findViewById(R.id.online);

        int currentId = CurrentComputer.getId();

        String currentName = CurrentComputer.getName();
        String currentStatus = CurrentComputer.getStatus();
        String currentLocation = CurrentComputer.getLocation();
        String currentOnline = CurrentComputer.getLastOnline();

        name.setText(currentName);
        status.setText(currentStatus);
        location.setText(currentLocation);
        online.setText(currentOnline);

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

                CurrentComputer.setId(currentId);
                CurrentComputer.setName(editedName);
                CurrentComputer.setStatus(editedStatus);
                CurrentComputer.setLocation(editedLocation);
                CurrentComputer.setLastOnline(editedLastOnline);
                // Обновляем данные в объекте
                databaseAccessor.updateNote(currentId, editedName, editedStatus, editedLocation, editedLastOnline);

                ArrayList<Computer> resultItems = databaseAccessor.getAllData();

                Intent resultIntent = new Intent();
                resultIntent.putExtra("num", numberComputer); // Номер заметки, которую мы обновили
                resultIntent.putExtra("updatedComputer", CurrentComputer); // Обновленный объект Note


                // Устанавливаем результат и передаем Intent обратно в MainActivity
                setResult(Activity.RESULT_OK, resultIntent);

                // Завершаем Activity
                finish();

            }
        });
    }

}
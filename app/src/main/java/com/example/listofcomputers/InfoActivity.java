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

public class InfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        Computer CurrentComputer = MainActivity.dataItems.get(bundle.getInt("computer"));

        // Настройка полей для редактирования
        EditText name = findViewById(R.id.name);
        EditText text = findViewById(R.id.status);
        String currentName = CurrentComputer.getName();
        String currentStatus = CurrentComputer.getStatus();
        text.setText(currentStatus);
        name.setText(currentName);
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
        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text.setFocusableInTouchMode(true);
                text.setFocusable(true);
                text.setCursorVisible(true);
                text.requestFocus();
            }
        });

        Button saveButton = findViewById(R.id.saveButton);
        // Обработчик события для кнопки сохранения
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Получаем отредактированные данные из полей ввода
                String editedTextname = name.getText().toString();
                String editedTexttext = text.getText().toString();
                // Обновляем данные в объекте
                /*
                currentNote.setName(editedTextname);
                currentNote.setText(editedTexttext);

                 */

                Intent resultIntent = new Intent();


                // Устанавливаем результат и передаем Intent обратно в MainActivity
                setResult(Activity.RESULT_OK, resultIntent);

                // Завершаем Activity2
                finish();

            }
        });
    }
}
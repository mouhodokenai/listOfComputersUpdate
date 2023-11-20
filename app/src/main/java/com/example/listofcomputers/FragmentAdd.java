package com.example.listofcomputers;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


public class FragmentAdd extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        DataBaseAccessor databaseAccessor = new DataBaseAccessor(this);

        // Настройка полей для редактирования
        EditText name = findViewById(R.id.name);
        TextView status = findViewById(R.id.status);
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
        Switch stateBut = findViewById(R.id.stateBut);
        stateBut .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String newStatus = isChecked ? "Online" : "Offline";
                status.setText(newStatus);
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

                if (editedName.isEmpty() || editedStatus.isEmpty() || editedLocation.isEmpty() || editedLastOnline.isEmpty()) {
                    // Хотя бы одно поле не заполнено, высвечиваем Toast
                    Toast.makeText(getApplicationContext(), "Заполните все поля", Toast.LENGTH_SHORT).show();
                } else {
                    Computer newComputer = new Computer(editedName, editedStatus, editedLocation, editedLastOnline);

                    // Обновляем данные в объекте
                    databaseAccessor.addComputer(editedName, editedStatus, editedLocation, editedLastOnline);

                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("updatedComputer", newComputer); // Обновленный объект

                    // Устанавливаем результат и передаем Intent обратно в MainActivity
                    setResult(Activity.RESULT_OK, resultIntent);

                    // Завершаем Activity
                    finish();
                }

            }
        });

    }
}
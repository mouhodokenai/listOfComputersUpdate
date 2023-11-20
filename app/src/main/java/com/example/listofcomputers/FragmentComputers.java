package com.example.listofcomputers;

import static android.content.Intent.getIntent;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
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

public class FragmentComputers extends Fragment {
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        DataBaseAccessor databaseAccessor = new DataBaseAccessor(this);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        Computer CurrentComputer = (Computer) bundle.get("computer");
        int numberComputer = bundle.getInt("number");

        // Настройка полей для редактирования
        EditText name = findViewById(R.id.name);
        TextView status = findViewById(R.id.status);
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

        Switch stateBut = findViewById(R.id.stateBut);

        SharedPreferences sharedPref = getSharedPreferences("MyPrefs", MODE_PRIVATE);


        if (currentStatus.equals("Online")){
            stateBut.setChecked(true);
        } else {
            stateBut.setChecked(false);
        }

        stateBut.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String newStatus = isChecked ? "Online" : "Offline";
                status.setText(newStatus);
                databaseAccessor.editStatus(currentId, newStatus);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putBoolean("switchState", isChecked);
                editor.apply();
            }
        });



        Button saveButton = findViewById(R.id.saveButton);
        Button deleteButton = findViewById(R.id.deleteButton);
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
                    CurrentComputer.setId(currentId);
                    CurrentComputer.setName(editedName);
                    CurrentComputer.setStatus(editedStatus);
                    CurrentComputer.setLocation(editedLocation);
                    CurrentComputer.setLastOnline(editedLastOnline);
                    // Обновляем данные в объекте
                    databaseAccessor.updateComputer(currentId, editedName, editedStatus, editedLocation, editedLastOnline);

                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("num", numberComputer); // Номер компьютера, который мы обновили
                    resultIntent.putExtra("del", false);
                    resultIntent.putExtra("updatedComputer", CurrentComputer); // Обновленный объект

                    // Устанавливаем результат и передаем Intent обратно в MainActivity
                    setResult(Activity.RESULT_OK, resultIntent);
                    finish();
                }
            }
        });
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent resultIntent = new Intent();
                int id = CurrentComputer.getId();
                databaseAccessor.deleteComputer(id);
                resultIntent.putExtra("num", numberComputer);
                resultIntent.putExtra("del", true);

                // Устанавливаем результат и передаем Intent обратно в MainActivity
                setResult(Activity.RESULT_OK, resultIntent);

                finish();
            }
        });
    }

    private Intent getIntent() {

    }


    private void setContentView(int activityInfo) {

    }


}
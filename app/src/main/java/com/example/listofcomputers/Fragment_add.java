package com.example.listofcomputers;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class Fragment_add extends Fragment {
    EditText name;
    TextView status;
    EditText location;
    EditText online;
    Switch stateBut;
    Button saveButton;
    Button deleteButton;
    Button timeButton;
    Button dateButton;

    private DataBaseAccessor databaseAccessor;
    private SharedPreferences sharedPref;
    static int currentId;
    static String currentName, currentStatus, currentLocation, currentOnline;
    static Calendar dateAndTime;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add, container, false);
        dateAndTime = Calendar.getInstance();
        name = view.findViewById(R.id.name);
        status = view.findViewById(R.id.status);
        location = view.findViewById(R.id.location);
        online = view.findViewById(R.id.online);
        stateBut = view.findViewById(R.id.stateBut);
        saveButton = view.findViewById(R.id.saveButton);
        timeButton = view.findViewById(R.id.btn_time);
        dateButton = view.findViewById(R.id.btn_date);

        dateButton.setOnClickListener(v -> setDate(v));

        timeButton.setOnClickListener(v -> setTime(v));

        name.setText(currentName);
        status.setText(currentStatus);
        location.setText(currentLocation);
        online.setText(currentOnline);

        stateBut.setChecked(false);


        saveButton.setOnClickListener(v -> onSaveButtonClick());

        stateBut.setOnCheckedChangeListener((buttonView, isChecked) -> {
            String newStatus = isChecked ? "Online" : "Offline";
            status.setText(newStatus);
            databaseAccessor.editStatus(currentId, newStatus);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean("switchState", isChecked);
            editor.apply();
        });

        name.setOnClickListener(v -> setEditTextFocus(name));
        location.setOnClickListener(v -> setEditTextFocus(location));
        online.setOnClickListener(v -> setEditTextFocus(online));

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        databaseAccessor = new DataBaseAccessor(requireContext());
        sharedPref = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
    }

    private void setEditTextFocus(EditText editText) {
        editText.setFocusableInTouchMode(true);
        editText.setFocusable(true);
        editText.setCursorVisible(true);
        editText.requestFocus();
    }

    private void onSaveButtonClick() {
        EditText name = requireView().findViewById(R.id.name);
        TextView status = requireView().findViewById(R.id.status);
        EditText location = requireView().findViewById(R.id.location);
        EditText online = requireView().findViewById(R.id.online);

        String editedName = name.getText().toString();
        String editedStatus = status.getText().toString();
        String editedLocation = location.getText().toString();
        String editedLastOnline = online.getText().toString();

        if (editedName.isEmpty() || editedStatus.isEmpty() || editedLocation.isEmpty() || editedLastOnline.isEmpty()) {
            // Хотя бы одно поле не заполнено, высвечиваем Toast
            Toast.makeText(requireContext(), "Заполните все поля", Toast.LENGTH_SHORT).show();
        } else {
            Computer newComputer = new Computer(editedName, editedStatus, editedLocation, editedLastOnline);

            // Обновляем данные в объекте
            databaseAccessor.addComputer(editedName, editedStatus, editedLocation, editedLastOnline);

            MainActivity.dataItems.add(newComputer);

            // Обновление адаптера
            MainActivity.adapter.notifyDataSetChanged();
            getFragmentManager().beginTransaction().remove(this).commit();
        }
    }
    public void setDate(View v) {
        new DatePickerDialog(requireContext(), d,
                dateAndTime.get(Calendar.YEAR),
                dateAndTime.get(Calendar.MONTH),
                dateAndTime.get(Calendar.DAY_OF_MONTH))
                .show();
    }

    // отображаем диалоговое окно для выбора времени
    public void setTime(View v) {
        new TimePickerDialog(requireContext(), t,
                dateAndTime.get(Calendar.HOUR_OF_DAY),
                dateAndTime.get(Calendar.MINUTE), true)
                .show();
    }
    // установка начальных даты и времени
    private void setInitialDateTime() {
        online.setText(DateUtils.formatDateTime(requireContext(),
                dateAndTime.getTimeInMillis(),
                DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR
                        | DateUtils.FORMAT_SHOW_TIME));
    }

    TimePickerDialog.OnTimeSetListener t=new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            dateAndTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
            dateAndTime.set(Calendar.MINUTE, minute);
            setInitialDateTime();
        }
    };

    // установка обработчика выбора даты
    DatePickerDialog.OnDateSetListener d=new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            dateAndTime.set(Calendar.YEAR, year);
            dateAndTime.set(Calendar.MONTH, monthOfYear);
            dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            setInitialDateTime();
        }
    };

}
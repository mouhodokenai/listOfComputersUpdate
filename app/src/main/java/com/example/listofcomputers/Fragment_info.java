package com.example.listofcomputers;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Calendar;

public class Fragment_info extends Fragment {
    EditText name;
    TextView status;
    EditText location;
    EditText online;
    Switch stateBut;
    Button saveButton;
    Button deleteButton;

    private DataBaseAccessor databaseAccessor;
    private SharedPreferences sharedPref;
    static int currentId;
    static String currentName, currentStatus, currentLocation, currentOnline;
    static Computer currentComputer;
    static int numberComputer;
    static Calendar dateAndTime;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_info, container, false);
        dateAndTime = Calendar.getInstance();
        name = view.findViewById(R.id.name);
        status = view.findViewById(R.id.status);
        location = view.findViewById(R.id.location);
        online = view.findViewById(R.id.online);
        stateBut = view.findViewById(R.id.stateBut);
        saveButton = view.findViewById(R.id.saveButton);
        deleteButton = view.findViewById(R.id.deleteButton);

        name.setText(currentName);
        status.setText(currentStatus);
        location.setText(currentLocation);
        online.setText(currentOnline);

        if (currentStatus.equals("Online")) {
            stateBut.setChecked(true);
        } else {
            stateBut.setChecked(false);
        }

        saveButton.setOnClickListener(v -> onSaveButtonClick(currentComputer, numberComputer));
        deleteButton.setOnClickListener(v -> onDeleteButtonClick(currentComputer, numberComputer));

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

        Bundle args = getArguments();
        if (args != null) {
            currentComputer =  (Computer) args.getSerializable("computer");
            numberComputer = args.getInt("number");

            currentId = args.getInt("id");
            currentName = args.getString("name");
            currentStatus = args.getString("status");
            currentLocation = args.getString("location");
            currentOnline = args.getString("online");

            /*
            if (currentComputer != null) {
                String currentName = currentComputer.getName();
                String currentStatus = currentComputer.getStatus();
                String currentLocation = currentComputer.getLocation();
                String currentOnline = currentComputer.getLastOnline();

             */
            }
        }

    private void setEditTextFocus(EditText editText) {
        editText.setFocusableInTouchMode(true);
        editText.setFocusable(true);
        editText.setCursorVisible(true);
        editText.requestFocus();
    }

    private void onSaveButtonClick(Computer currentComputer, int numberComputer) {
        EditText name = requireView().findViewById(R.id.name);
        TextView status = requireView().findViewById(R.id.status);
        EditText location = requireView().findViewById(R.id.location);
        EditText online = requireView().findViewById(R.id.online);

        String editedName = name.getText().toString();
        String editedStatus = status.getText().toString();
        String editedLocation = location.getText().toString();
        String editedLastOnline = online.getText().toString();
        Log.d("TAG", "onSaveButtonClick: ");
        if (currentComputer == null) {
            currentComputer = new Computer();
        }

        if (editedName.isEmpty() || editedStatus.isEmpty() || editedLocation.isEmpty() || editedLastOnline.isEmpty()) {
            Toast.makeText(requireContext(), "Заполните все поля", Toast.LENGTH_SHORT).show();
        } else {
            currentComputer.setId(currentId);
            currentComputer.setName(editedName);
            currentComputer.setStatus(editedStatus);
            currentComputer.setLocation(editedLocation);
            currentComputer.setLastOnline(editedLastOnline);
            Log.d("TAG", "onSaveButtonClick: ");
            databaseAccessor.updateComputer(currentComputer.getId(), editedName, editedStatus, editedLocation, editedLastOnline);
            Log.d("TAG", "onSaveButtonClick: ");

            MainActivity.dataItems.set(numberComputer, currentComputer);

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

    private void onDeleteButtonClick(Computer currentComputer, int numberComputer) {
        databaseAccessor.deleteComputer(currentId);

    }
}

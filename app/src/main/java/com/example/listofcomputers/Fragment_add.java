package com.example.listofcomputers;

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

public class Fragment_add extends Fragment {

    private EditText name;
    private TextView status;
    private EditText location;
    private EditText online;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add, container, false);

        DataBaseAccessor databaseAccessor = new DataBaseAccessor(requireContext());

        // Настройка полей для редактирования
        EditText name = view.findViewById(R.id.name);
        TextView status = view.findViewById(R.id.status);
        EditText location = view.findViewById(R.id.location);
        EditText online = view.findViewById(R.id.online);

        // Обработчики событий для полей редактирования
        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setEditTextFocus(name);
            }
        });

        Switch stateBut = view.findViewById(R.id.stateBut);
        stateBut.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String newStatus = isChecked ? "Online" : "Offline";
                status.setText(newStatus);
            }
        });

        online.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setEditTextFocus(online);
            }
        });

        Button saveButton = view.findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("updatedComputer", newComputer); // Обновленный объект

                    // Устанавливаем результат и передаем Intent обратно в MainActivity
                    requireActivity().setResult(requireActivity().RESULT_OK, resultIntent);

                    // Завершаем Activity
                    requireActivity().finish();
                }
            }
        });

        return view;
    }

    private void setEditTextFocus(EditText editText) {
        editText.setFocusableInTouchMode(true);
        editText.setFocusable(true);
        editText.setCursorVisible(true);
        editText.requestFocus();
    }
}

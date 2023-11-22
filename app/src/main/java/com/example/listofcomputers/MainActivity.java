package com.example.listofcomputers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    static ArrayAdapter adapter;
    static ArrayList<Computer> dataItems;
    private Fragment_info infoFragment;
    private Fragment_add addFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);
        dataItems = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, dataItems);
        listView.setAdapter(adapter);

        infoFragment = new Fragment_info();
        addFragment = new Fragment_add();

        FragmentManager fragmentManager = getSupportFragmentManager();

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, addFragment);
        fragmentTransaction.commit();

        dataItems.add(new Computer("Computer 1", "Online", "Location 1", "Last Online 1"));
        dataItems.add(new Computer("Computer 2", "Offline", "Location 2", "Last Online 2"));

        adapter.notifyDataSetChanged();

        Button addButton = findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.fragment_container, addFragment);
                transaction.commit();
            }
        });

        listView.setOnItemClickListener((parent, view, position, id) -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable("computer", dataItems.get(position)); // Передаем выбранный объект Computer

            int idComp = dataItems.get(position).getId();
            String nameComp = dataItems.get(position).getName();
            String statusComp = dataItems.get(position).getStatus();
            String locationComp = dataItems.get(position).getLocation();
            String onlineComp = dataItems.get(position).getLastOnline();
            bundle.putInt("id", idComp);
            bundle.putString("name", nameComp);
            bundle.putString("status", statusComp);
            bundle.putString("location", locationComp);
            bundle.putString("online", onlineComp);
            bundle.putInt("number", position);                           // Передаем номер элемента в списке

            infoFragment.setArguments(bundle);

            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.fragment_container, infoFragment);
            transaction.commit();
        });
    }
}

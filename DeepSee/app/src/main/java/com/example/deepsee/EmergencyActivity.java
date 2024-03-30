package com.example.deepsee;

import android.os.Bundle;

import com.example.deepsee.contacts.ContactListAdapter;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.deepsee.databinding.ActivityEmergencyBinding;

public class EmergencyActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityEmergencyBinding binding;
    int [] img = {R.drawable.ic_launcher_foreground};
    String [] name = {"bob acious"};
    String [] number = {"1234567890"};

    //@Override
    //protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        binding = ActivityEmergencyBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
//
//        ContactListAdapter adapter = new ContactListAdapter(img,name,number);
//        RecyclerView recyclerView = findViewById(R.id.add_new_recyclerview);
//        recyclerView.setAdapter(adapter);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));


}
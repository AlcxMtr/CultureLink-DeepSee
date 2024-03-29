package com.example.deepsee;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.deepsee.contacts.ContactListAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EmergencyContactFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class EmergencyContactFragment extends Fragment {

    private View binding;
    int [] img = {R.drawable.ic_launcher_foreground};
    String [] name = {"bob acious"};
    String [] number = {"1234567890"};

    public EmergencyContactFragment() {
        // Required empty public constructor
        super(R.layout.activity_emergency);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.binding = inflater.inflate(R.layout.activity_emergency, container, false);
        return binding;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ContactListAdapter adapter = new ContactListAdapter(img,name,number);
        RecyclerView recyclerView = binding.findViewById(R.id.add_new_recyclerview);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
    }



}
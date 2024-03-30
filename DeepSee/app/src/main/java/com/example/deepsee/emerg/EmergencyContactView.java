package com.example.deepsee.emerg;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.deepsee.R;
import com.example.deepsee.contacts.ContactListAdapter;
import com.example.deepsee.databinding.EmrgContentViewBinding;

import java.util.ArrayList;

public class EmergencyContactView extends Fragment {

    private EmrgContentViewBinding binding;

    public static ArrayList<Integer> display_img = new ArrayList<>();
    public static ArrayList<String> display_name = new ArrayList<>();
    public static ArrayList<String> display_number = new ArrayList<>();
    public static ContactListAdapter display_adapter;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = EmrgContentViewBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //EmergencyContactAdd.added_adapter = new ContactListAdapter(EmergencyContactAdd.added_img,EmergencyContactAdd.added_name,EmergencyContactAdd.added_number);
//        RecyclerView added_recyclerView = binding.emergencyContactsRecycler;
//        added_recyclerView.setAdapter(EmergencyContactAdd.added_adapter);
//        added_recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        display_adapter = new ContactListAdapter(display_img,display_name,display_number,2);
        RecyclerView display_recyclerView = binding.emergencyContactsRecycler;
        display_recyclerView.setAdapter(display_adapter);
        display_recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        binding.buttonFirst.setOnClickListener(v ->
                NavHostFragment.findNavController(EmergencyContactView.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment)
        );
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
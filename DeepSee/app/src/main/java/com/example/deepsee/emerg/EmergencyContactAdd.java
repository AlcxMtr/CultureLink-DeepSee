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
import com.example.deepsee.contacts.Contact;
import com.example.deepsee.contacts.ContactListAdapter;
import com.example.deepsee.databinding.EmrgContentAddBinding;
import com.example.deepsee.messaging.SMSActivity;

import java.util.ArrayList;

public class EmergencyContactAdd extends Fragment {

    private EmrgContentAddBinding binding;

//    int [] img = {R.drawable.ic_launcher_foreground};
//    String [] name = {"bob acious"};
//    String [] number = {"1234567890"};

    public static ArrayList<Integer> img = new ArrayList<>();
    public static ArrayList<String> name = new ArrayList<>();
    public static ArrayList<String> number = new ArrayList<>();
    ArrayList<Contact> contacts;
    //ArrayList<Contact> added_contacts = new ArrayList<>();

    public static ArrayList<Integer> added_img = new ArrayList<>();
    public static ArrayList<String> added_name = new ArrayList<>();
    public static ArrayList<String> added_number = new ArrayList<>();

    public static ContactListAdapter adapter;
    public static ContactListAdapter added_adapter;


    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = EmrgContentAddBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        contacts = Contact.getContacts(binding.addNewRecyclerview.getContext());

        for (Contact contact: contacts) {
            img.add(R.drawable.ic_launcher_foreground);
            name.add(contact.getName());
            number.add(contact.getContactNumber());

            System.out.println("name: " + contact.getName() + " num: " + contact.getContactNumber());
        }
        //ContactListAdapter adapter = new ContactListAdapter(img,name,number);
        adapter = new ContactListAdapter(img,name,number);
        RecyclerView recyclerView = binding.addNewRecyclerview;
        //RecyclerView recyclerView = binding.addedContactsView;
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        //ContactListAdapter added_adapter = new ContactListAdapter(added_img,added_name,added_number);
        added_adapter = new ContactListAdapter(added_img,added_name,added_number);
        RecyclerView added_recyclerView = binding.addedContactsView;
        added_recyclerView.setAdapter(added_adapter);
        added_recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));



        binding.buttonSecond.setOnClickListener(v ->
                NavHostFragment.findNavController(EmergencyContactAdd.this)
                        .navigate(R.id.action_SecondFragment_to_FirstFragment)

        );
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
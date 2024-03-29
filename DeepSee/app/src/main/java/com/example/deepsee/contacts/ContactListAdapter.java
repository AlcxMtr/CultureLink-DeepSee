package com.example.deepsee.contacts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.deepsee.R;
import com.example.deepsee.emerg.EmergencyContactAdd;

import java.util.ArrayList;


public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.ViewHolder>{
    ArrayList<Integer> photo;
    ArrayList<String> name;
    ArrayList<String> number;

    //public static ArrayList<Contact> addedList;


    public ContactListAdapter(ArrayList<Integer> photo, ArrayList<String> name, ArrayList<String> number) {
        //ContactListAdapter.addedList = addedList;
        this.photo = photo;
        this.name = name;
        this.number = number;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.emerg_contact_rectangle,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //holder.contactPhoto.setImageResource(photo[position]);
        holder.name.setText("" + name.get(position));
        holder.number.setText("" + number.get(position));

    }

    @Override
    public int getItemCount() {
        return name.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public ImageView contactPhoto;
        public TextView name;
        public TextView number;
        public Button addButton;




        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            contactPhoto = (ImageView) itemView.findViewById(R.id.emerg_contact_photo);
            name = (TextView) itemView.findViewById(R.id.Emerg_Contact_Name);
            number = (TextView) itemView.findViewById(R.id.Emerg_Contact_Number);
            addButton = (Button) itemView.findViewById(R.id.button);
            addButton.setOnClickListener(this::onClick);
        }


        public void onClick(View v) {

            int position = getBindingAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
//                 item = itemList.get(position);
                EmergencyContactAdd.added_img.add(R.drawable.ic_launcher_foreground);
                EmergencyContactAdd.added_name.add((String) name.getText());
                EmergencyContactAdd.added_number.add((String) number.getText());
                System.out.println("name: " + name.getText() + " num: " + number.getText());
                EmergencyContactAdd.added_adapter.notifyItemInserted(EmergencyContactAdd.added_img.size());
                EmergencyContactAdd.adapter.notifyItemRemoved(position);
                addButton.setText("remove");
            }

        }


    }
}

package com.example.deepsee.contacts;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import com.example.deepsee.emerg.EmergencyContactView;

import java.util.ArrayList;


public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.ViewHolder>{
    ArrayList<Integer> photo;
    ArrayList<String> name;
    ArrayList<String> number;

    int flag_added;

    //public static ArrayList<Contact> addedList;


    public ContactListAdapter(ArrayList<Integer> photo, ArrayList<String> name, ArrayList<String> number, int flag) {
        //ContactListAdapter.addedList = addedList;
        this.photo = photo;
        this.name = name;
        this.number = number;
        this.flag_added = flag;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        if(this.flag_added == 0){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.emerg_contact_rectangle, parent, false);
        }
        else if(this.flag_added == 1){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.emerg_contact_delete_rectangle, parent, false);
        }
        else{
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.emerg_contact_display_rectangle, parent, false);
        }
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
        public ImageView callButton;




        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            callButton = (ImageView) itemView.findViewById(R.id.callButton);
            contactPhoto = (ImageView) itemView.findViewById(R.id.emerg_contact_photo);
            name = (TextView) itemView.findViewById(R.id.Emerg_Contact_Name);
            number = (TextView) itemView.findViewById(R.id.Emerg_Contact_Number);
            addButton = (Button) itemView.findViewById(R.id.button);
            addButton.setOnClickListener(this::onClick);

            if(callButton != null){

                callButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = getBindingAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {


                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        System.out.println("name: " + name.getText() + " num: " + number.getText());

                        callIntent.setData(Uri.parse("tel:" + number.getText()));
                        v.getContext().startActivity(callIntent);}
                    }
                });
            }

        }




        public void onClick(View v) {

            int position = getBindingAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {


//
                if(addButton.getText().toString().equals("Remove")){
                    EmergencyContactAdd.img.add(R.drawable.contacts);
                    EmergencyContactAdd.name.add((String) name.getText());
                    EmergencyContactAdd.number.add((String) number.getText());

                    EmergencyContactView.display_img.remove(position+1);
                    EmergencyContactView.display_name.remove(position+1);
                    EmergencyContactView.display_number.remove(position+1);

                    EmergencyContactAdd.added_img.remove(position);
                    EmergencyContactAdd.added_name.remove(position);
                    EmergencyContactAdd.added_number.remove(position);
                    System.out.println("name: " + name.getText() + " num: " + number.getText());


                    EmergencyContactAdd.adapter.notifyItemInserted(EmergencyContactAdd.added_img.size());
                    EmergencyContactAdd.added_adapter.notifyItemRemoved(position);
                    EmergencyContactView.display_adapter.notifyItemRemoved(position+1);
                    //addButton.setText("Add");

                }
                else if (addButton.getText().toString().equals("Add") ){
                    System.out.println(addButton.getText());
                    //addButton.setText("Remove");
                    System.out.println(addButton.getText());
                    EmergencyContactAdd.added_img.add(R.drawable.contacts);
                    EmergencyContactAdd.added_name.add((String) name.getText());
                    EmergencyContactAdd.added_number.add((String) number.getText());

                    EmergencyContactView.display_img.add(R.drawable.contacts);
                    EmergencyContactView.display_name.add((String) name.getText());
                    EmergencyContactView.display_number.add((String) number.getText());

                    EmergencyContactAdd.img.remove(position);
                    EmergencyContactAdd.name.remove(position);
                    EmergencyContactAdd.number.remove(position);

                    System.out.println("name: " + name.getText() + " num: " + number.getText());
//                    EmergencyContactAdd.adapter = new ContactListAdapter(EmergencyContactAdd.img,EmergencyContactAdd.name,EmergencyContactAdd.number);
//                    EmergencyContactAdd.added_adapter = new ContactListAdapter(EmergencyContactAdd.added_img,EmergencyContactAdd.added_name,EmergencyContactAdd.added_number);
//
                    EmergencyContactAdd.added_adapter.notifyItemInserted(EmergencyContactAdd.added_img.size());
                    EmergencyContactView.display_adapter.notifyItemInserted(EmergencyContactView.display_img.size());
                    EmergencyContactAdd.adapter.notifyItemRemoved(position);
                }





            }

        }


    }
}

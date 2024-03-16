package com.example.deepsee.contacts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.deepsee.R;


public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.ViewHolder>{
    int[] photo;
    String[] name;
    String[] number;
    public ContactListAdapter(int[] photo, String[] name, String[] number) {
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
        holder.name.setText("" + name[position]);
        holder.number.setText("" + number[position]);

    }

    @Override
    public int getItemCount() {
        return name.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView contactPhoto;
        public TextView name;
        public TextView number;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            contactPhoto = (ImageView) itemView.findViewById(R.id.emerg_contact_photo);
            name = (TextView) itemView.findViewById(R.id.Emerg_Contact_Name);
            number = (TextView) itemView.findViewById(R.id.Emerg_Contact_Number);
        }
    }
}

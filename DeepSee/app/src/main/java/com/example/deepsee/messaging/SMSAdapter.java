//package com.example.deepsee;
//import android.content.Context;
//import android.view.ViewGroup;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import java.util.List;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.widget.TextView;
//import android.content.Intent;
//
//public class SMSAdapter extends RecyclerView.Adapter<SMSAdapter.SMSViewHolder> {
//
//    private List<SMSMessages> smsMessages;
//    private Context mContext;
//
//    // Constructor to initialize the adapter with SMS data
//    public SMSAdapter(Context context, List<SMSMessages> smsMessages) {
//        this.smsMessages = smsMessages;
//        mContext = context;
//    }
//
//    // ViewHolder class to hold the views for each SMS item
//    public static class SMSViewHolder extends RecyclerView.ViewHolder {
//        public TextView contactNameTextView;
//        public TextView messageTextView;
//        public TextView timestampTextView;
//
//        public SMSViewHolder(View itemView) {
//            super(itemView);
//            contactNameTextView = itemView.findViewById(R.id.contact_name);
//            messageTextView = itemView.findViewById(R.id.textmsg_shortened);
//            timestampTextView = itemView.findViewById(R.id.timeofmsg);
//        }
//    }
//
//    @NonNull
//    @Override
//    public SMSViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        // Inflate the layout for each SMS item
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_sms_message, parent, false);
//        return new SMSViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull SMSViewHolder holder, int position) {
//        // Get the SMS message at the current position
//        final SMSMessages smsMessage = smsMessages.get(position);
//
//        // Bind data to views
//        holder.contactNameTextView.setText(smsMessage.getContactName());
//        holder.messageTextView.setText(smsMessage.getMessage());
//        holder.timestampTextView.setText(smsMessage.getTimestamp());
//
//        // Set OnClickListener to open messaging app when card view is clicked
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // Open messaging app
//                Intent intent = new Intent(Intent.ACTION_MAIN);
//                intent.addCategory(Intent.CATEGORY_APP_MESSAGING);
//                mContext.startActivity(intent);
//            }
//        });
//    }
//
//    @Override
//    public int getItemCount() {
//        return smsMessages.size();
//    }
//}
//
//

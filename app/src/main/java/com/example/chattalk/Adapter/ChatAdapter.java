package com.example.chattalk.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chattalk.Models.MessageModel;
import com.example.chattalk.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter{

    ArrayList<MessageModel> messageModels;
    Context context;
    int SENDER_VIEW_TYPE = 1;
    int RECEIVER_VIEW_TYPE = 2;

    public ChatAdapter(ArrayList<MessageModel> messageModels, Context context) {
        this.messageModels = messageModels;
        this.context = context;
    }

    //This function will run after getItemViewType
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == SENDER_VIEW_TYPE){
            View view = LayoutInflater.from(context).inflate(R.layout.single_row_sender,parent,false);
            return new SenderViewHolder(view);
        }
        else {
            View view = LayoutInflater.from(context).inflate(R.layout.single_row_reciever,parent,false);
            return new RecieverViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MessageModel model = messageModels.get(position);

        if(holder.getClass() == SenderViewHolder.class){

            ((SenderViewHolder) holder).senderMsg.setText(model.getMessage());
            ((SenderViewHolder)holder).senderTime.setText(model.getTimestamp().toString());

        }
        else{

            ((RecieverViewHolder) holder).receiverMsg.setText(model.getMessage());

        }

    }

    @Override
    public int getItemCount() {
        return messageModels.size();
    }

    @Override
    public int getItemViewType(int position) {

        //If the person who are messaging is sender then we show sender layout in app
        //We can identify sender, if person who is logging orsigning we can say that person is sender otherwise receiver

        if(messageModels.get(position).getuId().equals(FirebaseAuth.getInstance().getUid())){
            return SENDER_VIEW_TYPE;
        }
        else {
            return RECEIVER_VIEW_TYPE;
        }

    }

    public class RecieverViewHolder extends RecyclerView.ViewHolder {
        TextView receiverMsg, receiverTime;
        public RecieverViewHolder(@NonNull View itemView) {
            super(itemView);
            receiverMsg = itemView.findViewById(R.id.receive_msg);
            receiverTime = itemView.findViewById(R.id.receive_time);
        }
    }
    public class SenderViewHolder extends RecyclerView.ViewHolder{
        TextView senderMsg, senderTime;
        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);
            senderMsg = itemView.findViewById(R.id.sender_msg);
            senderTime = itemView.findViewById(R.id.sender_time);

        }
    }
}

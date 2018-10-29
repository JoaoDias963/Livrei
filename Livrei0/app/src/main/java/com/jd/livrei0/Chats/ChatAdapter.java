package com.jd.livrei0.Chats;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jd.livrei0.R;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatViewHolders>  {

    private List<ChatObject> chatList;
    private Context context;

    public ChatAdapter(List<ChatObject> chatList, Context context){
        this.chatList = chatList;
        this.context = context;
    }

    @NonNull
    @Override
    public ChatViewHolders onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View layoutView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_chat, null, false);
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(layoutParams);
        ChatViewHolders chatViewHolders = new ChatViewHolders((layoutView));

        return chatViewHolders;
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolders chatViewHolders, int posicao) {
        chatViewHolders.mMensagemChat.setText(chatList.get(posicao).getMensagem());
        if (chatList.get(posicao).getUsuarioAtualBoolean()){
            chatViewHolders.mMensagemChat.setGravity(Gravity.END);
            chatViewHolders.mMensagemChat.setTextColor(Color.parseColor("#404040"));
            chatViewHolders.mChatContainer.setBackgroundColor(Color.parseColor("#F4F4F4"));
        }else{
            chatViewHolders.mMensagemChat.setGravity(Gravity.START);
            chatViewHolders.mMensagemChat.setTextColor(Color.parseColor("#FFFFFF"));
            chatViewHolders.mChatContainer.setBackgroundColor(Color.parseColor("#2DB4C8"));
        }



    }

    @Override
    public int getItemCount() {
        return this.chatList.size();
    }
}

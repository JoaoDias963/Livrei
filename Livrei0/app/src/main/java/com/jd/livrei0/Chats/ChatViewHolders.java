package com.jd.livrei0.Chats;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jd.livrei0.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView mMensagemChat;
    public CircleImageView mImagemPerfil;
    public LinearLayout mChatContainer;

    public ChatViewHolders(View itemView){
        super(itemView);
        itemView.setOnClickListener(this);

        mMensagemChat = itemView.findViewById(R.id.chatmensagem);
        mImagemPerfil = itemView.findViewById(R.id.image_perfil_circular);
        mChatContainer = itemView.findViewById(R.id.chatcontainer);


    }


    @Override
    public void onClick(View v) {

    }
}

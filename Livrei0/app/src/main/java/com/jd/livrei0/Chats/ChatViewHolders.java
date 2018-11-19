package com.jd.livrei0.Chats;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jd.livrei0.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView mMensagemChatEnviado, mMensagemChatRecebido,mTituloChat;
    public CircleImageView mImagemPerfilEnviado,mImagemPerfilRecebido;
    public RelativeLayout mChatContainerEnviado,mChatContainerRecebido;


    public ChatViewHolders(View itemView){
        super(itemView);
        itemView.setOnClickListener(this);

        mTituloChat = itemView.findViewById(R.id.textoTituloChat);

        mMensagemChatRecebido = itemView.findViewById(R.id.chatmensagem);
        mImagemPerfilRecebido = itemView.findViewById(R.id.image_perfil_circular);
        mChatContainerRecebido = itemView.findViewById(R.id.chatcontainer);

        mMensagemChatEnviado = itemView.findViewById(R.id.chatmensagem_sender);
        mImagemPerfilEnviado = itemView.findViewById(R.id.image_perfil_circular_sender);
        mChatContainerEnviado =itemView.findViewById(R.id.chatcontainersender);


    }


    @Override
    public void onClick(View v) {

    }

}

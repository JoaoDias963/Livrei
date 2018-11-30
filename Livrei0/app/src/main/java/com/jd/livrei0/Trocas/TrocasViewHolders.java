package com.jd.livrei0.Trocas;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jd.livrei0.Chats.ChatActivity;
import com.jd.livrei0.R;

public class TrocasViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView mTrocasIdNome;
    public TextView mTrocaId;
    public TextView mStatusTroca;
    public ImageView mImagemTroca;

    public TrocasViewHolders(View itemView){
        super(itemView);
        itemView.setOnClickListener(this);

        mTrocasIdNome = (TextView) itemView.findViewById(R.id.TrocaUsuario);
        mStatusTroca = (TextView) itemView.findViewById(R.id.StatusTroca);
        mStatusTroca.setVisibility(TextView.INVISIBLE);
        mImagemTroca = (ImageView) itemView.findViewById(R.id.imgTroca);
        mTrocaId = (TextView) itemView.findViewById(R.id.idTroca);
        mTrocaId.setVisibility(TextView.INVISIBLE);


    }


    @Override
    public void onClick(View v) {
        Intent intent = new Intent(itemView.getContext(), ChatActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("trocaId", mTrocaId.getText().toString());
        bundle.putString("status", mStatusTroca.getText().toString());

        intent.putExtras(bundle);
        itemView.getContext().startActivity(intent);
    }


}

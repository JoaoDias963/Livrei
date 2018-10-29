package com.jd.livrei0.Trocas;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.jd.livrei0.R;

import java.util.List;

public class TrocasAdapter extends RecyclerView.Adapter<TrocasViewHolders>  {

    private List<TrocasObject> listaTrocas;
    private Context context;

    public TrocasAdapter(List<TrocasObject> listaTrocas, Context context){
        this.listaTrocas = listaTrocas;
        this.context = context;
    }

    @NonNull
    @Override
    public TrocasViewHolders onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View layoutView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.trocas_item, null, false);
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(layoutParams);
        TrocasViewHolders trocasViewHolders = new TrocasViewHolders((layoutView));

        return trocasViewHolders;
    }

    @Override
    public void onBindViewHolder(@NonNull TrocasViewHolders trocasViewHolders, int posicao) {

        trocasViewHolders.mTrocasIdNome.setText("Dono do livro: " + listaTrocas.get(posicao).getNome());
        trocasViewHolders.mStatusTroca.setText("Situação da troca: " + listaTrocas.get(posicao).getStatus());
        trocasViewHolders.mTrocaId.setText(listaTrocas.get(posicao).getIdTroca());
        if (listaTrocas.get(posicao).getimgUrlLivro() != null){
            Glide.with(context).load(listaTrocas.get(posicao).getimgUrlLivro()).apply(new RequestOptions().circleCrop()).into(trocasViewHolders.mImagemTroca);
        }

    }

    @Override
    public int getItemCount() {
        return this.listaTrocas.size();
    }
}

package com.jd.livrei0.Cards;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jd.livrei0.R;

import java.util.List;

public class arrayAdapter extends ArrayAdapter<CardAdapter> {

    //Esta classe popula os cards
    Context context;

    public arrayAdapter (Context context, int resourceId, List<CardAdapter> livros){
        super(context, resourceId, livros);
    }

    //popula o card
    public View getView(int position, View convertView, ViewGroup parent){
        CardAdapter cardLivro = getItem(position);

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item, parent, false);
        }

        TextView titulo = (TextView) convertView.findViewById(R.id.livroTitulo);
        ImageView imagemLivro = (ImageView) convertView.findViewById(R.id.imagemLivro);

        titulo.setText(cardLivro.getTitulo());
        //traz a url da imagem com GLIDE
        Glide.with(convertView.getContext()).load(cardLivro.getImgUrl()).into(imagemLivro);


        return convertView;

    }
}

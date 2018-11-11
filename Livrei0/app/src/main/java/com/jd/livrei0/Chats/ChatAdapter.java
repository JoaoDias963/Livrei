package com.jd.livrei0.Chats;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jd.livrei0.R;
import com.jd.livrei0.Trocas.TrocasActivity;
import com.jd.livrei0.Trocas.TrocasAdapter;
import com.jd.livrei0.Trocas.TrocasObject;
import com.jd.livrei0.Trocas.TrocasViewHolders;

import java.io.File;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatViewHolders>  {

    private List<ChatObject> chatList;
    private Context context;

    private String usuarioLogado = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private DatabaseReference mDadosUsuarioLogado = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(usuarioLogado);
    private DatabaseReference mDadosUsuarioTroca, mDatabaseChat ;

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
    public void onBindViewHolder(@NonNull final ChatViewHolders chatViewHolders, int posicao) {

        chatViewHolders.mMensagemChat.setText(chatList.get(posicao).getMensagem());
        if (chatList.get(posicao).getUsuarioAtualBoolean()){
            chatViewHolders.mMensagemChat.setGravity(Gravity.END);
            //monta img perfil

            DatabaseReference foto =mDadosUsuarioLogado.child("urlFotoPerfil");
            foto.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        String url = dataSnapshot.getValue().toString();
                        Glide.with(context).load(url).into(chatViewHolders.mImagemPerfil);
                    }




                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


            chatViewHolders.mMensagemChat.setTextColor(Color.parseColor("#404040"));
            chatViewHolders.mChatContainer.setBackgroundColor(Color.parseColor("#F4F4F4"));
        }



        else {
            //testa foto outro usuario
            String troca = chatList.get(posicao).getTrocaId();

            mDatabaseChat = FirebaseDatabase.getInstance().getReference().child("Chat").child(troca);
            //TESTE

            mDatabaseChat.addChildEventListener(new ChildEventListener() {
                                                    @Override
                                                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                                                        if (dataSnapshot.exists()){

                                                            String criadoPeloUsuario = null;
                                                            if (dataSnapshot.child("CriadoPeloUsuario").getValue() != null){

                                                                criadoPeloUsuario = dataSnapshot.child("CriadoPeloUsuario").getValue().toString();
                                                                Log.d("USUARIO",criadoPeloUsuario);
                                                                if (!criadoPeloUsuario.equals(usuarioLogado)){
                                                                    mDadosUsuarioTroca = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(criadoPeloUsuario).child("urlFotoPerfil");
                                                                    mDadosUsuarioTroca.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                        @Override
                                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                            if (dataSnapshot.exists()){
                                                                                String url = dataSnapshot.getValue().toString();
                                                                                Glide.with(context).load(url).into(chatViewHolders.mImagemPerfil);
                                                                            }
                                                                        }

                                                                        @Override
                                                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                        }
                                                                    });
                                                                }
                                                            }
                                                        }
                                                    }

                                                    @Override
                                                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                                                    }

                                                    @Override
                                                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                                                    }

                                                    @Override
                                                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                                    }
                                                });


                    //FIM TESTE
           /* mDadosUsuarioTroca = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(troca).child("urlFotoPerfil");
            //DatabaseReference foto =mDadosUsuarioLogado.child("urlFotoPerfil");
            mDadosUsuarioTroca.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        String url = dataSnapshot.getValue().toString();
                        Glide.with(context).load(url).into(chatViewHolders.mImagemPerfil);
                    }
*/
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

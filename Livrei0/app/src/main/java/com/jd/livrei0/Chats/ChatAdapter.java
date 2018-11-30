package com.jd.livrei0.Chats;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

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

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatAdapter extends RecyclerView.Adapter<ChatViewHolders> {

    private List<ChatObject> chatList;
    private Context context;

    private String usuarioLogado = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private DatabaseReference mDadosUsuarioLogado ;
    private DatabaseReference mDadosUsuarioTroca, mDatabaseChat;


    public ChatAdapter(List<ChatObject> chatList, Context context) {
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
    public void onBindViewHolder(@NonNull final ChatViewHolders chatViewHolders, final int posicao) {

        mDadosUsuarioLogado =  FirebaseDatabase.getInstance().getReference().child("Usuarios").child(usuarioLogado);

        //chatViewHolders.mTituloChat.setText(chatList.get(posicao).getTituloStatus());

        //usuario atual
        if (chatList.get(posicao).getUsuarioAtualBoolean()) {

            chatViewHolders.mMensagemChatEnviado.setText(chatList.get(posicao).getMensagem());
            chatViewHolders.mMensagemChatRecebido.setVisibility(View.INVISIBLE);
            chatViewHolders.mChatContainerRecebido.setVisibility(View.INVISIBLE);
            //monta img perfil

            DatabaseReference foto = mDadosUsuarioLogado.child("urlFotoPerfil");
            foto.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String url = dataSnapshot.getValue().toString();
                        //chatViewHolders.mImagemPerfilEnviado.setX(950);
                        Glide.with(context).load(url).into(chatViewHolders.mImagemPerfilEnviado);
                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            chatViewHolders.mChatContainerEnviado.setGravity(Gravity.END);
            chatViewHolders.mMensagemChatEnviado.setGravity(Gravity.END);
            chatViewHolders.mMensagemChatEnviado.setPadding(0, 0, 150, 0);
            chatViewHolders.mMensagemChatEnviado.setTextColor(Color.parseColor("#000000"));
            chatViewHolders.mChatContainerEnviado.setBackgroundColor(Color.parseColor("#FFCC80"));
            chatViewHolders.mChatContainerEnviado.setBackgroundResource(R.drawable.redondo_usuario);
            //chatViewHolders.mChatContainer.getLayoutParams().width = 1000;


        } else {//usuario do chat

            chatViewHolders.mMensagemChatRecebido.setText(chatList.get(posicao).getMensagem());
            chatViewHolders.mMensagemChatEnviado.setVisibility(View.INVISIBLE);
            chatViewHolders.mChatContainerEnviado.setVisibility(View.INVISIBLE);

            //testa foto outro usuario
            String troca = chatList.get(posicao).getTrocaId();

            mDatabaseChat = FirebaseDatabase.getInstance().getReference().child("Chat").child(troca);
            //TESTE
/*


                            if (!criadoPeloUsuario.equals(usuarioLogado)) {


                                //tesste pega status da troca
                                mDadosUsuarioLogado = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(usuarioLogado).child("Trocas").child(criadoPeloUsuario).child("Status");
                                mDadosUsuarioLogado.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()){
                                            //Log.d("ENTROU", dataSnapshot.getValue().toString());
                                            String status = dataSnapshot.getValue().toString();

                                            //chatViewHolders.mTituloChat.setText(status);

                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                                //fim teste status*/

            mDatabaseChat.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    if (dataSnapshot.exists()) {

                        String criadoPeloUsuario = null;
                        if (dataSnapshot.child("CriadoPeloUsuario").getValue() != null) {

                            criadoPeloUsuario = dataSnapshot.child("CriadoPeloUsuario").getValue().toString();

                            final String[] urlfoto = {""};
                            mDadosUsuarioTroca = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(criadoPeloUsuario).child("urlFotoPerfil");
                            mDadosUsuarioTroca.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                         String url = dataSnapshot.getValue().toString();


                                         //urlfoto[0] = url;
                                            carregarFoto(url, chatViewHolders.mImagemPerfilRecebido);//Glide.with(context).load(url).into(chatViewHolders.mImagemPerfil);



                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                            //carregarFoto(urlfoto[0], chatViewHolders.mImagemPerfilRecebido);//Glide.with(context).load(url).into(chatViewHolders.mImagemPerfil);

                        }


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
                        chatViewHolders.mMensagemChatRecebido.setGravity(Gravity.START);

                        chatViewHolders.mMensagemChatRecebido.setTextColor(Color.parseColor("#000000"));
                        chatViewHolders.mChatContainerRecebido.setBackgroundColor(Color.parseColor("#FFF3E0"));

                        chatViewHolders.mChatContainerRecebido.setBackgroundResource(R.drawable.redondo);


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
        }
    }


    private void carregarFoto(String url, CircleImageView chatViewHolders) {

        Glide.with(context.getApplicationContext()).load(url).into(chatViewHolders);
    }

    @Override
    public int getItemCount() {
        return this.chatList.size();
    }


}



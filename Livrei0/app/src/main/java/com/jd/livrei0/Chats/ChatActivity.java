package com.jd.livrei0.Chats;


import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jd.livrei0.Dialog.DialogTroca;
import com.jd.livrei0.R;
import com.jd.livrei0.Trocas.TrocasAdapter;
import com.jd.livrei0.Trocas.TrocasObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mChatAdapter;
    private RecyclerView.LayoutManager mChatLayoutManager;

    private EditText mEnviarEditText;
    private Button mBotaoEnviar;
    private FloatingActionButton mFab;
    private TextView mStatusTitulo;

    private String statusTrocaUsuario = "";
    private String statusTrocaAtual = "";

    private NestedScrollView mNestedScrollView;




    private String usuarioAtual, trocaId, chatId, status;

    DatabaseReference mDatabaseFotoUserAtual, mDatabaseChat, mDatabaseUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);



        //pega o id criado no bundle do viewholder
        trocaId = getIntent().getExtras().getString("trocaId");
        status = getIntent().getExtras().getString("status");

        usuarioAtual = FirebaseAuth.getInstance().getUid();

        mDatabaseFotoUserAtual = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(usuarioAtual).child("urlFotoPerfil");
        mDatabaseChat = FirebaseDatabase.getInstance().getReference().child("Chat").child(trocaId);
        mDatabaseUser = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(usuarioAtual).child("Trocas").child(trocaId);
        getMensagensChat();
        //getChatId();



        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setHasFixedSize(false);
        mChatLayoutManager = new LinearLayoutManager(ChatActivity.this);

        mNestedScrollView = (NestedScrollView) findViewById(R.id.nestedScroll);


        mRecyclerView.setLayoutManager(mChatLayoutManager);
        mChatAdapter = new ChatAdapter(getDataSetChat(), ChatActivity.this);
        mRecyclerView.setAdapter(mChatAdapter);

        mFab = (FloatingActionButton) findViewById(R.id.fab);

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                realizaTroca();
            }
        });

        mStatusTitulo = findViewById(R.id.textoTituloChat);

        mEnviarEditText = findViewById(R.id.mensagem);
        mBotaoEnviar = findViewById(R.id.enviarMsg);


        //teste rolar view
        //mRecyclerView.smoothScrollToPosition(mRecyclerView.getAdapter().getItemCount()-1);
        //


        mBotaoEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enviarMensagem();
                //mNestedScrollView.fullScroll(NestedScrollView.FOCUS_DOWN);
            }


        });


    }

    private void realizaTroca() {


        DialogTroca dialogTroca = new DialogTroca();
        Bundle bundle = new Bundle();

        bundle.putString("TROCA", trocaId);
        dialogTroca.setArguments(bundle);

        dialogTroca.show(getSupportFragmentManager(), "DialogTroca");





    }

    private void getMensagensChat() {

        //TESTE STATUS
       // Log.d("USUARIO", mDatabaseUser.getKey().toString());

        DatabaseReference chatTroca = FirebaseDatabase.getInstance().getReference().child("Chat").child(mDatabaseUser.getKey());
        chatTroca.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.exists()){
                     String criadopelousuario = null;

                    if (dataSnapshot.child("CriadoPeloUsuario").getValue() != null){
                        criadopelousuario = dataSnapshot.child("CriadoPeloUsuario").getValue().toString();

                        if(!criadopelousuario.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                            ///////////////////////////////
                            final DatabaseReference statusTroca = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(criadopelousuario).child("Trocas").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Status");
                            final String finalCriadopelousuario = criadopelousuario;
                            statusTroca.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()){



                                        final String statusUsuarioTroca = dataSnapshot.getValue().toString();

                                        //teste comparar
                                        DatabaseReference statusTrocaUsuarioAtual = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Trocas").child(finalCriadopelousuario).child("Status");
                                        statusTrocaUsuarioAtual.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                if (dataSnapshot.exists()){
                                                    final String statusUsuarioAtual = dataSnapshot.getValue().toString();

                                                   setarStatusNaView(statusUsuarioTroca,statusUsuarioAtual);
                                                   statusTrocaUsuario = statusUsuarioTroca;
                                                   statusTrocaAtual = statusUsuarioAtual;
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });


                                    }
                                   // mNestedScrollView.fullScroll(NestedScrollView.FOCUS_DOWN);


                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });





                            ///////////////////////////////////////

                        }
                    }


                } mNestedScrollView.post(new Runnable() {
                    public void run() {
                        mNestedScrollView.fullScroll(NestedScrollView.FOCUS_DOWN);
                    }
                });
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







        //FIM TESTE STATUS
        mDatabaseChat.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.exists()){

                    String mensagem = null;
                    String criadoPeloUsuario = null;

                    if (dataSnapshot.child("texto").getValue() != null){
                        mensagem = dataSnapshot.child("texto").getValue().toString();
                    }
                    if (dataSnapshot.child("CriadoPeloUsuario").getValue() != null){
                        criadoPeloUsuario = dataSnapshot.child("CriadoPeloUsuario").getValue().toString();
                    }

                    if (mensagem != null && criadoPeloUsuario!=null){
                        Boolean usuarioAtualBoolean = false;


                        if (criadoPeloUsuario.equals(usuarioAtual)){
                            usuarioAtualBoolean = true;


                            ChatObject novaMensagem = new ChatObject(mensagem,usuarioAtualBoolean,trocaId, status);
                            resultChat.add(novaMensagem);


                            //teste rolagem


                            mChatAdapter.notifyDataSetChanged();
                            //mNestedScrollView.fullScroll(NestedScrollView.FOCUS_DOWN);

                            mNestedScrollView.post(new Runnable() {
                                public void run() {
                                    mNestedScrollView.fullScroll(NestedScrollView.FOCUS_DOWN);
                                }
                            });



                        }else{

                            ChatObject novaMensagem = new ChatObject(mensagem,usuarioAtualBoolean, trocaId, status);
                            resultChat.add(novaMensagem);


                            mChatAdapter.notifyDataSetChanged();
                            //mNestedScrollView.fullScroll(NestedScrollView.FOCUS_DOWN);

                            mNestedScrollView.post(new Runnable() {
                                public void run() {
                                    mNestedScrollView.fullScroll(NestedScrollView.FOCUS_DOWN);
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
    }

    private void enviarMensagem() {

        String enviarMensagemTexto = mEnviarEditText.getText().toString();
        View view = getWindow().getDecorView().findViewById(android.R.id.content);

        if (!enviarMensagemTexto.isEmpty()){
            DatabaseReference newMessageDb = mDatabaseChat.push();

            Map novaMensagem = new HashMap();
            novaMensagem.put("CriadoPeloUsuario", usuarioAtual);
            novaMensagem.put("texto", enviarMensagemTexto);

            newMessageDb.setValue(novaMensagem);
           // hideKeyboard(view);
           /* mNestedScrollView.post(new Runnable() {
                public void run() {
                    mNestedScrollView.fullScroll(NestedScrollView.FOCUS_DOWN);
                }
            });*/

        }

        mEnviarEditText.setText("");







    }

    private void getChatId(){
        mDatabaseUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    chatId = dataSnapshot.getValue().toString();
                    mDatabaseChat = mDatabaseChat.child(chatId);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void hideKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }


    private ArrayList<ChatObject> resultChat = new ArrayList<ChatObject>();
    private List<ChatObject> getDataSetChat() {
        return resultChat;
    }


    private void setarStatusNaView(String statusUsuarioTroca, String statusUsuarioAtual){

        if (statusUsuarioTroca.equals(statusUsuarioAtual)){
            mStatusTitulo.setText("Status da troca: "+ statusUsuarioAtual);
        }
            if (statusUsuarioAtual.equals("Pendente") && statusUsuarioTroca.equals("Troca realizada com sucesso")){
                mStatusTitulo.setText("Status da troca: " + statusUsuarioAtual);
            }
                if (statusUsuarioAtual.equals("Troca realizada com sucesso") && statusUsuarioTroca.equals("Pendente")){
                    mStatusTitulo.setText("Status da troca: "+ statusUsuarioTroca);
                }



    }


}

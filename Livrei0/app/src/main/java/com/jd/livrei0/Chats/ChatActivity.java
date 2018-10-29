package com.jd.livrei0.Chats;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jd.livrei0.R;
import com.jd.livrei0.Trocas.TrocasAdapter;
import com.jd.livrei0.Trocas.TrocasObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mChatAdapter;
    private RecyclerView.LayoutManager mChatLayoutManager;

    private EditText mEnviarEditText;
    private Button mBotaoEnviar;

    private String usuarioAtual, trocaId, chatId;

    DatabaseReference mDatabaseUser, mDatabaseChat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);


        //pega o id criado no bundle do viewholder
        trocaId = getIntent().getExtras().getString("trocaId");

        usuarioAtual = FirebaseAuth.getInstance().getUid();

        //mDatabaseUser = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(usuarioAtual).child("Trocas").child(trocaId);
        mDatabaseChat = FirebaseDatabase.getInstance().getReference().child("Chat").child(trocaId);
        getMensagensChat();
        //getChatId();

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setHasFixedSize(false);
        mChatLayoutManager = new LinearLayoutManager(ChatActivity.this);
        mRecyclerView.setLayoutManager(mChatLayoutManager);
        mChatAdapter = new ChatAdapter(getDataSetChat(), ChatActivity.this);
        mRecyclerView.setAdapter(mChatAdapter);

        mEnviarEditText = findViewById(R.id.mensagem);
        mBotaoEnviar = findViewById(R.id.enviarMsg);

        mBotaoEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enviarMensagem();
            }


        });

    }

    private void getMensagensChat() {
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
                        }
                        ChatObject novaMensagem = new ChatObject(mensagem,usuarioAtualBoolean);
                        resultChat.add(novaMensagem);
                        mChatAdapter.notifyDataSetChanged();

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

        if (!enviarMensagemTexto.isEmpty()){
            DatabaseReference newMessageDb = mDatabaseChat.push();

            Map novaMensagem = new HashMap();
            novaMensagem.put("CriadoPeloUsuario", usuarioAtual);
            novaMensagem.put("texto", enviarMensagemTexto);

            newMessageDb.setValue(novaMensagem);
        }
        mEnviarEditText.setText(null);

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


    private ArrayList<ChatObject> resultChat = new ArrayList<ChatObject>();
    private List<ChatObject> getDataSetChat() {
        return resultChat;
    }
}

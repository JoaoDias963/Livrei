package com.jd.livrei0.Trocas;

import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jd.livrei0.R;

import java.util.ArrayList;
import java.util.List;

public class TrocasActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mTrocasAdapter;
    private RecyclerView.LayoutManager mTrocasLayoutManager;

    private String usuarioAtual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trocas);
        usuarioAtual = FirebaseAuth.getInstance().getUid();

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setHasFixedSize(true);
        mTrocasLayoutManager = new LinearLayoutManager(TrocasActivity.this);
        mRecyclerView.setLayoutManager(mTrocasLayoutManager);
        mTrocasAdapter = new TrocasAdapter(getDataSetTrocas(), TrocasActivity.this);
        mRecyclerView.setAdapter(mTrocasAdapter);

        getIdTroca();





    }

    private void getIdTroca() {

        DatabaseReference mTrocaDb = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(usuarioAtual).child("Trocas");
        mTrocaDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot troca: dataSnapshot.getChildren()){
                        informacoesDaTroca(troca.getKey());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void informacoesDaTroca(String key) {

        //referencia ao id da troca atual
        //final String idDaTroca = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(usuarioAtual).child("Trocas").child(key).getKey();
        final DatabaseReference mTrocaDb = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(usuarioAtual).child("Trocas").child(key);


        mTrocaDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    final String userId = dataSnapshot.getKey();


                    DatabaseReference mUsuarioTroca = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(userId);
                    /*
                    /////SEM TEMPO REAL
                    mUsuarioTroca.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String nome = "";
                            String imagemLivro = "";
                            String imagemPerfil = "";
                            String titulo = "";
                            String status = "";
                            String idTroca = "";
                            if (dataSnapshot.child("Nome").getKey() != null) {
                                nome = dataSnapshot.child("Nome").getValue().toString();

                            }
                            if (dataSnapshot.child("urlFotoPerfil").getKey() != null) {
                                imagemPerfil = dataSnapshot.child("urlFotoPerfil").getValue().toString();

                            }
                            if (dataSnapshot.child("Doacao").child("urlFotoDoacaoLivro").getKey() != null) {
                                imagemLivro = dataSnapshot.child("Doacao").child("urlFotoDoacaoLivro").getValue().toString();

                            }
                            if (dataSnapshot.child("Doacao").child("titulo").getKey() != null) {
                                titulo = dataSnapshot.child("Doacao").child("titulo").getValue().toString();

                            }
                            if (dataSnapshot.child("Trocas").child(usuarioAtual).child("Status").getKey() != null) {
                                status = dataSnapshot.child("Trocas").child(usuarioAtual).child("Status").getValue().toString();
                            }
                            if (dataSnapshot.child("Trocas").child(usuarioAtual).getKey() != null){
                                //IDTROCA FORMADO PELO ID DO USUARIO + CHATID
                                idTroca = dataSnapshot.child("Trocas").child(usuarioAtual).child("ChatId").getValue().toString();
                            }


                            TrocasObject objetoTroca = new TrocasObject(userId, nome, imagemPerfil, imagemLivro, titulo, status, idTroca);
                            resultTrocas.add(objetoTroca);
                            mTrocasAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    //FIM SEM TEMPO REAL
                    */

                    //TESTE TEMPO REAL
                    mUsuarioTroca.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                            String nome = "";
                            String imagemLivro = "";
                            String imagemPerfil = "";
                            String titulo = "";
                            String status = "";
                            String idTroca = "";
                            if (dataSnapshot.child("Nome").getKey() != null) {
                                nome = dataSnapshot.child("Nome").getValue().toString();

                            }
                            if (dataSnapshot.child("urlFotoPerfil").getKey() != null) {
                                imagemPerfil = dataSnapshot.child("urlFotoPerfil").getValue().toString();

                            }
                            if (dataSnapshot.child("Doacao").child("urlFotoDoacaoLivro").getKey() != null) {
                                imagemLivro = dataSnapshot.child("Doacao").child("urlFotoDoacaoLivro").getValue().toString();

                            }
                            if (dataSnapshot.child("Doacao").child("titulo").getKey() != null) {
                                titulo = dataSnapshot.child("Doacao").child("titulo").getValue().toString();

                            }
                            if (dataSnapshot.child("Trocas").child(usuarioAtual).child("Status").getKey() != null) {
                                status = dataSnapshot.child("Trocas").child(usuarioAtual).child("Status").getValue().toString();
                            }
                            if (dataSnapshot.child("Trocas").child(usuarioAtual).getKey() != null){
                                //IDTROCA FORMADO PELO ID DO USUARIO + CHATID
                                idTroca = dataSnapshot.child("Trocas").child(usuarioAtual).child("ChatId").getValue().toString();
                            }


                            TrocasObject objetoTroca = new TrocasObject(userId, nome, imagemPerfil, imagemLivro, titulo, status, idTroca);
                            resultTrocas.add(objetoTroca);
                            mTrocasAdapter.notifyDataSetChanged();
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
                    //FIM TESTE TEMPO REAL


                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            };


    });
    }

    private ArrayList<TrocasObject> resultTrocas = new ArrayList<TrocasObject>();
    private List<TrocasObject> getDataSetTrocas() {
        return resultTrocas;
    }
}

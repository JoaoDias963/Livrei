package com.jd.livrei0.Fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jd.livrei0.Chats.ChatActivity;
import com.jd.livrei0.Chats.ChatAdapter;
import com.jd.livrei0.Chats.ChatObject;
import com.jd.livrei0.R;
import com.jd.livrei0.Trocas.TrocasActivity;
import com.jd.livrei0.Trocas.TrocasAdapter;
import com.jd.livrei0.Trocas.TrocasObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class TrocasDisponiveisFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mTrocasAdapter;
    private RecyclerView.LayoutManager mTrocasLayoutManager;

    private String usuarioAtual;
    public TrocasDisponiveisFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_trocas_disponiveis, container, false);

        usuarioAtual = FirebaseAuth.getInstance().getUid();

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setHasFixedSize(true);
        mTrocasLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mTrocasLayoutManager);
        mTrocasAdapter = new TrocasAdapter(getDataSetTrocas(), getActivity());
        mRecyclerView.setAdapter(mTrocasAdapter);

        getIdTroca();


        return view;
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
                    mUsuarioTroca.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists() && dataSnapshot.child("Trocas").child(usuarioAtual).child("Status").getValue().equals("Troca realizada com sucesso")){
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
                                if (dataSnapshot.child("Doacao").exists() && dataSnapshot.child("Doacao").child("urlFotoDoacaoLivro").getKey() != null) {
                                    imagemLivro = dataSnapshot.child("Doacao").child("urlFotoDoacaoLivro").getValue().toString();

                                }
                                if (dataSnapshot.child("Doacao").exists() && dataSnapshot.child("Doacao").child("titulo").getKey() != null) {
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

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


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

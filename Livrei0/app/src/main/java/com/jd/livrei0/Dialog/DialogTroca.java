package com.jd.livrei0.Dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jd.livrei0.Chats.ChatActivity;
import com.jd.livrei0.R;

import java.util.HashMap;
import java.util.Map;

public class DialogTroca extends DialogFragment {

    DatabaseReference mTrocasUsuarioAtual, mDatabaseChat, mStatusUsuarioTroca, mTrocasUsuarioTroca;



    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {



        Bundle bundle = this.getArguments();
        final String idTroca = bundle.getString("TROCA");
        Log.d("TROCA",idTroca);

        mDatabaseChat = FirebaseDatabase.getInstance().getReference().child("Chat").child(idTroca);

        final String usuarioAtual = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mTrocasUsuarioAtual = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(usuarioAtual);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMessage("Para que possamos indicar se a troca foi realizada, ambos os usuários devem concordar")
                .setPositiveButton("SIM, OS LIVROS FORAM TROCADOS", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mDatabaseChat.addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                                if (dataSnapshot.exists()){
                                    String criadoPeloUsuario = null;
                                    if (dataSnapshot.child("CriadoPeloUsuario").getValue() != null){
                                        criadoPeloUsuario = dataSnapshot.child("CriadoPeloUsuario").getValue().toString();

                                        if (!criadoPeloUsuario.equals(usuarioAtual)){
                                            mTrocasUsuarioAtual.child("Trocas").child(criadoPeloUsuario).child("ConfirmadoPeloUsuario").setValue(usuarioAtual);
                                            Toast.makeText(getContext(),"Se o status da troca não mudou, o outro usuário ainda não confirmou a troca",Toast.LENGTH_LONG).show();


                                            //testa setar troca realizada
                                            mStatusUsuarioTroca = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(criadoPeloUsuario).child("Trocas").child(usuarioAtual);
                                            final String finalCriadoPeloUsuario = criadoPeloUsuario;

                                            mStatusUsuarioTroca.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    if (dataSnapshot.exists()){
                                                        if (dataSnapshot.child("ConfirmadoPeloUsuario").getValue().equals(finalCriadoPeloUsuario)){




                                                            //usuario atual
                                                            mTrocasUsuarioAtual = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(usuarioAtual).child("Trocas").child(finalCriadoPeloUsuario).child("Status");
                                                            mTrocasUsuarioAtual.setValue("Troca realizada com sucesso");

                                                            //Usuario da troca
                                                            //mTrocasUsuarioTroca = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(finalCriadoPeloUsuario).child("Trocas").child(usuarioAtual).child("Status");
                                                           // mTrocasUsuarioTroca.setValue("Troca realizada com sucesso");



                                                        }



                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                }
                                            });

                                            //fim testa setar troca realizada
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
                })
                .setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getContext(), "Combine uma forma para trocar seu livro com o outro usuário",Toast.LENGTH_LONG).show();
                    }
                });

        builder.setTitle("A troca foi realizada?");

        AlertDialog dialog = builder.create();

        return dialog;
    }



}

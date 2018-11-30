package com.jd.livrei0;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jd.livrei0.Cards.CardAdapter;
import com.jd.livrei0.Cards.arrayAdapter;
import com.jd.livrei0.Fragment.LivrosFragment;
import com.jd.livrei0.Fragment.TrocasDisponiveisFragment;
import com.jd.livrei0.TabAdapter.TabAdapter;
import com.jd.livrei0.Trocas.TrocasActivity;
import com.jd.livrei0.Utils.SlidingTabLayout;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

        private CardAdapter cardsData[];
        private com.jd.livrei0.Cards.arrayAdapter arrayAdapter;
        private int i;
        private Toolbar toolbar;

        private FirebaseAuth mFirebaseAuth;
        private DatabaseReference mUsuarioDb , mChatDb;

        ListView listView;
        List<CardAdapter> rowLivros;

        private String TrocaId;

        //teste fragment
        private SlidingTabLayout slidingTabLayout;
        private ViewPager viewPager;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);



            mUsuarioDb = FirebaseDatabase.getInstance().getReference().child("Usuarios");
            mChatDb = FirebaseDatabase.getInstance().getReference().child("Chat");
            mFirebaseAuth = FirebaseAuth.getInstance();

            toolbar = (Toolbar) findViewById(R.id.toolbar_principal);
            toolbar.setTitle("Livrei");
            setSupportActionBar(toolbar);



            //sair = (Button) findViewById(R.id.btnSair);

       /*     sair.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deslogar();
                }
            });
*/
            checarDoacoes();

            rowLivros = new ArrayList<CardAdapter>();

            arrayAdapter = new arrayAdapter(this, R.layout.item, rowLivros  );

            //SwipeFlingAdapterView flingContainer = (SwipeFlingAdapterView) findViewById(R.id.frame);

            //teste fragment
            slidingTabLayout = (SlidingTabLayout) findViewById(R.id.slt_tab);
            viewPager = (ViewPager) findViewById(R.id.vp_pagina);
            //

            //


            slidingTabLayout.setDistributeEvenly(true);

            //configura adapter
            final TabAdapter tabAdapter = new TabAdapter(getSupportFragmentManager());
            viewPager.setAdapter(tabAdapter);

            slidingTabLayout.setViewPager(viewPager);



            slidingTabLayout.setSelectedIndicatorColors(ContextCompat.getColor(this, R.color.colorSlide));




           // flingContainer.setAdapter(arrayAdapter);
          /*  flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
                @Override
                public void removeFirstObjectInAdapter() {
                    // this is the simplest way to delete an object from the Adapter (/AdapterView)
                    Log.d("LIST", "removed object!");
                    rowLivros.remove(0);
                    arrayAdapter.notifyDataSetChanged();
                }

                @Override
                public void onLeftCardExit(Object dataObject) {
                    //Do something on the left!
                    //You also have access to the original object.
                    //If you want to use it just cast it (String) dataObject

                    //faz o cast para o adapter transformando num objeto que pode ser usado
                    CardAdapter livro = (CardAdapter) dataObject;
                    String userId = livro.getUserId();
                    String titulo = livro.getTitulo();

                    //cria no banco o registro do "NaoInteressa"/deslizar para esquerda
                    //estrutura: "Usuarios" > "NaoInteressa" > titulo > idUsuario Dono > id Usuario atual
                    mUsuarioDb.child(mFirebaseAuth.getCurrentUser().getUid()).child("NaoInteressa").child(userId).child(titulo).setValue(true);
                    Toast.makeText(MainActivity.this, "Este livro não me interessa",Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onRightCardExit(Object dataObject) {
                    //faz o cast para o adapter transformando num objeto que pode ser usado
                    CardAdapter livro = (CardAdapter) dataObject;
                    String userId = livro.getUserId();
                    String titulo = livro.getTitulo();

                    //cria no banco o registro do "Interessa"/deslizar para direita
                    //estrutura: "Usuarios" > "Interessa" >titulo > idUsuario Dono > id Usuario atual
                    mUsuarioDb.child(mFirebaseAuth.getCurrentUser().getUid()).child("Interessa").child(userId).child(titulo).setValue(true);

                    //testa se os dois usuarios tem interesse nos livros
                    testaSeTroca(userId);
                    Toast.makeText(MainActivity.this, "Este livro me interessou",Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onAdapterAboutToEmpty(int itemsInAdapter) {

                }

                @Override
                public void onScroll(float scrollProgressPercent) {

                }
            });


            // Optionally add an OnItemClickListener
            flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
                @Override
                public void onItemClicked(int itemPosition, Object dataObject) {
                    Toast.makeText(MainActivity.this, "Clique",Toast.LENGTH_SHORT).show();
                }
            });
*/
        }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
            MenuInflater menuInflater = getMenuInflater();
            menuInflater.inflate(R.menu.menu_main, menu);

            return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

            switch (item.getItemId()){
                case R.id.perfil:
                    abrirPerfil();
                    break;
                case R.id.sair:
                    deslogar();
                    break;
                case R.id.cad_livro:
                    cadastrarLivro();
                    break;
                /*case R.id.trocas_disponiveis:
                    mostrarTrocas();
                    break;*/
                default:
                    return super.onOptionsItemSelected(item);
            }
        return super.onOptionsItemSelected(item);
    }

    /*private void testaSeTroca(final String userId) {
            //referência ao livro que apareceu na tela foi para o "Interessa"
            DatabaseReference interresseUsuarioAtual = mUsuarioDb.child(mFirebaseAuth.getCurrentUser().getUid()).child("Interessa").child(userId);
            interresseUsuarioAtual.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        //referencia o usuario recebido e coloca o usuario atual no Trocas
                        final DatabaseReference interesseUsuarioRecebido = mUsuarioDb.child(dataSnapshot.getKey()).child("Interessa").child(mFirebaseAuth.getCurrentUser().getUid());
                        interesseUsuarioRecebido.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    //chatId formado por userId troca+ usuario atual +chatID.push
                                    String chatId = userId + dataSnapshot.getKey() + FirebaseDatabase.getInstance().getReference().child("Chat").push().getKey();

                                    // salvar chatId no "chat" -> child(USUARIO DA TROCA PARA RETORNAR NO CHAT ACTIVITY)
                                    mChatDb.child(chatId).child("UsuarioDaTroca").setValue(userId);

                                    //referencia o usuario encontrado e seta a troca nele
                                    //idTroca

                                    //mUsuarioDb.child(dataSnapshot.getKey()).child("Trocas").child(idTroca).child(userId).setValue(true);
                                    mUsuarioDb.child(dataSnapshot.getKey()).child("Trocas").child(userId).child("ChatId").setValue(chatId);
                                    mUsuarioDb.child(dataSnapshot.getKey()).child("Trocas").child(userId).child("Status").setValue("Pendente");



                                    //referencia o usuario atual e coloca o usuario recebido no Trocas
                                    //mUsuarioDb.child(userId).child("Trocas").child(idTroca).child(mFirebaseAuth.getCurrentUser().getUid()).setValue(true);
                                    mUsuarioDb.child(userId).child("Trocas").child(mFirebaseAuth.getCurrentUser().getUid()).child("ChatId").setValue(chatId);
                                    mUsuarioDb.child(userId).child("Trocas").child(mFirebaseAuth.getCurrentUser().getUid()).child("Status").setValue("Pendente");





                                    Toast.makeText(MainActivity.this, "Você tem uma troca pendente", Toast.LENGTH_LONG).show();
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

                }
            });
    }*/


    public void checarDoacoes() {
        final DatabaseReference mDoacoes = FirebaseDatabase.getInstance().getReference().child("Usuarios");

        mDoacoes.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {


                //se a chave usuario for diferente do logado && se usuario atual já não foi registrado no interessa ou nao interessa
                if (dataSnapshot.exists() && dataSnapshot.child("Doacao").exists() && !dataSnapshot.getKey().equals(mFirebaseAuth.getCurrentUser().getUid() ) && (!dataSnapshot.child("NaoInteressa").hasChild(mFirebaseAuth.getCurrentUser().getUid()))) {

                        //testa se o livro n está em troca pendente
                        if (!dataSnapshot.child("Trocas").child(mFirebaseAuth.getCurrentUser().getUid()).exists() ) {

                            //dataSnapshot.getKey() aqui acessa primeiro filho após "Usuarios" no banco trazendo o UId
                            //dataSnapshot.child("Doacao") acessa o valor de qualquer filho depois do UId com "Doacao"
                            //trocar "urlFotoPerfil" por "urlFotoLivro"
                            CardAdapter livro = new CardAdapter(dataSnapshot.getKey(), dataSnapshot.child("Doacao").child("titulo").getValue().toString(), dataSnapshot.child("Doacao").child("urlFotoDoacaoLivro").getValue().toString());
                            rowLivros.add(livro);
                            arrayAdapter.notifyDataSetChanged();
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

                /*CardAdapter livro = new CardAdapter(dataSnapshot.getKey(), dataSnapshot.child("Doacao").child(TrocaId).child(dataSnapshot.getKey()).child("titulo").getValue().toString(), dataSnapshot.child("Doacao").child(TrocaId).child(dataSnapshot.getKey()).child("urlFotoDoacaoLivro").getValue().toString());
                rowLivros.add(livro);
                arrayAdapter.notifyDataSetChanged();*/

        });
    }






    public void deslogar(){
        mFirebaseAuth.signOut();
        Intent intent = new Intent(MainActivity.this, EscolhaLoginActivity.class);
        startActivity(intent);
        finish();
        return;
    }

    public void abrirPerfil() {
        Intent intent = new Intent(MainActivity.this, PerfilActivity.class);
        startActivity(intent);
        return;
    }

    public void cadastrarLivro() {

            Intent intent = new Intent(MainActivity.this, CadastrarDoacaoActivity.class);
            startActivity(intent);
            return;
    }

    public void mostrarTrocas() {
        Intent intent = new Intent(MainActivity.this, TrocasActivity.class);
        startActivity(intent);
        return;
    }



}

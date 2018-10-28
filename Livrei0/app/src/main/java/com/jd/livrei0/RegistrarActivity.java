package com.jd.livrei0;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegistrarActivity extends AppCompatActivity {


    private TextInputEditText mEmail;
    private TextInputEditText mSenha;
    private TextInputEditText mNome;
    private Button mRegistrar;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mFirebaseAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                if(user != null){
                    Intent intent = new Intent(RegistrarActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    return;
                }
            }
        };


        mRegistrar = (Button) findViewById(R.id.btnRegistrar);
        mNome = (TextInputEditText) findViewById(R.id.intxtNomeRegistrar);
        mEmail = (TextInputEditText) findViewById(R.id.intxEmailRegistrar);
        mSenha = (TextInputEditText) findViewById(R.id.intxtSenhaRegistrar);


        mRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String nome = mNome.getEditableText().toString();
                final String email = mEmail.getEditableText().toString();
                final String senha = mSenha.getEditableText().toString();



                mFirebaseAuth.createUserWithEmailAndPassword(email,senha).addOnCompleteListener(RegistrarActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()){
                            Toast.makeText(RegistrarActivity.this, "Ocorreu um erro ao registrar, tente novamente",Toast.LENGTH_SHORT).show();
                        }else{
                            String userId = mFirebaseAuth.getCurrentUser().getUid();
                            DatabaseReference usuarioAtual = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(userId);
                            usuarioAtual.child("Nome").setValue(nome);
                            usuarioAtual.child("E-mail").setValue(email);
                            //testar intent Perfil
                            Intent intent = new Intent(RegistrarActivity.this, PerfilActivity.class);
                            startActivity(intent);

                        }

                    }
                });
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mFirebaseAuth.addAuthStateListener(mFirebaseAuthStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mFirebaseAuth.removeAuthStateListener(mFirebaseAuthStateListener);
    }
}

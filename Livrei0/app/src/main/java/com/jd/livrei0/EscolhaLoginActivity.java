package com.jd.livrei0;

import android.content.Intent;
import android.graphics.Color;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.jd.livrei0.Utils.GradientDrawable;

public class EscolhaLoginActivity extends AppCompatActivity {

    private Button mEntrar/*, mMaterial*/;
    private TextInputEditText mEmail;
    private TextInputEditText mSenha;
    private TextView mRegistrar;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mFirebaseAuthStateListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escolha_login);
       // getActionBar().hide();

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                if(user != null){
                    Intent intent = new Intent(EscolhaLoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    return;
                }
            }
        };


        mEntrar = (Button) findViewById(R.id.btnLogin);
        mEmail = (TextInputEditText) findViewById(R.id.intxEmail);
        mSenha = (TextInputEditText) findViewById(R.id.intxtSenha);
        mRegistrar = (TextView) findViewById(R.id.txtRegistrar);
        mRegistrar.setHighlightColor(Color.parseColor("#FFD180"));
        mEntrar.setHighlightColor(Color.parseColor("#FFD180"));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
            mEntrar.setBackground(new GradientDrawable().setBorderRoundedBUTTONConfirma());
        }else{
            mEntrar.setBackgroundResource(R.drawable.btn_arredondado_confirma);
        }

        //mMaterial = (Button) findViewById(R.id.material);

        mRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EscolhaLoginActivity.this, RegistrarActivity.class);
                startActivity(intent);
                //finish();
                return;
            }
        });

        mEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = mEmail.getEditableText().toString();
                final String senha = mSenha.getEditableText().toString();
                mFirebaseAuth.signInWithEmailAndPassword(email,senha).addOnCompleteListener(EscolhaLoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()){
                            Toast.makeText(EscolhaLoginActivity.this, "Combinação de e-mail e senha está errada",Toast.LENGTH_SHORT).show();
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

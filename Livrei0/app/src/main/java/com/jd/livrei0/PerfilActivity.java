package com.jd.livrei0;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PerfilActivity extends AppCompatActivity {

    private static final int TAKE_FOTO_CODE = 1234;
    private EditText mNomeUsuario;
    private Button mConfirmaPerfil, mCancelaPerfil;
    private ImageView mImagemPerfil;
    private String urlDownloadFotoPerfil;

    private FirebaseAuth mAuth;
    private DatabaseReference mUsuarioDB;

    private String userId, nome, imgPerfilUrl;

    private Uri resultUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        mNomeUsuario = (EditText) findViewById(R.id.nomeUsuario);

        mImagemPerfil = (ImageView) findViewById(R.id.imagemPerfil);

        mConfirmaPerfil = (Button) findViewById(R.id.btnConfirmaPerfil);
        mCancelaPerfil = (Button) findViewById(R.id.btnCancelaPerfil);

        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getCurrentUser().getUid();
        mUsuarioDB = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(userId);

        getInformacoesUsuario();

        mImagemPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               tirarFoto();
            }
        });

        mConfirmaPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                salvarInformacaoUsuario();

            }
        });


        mCancelaPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                return;
            }
        });

    }

    private void tirarFoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, TAKE_FOTO_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == TAKE_FOTO_CODE && resultCode == RESULT_OK){
            //recupera imagem atraves do bundle
            Bundle extras = data.getExtras();
            Bitmap imagem = (Bitmap) extras.get("data");
            mImagemPerfil.setImageBitmap(imagem);
            final Uri imageUri = data.getData();
            resultUri = imageUri;
            //mImagemPerfil.setImageURI(resultUri);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void getInformacoesUsuario() {
        mUsuarioDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists() && dataSnapshot.getChildrenCount()>0){
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    if(map.get("Nome") != null){
                        nome = map.get("Nome").toString(); // pega o child "Nome"
                        mNomeUsuario.setText(nome);//popula com o nome do banco
                    }
                    if(map.get("urlFotoPerfil") != null){
                        imgPerfilUrl = map.get("urlFotoPerfil").toString(); // pega o child "urlFotoPerfil"
                        Glide.with(getApplicationContext()).load(imgPerfilUrl).into(mImagemPerfil);//popula com o foto do banco pela url
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void salvarInformacaoUsuario() {
         nome = mNomeUsuario.getText().toString();

        Map informacoesUsuario = new HashMap();
        informacoesUsuario.put("Nome", nome);

        mUsuarioDB.updateChildren(informacoesUsuario);

        if (mImagemPerfil != null){
            StorageReference caminhoFotoPerfil = FirebaseStorage.getInstance().getReference().child("ImagensPerfil").child(userId);
            /*Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(), resultUri);
            } catch (IOException e) {
                e.printStackTrace();
            }*/

            mImagemPerfil.setDrawingCacheEnabled(true);
            mImagemPerfil.buildDrawingCache();
            Bitmap bitmap = mImagemPerfil.getDrawingCache();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 35, baos);
            mImagemPerfil.setDrawingCacheEnabled(false);
            byte[] data = baos.toByteArray();

            //upload Task
            caminhoFotoPerfil.putBytes(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!urlTask.isSuccessful());
                    Uri downloadUrl = urlTask.getResult();
                    urlDownloadFotoPerfil = downloadUrl.toString();

                    //update nas informações do usuario atual
                    Map userInfo = new HashMap();
                    userInfo.put("urlFotoPerfil", urlDownloadFotoPerfil);
                    mUsuarioDB.updateChildren(userInfo);

                    finish();
                    return;

                }
            });
        }else{
            finish();
        }
    }
}

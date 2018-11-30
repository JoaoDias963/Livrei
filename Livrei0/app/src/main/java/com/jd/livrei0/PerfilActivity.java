package com.jd.livrei0;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
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
import com.jd.livrei0.Utils.GradientDrawable;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class PerfilActivity extends AppCompatActivity {

    private static final int TAKE_FOTO_CODE = 1234;
    private static final int MY_REQUEST_CAMERA = 4321 ;
    private static final int MY_REQUEST_WRITESTORAGE = 1243;
    private EditText mNomeUsuario;
    private Button mConfirmaPerfil;
    private ImageView mImagemPerfil, mRodarImagem;
    private String urlDownloadFotoPerfil;

    private FirebaseAuth mAuth;
    private DatabaseReference mUsuarioDB;

    private String userId, nome, imgPerfilUrl;

    private Uri resultUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        if (ContextCompat.checkSelfPermission(PerfilActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},MY_REQUEST_CAMERA);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_REQUEST_WRITESTORAGE);
        }

        mNomeUsuario = (EditText) findViewById(R.id.nomeUsuario);
        mImagemPerfil = (ImageView) findViewById(R.id.imagemPerfil);
        mRodarImagem = (ImageView) findViewById(R.id.rotateImagePerfil);
        mConfirmaPerfil = (Button) findViewById(R.id.btnConfirmaPerfil);

        mRodarImagem.setVisibility(View.INVISIBLE);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mNomeUsuario.setBackground(new GradientDrawable().setBorderRoundedEDITTEXT());
            //mConfirmaPerfil.setBackground(getResources().getDrawable(R.drawable.btn_arredondado_confirma));
            mConfirmaPerfil.setBackground(new GradientDrawable().setBorderRoundedBUTTONConfirma());

        }else {
            mNomeUsuario.setBackgroundDrawable(new GradientDrawable().setBorderRoundedEDITTEXT());
            mConfirmaPerfil.setBackgroundResource(R.drawable.btn_arredondado_confirma);

        }

        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getCurrentUser().getUid();
        mUsuarioDB = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(userId);

        getInformacoesUsuario();

        mRodarImagem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rodarImagem();
            }
        });

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






    }

    private void rodarImagem() {
        BitmapDrawable drawable = (BitmapDrawable) mImagemPerfil.getDrawable();
        Bitmap imagem = drawable.getBitmap();

        Matrix matrix = new Matrix();
        matrix.postRotate(90);

        Bitmap rotacionada =  Bitmap.createBitmap(imagem,0,0, imagem.getWidth(),imagem.getHeight(),matrix,true);
        mImagemPerfil.setImageBitmap(rotacionada);
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


            //mImagemPerfil.setRotation(270);
            final Uri imageUri = data.getData();
            resultUri = imageUri;
            //mImagemPerfil.setImageURI(resultUri);

            mRodarImagem.setVisibility(View.VISIBLE);


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

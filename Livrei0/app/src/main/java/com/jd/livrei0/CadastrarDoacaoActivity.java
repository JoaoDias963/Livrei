package com.jd.livrei0;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.jd.livrei0.Utils.GradientDrawable;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class CadastrarDoacaoActivity extends AppCompatActivity {

    private static final int TAKE_FOTO_CODE = 1234;
    private static final int MY_REQUEST_CAMERA = 4321;
    private static final int MY_REQUEST_WRITESTORAGE = 1243;
    private EditText mTitulo, mAutor;
    private Button mTrocarLivro;
    private String urlLivroDoacao;
    private ImageView mFoto, mRotacionaImagem;
    private Uri resultUri;
    private String urlDownloadFotoDoacaoLivro;

    private FirebaseAuth mFirebaseAuth;
    private String userId;
    private DatabaseReference mUsuarioDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_doacao);


        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA},MY_REQUEST_CAMERA);
        }
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_REQUEST_WRITESTORAGE);
        }

        mFirebaseAuth = FirebaseAuth.getInstance();
        userId = mFirebaseAuth.getCurrentUser().getUid();
        mUsuarioDb = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(userId);


        mTitulo = (EditText) findViewById(R.id.intxtCadTitulo);
        mAutor = (EditText) findViewById(R.id.intxtCadAutor);
        mFoto = (ImageView) findViewById(R.id.imgDoaLivro);
        mRotacionaImagem = (ImageView) findViewById(R.id.rotateImageLivro);
        mTrocarLivro = (Button) findViewById(R.id.btnDoar);


        mRotacionaImagem.setVisibility(View.INVISIBLE);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mTitulo.setBackground(new GradientDrawable().setBorderRoundedEDITTEXT());
            mAutor.setBackground(new GradientDrawable().setBorderRoundedEDITTEXT());
            //mConfirmaPerfil.setBackground(getResources().getDrawable(R.drawable.btn_arredondado_confirma));
            mTrocarLivro.setBackground(new GradientDrawable().setBorderRoundedBUTTONConfirma());

        }else {
            mTitulo.setBackgroundDrawable(new GradientDrawable().setBorderRoundedEDITTEXT());
            mAutor.setBackgroundDrawable(new GradientDrawable().setBorderRoundedEDITTEXT());
            mTrocarLivro.setBackgroundResource(R.drawable.btn_arredondado_confirma);

        }

//teste tag foto android+21
        mFoto.setTag(R.drawable.bookicone);
        //////
        mFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tirarFoto();
            }
        });
        mRotacionaImagem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rodarImagem();
            }
        });

    }

    private void rodarImagem() {
        BitmapDrawable drawable = (BitmapDrawable) mFoto.getDrawable();
        Bitmap imagem = drawable.getBitmap();

        Matrix matrix = new Matrix();
        matrix.postRotate(90);

        Bitmap rotacionada =  Bitmap.createBitmap(imagem,0,0, imagem.getWidth(),imagem.getHeight(),matrix,true);
        mFoto.setImageBitmap(rotacionada);
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
            //teste mudanca foto
            mFoto.setTag(imagem.getGenerationId());
            //teste mudanca foto

            mFoto.setImageBitmap(imagem);
            final Uri imageUri = data.getData();
            resultUri = imageUri;
            //mImagemPerfil.setImageURI(resultUri);
            mRotacionaImagem.setVisibility(View.VISIBLE);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void cadastrarDoacao(View view) {
        if (mFoto != null) {

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                if ((Integer) mFoto.getTag() != R.drawable.bookicone){
                    cadastra();
                }else {
                    Toast.makeText(this, "Clique na imagem e tire uma foto do seu livro antes", Toast.LENGTH_LONG).show();
                }
            }else{
                if(!mFoto.getDrawable().getConstantState().equals(getResources().getDrawable(R.drawable.bookicone).getConstantState())){
                    cadastra();
                }else {
                    Toast.makeText(this, "Clique na imagem e tire uma foto do seu livro antes", Toast.LENGTH_LONG).show();
                }
            }
/*
            if ((Integer) mFoto.getTag() != R.drawable.bookicone){


                final String titulo = mTitulo.getEditableText().toString();
                final String autor = mAutor.getEditableText().toString();

           /* Map informacoesDoacao = new HashMap();
            informacoesDoacao.put("titulo", titulo);
            informacoesDoacao.put("autor", autor);


            mUsuarioDb.child("Doacao").updateChildren(informacoesDoacao); */
/*
                StorageReference caminhoFotoDoacaoLivro = FirebaseStorage.getInstance().getReference().child("ImagensLivrosDoacao").child(userId);

                mFoto.setDrawingCacheEnabled(true);
                mFoto.buildDrawingCache();
                Bitmap bitmap = mFoto.getDrawingCache();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 35, baos);
                mFoto.setDrawingCacheEnabled(false);
                byte[] data = baos.toByteArray();

                caminhoFotoDoacaoLivro.putBytes(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!urlTask.isSuccessful()) ;
                        Uri downloadUrl = urlTask.getResult();
                        urlDownloadFotoDoacaoLivro = downloadUrl.toString();

                        //update nas informações do usuario atual
                        Map livroInfo = new HashMap();
                        livroInfo.put("urlFotoDoacaoLivro", urlDownloadFotoDoacaoLivro);
                        livroInfo.put("titulo", titulo);
                        livroInfo.put("autor", autor);
                        //gera id do livro
                        //String idLivro = mUsuarioDb.child("Doacao").push().getKey();
                        //livroInfo.put("idLivro", idLivro);
                        mUsuarioDb.child("Doacao")/*.child(idLivro)*//*.updateChildren(livroInfo);

                        finish();
                        return;

                    }
                });
            } else {
                Toast.makeText(this, "Tire uma foto do seu livro antes", Toast.LENGTH_LONG).show();
                finish();
                return;
            }*/

        } else {
            finish();
        }//fim if foto not null


    }




    public void cadastra(){

        final String titulo = mTitulo.getEditableText().toString();
        final String autor = mAutor.getEditableText().toString();

        if (!autor.isEmpty() && !titulo.isEmpty()){
              /* Map informacoesDoacao = new HashMap();
            informacoesDoacao.put("titulo", titulo);
            informacoesDoacao.put("autor", autor);


            mUsuarioDb.child("Doacao").updateChildren(informacoesDoacao); */

            StorageReference caminhoFotoDoacaoLivro = FirebaseStorage.getInstance().getReference().child("ImagensLivrosDoacao").child(userId);

            mFoto.setDrawingCacheEnabled(true);
            mFoto.buildDrawingCache();
            Bitmap bitmap = mFoto.getDrawingCache();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 35, baos);
            mFoto.setDrawingCacheEnabled(false);
            byte[] data = baos.toByteArray();

            caminhoFotoDoacaoLivro.putBytes(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!urlTask.isSuccessful()) ;
                    Uri downloadUrl = urlTask.getResult();
                    urlDownloadFotoDoacaoLivro = downloadUrl.toString();

                    //update nas informações do usuario atual
                    Map livroInfo = new HashMap();
                    livroInfo.put("urlFotoDoacaoLivro", urlDownloadFotoDoacaoLivro);
                    livroInfo.put("titulo", titulo);
                    livroInfo.put("autor", autor);
                    //gera id do livro
                    //String idLivro = mUsuarioDb.child("Doacao").push().getKey();
                    //livroInfo.put("idLivro", idLivro);
                    mUsuarioDb.child("Doacao")/*.child(idLivro)*/.updateChildren(livroInfo);

                    finish();
                    return;

                }
            });

        }else{
            Toast.makeText(this,"Preencha os dados corretamente",Toast.LENGTH_LONG).show();
        }

    }
}


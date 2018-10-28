package com.jd.livrei0;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class CadastrarDoacaoActivity extends AppCompatActivity {

    private static final int TAKE_FOTO_CODE = 1234;
    private TextInputEditText mTitulo, mAutor;
    private String urlLivroDoacao;
    private ImageView mFoto;
    private Uri resultUri;
    private String urlDownloadFotoDoacaoLivro;

    private FirebaseAuth mFirebaseAuth;
    private String userId;
    private DatabaseReference mUsuarioDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_doacao);

        mFirebaseAuth = FirebaseAuth.getInstance();
        userId = mFirebaseAuth.getCurrentUser().getUid();
        mUsuarioDb = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(userId);


        mTitulo = (TextInputEditText) findViewById(R.id.intxtCadTitulo);
        mAutor = (TextInputEditText) findViewById(R.id.intxtCadAutor);
        mFoto = (ImageView) findViewById(R.id.imgDoaLivro);

        mFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tirarFoto();
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
            mFoto.setImageBitmap(imagem);
            final Uri imageUri = data.getData();
            resultUri = imageUri;
            //mImagemPerfil.setImageURI(resultUri);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void cadastrarDoacao(View view) {
        if(mFoto != null){
            final String titulo = mTitulo.getEditableText().toString();
            final String autor = mAutor.getEditableText().toString();

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
                    while (!urlTask.isSuccessful());
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
            finish();
        }



        }//fim if foto not null


    public void cancelarDoacao(View view) {
        finish();
        return;
    }
}


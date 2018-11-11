package com.jd.livrei0.Fragment;


import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TabHost;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.SuccessContinuation;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.jd.livrei0.R;
import com.jd.livrei0.TabAdapter.TabAdapter;
import com.jd.livrei0.Utils.GradientDrawable;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class CadLivroTrocaFragment extends Fragment {

    private static final int TAKE_FOTO_CODE = 1234;
    private EditText mTitulo, mAutor;
    private Button mTrocarLivro, mCancelaTroca;
    private String urlLivroDoacao;
    private ImageView mFoto;
    private Uri resultUri;
    private String urlDownloadFotoDoacaoLivro;

    private FirebaseAuth mFirebaseAuth;
    private String userId;
    private DatabaseReference mUsuarioDb;

    public CadLivroTrocaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_cad_livro_troca, container, false);

        mFirebaseAuth = FirebaseAuth.getInstance();
        userId = mFirebaseAuth.getCurrentUser().getUid();
        mUsuarioDb = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(userId);


        mTitulo = (EditText) view.findViewById(R.id.intxtCadTitulo);
        mAutor = (EditText) view.findViewById(R.id.intxtCadAutor);
        mFoto = (ImageView) view.findViewById(R.id.imgDoaLivro);
        mTrocarLivro = (Button) view.findViewById(R.id.btnDoar);
        mCancelaTroca = (Button) view.findViewById(R.id.btnCancelaDoacao);



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mTitulo.setBackground(new GradientDrawable().setBorderRoundedEDITTEXT());
            mAutor.setBackground(new GradientDrawable().setBorderRoundedEDITTEXT());
            //mConfirmaPerfil.setBackground(getResources().getDrawable(R.drawable.btn_arredondado_confirma));
            mTrocarLivro.setBackground(new GradientDrawable().setBorderRoundedBUTTONConfirma());
            mCancelaTroca.setBackground(new GradientDrawable().setBorderRoundedBUTTONCancela());
        }else {
            mTitulo.setBackgroundDrawable(new GradientDrawable().setBorderRoundedEDITTEXT());
            mAutor.setBackgroundDrawable(new GradientDrawable().setBorderRoundedEDITTEXT());
            mTrocarLivro.setBackgroundResource(R.drawable.btn_arredondado_confirma);
            mCancelaTroca.setBackgroundResource(R.drawable.btn_arredondado_cancela);
        }

        mFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tirarFoto();
            }
        });

        mTrocarLivro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cadastrarDoacao(view);
            }
        });

        return view;
    }

    private void tirarFoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, TAKE_FOTO_CODE);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
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
            final String titulo = mTitulo.getText().toString();
            final String autor = mAutor.getText().toString();

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



                    return;

                }
            });

        }



    }//fim if foto not null

}

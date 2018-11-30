package com.jd.livrei0.TabAdapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;

import com.jd.livrei0.Fragment.CadLivroTrocaFragment;
import com.jd.livrei0.Fragment.LivrosFragment;
import com.jd.livrei0.Fragment.PerfilFragment;
import com.jd.livrei0.Fragment.TrocasDisponiveisFragment;

public class TabAdapter extends FragmentStatePagerAdapter {

    private String[] tituloAbas = {"LIVROS DISPONÍVEIS","TROCAS"};

    public TabAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        Fragment fragment = null;

        switch (position){
            case 0:
                fragment = new LivrosFragment();


                break;
            case 1:

                fragment = new TrocasDisponiveisFragment();




                break;


        }

        return fragment;
    }

    @Override
    public int getCount() {
        return tituloAbas.length;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tituloAbas[position];
    }




}

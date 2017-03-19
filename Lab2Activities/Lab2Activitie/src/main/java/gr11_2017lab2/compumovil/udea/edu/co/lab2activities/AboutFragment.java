package gr11_2017lab2.compumovil.udea.edu.co.lab2activities;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class AboutFragment extends Fragment {
    public AboutFragment(){

    }
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
        return  inflater.inflate(R.layout.fragment_about,container,false);
    }
}

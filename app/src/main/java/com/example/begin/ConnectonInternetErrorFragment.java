package com.example.begin;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.begin.productview.ProductGridFragment;


public class ConnectonInternetErrorFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_connecton_internet_error, container, false);

        Button buttonRetry = view.findViewById(R.id.buttonRetry);
        buttonRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ConnectionInternetError) getActivity()).refreshCurrentErrorPage();
            }
        });

        return view;
    }

}

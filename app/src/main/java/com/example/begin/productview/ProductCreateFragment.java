package com.example.begin.productview;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.begin.R;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProductCreateFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductCreateFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_product_create, container, false);

        Button gridBtn = view.findViewById(R.id.add_button);

        // Set an error if the password is less than 8 characters.
        gridBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Hello", Toast.LENGTH_SHORT).show();// ((NavigationHost) getActivity()).navigateTo(new ProductCreateFragment(), false);
            }
        });
        return view;
    }
}

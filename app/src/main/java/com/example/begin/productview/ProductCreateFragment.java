package com.example.begin.productview;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.begin.R;
import com.example.begin.productview.netwok.ProductCreateDTO;
import com.example.begin.productview.netwok.ProductCreateResultDTO;
import com.example.begin.productview.netwok.ProductDTOService;
import com.google.android.material.textfield.TextInputEditText;
import com.example.begin.NavigationHost;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


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

        final TextInputEditText titleEditText = view.findViewById(R.id.title_edit_text);
        final TextInputEditText priceEditText = view.findViewById(R.id.price_edit_text);
        // Set an error if the password is less than 8 characters.
        gridBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
           String title = titleEditText.getText().toString();
           String price = priceEditText.getText().toString();
           Toast.makeText(getContext(), title + price, Toast.LENGTH_SHORT).show();// ((NavigationHost) getActivity()).navigateTo(new ProductCreateFragment(), false);
                ProductDTOService.getInstance()
                        .getJSONApi()
                        .createProduct(new ProductCreateDTO(title, price))
                        .enqueue(new Callback<ProductCreateResultDTO>() {
                            @Override
                            public void onResponse(Call<ProductCreateResultDTO> call, Response<ProductCreateResultDTO> response) {
                            if(response.isSuccessful()){
                                ProductCreateResultDTO resultDTO = response.body();
                                ((NavigationHost) getActivity()).navigateTo(new ProductGridFragment(), true);
                            }
                            else{
                                Log.e("error create", "----------Error----------");
                            }
                            }

                            @Override
                            public void onFailure(Call<ProductCreateResultDTO> call, Throwable t) {

                            }
                        });

            }
        });
        return view;
    }
}

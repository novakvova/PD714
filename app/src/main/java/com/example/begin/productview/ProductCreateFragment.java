package com.example.begin.productview;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.begin.R;
import com.example.begin.account.LoginDTOBadRequest;
import com.example.begin.productview.netwok.ProductCreateDTO;
import com.example.begin.productview.netwok.ProductCreateErrorDTO;
import com.example.begin.productview.netwok.ProductCreateResultDTO;
import com.example.begin.productview.netwok.ProductDTOService;
import com.example.begin.utils.CommonUtils;
import com.example.begin.utils.FileUtil;
import com.google.android.material.textfield.TextInputEditText;
import com.example.begin.NavigationHost;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ProductCreateFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    public static final int PICKFILE_RESULT_CODE = 1;
    ImageView chooseImage;
    String chooseImageBase64;
    TextInputLayout title_text_input;
    TextInputLayout price_text_input;
    TextView error_img;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_product_create, container, false);

        Button btnAddProduct = view.findViewById(R.id.add_button);
        Button btnSelectImage = view.findViewById(R.id.btnSelectImage);
        Button btnCancel = view.findViewById(R.id.cancel_button);

        chooseImage = (ImageView) view.findViewById(R.id.chooseImage);

        btnSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
                chooseFile.setType("image/*");
                chooseFile = Intent.createChooser(chooseFile, "Choose a file");
                startActivityForResult(chooseFile, PICKFILE_RESULT_CODE);

            }
        });

        final TextInputEditText titleEditText = view.findViewById(R.id.title_edit_text);
        final TextInputEditText priceEditText = view.findViewById(R.id.price_edit_text);
        final TextView errorMessage = view.findViewById(R.id.error_message);
        title_text_input = view.findViewById(R.id.title_text_input);
        price_text_input = view.findViewById(R.id.price_text_input);
        error_img = view.findViewById(R.id.error_img);

        // Set an error if the password is less than 8 characters.
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Toast.makeText(getActivity(),"Successful", Toast.LENGTH_LONG).show();
                ((NavigationHost) getActivity()).navigateTo(new ProductGridFragment(), true);
            }
        });

        btnAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = titleEditText.getText().toString();
                String price = priceEditText.getText().toString();
                //Toast.makeText(getContext(), title + price, Toast.LENGTH_SHORT).show();// ((NavigationHost) getActivity()).navigateTo(new ProductCreateFragment(), false);
                errorMessage.setText("");
                CommonUtils.showLoading(getActivity());
                ProductDTOService.getInstance()
                        .getJSONApi()
                        .createProduct(new ProductCreateDTO(title, price, chooseImageBase64))
                        .enqueue(new Callback<ProductCreateResultDTO>() {
                            @Override
                            public void onResponse(Call<ProductCreateResultDTO> call, Response<ProductCreateResultDTO> response) {
                                CommonUtils.hideLoading();
                                if (response.isSuccessful()) {
                                    ProductCreateResultDTO resultDTO = response.body();
                                    ((NavigationHost) getActivity()).navigateTo(new ProductGridFragment(), true);
                                } else {

                                    try {
                                        String json = response.errorBody().string();
                                        Gson gson = new Gson();
                                        ProductCreateErrorDTO resultBad = gson.fromJson(json, ProductCreateErrorDTO.class);
                                        title_text_input.setError(resultBad.getTitle());
                                        price_text_input.setError(resultBad.getPrice());
                                        //Log.d(TAG,"++++++++++++++++++++++++++++++++"+response.errorBody().string());
                                        errorMessage.setText(resultBad.getInvalid());

                                        error_img.setText(resultBad.getImageBase64());
                                    } catch (Exception e) {
                                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<ProductCreateResultDTO> call, Throwable t) {
                                CommonUtils.hideLoading();
                                Log.e("ERROR","*************ERORR request***********");
                            }
                        });

            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PICKFILE_RESULT_CODE:
                if (resultCode == -1) {
                    Uri fileUri = data.getData();
                    try {
                        File imgFile = FileUtil.from(this.getActivity(), fileUri);

                        byte[] buffer = new byte[(int) imgFile.length() + 100];
                        int length = new FileInputStream(imgFile).read(buffer);
                        chooseImageBase64 = Base64.encodeToString(buffer, 0, length, Base64.NO_WRAP);
                        Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                        chooseImage.setImageBitmap(myBitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                break;
        }
    }


}

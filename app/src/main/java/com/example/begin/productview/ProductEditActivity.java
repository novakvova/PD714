package com.example.begin.productview;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.toolbox.NetworkImageView;
import com.example.begin.BaseActivity;
import com.example.begin.R;
import com.example.begin.network.ImageRequester;
import com.example.begin.productview.netwok.ProductDTO;
import com.example.begin.productview.netwok.ProductDTOService;
import com.example.begin.utils.CommonUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductEditActivity extends BaseActivity {

    public static final int PICKFILE_RESULT_CODE = 1;

    private EditText editTextTitle;
    private EditText editTextPrice;
    private NetworkImageView editImage;
    private Button buttonEdit;
    private Button buttonCancel;
    private int id;
    //image
    private ImageRequester imageRequester;
    private Button btnSelectImage;
    private String chooseImageBase64;
    private ProductDTO productDTO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_edit);
        imageRequester = ImageRequester.getInstance();

        setupViews();
        initProduct();
        setButtonCancelListener();
        setButtonEditListener();
        setbtnSelectImageListener();
    }

    private void setupViews() {
        editTextTitle = findViewById(R.id.editTextTitle);
        editTextPrice = findViewById(R.id.editTextPrice);
        editImage = findViewById(R.id.chooseImage);
        buttonEdit = findViewById(R.id.buttonEdit);
        buttonCancel = findViewById(R.id.buttonCancel);
        btnSelectImage = findViewById(R.id.btnSelectImage);
    }

    private void initProduct() {
        Intent intent = getIntent();
        if (intent != null) {
            id = intent.getIntExtra(Constants.PRODUCT_INTENT_ID, 0);
        }
        //      Toast.makeText(getApplicationContext(),String.valueOf(id),Toast.LENGTH_LONG).show();
        ProductDTOService.getInstance()
                .getJSONApi()
                .getEditProduct(id)
                .enqueue(new Callback<ProductDTO>() {

                    @Override
                    public void onResponse(@NonNull Call<ProductDTO> call, @NonNull Response<ProductDTO> response) {
                        CommonUtils.hideLoading();
                        if (response.isSuccessful()) {
                            productDTO = response.body();
                            editTextTitle.setText(productDTO.getTitle());
                            editTextPrice.setText(productDTO.getPrice());
                            editImage = findViewById(R.id.chooseImage);
                            imageRequester.setImageFromUrl(editImage, productDTO.getUrl());

                        } else {

                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ProductDTO> call, @NonNull Throwable t) {
                        CommonUtils.hideLoading();
                        Log.e("ERROR", "*************ERORR request***********");
                        t.printStackTrace();

                    }
                });
    }

    private void setButtonCancelListener() {
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
    }

    private void setButtonEditListener() {
        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = editTextTitle.getText().toString().trim();
                String price = editTextPrice.getText().toString().trim();
                String base64 = chooseImageBase64;
                productDTO.setTitle(title);
                productDTO.setPrice(price);
                productDTO.setImageBase64(base64);


                ProductDTOService.getInstance()
                        .getJSONApi()
                        .editProduct(productDTO)
                        .enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {

                                CommonUtils.hideLoading();
                                if (response.isSuccessful()) {
                                    setResult(RESULT_OK);
                                    finish();
                                }
                            }

                            @Override
                            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                                //CommonUtils.hideLoading();
                                Log.e("ERROR", "*************ERORR request***********");
                                t.printStackTrace();
                                CommonUtils.hideLoading();
                            }
                        });
            }
        });
    }

    private void setbtnSelectImageListener() {
        btnSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
                chooseFile.setType("image/*");
                chooseFile = Intent.createChooser(chooseFile, "Оберіть фото");
                startActivityForResult(chooseFile, PICKFILE_RESULT_CODE);
            }
        });
    }
}

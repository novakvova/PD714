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
import android.widget.Toast;

import com.example.begin.R;
import com.example.begin.productview.netwok.ProductCreateDTO;
import com.example.begin.productview.netwok.ProductCreateResultDTO;
import com.example.begin.productview.netwok.ProductDTOService;
import com.example.begin.utils.FileUtil;
import com.google.android.material.textfield.TextInputEditText;
import com.example.begin.NavigationHost;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ProductCreateFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    public static final int PICKFILE_RESULT_CODE = 1;
    ImageView myImage;
    private Uri fileUri;
    private String filePath;

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
        Button btnSelectImage = view.findViewById(R.id.btnSelectImage);

        myImage = (ImageView) view.findViewById(R.id.imgSource);

        btnSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
                chooseFile.setType( "image/*");
                chooseFile = Intent.createChooser(chooseFile, "Choose a file");
                startActivityForResult(chooseFile, PICKFILE_RESULT_CODE);
                //Toast.makeText(getContext(), "Hello", Toast.LENGTH_LONG).show();
            }
        });

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
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PICKFILE_RESULT_CODE:
                if (resultCode == -1) {
                    fileUri = data.getData();
                    //filePath = fileUri.getPath();
                    //String myFilePath=getFilePathFromContentPath(fileUri);
                    //Toast.makeText(getContext(), myFilePath, Toast.LENGTH_SHORT).show();
                    try {
                        File imgFile= FileUtil.from(this.getActivity(),fileUri);

                        imgFile.getPath();
                        byte[] buffer = new byte[(int) imgFile.length() + 100];
                        int length = new FileInputStream(imgFile).read(buffer);
                        String base64 = Base64.encodeToString(buffer, 0, length,
                                Base64.DEFAULT);

                        Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                        myImage.setImageBitmap(myBitmap);

int p=10;
                    }
                    catch(IOException e)
                    {
                        e.printStackTrace();
                    }
                }

                break;
        }
    }

    String getFilePathFromContentPath(Uri uri) {
        String filePath = null;
        if ("content".equals(uri.getScheme())) {
            Cursor cursor = this.getActivity().getContentResolver().query(uri,
                    new String[]{android.provider.MediaStore.Images.ImageColumns.DATA},
                    null,
                    null,
                    null);
            cursor.moveToFirst();
            filePath = cursor.getString(0);
            cursor.close();
        } else {
            filePath = uri.getPath();
        }
        return filePath;
    }

}

package com.example.begin.productview;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.begin.NavigationHost;
import com.example.begin.R;
import com.example.begin.network.ProductEntry;
import com.example.begin.productview.click_listeners.OnDeleteListener;
import com.example.begin.productview.click_listeners.OnEditListener;
import com.example.begin.productview.netwok.ProductDTO;
import com.example.begin.productview.netwok.ProductDTOService;
import com.example.begin.utils.CommonUtils;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductGridFragment extends Fragment implements OnEditListener, OnDeleteListener {

    private static final String TAG = ProductGridFragment.class.getSimpleName();

    private RecyclerView recyclerView;

    private ProductCardRecyclerViewAdapter productEntryAdapter;
    private List<ProductEntry> listProductEntry;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("WrongConstant")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_product_grid, container, false);
        Button btnCreateNew = view.findViewById(R.id.add_button);

        // Set an error if the password is less than 8 characters.
        btnCreateNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((NavigationHost) getActivity()).navigateTo(new ProductCreateFragment(), false);
            }
        });

        // Set up the RecyclerView
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false));

        listProductEntry = new ArrayList<ProductEntry>();
        productEntryAdapter = new ProductCardRecyclerViewAdapter(listProductEntry, this, this);

//        List<ProductEntry> list = ProductEntry.initProductEntryList(getResources());
//        ProductCardRecyclerViewAdapter adapter = new ProductCardRecyclerViewAdapter(list);
//
        recyclerView.setAdapter(productEntryAdapter);

        int largePadding = getResources().getDimensionPixelSize(R.dimen.shr_product_grid_spacing);
        int smallPadding = getResources().getDimensionPixelSize(R.dimen.shr_product_grid_spacing_small);
        recyclerView.addItemDecoration(new ProductGridItemDecoration(largePadding, smallPadding));

        CommonUtils.showLoading(getActivity());
        ProductDTOService.getInstance()
                .getJSONApi()
                .getAllProducts()
                .enqueue(new Callback<List<ProductDTO>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<ProductDTO>> call, @NonNull Response<List<ProductDTO>> response) {
                        CommonUtils.hideLoading();
                        if(response.isSuccessful()) {
                            List<ProductDTO> list = response.body();
                            //int size = list.size();
                            // String res= list.get(0).toString();
                            //Log.d(TAG, "--------result server-------"+res);

                            //List<ProductEntry> newlist = new ArrayList<ProductEntry>();//ProductEntry.initProductEntryList(getResources());
                            for (ProductDTO item : list) {
                                Log.d("Show", item.toString());
                                ProductEntry pe = new ProductEntry(item.getId(), item.getTitle(), item.getUrl(), item.getUrl(), item.getPrice(), "sdfasd");
                                listProductEntry.add(pe);
                            }
                            //ProductCardRecyclerViewAdapter newAdapter = new ProductCardRecyclerViewAdapter(newlist);
                            //recyclerView.swapAdapter(newAdapter, false);
                            productEntryAdapter.notifyDataSetChanged();
                        }

                    }

                    @Override
                    public void onFailure(@NonNull Call<List<ProductDTO>> call, @NonNull Throwable t) {
                        CommonUtils.hideLoading();
                        Log.e("ERROR","*************ERORR request***********");
//                        if(t instanceof NoConnectivityException) {
//                            ((ConnectionInternetError) getActivity()).navigateErrorPage(new ProductGridFragment(), true); // Navigate to the next Fragment
//                            //Toast.makeText(getActivity().getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
//                        }
                        t.printStackTrace();
                    }
                });

        return view;
    }


    private void deleteConfirm(final ProductEntry productEntry) {
        CommonUtils.showLoading(getContext());
        ProductDTOService.getInstance()
                .getJSONApi()
                .DeleteRequest(productEntry.id)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                        CommonUtils.hideLoading();
                        if (response.isSuccessful()) {
                            listProductEntry.remove(productEntry);
                            productEntryAdapter.notifyDataSetChanged();
                        } else {
                            //  Log.e(TAG, "_______________________" + response.errorBody().charStream());

                            try {
//                                                String json = response.errorBody().string();
//                                                Gson gson  = new Gson();
//                                                ProductCreateInvalidDTO resultBad = gson.fromJson(json, ProductCreateInvalidDTO.class);
                                //Log.d(TAG,"++++++++++++++++++++++++++++++++"+response.errorBody().string());
                                //errormessage.setText(resultBad.getInvalid());
                            } catch (Exception e) {
                                //Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                        CommonUtils.hideLoading();
                        Log.e("ERROR", "*************ERORR request***********");
                        t.printStackTrace();

                    }
                });
    }


    @Override
    public void deleteItem(final ProductEntry productEntry) {
        new MaterialAlertDialogBuilder(getContext())
                .setTitle("Видалення")
                .setMessage("Ви дійсно бажаєте видалити \"" + productEntry.title + "\"?")
                .setNegativeButton("Скасувати", null)
                .setPositiveButton("Видалити", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteConfirm(productEntry);
                    }
                })
                .show();

        //Toast.makeText(getActivity(), "Delete Item", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void editItem(ProductEntry productEntry, int index) {
        Toast.makeText(getActivity(), "Edit Item", Toast.LENGTH_LONG).show();
    }
}

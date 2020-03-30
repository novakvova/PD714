package com.example.begin;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.begin.account.AccountService;
import com.example.begin.account.LoginDTO;
import com.example.begin.account.LoginDTOBadRequest;
import com.example.begin.account.TokenDTO;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginFragment extends Fragment {

    private static final String TAG = LoginFragment.class.getSimpleName();
    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        final TextInputLayout passwordTextInput = view.findViewById(R.id.password_text_input);
        final TextInputEditText passwordEditText = view.findViewById(R.id.password_edit_text);
        final TextInputEditText phoneEditText = view.findViewById(R.id.telephone_edit_text);
        final TextView errorMessage = view.findViewById(R.id.error_message);
        MaterialButton nextButton = view.findViewById(R.id.next_button);

        // Set an error if the password is less than 8 characters.
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isPasswordValid(passwordEditText.getText())) {
                    passwordTextInput.setError("Пароль має бути мін 8 символів");
                } else {
                    passwordTextInput.setError(null); // Clear the error
                    String login = phoneEditText.getText().toString();
                    String password = passwordEditText.getText().toString();

                    LoginDTO loginDTO=new LoginDTO(login, password);
                    errorMessage.setText("");
                    AccountService.getInstance()
                            .getJSONApi()
                            .loginRequest(loginDTO)
                            .enqueue(new Callback<TokenDTO>() {
                                @Override
                                public void onResponse(@NonNull Call<TokenDTO> call, @NonNull Response<TokenDTO> response) {
                                    if(response.isSuccessful()) {
                                        TokenDTO tokenDTO = response.body();
                                        //((JwtServiceHolder) getActivity()).SaveJWTToken(tokenDTO.getToken()); // Navigate to the register Fragment
                                        //((NavigationHost) getActivity()).navigateTo(new ProductGridFragment(), false); // Navigate to the products Fragment
                                        Log.e(TAG,"*************GOOD Request***********"+ tokenDTO.getToken());
                                    }
                                    else {
                                        Log.e(TAG,"_______________________"+response.errorBody().charStream());

                                        try {
                                            String json = response.errorBody().string();
                                            Gson gson = new Gson();
                                            LoginDTOBadRequest resultBad = gson.fromJson(json, LoginDTOBadRequest.class);
                                            //Log.d(TAG,"++++++++++++++++++++++++++++++++"+response.errorBody().string());
                                            errorMessage.setText(resultBad.getInvalid());
                                        } catch (Exception e) {
                                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    }

                                    //Log.d(TAG,tokenDTO.toString());
                                    //CommonUtils.hideLoading();
                                }

                                @Override
                                public void onFailure(@NonNull Call<TokenDTO> call, @NonNull Throwable t) {
                                    //CommonUtils.hideLoading();
                                    Log.e("ERROR","*************ERORR request***********");
                                    t.printStackTrace();
                                }
                            });



                    //((NavigationHost) getActivity()).navigateTo(new ProductGridFragment(), false); // Navigate to the next Fragment
                }
            }
        });

        // Clear the error once more than 8 characters are typed.
        passwordEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (isPasswordValid(passwordEditText.getText())) {
                    passwordTextInput.setError(null); //Clear the error
                    //return true;
                }
                return false;
            }
        });

        return view;
    }

    /*
        In reality, this will have more complex logic including, but not limited to, actual
        authentication of the username and password.
     */
    private boolean isPasswordValid(@Nullable Editable text) {
        return text != null && text.length() >= 8;
    }
}

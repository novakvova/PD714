package com.example.begin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.begin.userview.UserGridFragment;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class MainActivity extends AppCompatActivity implements NavigationHost  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJEUExSSTVUTEVNMjFTQzNER0xHUjBJOFpYIiwiaXNzIjoiaHR0cHM6Ly9hcGkuc3Rvcm1wYXRoLmNvbS92MS9hcHBsaWNhdGlvbnMvNWpvQVVKdFZONHNkT3dUVVJEc0VDNSIsImlhdCI6MTQwNjY1OTkxMCwiZXhwIjoxNDA2NjYzNTEwLCJzY29wZSI6IiJ9.ypDMDMMCRCtDhWPMMc9l_Q-O-rj5LATalHYa3droYkY";
//
//        SaveJWTToken(token);
        String token = getToken();
        //if(token!=null)
            Log.i("Login",token);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, new LoginFragment())
                    .commit();
        }
    }

    private void SaveJWTToken(String token) {
        SharedPreferences prefs;
        SharedPreferences.Editor edit;
        prefs=this.getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        edit=prefs.edit();
        try {

            edit.putString("token",token);
            Log.i("Login",token);
            edit.commit();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private String getToken() {
        SharedPreferences prefs=this.getSharedPreferences("myPrefs",Context.MODE_PRIVATE);
        String token = prefs.getString("token","");
        return token;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent;
        switch (item.getItemId())
        {
            case R.id.home:
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                return true;
            case R.id.register:
                //intent = new Intent(this, RegisterActivity.class);
                //startActivity(intent);
                this.navigateTo(new RegisterFragment(), false);
                return true;

            case R.id.login:
                this.navigateTo(new LoginFragment(), false); // Navigate to the next Fragment
                return true;
            case R.id.users:
                this.navigateTo(new UserGridFragment(), false); // Navigate to the next Fragment
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public void navigateTo(Fragment fragment, boolean addToBackstack) {
        FragmentTransaction transaction =
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, fragment);

        if (addToBackstack) {
            transaction.addToBackStack(null);
        }

        transaction.commit();
    }


}

package com.example.begin;

import androidx.annotation.NonNull;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.begin.productview.ProductGridFragment;
import com.example.begin.userview.UserGridFragment;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            String token = getToken();
            if (token!=null||token.equals("")){
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.container, new ProductGridFragment())
                        .commit();
            }
            else {
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.container, new LoginFragment())
                        .commit();
            }
        }
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
}

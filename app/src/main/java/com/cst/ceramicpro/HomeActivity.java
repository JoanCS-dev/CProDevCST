package com.cst.ceramicpro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    SharedPreferences cookies;
    DrawerLayout drawerLayout;
    String User, Imagen;
    int Id_User;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Init();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        View hView = navigationView.getHeaderView(0);
        TextView username = (TextView) hView.findViewById(R.id.user_name);
        //CircularImageView img = (CircularImageView) hView.findViewById(R.id.user_image);
        username.setText(User);
        /*if(Objects.equals(Imagen, "")){
            Picasso.get().load(R.drawable.ceramic_pro).error(R.drawable.ceramic_pro).into(img);
        }else{
            Picasso.get().load(Imagen).error(R.drawable.ceramic_pro).into(img);
        }*/

        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
                break;

            case R.id.nav_notification:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new NotificationFragment()).commit();
                break;

            case R.id.nav_reserv:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ReservationFragment()).commit();
                break;

            case R.id.nav_close_sesion:
                startActivity(new Intent(HomeActivity.this, StartActivity.class));
                finish();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        } else{
            super.onBackPressed();
        }
    }

    private void Init(){
        ValidateSesion();
    }

    private void ValidateSesion(){
        cookies = getSharedPreferences("Cookies", MODE_PRIVATE);
        Id_User = cookies.getInt("Id_User", 0);
        //User = cookies.getString("User", null);
        User = "Usuario";
        Imagen = cookies.getString("Image", null);

        /*if(Id_User == 0 && User == null){
            startActivity(new Intent(HomeActivity.this, LoginActivity.class));
            finish();
        }*/
    }
}
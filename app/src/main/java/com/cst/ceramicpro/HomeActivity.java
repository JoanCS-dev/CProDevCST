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


public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private SharedPreferences cookies;
    private DrawerLayout drawerLayout;
    private String strToken, fullName, strCode;
    //TextView  username;

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
        //username = (TextView) hView.findViewById(R.id.user_name);


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

            case R.id.nav_planes:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new PaqueteFragment()).commit();
                break;

            case R.id.nav_reserv:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ReservationFragment()).commit();
                break;

            case R.id.nav_membership:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MembershipFragment()).commit();
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
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void Init(){
        ValidateSesion();
    }

    private void ValidateSesion(){
        cookies = getSharedPreferences("SHA_CST_DB", MODE_PRIVATE);
        strToken = cookies.getString("strToken", "");
        fullName = cookies.getString("fullName", "");

        /*if(strToken == "" && fullName == ""){
            startActivity(new Intent(HomeActivity.this, LoginActivity.class));
            finish();
        }*/
    }

}
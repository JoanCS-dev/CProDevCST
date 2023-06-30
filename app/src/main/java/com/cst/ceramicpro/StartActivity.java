package com.cst.ceramicpro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialog;

public class StartActivity extends AppCompatActivity {

    Button LoginRedirect, RegisterRedirect;
    TextView Invitado;
    BottomSheetDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        LoginRedirect = findViewById(R.id.Btn_Act_Login);
        RegisterRedirect = findViewById(R.id.Btn_Act_Register);
        Invitado = findViewById(R.id.Btn_Invitado);
        dialog = new BottomSheetDialog(this);

        createDialog();

        LoginRedirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(StartActivity.this, LoginActivity.class));

            }
        });

        Invitado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
            }
        });

        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        RegisterRedirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(StartActivity.this, RegisterActivity.class));
            }
        });
    }

    private void createDialog() {
        View view = getLayoutInflater().inflate(R.layout.bottom_dialog,null,false);

        Button btn_si = view.findViewById(R.id.Btn_Si);
        Button btn_no = view.findViewById(R.id.Btn_No);

        btn_si.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                startActivity(new Intent(StartActivity.this, HomeActivity.class));
            }
        });

        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.setContentView(view);
    }
}
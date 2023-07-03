package com.cst.ceramicpro;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialog;

public class StartActivity extends AppCompatActivity {

    private Button LoginRedirect, RegisterRedirect;
    private TextView Invitado, btn_copyright;
    private BottomSheetDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        LoginRedirect = findViewById(R.id.Btn_Act_Login);
        RegisterRedirect = findViewById(R.id.Btn_Act_Register);
        Invitado = findViewById(R.id.Btn_Invitado);
        dialog = new BottomSheetDialog(this, R.style.BottomSheetDialogTheme);
        btn_copyright = findViewById(R.id.btn_copyright);

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

        RegisterRedirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(StartActivity.this, RegisterActivity.class));
            }
        });

        btn_copyright.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowModalEditText();
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
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void ShowModalEditText(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Registar url");
        //alert.setMessage("Escribe la url del servidor");

        final EditText input = new EditText(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        alert.setView(input);

        alert.setPositiveButton("Guardar",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String url = input.getText().toString();
                        SharedPreferences sharedPreferences = getSharedPreferences("SHA_CST_DB", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("url", url);
                        editor.apply();
                    }
                });

        alert.setNegativeButton("Cancelar",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alert.show();
    }
}
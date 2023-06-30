package com.cst.ceramicpro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.cst.ceramicpro.models.AuthRequestVM;
import com.cst.ceramicpro.models.AuthResponseVM;
import com.cst.ceramicpro.models.ResponseVM;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {
    private MediaType mediaType = MediaType.parse("application/json");
    private OkHttpClient client;
    private Gson gson;
    private AlertDialog loading;
    private ImageButton Back;
    private Button Ingresar;
    private TextInputEditText txt_Email, txt_Pass;
    private String URL = "http://192.168.100.8:8082/Api/Account/Auth";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        client = new OkHttpClient();
        gson = new Gson();

        Back = findViewById(R.id.btn_back_login);
        Ingresar = findViewById(R.id.Btn_Login);
        txt_Email = findViewById(R.id.txt_Email);
        txt_Pass = findViewById(R.id.txt_Pass);

        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, StartActivity.class));
                finish();
            }
        });

        Ingresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String _email_ = txt_Email.getText().toString();
                String _pass_ = txt_Pass.getText().toString();
                if(_email_.equals("") || _pass_.equals("")){
                    if(_email_.equals("")){
                        Message("Información", "Por favor escribe el correo electrónico");
                    }else if(_pass_.equals("")){
                        Message("Información", "Por favor escribe la contraseña");
                    }
                }else{
                    Show();
                    LogIn();
                }
            }
        });
    }

    public void LogIn(){
        AuthRequestVM req = new AuthRequestVM();
        req.acUser = txt_Email.getText().toString();
        req.acPassword = txt_Pass.getText().toString();
        RequestBody body = RequestBody.create(gson.toJson(req), mediaType);
        Request request = new Request.Builder()
                .url(URL)
                .post(body)
                .addHeader("Content-Type", "application/json")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loading.hide();
                        Message("Respuesta fallida!", "Ocurrió un error en el servidor. Verifica tu conexión a internet o por favor contactarse con Sistemas.");
                    }
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loading.hide();
                        try{
                            String string_json = response.body().string();
                            Type fooType = new TypeToken<ResponseVM<AuthResponseVM>>() {}.getType();
                            ResponseVM<AuthResponseVM> res = gson.fromJson(string_json, fooType);
                            if(res.ok){
                                SharedPreferences sharedPreferences = getSharedPreferences("SHA_CST_DB", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("strToken", res.data.strToken);
                                editor.putString("fullName", res.data.fullName);
                                editor.apply();
                                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                                finish();
                            }else{
                                Message("Información", res.message);
                            }
                        }catch (Exception ex){
                            Message("Error", ex.getMessage());
                        }
                    }
                });
            }
        });
    }
    private void Show() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setView(R.layout.dialogo_progreso);
        loading = builder.create();
        loading.show();
        loading.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    private void Message(String Title, String Message) {
        MaterialAlertDialogBuilder Builder = new MaterialAlertDialogBuilder(LoginActivity.this);
        Builder.setTitle(Title)
                .setMessage(Message)
                .setPositiveButton("Ok", null).show();
    }
}
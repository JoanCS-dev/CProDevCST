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
import android.widget.Toast;

import com.cst.ceramicpro.models.AuthRequestVM;
import com.cst.ceramicpro.models.AuthResponseVM;
import com.cst.ceramicpro.models.ResponseVM;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    private String URL = "";
    SharedPreferences cookies;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        cookies = getSharedPreferences("SHA_CST_DB", MODE_PRIVATE);

        URL = cookies.getString("url", "http://localhost:8082");

        if(URL == ""){
            Toast.makeText(this, "Por favor ingresa la url del servidor", Toast.LENGTH_SHORT).show();
        }

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
                    }else if(!ValidEmail(_email_)){
                        Message("Información", "Por favor escribe un correo electrónico valido");
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

    public void LogIn() {
        if(URL != ""){
            AuthRequestVM req = new AuthRequestVM();
            req.acUser = txt_Email.getText().toString();
            req.acPassword = txt_Pass.getText().toString();
            RequestBody body = RequestBody.create(gson.toJson(req), mediaType);
            Request request = new Request.Builder()
                    .url(URL + "/Api/Account/Auth")
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
                            try {
                                String string_json = response.body().string();
                                Type res_Type = new TypeToken<ResponseVM<AuthResponseVM>>() {
                                }.getType();
                                ResponseVM<AuthResponseVM> res = gson.fromJson(string_json, res_Type);
                                if (res.ok) {
                                    SharedPreferences sharedPreferences = getSharedPreferences("SHA_CST_DB", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("strToken", res.data.strToken);
                                    editor.putString("fullName", res.data.fullName);
                                    editor.putString("strCode", res.data.strCode);
                                    editor.apply();
                                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                                    finish();
                                } else {
                                    Message("Información", res.message);
                                }
                            } catch (Exception ex) {
                                Message("Error", ex.getMessage());
                            }
                        }
                    });
                }
            });

        }else{
            Message("Error", "Por favor ingresa la url del servidor");
        }
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
    private boolean ValidEmail(String email){
        Pattern pattern = Pattern
                .compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        Matcher mather = pattern.matcher(email);
        return mather.find();
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
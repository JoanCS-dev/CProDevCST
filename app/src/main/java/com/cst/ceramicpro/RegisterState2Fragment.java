package com.cst.ceramicpro;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.cst.ceramicpro.models.AccountRequestVM;
import com.cst.ceramicpro.models.PeopleRequestVM;
import com.cst.ceramicpro.models.ResponseVM;
import com.cst.ceramicpro.models.SettlementRequestVM;
import com.cst.ceramicpro.models.SettlementResponseVM;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class RegisterState2Fragment extends Fragment {
    private MediaType mediaType = MediaType.parse("application/json");
    private OkHttpClient client;
    private Gson gson;
    private Dialog loading;
    private Button B_Register_Full;
    private ImageButton Back;
    private String txt_Name, txt_Lastname, txt_Phone, txt_Email, txt_Pass;
    private TextInputEditText txt_CodigoPostal, txt_Estado, txt_Municipio, txt_Calle, txt_NumExterior, txt_NumInterior;
    private Button btn_search_cp;
    private String URL = "http://192.168.100.3:8082/Api";
    private View view;
    private AutoCompleteTextView autoCompleteAsentamiento;
    private List<SettlementResponseVM> Lst;
    private long SettlementID = 0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_register_state2, container, false);
        getParentFragmentManager().setFragmentResultListener("requestKey", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String key, @NonNull Bundle bundle) {
                // We use a String here, but any type that can be put in a Bundle is supported
                txt_Name = bundle.getString("txt_Name");
                txt_Lastname = bundle.getString("txt_Lastname");
                txt_Phone = bundle.getString("txt_Phone");
                txt_Email = bundle.getString("txt_Email");
                txt_Pass = bundle.getString("txt_Pass");
                // Do something with the result...
            }
        });

        client = new OkHttpClient();
        gson = new Gson();

        txt_CodigoPostal = view.findViewById(R.id.txt_CodigoPostal);
        txt_Estado = view.findViewById(R.id.txt_Estado);
        txt_Municipio = view.findViewById(R.id.txt_Municipio);
        txt_Calle = view.findViewById(R.id.txt_Calle);
        txt_NumExterior = view.findViewById(R.id.txt_NumExterior);
        txt_NumInterior = view.findViewById(R.id.txt_NumInterior);
        B_Register_Full = view.findViewById(R.id.Btn_Registered);
        Back = view.findViewById(R.id.Btn_Back_R);
        btn_search_cp = view.findViewById(R.id.btn_search_cp);
        autoCompleteAsentamiento = view.findViewById(R.id.autoCompleteAsentamiento);


        autoCompleteAsentamiento.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String data = adapterView.getItemAtPosition(i).toString();
                SettlementID = 0;
                for (SettlementResponseVM item: Lst) {
                    if(data == item.settDesc){
                        SettlementID = item.settlementID;
                        break;
                    }
                }
            }
        });

        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new RegisterState1Fragment();
                FragmentTransaction fm = getActivity().getSupportFragmentManager().beginTransaction();
                fm.replace(R.id.container,fragment).commit();
            }
        });

        B_Register_Full.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String __txt_CodigoPostal = txt_CodigoPostal.getText().toString();
                String __txt_Calle = txt_Calle.getText().toString();
                String __txt_NumExterior = txt_NumExterior.getText().toString();
                String __txt_NumInterior = txt_NumInterior.getText().toString();
                if(
                    __txt_CodigoPostal.equals("") ||
                    __txt_Calle.equals("") ||
                    __txt_NumExterior.equals("") ||
                    __txt_NumInterior.equals("") || SettlementID == 0){
                    if(__txt_CodigoPostal.equals("")) {
                        Message("Información", "Por favor escribe tu código postal.");
                    }else if(SettlementID == 0){
                        Message("Información", "Por favor selecciona tu asentamiento.");
                    }else if(__txt_Calle.equals("")){
                        Message("Información", "Por favor escribe la calle.");
                    }else if(__txt_NumExterior.equals("")){
                        Message("Información", "Por favor escribe el número exterior.");
                    }else if(__txt_NumInterior.equals("")){
                        Message("Información", "Por favor escribe el número interior.");
                    }
                }else{
                    Show();
                    SaveAs();
                }
            }
        });

        btn_search_cp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(txt_CodigoPostal.equals("")){
                    Message("Información", "Por favor escribe tu código postal");
                }else{
                    Show();
                    SearchCP();
                }
            }
        });

        return view;
    }

    public void SearchCP(){
        SettlementRequestVM req = new SettlementRequestVM();
        req.SettCP = txt_CodigoPostal.getText().toString();

        RequestBody body = RequestBody.create(gson.toJson(req), mediaType);
        Request request = new Request.Builder()
                .url(URL + "/Settlement/SettlementGetByCP")
                .post(body)
                .addHeader("Content-Type", "application/json")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loading.hide();
                        Message("Respuesta fallida!", "Ocurrió un error en el servidor. Verifica tu conexión a internet o por favor contactarse con Sistemas.");
                    }
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loading.hide();
                        try{
                            String string_json = response.body().string();
                            Type res_Type = new TypeToken<ResponseVM<List<SettlementResponseVM>>>() {}.getType();
                            ResponseVM<List<SettlementResponseVM>> res = gson.fromJson(string_json, res_Type);
                            if(res.ok){
                                txt_Estado.setText(res.data.get(0).stateName);
                                txt_Municipio.setText(res.data.get(0).municipalityName);
                                Lst = res.data;
                                ArrayList<String> arr = new ArrayList<>();
                                for (SettlementResponseVM item:Lst) {
                                    arr.add(item.settDesc);
                                }

                                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(view.getContext(), R.layout.drop_down_item, arr);
                                autoCompleteAsentamiento.setAdapter(arrayAdapter);

                            }else{
                                Message("Información", res.message);
                                txt_Estado.setText("");
                                txt_Municipio.setText("");
                            }
                        }catch (Exception ex){
                            Message("Error", ex.getMessage());
                            txt_Estado.setText("");
                            txt_Municipio.setText("");
                        }
                    }
                });
            }
        });
    }
    public void SaveAs(){
        PeopleRequestVM peopleRequestVM = new PeopleRequestVM();
        peopleRequestVM.peopleID = 0;
        peopleRequestVM.peFirstName = txt_Name;
        peopleRequestVM.peLastName = txt_Lastname;
        peopleRequestVM.peDateOfBirth = "2023-01-01";
        peopleRequestVM.peStatus = false;
        peopleRequestVM.peRDate = "2023-01-01";
        peopleRequestVM.peStreet = txt_Calle.getText().toString();
        peopleRequestVM.peOutsideCode = txt_NumExterior.getText().toString();
        peopleRequestVM.peInsideCode = txt_NumInterior.getText().toString();
        peopleRequestVM.settlementID = SettlementID;

        AccountRequestVM acc = new AccountRequestVM();
        acc.accountID = 0;
        acc.acUser = txt_Email;
        acc.acPassword = txt_Pass;
        acc.acEmailAddress = txt_Email;
        acc.acPhoneNumber = txt_Phone;
        acc.acVerifyEmail = false;
        acc.acStatus = "NA";
        acc.acRDate = "2023-01-01";
        acc.peopleID = 0;
        acc.profileID = 0;
        acc.peopleVM = peopleRequestVM;

        RequestBody body = RequestBody.create(gson.toJson(acc), mediaType);
        Request request = new Request.Builder()
                .url(URL + "/Account/Add")
                .post(body)
                .addHeader("Content-Type", "application/json")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loading.hide();
                        Message("Respuesta fallida!", "Ocurrió un error en el servidor. Verifica tu conexión a internet o por favor contactarse con Sistemas.");
                    }
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loading.hide();
                        try{
                            String string_json = response.body().string();

                            ResponseVM res = gson.fromJson(string_json, ResponseVM.class);
                            //Message("Información", res.message);
                            if(res.ok){
                                startActivity(new Intent(getActivity(), LoginActivity.class));
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
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setCancelable(false);
        builder.setView(R.layout.dialogo_progreso);
        loading = builder.create();
        loading.show();
        loading.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }
    private void Message(String Title, String Message) {
        MaterialAlertDialogBuilder Builder = new MaterialAlertDialogBuilder(view.getContext());
        Builder.setTitle(Title)
                .setMessage(Message)
                .setPositiveButton("Ok", null).show();
    }
}
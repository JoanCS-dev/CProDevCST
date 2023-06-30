package com.cst.ceramicpro;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.android.material.textfield.TextInputEditText;


public class RegisterState1Fragment extends Fragment {

    private Button B_Continuar;
    private ImageButton Back;
    private TextInputEditText txt_Name, txt_Lastname, txt_Phone, txt_Email, txt_Pass, txt_Confirm_Pass;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_register_state1, container, false);

        B_Continuar = view.findViewById(R.id.Btn_Continuar);
        Back = view.findViewById(R.id.Btn_Back_F);
        txt_Name = view.findViewById(R.id.txt_Name);
        txt_Lastname = view.findViewById(R.id.txt_Lastname);
        txt_Phone = view.findViewById(R.id.txt_Phone);
        txt_Email = view.findViewById(R.id.txt_Email);
        txt_Pass = view.findViewById(R.id.txt_Pass);
        txt_Confirm_Pass = view.findViewById(R.id.txt_Confirm_Pass);

        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), StartActivity.class));
            }
        });

        B_Continuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle result = new Bundle();
                result.putString("txt_Name", txt_Name.getText().toString());
                result.putString("txt_Lastname", txt_Lastname.getText().toString());
                result.putString("txt_Phone", txt_Phone.getText().toString());
                result.putString("txt_Email", txt_Email.getText().toString());
                result.putString("txt_Pass", txt_Pass.getText().toString());
                getParentFragmentManager().setFragmentResult("requestKey", result);
                Fragment fragment = new RegisterState2Fragment();
                FragmentTransaction fm = getActivity().getSupportFragmentManager().beginTransaction();
                fm.replace(R.id.container,fragment).commit();
            }
        });

        return view;
    }
}
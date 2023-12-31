package com.cst.ceramicpro;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class MembershipFragment extends Fragment {

    private ImageView ivQrCode;
    private View view;
    private SharedPreferences cookies;
    private String strCode;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_membership, container, false);
        cookies = view.getContext().getSharedPreferences("SHA_CST_DB", Context.MODE_PRIVATE);
        ivQrCode = view.findViewById(R.id.QR_content);
        strCode = cookies.getString("strCode", "");
        try {
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.encodeBitmap(
                    strCode,
                    BarcodeFormat.QR_CODE,
                    1000,
                    1000
            );
            ivQrCode.setImageBitmap(bitmap);
        }catch (Exception e) {
            e.printStackTrace();
        }

        return view;
    }


}
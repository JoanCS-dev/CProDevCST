package com.cst.ceramicpro;

import android.graphics.Bitmap;
import android.os.Bundle;

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_membership, container, false);

        ivQrCode = view.findViewById(R.id.QR_content);

        try {
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.encodeBitmap(
                    "IdGenerateRandom",
                    BarcodeFormat.QR_CODE,
                    750,
                    750
            );
            ivQrCode.setImageBitmap(bitmap);
        }catch (Exception e) {
            e.printStackTrace();
        }

        return view;
    }


}
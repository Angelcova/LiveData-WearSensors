package com.example.appos.presentation;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import es.uji.geotec.wearossensors.permissions.handler.PermissionsRequestHandler;


// Clase originaria de la Demo de BackgroundSensor, no se ha hecho ningún tipo de modificación
public class RequestDataPermissionsActivity extends FragmentActivity {
    private PermissionsRequestHandler handler;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        handler = new PermissionsRequestHandler(this);
        handler.onPermissionsResult(this::updateUI);
        handler.handleRequest();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        handler.handleResult(requestCode, permissions, grantResults);
    }

    private void updateUI(boolean success) {
        // Update your UI...
    }
}


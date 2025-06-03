package com.example.realtimedataapp;


import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

// Main de la aplicación del telefono
public class MainActivity extends Activity {

    private TextView textView;
    private Handler handler = new Handler();
    private Runnable sync;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Se muestra en la interfaz que se esperan los datos
        textView = new TextView(this);
        textView.setText(" Esperando datos desde el reloj...");
        setContentView(textView);

        // Se refresca periódicamente el texto con la última información obtenida
        sync = new Runnable() {
            @Override
            public void run() {
                String data = DataHolder.getData();
                textView.setText("Estado: " + data);
                handler.postDelayed(this, 1000);
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        handler.post(sync);
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(sync);
    }
}

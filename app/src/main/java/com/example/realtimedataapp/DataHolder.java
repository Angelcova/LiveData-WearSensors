package com.example.realtimedataapp;

import android.util.Log;

// Clase encargada de recoger el último dato enviado por el reloj
public class DataHolder {
    private static String infoWatch = " Esperando datos del reloj...";

    // Función encargada de guardar el nuevo valor
    public static synchronized void setData(String data) {
         infoWatch = data;
        Log.d("DataHolder", "Dato actualizado: " + data);
    }

    // Función encargada de obtener el valor del último dato obtenido
    public static synchronized String getData() {
        return infoWatch;
    }

}

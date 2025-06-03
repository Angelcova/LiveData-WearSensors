package com.example.realtimedataapp;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.WearableListenerService;

// Listener encargado de obtener la nueva información proveniente de la aplicación del reloj
public class DataListenerService extends WearableListenerService{
    private static final String TAG = "DataListenerService";
    private static final String PATH = "/datos_sensor";

    @Override
    public void onDataChanged(@NonNull DataEventBuffer dataEvents) {
        for (DataEvent event : dataEvents) {
            if (event.getType() == DataEvent.TYPE_CHANGED) {
                DataItem item = event.getDataItem();

                if (PATH.equals(item.getUri().getPath())) {
                    DataMap dataMap = DataMapItem.fromDataItem(item).getDataMap();
                    String valor = dataMap.getString("clave_dato");
                    Log.d(TAG, "Valor recibido: " + valor);
                    DataHolder.setData(valor);
                }
            }
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

}

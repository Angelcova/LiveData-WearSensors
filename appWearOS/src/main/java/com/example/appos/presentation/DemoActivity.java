package com.example.appos.presentation;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.appos.R;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

import java.util.List;

import es.uji.geotec.backgroundsensors.collection.CollectionConfiguration;
import es.uji.geotec.backgroundsensors.sensor.Sensor;
import es.uji.geotec.backgroundsensors.sensor.SensorManager;
import es.uji.geotec.backgroundsensors.service.manager.ServiceManager;
import es.uji.geotec.wearossensors.permissions.PermissionsManager;
import es.uji.geotec.wearossensors.sensor.WearSensor;
import es.uji.geotec.wearossensors.services.WearSensorRecordingService;

/* Clase basada en la Demo de Background sensor, modificada para la el uso de sensores como el de la frecuencia
cardiaca y para el envio de datos a la aplicación del telefono (última función no implmentada con
exito)
 */

public class DemoActivity extends Activity {

    private static final String TAG = "Demo";

    private SensorManager sensorManager;
    private ServiceManager serviceManager;
    private Spinner sensorSpinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);

        sensorManager = new SensorManager(this);
        serviceManager = new ServiceManager(this, WearSensorRecordingService.class);

        // Inject the permissions Activity
        PermissionsManager.setPermissionsActivity(this, RequestDataPermissionsActivity.class);

        // Se piden permisos para poder acceder a los sensores de localización y de frecuencia cardiaca
        if (ActivityCompat.checkSelfPermission(DemoActivity.this, Manifest.permission.BODY_SENSORS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.BODY_SENSORS}, 1001);
        }

        // Se piden los permisos necesarios para Android 13+ y los permisos de notificación
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            PermissionsManager.launchRequiredPermissionsRequest(this);
            serviceManager.enableServiceNotification();
        }

        sensorSpinner = findViewById(R.id.sensors_spinner);

        // Se obtienen los sensores que están disponibles y se guardan
        List<Sensor> sensors = sensorManager.availableSensors(WearSensor.values());
        ArrayAdapter<Sensor> adapter = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, sensors);


        sensorSpinner.setAdapter(adapter);
        sensorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d(TAG, "onItemSelected: " + adapterView.getItemAtPosition(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    // Función encargada de comenzar la recolleción de la información de los sensores
    public void onStartTap(View view) {
        WearSensor selectedSensor = (WearSensor) sensorSpinner.getSelectedItem();
        CollectionConfiguration config = new CollectionConfiguration(
                selectedSensor,
                android.hardware.SensorManager.SENSOR_DELAY_GAME,
                selectedSensor == WearSensor.HEART_RATE || selectedSensor == WearSensor.LOCATION ? 1 : 50
        );

        serviceManager.startCollection(config, records -> {

            // Se muestran las muestras recogidas y la primera de estas
            Log.d(TAG, "onRecordsCollected: " + records.size() + " records");
            Log.d(TAG, "a sample: " + records.get(0));

            // Se intenta mandar el numero 1 al telefono para probar la conectivad aunque sin exito
            PutDataMapRequest putDataMapReq = PutDataMapRequest.create("/datos_sensor");
            putDataMapReq.getDataMap().putString("clave_dato", "1");
            PutDataRequest putDataReq = putDataMapReq.asPutDataRequest().setUrgent();

            Wearable.getDataClient(this)
                    .putDataItem(putDataReq)
                    .addOnSuccessListener(dataItem -> Log.d("Wear", "Dato enviado con éxito"))
                    .addOnFailureListener(e -> Log.e("Wear", "Error al enviar dato", e));

        });
    }

    // Función encargada de parar la recolección de datos
    public void onStopTap(View view) {
        Sensor sensor = (Sensor) sensorSpinner.getSelectedItem();
        this.serviceManager.stopCollection(sensor);
    }
}
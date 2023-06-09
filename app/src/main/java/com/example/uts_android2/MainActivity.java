package com.example.uts_android2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Priority;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.annotation.AnnotationFormatError;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;




public class MainActivity extends AppCompatActivity implements List<ModelMain> {

    int REQ_PERMISSION = 100;
    double strCurrentLatitude;
    double strCurrentLongitude;
    String strCurrentLatLong, strImage;
    SimpleLocation simpleLocation;
    ProgressBar pbLoading;
    MainAdapter mainAdapter;
    RecyclerView rvListHospital;
    TextView tvCurrentLocation;
    ImageView imageProfile;
    List<ModelMain> modelMainList = new ArrayList<>();
    private int bits;

    SensorManager sensorManager;
    private Object SensorManager;
    TextView sensorText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageProfile = findViewById(R.id.imageProfile);
        tvCurrentLocation = findViewById(R.id.tvCurrentLocation);
        pbLoading = findViewById(R.id.pbLoading);
        rvListHospital = findViewById(R.id.rvListHospital);
        sensorText = findViewById(R.id.sensor);

        setPermission();
        setStatusBar();
        setLocation();
        setInitLayout();

        //get data rumah sakit
        getRumahSakit();

        //get nama daerah
        getCurrentLocation();

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);

        SensorEventListener temperatur = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                if (sensorEvent.sensor.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE) {
                    sensorText.setText("" + sensorEvent.values[0]);
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        };

        sensorManager.registerListener(temperatur, sensor, sensorManager.SENSOR_DELAY_NORMAL);

    }

    private void setLocation() {
        simpleLocation = new SimpleLocation(this);

        if (!simpleLocation.hasLocationEnabled()) {
            SimpleLocation.openSettings(this);
        }

        //get location
        strCurrentLatitude = simpleLocation.getLatitude();
        strCurrentLatLong = simpleLocation.getLongitude();

        //set location latlong
        strCurrentLatLong = strCurrentLatitude + "," + strCurrentLongitude;
    }

    private void setInitLayout() {
        mainAdapter = new MainAdapter(MainActivity.this, modelMainList);
        rvListHospital.setHasFixedSize(true);
        rvListHospital.setLayoutManager(new LinearLayoutManager(this));
        rvListHospital.setAdapter(mainAdapter);

        imageProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });
    }

    private void getCurrentLocation() {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addressList = geocoder.getFromLocation(strCurrentLatitude, strCurrentLongitude, 1);
            if (addressList != null && addressList.size() > 0) {
                String strCurrentLocation = addressList.get(0).getLocality();
                tvCurrentLocation.setText(strCurrentLocation);
                tvCurrentLocation.setSelected(true);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQ_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_GRANTED) {
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_PERMISSION && requestCode == RESULT_OK) {
            //load data rs
            getRumahSakit();
        }
    }
    
    private void getRumahSakit() {
        pbLoading.setVisibility(View.VISIBLE);
        AndroidNetworking.get(ApiClient.BASE_URL + strCurrentLatLong + ApiClient.TYPE + ApiClient.API_KEY).setPriority(Priority.IMMEDIATE).build().getAsJSONObject(new JSONObjectRequestListener(){
            @Override
            public void onResponse(JSONObject response) {
                try {
                    pbLoading.setVisibility(View.GONE);
                    JSONArray jsonArrayResult = response.getJSONArray("results");
                    for (int i=0; i < jsonArrayResult.length(); i++) {
                        JSONObject jsonObjectResult = jsonArrayResult.getJSONObject(i);

                        ModelMain modelMain = new ModelMain();
                        modelMain.setStrName(jsonArrayResult.getString("name"));
                        modelMain.setStrVicinity(jsonArrayResult.getString("vicinity"));

                        //get latlong
                        JSONObject jsonObjectGeo = jsonObjectResult.getJSONObject("geometry");
                        JSONObject jsonObjectLoc = jsonObjectGeo.getJSONObject("location");

                        modelMain.setLatloc(jsonObjectLoc.getDouble("lat"));
                        modelMain.setLongloc(jsonObjectLoc.getDouble("lng"));

                        //handle photo result
                        try {
                            JSONArray jsonArrayImage = jsonArrayResult.getJSONArray("photos");
                            for (int x=0; x < jsonArrayImage.length(); x++) {
                                JSONObject jsonObjectData = jsonArrayImage.getJSONObject(x);
                                strImage = jsonObjectData.getString("photo_reference");
                                modelMain.setStrPhoto(strImage);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            modelMain.setStrPhoto(null);
                        }
                        modelMainList.add(modelMain);
                    }
                    mainAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "Gagal menampilkan data!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(AnnotationFormatError annotationFormatError) {
                pbLoading.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, "Tidak ada jaringan internet!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setStatusBar() {
        if (Build.VERSION.SDK_INT < 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            }
        }
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    private void setWindowFlag(MainActivity mainActivity, int flagTranslucentStatus, boolean b) {
        Window window = mainActivity.getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        if (b) {
            layoutParams.flags |= bits;
        } else {
            layoutParams.flags &= ~bits;
        }
        window.setAttributes(layoutParams);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle options menu item clicks here.
        int id = item.getItemId();
        if (id ==R.id.action_language) {
            Intent languageIntent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
            startActivity(languageIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean contains(@Nullable Object o) {
        return false;
    }

    @NonNull
    @Override
    public Iterator<ModelMain> iterator() {
        return null;
    }

    @NonNull
    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @NonNull
    @Override
    public <T> T[] toArray(@NonNull T[] ts) {
        return null;
    }

    @Override
    public boolean add(ModelMain modelMain) {
        return false;
    }

    @Override
    public boolean remove(@Nullable Object o) {
        return false;
    }

    @Override
    public boolean containsAll(@NonNull Collection<?> collection) {
        return false;
    }

    @Override
    public boolean addAll(@NonNull Collection<? extends ModelMain> collection) {
        return false;
    }

    @Override
    public boolean addAll(int i, @NonNull Collection<? extends ModelMain> collection) {
        return false;
    }

    @Override
    public boolean removeAll(@NonNull Collection<?> collection) {
        return false;
    }

    @Override
    public boolean retainAll(@NonNull Collection<?> collection) {
        return false;
    }

    @Override
    public void clear() {

    }

    @Override
    public ModelMain get(int i) {
        return null;
    }

    @Override
    public ModelMain set(int i, ModelMain modelMain) {
        return null;
    }

    @Override
    public void add(int i, ModelMain modelMain) {

    }

    @Override
    public ModelMain remove(int i) {
        return null;
    }

    @Override
    public int indexOf(@Nullable Object o) {
        return 0;
    }

    @Override
    public int lastIndexOf(@Nullable Object o) {
        return 0;
    }

    @NonNull
    @Override
    public ListIterator<ModelMain> listIterator() {
        return null;
    }

    @NonNull
    @Override
    public ListIterator<ModelMain> listIterator(int i) {
        return null;
    }

    @NonNull
    @Override
    public List<ModelMain> subList(int i, int i1) {
        return null;
    }

    private class SimpleLocation {
    }

    private abstract class JSONObjectRequestListener {
        public abstract void onResponse(JSONObject response);

        public abstract void onError(AnnotationFormatError annotationFormatError);
    }
}
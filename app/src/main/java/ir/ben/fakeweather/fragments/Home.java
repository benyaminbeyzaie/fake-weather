package ir.ben.fakeweather.fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ir.ben.fakeweather.R;


public class Home extends Fragment {


    TextView time ,humidity,maxtemp , mintemp,wind,pressure;

    public Home() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_home, container, false);

        time = view.findViewById(R.id.time_id);
        humidity = view.findViewById(R.id.humidity_id);
        maxtemp = view.findViewById(R.id.maxtemp_id);
        mintemp = view.findViewById(R.id.mintemp_id);
        wind = view.findViewById(R.id.wind_id);
        pressure = view.findViewById(R.id.pressure_id);



        return view;
    }



}
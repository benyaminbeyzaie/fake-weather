package ir.ben.fakeweather.fragments;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import ir.ben.fakeweather.R;
import ir.ben.fakeweather.models.Daily;


public class ShowWeather extends Fragment {

    private Daily current;
    private String time;
    private ImageView imageView;
    private TextView timeView;
    private  TextView humidity;
    private  TextView maxtemp;
    private  TextView mintemp;
    private  TextView wind;
    private  TextView pressure;
    private TextView status;
    private TextView windDegree;



    public ShowWeather() {
        // Required empty public constructor
    }


    public ShowWeather(Daily daily , String time) {
        this.current = daily;
        this.time = time;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_show_weather, container, false);
        imageView = view.findViewById(R.id.imageView_icon_show);
        timeView = view.findViewById(R.id.time_id_show);
        humidity = view.findViewById(R.id.humidity_id_show);
        maxtemp = view.findViewById(R.id.maxtemp_id_show);
        mintemp = view.findViewById(R.id.mintemp_id_show);
        wind = view.findViewById(R.id.wind_id_show);
        pressure = view.findViewById(R.id.pressure_id_show);
        status = view.findViewById(R.id.status_id_show);
        windDegree = view.findViewById(R.id.wind_deg_id_show);


        timeView.setText(time);
        humidity.setText(":\t" + current.getHumidity()+"");
        maxtemp.setText(":\t" +current.getTemp().getMax() +"");
        mintemp.setText(":\t" +current.getTemp().getMin()+"");
        wind.setText(":\t" +current.getWindSpeed()+"");
        windDegree.setText(":\t" +current.getWindDeg()+"");
        pressure.setText(":\t" +current.getPressure()+"");
        String iconCode = current.getWeather().get(0).getIcon();
//        String iconCode = response.body().getCurrent().getWeather().get(0).getIcon();
        Picasso.get().load("https://openweathermap.org/img/wn/"+iconCode+"@2x.png").
                placeholder(R.drawable.ic_launcher_background).into(imageView);
        status.setText(current.getWeather().get(0).getDescription());

        imageView.setOnClickListener(view1 -> {
//            getActivity().getFragmentManager().beginTransaction().remove(ShowWeather.this).commit();
            Toast.makeText(getContext(), "cli", Toast.LENGTH_SHORT).show();
        });

        CardView cardView = view.findViewById(R.id.cardView);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        return view;
    }



}
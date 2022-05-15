package ir.ben.fakeweather.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.Log;
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
    private String time , icon , hu , mxt , mit , win , windir , pres , stat ;
    private ImageView imageView;
    private TextView timeView;
    private  TextView humidity;
    private  TextView maxtemp;
    private  TextView mintemp;
    private  TextView wind;
    private  TextView pressure;
    private TextView status;
    private TextView windDegree;


    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;


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
//        sharedPref = getContext().getSharedPreferences("Weather", MODE_PRIVATE);
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

        if (current!=null) {

            hu = current.getHumidity()+"";
            mxt = current.getTemp().getMax() + "";
            mit = current.getTemp().getMin() + "";
            win = current.getWindSpeed() + "";
            windir = current.getWindDeg() + "";
            pres = current.getPressure() + "";
            stat = current.getWeather().get(0).getDescription();
            icon = current.getWeather().get(0).getIcon();


            timeView.setText(time);
            humidity.setText(":\t".concat(hu));
            maxtemp.setText(":\t".concat(mxt));
            mintemp.setText(":\t".concat(mit));
            wind.setText(":\t".concat(win));
            windDegree.setText(":\t".concat(windir));
            pressure.setText(":\t".concat(pres));


//        String iconCode = response.body().getCurrent().getWeather().get(0).getIcon();
            Picasso.get().load("https://openweathermap.org/img/wn/" + icon + "@2x.png").
                    placeholder(R.drawable.ic_launcher_background).into(imageView);
            status.setText(stat);
        }
//        else {
//
//            time = sharedPref.getString(getString(R.string.get_time) , "00:00");
//            hu = sharedPref.getString(getString(R.string.get_humidity) , "00:00");
//            mxt = sharedPref.getString(getString(R.string.get_maxtemp) , "00:00");
//            mit = sharedPref.getString(getString(R.string.get_mintemp) , "00:00");
//            win = sharedPref.getString(getString(R.string.get_wind) , "00:00");
//            windir = sharedPref.getString(getString(R.string.get_wind_dir) , "00:00");
//            pres = sharedPref.getString(getString(R.string.get_pressure) , "00:00");
//            stat = sharedPref.getString(getString(R.string.get_status) , "00:00");
//            icon = sharedPref.getString(getString(R.string.get_icon) , "00:00");;
//
//
//            timeView.setText(time);
//            humidity.setText(":\t".concat(hu));
//            maxtemp.setText(":\t".concat(mxt));
//            mintemp.setText(":\t".concat(mit));
//            wind.setText(":\t".concat(win));
//            windDegree.setText(":\t".concat(windir));
//            pressure.setText(":\t".concat(pres));
//
//
////        String iconCode = response.body().getCurrent().getWeather().get(0).getIcon();
//            Picasso.get().load("https://openweathermap.org/img/wn/" + icon + "@2x.png").
//                    placeholder(R.drawable.ic_launcher_background).into(imageView);
//            status.setText(stat);
//        }

        return view;
    }

    @Override
    public void onPause() {
        Log.d("show" , " paused");

//        sharedPref.edit().putString(getString(R.string.get_time) , time).apply();
//        sharedPref.edit().putString(getString(R.string.get_humidity) , hu).apply();
//        sharedPref.edit().putString(getString(R.string.get_pressure) , pres).apply();
//        sharedPref.edit().putString(getString(R.string.get_maxtemp) , mxt).apply();
//        sharedPref.edit().putString(getString(R.string.get_mintemp) , mit).apply();
//        sharedPref.edit().putString(getString(R.string.get_icon) , icon).apply();
//        sharedPref.edit().putString(getString(R.string.get_status) , stat).apply();
//        sharedPref.edit().putString(getString(R.string.get_wind) , win).apply();
//        sharedPref.edit().putString(getString(R.string.get_wind_dir) , windir).apply();

        super.onPause();
    }


    @Override
    public void onStop() {
        Log.d("show" , " stopped");
        super.onStop();
    }

    @Override
    public void onDestroy() {
        Log.d("show" , " destroy");
        super.onDestroy();
    }
}
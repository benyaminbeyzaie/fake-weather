package ir.ben.fakeweather.utils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ir.ben.fakeweather.R;
import ir.ben.fakeweather.models.Daily;

public class WeatherAdaptor extends RecyclerView.Adapter<WeatherAdaptor.WeatherViewHolder> {

    private HashMap<Integer , String> map = new HashMap<>();

    private List<Daily> dailies = new ArrayList<>();

    @NonNull
    @Override
    public WeatherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.weather_design , parent , false);
        map.put(0 , "Today");
        map.put(1 , "Tommorow");
        for (int i = 2; i <=7 ; i++) {
            map.put(i , i+" days later");
        }
//        map.put(2 , "3 days later");

        return new WeatherViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherViewHolder holder, int position) {
        Daily current = dailies.get(position);
        holder.time.setText(map.get(position));
        holder.humidity.setText(current.getHumidity()+"");
//        holder.maxtemp.setText(current.getTemp().getMax() +"");
//        holder.mintemp.setText(current.getTemp().getMin()+"");
        holder.wind.setText(current.getWindSpeed()+"");
        holder.windDeg.setText(current.getWindDeg()+"");
        holder.pressure.setText(current.getPressure()+"");
//        String iconCode = response.body().getCurrent().getWeather().get(0).getIcon();
        Picasso.get().load("https://openweathermap.org/img/wn/"+current.getIcon()+"@2x.png").
                placeholder(R.drawable.ic_launcher_background).into(holder.imageView);
        holder.status.setText(current.getDescription());
    }

    @Override
    public int getItemCount() {
        return dailies.size();
    }

    public Daily getDaily(int position){
        return dailies.get(position);
    }

    public void setDailies(List<Daily> dailies){
        this.dailies = dailies;
        notifyDataSetChanged();
    }

    public void clear() {
        int size = getItemCount();
        dailies.clear();
        notifyItemRangeRemoved(0 , size);
    }


    public class WeatherViewHolder extends RecyclerView.ViewHolder{

        private ImageView imageView;
        private  TextView time;
        private  TextView humidity;
//        private  TextView maxtemp;
//        private  TextView mintemp;
        private  TextView wind;
        private  TextView windDeg;
        private  TextView pressure;
        private TextView status;

        public WeatherViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView_icon);
            time = itemView.findViewById(R.id.time_id);
            humidity = itemView.findViewById(R.id.humidity_id);
//            maxtemp = itemView.findViewById(R.id.maxtemp_id);
//            mintemp = itemView.findViewById(R.id.mintemp_id);
            wind = itemView.findViewById(R.id.wind_id);
            windDeg = itemView.findViewById(R.id.wind_deg);
            pressure = itemView.findViewById(R.id.pressure_id);
            status = itemView.findViewById(R.id.status_id);
        }
    }
}

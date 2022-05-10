package ir.ben.fakeweather.utils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import ir.ben.fakeweather.MainActivity;
import ir.ben.fakeweather.R;
import ir.ben.fakeweather.models.Daily;

public class WeatherAdaptor extends RecyclerView.Adapter<WeatherAdaptor.WeatherViewHolder> {

    private HashMap<Integer, String> map = new HashMap<>();
    private HashMap<String, Integer> map2 = new HashMap<>();

    private List<Daily> dailies = new ArrayList<>();
    int today;

    @NonNull
    @Override
    public WeatherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.weather_design, parent, false);
        Calendar calendar = Calendar.getInstance();
        map2.put("Monday", 1);
        map2.put("Tuesday", 2);
        map2.put("Wednesday", 3);
        map2.put("Thursday", 4);
        map2.put("Friday", 5);
        map2.put("Saturday", 6);
        map2.put("Sunday", 7);

        for (Map.Entry<String, Integer> stringIntegerEntry : map2.entrySet()) {
            map.put(stringIntegerEntry.getValue(), stringIntegerEntry.getKey());
        }

        Date date = calendar.getTime();
        String tod = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(date.getTime());
//        today = map2.get(tod);
        today = map2.get("Sunday");
//        map.put(0, new SimpleDateFormat("EEEE", Locale.ENGLISH).format(date.getTime()));
//
//
//        map.put(1, "Tommorow");
//        for (int i = 2; i <= 7; i++) {
//            map.put(i, i + " days later");
//        }
//        map.put(2 , "3 days later");

        return new WeatherViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherViewHolder holder, int position) {
        Daily current = dailies.get(position);
        int index = today + position;
        if (index>=8){
            holder.time.setText(String.format(":\t"  +"Next %s" , map.get(index-7)));
        }else {
            holder.time.setText(":\t"  +map.get(index));
        }

        holder.humidity.setText(":\t"  +current.getHumidity() + "");
        holder.maxtemp.setText(":\t"  +current.getTemp().getMax() + "");
        holder.mintemp.setText(":\t"  +current.getTemp().getMin() + "");
//        holder.wind.setText(current.getWindSpeed()+"");
//        holder.pressure.setText(current.getPressure()+"");
        String iconCode = current.getWeather().get(0).getIcon();
//        String iconCode = response.body().getCurrent().getWeather().get(0).getIcon();
        Picasso.get().load("https://openweathermap.org/img/wn/" + iconCode + "@2x.png").
                placeholder(R.drawable.ic_launcher_background).into(holder.imageView);
        holder.status.setText(current.getWeather().get(0).getDescription());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "CLICKED", Toast.LENGTH_SHORT).show();
                if (index>=8){
                    ((MainActivity) view.getContext()).showMore(current , String.format("Next %s" , map.get(index-7)));
//                    holder.time.setText(String.format("Next %s" , map.get(index-7)));
                }else {
                    ((MainActivity) view.getContext()).showMore(current , map.get(index));
//                    holder.time.setText(map.get(index));
                }


            }
        });

    }

    @Override
    public int getItemCount() {
        return dailies.size();
    }

    public Daily getDaily(int position) {
        return dailies.get(position);
    }

    public void setDailies(List<Daily> dailies) {
        this.dailies = dailies;
        notifyDataSetChanged();
    }


    public class WeatherViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;
        private TextView time;
        private TextView humidity;
        private TextView maxtemp;
        private TextView mintemp;
        //        private  TextView wind;
//        private  TextView pressure;
        private TextView status;
        private final CardView cardView;

        public WeatherViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView_icon);
            time = itemView.findViewById(R.id.time_id);
            humidity = itemView.findViewById(R.id.humidity_id);
            maxtemp = itemView.findViewById(R.id.maxtemp_id);
            mintemp = itemView.findViewById(R.id.mintemp_id);
//            wind = itemView.findViewById(R.id.wind_id);
//            pressure = itemView.findViewById(R.id.pressure_id);
            status = itemView.findViewById(R.id.status_id);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }
}
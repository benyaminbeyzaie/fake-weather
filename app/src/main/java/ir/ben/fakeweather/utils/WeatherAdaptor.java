package ir.ben.fakeweather.utils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

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

    private final HashMap<Integer, String> map = new HashMap<>();
    private final HashMap<String, Integer> map2 = new HashMap<>();

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
        today = map2.get(tod);
        return new WeatherViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherViewHolder holder, int position) {
        Daily current = dailies.get(position);
        int index = today + position;
        if (index == today) {
            holder.time.setText(":\t".concat(map.get(index) + " (today)"));
        } else if (index >= 8) {
            holder.time.setText(String.format(":\t" + "Next %s", map.get(index - 7)));
        } else {
            holder.time.setText(":\t".concat(map.get(index)));
        }

        String humidity = current.getHumidity() == null ? "-" : current.getHumidity().toString();
        String max = "-";
        String min = "-";

        if (current.getTemp() != null) {
            max = current.getTemp().getMax() == null ? "-" : current.getTemp().getMax().toString();
            min = current.getTemp().getMax() == null ? "-" : current.getTemp().getMin().toString();
        }


        holder.humidity.setText(":\t".concat(humidity + ""));
        holder.maxtemp.setText(":\t".concat(max + ""));
        holder.mintemp.setText(":\t".concat(min + ""));
        String iconCode = "-";
        String description = "-";

        if (current.getWeather() != null && current.getWeather().size() > 0) {
            iconCode = current.getWeather().get(0).getIcon();
            description = current.getWeather().get(0).getDescription();
        }
        Picasso.get().load("https://openweathermap.org/img/wn/" + iconCode + "@2x.png").
                placeholder(R.drawable.ic_launcher_background).into(holder.imageView);
        holder.status.setText(description);
        holder.cardView.setOnClickListener(view -> {

            if (index >= 8) {
                ((MainActivity) view.getContext()).showMore(current, String.format("Next %s", map.get(index - 7)));
            } else {
                ((MainActivity) view.getContext()).showMore(current, map.get(index));
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

    public void clear() {
        int size = getItemCount();
        dailies.clear();
        notifyItemRangeRemoved(0, size);
    }


    public class WeatherViewHolder extends RecyclerView.ViewHolder {

        private final ImageView imageView;
        private final TextView time;
        private final TextView humidity;
        private final TextView maxtemp;
        private final TextView mintemp;
        private final TextView status;
        private final CardView cardView;

        public WeatherViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView_icon);
            time = itemView.findViewById(R.id.time_id);
            humidity = itemView.findViewById(R.id.humidity_id);
            maxtemp = itemView.findViewById(R.id.maxtemp_id);
            mintemp = itemView.findViewById(R.id.mintemp_id);
            status = itemView.findViewById(R.id.status_id);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }
}

package ir.ben.fakeweather.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class Functions {

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }



    public static void toast(@Nullable Context context , String message){
        if (context != null){
            Toast.makeText(context , message, Toast.LENGTH_SHORT).show();
        }
    }
}

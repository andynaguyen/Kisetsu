package com.nguyen.andy.kisetsu;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Sole purpose is to check if the phone is connected to the internet
 */
public class ConnectivityCheck {
    /**
     * Checks for internet connectivity
     * @param context which activity called this method
     * @return whether the phone is connected to the internet
     */
    public static boolean isConnectedToInternet(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }
}

package com.shd.halperclass.otherClass;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
public class CheckInternet {
    final Context context;

    public CheckInternet(Context context) {
        this.context = context;
    }

    public boolean Check() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager == null) {
            return false; // Connectivity manager unavailable
        }

        NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
        return capabilities != null && (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR));
    }
}

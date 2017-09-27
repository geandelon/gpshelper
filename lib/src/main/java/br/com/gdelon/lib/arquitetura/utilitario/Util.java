package br.com.gdelon.lib.arquitetura.utilitario;

import android.content.Context;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;

import br.com.gdelon.lib.BuildConfig;
import br.com.gdelon.lib.arquitetura.GPSLib;

/**
 * Created by geandelon on 09/06/2017.
 */
public class Util {

    private final static String TAG = Util.class.getSimpleName();

    public static float[] calcularDistancia(Location anterior, Location atual) {
        float[] dist = new float[1];
        if (anterior != null && atual != null) {
            Location.distanceBetween(anterior.getLatitude(), anterior.getLongitude(), atual.getLatitude(), atual.getLongitude(), dist);
        }
        return dist;
    }

    public static boolean isUsandoFakeGps(Location location) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            if (location.isFromMockProvider()) {
                Log.d(TAG, "-- !!!! FAKE GPS ATIVADO !!!! --");
                if (BuildConfig.DEBUG) {
                    Log.i(TAG, "DEBUG validaFakeGps");
                    return false;
                }
                return true;
            }
        }
        return false;
    }

    public static boolean isConectadoInternet() {
        Context context = GPSLib.getContext();
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }
}

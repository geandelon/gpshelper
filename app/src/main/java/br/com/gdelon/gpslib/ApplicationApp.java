package br.com.gdelon.gpslib;

import android.app.Application;

import com.facebook.stetho.Stetho;

import br.com.gdelon.lib.arquitetura.GPSLib;

/**
 * Created by geandelon on 19/06/2017.
 */

public class ApplicationApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        GPSLib.inicializar(getApplicationContext().getApplicationContext());
        Stetho.initializeWithDefaults(this);
    }
}

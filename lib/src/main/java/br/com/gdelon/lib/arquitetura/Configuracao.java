package br.com.gdelon.lib.arquitetura;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import br.com.gdelon.lib.R;

/**
 * Created by geandelon on 09/06/2017.
 */
public final class Configuracao {

    private final String TAG = Configuracao.class.getSimpleName();

    private SharedPreferences sharedPreferences;

    private Context contexto;

    public Configuracao(Context contexto) {
        this.contexto = contexto;
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(contexto);
    }

    public Configuracao() {
        this.contexto = GPSLib.getContext();
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(contexto);
    }

    public boolean isRastrearUsuario() {
        return this.sharedPreferences.getBoolean(
                this.contexto.getString(R.string.config_rastrear_usuario),
                contexto.getResources().getBoolean(R.bool.config_padrao_bool));
    }

    public void setRastrearUsuario(boolean rastrearUsuario) {
        sharedPreferences.edit().putBoolean(
                contexto.getString(R.string.config_rastrear_usuario),
                rastrearUsuario).apply();
    }

    public void setCodigoUsuario(String codigoUsuario) {
        this.sharedPreferences.edit().putString(
                this.contexto.getString(R.string.config_codigo_usuario), codigoUsuario).apply();
    }

    public String getCodigoUsuario() {
        return this.sharedPreferences.getString(this.contexto.getString(R.string.config_codigo_usuario),
                null);
    }

    public long getIntervaloAtualizacaoTempo() {
        return this.sharedPreferences.getLong(this.contexto.getString(R.string.config_intervalo_coleta_tempo_gps),
                3000L);
    }

    public void setIntervaloAtualizacaoTempo(long intervalo) {
        sharedPreferences.edit().putLong(
                contexto.getString(R.string.config_intervalo_coleta_tempo_gps),
                intervalo).apply();
    }

    public long getIntervaloAtualizacaoDistencia() {
        return this.sharedPreferences.getLong(this.contexto.getString(R.string.config_intervalo_coleta_distancia_gps),
                25L);
    }

    public void setIntervaloAtualizacaoDistencia(long intervalo) {
        sharedPreferences.edit().putLong(
                contexto.getString(R.string.config_intervalo_coleta_distancia_gps),
                intervalo).apply();
    }

    public void setTituloNotificacao(String tituloNotificacao) {
        this.sharedPreferences.edit().putString(
                this.contexto.getString(R.string.config_titulo_notificacao), tituloNotificacao).apply();
    }

    public String getTituloNotificacao() {
        return this.sharedPreferences.getString(this.contexto.getString(R.string.config_titulo_notificacao),
                "Máxima Sistemas");
    }

    public int getIconeNotificacao() {
        return this.sharedPreferences.getInt(this.contexto.getString(R.string.config_icone_notificacao),
                R.mipmap.ic_gps);
    }

    public void setIconeNotificacao(int idIconeNotificacao) {
        sharedPreferences.edit().putInt(
                contexto.getString(R.string.config_icone_notificacao),
                idIconeNotificacao).apply();
    }


}

package br.com.gdelon.lib.arquitetura;

import android.content.Context;

import br.com.gdelon.lib.arquitetura.db.DatabaseHelper;
import br.com.gdelon.lib.arquitetura.db.DatabaseManager;

/**
 * Created by geandelon on 19/06/2017.
 */

public final class GPSLib {
    private static Context context;
    private static Configuracao configuracao;

    public static void inicializar(Context contexto) {
        GPSLib.context = contexto;
        configuracao = new Configuracao(contexto);
        DatabaseManager.inicializarInstancia(new DatabaseHelper(contexto));
    }

    public static void inicializar(Context contexto, String tituloNotificacao, int iconeNotificacao) {
        inicializar(contexto);
        configuracao = new Configuracao(contexto);
        configuracao.setTituloNotificacao(tituloNotificacao);
        configuracao.setIconeNotificacao(iconeNotificacao);
    }

    public static Context getContext() {
        return context;
    }

    public static Configuracao getConfiguracao() {
        return configuracao;
    }
}

package br.com.gdelon.lib.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import br.com.gdelon.lib.arquitetura.excecao.PersistenciaExcecao;
import br.com.gdelon.lib.model.Evento;
import br.com.gdelon.lib.model.persistencia.EventoDaoSqlite;

/**
 * Created by geandelon on 12/06/2017.
 */
public class SalvaEventoService extends IntentService {

    private final String TAG = SalvaEventoService.class.getSimpleName();

    public SalvaEventoService() {
        super(SalvaEventoService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Evento evento = intent.getParcelableExtra(GPSService.EVENTO);
        EventoDaoSqlite eventoDaoSqlite = new EventoDaoSqlite();
        try {
            eventoDaoSqlite.incluir(evento);
        } catch (PersistenciaExcecao ex) {
            Log.e(TAG, "onHandleIntent: Detalhes: ", ex);
        }
    }
}

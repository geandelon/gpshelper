package br.com.gdelon.lib.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import br.com.gdelon.lib.arquitetura.excecao.PersistenciaExcecao;
import br.com.gdelon.lib.model.Rastreamento;
import br.com.gdelon.lib.model.persistencia.RastreamentoDaoSqlite;

/**
 * Created by geandelon on 19/08/2016.
 */

public class SalvaRastreamentoService extends IntentService {

    private final String TAG = SalvaRastreamentoService.class.getSimpleName();

    public SalvaRastreamentoService() {
        super(SalvaRastreamentoService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Rastreamento rastreamento = intent.getParcelableExtra(GPSService.RASTREAMENTO);
        RastreamentoDaoSqlite rastreamentoDaoSqlite = new RastreamentoDaoSqlite();
        try {
            rastreamentoDaoSqlite.incluir(rastreamento);
        } catch (PersistenciaExcecao persistenciaExcecao) {
            Log.e(TAG, "onHandleIntent: Detalhes: ", persistenciaExcecao);
        }
    }
}

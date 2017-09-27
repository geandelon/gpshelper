package br.com.gdelon.gpslib;

import android.Manifest;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.Address;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.joda.time.DateTime;

import java.util.List;
import java.util.UUID;

import br.com.gdelon.lib.arquitetura.Configuracao;
import br.com.gdelon.lib.arquitetura.GPSLib;
import br.com.gdelon.lib.arquitetura.excecao.PersistenciaExcecao;
import br.com.gdelon.lib.model.Evento;
import br.com.gdelon.lib.model.Rastreamento;
import br.com.gdelon.lib.model.persistencia.EventoDaoSqlite;
import br.com.gdelon.lib.model.persistencia.RastreamentoDaoSqlite;
import br.com.gdelon.lib.service.GPSService;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class MainActivity extends AppCompatActivity implements ServiceConnection, GPSService.OnMelhorLocalizacaoCapturadaListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    public static final String RCA_TESTE = "356";

    private GPSService.IGPSService mGpsService;

    private Intent gpsServiceIntent;

    private Configuracao configuracao;

    private Button btnRastrearUsuario;

    private Button btnIniciarServicoGps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        btnRastrearUsuario = (Button) findViewById(R.id.btn_rastreio_usuario);
        btnIniciarServicoGps = (Button) findViewById(R.id.btn_iniciar_servico_gps);

        configuracao = GPSLib.getConfiguracao();
        gpsServiceIntent = new Intent(this, GPSService.class);



    }

    private void insereRastreamentosAntigos()  {


        try {

            /***
             * Insere alguns registros para teste,
             */

            RastreamentoDaoSqlite dao = new RastreamentoDaoSqlite();

            Rastreamento rastreamento = dao.listarUltimaPosicao(RCA_TESTE);


            for (int i=0;i < 1000; i++  ){

                DateTime dtEnvio  = rastreamento.getDataHoraCaptura().plusHours(1);
                DateTime dtCaptura = rastreamento.getDataHoraCaptura();

                dtEnvio = dtEnvio.minusDays(i % 7);
                dtCaptura = dtCaptura.minusDays(i % 7);
                rastreamento.setHash(UUID.randomUUID().toString());
                rastreamento.setDataHoraCaptura(dtCaptura);
                rastreamento.setDataHoraEnvio(dtEnvio);

                dao.incluir(rastreamento);
            }

            Log.i(TAG, "teste");

            /***
             * Delete itens inseridos apartir de uma data especifica
             */

             dao.limparRastreamentosAntigosEnviados(RCA_TESTE, new DateTime().minusDays(3));



        }catch (PersistenciaExcecao e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }



    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGpsService != null) {
            unbindService(this);
        }
    }

    @NeedsPermission({Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION})
    public void iniciarServicoGps() {
        startService(gpsServiceIntent);
        bindService(gpsServiceIntent, this, Context.BIND_AUTO_CREATE);

        if (configuracao.isRastrearUsuario()) {
            btnRastrearUsuario.setText("Parar rastreamento");
        } else {
            btnRastrearUsuario.setText("Rastrear");
        }
    }

    public void iniciarServicoGps(View view) {
        if (!isMyServiceRunning(GPSService.class)) {
            MainActivityPermissionsDispatcher.iniciarServicoGpsWithCheck(this);
            btnIniciarServicoGps.setText("Parar serviço gps");
        } else {
            btnIniciarServicoGps.setText("Iniciar serviço gps");
            btnRastrearUsuario.setText("Rastrear");
            unbindService(this);
            stopService(gpsServiceIntent);
            mGpsService = null;
        }
    }

    public void rastrearUsuario(View view) {
        if (mGpsService != null) {
            if (configuracao.isRastrearUsuario()) {
                mGpsService.rastrearUsuario(false, null);
                btnRastrearUsuario.setText("Rastrear");
            } else {
                mGpsService.rastrearUsuario(true, RCA_TESTE);
                btnRastrearUsuario.setText("Parar rastreamento");
            }
        } else {
            Toast.makeText(this, "GPSService está nulo.", Toast.LENGTH_LONG).show();
        }


    }

    public void solicitarLocalizacao(View view) {
        if (mGpsService != null) {
            mGpsService.solicitarLocalizacao(this);
        } else {
            Toast.makeText(this, "GPSService está nulo.", Toast.LENGTH_LONG).show();
        }
    }

    public void consultarRastreamento(View view) {
        // O TRECHO DE CÓDIGO ABAIXO É SÓ UM EXEMPLO DE UTILIZAÇÃO
        // O IDEAL É FAZER A CONSULTA DENTRO DE UM THREAD SEPARADA DA THREAD DE MAIN
        try {
            List<Rastreamento> rastreamentos = new RastreamentoDaoSqlite().listar();
            Toast.makeText(this, "Qtd de rastreamentos: " + String.valueOf(rastreamentos.size()), Toast.LENGTH_LONG).show();
            rastreamentos = new RastreamentoDaoSqlite().listarNaoEnviadosUsuario(RCA_TESTE);
        } catch (PersistenciaExcecao ex) {
            Log.e(TAG, "consultarRastreamento - Detalhes: " + ex.getMessage());
        }
    }


    public void consultarUltimoRastreamento(View view) {
        // O TRECHO DE CÓDIGO ABAIXO É SÓ UM EXEMPLO DE UTILIZAÇÃO
        // O IDEAL É FAZER A CONSULTA DENTRO DE UM THREAD SEPARADA DA THREAD DE MAIN
        try {

            Rastreamento rastreamentos = new RastreamentoDaoSqlite().listarUltimaPosicao(RCA_TESTE);
            if (rastreamentos != null) {
                Toast.makeText(this, "Data Rastreamento: " + String.valueOf(rastreamentos.getDataHoraCaptura().toString()), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Rastreamento está nulo.", Toast.LENGTH_LONG).show();
            }


        } catch (PersistenciaExcecao ex) {
            Log.e(TAG, "consultarRastreamento - Detalhes: " + ex.getMessage());
        }
    }

    public void limparUltimosRastreamentos(View view){
        insereRastreamentosAntigos();
    }

    public void SalvarRastreamentoComoEnviado(View view) {
        // O TRECHO DE CÓDIGO ABAIXO É SÓ UM EXEMPLO DE UTILIZAÇÃO
        // O IDEAL É FAZER A CONSULTA DENTRO DE UM THREAD SEPARADA DA THREAD DE MAIN
        try {
            List<Rastreamento> rastreamentos = new RastreamentoDaoSqlite().listarNaoEnviadosUsuario(RCA_TESTE,1);

            for(Rastreamento rastreamento:rastreamentos)rastreamento.setDataHoraEnvio(new DateTime());

            new RastreamentoDaoSqlite().alterar(rastreamentos);
            Toast.makeText(this, "Qtd de rastreamentos: " + String.valueOf(rastreamentos.size()), Toast.LENGTH_LONG).show();

        } catch (PersistenciaExcecao ex) {
            Log.e(TAG, "consultarRastreamento - Detalhes: " + ex.getMessage());
        }
    }

    public void registrarEvento(View view) {
        try {
            Evento evento = new Evento();
            evento.setCodigoUsuario(RCA_TESTE);
            evento.setTipo(1);
            mGpsService.registrarEvento(evento);
            Toast.makeText(this, "Evento registrado com sucesso!", Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            Log.e(TAG, "registrarEvento - Detalhes: " + ex.getMessage());
        }
    }


    public void registrarRastreamento(View view) {
        try {

            RastreamentoDaoSqlite rastreamentoDaoSqlite = new RastreamentoDaoSqlite();
            Rastreamento rastreamento = rastreamentoDaoSqlite.listarUltimaPosicao(RCA_TESTE);
            rastreamento.setInformacoesAdicionais("teste ");
            try {
                rastreamentoDaoSqlite.incluir(rastreamento);
            } catch (PersistenciaExcecao persistenciaExcecao) {
                Log.e(TAG, "onHandleIntent: Detalhes: ", persistenciaExcecao);
            }

            Toast.makeText(this, "rastreamento registrado com sucesso!", Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            Log.e(TAG, "registrarEvento - Detalhes: " + ex.getMessage());
        }
    }

    public void consultarEvento(View view) {
        // O TRECHO DE CÓDIGO ABAIXO É SÓ UM EXEMPLO DE UTILIZAÇÃO
        // O IDEAL É FAZER A CONSULTA DENTRO DE UM THREAD SEPARADA DA THREAD DE MAIN
        try {
            List<Evento> eventos = new EventoDaoSqlite().listar();
            Toast.makeText(this, "Qtd de eventos: " + String.valueOf(eventos.size()), Toast.LENGTH_LONG).show();
        } catch (PersistenciaExcecao ex) {
            Log.e(TAG, "consultarRastreamento - Detalhes: " + ex.getMessage());
        }
    }

    public void SalvarEventoComoEnviado(View view) {
        // O TRECHO DE CÓDIGO ABAIXO É SÓ UM EXEMPLO DE UTILIZAÇÃO
        // O IDEAL É FAZER A CONSULTA DENTRO DE UM THREAD SEPARADA DA THREAD DE MAIN
        try {
            List<Evento> eventos = new EventoDaoSqlite().listarNaoEnviadosUsuario(RCA_TESTE,1);


            for(Evento evento:eventos)evento.setDataHoraEnvio(new DateTime());

            new EventoDaoSqlite().alterar(eventos);


        } catch (PersistenciaExcecao ex) {
            Log.e(TAG, "consultarRastreamento - Detalhes: " + ex.getMessage());
        }
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        if (GPSService.class.getName().equals(name.getClassName())) {
            mGpsService = (GPSService.IGPSService) service;
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

    }

    @Override
    public void onLocalizacaoCapturadaSucesso(Location localizacao) {
        Log.i(TAG, "Nova localização - " + localizacao.toString());
        Toast.makeText(this, "Nova localização - " + localizacao.toString(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onLocalizacaoCaputaradaGeocoding(List<Address> enderecos) {
        Log.i(TAG, "Geocoding Reverso");
        Toast.makeText(this, "Qtd de endereços: " + enderecos.size(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onLocalizacaoCapturadaErro() {

    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}

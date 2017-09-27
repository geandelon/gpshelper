package br.com.gdelon.lib.service;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Location;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.location.DetectedActivity;

import org.joda.time.DateTime;
import org.joda.time.Minutes;

import java.util.List;
import java.util.UUID;

import io.nlopez.smartlocation.OnActivityUpdatedListener;
import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.OnReverseGeocodingListener;
import io.nlopez.smartlocation.SmartLocation;
import io.nlopez.smartlocation.location.config.LocationParams;
import br.com.gdelon.lib.BuildConfig;
import br.com.gdelon.lib.arquitetura.Configuracao;
import br.com.gdelon.lib.arquitetura.GPSLib;
import br.com.gdelon.lib.arquitetura.excecao.ExcecaoApp;
import br.com.gdelon.lib.arquitetura.utilitario.Util;
import br.com.gdelon.lib.model.Evento;
import br.com.gdelon.lib.model.Rastreamento;
import br.com.gdelon.lib.model.persistencia.RastreamentoDaoSqlite;

/**
 * Created by geandelon on 09/06/2017.
 */
public class GPSService extends Service implements OnLocationUpdatedListener {

    private final String TAG = GPSService.class.getSimpleName();

    public static final String RASTREAMENTO = "rastreamento";

    public static final String EVENTO = "eventos";

    public static final int ID_NOTIFICATION_SERVICE = 1000;

    private final IBinder mBinder = new GPSServiceBinder();

    private SmartLocation mRastreioSmartLocation;

    private SmartLocation mMelhorLocalizacaoSmartLocation;

    private SmartLocation mEventoSmartLocation;

    private Rastreamento mUltimaRastreamento;

    private Context contexto;

    private NotificationManager mNotifyManager;

    private NotificationCompat.Builder mBuilder;

    private Configuracao configuracao;

    private String jsonInformacoesAdicionais;

    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) {
            Log.i(TAG, "onCreate");
        }
        configuracao = GPSLib.getConfiguracao();
        contexto = GPSLib.getContext();
        mRastreioSmartLocation = SmartLocation.with(GPSService.this);
        mMelhorLocalizacaoSmartLocation = SmartLocation.with(contexto);
        mEventoSmartLocation = SmartLocation.with(contexto);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        criarNotificacao();
        if (BuildConfig.DEBUG) {
            Log.i(TAG, "O serviço foi iniciado via startService");
        }
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        if (BuildConfig.DEBUG) {
            Log.i(TAG, "Um cliente vinculou ao serviço via bindService");
        }
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        // Todos os clientes desvincularam via unbindService
        return super.onUnbind(intent);
    }

    @Override
    public void onRebind(Intent intent) {
        // Um cliente foi vinculado ao serviço via bindService
        super.onRebind(intent);
    }

    @Override
    public void onDestroy() {
        // O serviço não está sendo usado e será destruído
        if (mRastreioSmartLocation != null) {
            mRastreioSmartLocation.location().stop();
        }

        if (mMelhorLocalizacaoSmartLocation != null) {
            mMelhorLocalizacaoSmartLocation.location().stop();
        }

        if (mEventoSmartLocation != null) {
            mEventoSmartLocation.location().stop();
        }

        stopForeground(true);

        super.onDestroy();
    }

    private void criarNotificacao() {
        mNotifyManager =
                (NotificationManager) contexto.getSystemService(Context.NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(contexto)
                .setSmallIcon(configuracao.getIconeNotificacao())
                .setPriority(NotificationCompat.PRIORITY_MIN)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setContentTitle(configuracao.getTituloNotificacao())
                .setContentText("Serviço ativo")
                .setWhen(System.currentTimeMillis());

        mNotifyManager.notify(
                ID_NOTIFICATION_SERVICE,
                mBuilder.build()
        );

        startForeground(ID_NOTIFICATION_SERVICE, mBuilder.build());
    }

    /**
     * Classe utilizada para fazer o Binder. Sabemos que o service sempre
     * roda no mesmo processo que seus clientes, logo não precisamos liddar
     * com chamadas IPC.
     */
    public class GPSServiceBinder extends Binder implements IGPSService {

        /**
         * Método responsável por iniciar ou parar o rastramento
         *
         * @param iniciar se true rastrea se false desliga o rastreio
         */
        @Override
        public void rastrearUsuario(boolean iniciar, String codigoUsuario) {
            if (iniciar) {
                iniciarRastreamento(codigoUsuario);
            } else {
                pararRastreamento();
            }
        }

        @Override
        public void solicitarLocalizacao(OnMelhorLocalizacaoCapturadaListener listener) {
            GPSService.this.solicitarLocalizacao(listener);
        }

        @Override
        public void registrarEvento(Evento evento) throws ExcecaoApp {
            GPSService.this.registrarEvento(evento);
        }

        @Override
        public void registrarRastreamento(Rastreamento rastreamento) throws ExcecaoApp {
            GPSService.this.registrarRastreamento(rastreamento);
        }

        @Override
        public void setDadosAdicionaisRastreamento(String json) {
            jsonInformacoesAdicionais = json;
        }

    }

    public interface OnMelhorLocalizacaoCapturadaListener {

        void onLocalizacaoCapturadaSucesso(Location localizacao);

        void onLocalizacaoCaputaradaGeocoding(List<Address> enderecos);

        void onLocalizacaoCapturadaErro();

    }

    public interface IGPSService {

        void rastrearUsuario(boolean iniciar, String codigoUsuario);

        void solicitarLocalizacao(final OnMelhorLocalizacaoCapturadaListener listener);

        void registrarEvento(Evento evento) throws ExcecaoApp;

        void registrarRastreamento(Rastreamento rastreamento) throws ExcecaoApp;

        void setDadosAdicionaisRastreamento(String json);

    }

    /////////////////////////////////////////////////////////////////
    //////////////// MÉTODOS CHAMADOS PELO CLIENTE //////////////////
    ////////////////////////////////////////////////////////////////
    /**
     * Método responsável por obter a localização atual do cliente
     */
    private void solicitarLocalizacao(final OnMelhorLocalizacaoCapturadaListener listener) {
        if (BuildConfig.DEBUG) {
            Log.i(TAG, "Solicitando uma coordenada");
        }

        if (mMelhorLocalizacaoSmartLocation != null) {
            mMelhorLocalizacaoSmartLocation.location().stop();
        }

        mMelhorLocalizacaoSmartLocation = SmartLocation.with(contexto);
        mMelhorLocalizacaoSmartLocation.location()
                .config(LocationParams.NAVIGATION)
                .continuous()
                .start(new OnLocationUpdatedListener() {
                    @Override
                    public void onLocationUpdated(Location location) {
                        // Capturou uma coordenada que está aderente aos parâmetros informados
                        mMelhorLocalizacaoSmartLocation.location().stop();
                        if (listener == null) {
                            return;
                        }

                        listener.onLocalizacaoCapturadaSucesso(location);

                        if (Util.isConectadoInternet()) {
                            mMelhorLocalizacaoSmartLocation.geocoding().reverse(location, new OnReverseGeocodingListener() {
                                @Override
                                public void onAddressResolved(Location location, List<Address> enderecos) {
                                    mMelhorLocalizacaoSmartLocation.geocoding().stop();

                                    if (enderecos != null && !enderecos.isEmpty()) {
                                        listener.onLocalizacaoCaputaradaGeocoding(enderecos);
                                    }
                                }
                            });
                        }
                    }
                });
    }

    /**
     * Método responsável por iniciar o rastreamento que utiliza a smartlocation
     *
     * @param codigoUsuario
     */
    private void iniciarRastreamento(@NonNull String codigoUsuario) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "Iniciar rastreamento");
        }

        if (mRastreioSmartLocation != null) {
            mRastreioSmartLocation.location().stop();

            mRastreioSmartLocation.location()
                    .config(LocationParams.NAVIGATION)
                    .continuous()
                    .start(this);

            mRastreioSmartLocation.activity().start(new OnActivityUpdatedListener() {
                @Override
                public void onActivityUpdated(DetectedActivity detectedActivity) {
                    Log.d(TAG, "Atividade identificada: " + getNameFromType(detectedActivity));
                }
            });

            configuracao.setRastrearUsuario(true);
            configuracao.setCodigoUsuario(codigoUsuario);
        }
    }

    /**
     * Método responsável por parar o rastreamento que utiliza a smartlocation
     */
    private void pararRastreamento() {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "Parar rastreamento");
        }

        if (mRastreioSmartLocation != null) {
            mRastreioSmartLocation.location().stop();
        }

        configuracao.setRastrearUsuario(false);
    }

    /**
     * Método responsável por processar as coordenadas provenientes do rastramento
     */
    private void processarRastreamento(Location localizacaoAtual) {
        Location ultimaLocation = new Location("ultimaLocation");

        if (mUltimaRastreamento != null) {
            ultimaLocation.setLatitude(mUltimaRastreamento.getLatitude());
            ultimaLocation.setLongitude(mUltimaRastreamento.getLongitude());
        }

        if (localizacaoAtual.getAccuracy() <= 50.0) {
            float[] dist = Util.calcularDistancia(ultimaLocation, localizacaoAtual);

            Rastreamento rastreamentoAtual = new Rastreamento(localizacaoAtual, configuracao.getCodigoUsuario(), jsonInformacoesAdicionais);

            // É a mesma coordenada? Tem mais de 15 minutos?
            // Deixa gravar para não dar a impressão que o promotor não está sendo monitorado
            if (mUltimaRastreamento != null && dist[0] <= 5.0) {
                final boolean temMaisDe5Minutos = Minutes.minutesBetween(mUltimaRastreamento.getDataHoraCaptura(), rastreamentoAtual.getDataHoraCaptura())
                        .isGreaterThan(Minutes.minutes(15));
                if (!temMaisDe5Minutos) {
                    return;
                }
            }
            rastreamentoAtual.setDistancia(dist[0]);
            Intent it = new Intent(contexto, SalvaRastreamentoService.class);
            it.putExtra(RASTREAMENTO, rastreamentoAtual);
            contexto.startService(it);

            mUltimaRastreamento = rastreamentoAtual;
            if (BuildConfig.DEBUG) {
                Log.d(TAG, "processarRastreamento - Nova localização" + localizacaoAtual.toString());
            }
        }
    }

    private void registrarEvento(final Evento evento) throws ExcecaoApp {
        if (BuildConfig.DEBUG) {
            Log.i(TAG, "Registrando um Evento");
        }

        if (TextUtils.isEmpty(evento.getCodigoUsuario())) {
            throw new ExcecaoApp("Impossível registrar o evento. Código de usuário não informado.");
        }

        if (evento.getTipo() <= 0) {
            throw new ExcecaoApp("Impossível registrar o evento. Tipo de evento não informado.");
        }

        //verifica se a ultima posição coletada tem mais de 30 segundos
        Rastreamento rastreamentos = new RastreamentoDaoSqlite().listarUltimaPosicao(evento.getCodigoUsuario());
        if (rastreamentos != null && ((new DateTime().getMillis()) - (rastreamentos.getDataHoraCaptura().getMillis()) <= 1000 * 30)) {
            evento.setHash(UUID.randomUUID().toString());
            evento.setLatitude(rastreamentos.getLatitude());
            evento.setLongitude(rastreamentos.getLongitude());
            evento.setDataHoraCaptura(new DateTime());
            Intent it = new Intent(contexto, SalvaEventoService.class);
            it.putExtra(EVENTO, evento);
            contexto.startService(it);
        } else {

            mEventoSmartLocation.location()
                    .config(LocationParams.NAVIGATION)
                    .continuous()
                    .start(new OnLocationUpdatedListener() {
                        @Override
                        public void onLocationUpdated(Location location) {
                            // Capturou uma coordenada que está aderente aos parâmetros informados
                            mEventoSmartLocation.location().stop();

                            evento.setHash(UUID.randomUUID().toString());
                            evento.setLatitude(location.getLatitude());
                            evento.setLongitude(location.getLongitude());
                            evento.setDataHoraCaptura(new DateTime());

                            Intent it = new Intent(contexto, SalvaEventoService.class);
                            it.putExtra(EVENTO, evento);
                            contexto.startService(it);
                        }
                    });
        }
    }

    private void registrarRastreamento(final Rastreamento rastreamento) throws ExcecaoApp {
        if (BuildConfig.DEBUG) {
            Log.i(TAG, "Registrando um Evento");
        }

        if (TextUtils.isEmpty(rastreamento.getCodigoUsuario())) {
            throw new ExcecaoApp("Impossível Rastreamento o evento. Código de usuário não informado.");
        }

        rastreamento.setId(0);
        rastreamento.setHash(UUID.randomUUID().toString());
        rastreamento.setDataHoraCaptura(new DateTime());
        Intent it = new Intent(contexto, SalvaRastreamentoService.class);
        it.putExtra(RASTREAMENTO, rastreamento);
        contexto.startService(it);
    }

    @Override
    public void onLocationUpdated(Location location) {
        processarRastreamento(location);
    }

    private String getNameFromType(DetectedActivity activityType) {
        switch (activityType.getType()) {
            case DetectedActivity.IN_VEHICLE:
                return "No veículo";
            case DetectedActivity.ON_BICYCLE:
                return "Na bicicleta";
            case DetectedActivity.ON_FOOT:
                return "A pé";
            case DetectedActivity.STILL:
                return "still";
            case DetectedActivity.TILTING:
                return "tilting";
            default:
                return "unknown";
        }
    }
}

package br.com.gdelon.lib.model.persistencia;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

import br.com.gdelon.lib.arquitetura.excecao.PersistenciaExcecao;
import br.com.gdelon.lib.arquitetura.persistencia.BaseMap;
import br.com.gdelon.lib.arquitetura.persistencia.PersistenciaSqlite;
import br.com.gdelon.lib.arquitetura.transform.TransformerInterface;
import br.com.gdelon.lib.arquitetura.utilitario.UtilData;
import br.com.gdelon.lib.model.BaseModel;
import br.com.gdelon.lib.model.Evento;
import br.com.gdelon.lib.model.transform.EventoTransformer;

/**
 * Created by geandelon on 12/06/2017.
 */

public class EventoDaoSqlite extends PersistenciaSqlite<Evento> implements EventoDao {
    private static String TAG = EventoDaoSqlite.class.getSimpleName();
    public static final int ORDER_ASC = 0;
    public static final int ORDER_DESC = 1;

    @Override
    public BaseModel getEntidade() {
        return new Evento();
    }

    @Override
    public TransformerInterface getObjetoTransformer() {
        return new EventoTransformer();
    }

    @Override
    public ContentValues getValores(Evento evento) {
        ContentValues valores = new ContentValues();
        valores.put(BaseMap.CODIGO_USUARIO_MAP, evento.getCodigoUsuario());
        valores.put(BaseMap.TIPO_MAP, evento.getTipo());
        valores.put(BaseMap.HASH_MAP, evento.getHash());
        valores.put(BaseMap.LATITUDE_MAP, evento.getLatitude());
        valores.put(BaseMap.LONGITUDE_MAP, evento.getLongitude());
        valores.put(BaseMap.INFORMACOES_ADICIONAIS_MAP, evento.getInformacoesAdicionais());
        valores.put(BaseMap.DATA_HORA_CAPTURA_MAP, UtilData.getDataSqlite(evento.getDataHoraCaptura()));
        valores.put(BaseMap.DATA_HORA_ENVIO_MAP, UtilData.getDataSqlite(evento.getDataHoraEnvio()));
        return valores;
    }

    @Override
    public List<Evento> listarNaoEnviadosUsuario(String codigoUsuario) throws PersistenciaExcecao {
        ArrayList<Evento> lista = new ArrayList<>();
        Cursor cursor = null;

        try {
            String argumentos[] = new String[]{codigoUsuario};
            SQLiteDatabase database = databaseManager.abrirDatabase();
            cursor = database.rawQuery(Query.Evento.SELECT_NAO_ENVIADOS_DO_USUARIO, argumentos);
            if (cursor.moveToFirst()) {
                do {
                    lista.add((Evento) getObjetoTransformer().toModelo(cursor));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            throw new PersistenciaExcecao(e.getMessage(), e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            databaseManager.fecharDatabase();
        }
        return lista;

    }


    @Override
    public List<Evento> listarNaoEnviadosUsuario(String codigoUsuario, int limit) throws PersistenciaExcecao {
        ArrayList<Evento> lista = new ArrayList<>();
        Cursor cursor = null;

        try {
            String argumentos[] = new String[]{codigoUsuario, String.valueOf(limit)};
            SQLiteDatabase database = databaseManager.abrirDatabase();
            cursor = database.rawQuery(Query.Evento.SELECT_NAO_ENVIADOS_DO_USUARIO_LIMIT, argumentos);
            if (cursor.moveToFirst()) {
                do {
                    lista.add((Evento) getObjetoTransformer().toModelo(cursor));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            throw new PersistenciaExcecao(e.getMessage(), e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            databaseManager.fecharDatabase();
        }
        return lista;

    }

    @Override
    public List<Evento> listarNaoEnviadosUsuario(String codigoUsuario, int limit, int orderType) throws PersistenciaExcecao {
        ArrayList<Evento> lista = new ArrayList<>();
        Cursor cursor = null;

        try {
            String argumentos[] = new String[]{codigoUsuario, String.valueOf(limit)};
            SQLiteDatabase database = databaseManager.abrirDatabase();
            if (orderType == ORDER_ASC) {
                cursor = database.rawQuery(Query.Evento.SELECT_NAO_ENVIADOS_DO_USUARIO_LIMIT_ORDER_ASC, argumentos);
            } else {
                cursor = database.rawQuery(Query.Evento.SELECT_NAO_ENVIADOS_DO_USUARIO_LIMIT, argumentos);
            }
            if (cursor.moveToFirst()) {
                do {
                    lista.add((Evento) getObjetoTransformer().toModelo(cursor));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            throw new PersistenciaExcecao(e.getMessage(), e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            databaseManager.fecharDatabase();
        }
        return lista;
    }

    /***
     * Ira deletar Registros apartir de uma data passa por parametro
     *
     * @param codigoUsuario
     * @param dataLimite
     * @return
     * @throws PersistenciaExcecao
     */
    @Override
    public int limparEventosAntigosEnviados(String codigoUsuario, DateTime dataLimite) throws PersistenciaExcecao {
        String argumentos[] = new String[]{codigoUsuario, UtilData.getDataSqlite(dataLimite)};
        int retorno = 0;
        try {
            SQLiteDatabase database = databaseManager.abrirDatabase();
            retorno = database.delete(Evento.TABELA, Query.Evento.DELETE_WHERE_CLAUSE_EVENTOS_ANTIGOS_ENVIADOS, argumentos);

        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            throw new PersistenciaExcecao(e.getMessage(), e);
        } finally {
            databaseManager.fecharDatabase();
        }
        return retorno;
    }


}

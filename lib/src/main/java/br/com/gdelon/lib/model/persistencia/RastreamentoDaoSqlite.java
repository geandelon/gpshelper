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
import br.com.gdelon.lib.model.Rastreamento;
import br.com.gdelon.lib.model.transform.RastreamentoTransformer;

/**
 * Created by geandelon on 12/06/2017.
 */

public class RastreamentoDaoSqlite extends PersistenciaSqlite<Rastreamento> implements RastreamentoDao {

    private static String TAG = RastreamentoDaoSqlite.class.getSimpleName();
    public static final int ORDER_ASC = 0;
    public static final int ORDER_DESC = 1;

    @Override
    public BaseModel getEntidade() {
        return new Rastreamento();
    }

    @Override
    public TransformerInterface getObjetoTransformer() {
        return new RastreamentoTransformer();
    }

    @Override
    public ContentValues getValores(Rastreamento entidade) {
        ContentValues valores = new ContentValues();
        valores.put(BaseMap.CODIGO_USUARIO_MAP, entidade.getCodigoUsuario());
        valores.put(BaseMap.HASH_MAP, entidade.getHash());
        valores.put(BaseMap.LATITUDE_MAP, entidade.getLatitude());
        valores.put(BaseMap.LONGITUDE_MAP, entidade.getLongitude());
        valores.put(BaseMap.VELOCIDADE_MAP, entidade.getVelocidade());
        valores.put(BaseMap.ACURACIA_MAP, entidade.getAcuracia());
        valores.put(BaseMap.DATA_HORA_CAPTURA_MAP, UtilData.getDataSqlite(entidade.getDataHoraCaptura()));
        valores.put(BaseMap.DATA_HORA_ENVIO_MAP, UtilData.getDataSqlite(entidade.getDataHoraEnvio()));
        valores.put(BaseMap.DISTANCIA_MAP, entidade.getDistancia());
        valores.put(BaseMap.INFORMACOES_ADICIONAIS_MAP, entidade.getInformacoesAdicionais());

        return valores;
    }

    @Override
    public List<Rastreamento> listarNaoEnviadosUsuario(String codigoUsuario) throws PersistenciaExcecao {
        ArrayList<Rastreamento> lista = new ArrayList<>();
        Cursor cursor = null;

        try {
            String argumentos[] = new String[] {codigoUsuario};
            SQLiteDatabase database = databaseManager.abrirDatabase();
            cursor = database.rawQuery(Query.Rastreamento.SELECT_NAO_ENVIADOS_DO_USUARIO, argumentos);
            if (cursor.moveToFirst()) {
                do {
                    lista.add((Rastreamento) getObjetoTransformer().toModelo(cursor));
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
    public List<Rastreamento> listarNaoEnviadosUsuario(String codigoUsuario,int limit) throws PersistenciaExcecao {
        ArrayList<Rastreamento> lista = new ArrayList<>();
        Cursor cursor = null;

        try {
            String argumentos[] = new String[] {codigoUsuario, String.valueOf(limit)};
            SQLiteDatabase database = databaseManager.abrirDatabase();
            cursor = database.rawQuery(Query.Rastreamento.SELECT_NAO_ENVIADOS_DO_USUARIO_LIMIT, argumentos);
            if (cursor.moveToFirst()) {
                do {
                    lista.add((Rastreamento) getObjetoTransformer().toModelo(cursor));
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
    public List<Rastreamento> listarNaoEnviadosUsuario(String codigoUsuario, int limit, int orderType) throws PersistenciaExcecao {
        ArrayList<Rastreamento> lista = new ArrayList<>();
        Cursor cursor = null;

        try {
            String argumentos[] = new String[]{codigoUsuario, String.valueOf(limit)};
            SQLiteDatabase database = databaseManager.abrirDatabase();
            if (orderType == ORDER_ASC) {
                cursor = database.rawQuery(Query.Rastreamento.SELECT_NAO_ENVIADOS_DO_USUARIO_LIMIT_ORDER_ASC, argumentos);
            } else {
                cursor = database.rawQuery(Query.Rastreamento.SELECT_NAO_ENVIADOS_DO_USUARIO_LIMIT, argumentos);
            }
            if (cursor.moveToFirst()) {
                do {
                    lista.add((Rastreamento) getObjetoTransformer().toModelo(cursor));
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
    public Rastreamento listarUltimaPosicao(String codigoUsuario) throws PersistenciaExcecao {
        Rastreamento lista =null;
        Cursor cursor = null;

        try {
            String argumentos[] = new String[]{codigoUsuario, String.valueOf(Integer.MAX_VALUE)};
            SQLiteDatabase database = databaseManager.abrirDatabase();

                cursor = database.rawQuery(Query.Rastreamento.SELECT_NAO_ENVIADOS_DO_USUARIO_LIMIT_ORDER_ASC, argumentos);

            if (cursor.moveToFirst()) {
                do {
                    lista=((Rastreamento) getObjetoTransformer().toModelo(cursor));
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
    public List<Rastreamento> listarUltimasPosicoes(String codigoUsuario,int limit) throws PersistenciaExcecao {
        List<Rastreamento> lista = new ArrayList<>();
        Cursor cursor = null;

        try {
            String argumentos[] = new String[] {codigoUsuario, String.valueOf(limit)};
            SQLiteDatabase database = databaseManager.abrirDatabase();
            cursor = database.rawQuery(Query.Rastreamento.SELECT_ULTIMAS_POSICOES, argumentos);
            if (cursor.moveToFirst()) {
                do {
                    lista.add((Rastreamento) getObjetoTransformer().toModelo(cursor));
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
    public List<Rastreamento> listarUltimasPosicoesEnviadas(String codigoUsuario,int limit) throws PersistenciaExcecao {
        List<Rastreamento> lista = new ArrayList<>();
        Cursor cursor = null;

        try {
            String argumentos[] = new String[] {codigoUsuario, String.valueOf(limit)};
            SQLiteDatabase database = databaseManager.abrirDatabase();
            cursor = database.rawQuery(Query.Rastreamento.SELECT_ULTIMAS_POSICOES_ENVIADAS, argumentos);
            if (cursor.moveToFirst()) {
                do {
                    lista.add((Rastreamento) getObjetoTransformer().toModelo(cursor));
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
     * Ira reletar Registris apartir de uma data passa por parametro
     * @param codigoUsuario
     * @param dataLimite
     * @return retorna a quantidade de registros deletados
     * @throws PersistenciaExcecao
     */
    @Override
    public int limparRastreamentosAntigosEnviados(String codigoUsuario, DateTime dataLimite) throws PersistenciaExcecao {
        String argumentos[] = new String[] {codigoUsuario, UtilData.getDataSqlite(dataLimite) };
        int retorno = 0;
        try {
            SQLiteDatabase database = databaseManager.abrirDatabase();
            retorno = database.delete(Rastreamento.TABELA, Query.Rastreamento.DELETE_WHERE_CLAUSE_RASTREAMENTO_ANTIGOS_ENVIADOS, argumentos );

        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            throw new PersistenciaExcecao(e.getMessage(), e);
        } finally {
            databaseManager.fecharDatabase();
        }
        return retorno;
    }
}

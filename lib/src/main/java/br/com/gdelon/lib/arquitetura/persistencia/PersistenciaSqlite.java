package br.com.gdelon.lib.arquitetura.persistencia;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import br.com.gdelon.lib.arquitetura.db.DatabaseManager;
import br.com.gdelon.lib.arquitetura.excecao.PersistenciaExcecao;
import br.com.gdelon.lib.model.BaseModel;
import br.com.gdelon.lib.arquitetura.transform.TransformerInterface;

/**
 * @author geandelon
 */
public abstract class PersistenciaSqlite<E extends BaseModel> implements Persistencia<E> {

    private static final String TAG = PersistenciaSqlite.class.getSimpleName();

    protected DatabaseManager databaseManager;

    public abstract BaseModel getEntidade();

    public abstract TransformerInterface getObjetoTransformer();

    public PersistenciaSqlite() {
        databaseManager = DatabaseManager.getInstancia();
    }

    @Override
    public void incluir(E entidade) throws PersistenciaExcecao {
        try {
            SQLiteDatabase database = databaseManager.abrirDatabase();
            long id = database.insertOrThrow(getEntidade().getNomeTabela(), null, getValores(entidade));
            entidade.setId((int) id);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            throw new PersistenciaExcecao(e.getMessage(), e);
        } finally {
            if (databaseManager != null) {
                databaseManager.fecharDatabase();
            }
        }
    }

    @Override
    public boolean alterar(E entidade) throws PersistenciaExcecao {
        try {
            SQLiteDatabase database = databaseManager.abrirDatabase();
            String where = BaseMap.ID_MAP + " = ?";
            String argumentos[] = new String[]{String.valueOf(entidade.getId())};
            return database.update(
                    getEntidade().getNomeTabela(),
                    getValores(entidade),
                    where,
                    argumentos
            ) > 0;
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            throw new PersistenciaExcecao(e.getMessage(), e);
        } finally {
            if (databaseManager != null) {
                databaseManager.fecharDatabase();
            }
        }
    }

    @Override
    public void alterar(List<E> entidades) throws PersistenciaExcecao {

        SQLiteDatabase database = null;
        try {
            database = databaseManager.abrirDatabase();
            database.beginTransaction();

            for (E entidade : entidades) {

                String where = BaseMap.ID_MAP + " = ?";
                String argumentos[] = new String[]{String.valueOf(entidade.getId())};
                 database.update(
                        getEntidade().getNomeTabela(),
                        getValores(entidade),
                        where,
                        argumentos
                );
            }

            database.setTransactionSuccessful();

        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            throw new PersistenciaExcecao(e.getMessage(), e);
        } finally {

            if(database != null){
                database.endTransaction();
            }

            if (databaseManager != null) {
                databaseManager.fecharDatabase();
            }
        }


    }

    @Override
    public boolean excluirPorId(Object id) throws PersistenciaExcecao {
        SQLiteDatabase database = databaseManager.abrirDatabase();
        String where = BaseMap.ID_MAP + " = ?";
        String argumentos[] = new String[]{String.valueOf(id)};
        return database.delete(getEntidade().getNomeTabela(), where, argumentos) > 0;
    }

    @Override
    public boolean excluir() throws PersistenciaExcecao {
        SQLiteDatabase database = databaseManager.abrirDatabase();
        return database.delete(getEntidade().getNomeTabela(), null, null) > 0;
    }

    @Override
    public List<E> listar() throws PersistenciaExcecao {
        ArrayList<E> lista = new ArrayList<>();
        Cursor cursor = null;
        try {
            SQLiteDatabase database = databaseManager.abrirDatabase();
            cursor = database.query(
                    getEntidade().getNomeTabela(),
                    null,
                    null,
                    null,
                    null,
                    null,
                    null
            );

            if (cursor.moveToFirst()) {
                do {
                    lista.add((E) getObjetoTransformer().toModelo(cursor));
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
    public E consultar(Object codigo) throws PersistenciaExcecao {
        return null;
    }

    @Override
    public E consultarPorId(int id) throws PersistenciaExcecao {
        return null;
    }

    @Override
    public void incluirAlterarBulk(List<E> entidades) throws PersistenciaExcecao {
    }

    @Override
    public ContentValues getValores(E entidade) {
        return null;
    }

    @Override
    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    @Override
    public List<Integer> getListaIds(List<E> valores) {
        List<Integer> ids = new ArrayList<>();
        for (E entidade : valores) {
            ids.add(entidade.getId());
        }
        return ids;
    }

    @Override
    public void excluirPorIn(List<E> valores, boolean usaNOT) throws PersistenciaExcecao {
        try {
            SQLiteDatabase database = databaseManager.abrirDatabase();
            String where = " WHERE " + BaseMap.ID_MAP + (usaNOT ? " NOT " : "") + " IN ( %s );";
            String args = TextUtils.join(", ", getListaIds(valores));
            String query = "DELETE FROM " + getEntidade().getNomeTabela() + where;
            database.execSQL(String.format(query, args));
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            throw new PersistenciaExcecao(e.getMessage(), e);
        } finally {
            if (databaseManager != null) {
                databaseManager.fecharDatabase();
            }
        }
    }

    @Override
    public List<E> listarNaoEnviadas() throws PersistenciaExcecao {
        ArrayList<E> lista = new ArrayList<>();
        Cursor cursor = null;
        try {
            SQLiteDatabase database = databaseManager.abrirDatabase();
            cursor = database.query(
                    getEntidade().getNomeTabela()
                    , null
                    , BaseMap.ENVIADA_MAP + " = 0"
                    , null
                    , null
                    , null
                    , null
            );
            if (cursor.moveToFirst()) {
                do {
                    lista.add((E) getObjetoTransformer().toModelo(cursor));
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
    public boolean atualizarEnviada(String coluna, String hash) throws PersistenciaExcecao {
        try {
            SQLiteDatabase database = databaseManager.abrirDatabase();
            String where = coluna + " = ?";
            String argumentos[] = new String[]{hash};
            ContentValues valores = new ContentValues();
            valores.put(BaseMap.ENVIADA_MAP, true);
            return database.update(
                    getEntidade().getNomeTabela(),
                    valores,
                    where,
                    argumentos
            ) > 0;
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            throw new PersistenciaExcecao(e.getMessage(), e);
        } finally {
            if (databaseManager != null) {
                databaseManager.fecharDatabase();
            }
        }
    }

    @Override
    public int getQuantidade() throws PersistenciaExcecao {
        Cursor cursor = null;
        try {
            SQLiteDatabase database = databaseManager.abrirDatabase();
            cursor = database.query(
                    getEntidade().getNomeTabela(),
                    null,
                    null,
                    null,
                    null,
                    null,
                    null
            );
            return cursor.getCount();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            throw new PersistenciaExcecao(e.getMessage(), e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            databaseManager.fecharDatabase();
        }
    }
}

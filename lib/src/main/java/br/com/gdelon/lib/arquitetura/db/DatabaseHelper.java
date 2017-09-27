package br.com.gdelon.lib.arquitetura.db;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import android.util.Log;

import br.com.gdelon.lib.R;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int VERSAO_BD_1_0_0 = 1;
    private static final int VERSAO_BD_2_0_0 = 2;
    private static final int VERSAO_BD_3_0_0 = 3;
    private static final int VERSAO_BD_5_0_0 = 5;
    private static final int VERSAO_BD_6_0_0 = 6;

    private static final int VERSAO_BD = 6;
    private static final String NOME_BD = "GPSLIB.db";
    private static final String LOG_TAG = DatabaseHelper.class.getSimpleName();





    private Context contexto;

    private final String LISTAR_TODAS_TABELAS = "SELECT name FROM sqlite_master WHERE type='table';";
    private final String DROP_TABLE = "DROP TABLE IF EXISTS ";

    public DatabaseHelper(Context contexto) {
        super(contexto, NOME_BD, null, VERSAO_BD);
        this.contexto = contexto;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        executarTransacaoSQL(db, contexto.getString(R.string.bd_ultima_versao).split(";"));
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.execSQL("PRAGMA auto_vacuum = FULL");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int versaoAntiga, int novaVersao) {
        Log.w(LOG_TAG, "Atualizando a base de dados da versão " + versaoAntiga + " para " + novaVersao);

        String sql = "";

        for (int i = versaoAntiga; i <= novaVersao; i++) {
            sql = sql.concat(getSqlUpgrade(i));
        }

        if (!TextUtils.isEmpty(sql)) {
            String[] sqls = sql.concat(contexto.getString(R.string.bd_ultima_versao)).split(";");
            executarTransacaoSQL(db, sqls);
        }
    }

    private void executarTransacaoSQL(SQLiteDatabase db, String[] sql) {
        try {
            db.beginTransaction();
            executarComandosSQL(db, sql);
            db.setTransactionSuccessful();
        } catch (SQLException ex) {
            Log.e(LOG_TAG, "Erro ao executar o script. ", ex);
        } finally {
            db.endTransaction();
        }
    }

    /**
     * Executa todos os comandos SQL passados no vetor String[]
     *
     * @param db  A base de dados onde os comandos serão executados
     * @param sql Um vetor de comandos SQL a serem executados
     */
    private void executarComandosSQL(SQLiteDatabase db, String[] sql) {
        for (String s : sql) {
            try {
                if (s.trim().length() > 0) {
                    db.execSQL(s);
                }
            } catch (SQLException ex) {
                Log.e(LOG_TAG, "Erro ao criar a tabela. ", ex);
            }
        }
    }


    private String getSqlUpgrade(int versaoAntiga) {
        String sql = "";
        switch (versaoAntiga) {
            case VERSAO_BD_1_0_0:
                sql = contexto.getString(R.string.bd_1_0_0_para_2_0_0);
                break;
            case VERSAO_BD_2_0_0:
                sql = contexto.getString(R.string.bd_2_0_0_para_3_0_0);
                break;
            case VERSAO_BD_3_0_0:
                sql = contexto.getString(R.string.bd_2_0_0_para_3_0_0);
                break;
            case VERSAO_BD_5_0_0:
                sql = contexto.getString(R.string.bd_4_0_0_para_5_0_0);
                break;
            case VERSAO_BD_6_0_0:
                sql = contexto.getString(R.string.bd_4_0_0_para_5_0_0);
                break;
            default:
                break;
        }
        return sql;
    }

    public void limparBaseDeDados() {

        SQLiteDatabase database = getWritableDatabase();

        Cursor cursor = database.rawQuery(LISTAR_TODAS_TABELAS, null);

        cursor.moveToFirst();

        do {

            String nomeTabela = cursor.getString(cursor.getColumnIndex("name"));
            if (nomeTabela.contains("PROM")) {
                database.execSQL(DROP_TABLE + nomeTabela);
            }

        } while (cursor.moveToNext());

        database.close();
        database = getWritableDatabase();

        String[] sql = contexto.getString(R.string.bd_ultima_versao).split(";");


        for (String tabela : sql) {
            database.execSQL(tabela);
        }
        cursor.close();
        database.close();

    }

}

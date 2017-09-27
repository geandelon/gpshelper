package br.com.gdelon.lib.arquitetura.db;

import android.database.sqlite.SQLiteDatabase;

import java.util.concurrent.atomic.AtomicInteger;

public class DatabaseManager {

	private static DatabaseManager instancia;
	private static DatabaseHelper mDatabaseHelper;

	private SQLiteDatabase mDatabase;
	// Lidando com concorrência com a classe AtomicInteger
	private AtomicInteger mOpenCounter = new AtomicInteger();

	public static synchronized void inicializarInstancia(DatabaseHelper helper) {
		if (instancia == null) {
			instancia = new DatabaseManager();
			mDatabaseHelper = helper;
		}
	}

	public static synchronized DatabaseManager getInstancia() {
		if (instancia == null) {
			throw new IllegalStateException(
					DatabaseManager.class.getSimpleName()
							+ " não foi inicializado, chame o método inicializarInstancia(..) primeiro.");
		}

		return instancia;
	}

	public synchronized SQLiteDatabase abrirDatabase() {
		if (mOpenCounter.incrementAndGet() == 1) {
			// Opening new database
			mDatabase = mDatabaseHelper.getWritableDatabase();
			//mDatabase.setForeignKeyConstraintsEnabled(false);
			//mDatabase.execSQL("PRAGMA foreign_key=OFF;");
		}
		return mDatabase;
	}

	public synchronized void fecharDatabase() {
		if (mOpenCounter.decrementAndGet() == 0) {
			// Closing database
			if (mDatabase != null) {
				mDatabase.close();
			}
		}
	}
}

package br.com.gdelon.lib.arquitetura.persistencia;

import android.content.ContentValues;

import java.util.List;

import br.com.gdelon.lib.arquitetura.db.DatabaseManager;
import br.com.gdelon.lib.arquitetura.excecao.PersistenciaExcecao;

public interface Persistencia<E> {
	
	void incluir(E entidade) throws PersistenciaExcecao;

	boolean alterar(E entidade) throws PersistenciaExcecao;

	void alterar(List<E> entidade) throws PersistenciaExcecao;
	
	boolean excluirPorId(Object id) throws PersistenciaExcecao;
	
	List<E> listar() throws PersistenciaExcecao;
	
	E consultar(Object codigo) throws PersistenciaExcecao;

	E consultarPorId(int id) throws PersistenciaExcecao;

	void incluirAlterarBulk(List<E> entidades) throws PersistenciaExcecao;

	ContentValues getValores(E entidade);

	List<Integer> getListaIds(List<E> valores);

	void excluirPorIn(List<E> valores, boolean usaNOT) throws PersistenciaExcecao;

	boolean excluir() throws PersistenciaExcecao;

	List<E> listarNaoEnviadas() throws PersistenciaExcecao;

	boolean atualizarEnviada(String coluna, String hash) throws PersistenciaExcecao;

	int getQuantidade() throws PersistenciaExcecao;

	DatabaseManager getDatabaseManager();
}

package br.com.gdelon.lib.model.persistencia;

import org.joda.time.DateTime;

import java.util.List;

import br.com.gdelon.lib.arquitetura.excecao.PersistenciaExcecao;
import br.com.gdelon.lib.arquitetura.persistencia.Persistencia;
import br.com.gdelon.lib.model.Rastreamento;

/**
 * Created by geandelon on 12/06/2017.
 */

public interface RastreamentoDao extends Persistencia<Rastreamento> {

    List<Rastreamento> listarNaoEnviadosUsuario(String codigoUsuario) throws PersistenciaExcecao;

    List<Rastreamento> listarNaoEnviadosUsuario(String codigoUsuario,int limit) throws PersistenciaExcecao;

    List<Rastreamento> listarNaoEnviadosUsuario(String codigoUsuario, int limit, int orderType)throws PersistenciaExcecao;

    Rastreamento listarUltimaPosicao(String codigoUsuario) throws PersistenciaExcecao;

    List<Rastreamento> listarUltimasPosicoes(String codigoUsuario,int limit) throws PersistenciaExcecao;

    List<Rastreamento> listarUltimasPosicoesEnviadas(String codigoUsuario,int limit) throws PersistenciaExcecao;

    int limparRastreamentosAntigosEnviados(String codigoUsuario, DateTime dataLimite) throws PersistenciaExcecao;

}

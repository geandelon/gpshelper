package br.com.gdelon.lib.model.persistencia;

import org.joda.time.DateTime;

import java.util.List;

import br.com.gdelon.lib.arquitetura.excecao.PersistenciaExcecao;
import br.com.gdelon.lib.arquitetura.persistencia.Persistencia;
import br.com.gdelon.lib.model.Evento;

/**
 * Created by geandelon on 12/06/2017.
 */

public interface EventoDao extends Persistencia<Evento> {

    List<Evento> listarNaoEnviadosUsuario(String codigoUsuario) throws PersistenciaExcecao;

    List<Evento> listarNaoEnviadosUsuario(String codigoUsuario, int limit) throws PersistenciaExcecao;

    List<Evento> listarNaoEnviadosUsuario(String codigoUsuario, int limit, int orderType) throws PersistenciaExcecao;

    int limparEventosAntigosEnviados(String codigoUsuario, DateTime dataLimite) throws PersistenciaExcecao;
}

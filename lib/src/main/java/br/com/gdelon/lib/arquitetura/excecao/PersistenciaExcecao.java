package br.com.gdelon.lib.arquitetura.excecao;

import java.util.List;

public class PersistenciaExcecao extends ExcecaoApp {

	public PersistenciaExcecao() {
		super();
	}

	public PersistenciaExcecao(List<String> listaMensagens, Exception e) {
		super(listaMensagens, e);
	}

	public PersistenciaExcecao(List<String> listaMensagens) {
		super(listaMensagens);
	}

	public PersistenciaExcecao(String message, Throwable cause) {
		super(message, cause);
	}

	public PersistenciaExcecao(String message) {
		super(message);
	}

	public PersistenciaExcecao(Throwable cause) {
		super(cause);
	}

}

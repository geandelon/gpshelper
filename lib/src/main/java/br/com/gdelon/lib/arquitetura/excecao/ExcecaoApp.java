package br.com.gdelon.lib.arquitetura.excecao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ExcecaoApp extends Exception {
	
	private static final long serialVersionUID = 1L;

	private List<String> listaMensagens = new ArrayList<>();

	public ExcecaoApp(String message) {
		super(message);
	}

	public ExcecaoApp() {}

	public ExcecaoApp(List<String> listaMensagens, Exception e) {
		super(e);
		this.listaMensagens = listaMensagens;
	}

	public ExcecaoApp(List<String> listaMensagens) {
		this.listaMensagens = listaMensagens;
	}

	public ExcecaoApp(String message, Throwable cause) {
		super(message, cause);
	}

	public ExcecaoApp(Throwable cause) {
		super(cause);
	}

	public List<String> getListaMensagens() {
		return Collections.unmodifiableList(listaMensagens);
	}

	public String getMessage() {
		String demaisMensagens = "";
		if (listaMensagens != null && !listaMensagens.isEmpty()) {
			demaisMensagens = listaMensagens.toString();
		}
		return String.format("%s %s", super.getMessage(), demaisMensagens);
	}

}

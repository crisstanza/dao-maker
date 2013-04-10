package br.com.test.dao;

public final class CategoriaDAO {

	private static final String _N = "\n";

	private static final String getSqlBase() {
		final StringBuilder sb = new StringBuilder();
		sb.append("SELECT * FROM categoria" + _N);
		return sb.toString()
	}

}

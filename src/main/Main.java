package main;

import java.util.List;

import dao.DAO;
import model.Table;

import maker.Maker;

public final class Main {

	private final boolean MOCK = true;

	public static final void main(final String[] args) throws Exception {
		final Main main = new Main();
		main.go();
	}

	private Main() {
	}

	private final void go() throws Exception {
		final List<Table> tables = MOCK ? DAO.mockTables() : DAO.getTables();
		final Maker maker = new Maker();
		maker.go(tables);
		//
		System.out.println("Tabelas: " + tables.size());
		//
	}

}

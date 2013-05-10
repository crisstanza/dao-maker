package main;

import java.util.List;

import maker.Maker;
import model.Table;
import config.Config;
import dao.DAO;

public final class Main {

	private final Config config;

	public static final void main(final String[] args) throws Exception {
		final Main main = new Main();
		main.go();
	}

	private Main() {
		config = Config.getInstance();
	}

	private final void go() throws Exception {
		final List<Table> tables = config.isMock() ? DAO.mockTables() : DAO.getTables();
		final Maker maker = new Maker();
		maker.go(tables);
		//
		System.out.println("Tabelas: " + tables.size());
		//
	}

}

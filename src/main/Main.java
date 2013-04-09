package main;

import java.util.List;

import model.Table;
import dao.DAO;

public final class Main {

	public static final void main(final String[] args) throws Exception {
		final Main main = new Main();
		main.go();
	}

	private Main() {
	}

	private final void go() throws Exception {
		final List<Table> tables = DAO.getTables();

		System.out.println(tables);

	}

}

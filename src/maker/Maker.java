package maker;

import java.util.List;

import model.Table;

public final class Maker {

	public final void go(final List<Table> tables) throws Exception {
		goBeans(tables);
		goDAOs(tables);
		goEJBs(tables);
	}

	private final void goBeans(final List<Table> tables) throws Exception {
		final BeanMaker maker = new BeanMaker();
		for (Table table : tables) {
			if (table.getPrimaryKey() != null) {
				maker.go(table);
			}
		}
	}

	private final void goDAOs(final List<Table> tables) throws Exception {
		final DAOMaker maker = new DAOMaker();
		maker.goGenericDAO();
		for (Table table : tables) {
			if (table.getPrimaryKey() != null) {
				maker.go(table);
			}
		}
	}

	private final void goEJBs(final List<Table> tables) throws Exception {
		final EJBMaker maker = new EJBMaker();
		for (Table table : tables) {
			if (table.getPrimaryKey() != null) {
				maker.go(table);
			}
		}
	}

}

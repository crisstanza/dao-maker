package maker;

import java.util.List;

import model.Table;

public final class Maker {

	public final void go(final List<Table> tables) throws Exception {
		goBeans(tables);
		goDAOs(tables);
	}

	private final void goBeans(final List<Table> tables) throws Exception {
		final BeanMaker beanMaker = new BeanMaker();
		for ( Table table : tables ) {
			beanMaker.go(table);
		}
	}

	private final void goDAOs(final List<Table> tables) throws Exception {
		final DAOMaker daoMaker = new DAOMaker();
		daoMaker.goGenericDAO();
		for ( Table table : tables ) {
			daoMaker.go(table);
		}
	}

}

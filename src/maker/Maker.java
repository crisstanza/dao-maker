package maker;

import java.util.List;

import config.Config;
import model.Table;

public final class Maker {

	private final Config config;

	public Maker() {
		config = Config.getInstance();
	}

	public final void go(final List<Table> tables) throws Exception {

		System.out.println(tables);

	}

}

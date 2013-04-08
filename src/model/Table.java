package model;

import java.util.List;

public final class Table {

	private String name;

	private List<Column> columns;

	public final void setName(final String name) {
		this.name = name;
	}

	public final String getName() {
		return this.name;
	}

	public final void setColumns(final List<Column> columns) {
		this.columns = columns;
	}

	public final List<Column> getColumns() {
		return this.columns;
	}

	public final String toString() {
		return name;
	}

}

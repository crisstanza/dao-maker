package model;

public final class Column {

	private String name;
	private int type;

	public final void setName(final String name) {
		this.name = name;
	}

	public final String getName() {
		return this.name;
	}

	public final void setType(final int type) {
		this.type = type;
	}

	public final int getType() {
		return this.type;
	}

	public final String toString() {
		return name;
	}

}

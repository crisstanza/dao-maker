package model;

public final class Column {

	private String name;
	private int type;
	private String typeName;

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

	public final void setTypeName(final String typeName) {
		this.typeName = typeName;
	}

	public final String getTypeName() {
		return this.typeName;
	}

	public final String toString() {
		return type + " " + typeName + " " + name;
	}

}

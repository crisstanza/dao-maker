package config;

import java.util.ResourceBundle;

public final class Config {

	private static final Config instance = new Config();
	private final ResourceBundle rb = ResourceBundle.getBundle("config");

	private Config() {
	}

	public static final Config getInstance() {
		return instance;
	}

	public final String getDbDriver() {
		return rb.getString("db-driver");
	}

	public final String getDbUrl() {
		return rb.getString("db-url");
	}

	public final String getDbUser() {
		return rb.getString("db-user");
	}

	public final String getDbPass() {
		return rb.getString("db-pass");
	}

	public final String getFolderOut() {
		return rb.getString("folder-out");
	}

	public final String getDefaultPackage() {
		return rb.getString("default-package");
	}

	public final String getFilterSufix() {
		return rb.getString("filter-sufix");
	}

	public final String getDataSourceName() {
		return rb.getString("data-source-name");
	}

	public final boolean isMock() {
		return Boolean.parseBoolean(rb.getString("mock"));
	}

}

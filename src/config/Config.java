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

}

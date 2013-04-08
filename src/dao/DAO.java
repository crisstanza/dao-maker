package dao;

import java.util.ArrayList;
import java.util.List;

import model.Column;
import model.Table;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;

import config.Config;

public final class DAO {

	private DAO() {
	}

	public static final Connection getConnection() throws Exception {
		final Config config = Config.getInstance();
		Class.forName(config.getDbDriver());
		return DriverManager.getConnection(config.getDbUrl(), config.getDbUser(), config.getDbPass());
	}

	public static final List<Table> getTables() throws Exception {
		Connection con = null;
		try {
			final Config config = Config.getInstance();
			Class.forName(config.getDbDriver());
			con = DriverManager.getConnection(config.getDbUrl(), config.getDbUser(), config.getDbPass());
			return DAO.getTables(con);
		} catch ( final Exception exc  ) {
			throw exc;
		} finally {
			if ( con != null ) {
				con.close();
			}
		}
	}

	private static final List<Table> getTables(final Connection con) throws Exception {
		final List<Table> tables = new ArrayList<Table>();
		final DatabaseMetaData md = con.getMetaData();
		final ResultSet rs = md.getTables(null, null, "%", null);
		while (rs.next()) {
  			final Table table = new Table();
  			table.setName(rs.getString(3));
  			table.setColumns(DAO.getColumns(con, table.getName()));
  			tables.add(table);
		}
		return tables;
	}

	private static final List<Column> getColumns(final Connection con, final String table) throws Exception {
		final List<Column> columns = new ArrayList<Column>();
		final Statement st = con.createStatement();
		final ResultSet rs = st.executeQuery("SELECT * FROM "+table);
		final ResultSetMetaData md = rs.getMetaData();
		final int count = md.getColumnCount();
		for (int i = 1; i <= count; i++){
			final Column column = new Column();
			column.setName(md.getColumnName(i));
			column.setType(md.getColumnType(i));
			columns.add(column);
		}
		return columns;
	}

}

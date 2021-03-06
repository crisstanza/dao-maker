package dao;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import model.Column;
import model.Table;
import config.Config;

public final class DAO {

	private DAO() {
	}

	private static final Connection open() throws Exception {
		final Config config = Config.getInstance();
		Class.forName(config.getDbDriver());
		return DriverManager.getConnection(config.getDbUrl(), config.getDbUser(), config.getDbPass());
	}

	public static final List<Table> mockTables() throws Exception {
		final List<Table> tables = new ArrayList<Table>();
		final Table table = new Table();
		table.setName("categoria");
		table.setColumns(DAO.mockColumns());
		tables.add(table);
		return tables;
	}

	public static final List<Column> mockColumns() throws Exception {
		final List<Column> columns = new ArrayList<Column>();
		{
			final Column column = new Column();
			column.setName("id");
			column.setType(Types.INTEGER);
			column.setTypeName("int4");
			column.setClassName("java.lang.Integer");
			columns.add(column);
		}
		{
			final Column column2 = new Column();
			column2.setName("name");
			column2.setType(Types.VARCHAR);
			column2.setTypeName("varchar");
			column2.setClassName("java.lang.String");
			columns.add(column2);
		}
		{
			final Column column3 = new Column();
			column3.setName("status");
			column3.setType(Types.BOOLEAN);
			column3.setTypeName("bool");
			column3.setClassName("java.lang.Boolean");
			columns.add(column3);
		}
		{
			final Column column4 = new Column();
			column4.setName("createDate");
			column4.setType(Types.TIMESTAMP);
			column4.setTypeName("timestamp");
			column4.setClassName("java.util.Date");
			columns.add(column4);
		}
		{
			final Column column5 = new Column();
			column5.setName("price");
			column5.setType(Types.DOUBLE);
			column5.setTypeName("numeric");
			column5.setClassName("java.math.BigDecimal");
			columns.add(column5);
		}
		return columns;
	}

	public static final List<Table> getTables() throws Exception {
		Connection con = null;
		try {
			con = DAO.open();
			return DAO.getTables(con);
		} catch (final Exception exc) {
			throw exc;
		} finally {
			if (con != null) {
				con.close();
			}
		}
	}

	private static final List<Table> getTables(final Connection con) throws Exception {
		final List<Table> tables = new ArrayList<Table>();
		final DatabaseMetaData md = con.getMetaData();
		final ResultSet rs = md.getTables(null, null, "%", new String[] { "TABLE" });
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
		final ResultSet rs = st.executeQuery("SELECT * FROM " + table);
		final ResultSetMetaData md = rs.getMetaData();
		final int count = md.getColumnCount();
		for (int i = 1; i <= count; i++) {
			final Column column = new Column();
			column.setName(md.getColumnName(i));
			column.setType(md.getColumnType(i));
			column.setTypeName(md.getColumnTypeName(i));
			column.setClassName(md.getColumnClassName(i));
			columns.add(column);
			//
			// System.out.println( column );
			//
		}
		return columns;
	}

}

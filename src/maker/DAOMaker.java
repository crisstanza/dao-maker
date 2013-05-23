package maker;

import java.io.File;
import java.io.Writer;

import model.Column;
import model.Table;
import utils.IOUtils;
import utils.SrcUtils;
import config.Config;

public final class DAOMaker {

	private static final String _N = "\n";

	private final Config config;

	public DAOMaker() {
		config = Config.getInstance();
	}

	public final void go(final Table table) throws Exception {
		final String beanPackageName = SrcUtils.getDefaultBeansPackage(config.getDefaultPackage());
		final String packageName = SrcUtils.getDefaultDAOsPackage(config.getDefaultPackage());
		final File folderOut = new File(config.getFolderOut());
		final File defaultPackage = new File(folderOut, SrcUtils.getFolderFromPackage(packageName));
		defaultPackage.mkdirs();
		final String mainBeanClassName = SrcUtils.getJavaClassName(table.getName());
		final String mainClassName = SrcUtils.getDefaultDAOName(table.getName());
		final String mainVarName = SrcUtils.getJavaVariableName(table.getName());
		final String filterVarName = SrcUtils.getJavaVariableName(config.getFilterSufix());
		final File file = new File(defaultPackage, SrcUtils.getJavaSourceName(mainClassName));
		final Writer bw = IOUtils.getWriter(file);
		bw.write("package " + packageName + ";" + _N);
		bw.write(_N);
		bw.write("import java.sql.Connection;" + _N);
		bw.write("import java.sql.PreparedStatement;" + _N);
		bw.write("import java.sql.ResultSet;" + _N);
		bw.write("import java.sql.Statement;" + _N);
		bw.write("import java.sql.Timestamp;" + _N);
		bw.write("import java.util.ArrayList;" + _N);
		bw.write("import java.util.List;" + _N);
		bw.write(_N);
		bw.write("import " + beanPackageName + "." + mainBeanClassName + ";" + _N);
		bw.write("import " + beanPackageName + "." + mainBeanClassName + config.getFilterSufix() + ";" + _N);
		bw.write(_N);
		bw.write("public final class " + mainClassName + " {" + _N);
		bw.write(_N);
		bw.write("	private static final String _N = \"\\n\";" + _N);
		bw.write(_N);
		bw.write("	private " + mainClassName + "() {" + _N);
		bw.write("	}" + _N);
		bw.write(_N);
		bw.write("	private static final String getSQLSelect(final " + mainBeanClassName + config.getFilterSufix() + " " + filterVarName + ") {" + _N);
		bw.write("		final StringBuilder sb = new StringBuilder();" + _N);
		bw.write("		sb.append(\"SELECT\" + _N);" + _N);
		for (int i = 0; i < table.getColumns().size(); i++) {
			final Column column = table.getColumns().get(i);
			bw.write("		sb.append(\"	" + column.getName() + (i < table.getColumns().size() - 1 ? "," : "") + "\" + _N);" + _N);
		}
		bw.write("		sb.append(\"FROM\" + _N);" + _N);
		bw.write("		sb.append(\"	" + table.getName() + "\" + _N);" + _N);
		bw.write("		if ( " + filterVarName + " != null ) {" + _N);
		bw.write("			sb.append(\"WHERE\" + _N);" + _N);
		for (int i = 0; i < table.getColumns().size(); i++) {
			final Column column = table.getColumns().get(i);
			bw.write("			sb.append(" + filterVarName + "." + SrcUtils.getJavaGetterName(column.getName()) + "() != null ? \"	" + column.getName() + " = ? AND\" + _N : \"\");" + _N);
		}
		bw.write("			sb.append(\"	1 = 1\" + _N);" + _N);
		bw.write("		}" + _N);
		bw.write("		sb.append(\"ORDER BY\" + _N);" + _N);
		bw.write("		sb.append(\"	" + table.getPrimaryKey().getName() + "\");" + _N);
		bw.write("		return sb.toString();" + _N);
		bw.write("	}" + _N);
		bw.write(_N);
		bw.write("	private static final void update(final " + mainBeanClassName + " " + mainVarName + ") throws Exception {" + _N);
		bw.write("		PreparedStatement ps = null;" + _N);
		bw.write("		Connection con = null;" + _N);
		bw.write("		StringBuilder query = new StringBuilder();" + _N);
		bw.write("		try {" + _N);
		bw.write("			con = DAO.open();" + _N);
		bw.write("			query.append(\"UPDATE " + table.getName() + "\" + _N);" + _N);
		bw.write("			query.append(\"SET\" + _N);" + _N);
		for (int i = 0; i < table.getColumns().size(); i++) {
			final Column column = table.getColumns().get(i);
			if (!column.isPrimaryKey()) {
				bw.write("			query.append(\"	" + column.getName() + " = ?" + (i < table.getColumns().size() - 1 ? "," : "") + "\" + _N);" + _N);
			}
		}
		bw.write("			query.append(\"WHERE id = ?\");" + _N);
		bw.write("			ps = con.prepareStatement(query.toString());" + _N);
		for (int i = 0, j = 1; i < table.getColumns().size(); i++) {
			final Column column = table.getColumns().get(i);
			if (!column.isPrimaryKey()) {
				if (SrcUtils.isTimestamp(column.getType())) {
					bw.write("			ps." + SrcUtils.getJavaPreparedStatementSetterName(column.getClassName()) + "(" + j + ", new Timestamp(" + mainVarName + "." + SrcUtils.getJavaGetterName(column.getName()) + "().getTime()));" + _N);
				} else {
					bw.write("			ps." + SrcUtils.getJavaPreparedStatementSetterName(column.getClassName()) + "(" + j + ", " + mainVarName + "." + SrcUtils.getJavaGetterName(column.getName()) + "());" + _N);
				}
				j++;
			}
		}
		bw.write("			ps." + SrcUtils.getJavaPreparedStatementSetterName(table.getPrimaryKey().getClassName()) + "(" + table.getColumns().size() + ", " + mainVarName + "." + SrcUtils.getJavaGetterName(table.getPrimaryKey().getName()) + "());" + _N);
		bw.write("			ps.executeUpdate();" + _N);
		bw.write("		} catch (final Exception exc) {" + _N);
		bw.write("			exc.printStackTrace();" + _N);
		bw.write("			throw new Exception(_N + query + _N, exc);" + _N);
		bw.write("		} finally {" + _N);
		bw.write("			DAO.close(null, ps, con);" + _N);
		bw.write("		}" + _N);
		bw.write("	}" + _N);
		bw.write(_N);
		bw.write("	private static final void insert(final " + mainBeanClassName + " " + mainVarName + ") throws Exception {" + _N);
		bw.write("		PreparedStatement ps = null;" + _N);
		bw.write("		ResultSet rs = null;" + _N);
		bw.write("		Connection con = null;" + _N);
		bw.write("		StringBuilder query = new StringBuilder();" + _N);
		bw.write("		try {" + _N);
		bw.write("			con = DAO.open();" + _N);
		bw.write("			query.append(\"INSERT INTO " + table.getName() + " (\" + _N);" + _N);
		for (int i = 0; i < table.getColumns().size(); i++) {
			final Column column = table.getColumns().get(i);
			if (!column.isPrimaryKey()) {
				bw.write("			query.append(\"	" + column.getName() + (i < table.getColumns().size() - 1 ? "," : "") + "\" + _N);" + _N);
			}
		}
		bw.write("			query.append(\") VALUES (\" + _N);" + _N);
		for (int i = 0; i < table.getColumns().size(); i++) {
			final Column column = table.getColumns().get(i);
			if (!column.isPrimaryKey()) {
				bw.write("			query.append(\"	?" + (i < table.getColumns().size() - 1 ? "," : "") + "\" + _N);" + _N);
			}
		}
		bw.write("			query.append(\")\");" + _N);
		bw.write("			ps = con.prepareStatement(query.toString(), Statement.RETURN_GENERATED_KEYS);" + _N);
		for (int i = 0, j = 1; i < table.getColumns().size(); i++) {
			final Column column = table.getColumns().get(i);
			if (!column.isPrimaryKey()) {
				if (SrcUtils.isTimestamp(column.getType())) {
					bw.write("			ps." + SrcUtils.getJavaPreparedStatementSetterName(column.getClassName()) + "(" + j + ", new Timestamp(" + mainVarName + "." + SrcUtils.getJavaGetterName(column.getName()) + "().getTime()));" + _N);
				} else {
					bw.write("			ps." + SrcUtils.getJavaPreparedStatementSetterName(column.getClassName()) + "(" + j + ", " + mainVarName + "." + SrcUtils.getJavaGetterName(column.getName()) + "());" + _N);
				}
				j++;
			}
		}
		bw.write("			ps.executeUpdate();" + _N);
		bw.write("			rs = ps.getGeneratedKeys();" + _N);
		bw.write("			rs.next();" + _N);
		bw.write("			" + mainVarName + ".setId(rs." + SrcUtils.getJavaPreparedStatementGetterName(table.getPrimaryKey().getClassName()) + "(1));" + _N);
		bw.write("		} catch (final Exception exc) {" + _N);
		bw.write("			exc.printStackTrace();" + _N);
		bw.write("			throw new Exception(_N + query + _N, exc);" + _N);
		bw.write("		} finally {" + _N);
		bw.write("			DAO.close(rs, ps, con);" + _N);
		bw.write("		}" + _N);
		bw.write("	}" + _N);
		bw.write(_N);
		bw.write("	public static final int save(final " + mainBeanClassName + " " + mainVarName + ") throws Exception {" + _N);
		bw.write("		if ( " + mainVarName + ".getId() == null ) {" + _N);
		bw.write("			" + mainClassName + ".update(" + mainVarName + ");" + _N);
		bw.write("		} else {" + _N);
		bw.write("			" + mainClassName + ".insert(" + mainVarName + ");" + _N);
		bw.write("		}" + _N);
		bw.write("		return " + mainVarName + ".getId();" + _N);
		bw.write("	}" + _N);
		bw.write(_N);
		bw.write("	public static final " + mainBeanClassName + " findById(final Integer id) throws Exception {" + _N);
		bw.write("		final Connection con = null;" + _N);
		bw.write("		return " + mainClassName + ".findById(id, con);" + _N);
		bw.write("	}" + _N);
		bw.write(_N);
		bw.write("	public static final " + mainBeanClassName + " findById(final Integer id, final Connection con) throws Exception {" + _N);
		bw.write("		final " + mainBeanClassName + config.getFilterSufix() + " " + filterVarName + " = new " + mainBeanClassName + config.getFilterSufix() + "();" + _N);
		bw.write("		" + filterVarName + ".setId(id);" + _N);
		bw.write("		return " + mainClassName + ".find(con, " + mainClassName + ".getSQLSelect(" + filterVarName + "), " + filterVarName + ");" + _N);
		bw.write("	}" + _N);
		bw.write(_N);
		bw.write("	public static final List<" + mainBeanClassName + "> findAll() throws Exception {" + _N);
		bw.write("		final Connection con = null;" + _N);
		bw.write("		return " + mainClassName + ".findAll(con);" + _N);
		bw.write("	}" + _N);
		bw.write(_N);
		bw.write("	public static final List<" + mainBeanClassName + "> findAll(final Connection con) throws Exception {" + _N);
		bw.write("		final " + mainBeanClassName + config.getFilterSufix() + " " + filterVarName + " = null;" + _N);
		bw.write("		return " + mainClassName + ".findAll(con, " + mainClassName + ".getSQLSelect(" + filterVarName + "), " + filterVarName + ");" + _N);
		bw.write("	}" + _N);
		bw.write(_N);
		bw.write("	private static final " + mainBeanClassName + " find(final Connection conAux, final String query, final " + mainBeanClassName + config.getFilterSufix() + " " + filterVarName + ") throws Exception {" + _N);
		bw.write("		PreparedStatement ps = null;" + _N);
		bw.write("		ResultSet rs = null;" + _N);
		bw.write("		Connection con = null;" + _N);
		bw.write("		try {" + _N);
		bw.write("			con = conAux == null ? DAO.open() : conAux;" + _N);
		bw.write("			ps = con.prepareStatement(query);" + _N);
		bw.write("			rs = find(ps, " + filterVarName + ");" + _N);
		bw.write("			if (rs.next()) {" + _N);
		bw.write("				return find(rs, con);" + _N);
		bw.write("			} else {" + _N);
		bw.write("				return null;" + _N);
		bw.write("			}" + _N);
		bw.write("		} catch ( final Exception exc ) {" + _N);
		bw.write("			exc.printStackTrace();" + _N);
		bw.write("			throw new Exception(_N + query + _N, exc);" + _N);
		bw.write("		} finally {" + _N);
		bw.write("			DAO.close(rs, ps, conAux == null ? con : null);" + _N);
		bw.write("		}" + _N);
		bw.write("	}" + _N);
		bw.write(_N);
		bw.write("	private static final List<" + mainBeanClassName + "> findAll(final Connection conAux, final String query, final " + mainBeanClassName + config.getFilterSufix() + " " + filterVarName + ") throws Exception {" + _N);
		bw.write("		PreparedStatement ps = null;" + _N);
		bw.write("		ResultSet rs = null;" + _N);
		bw.write("		Connection con = null;" + _N);
		bw.write("		final List<" + mainBeanClassName + "> list = new ArrayList<" + mainBeanClassName + ">();" + _N);
		bw.write("		try {" + _N);
		bw.write("			con = conAux == null ? DAO.open() : conAux;" + _N);
		bw.write("			ps = con.prepareStatement(query);" + _N);
		bw.write("			rs = find(ps, " + filterVarName + ");" + _N);
		bw.write("			while (rs.next()) {" + _N);
		bw.write("				list.add(find(rs, con));" + _N);
		bw.write("			}" + _N);
		bw.write("			return list;" + _N);
		bw.write("		} catch ( final Exception exc ) {" + _N);
		bw.write("			exc.printStackTrace();" + _N);
		bw.write("			throw new Exception(_N + query + _N, exc);" + _N);
		bw.write("		} finally {" + _N);
		bw.write("			DAO.close(rs, ps, conAux == null ? con : null);" + _N);
		bw.write("		}" + _N);
		bw.write("	}" + _N);
		bw.write(_N);
		bw.write("	private static final ResultSet find(final PreparedStatement ps, final " + mainBeanClassName + config.getFilterSufix() + " " + filterVarName + ") throws Exception {" + _N);
		bw.write("		if (" + filterVarName + " != null) {" + _N);
		bw.write("			int i = 1;" + _N);
		for (int i = 0; i < table.getColumns().size(); i++) {
			final Column column = table.getColumns().get(i);
			bw.write("			if (" + filterVarName + "." + SrcUtils.getJavaGetterName(column.getName()) + "() != null) {" + _N);
			if (SrcUtils.isTimestamp(column.getType())) {
				bw.write("				ps." + SrcUtils.getJavaPreparedStatementSetterName(column.getClassName()) + "(i++, new Timestamp(" + filterVarName + "." + SrcUtils.getJavaGetterName(column.getName()) + "().getTime()));" + _N);
			} else {
				bw.write("				ps." + SrcUtils.getJavaPreparedStatementSetterName(column.getClassName()) + "(i++, " + filterVarName + "." + SrcUtils.getJavaGetterName(column.getName()) + "());" + _N);
			}
			bw.write("			}" + _N);
		}
		bw.write("		}" + _N);
		bw.write("		final ResultSet rs = ps.executeQuery();" + _N);
		bw.write("		return rs;" + _N);
		bw.write("	}" + _N);
		bw.write(_N);
		bw.write("	private static final " + mainBeanClassName + " find(final ResultSet rs, final Connection con) throws Exception {" + _N);
		bw.write("		final " + mainBeanClassName + " " + mainVarName + " = new " + mainBeanClassName + "();" + _N);
		for (Column column : table.getColumns()) {
			bw.write("		" + mainVarName + "." + SrcUtils.getJavaSetterName(column.getName()) + "(rs." + SrcUtils.getJavaPreparedStatementGetterName(column.getClassName()) + "(\"" + column.getName() + "\"));" + _N);
		}
		bw.write("		return " + mainVarName + ";" + _N);
		bw.write("	}" + _N);
		bw.write(_N);
		bw.write("}" + _N);
		bw.close();
	}

	public final void goGenericDAO() throws Exception {
		final String packageName = config.getDefaultPackage() + ".dao";
		final File folderOut = new File(config.getFolderOut());
		final File defaultPackage = new File(folderOut, SrcUtils.getFolderFromPackage(packageName));
		defaultPackage.mkdirs();
		final File file = new File(defaultPackage, SrcUtils.getJavaSourceName(SrcUtils.getDefaultDAOName("")));
		final Writer bw = IOUtils.getWriter(file);
		bw.write("package " + packageName + ";" + _N);
		bw.write(_N);
		bw.write("import java.sql.Connection;" + _N);
		bw.write("import java.sql.ResultSet;" + _N);
		bw.write("import java.sql.Statement;" + _N);
		bw.write(_N);
		bw.write("import javax.naming.Context;" + _N);
		bw.write("import javax.naming.InitialContext;" + _N);
		bw.write("import javax.sql.DataSource;" + _N);
		bw.write(_N);
		bw.write("public final class DAO {" + _N);
		bw.write(_N);
		bw.write("	private static final String DATA_SOURCE = \"" + config.getDataSourceName() + "\";" + _N);
		bw.write(_N);
		bw.write("	private DAO() {" + _N);
		bw.write("	}" + _N);
		bw.write(_N);
		bw.write("	public static final Connection open() throws Exception {" + _N);
		bw.write("		final Context ctx = new InitialContext();" + _N);
		bw.write("		final DataSource ds = (DataSource) ctx.lookup(DAO.DATA_SOURCE);" + _N);
		bw.write("		final Connection con = ds.getConnection();" + _N);
		bw.write("		return con;" + _N);
		bw.write("	}" + _N);
		bw.write(_N);
		bw.write("	public static final void close(final ResultSet rs, final Statement s, final Connection con) {" + _N);
		bw.write("		DAO.close(rs);" + _N);
		bw.write("		DAO.close(s);" + _N);
		bw.write("		DAO.close(con);" + _N);
		bw.write("	}" + _N);
		bw.write(_N);
		bw.write("	private static final void close(final ResultSet rs) {" + _N);
		bw.write("		try {" + _N);
		bw.write("			if ( rs != null ) {" + _N);
		bw.write("				rs.close();" + _N);
		bw.write("			}" + _N);
		bw.write("		} catch (final Exception exc) {" + _N);
		bw.write("			exc.printStackTrace();" + _N);
		bw.write("		}" + _N);
		bw.write("	}" + _N);
		bw.write(_N);
		bw.write("	private static final void close(final Statement s) {" + _N);
		bw.write("		try {" + _N);
		bw.write("			if ( s != null ) {" + _N);
		bw.write("				s.close();" + _N);
		bw.write("			}" + _N);
		bw.write("		} catch (final Exception exc) {" + _N);
		bw.write("			exc.printStackTrace();" + _N);
		bw.write("		}" + _N);
		bw.write("	}" + _N);
		bw.write(_N);
		bw.write("	private static final void close(final Connection con) {" + _N);
		bw.write("		try {" + _N);
		bw.write("			if ( con != null ) {" + _N);
		bw.write("				con.close();" + _N);
		bw.write("			}" + _N);
		bw.write("		} catch (final Exception exc) {" + _N);
		bw.write("			exc.printStackTrace();" + _N);
		bw.write("		}" + _N);
		bw.write("	}" + _N);
		bw.write(_N);
		bw.write("	public static final Integer selectInt(final Connection conAux, final String query, final Integer n) throws Exception {" + _N);
		bw.write("		PreparedStatement ps = null;" + _N);
		bw.write("		ResultSet rs = null;" + _N);
		bw.write("		Connection con = null;" + _N);
		bw.write("		try {" + _N);
		bw.write("			con = conAux == null ? DAO.open() : conAux;" + _N);
		bw.write("			ps = con.prepareStatement(query);" + _N);
		bw.write("			if (n != null) {" + _N);
		bw.write("				ps.setInt(1, n);" + _N);
		bw.write("			}" + _N);
		bw.write("			rs = ps.executeQuery();" + _N);
		bw.write("			if (rs.next()) {" + _N);
		bw.write("				return rs.getInt(1);" + _N);
		bw.write("			} else {" + _N);
		bw.write("				return null;" + _N);
		bw.write("			}" + _N);
		bw.write("		} catch (final Exception exc) {" + _N);
		bw.write("			exc.printStackTrace();" + _N);
		bw.write("			throw new Exception(_N + query + _N, exc);" + _N);
		bw.write("		} finally {" + _N);
		bw.write("			DAO.close(rs, ps, conAux == null ? con : null);" + _N);
		bw.write("		}" + _N);
		bw.write("	}" + _N);
		bw.write(_N);
		bw.write("	public static final void update(final Connection conAux, final String query, final Timestamp ts, final Integer n) throws Exception {" + _N);
		bw.write("		PreparedStatement ps = null;" + _N);
		bw.write("		ResultSet rs = null;" + _N);
		bw.write("		Connection con = null;" + _N);
		bw.write("		try {" + _N);
		bw.write("			con = conAux == null ? DAO.open() : conAux;" + _N);
		bw.write("			ps = con.prepareStatement(query);" + _N);
		bw.write("			int i = 1;" + _N);
		bw.write("			if (ts != null) {" + _N);
		bw.write("				ps.setTimestamp(i++, ts);" + _N);
		bw.write("			}" + _N);
		bw.write("			if (n != null) {" + _N);
		bw.write("				ps.setInt(i++, n);" + _N);
		bw.write("			}" + _N);
		bw.write("			ps.executeUpdate();" + _N);
		bw.write("		} catch (final Exception exc) {" + _N);
		bw.write("			exc.printStackTrace();" + _N);
		bw.write("			throw new Exception(_N + query + _N, exc);" + _N);
		bw.write("		} finally {" + _N);
		bw.write("			DAO.close(rs, ps, conAux == null ? con : null);" + _N);
		bw.write("		}" + _N);
		bw.write("	}" + _N);
		bw.write(_N);
		bw.write("}" + _N);
		bw.close();
	}
}

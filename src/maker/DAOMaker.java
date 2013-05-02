package maker;

import java.io.File;

import java.util.List;

import config.Config;
import model.Column;
import model.Table;
import utils.IOUtils;
import utils.SrcUtils;

import java.io.Writer;

public final class DAOMaker {

	private static final String _N = "\n";

	private final Config config;

	public DAOMaker() {
		config = Config.getInstance();
	}

	public final void go(final Table table) throws Exception {
		final String packageName = config.getDefaultPackage() + ".dao";
		final File folderOut = new File(config.getFolderOut());
		final File defaultPackage = new File(folderOut, SrcUtils.getFolderFromPackage(packageName));
		defaultPackage.mkdirs();
		goGenericDAO(packageName, defaultPackage);
		final String mainClassName = SrcUtils.getJavaClassName(table.getName());
		final String mainVarName = SrcUtils.getJavaVariableName(table.getName());
		final File file = new File(defaultPackage, SrcUtils.getJavaSourceName(table.getName(), "DAO"));
		final Writer bw = IOUtils.getWriter(file);
		bw.write("package " + packageName + ";" + _N);
		bw.write(_N);
		bw.write("import java.sql.Connection;" + _N);
		bw.write("import java.sql.PreparedStatement;" + _N);
		bw.write("import java.sql.ResultSet;" + _N);
		bw.write("import java.util.Date;" + _N);
		bw.write("import java.util.ArrayList;" + _N);
		bw.write("import java.util.List;" + _N);
		bw.write(_N);
		bw.write("public final class " + mainClassName + "DAO {"+ _N);
		bw.write(_N);
		bw.write("	private static final String _N = \"\\n\";" + _N);
		bw.write(_N);
		bw.write("	private " + mainClassName + "DAO {"+ _N);
		bw.write("	}"+ _N);
		bw.write(_N);
		bw.write("	private static final String getSqlBase(final "+mainClassName+"Filter filter) {" + _N);		
		bw.write("		final StringBuilder sb = new StringBuilder();" + _N);
		bw.write("		sb.append(\"SELECT * FROM "+table.getName()+"\" + _N);" + _N);
		bw.write("		return sb.toString()" + _N);
		bw.write("	}" + _N);
		bw.write(_N);
		bw.write("	private static final int save(final "+mainClassName+" "+mainVarName+") {" + _N);		
		bw.write("		if ( "+mainVarName+".getId() > 0 ) throws Exception {" + _N);
		bw.write("			" + mainClassName + "DAO.update("+mainVarName+");" + _N);
		bw.write("		} else {" + _N);
		bw.write("			" + mainClassName + "DAO.insert("+mainVarName+");" + _N);
		bw.write("		}" + _N);
		bw.write("		return "+mainVarName+".getId();" + _N);
		bw.write("	}" + _N);
		bw.write(_N);
		bw.write("	private static final "+mainClassName+" findById(final int id) throws Exception {" + _N);		
		bw.write("		final "+mainClassName+"Filter filter = new "+mainClassName+"Filter();" + _N);
		bw.write("		filter.setId(id);" + _N);
		bw.write("		return " + mainClassName + "DAO.find("+mainClassName + "DAO.getSqlBase(filter));" + _N);
		bw.write("	}" + _N);
		bw.write(_N);
		bw.write("	private static final List<"+mainClassName+"> findAll() throws Exception {" + _N);		
		bw.write("		final StringBuilder sb = new StringBuilder();" + _N);
		bw.write("		return " + mainClassName + "DAO.findAll("+mainClassName + "DAO.getSqlBase());" + _N);
		bw.write("	}" + _N);
		bw.write(_N);
		bw.write("	private static final "+mainClassName+" find(final String query) throws Exception {" + _N);		
		bw.write("		PreparedStatement ps = null;" + _N);
		bw.write("		ResultSet rs = null;" + _N);
		bw.write("		Connection con = null;" + _N);
		bw.write("		try {" + _N);
		bw.write("			con = DAO.getConexao();" + _N);
		bw.write("			ps = con.prepareStatement( query );" + _N);
		bw.write("			rs = ps.executeQuery();" + _N);
		bw.write("			if ( rs.next() ) {" + _N);
		bw.write("				return find(rs);" + _N);
		bw.write("			} else {" + _N);
		bw.write("				return null;" + _N);
		bw.write("			}" + _N);
		bw.write("		} catch ( final Exception exc ) {" + _N);
		bw.write("			exc.printStackTrace();" + _N);
		bw.write("			throw new Exception(_N + query + _N, exc);" + _N);
		bw.write("		} finally {" + _N);
		bw.write("			DAO.close(rs, ps, con);" + _N);
		bw.write("		}" + _N);
		bw.write("	}" + _N);
		bw.write(_N);
		bw.write("	private static final List<"+mainClassName+"> findAll(final String query) throws Exception {" + _N);
		bw.write("		PreparedStatement ps = null;" + _N);
		bw.write("		ResultSet rs = null;" + _N);
		bw.write("		Connection con = null;" + _N);
		bw.write("		final List<"+mainClassName+"> list = new ArrayList<"+mainClassName+">();" + _N);
		bw.write("		try {" + _N);
		bw.write("			con = DAO.getConexao();" + _N);
		bw.write("			ps = con.prepareStatement( query );" + _N);
		bw.write("			rs = ps.executeQuery();" + _N);
		bw.write("			while ( rs.next() ) {" + _N);
		bw.write("				list.add(find(rs));" + _N);
		bw.write("			}" + _N);
		bw.write("			return list;" + _N);
		bw.write("		} catch ( final Exception exc ) {" + _N);
		bw.write("			exc.printStackTrace();" + _N);
		bw.write("			throw new Exception(_N + query + _N, exc);" + _N);
		bw.write("		} finally {" + _N);
		bw.write("			DAO.close(rs, ps, con);" + _N);
		bw.write("		}" + _N);
		bw.write("	}" + _N);
		bw.write(_N);
		bw.write("	private static final "+mainClassName+" find(final ResultSet rs) throws Exception {" + _N);
		bw.write("		final " + mainClassName + " " + mainVarName + " = new " + mainClassName + "();" + _N);
		for ( Column column : table.getColumns() ) {
			bw.write("		" + mainVarName + "." + SrcUtils.getJavaSetterName(column.getName()) + "(rs.get"+SrcUtils.getJavaClassName(SrcUtils.getSimpleName(column.getClassName())) +"(\""+column.getName()+"\"));" + _N);
		}
		bw.write("		return " + mainVarName + ";" + _N);
		bw.write("	}" + _N);
		bw.write(_N);
		bw.write("}"+ _N);
		bw.close();
	}

	public final void goGenericDAO(final String packageName, final File defaultPackage) throws Exception {
		final File file = new File(defaultPackage, SrcUtils.getJavaSourceName(table.getName(), "DAO"));
		final Writer bw = IOUtils.getWriter(file);
		bw.write("package " + packageName + ";" + _N);
		bw.write(_N);
		bw.write("public final class DAO {"+ _N);
		bw.write(_N);
		bw.write("}"+ _N);
		bw.close();		
	}

}

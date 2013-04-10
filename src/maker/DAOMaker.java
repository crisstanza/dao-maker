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
		final File file = new File(defaultPackage, SrcUtils.getJavaSourceName(table.getName(), "DAO"));
		final Writer bw = IOUtils.getWriter(file);
		bw.write("package " + packageName + ";" + _N);
		bw.write(_N);
		bw.write("public final class " + SrcUtils.getJavaClassName(table.getName()) + "DAO {"+ _N);
		bw.write(_N);
		bw.write("	private static final String _N = \"\\n\";" + _N);
		bw.write(_N);
		bw.write("	private static final String getSqlBase() {" + _N);		
		bw.write("		final StringBuilder sb = new StringBuilder();" + _N);
		bw.write("		sb.append(\"SELECT * FROM "+table.getName()+"\" + _N);" + _N);
		bw.write("		return sb.toString()" + _N);
		bw.write("	}" + _N);
		bw.write(_N);
		bw.write("}"+ _N);
		bw.close();
	}

}

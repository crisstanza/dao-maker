package maker;

import java.io.File;
import java.io.Writer;

import model.Table;
import utils.IOUtils;
import utils.SrcUtils;
import config.Config;

public final class EJBMaker {

	private static final String _N = "\n";

	private final Config config;

	public EJBMaker() {
		config = Config.getInstance();
	}

	public final void go(final Table table) throws Exception {
		final String packageName = SrcUtils.getDefaultEJBsPackage(config.getDefaultPackage());
		final File folderOut = new File(config.getFolderOut());
		final File defaultPackage = new File(folderOut, SrcUtils.getFolderFromPackage(packageName));
		defaultPackage.mkdirs();
		final File file = new File(defaultPackage, SrcUtils.getJavaSourceName(SrcUtils.getDefaultEJBName(table.getName())));
		final Writer bw = IOUtils.getWriter(file);
		bw.write("package " + packageName + ";" + _N);
		bw.write(_N);
		bw.write("import java.util.List;" + _N);
		bw.write(_N);
		bw.write("import javax.ejb.Stateless;" + _N);
		bw.write(_N);
		bw.write("import " + SrcUtils.getDefaultDAOsPackage(config.getDefaultPackage()) + "." + SrcUtils.getDefaultDAOName(table.getName()) + ";" + _N);
		bw.write("import " + SrcUtils.getDefaultBeansPackage(config.getDefaultPackage()) + "." + SrcUtils.getJavaClassName(table.getName()) + ";" + _N);
		bw.write(_N);
		bw.write("@Stateless" + _N);
		bw.write("public class " + SrcUtils.getDefaultEJBName(table.getName()) + " {" + _N);
		bw.write(_N);
		bw.write("	public final List<" + SrcUtils.getJavaClassName(table.getName()) + "> findAll() throws Exception {" + _N);
		bw.write("		return " + SrcUtils.getDefaultDAOName(table.getName()) + ".findAll();" + _N);
		bw.write("	}" + _N);
		bw.write(_N);
		bw.write("	public final " + SrcUtils.getJavaClassName(table.getName()) + " findById(final Integer id) throws Exception {" + _N);
		bw.write("		return " + SrcUtils.getDefaultDAOName(table.getName()) + ".findById(id);" + _N);
		bw.write("	}" + _N);
		bw.write(_N);
		bw.write("}" + _N);
		bw.close();
	}

}

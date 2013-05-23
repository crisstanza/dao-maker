package maker;

import java.io.File;
import java.io.Writer;

import model.Table;
import utils.IOUtils;
import utils.SrcUtils;
import config.Config;

public final class TestMaker {

	private static final String _N = "\n";

	private final Config config;

	public TestMaker() {
		config = Config.getInstance();
	}

	public final void go(final Table table) throws Exception {
		final String packageName = SrcUtils.getDefaultTestsPackage(config.getDefaultPackage());
		final String mainVarName = SrcUtils.getJavaVariableName(table.getName());
		final File folderOut = new File(config.getFolderOut());
		final File defaultPackage = new File(folderOut, SrcUtils.getFolderFromPackage(packageName));
		defaultPackage.mkdirs();
		final File file = new File(defaultPackage, SrcUtils.getJavaSourceName(SrcUtils.getDefaultTestName(table.getName())));
		final Writer bw = IOUtils.getWriter(file);
		bw.write("package " + packageName + ";" + _N);
		bw.write(_N);
		bw.write("import java.util.List;" + _N);
		bw.write(_N);
		bw.write("import junit.framework.TestCase;" + _N);
		bw.write(_N);
		bw.write("import org.junit.Test;" + _N);
		bw.write(_N);
		bw.write("import " + SrcUtils.getDefaultEJBsPackage(config.getDefaultPackage()) + "." + SrcUtils.getDefaultEJBName(table.getName()) + ";" + _N);
		bw.write("import " + SrcUtils.getDefaultBeansPackage(config.getDefaultPackage()) + "." + SrcUtils.getJavaClassName(table.getName()) + ";" + _N);
		bw.write(_N);
		bw.write("public class " + SrcUtils.getDefaultTestName(table.getName()) + " extends TestCase {" + _N);
		bw.write(_N);
		bw.write("	private final " + SrcUtils.getDefaultEJBName(table.getName()) + " " + mainVarName + "Bean = new " + SrcUtils.getDefaultEJBName(table.getName()) + "();" + _N);
		bw.write(_N);
		bw.write("	@Test" + _N);
		bw.write("	public final void testFindAll() throws Exception {" + _N);
		bw.write("		final List<" + SrcUtils.getJavaClassName(table.getName()) + "> list = " + mainVarName + "Bean.findAll();" + _N);
		bw.write("		assertNotNull(list);" + _N);
		bw.write("	}" + _N);
		bw.write(_N);
		bw.write("	@Test" + _N);
		bw.write("	public final void testFindById() throws Exception {" + _N);
		bw.write("		final " + SrcUtils.getJavaClassName(table.getName()) + " object = " + mainVarName + "Bean.findById(1);" + _N);
		bw.write("		assertNotNull(object);" + _N);
		bw.write("	}" + _N);
		bw.write(_N);
		bw.write("}" + _N);
		bw.close();
	}

}

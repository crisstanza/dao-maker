package maker;

import java.io.File;

import java.util.List;

import config.Config;
import model.Column;
import model.Table;
import utils.IOUtils;
import utils.SrcUtils;

import java.io.Writer;

public final class BeanMaker {

	private static final String _N = "\n";

	private final Config config;

	public BeanMaker() {
		config = Config.getInstance();
	}

	public final void go(final Table table) throws Exception {
		final String packageName = SrcUtils.getDefaultBeansPackage(config.getDefaultPackage());
		final File folderOut = new File(config.getFolderOut());
		final File defaultPackage = new File(folderOut, SrcUtils.getFolderFromPackage(packageName));
		defaultPackage.mkdirs();
		final File file = new File(defaultPackage, SrcUtils.getJavaSourceName(table.getName()));
		final Writer bw = IOUtils.getWriter(file);
		bw.write("package " + packageName + ";" + _N);
		bw.write(_N);
		bw.write("public final class " + SrcUtils.getJavaClassName(table.getName()) + " {"+ _N);
		bw.write(_N);
		for ( Column column : table.getColumns() ) {
			bw.write("	private " + SrcUtils.getSimpleName(column.getClassName()) + " " + column.getName() + ";" + _N);
		}
		bw.write(_N);
		for ( Column column : table.getColumns() ) {
			bw.write("	public final void " + SrcUtils.getJavaSetterName(column.getName()) + "(final " + SrcUtils.getSimpleName(column.getClassName()) + " " + column.getName() + ") {" + _N);
			bw.write("		this." + column.getName() + " = " + column.getName() + ";" + _N);
			bw.write("	}" + _N);
			bw.write(_N);
			bw.write("	public final " + SrcUtils.getSimpleName(column.getClassName()) + " " + SrcUtils.getJavaGetterName(column.getName()) + "() {" + _N);
			bw.write("		return this." + column.getName() + ";" + _N);
			bw.write("	}" + _N);
			bw.write(_N);
		}
		bw.write("}"+ _N);
		bw.close();
		go(table, packageName, defaultPackage);
	}

	public final void go(final Table table, final String packageName, final File defaultPackage) throws Exception {
		final File file = new File(defaultPackage, SrcUtils.getJavaSourceName(table.getName(), config.getFilterSufix()));
		final Writer bw = IOUtils.getWriter(file);
		bw.write("package " + packageName + ";" + _N);
		bw.write(_N);
		bw.write("public final class " + SrcUtils.getJavaClassName(table.getName(), config.getFilterSufix()) + " extends "+SrcUtils.getJavaClassName(table.getName())+" {"+ _N);
		bw.write("}"+ _N);
		bw.close();		
	}

}

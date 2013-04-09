package utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;
import java.io.Writer;

public final class IOUtils {

	private IOUtils() {
	}

	public static final Writer getWriter(final File file) throws Exception {
		return new BufferedWriter(new FileWriter(file));
	}

	public static final Writer getWriter(final String file) throws Exception {
		return IOUtils.getWriter(new File(file));
	}

	public static final Reader getReader(final String file) throws Exception {
		return new BufferedReader(new FileReader(new File(file)));
	}

	public static final void setFile(final File file, final String contents) throws Exception {
		final Writer writer = IOUtils.getWriter(file);
		writer.write(contents);
		writer.close();
	}

}

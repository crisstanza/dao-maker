package utils;

import java.io.File;

public final class SrcUtils {

	private SrcUtils() {
	}

	public static final String getFolderFromPackage(final String str) throws Exception {
		if ( str == null ) {
			return null;
		} else {
			return str.replaceAll("\\.", "//");
		}
	}

	public static final String getSimpleName(final String str) throws Exception {
		if ( str == null ) {
			return null;
		} else {
			return str.substring(str.lastIndexOf('.') + 1);
		}
	}

	public static final String getJavaClassName(final String str) throws Exception {
		if ( str == null ) {
			return null;
		} else if ( str.length() == 1 ) {
			return str.toUpperCase();
		} else {
			return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
		}
	}

	public static final String getJavaVariableName(final String str) throws Exception {
		if ( str == null ) {
			return null;
		} else if ( str.length() == 1 ) {
			return str.toLowerCase();
		} else {
			return str.substring(0, 1).toLowerCase() + str.substring(1).toLowerCase();
		}
	}

	public static final String getJavaSourceName(final String str) throws Exception {
		return SrcUtils.getJavaSourceName(str, "");
	}

	public static final String getJavaSourceName(final String str, final String str2) throws Exception {
		return SrcUtils.getJavaClassName(str) + str2 + ".java";
	}

	public static final String getJavaSetterName(final String str) throws Exception {
		return "set" + SrcUtils.getJavaClassName(str);
	}
	
	public static final String getJavaGetterName(final String str) throws Exception {
		return "get" + SrcUtils.getJavaClassName(str);
	}
	
}

package utils;

import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

public final class SrcUtils {

	private static Map<String, String> CLASS_NAME_REPLACEMENTS;
	private static Map<String, String> PREPARED_STATEMENT_REPLACEMENTS;

	private static final Map<String, String> getClassNameReplacements() {
		if (CLASS_NAME_REPLACEMENTS == null) {
			CLASS_NAME_REPLACEMENTS = new HashMap<String, String>();
			CLASS_NAME_REPLACEMENTS.put("BigDecimal", "Double");
			CLASS_NAME_REPLACEMENTS.put("Timestamp", "Date");
		}
		return CLASS_NAME_REPLACEMENTS;
	}

	private static final Map<String, String> getPreparedStatementReplacements() {
		if (PREPARED_STATEMENT_REPLACEMENTS == null) {
			PREPARED_STATEMENT_REPLACEMENTS = new HashMap<String, String>();
			PREPARED_STATEMENT_REPLACEMENTS.put("BigDecimal", "Double");
			PREPARED_STATEMENT_REPLACEMENTS.put("Timestamp", "Date");
			PREPARED_STATEMENT_REPLACEMENTS.put("Integer", "Int");
		}
		return PREPARED_STATEMENT_REPLACEMENTS;
	}

	private SrcUtils() {
	}

	private static final String fixSimpleName(final String key) {
		final Map<String, String> replacements = SrcUtils.getClassNameReplacements();
		final String value = replacements.get(key);
		if (value == null) {
			return key;
		} else {
			return value;
		}
	}

	private static final String fixPreparedStatementName(final String key) {
		final Map<String, String> replacements = SrcUtils.getPreparedStatementReplacements();
		final String value = replacements.get(key);
		if (value == null) {
			return key;
		} else {
			return value;
		}
	}

	public static final String getFolderFromPackage(final String str) throws Exception {
		if (str == null) {
			return null;
		} else {
			return str.replaceAll("\\.", "//");
		}
	}

	public static final String getSimpleName(final String str) throws Exception {
		if (str == null) {
			return null;
		} else {
			return SrcUtils.fixSimpleName(str.substring(str.lastIndexOf('.') + 1));
		}
	}

	public static final String getJavaClassName(final String str, final String str2) {
		return SrcUtils.getJavaClassName(str) + str2;
	}

	public static final String getJavaClassName(final String str) {
		if (str == null) {
			return null;
		} else if (str.length() == 0) {
			return str;
		} else if (str.length() == 1) {
			return str.toUpperCase();
		} else {
			return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
		}
	}

	public static final String getJavaVariableName(final String str) throws Exception {
		if (str == null) {
			return null;
		} else if (str.length() == 0) {
			return str;
		} else if (str.length() == 1) {
			return str.toLowerCase();
		} else {
			return str.substring(0, 1).toLowerCase() + str.substring(1).toLowerCase();
		}
	}

	public static final String getJavaSourceName(final String str) throws Exception {
		return SrcUtils.getJavaSourceName(str, "");
	}

	public static final String getJavaSourceName(final String str, final String str2) throws Exception {
		if (str == null) {
			return null;
		} else if (str.length() == 0) {
			return str;
		} else if (str.length() == 1) {
			return str.toUpperCase() + str2 + ".java";
		} else {
			return str.substring(0, 1).toUpperCase() + str.substring(1) + str2 + ".java";
		}
	}

	public static final String getJavaPreparedStatementSetterName(final String str) {
		if (str == null) {
			return null;
		} else {
			return "set" + SrcUtils.fixPreparedStatementName(str.substring(str.lastIndexOf('.') + 1));
		}
	}

	public static final String getJavaPreparedStatementGetterName(final String str) {
		if (str == null) {
			return null;
		} else {
			return "get" + SrcUtils.fixPreparedStatementName(str.substring(str.lastIndexOf('.') + 1));
		}
	}

	public static final String getJavaSetterName(final String str) throws Exception {
		return "set" + SrcUtils.getJavaClassName(str);
	}

	public static final String getJavaGetterName(final String str) throws Exception {
		return "get" + SrcUtils.getJavaClassName(str);
	}

	public static final String getDefaultBeansPackage(final String defaultPackage) {
		return defaultPackage + ".model";
	}

	public static final String getDefaultEJBsPackage(final String defaultPackage) {
		return defaultPackage + ".ejb";
	}

	public static final String getDefaultDAOsPackage(final String defaultPackage) {
		return defaultPackage + ".dao";
	}

	public static final String getDefaultEJBName(final String str) {
		return SrcUtils.getJavaClassName(str, "Bean");
	}

	public static final String getDefaultDAOName(final String str) {
		return SrcUtils.getJavaClassName(str, "DAO");
	}

	public static final boolean isTimestamp(final int type) {
		return type == Types.TIME || type == Types.TIMESTAMP || type == Types.DATE;
	}

}

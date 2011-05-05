
package org.webframe.support.util;

/**
 * @author <a href="mailto:guoqing.huang@berheley.com">黄国庆 </a>
 * @version $Id: codetemplates.xml,v 1.1 2009/09/07 08:48:12 Exp $ Create: 2011-3-19 下午05:50:53
 */
public final class SystemLogUtils {

	private SystemLogUtils() {
	}

	public static void println(Object msg) {
		System.out.println(msg);
	}

	public static void errorPrintln(Object msg) {
		System.err.println("[ERROR] " + msg);
	}

	public static void rootPrintln(Object msg) {
		println("<=======================" + msg + "=======================>");
	}

	public static void secondPrintln(Object msg) {
		println("  " + msg + "----------->");
	}

	public static void thirdPrintln(Object msg) {
		println("    " + msg);
	}
}

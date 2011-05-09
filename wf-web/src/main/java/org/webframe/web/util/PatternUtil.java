/*
 * wf-web
 * Created on 2011-5-9-下午09:25:49
 */

package org.webframe.web.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author <a href="mailto:guoqing.huang@berheley.com">huangguoqing</a>
 * @version $Id: codetemplates.xml,v 1.3 2009/05/05 02:30:07 huangguoqing Exp $ Create: 2011-1-21
 *          下午03:17:04
 */
public abstract class PatternUtil {

	public static List<String> matchs(String regex, CharSequence input) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(input);
		List<String> list = new ArrayList<String>();
		while (matcher.find()) {
			list.add(matcher.group(1));
		}
		return list;
	}
}

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
 * 正则匹配器工具类
 * 
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @since 2012-1-29 上午09:28:26
 * @version
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

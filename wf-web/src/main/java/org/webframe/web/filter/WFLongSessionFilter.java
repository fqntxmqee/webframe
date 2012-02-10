/*
 * wf-web
 * Created on 2012-2-10-上午10:00:35
 */

package org.webframe.web.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.orm.hibernate3.support.OpenSessionInViewFilter;
import org.webframe.core.util.PropertyConfigurerUtils;

/**
 * 提供Hibernate 长Session，当webframe没有加载Hibernate时，跳过该filter
 * 
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @since 2012-2-10 上午10:00:35
 * @version
 */
public class WFLongSessionFilter extends OpenSessionInViewFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
				throws ServletException, IOException {
		// 如果配置文件中hibernate.load为，则不加载hibernate，不生成可以使用的SessionFacotry
		if ("false".equals(PropertyConfigurerUtils.getString("hibernate.load"))) {
			filterChain.doFilter(request, response);
		} else {
			super.doFilterInternal(request, response, filterChain);
		}
	}
}

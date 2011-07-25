
package org.webframe.web.filter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.Filter;

/**
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @version $Id: codetemplates.xml,v 1.1 2009/09/07 08:48:12 Exp $ Create: 2011-4-21 下午02:37:12
 */
public class WFFilterContext {

	private List<Filter>	beforeSecurity	= new LinkedList<Filter>();

	private List<Filter>	afterSecurity	= new LinkedList<Filter>();

	private List<Filter>	allFilters		= new ArrayList<Filter>();

	/**
	 * 获取安全过滤器之前的Filter List
	 * 
	 * @return not null
	 * @author 黄国庆 2011-4-21 下午02:38:57
	 */
	public List<Filter> getBeforeSecurity() {
		return beforeSecurity;
	}

	public void setBeforeSecurity(List<Filter> beforeSecurity) {
		this.beforeSecurity.addAll(beforeSecurity);
		allFilters.addAll(beforeSecurity);
	}

	/**
	 * 获取安全过滤器之后的Filter List
	 * 
	 * @return not null
	 * @author 黄国庆 2011-4-21 下午02:40:00
	 */
	public List<Filter> getAfterSecurity() {
		return afterSecurity;
	}

	public void setAfterSecurity(List<Filter> afterSecurity) {
		this.afterSecurity.addAll(afterSecurity);
		allFilters.addAll(afterSecurity);
	}

	public List<Filter> getAllFilters() {
		return allFilters;
	}

	public void destroy() {
		for (Filter filter : getAllFilters()) {
			filter.destroy();
		}
	}
}

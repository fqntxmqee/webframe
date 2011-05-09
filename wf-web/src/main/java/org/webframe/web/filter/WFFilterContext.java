
package org.webframe.web.filter;

import java.util.LinkedList;
import java.util.List;

import javax.servlet.Filter;

/**
 * @author <a href="mailto:guoqing.huang@berheley.com">黄国庆 </a>
 * @version $Id: codetemplates.xml,v 1.1 2009/09/07 08:48:12 Exp $ Create: 2011-4-21 下午02:37:12
 */
public class WFFilterContext {

	private List<Filter>	beforeSecurity;

	private List<Filter>	afterSecurity;

	/**
	 * 获取安全过滤器之前的Filter List
	 * 
	 * @return not null
	 * @author 黄国庆 2011-4-21 下午02:38:57
	 */
	public List<Filter> getBeforeSecurity() {
		if (beforeSecurity == null) {
			setBeforeSecurity(new LinkedList<Filter>());
		}
		return beforeSecurity;
	}

	public void setBeforeSecurity(List<Filter> beforeSecurity) {
		this.beforeSecurity = beforeSecurity;
	}

	/**
	 * 获取安全过滤器之后的Filter List
	 * 
	 * @return not null
	 * @author 黄国庆 2011-4-21 下午02:40:00
	 */
	public List<Filter> getAfterSecurity() {
		if (afterSecurity == null) {
			setAfterSecurity(new LinkedList<Filter>());
		}
		return afterSecurity;
	}

	public void setAfterSecurity(List<Filter> afterSecurity) {
		this.afterSecurity = afterSecurity;
	}

	public void destroy() {
		for (Filter filter : getBeforeSecurity()) {
			filter.destroy();
		}
		for (Filter filter : getAfterSecurity()) {
			filter.destroy();
		}
	}
}

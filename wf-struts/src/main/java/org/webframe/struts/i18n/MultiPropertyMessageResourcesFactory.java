package org.webframe.struts.i18n;

import org.apache.struts.util.MessageResources;
import org.apache.struts.util.PropertyMessageResourcesFactory;


/**
 * MultiPropertyMessageResources工厂
 * 
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆</a>
 * @since 2012-5-10 下午4:38:10
 * @version
 */
public class MultiPropertyMessageResourcesFactory
			extends
				PropertyMessageResourcesFactory {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= -8720519411027587715L;

	@Override
	public MessageResources createResources(String config) {
		return new MultiPropertyMessageResources(this, config, this.returnNull);
	}
}

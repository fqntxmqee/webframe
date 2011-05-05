
package org.webframe.test;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.BeforeClass;

/**
 * @author <a href="mailto:guoqing.huang@berheley.com">黄国庆 </a>
 * @version $Id: codetemplates.xml,v 1.1 2009/09/07 08:48:12 Exp $ Create: 2011-3-28 下午08:51:08
 */
public class BaseHttpClientTests {

	protected static HttpClient	client	= new DefaultHttpClient();

	@BeforeClass
	public static void setUp() throws Exception {
	}
}


package org.webframe.web.valuelist;

import net.mlw.vlh.ValueList;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

/**
 * 用于freemarker模板解析时的对象包装类
 * 
 * @author <a href="mailto:guoqing.huang@berheley.com">huangguoqing</a>
 * @version $Id: codetemplates.xml,v 1.3 2009/05/05 02:30:07 huangguoqing Exp $ Create: 2011-1-19
 *          下午03:57:08
 */
public class ValueListFreemarkerObjectWrapper extends DefaultObjectWrapper {

	@Override
	public TemplateModel wrap(Object obj) throws TemplateModelException {
		if (obj instanceof ValueList) {
			return handleUnknownType(obj);
		}
		return super.wrap(obj);
	}
}


package org.webframe.web.springmvc.view.freemarker;

import org.webframe.web.page.ValueList;

import freemarker.template.DefaultObjectWrapper;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

/**
 * 用于freemarker模板解析时的对象包装类
 * 
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @since 2012-1-29 上午09:36:01
 * @version
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

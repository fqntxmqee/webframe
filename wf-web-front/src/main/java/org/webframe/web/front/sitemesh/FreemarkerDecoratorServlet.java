/*
 * com.berheley.bi.system
 * Created on 2011-12-12-下午12:27:01
 */

package org.webframe.web.front.sitemesh;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import freemarker.ext.beans.BeansWrapper;
import freemarker.template.SimpleHash;
import freemarker.template.Template;
import freemarker.template.TemplateModel;

/**
 * 封装freemarker
 * FreemarkerDecoratorServlet，添加ctx，statics变量；ctx为web容器上下文，statics提供freemarker模板中Java类静态方法访问支持
 * 
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @since 2012-2-8 下午10:37:04
 * @version
 */
public class FreemarkerDecoratorServlet extends com.opensymphony.module.sitemesh.freemarker.FreemarkerDecoratorServlet {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= -6943860446858680171L;

	@Override
	protected boolean preTemplateProcess(HttpServletRequest request, HttpServletResponse response, Template template, TemplateModel templateModel)
				throws ServletException, IOException {
		SimpleHash root = (SimpleHash) templateModel;
		root.put("ctx", request.getContextPath());
		root.put("statics", BeansWrapper.getDefaultInstance().getStaticModels());
		return super.preTemplateProcess(request, response, template, templateModel);
	}
}

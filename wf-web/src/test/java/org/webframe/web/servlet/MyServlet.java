/*
 * wf-web
 * Created on 2012-1-28-下午09:04:59
 */

package org.webframe.web.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 测试Servlet
 * 
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @version $Id: codetemplates.xml,v 1.1 2009/09/07 08:48:12 Exp $ Create: 2012-1-28 下午09:04:59
 */
public class MyServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 2588404891411121953L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.getWriter().println("MyServlet doGet Method!");
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.getWriter().println("MyServlet doPost Method!");
	}
}

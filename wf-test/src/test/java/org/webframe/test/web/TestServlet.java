/*
 * wf-test
 * Created on 2011-12-31-下午03:03:47
 */

package org.webframe.test.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @version $Id: codetemplates.xml,v 1.1 2009/09/07 08:48:12 Exp $ Create: 2011-12-31 下午03:03:47
 */
public class TestServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1468781528030859786L;

	public static final String	ROLE					= "role_";

	public static final String	LABEL					= "label_";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		System.out.println("SessionId: " + req.getSession());
		String role_ = req.getParameter(ROLE);
		String label_ = req.getParameter(LABEL);
		req.setAttribute(ROLE, role_);
		req.setAttribute(LABEL, label_);
		resp.getWriter().println("GET:" + role_ + label_);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		System.out.println("SessionId: " + req.getSession());
		String role_ = req.getParameter(ROLE);
		String label_ = req.getParameter(LABEL);
		resp.getWriter().println("POST:" + role_ + label_);
	}
}

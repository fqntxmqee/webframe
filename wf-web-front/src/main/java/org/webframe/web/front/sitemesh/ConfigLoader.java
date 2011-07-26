/*
 * wf-web-front
 * Created on 2011-7-26-上午08:10:47
 */

package org.webframe.web.front.sitemesh;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.Resource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

import com.opensymphony.module.sitemesh.Config;
import com.opensymphony.module.sitemesh.Decorator;
import com.opensymphony.module.sitemesh.mapper.DefaultDecorator;
import com.opensymphony.module.sitemesh.mapper.PathMapper;

/**
 * ConfigLoader
 * 
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @version $Id: codetemplates.xml,v 1.1 2009/09/07 08:48:12 Exp $ Create: 2011-7-26 上午08:10:47
 */
public class ConfigLoader {

	protected Log							log				= LogFactory.getLog(getClass());

	private Map<String, Decorator>	decorators		= null;

	private long							configLastModified;

	private Resource						configFile		= null;

	private String							configFileName	= null;

	private PathMapper					pathMapper		= null;

	private Config							config			= null;

	/** Create new ConfigLoader using supplied File. */
	public ConfigLoader(Resource configFile) throws ServletException {
		this.configFile = configFile;
		this.configFileName = configFile.getFilename();
		loadConfig();
	}

	/** Retrieve Decorator based on name specified in configuration file. */
	public Decorator getDecoratorByName(String name) throws ServletException {
		refresh();
		return (Decorator) decorators.get(name);
	}

	/** Get name of Decorator mapped to given path. */
	public String getMappedName(String path) throws ServletException {
		refresh();
		return pathMapper.get(path);
	}

	/** Load configuration from file. */
	private synchronized void loadConfig() throws ServletException {
		try {
			// Build a document from the file
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = null;
			if (configFile != null) {
				// Keep time we read the file to check if the file was modified
				configLastModified = configFile.lastModified();
				document = builder.parse(configFile.getInputStream());
			} else {
				document = builder.parse(config.getServletContext().getResourceAsStream(configFileName));
			}
			// Parse the configuration document
			parseConfig(document);
		} catch (ParserConfigurationException e) {
			throw new ServletException("Could not get XML parser", e);
		} catch (IOException e) {
			throw new ServletException("Could not read the config file: " + configFileName, e);
		} catch (SAXException e) {
			throw new ServletException("Could not parse the config file: " + configFileName, e);
		} catch (IllegalArgumentException e) {
			throw new ServletException("Could not find the config file: " + configFileName, e);
		}
	}

	/** Parse configuration from XML document. */
	private synchronized void parseConfig(Document document) {
		Element root = document.getDocumentElement();
		// get the default directory for the decorators
		String defaultDir = getAttribute(root, "defaultdir");
		if (defaultDir == null) defaultDir = getAttribute(root, "defaultDir");
		// Clear previous config
		pathMapper = new PathMapper();
		decorators = new HashMap<String, Decorator>();
		// Get decorators
		NodeList decoratorNodes = root.getElementsByTagName("decorator");
		Element decoratorElement = null;
		for (int i = 0; i < decoratorNodes.getLength(); i++) {
			String name = null, page = null, uriPath = null, role = null;
			// get the current decorator element
			decoratorElement = (Element) decoratorNodes.item(i);
			if (getAttribute(decoratorElement, "name") != null) {
				// The new format is used
				name = getAttribute(decoratorElement, "name");
				page = getAttribute(decoratorElement, "page");
				uriPath = getAttribute(decoratorElement, "webapp");
				role = getAttribute(decoratorElement, "role");
				// Append the defaultDir
				if (defaultDir != null && page != null && page.length() > 0 && !page.startsWith("/")) {
					if (page.charAt(0) == '/') page = defaultDir + page;
					else page = defaultDir + '/' + page;
				}
				// The uriPath must begin with a slash
				if (uriPath != null && uriPath.length() > 0) {
					if (uriPath.charAt(0) != '/') uriPath = '/' + uriPath;
				}
				// Get all <pattern>...</pattern> and <url-pattern>...</url-pattern> nodes and add a
				// mapping
				populatePathMapper(decoratorElement.getElementsByTagName("pattern"), role, name);
				populatePathMapper(decoratorElement.getElementsByTagName("url-pattern"), role, name);
			} else {
				// NOTE: Deprecated format
				name = getContainedText(decoratorNodes.item(i), "decorator-name");
				page = getContainedText(decoratorNodes.item(i), "resource");
				// We have this here because the use of jsp-file is deprecated, but we still want
				// it to work.
				if (page == null) page = getContainedText(decoratorNodes.item(i), "jsp-file");
			}
			Map<String, String> params = new HashMap<String, String>();
			NodeList paramNodes = decoratorElement.getElementsByTagName("init-param");
			for (int ii = 0; ii < paramNodes.getLength(); ii++) {
				String paramName = getContainedText(paramNodes.item(ii), "param-name");
				String paramValue = getContainedText(paramNodes.item(ii), "param-value");
				params.put(paramName, paramValue);
			}
			storeDecorator(new DefaultDecorator(name, page, uriPath, role, params));
		}
		// Get (deprecated format) decorator-mappings
		NodeList mappingNodes = root.getElementsByTagName("decorator-mapping");
		for (int i = 0; i < mappingNodes.getLength(); i++) {
			Element n = (Element) mappingNodes.item(i);
			String name = getContainedText(mappingNodes.item(i), "decorator-name");
			// Get all <url-pattern>...</url-pattern> nodes and add a mapping
			populatePathMapper(n.getElementsByTagName("url-pattern"), null, name);
		}
	}

	/**
	 * Extracts each URL pattern and adds it to the pathMapper map.
	 */
	private void populatePathMapper(NodeList patternNodes, String role, String name) {
		for (int j = 0; j < patternNodes.getLength(); j++) {
			Element p = (Element) patternNodes.item(j);
			Text patternText = (Text) p.getFirstChild();
			if (patternText != null) {
				String pattern = patternText.getData().trim();
				if (pattern != null) {
					if (role != null) {
						// concatenate name and role to allow more
						// than one decorator per role
						pathMapper.put(name + role, pattern);
					} else {
						pathMapper.put(name, pattern);
					}
				}
			}
		}
	}

	/** Override default behavior of element.getAttribute (returns the empty string) to return null. */
	private static String getAttribute(Element element, String name) {
		if (element != null && element.getAttribute(name) != null && element.getAttribute(name).trim() != "") {
			return element.getAttribute(name).trim();
		} else {
			return null;
		}
	}

	/**
	 * With a given parent XML Element, find the text contents of the child element with supplied
	 * name.
	 */
	private static String getContainedText(Node parent, String childTagName) {
		try {
			Node tag = ((Element) parent).getElementsByTagName(childTagName).item(0);
			String text = ((Text) tag.getFirstChild()).getData();
			return text;
		} catch (Exception e) {
			return null;
		}
	}

	/** Store Decorator in Map */
	private void storeDecorator(Decorator d) {
		if (d.getRole() != null) {
			decorators.put(d.getName() + d.getRole(), d);
		} else {
			decorators.put(d.getName(), d);
		}
	}

	/** Check if configuration file has been updated, and if so, reload. */
	private synchronized void refresh() throws ServletException {
		try {
			if (configFile != null && configLastModified != configFile.lastModified()) loadConfig();
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
	}
}

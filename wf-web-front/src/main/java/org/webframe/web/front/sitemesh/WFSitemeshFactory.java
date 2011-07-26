/*
 * wf-web-front
 * Created on 2011-7-25-下午04:00:12
 */

package org.webframe.web.front.sitemesh;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.Resource;
import org.springframework.util.ResourceUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.webframe.support.driver.resource.jar.JarResourceLoader;
import org.xml.sax.SAXException;

import com.opensymphony.module.sitemesh.Config;
import com.opensymphony.module.sitemesh.DecoratorMapper;
import com.opensymphony.module.sitemesh.PageParser;
import com.opensymphony.module.sitemesh.factory.BaseFactory;

/**
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @version $Id: codetemplates.xml,v 1.1 2009/09/07 08:48:12 Exp $ Create: 2011-7-25 下午04:00:12
 */
public class WFSitemeshFactory extends BaseFactory {

	protected Log			log				= LogFactory.getLog(getClass());

	static String			WEB_INF			= "/WEB-INF";

	String					configFileName	= WEB_INF + "/sitemesh.xml";

	Resource					configFile;

	long						configLastModified;

	Map<String, String>	configProps		= new HashMap<String, String>();

	String					excludesFileName;

	Resource					excludesFile;

	protected WFSitemeshFactory(Config config) {
		super(config);
		try {
			configFile = new JarResourceLoader(getClass()).getResource("classpath:sitemesh.xml");
		} catch (IOException e) {
			log.error(e);
		}
		loadConfig();
	}

	/** Refresh config before delegating to superclass. */
	public DecoratorMapper getDecoratorMapper() {
		refresh();
		return super.getDecoratorMapper();
	}

	/** Refresh config before delegating to superclass. */
	public PageParser getPageParser(String contentType) {
		refresh();
		return super.getPageParser(contentType);
	}

	/** Refresh config before delegating to superclass. */
	public boolean shouldParsePage(String contentType) {
		refresh();
		return super.shouldParsePage(contentType);
	}

	/**
	 * Returns <code>true</code> if the supplied path matches one of the exclude URLs specified in
	 * sitemesh.xml, otherwise returns <code>false</code>. This method refreshes the config before
	 * delgating to the superclass.
	 */
	public boolean isPathExcluded(String path) {
		refresh();
		return super.isPathExcluded(path);
	}

	/** Load configuration from file. */
	private synchronized void loadConfig() {
		try {
			// Load and parse the sitemesh.xml file
			Element root = loadSitemeshXML();
			NodeList sections = root.getChildNodes();
			// Loop through child elements of root node
			for (int i = 0; i < sections.getLength(); i++) {
				if (sections.item(i) instanceof Element) {
					Element curr = (Element) sections.item(i);
					NodeList children = curr.getChildNodes();
					if ("property".equalsIgnoreCase(curr.getTagName())) {
						String name = curr.getAttribute("name");
						String value = curr.getAttribute("value");
						if (!"".equals(name) && !"".equals(value)) {
							configProps.put("${" + name + "}", value);
						}
					} else if ("page-parsers".equalsIgnoreCase(curr.getTagName())) {
						// handle <page-parsers>
						loadPageParsers(children);
					} else if ("decorator-mappers".equalsIgnoreCase(curr.getTagName())) {
						// handle <decorator-mappers>
						loadDecoratorMappers(children);
					} else if ("excludes".equalsIgnoreCase(curr.getTagName())) {
						// handle <excludes>
						String fileName = replaceProperties(curr.getAttribute("file"));
						if (!"".equals(fileName)) {
							excludesFileName = fileName;
							loadExcludes();
						}
					}
				}
			}
		} catch (ParserConfigurationException e) {
			report("Could not get XML parser", e);
		} catch (IOException e) {
			report("Could not read config file : " + configFileName, e);
		} catch (SAXException e) {
			report("Could not parse config file : " + configFileName, e);
		}
	}

	private Element loadSitemeshXML() throws ParserConfigurationException, IOException, SAXException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		InputStream is = null;
		if (configFile == null) {
			is = config.getServletContext().getResourceAsStream(configFileName);
		} else if (configFile.exists()) {
			is = configFile.getInputStream();
		}
		if (is == null) { // load the default sitemesh configuration
			is = getClass().getClassLoader().getResourceAsStream(
				"com/opensymphony/module/sitemesh/factory/sitemesh-default.xml");
		}
		if (is == null) { // load the default sitemesh configuration using another classloader
			is = Thread.currentThread()
				.getContextClassLoader()
				.getResourceAsStream("com/opensymphony/module/sitemesh/factory/sitemesh-default.xml");
		}
		if (is == null) {
			throw new IllegalStateException("Cannot load default configuration from jar");
		}
		if (configFile != null) configLastModified = configFile.lastModified();
		Document doc = builder.parse(is);
		Element root = doc.getDocumentElement();
		// Verify root element
		if (!"sitemesh".equalsIgnoreCase(root.getTagName())) {
			report("Root element of sitemesh configuration file not <sitemesh>", null);
		}
		return root;
	}

	private void loadExcludes() throws ParserConfigurationException, IOException, SAXException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		InputStream is = null;
		if (excludesFile == null) {
			if (excludesFileName != null && excludesFileName.startsWith(WEB_INF)) {
				is = config.getServletContext().getResourceAsStream(excludesFileName);
			} else {
				is = ResourceUtils.getURL(excludesFileName).openStream();
			}
		} else if (excludesFile.exists()) {
			is = excludesFile.getInputStream();
		}
		if (is == null) {
			throw new IllegalStateException("Cannot load excludes configuration file from jar");
		}
		Document document = builder.parse(is);
		Element root = document.getDocumentElement();
		NodeList sections = root.getChildNodes();
		// Loop through child elements of root node looking for the <excludes> block
		for (int i = 0; i < sections.getLength(); i++) {
			if (sections.item(i) instanceof Element) {
				Element curr = (Element) sections.item(i);
				if ("excludes".equalsIgnoreCase(curr.getTagName())) {
					loadExcludeUrls(curr.getChildNodes());
				}
			}
		}
	}

	/** Loop through children of 'page-parsers' element and add all 'parser' mappings. */
	private void loadPageParsers(NodeList nodes) {
		clearParserMappings();
		for (int i = 0; i < nodes.getLength(); i++) {
			if (nodes.item(i) instanceof Element) {
				Element curr = (Element) nodes.item(i);
				if ("parser".equalsIgnoreCase(curr.getTagName())) {
					String className = curr.getAttribute("class");
					String contentType = curr.getAttribute("content-type");
					mapParser(contentType, className);
				}
			}
		}
	}

	private void loadDecoratorMappers(NodeList nodes) {
		clearDecoratorMappers();
		Properties emptyProps = new Properties();
		pushDecoratorMapper("com.opensymphony.module.sitemesh.mapper.NullDecoratorMapper", emptyProps);
		// note, this works from the bottom node up.
		for (int i = nodes.getLength() - 1; i > 0; i--) {
			if (nodes.item(i) instanceof Element) {
				Element curr = (Element) nodes.item(i);
				if ("mapper".equalsIgnoreCase(curr.getTagName())) {
					String className = curr.getAttribute("class");
					Properties props = new Properties();
					// build properties from <param> tags.
					NodeList children = curr.getChildNodes();
					for (int j = 0; j < children.getLength(); j++) {
						if (children.item(j) instanceof Element) {
							Element currC = (Element) children.item(j);
							if ("param".equalsIgnoreCase(currC.getTagName())) {
								String value = currC.getAttribute("value");
								props.put(currC.getAttribute("name"), replaceProperties(value));
							}
						}
					}
					// add mapper
					pushDecoratorMapper(className, props);
				}
			}
		}
		pushDecoratorMapper("com.opensymphony.module.sitemesh.mapper.InlineDecoratorMapper", emptyProps);
	}

	/**
	 * Reads in all the url patterns to exclude from decoration.
	 */
	private void loadExcludeUrls(NodeList nodes) {
		clearExcludeUrls();
		for (int i = 0; i < nodes.getLength(); i++) {
			if (nodes.item(i) instanceof Element) {
				Element p = (Element) nodes.item(i);
				if ("pattern".equalsIgnoreCase(p.getTagName()) || "url-pattern".equalsIgnoreCase(p.getTagName())) {
					Text patternText = (Text) p.getFirstChild();
					if (patternText != null) {
						String pattern = patternText.getData().trim();
						if (pattern != null) {
							addExcludeUrl(pattern);
						}
					}
				}
			}
		}
	}

	/** Check if configuration file has been modified, and if so reload it. */
	private void refresh() {
		try {
			if (configFile != null && configLastModified != configFile.lastModified()) loadConfig();
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
	}

	/**
	 * Replaces any properties that appear in the supplied string with their actual values
	 * 
	 * @param str the string to replace the properties in
	 * @return the same string but with any properties expanded out to their actual values
	 */
	private String replaceProperties(String str) {
		Set<Entry<String, String>> props = configProps.entrySet();
		for (Iterator<Entry<String, String>> it = props.iterator(); it.hasNext();) {
			Entry<String, String> entry = it.next();
			String key = entry.getKey();
			int idx;
			while ((idx = str.indexOf(key)) >= 0) {
				StringBuffer buf = new StringBuffer(100);
				buf.append(str.substring(0, idx));
				buf.append(entry.getValue());
				buf.append(str.substring(idx + key.length()));
				str = buf.toString();
			}
		}
		return str;
	}
}

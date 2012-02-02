
package org.webframe.easy.model.form;

/**
 * 页面视图元素类型
 * 
 * @author <a href="mailto:guoqing.huang@foxmail.com>huangguoqing</a>
 * @version $Id: codetemplates.xml,v 1.3 2009/05/05 02:30:07 huangguoqing Exp $ Create: 2010-12-29
 *          下午02:14:15
 */
public enum ViewElementType {
	HIDDEN("hidden", "hidden"), CHECKBOX("checkbox", "checkbox"), TEXT("text", "text"), TEXTAREA("textarea", "textarea"), PASSWORD(
				"password", "password"), RADIO("radio", "radio"), SELECT("select", "select"), FILE("file", "file"), IMAGE(
				"image", "image"), 未知类型("unknown", "unknown");

	private final String	value;

	private final String	label;

	private ViewElementType(String label, String value) {
		this.value = value;
		this.label = label;
	}

	public String getValue() {
		return value;
	}

	public String getLabel() {
		return label;
	}

	public static ViewElementType getName(String value) {
		for (ViewElementType type : ViewElementType.values()) {
			if (type.getValue().startsWith(value)) {
				return type;
			}
		}
		return ViewElementType.未知类型;
	}

	public String toString() {
		return this.getValue();
	}
}


package org.webframe.web.page.adapter;

import org.webframe.web.page.ValueListAdapter;
import org.webframe.web.page.ValueListInfo;

/**
 * Default abstract implementation of a ValueListAdapter.
 * 
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @version $Id: codetemplates.xml,v 1.1 2009/09/07 08:48:12 Exp $ Create: 2011-6-25 下午02:05:31
 */
public abstract class AbstractValueListAdapter implements ValueListAdapter {

	/** @see org.webframe.web.page.ValueListAdapter#getAdapterType() */
	private int			adapterType				= DO_NOTHING;

	/** The default number of entries per page. * */
	private int			defaultNumberPerPage	= Integer.MAX_VALUE;

	/** The default column to sort by. * */
	private String		defaultSortColumn;

	/** The default column to sort by. * */
	private Integer	defaultSortDirection	= ValueListInfo.ASCENDING;

	/**
	 * @see org.webframe.web.page.ValueListAdapter#getAdapterType()
	 */
	public int getAdapterType() {
		return adapterType;
	}

	/**
	 * Sets the adatper type.
	 * 
	 * @see org.webframe.web.page.ValueListAdapter#getAdapterType()
	 */
	public void setAdapterType(int adapterType) {
		this.adapterType = adapterType;
	}

	/**
	 * Sets the default number of entries per page.
	 * 
	 * @param defaultNumberPerPage The default number of entries per page.
	 */
	public void setDefaultNumberPerPage(int defaultNumberPerPage) {
		this.defaultNumberPerPage = defaultNumberPerPage;
	}

	/**
	 * Gets the default number of entries per page.
	 * 
	 * @return Returns the defaultNumberPerPage.
	 */
	public int getDefaultNumberPerPage() {
		return defaultNumberPerPage;
	}

	/**
	 * Sets the default sortColumn if none is present in the filters.
	 * 
	 * @param defaultSortColumn The defaultSortColumn to set.
	 */
	public void setDefaultSortColumn(String defaultSortColumn) {
		this.defaultSortColumn = defaultSortColumn;
	}

	/**
	 * @return Returns the defaultSortColumn.
	 */
	public String getDefaultSortColumn() {
		return defaultSortColumn;
	}

	/**
	 * Sets the default sort directon is none is supplied in the Map. Valid values: asc|desc
	 * 
	 * @param defaultSortDirection The defaultSortDirection to set.
	 */
	public void setDefaultSortDirection(String defaultSortDirection) {
		this.defaultSortDirection = "desc".equals(defaultSortDirection)
					? ValueListInfo.DESCENDING
					: ValueListInfo.ASCENDING;
	}

	/**
	 * Gets the sort direction defined as the default.
	 * 
	 * @return Returns the defaultSortDirection.
	 */
	public Integer getDefaultSortDirectionInteger() {
		return defaultSortDirection;
	}
}
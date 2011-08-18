/*
 * Created on 2.2.2005 azachar
 */

package org.webframe.web.page.adapter.util;

import org.webframe.web.page.ValueListInfo;

/**
 * This class is used in
 * <code>org.webframe.web.page.adapter.jdbc.objectWrapper.DefaultWrapperAdapter</code> Using this
 * wrapper you can replace record in valuelist with your record. Normally you select only ids and
 * valulist is populated with the wrapped objects, that has selected id. <p> <li><em> Using wrappers
 * is recommended in cases, when your hibernate hql is too complex, and you cannot controll all
 * composite joins. Then you would appreciate jdbc with the cunjuction of wrappers that allow you to
 * call third party managers, that return you wanted object. </em> </li> </p> <p> To pass additional
 * parameters to the object wrapper, adapter will always set actuall ValueListInfo before calling
 * <code>getWrappedRecord</code> method. </p> <p> The ObjectToBeWrapped is determined in adapter. It
 * this time it could be a <code>ResultSet</code> or <b>one column</b> of the record. </p>
 * 
 * @see org.webframe.web.page.adapter.jdbc.objectWrapper.DefaultWrapperAdapter#setWrapper(ObjectWrapper)
 * @see org.webframe.web.page.adapter.jdbc.objectWrapper.DefaultWrapperAdapter
 * @see org.webframe.web.page.adapter.util.ObjectValidator
 * @author Andrej Zachar
 * @version $Revision: 1.4 $ $Date: 2005/04/01 09:15:22 $
 */
public interface ObjectWrapper {

	/**
	 * This method wrap passed record,the type of this record you can specify in adpater's settings.
	 * Default adapter that use this interface is
	 * <code>org.webframe.web.page.adapter.jdbc.objectWrapper.DefaultWrapperAdapter</code>
	 * 
	 * @param objectToBeWrapped represent original object <ol> It could be a <li>whole
	 *           <code>ResultSet</code></li> or <li><b>one column</b> of the record from the
	 *           ResultSet.</li> </ol>
	 * @return object that represent the final "substituted" record in your valuelist.
	 * @see org.webframe.web.page.adapter.jdbc.objectWrapper.DefaultWrapperAdapter#setWrapResultSet(boolean)
	 */
	public Object getWrappedRecord(Object objectToBeWrapped);

	/**
	 * @param info ValueListInfo used to pass additional parameter to the wrapper from your
	 *           controler.
	 */
	public void setValueListInfo(ValueListInfo info);
}
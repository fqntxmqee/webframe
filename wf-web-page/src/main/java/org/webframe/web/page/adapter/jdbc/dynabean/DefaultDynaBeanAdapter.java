
package org.webframe.web.page.adapter.jdbc.dynabean;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.beanutils.BasicDynaBean;
import org.apache.commons.beanutils.BasicDynaClass;
import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.DynaProperty;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.webframe.web.page.ValueListInfo;
import org.webframe.web.page.adapter.jdbc.AbstractDynaJdbcAdapter;
import org.webframe.web.page.adapter.jdbc.dynabean.fix.ResultSetDynaClass;

/**
 * This ValueListAdapter returns a ValueList of DynaBean(s).
 * 
 * @see org.webframe.web.page.adapter.jdbc.AbstractDynaJdbcAdapter
 * @see org.webframe.web.page.adapter.jdbc.AbstractJdbcAdapter
 * @author Matthew L. Wilson, Andrej Zachar
 * @version $Revision: 1.6 $ $Date: 2005/08/19 16:06:29 $
 */
public class DefaultDynaBeanAdapter extends AbstractDynaJdbcAdapter {

	private static final Log	LOGGER	= LogFactory.getLog(DefaultDynaBeanAdapter.class);

	public List<Object> processResultSet(String name, ResultSet result, int numberPerPage, ValueListInfo info)
				throws SQLException {
		List<Object> list = new ArrayList<Object>();
		ResultSetDynaClass rsdc = new ResultSetDynaClass(result, false, isUseName());
		BasicDynaClass bdc = new BasicDynaClass(name, BasicDynaBean.class, rsdc.getDynaProperties());
		int rowIndex = 0;
		for (Iterator<DynaBean> rows = rsdc.iterator(); rows.hasNext() && rowIndex < numberPerPage; rowIndex++) {
			try {
				DynaBean oldRow = rows.next();
				DynaBean newRow = bdc.newInstance();
				DynaProperty[] properties = oldRow.getDynaClass().getDynaProperties();
				for (int i = 0, length = properties.length; i < length; i++) {
					String propertyName = properties[i].getName();
					Object value = oldRow.get(propertyName);
					newRow.set(propertyName, value);
				}
				list.add(newRow);
			} catch (Exception e) {
				LOGGER.error(e);
			}
		}
		return list;
	}
}
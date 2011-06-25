
package org.webframe.web.page.adapter.jdbc.spring;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;
import org.webframe.web.page.ValueListInfo;
import org.webframe.web.page.adapter.jdbc.AbstractJdbcAdapter;
import org.webframe.web.page.adapter.jdbc.spring.util.SpringConnectionCreator;

/**
 * org.webframe.web.page.adapter.jdbc.spring.SpringDaoValueListAdapter
 * 
 * @author Matthew L. Wilson
 * @version $Revision: 1.2 $ $Date: 2005/10/17 11:42:36 $
 */
public class SpringDaoValueListAdapter extends AbstractJdbcAdapter {

	private RowMapper<Object>	rowMapper;

	public SpringDaoValueListAdapter() {
		setConnectionCreator(new SpringConnectionCreator());
	}

	/**
	 * @see org.webframe.web.page.adapter.jdbc.AbstractJdbcAdapter#processResultSet(java.lang.String,
	 *      java.sql.ResultSet, int, org.webframe.web.page.ValueListInfo)
	 */
	public List<Object> processResultSet(String name, ResultSet result, int numberPerPage, ValueListInfo info)
				throws SQLException {
		List<Object> list = new ArrayList<Object>();
		for (int rowIndex = 0; result.next() && rowIndex < numberPerPage; rowIndex++) {
			list.add(rowMapper.mapRow(result, rowIndex));
		}
		return list;
	}

	/**
	 * @return Returns the rowMapper.
	 */
	public RowMapper<Object> getRowMapper() {
		return rowMapper;
	}

	/**
	 * @param rowMapper The rowMapper to set.
	 */
	public void setRowMapper(RowMapper<Object> rowMapper) {
		this.rowMapper = rowMapper;
	}
}
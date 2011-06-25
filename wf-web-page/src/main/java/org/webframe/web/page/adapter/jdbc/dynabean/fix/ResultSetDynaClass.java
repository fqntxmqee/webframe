
package org.webframe.web.page.adapter.jdbc.dynabean.fix;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.DynaClass;
import org.apache.commons.beanutils.DynaProperty;

/**
 * <p>Implementation of <code>DynaClass</code> for DynaBeans that wrap the <code>java.sql.Row</code>
 * objects of a <code>java.sql.ResultSet</code>. The normal usage pattern is something like:</p>
 * <pre> ResultSet rs = ...; ResultSetDynaClass rsdc = new ResultSetDynaClass(rs); Iterator rows =
 * rsdc.iterator(); while (rows.hasNext()) { DynaBean row = (DynaBean) rows.next(); ... process this
 * row ... } rs.close(); </pre> <p>Each column in the result set will be represented as a DynaBean
 * property of the corresponding name (optionally forced to lower case for portability).</p>
 * <p><strong>WARNING</strong> - Any {@link DynaBean} instance returned by this class, or from the
 * <code>Iterator</code> returned by the <code>iterator()</code> method, is directly linked to the
 * row that the underlying result set is currently positioned at. This has the following
 * implications:</p> <ul> <li>Once you retrieve a different {@link DynaBean} instance, you should no
 * longer use any previous instance.</li> <li>Changing the position of the underlying result set
 * will change the data that the {@link DynaBean} references.</li> <li>Once the underlying result
 * set is closed, the {@link DynaBean} instance may no longer be used.</li> </ul> <p>Any database
 * data that you wish to utilize outside the context of the current row of an open result set must
 * be copied. For example, you could use the following code to create standalone copies of the
 * information in a result set:</p> <pre> ArrayList results = new ArrayList(); // To hold copied
 * list ResultSetDynaClass rsdc = ...; DynaProperty properties[] = rsdc.getDynaProperties();
 * BasicDynaClass bdc = new BasicDynaClass("foo", BasicDynaBean.class, rsdc.getDynaProperties());
 * Iterator rows = rsdc.iterator(); while (rows.hasNext()) { DynaBean oldRow = (DynaBean)
 * rows.next(); DynaBean newRow = bdc.newInstance(); PropertyUtils.copyProperties(newRow, oldRow);
 * results.add(newRow); } </pre>
 * 
 * @author Craig R. McClanahan
 * @version $Revision: 1.3 $ $Date: 2005/08/19 16:06:29 $
 */
public class ResultSetDynaClass extends JDBCDynaClass implements DynaClass {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 5445508405862183299L;

	// ----------------------------------------------------------- Constructors
	/**
	 * <p>Construct a new ResultSetDynaClass for the specified <code>ResultSet</code>. The property
	 * names corresponding to column names in the result set will be lower cased.</p>
	 * 
	 * @param resultSet The result set to be wrapped
	 * @exception NullPointerException if <code>resultSet</code> is <code>null</code>
	 * @exception SQLException if the metadata for this result set cannot be introspected
	 */
	public ResultSetDynaClass(ResultSet resultSet) throws SQLException {
		this(resultSet, true, false);
	}

	/**
	 * <p>Construct a new ResultSetDynaClass for the specified <code>ResultSet</code>. The property
	 * names corresponding to the column names in the result set will be lower cased or not,
	 * depending on the specified <code>lowerCase</code> value.</p> <p><strong>WARNING</strong> - If
	 * you specify <code>false</code> for <code>lowerCase</code>, the returned property names will
	 * exactly match the column names returned by your JDBC driver. Because different drivers might
	 * return column names in different cases, the property names seen by your application will vary
	 * depending on which JDBC driver you are using.</p>
	 * 
	 * @param resultSet The result set to be wrapped
	 * @param lowerCase Should property names be lower cased?
	 * @param useName Should property names b ethe column name of the label name?
	 * @exception NullPointerException if <code>resultSet</code> is <code>null</code>
	 * @exception SQLException if the metadata for this result set cannot be introspected
	 */
	public ResultSetDynaClass(ResultSet resultSet, boolean lowerCase, boolean useName) throws SQLException {
		if (resultSet == null) {
			throw new NullPointerException();
		}
		this.resultSet = resultSet;
		this.lowerCase = lowerCase;
		this.useName = useName;
		introspect(resultSet);
	}

	// ----------------------------------------------------- Instance Variables
	/**
	 * Flag defining whether column names should be lower cased when converted to property names.
	 */
	protected boolean			lowerCase		= true;

	/**
	 * The set of dynamic properties that are part of this DynaClass.
	 */
	protected DynaProperty	properties[]	= null;

	/**
	 * <p>The <code>ResultSet</code> we are wrapping.</p>
	 */
	protected ResultSet		resultSet		= null;

	// --------------------------------------------------------- Public Methods
	/**
	 * <p>Return an <code>Iterator</code> of {@link DynaBean} instances for each row of the wrapped
	 * <code>ResultSet</code>, in "forward" order. Unless the underlying result set supports
	 * scrolling, this method should be called only once.</p>
	 */
	public Iterator<DynaBean> iterator() {
		return new ResultSetIterator(this);
	}

	// -------------------------------------------------------- Package Methods
	/**
	 * <p>Return the result set we are wrapping.</p>
	 */
	ResultSet getResultSet() {
		return (this.resultSet);
	}

	// ------------------------------------------------------ Protected Methods
	/**
	 * <p>Loads the class of the given name which by default uses the class loader used to load this
	 * library. Dervations of this class could implement alternative class loading policies such as
	 * using custom ClassLoader or using the Threads's context class loader etc. </p>
	 */
	protected Class<?> loadClass(String className) throws SQLException {
		try {
			return getClass().getClassLoader().loadClass(className);
		} catch (Exception e) {
			throw new SQLException("Cannot load column class '" + className + "': " + e);
		}
	}
}

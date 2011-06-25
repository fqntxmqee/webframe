
package org.webframe.web.page.adapter.jdbc.objectWrapper;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;

import org.webframe.web.page.adapter.util.ObjectValidator;

/**
 * Add validator support, that validate ResultSet, if is not valid, move to next or previous result
 * set. This implementation ensures that the validation method is invoked only once per object.
 * 
 * @author Andrej Zachar
 * @version $Revision: 1.8 $ $Date: 2005/09/16 12:17:07 $
 */
public class ResultSetDecorator extends ResultSetBridge {

	private static final int	INITIAL_CAPACITY	= 100;

	private ResultSet				_resultSet;

	private ObjectValidator		_validator;

	private int						_currentRow;

	private int[]					_index;

	private int						_size;

	private boolean				_isComplete;

	/**
	 * Default constructor
	 * 
	 * @param set
	 * @param objectwrapper
	 */
	public ResultSetDecorator(ResultSet rs, ObjectValidator objectValidator) {
		super();
		setValidator(objectValidator);
		setResultSet(rs); // the method calls reset() as well
	}

	/**
	 * @see java.sql.ResultSet#beforeFirst()
	 */
	public void beforeFirst() throws SQLException {
		if (!_resultSet.isBeforeFirst()) {
			_resultSet.beforeFirst();
		}
		_currentRow = 0;
	}

	/**
	 * @see java.sql.ResultSet#isBeforeFirst()
	 */
	public boolean isBeforeFirst() throws SQLException {
		return (_currentRow == 0);
	}

	/**
	 * @see java.sql.ResultSet#first()
	 */
	public boolean first() throws SQLException {
		return move(1);
	}

	/**
	 * @see java.sql.ResultSet#isFirst()
	 */
	public boolean isFirst() throws SQLException {
		return (_currentRow == 1);
	}

	/**
	 * @see java.sql.ResultSet#last()
	 */
	public boolean last() throws SQLException {
		if (_isComplete) {
			// the end has been already reached
			return move(_size);
		} else {
			afterLast();
			return previous();
		}
	}

	/**
	 * @see java.sql.ResultSet#isLast()
	 */
	public boolean isLast() throws SQLException {
		if (_isComplete) {
			// the end has been already reached
			return (_currentRow == _size);
		} else {
			boolean result = next();
			previous();
			return result;
		}
	}

	/**
	 * @see java.sql.ResultSet#afterLast()
	 */
	public void afterLast() throws SQLException {
		if (_isComplete) {
			// the end has been already reached
			_resultSet.afterLast();
			_currentRow = _size + 1;
		} else {
			lastKnown();
			while (nextValid());
		}
	}

	/**
	 * @see java.sql.ResultSet#isAfterLast()
	 */
	public boolean isAfterLast() throws SQLException {
		return _isComplete && (_currentRow > _size);
	}

	/**
	 * @see java.sql.ResultSet#next()
	 */
	public boolean next() throws SQLException {
		return move(_currentRow + 1);
	}

	/**
	 * @see java.sql.ResultSet#previous()
	 */
	public boolean previous() throws SQLException {
		return move(_currentRow - 1);
	}

	/**
	 * @see java.sql.ResultSet#relative(int)
	 */
	public boolean relative(int rows) throws SQLException {
		if (rows == 0) {
			return _resultSet.relative(0);
		}
		return move(_currentRow + rows);
	}

	/**
	 * @see java.sql.ResultSet#absolute(int)
	 */
	public boolean absolute(int row) throws SQLException {
		if (row > 0) {
			return move(row);
		} else if (row < 0) {
			if (!_isComplete) {
				// ensure the end has been already reached
				afterLast();
			}
			return move(_size + row + 1);
		} else {
			// row == 0
			beforeFirst();
			return false;
		}
	}

	private boolean move(int row) throws SQLException {
		if (row > 0) {
			if (row <= _size) {
				_currentRow = row;
				int absolute = _index[_currentRow - 1];
				if (absolute != _resultSet.getRow()) {
					return _resultSet.absolute(absolute);
				} else {
					return true;
				}
			} else {
				if (_isComplete) {
					afterLast();
					return false;
				} else {
					lastKnown();
					boolean result = true;
					while (result && (_currentRow < row)) {
						result = nextValid();
					}
					return result;
				}
			}
		} else {
			beforeFirst();
			return false;
		}
	}

	private void lastKnown() throws SQLException {
		if (_size > 0) {
			_currentRow = _size;
			int absolute = _index[_currentRow - 1];
			if (absolute != _resultSet.getRow()) {
				_resultSet.absolute(absolute);
			}
		} else {
			beforeFirst();
		}
	}

	private boolean nextValid() throws SQLException {
		boolean result;
		do {
			result = _resultSet.next();
		} while (result && !_validator.isAcceptable(_resultSet.getObject(1)));
		_currentRow++;
		if (result) {
			_size = _currentRow;
			ensureCapacity(_size);
			_index[_currentRow - 1] = _resultSet.getRow();
		} else {
			_isComplete = true;
		}
		return result;
	}

	private void ensureCapacity(int minCapacity) {
		int oldCapacity = _index.length;
		if (minCapacity > oldCapacity) {
			int[] oldData = _index;
			int newCapacity = (oldCapacity * 3) / 2 + 1;
			if (newCapacity < minCapacity) {
				newCapacity = minCapacity;
			}
			_index = new int[newCapacity];
			System.arraycopy(oldData, 0, _index, 0, oldCapacity);
		}
	}

	/* ###### delegated methods ###### */
	/**
	 * @see java.sql.ResultSet#getConcurrency()
	 */
	public int getConcurrency() throws SQLException {
		return _resultSet.getConcurrency();
	}

	/**
	 * @see java.sql.ResultSet#getFetchDirection()
	 */
	public int getFetchDirection() throws SQLException {
		return _resultSet.getFetchDirection();
	}

	/**
	 * @see java.sql.ResultSet#getFetchSize()
	 */
	public int getFetchSize() throws SQLException {
		return _resultSet.getFetchSize();
	}

	/**
	 * @see java.sql.ResultSet#getRow()
	 */
	public int getRow() throws SQLException {
		return _currentRow;
	}

	/**
	 * @see java.sql.ResultSet#getType()
	 */
	public int getType() throws SQLException {
		return _resultSet.getType();
	}

	/**
	 * @see java.sql.ResultSet#cancelRowUpdates()
	 */
	public void cancelRowUpdates() throws SQLException {
		_resultSet.cancelRowUpdates();
	}

	/**
	 * @see java.sql.ResultSet#clearWarnings()
	 */
	public void clearWarnings() throws SQLException {
		_resultSet.clearWarnings();
	}

	/**
	 * @see java.sql.ResultSet#close()
	 */
	public void close() throws SQLException {
		_resultSet.close();
	}

	/**
	 * @see java.sql.ResultSet#deleteRow()
	 */
	public void deleteRow() throws SQLException {
		_resultSet.deleteRow();
	}

	/**
	 * @see java.sql.ResultSet#insertRow()
	 */
	public void insertRow() throws SQLException {
		_resultSet.insertRow();
	}

	/**
	 * @see java.sql.ResultSet#moveToCurrentRow()
	 */
	public void moveToCurrentRow() throws SQLException {
		_resultSet.moveToCurrentRow();
	}

	/**
	 * @see java.sql.ResultSet#moveToInsertRow()
	 */
	public void moveToInsertRow() throws SQLException {
		_resultSet.moveToInsertRow();
	}

	/**
	 * @see java.sql.ResultSet#refreshRow()
	 */
	public void refreshRow() throws SQLException {
		_resultSet.refreshRow();
	}

	/**
	 * @see java.sql.ResultSet#updateRow()
	 */
	public void updateRow() throws SQLException {
		_resultSet.updateRow();
	}

	/**
	 * @see java.sql.ResultSet#rowDeleted()
	 */
	public boolean rowDeleted() throws SQLException {
		return _resultSet.rowDeleted();
	}

	/**
	 * @see java.sql.ResultSet#rowInserted()
	 */
	public boolean rowInserted() throws SQLException {
		return _resultSet.rowInserted();
	}

	/**
	 * @see java.sql.ResultSet#rowUpdated()
	 */
	public boolean rowUpdated() throws SQLException {
		return _resultSet.rowUpdated();
	}

	/**
	 * @see java.sql.ResultSet#wasNull()
	 */
	public boolean wasNull() throws SQLException {
		return _resultSet.wasNull();
	}

	/**
	 * @see java.sql.ResultSet#getByte(int)
	 */
	public byte getByte(int columnIndex) throws SQLException {
		return _resultSet.getByte(columnIndex);
	}

	/**
	 * @see java.sql.ResultSet#getDouble(int)
	 */
	public double getDouble(int columnIndex) throws SQLException {
		return _resultSet.getDouble(columnIndex);
	}

	/**
	 * @see java.sql.ResultSet#getFloat(int)
	 */
	public float getFloat(int columnIndex) throws SQLException {
		return _resultSet.getFloat(columnIndex);
	}

	/**
	 * @see java.sql.ResultSet#getInt(int)
	 */
	public int getInt(int columnIndex) throws SQLException {
		return _resultSet.getInt(columnIndex);
	}

	/**
	 * @see java.sql.ResultSet#getLong(int)
	 */
	public long getLong(int columnIndex) throws SQLException {
		return _resultSet.getLong(columnIndex);
	}

	/**
	 * @see java.sql.ResultSet#getShort(int)
	 */
	public short getShort(int columnIndex) throws SQLException {
		return _resultSet.getShort(columnIndex);
	}

	/**
	 * @see java.sql.ResultSet#setFetchDirection(int)
	 */
	public void setFetchDirection(int direction) throws SQLException {
		_resultSet.setFetchDirection(direction);
	}

	/**
	 * @see java.sql.ResultSet#setFetchSize(int)
	 */
	public void setFetchSize(int rows) throws SQLException {
		_resultSet.setFetchSize(rows);
	}

	/**
	 * @see java.sql.ResultSet#updateNull(int)
	 */
	public void updateNull(int columnIndex) throws SQLException {
		_resultSet.updateNull(columnIndex);
	}

	/**
	 * @see java.sql.ResultSet#getBoolean(int)
	 */
	public boolean getBoolean(int columnIndex) throws SQLException {
		return _resultSet.getBoolean(columnIndex);
	}

	/**
	 * @see java.sql.ResultSet#getBytes(int)
	 */
	public byte[] getBytes(int columnIndex) throws SQLException {
		return _resultSet.getBytes(columnIndex);
	}

	/**
	 * @see java.sql.ResultSet#updateByte(int, byte)
	 */
	public void updateByte(int columnIndex, byte x) throws SQLException {
		_resultSet.updateByte(columnIndex, x);
	}

	/**
	 * @see java.sql.ResultSet#updateDouble(int, double)
	 */
	public void updateDouble(int columnIndex, double x) throws SQLException {
		_resultSet.updateDouble(columnIndex, x);
	}

	/**
	 * @see java.sql.ResultSet#updateFloat(int, float)
	 */
	public void updateFloat(int columnIndex, float x) throws SQLException {
		_resultSet.updateFloat(columnIndex, x);
	}

	/**
	 * @see java.sql.ResultSet#updateInt(int, int)
	 */
	public void updateInt(int columnIndex, int x) throws SQLException {
		_resultSet.updateInt(columnIndex, x);
	}

	/**
	 * @see java.sql.ResultSet#updateLong(int, long)
	 */
	public void updateLong(int columnIndex, long x) throws SQLException {
		_resultSet.updateLong(columnIndex, x);
	}

	/**
	 * @see java.sql.ResultSet#updateShort(int, short)
	 */
	public void updateShort(int columnIndex, short x) throws SQLException {
		_resultSet.updateShort(columnIndex, x);
	}

	/**
	 * @see java.sql.ResultSet#updateBoolean(int, boolean)
	 */
	public void updateBoolean(int columnIndex, boolean x) throws SQLException {
		_resultSet.updateBoolean(columnIndex, x);
	}

	/**
	 * @see java.sql.ResultSet#updateBytes(int, byte[])
	 */
	public void updateBytes(int columnIndex, byte[] x) throws SQLException {
		_resultSet.updateBytes(columnIndex, x);
	}

	/**
	 * @see java.sql.ResultSet#getAsciiStream(int)
	 */
	public InputStream getAsciiStream(int columnIndex) throws SQLException {
		return _resultSet.getAsciiStream(columnIndex);
	}

	/**
	 * @see java.sql.ResultSet#getBinaryStream(int)
	 */
	public InputStream getBinaryStream(int columnIndex) throws SQLException {
		return _resultSet.getBinaryStream(columnIndex);
	}

	/**
	 * @see java.sql.ResultSet#getUnicodeStream(int)
	 * @deprecated
	 */
	public InputStream getUnicodeStream(int columnIndex) throws SQLException {
		return _resultSet.getUnicodeStream(columnIndex);
	}

	/**
	 * @see java.sql.ResultSet#updateAsciiStream(int, java.io.InputStream, int)
	 */
	public void updateAsciiStream(int columnIndex, InputStream x, int length) throws SQLException {
		_resultSet.updateAsciiStream(columnIndex, x, length);
	}

	/**
	 * @see java.sql.ResultSet#updateBinaryStream(int, java.io.InputStream, int)
	 */
	public void updateBinaryStream(int columnIndex, InputStream x, int length) throws SQLException {
		_resultSet.updateBinaryStream(columnIndex, x, length);
	}

	/**
	 * @see java.sql.ResultSet#getCharacterStream(int)
	 */
	public Reader getCharacterStream(int columnIndex) throws SQLException {
		return _resultSet.getCharacterStream(columnIndex);
	}

	/**
	 * @see java.sql.ResultSet#updateCharacterStream(int, java.io.Reader, int)
	 */
	public void updateCharacterStream(int columnIndex, Reader x, int length) throws SQLException {
		_resultSet.updateCharacterStream(columnIndex, x, length);
	}

	/**
	 * @see java.sql.ResultSet#getObject(int)
	 */
	public Object getObject(int columnIndex) throws SQLException {
		return _resultSet.getObject(columnIndex);
	}

	/**
	 * @see java.sql.ResultSet#updateObject(int, java.lang.Object)
	 */
	public void updateObject(int columnIndex, Object x) throws SQLException {
		_resultSet.updateObject(columnIndex, x);
	}

	/**
	 * @see java.sql.ResultSet#updateObject(int, java.lang.Object, int)
	 */
	public void updateObject(int columnIndex, Object x, int scale) throws SQLException {
		_resultSet.updateObject(columnIndex, x, scale);
	}

	/**
	 * @see java.sql.ResultSet#getCursorName()
	 */
	public String getCursorName() throws SQLException {
		return _resultSet.getCursorName();
	}

	/**
	 * @see java.sql.ResultSet#getString(int)
	 */
	public String getString(int columnIndex) throws SQLException {
		return _resultSet.getString(columnIndex);
	}

	/**
	 * @see java.sql.ResultSet#updateString(int, java.lang.String)
	 */
	public void updateString(int columnIndex, String x) throws SQLException {
		_resultSet.updateString(columnIndex, x);
	}

	/**
	 * @see java.sql.ResultSet#getByte(java.lang.String)
	 */
	public byte getByte(String columnName) throws SQLException {
		return _resultSet.getByte(columnName);
	}

	/**
	 * @see java.sql.ResultSet#getDouble(java.lang.String)
	 */
	public double getDouble(String columnName) throws SQLException {
		return _resultSet.getDouble(columnName);
	}

	/**
	 * @see java.sql.ResultSet#getFloat(java.lang.String)
	 */
	public float getFloat(String columnName) throws SQLException {
		return _resultSet.getFloat(columnName);
	}

	/**
	 * @see java.sql.ResultSet#findColumn(java.lang.String)
	 */
	public int findColumn(String columnName) throws SQLException {
		return _resultSet.findColumn(columnName);
	}

	/**
	 * @see java.sql.ResultSet#getInt(java.lang.String)
	 */
	public int getInt(String columnName) throws SQLException {
		return _resultSet.getInt(columnName);
	}

	/**
	 * @see java.sql.ResultSet#getLong(java.lang.String)
	 */
	public long getLong(String columnName) throws SQLException {
		return _resultSet.getLong(columnName);
	}

	/**
	 * @see java.sql.ResultSet#getShort(java.lang.String)
	 */
	public short getShort(String columnName) throws SQLException {
		return _resultSet.getShort(columnName);
	}

	/**
	 * @see java.sql.ResultSet#updateNull(java.lang.String)
	 */
	public void updateNull(String columnName) throws SQLException {
		_resultSet.updateNull(columnName);
	}

	/**
	 * @see java.sql.ResultSet#getBoolean(java.lang.String)
	 */
	public boolean getBoolean(String columnName) throws SQLException {
		return _resultSet.getBoolean(columnName);
	}

	/**
	 * @see java.sql.ResultSet#getBytes(java.lang.String)
	 */
	public byte[] getBytes(String columnName) throws SQLException {
		return _resultSet.getBytes(columnName);
	}

	/**
	 * @see java.sql.ResultSet#updateByte(java.lang.String, byte)
	 */
	public void updateByte(String columnName, byte x) throws SQLException {
		_resultSet.updateByte(columnName, x);
	}

	/**
	 * @see java.sql.ResultSet#updateDouble(java.lang.String, double)
	 */
	public void updateDouble(String columnName, double x) throws SQLException {
		_resultSet.updateDouble(columnName, x);
	}

	/**
	 * @see java.sql.ResultSet#updateFloat(java.lang.String, float)
	 */
	public void updateFloat(String columnName, float x) throws SQLException {
		_resultSet.updateFloat(columnName, x);
	}

	/**
	 * @see java.sql.ResultSet#updateInt(java.lang.String, int)
	 */
	public void updateInt(String columnName, int x) throws SQLException {
		_resultSet.updateInt(columnName, x);
	}

	/**
	 * @see java.sql.ResultSet#updateLong(java.lang.String, long)
	 */
	public void updateLong(String columnName, long x) throws SQLException {
		_resultSet.updateLong(columnName, x);
	}

	/**
	 * @see java.sql.ResultSet#updateShort(java.lang.String, short)
	 */
	public void updateShort(String columnName, short x) throws SQLException {
		_resultSet.updateShort(columnName, x);
	}

	/**
	 * @see java.sql.ResultSet#updateBoolean(java.lang.String, boolean)
	 */
	public void updateBoolean(String columnName, boolean x) throws SQLException {
		_resultSet.updateBoolean(columnName, x);
	}

	/**
	 * @see java.sql.ResultSet#updateBytes(java.lang.String, byte[])
	 */
	public void updateBytes(String columnName, byte[] x) throws SQLException {
		_resultSet.updateBytes(columnName, x);
	}

	/**
	 * @see java.sql.ResultSet#getBigDecimal(int)
	 */
	public BigDecimal getBigDecimal(int columnIndex) throws SQLException {
		return _resultSet.getBigDecimal(columnIndex);
	}

	/**
	 * @see java.sql.ResultSet#getBigDecimal(int, int)
	 * @deprecated
	 */
	public BigDecimal getBigDecimal(int columnIndex, int scale) throws SQLException {
		return _resultSet.getBigDecimal(columnIndex, scale);
	}

	/**
	 * @see java.sql.ResultSet#updateBigDecimal(int, java.math.BigDecimal)
	 */
	public void updateBigDecimal(int columnIndex, BigDecimal x) throws SQLException {
		_resultSet.updateBigDecimal(columnIndex, x);
	}

	/**
	 * @see java.sql.ResultSet#getURL(int)
	 */
	public URL getURL(int columnIndex) throws SQLException {
		return _resultSet.getURL(columnIndex);
	}

	/**
	 * @see java.sql.ResultSet#getArray(int)
	 */
	public Array getArray(int i) throws SQLException {
		return _resultSet.getArray(i);
	}

	/**
	 * @see java.sql.ResultSet#updateArray(int, java.sql.Array)
	 */
	public void updateArray(int columnIndex, Array x) throws SQLException {
		_resultSet.updateArray(columnIndex, x);
	}

	/**
	 * @see java.sql.ResultSet#getBlob(int)
	 */
	public Blob getBlob(int i) throws SQLException {
		return _resultSet.getBlob(i);
	}

	/**
	 * @see java.sql.ResultSet#updateBlob(int, java.sql.Blob)
	 */
	public void updateBlob(int columnIndex, Blob x) throws SQLException {
		_resultSet.updateBlob(columnIndex, x);
	}

	/**
	 * @see java.sql.ResultSet#getClob(int)
	 */
	public Clob getClob(int i) throws SQLException {
		return _resultSet.getClob(i);
	}

	/**
	 * @see java.sql.ResultSet#updateClob(int, java.sql.Clob)
	 */
	public void updateClob(int columnIndex, Clob x) throws SQLException {
		_resultSet.updateClob(columnIndex, x);
	}

	/**
	 * @see java.sql.ResultSet#getDate(int)
	 */
	public Date getDate(int columnIndex) throws SQLException {
		return _resultSet.getDate(columnIndex);
	}

	/**
	 * @see java.sql.ResultSet#updateDate(int, java.sql.Date)
	 */
	public void updateDate(int columnIndex, Date x) throws SQLException {
		_resultSet.updateDate(columnIndex, x);
	}

	/**
	 * @see java.sql.ResultSet#getRef(int)
	 */
	public Ref getRef(int i) throws SQLException {
		return _resultSet.getRef(i);
	}

	/**
	 * @see java.sql.ResultSet#updateRef(int, java.sql.Ref)
	 */
	public void updateRef(int columnIndex, Ref x) throws SQLException {
		_resultSet.updateRef(columnIndex, x);
	}

	/**
	 * @see java.sql.ResultSet#getMetaData()
	 */
	public ResultSetMetaData getMetaData() throws SQLException {
		return _resultSet.getMetaData();
	}

	/**
	 * @see java.sql.ResultSet#getWarnings()
	 */
	public SQLWarning getWarnings() throws SQLException {
		return _resultSet.getWarnings();
	}

	/**
	 * @see java.sql.ResultSet#getStatement()
	 */
	public Statement getStatement() throws SQLException {
		return _resultSet.getStatement();
	}

	/**
	 * @see java.sql.ResultSet#getTime(int)
	 */
	public Time getTime(int columnIndex) throws SQLException {
		return _resultSet.getTime(columnIndex);
	}

	/**
	 * @see java.sql.ResultSet#updateTime(int, java.sql.Time)
	 */
	public void updateTime(int columnIndex, Time x) throws SQLException {
		_resultSet.updateTime(columnIndex, x);
	}

	/**
	 * @see java.sql.ResultSet#getTimestamp(int)
	 */
	public Timestamp getTimestamp(int columnIndex) throws SQLException {
		return _resultSet.getTimestamp(columnIndex);
	}

	/**
	 * @see java.sql.ResultSet#updateTimestamp(int, java.sql.Timestamp)
	 */
	public void updateTimestamp(int columnIndex, Timestamp x) throws SQLException {
		_resultSet.updateTimestamp(columnIndex, x);
	}

	/**
	 * @see java.sql.ResultSet#getAsciiStream(java.lang.String)
	 */
	public InputStream getAsciiStream(String columnName) throws SQLException {
		return _resultSet.getAsciiStream(columnName);
	}

	/**
	 * @see java.sql.ResultSet#getBinaryStream(java.lang.String)
	 */
	public InputStream getBinaryStream(String columnName) throws SQLException {
		return _resultSet.getBinaryStream(columnName);
	}

	/**
	 * @see java.sql.ResultSet#getUnicodeStream(java.lang.String)
	 * @deprecated
	 */
	public InputStream getUnicodeStream(String columnName) throws SQLException {
		return _resultSet.getUnicodeStream(columnName);
	}

	/**
	 * @see java.sql.ResultSet#updateAsciiStream(java.lang.String, java.io.InputStream, int)
	 */
	public void updateAsciiStream(String columnName, InputStream x, int length) throws SQLException {
		_resultSet.updateAsciiStream(columnName, x, length);
	}

	/**
	 * @see java.sql.ResultSet#updateBinaryStream(java.lang.String, java.io.InputStream, int)
	 */
	public void updateBinaryStream(String columnName, InputStream x, int length) throws SQLException {
		_resultSet.updateBinaryStream(columnName, x, length);
	}

	/**
	 * @see java.sql.ResultSet#getCharacterStream(java.lang.String)
	 */
	public Reader getCharacterStream(String columnName) throws SQLException {
		return _resultSet.getCharacterStream(columnName);
	}

	/**
	 * @see java.sql.ResultSet#updateCharacterStream(java.lang.String, java.io.Reader, int)
	 */
	public void updateCharacterStream(String columnName, Reader reader, int length) throws SQLException {
		_resultSet.updateCharacterStream(columnName, reader, length);
	}

	/**
	 * @see java.sql.ResultSet#getObject(java.lang.String)
	 */
	public Object getObject(String columnName) throws SQLException {
		return _resultSet.getObject(columnName);
	}

	/**
	 * @see java.sql.ResultSet#updateObject(java.lang.String, java.lang.Object)
	 */
	public void updateObject(String columnName, Object x) throws SQLException {
		_resultSet.updateObject(columnName, x);
	}

	/**
	 * @see java.sql.ResultSet#updateObject(java.lang.String, java.lang.Object, int)
	 */
	public void updateObject(String columnName, Object x, int scale) throws SQLException {
		_resultSet.updateObject(columnName, x, scale);
	}

	/**
	 * @see java.sql.ResultSet#getObject(int, java.util.Map)
	 */
	public Object getObject(int i, Map<String, Class<?>> map) throws SQLException {
		return _resultSet.getObject(i, map);
	}

	/**
	 * @see java.sql.ResultSet#getString(java.lang.String)
	 */
	public String getString(String columnName) throws SQLException {
		return _resultSet.getString(columnName);
	}

	/**
	 * @see java.sql.ResultSet#updateString(java.lang.String, java.lang.String)
	 */
	public void updateString(String columnName, String x) throws SQLException {
		_resultSet.updateString(columnName, x);
	}

	/**
	 * @see java.sql.ResultSet#getBigDecimal(java.lang.String)
	 */
	public BigDecimal getBigDecimal(String columnName) throws SQLException {
		return _resultSet.getBigDecimal(columnName);
	}

	/**
	 * @see java.sql.ResultSet#getBigDecimal(java.lang.String, int)
	 * @deprecated
	 */
	public BigDecimal getBigDecimal(String columnName, int scale) throws SQLException {
		return _resultSet.getBigDecimal(columnName, scale);
	}

	/**
	 * @see java.sql.ResultSet#updateBigDecimal(java.lang.String, java.math.BigDecimal)
	 */
	public void updateBigDecimal(String columnName, BigDecimal x) throws SQLException {
		_resultSet.updateBigDecimal(columnName, x);
	}

	/**
	 * @see java.sql.ResultSet#getURL(java.lang.String)
	 */
	public URL getURL(String columnName) throws SQLException {
		return _resultSet.getURL(columnName);
	}

	/**
	 * @see java.sql.ResultSet#getArray(java.lang.String)
	 */
	public Array getArray(String colName) throws SQLException {
		return _resultSet.getArray(colName);
	}

	/**
	 * @see java.sql.ResultSet#updateArray(java.lang.String, java.sql.Array)
	 */
	public void updateArray(String columnName, Array x) throws SQLException {
		_resultSet.updateArray(columnName, x);
	}

	/**
	 * @see java.sql.ResultSet#getBlob(java.lang.String)
	 */
	public Blob getBlob(String colName) throws SQLException {
		return _resultSet.getBlob(colName);
	}

	/**
	 * @see java.sql.ResultSet#updateBlob(java.lang.String, java.sql.Blob)
	 */
	public void updateBlob(String columnName, Blob x) throws SQLException {
		_resultSet.updateBlob(columnName, x);
	}

	/**
	 * @see java.sql.ResultSet#getClob(java.lang.String)
	 */
	public Clob getClob(String colName) throws SQLException {
		return _resultSet.getClob(colName);
	}

	/**
	 * @see java.sql.ResultSet#updateClob(java.lang.String, java.sql.Clob)
	 */
	public void updateClob(String columnName, Clob x) throws SQLException {
		_resultSet.updateClob(columnName, x);
	}

	/**
	 * @see java.sql.ResultSet#getDate(java.lang.String)
	 */
	public Date getDate(String columnName) throws SQLException {
		return _resultSet.getDate(columnName);
	}

	/**
	 * @see java.sql.ResultSet#updateDate(java.lang.String, java.sql.Date)
	 */
	public void updateDate(String columnName, Date x) throws SQLException {
		_resultSet.updateDate(columnName, x);
	}

	/**
	 * @see java.sql.ResultSet#getDate(int, java.util.Calendar)
	 */
	public Date getDate(int columnIndex, Calendar cal) throws SQLException {
		return _resultSet.getDate(columnIndex, cal);
	}

	/**
	 * @see java.sql.ResultSet#getRef(java.lang.String)
	 */
	public Ref getRef(String colName) throws SQLException {
		return _resultSet.getRef(colName);
	}

	/**
	 * @see java.sql.ResultSet#updateRef(java.lang.String, java.sql.Ref)
	 */
	public void updateRef(String columnName, Ref x) throws SQLException {
		_resultSet.updateRef(columnName, x);
	}

	/**
	 * @see java.sql.ResultSet#getTime(java.lang.String)
	 */
	public Time getTime(String columnName) throws SQLException {
		return _resultSet.getTime(columnName);
	}

	/**
	 * @see java.sql.ResultSet#updateTime(java.lang.String, java.sql.Time)
	 */
	public void updateTime(String columnName, Time x) throws SQLException {
		_resultSet.updateTime(columnName, x);
	}

	/**
	 * @see java.sql.ResultSet#getTime(int, java.util.Calendar)
	 */
	public Time getTime(int columnIndex, Calendar cal) throws SQLException {
		return _resultSet.getTime(columnIndex, cal);
	}

	/**
	 * @see java.sql.ResultSet#getTimestamp(java.lang.String)
	 */
	public Timestamp getTimestamp(String columnName) throws SQLException {
		return _resultSet.getTimestamp(columnName);
	}

	/**
	 * @see java.sql.ResultSet#updateTimestamp(java.lang.String, java.sql.Timestamp)
	 */
	public void updateTimestamp(String columnName, Timestamp x) throws SQLException {
		_resultSet.updateTimestamp(columnName, x);
	}

	/**
	 * @see java.sql.ResultSet#getTimestamp(int, java.util.Calendar)
	 */
	public Timestamp getTimestamp(int columnIndex, Calendar cal) throws SQLException {
		return _resultSet.getTimestamp(columnIndex, cal);
	}

	/**
	 * @see java.sql.ResultSet#getObject(java.lang.String, java.util.Map)
	 */
	public Object getObject(String colName, Map<String, Class<?>> map) throws SQLException {
		return _resultSet.getObject(colName, map);
	}

	/**
	 * @see java.sql.ResultSet#getDate(java.lang.String, java.util.Calendar)
	 */
	public Date getDate(String columnName, Calendar cal) throws SQLException {
		return _resultSet.getDate(columnName, cal);
	}

	/**
	 * @see java.sql.ResultSet#getTime(java.lang.String, java.util.Calendar)
	 */
	public Time getTime(String columnName, Calendar cal) throws SQLException {
		return _resultSet.getTime(columnName, cal);
	}

	/**
	 * @see java.sql.ResultSet#getTimestamp(java.lang.String, java.util.Calendar)
	 */
	public Timestamp getTimestamp(String columnName, Calendar cal) throws SQLException {
		return _resultSet.getTimestamp(columnName, cal);
	}

	/* ###### getters / setters ###### */
	/**
	 * @return Returns the validator.
	 */
	public ObjectValidator getValidator() {
		return _validator;
	}

	/**
	 * @param validator The validator to set.
	 */
	public void setValidator(ObjectValidator validator) {
		_validator = validator;
	}

	/**
	 * @return Returns the rs.
	 */
	public ResultSet getResultSet() {
		return _resultSet;
	}

	/**
	 * @param rs The rs to set.
	 */
	public void setResultSet(ResultSet rs) {
		if (rs != _resultSet) {
			_resultSet = rs;
			reset();
		}
	}

	private void reset() {
		_index = new int[INITIAL_CAPACITY];
		_currentRow = 0;
		_size = 0;
		_isComplete = false;
	}
}
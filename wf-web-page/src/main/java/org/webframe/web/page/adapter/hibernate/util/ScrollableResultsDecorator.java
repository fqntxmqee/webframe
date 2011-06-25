
package org.webframe.web.page.adapter.hibernate.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Blob;
import java.sql.Clob;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.hibernate.HibernateException;
import org.hibernate.ScrollableResults;
import org.hibernate.type.Type;
import org.webframe.web.page.adapter.util.ObjectValidator;

/**
 * The ScrollableResultsDecorator class enable or disable putting objects into a final ResultsSet,
 * thus into a final valueList. To check validity it uses ObjectValidator's interface. This
 * implementation ensures that the validation method is invoked only once per object.
 * 
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @version $Id: codetemplates.xml,v 1.1 2009/09/07 08:48:12 Exp $ Create: 2011-6-25 下午02:02:03
 */
public class ScrollableResultsDecorator implements ScrollableResults {

	private static final int	INITIAL_CAPACITY	= 100;

	private ScrollableResults	_scrollableResults;

	private ObjectValidator		_validator;

	private int						_currentRow			= -1;

	private int[]					_index;

	private int						_size;

	private boolean				_isComplete;

	/**
	 * Default contructor of decorator;
	 */
	public ScrollableResultsDecorator(ScrollableResults scrollableResults, ObjectValidator validator) {
		super();
		setValidator(validator);
		setScrollableResults(scrollableResults); // the method calls reset() as well
	}

	/**
	 * @see org.hibernate.ScrollableResults#beforeFirst()
	 */
	public void beforeFirst() throws HibernateException {
		if (_scrollableResults.getRowNumber() >= 0) {
			_scrollableResults.beforeFirst();
		}
		_currentRow = -1;
	}

	/**
	 * @see org.hibernate.ScrollableResults#first()
	 */
	public boolean first() throws HibernateException {
		return move(0);
	}

	/**
	 * @see org.hibernate.ScrollableResults#isFirst()
	 */
	public boolean isFirst() throws HibernateException {
		return (_currentRow == 0);
	}

	/**
	 * @see org.hibernate.ScrollableResults#afterLast()
	 */
	public void afterLast() throws HibernateException {
		if (_isComplete) {
			// the end has been already reached
			_scrollableResults.afterLast();
			_currentRow = _size;
		} else {
			lastKnown();
			while (nextValid());
		}
	}

	/**
	 * @see org.hibernate.ScrollableResults#last()
	 */
	public boolean last() throws HibernateException {
		if (_isComplete) {
			// the end has been already reached
			return move(_size - 1);
		} else {
			afterLast();
			return previous();
		}
	}

	/**
	 * @see org.hibernate.ScrollableResults#isLast()
	 */
	public boolean isLast() throws HibernateException {
		if (_isComplete) {
			return ((_currentRow + 1) == _size);
		} else {
			boolean result = next();
			previous();
			return result;
		}
	}

	/**
	 * @see org.hibernate.ScrollableResults#next()
	 */
	public boolean next() throws HibernateException {
		return move(_currentRow + 1);
	}

	/**
	 * @see org.hibernate.ScrollableResults#previous()
	 */
	public boolean previous() throws HibernateException {
		return move(_currentRow - 1);
	}

	/**
	 * @see org.hibernate.ScrollableResults#scroll(int)
	 */
	public boolean scroll(int i) throws HibernateException {
		if (i == 0) {
			return _scrollableResults.scroll(0);
		}
		return move(_currentRow + i);
	}

	/**
	 * @see org.hibernate.ScrollableResults#setRowNumber(int)
	 */
	public boolean setRowNumber(int row) throws HibernateException {
		if (row >= 0) {
			return move(row);
		} else {
			if (!_isComplete) {
				// ensure the end has been already reached
				afterLast();
			}
			return move(_size + row);
		}
	}

	private boolean move(int row) throws HibernateException {
		if (row >= 0) {
			if (row < _size) {
				_currentRow = row;
				int rowNumber = _index[_currentRow];
				if (rowNumber != _scrollableResults.getRowNumber()) {
					return _scrollableResults.setRowNumber(rowNumber);
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

	private void lastKnown() throws HibernateException {
		if (_size > 0) {
			_currentRow = _size - 1;
			int rowNumber = _index[_currentRow];
			if (rowNumber != _scrollableResults.getRowNumber()) {
				_scrollableResults.setRowNumber(_index[_currentRow]);
			}
		} else {
			beforeFirst();
		}
	}

	private boolean nextValid() throws HibernateException {
		boolean result;
		do {
			result = _scrollableResults.next();
		} while (result && !_validator.isAcceptable(_scrollableResults.get(0)));
		_currentRow++;
		if (result) {
			_size = _currentRow + 1;
			ensureCapacity(_size);
			_index[_currentRow] = _scrollableResults.getRowNumber();
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

	/**
	 * @see org.hibernate.ScrollableResults#getRowNumber()
	 */
	public int getRowNumber() throws HibernateException {
		return _currentRow;
	}

	/* ###### delegated methods ###### */
	/**
	 * @see org.hibernate.ScrollableResults#get()
	 */
	public Object[] get() throws HibernateException {
		return _scrollableResults.get();
	}

	/**
	 * @see org.hibernate.ScrollableResults#get(int)
	 */
	public Object get(int arg0) throws HibernateException {
		return _scrollableResults.get(arg0);
	}

	/**
	 * @see org.hibernate.ScrollableResults#close()
	 */
	public void close() throws HibernateException {
		_scrollableResults.close();
	}

	/**
	 * @see org.hibernate.ScrollableResults#getBigDecimal(int)
	 */
	public BigDecimal getBigDecimal(int arg0) throws HibernateException {
		return _scrollableResults.getBigDecimal(arg0);
	}

	/**
	 * @see org.hibernate.ScrollableResults#getBigInteger(int)
	 */
	public BigInteger getBigInteger(int arg0) throws HibernateException {
		return _scrollableResults.getBigInteger(arg0);
	}

	/**
	 * @see org.hibernate.ScrollableResults#getBinary(int)
	 */
	public byte[] getBinary(int arg0) throws HibernateException {
		return _scrollableResults.getBinary(arg0);
	}

	/**
	 * @see org.hibernate.ScrollableResults#getBlob(int)
	 */
	public Blob getBlob(int arg0) throws HibernateException {
		return _scrollableResults.getBlob(arg0);
	}

	/**
	 * @see org.hibernate.ScrollableResults#getBoolean(int)
	 */
	public Boolean getBoolean(int arg0) throws HibernateException {
		return _scrollableResults.getBoolean(arg0);
	}

	/**
	 * @see org.hibernate.ScrollableResults#getByte(int)
	 */
	public Byte getByte(int arg0) throws HibernateException {
		return _scrollableResults.getByte(arg0);
	}

	/**
	 * @see org.hibernate.ScrollableResults#getCalendar(int)
	 */
	public Calendar getCalendar(int arg0) throws HibernateException {
		return _scrollableResults.getCalendar(arg0);
	}

	/**
	 * @see org.hibernate.ScrollableResults#getClob(int)
	 */
	public Clob getClob(int arg0) throws HibernateException {
		return _scrollableResults.getClob(arg0);
	}

	/**
	 * @see org.hibernate.ScrollableResults#getDate(int)
	 */
	public Date getDate(int arg0) throws HibernateException {
		return _scrollableResults.getDate(arg0);
	}

	/**
	 * @see org.hibernate.ScrollableResults#getDouble(int)
	 */
	public Double getDouble(int arg0) throws HibernateException {
		return _scrollableResults.getDouble(arg0);
	}

	/**
	 * @see org.hibernate.ScrollableResults#getFloat(int)
	 */
	public Float getFloat(int arg0) throws HibernateException {
		return _scrollableResults.getFloat(arg0);
	}

	/**
	 * @see org.hibernate.ScrollableResults#getCharacter(int)
	 */
	public Character getCharacter(int arg0) throws HibernateException {
		return _scrollableResults.getCharacter(arg0);
	}

	/**
	 * @see org.hibernate.ScrollableResults#getInteger(int)
	 */
	public Integer getInteger(int arg0) throws HibernateException {
		return _scrollableResults.getInteger(arg0);
	}

	/**
	 * @see org.hibernate.ScrollableResults#getLocale(int)
	 */
	public Locale getLocale(int arg0) throws HibernateException {
		return _scrollableResults.getLocale(arg0);
	}

	/**
	 * @see org.hibernate.ScrollableResults#getLong(int)
	 */
	public Long getLong(int arg0) throws HibernateException {
		return _scrollableResults.getLong(arg0);
	}

	/**
	 * @see org.hibernate.ScrollableResults#getShort(int)
	 */
	public Short getShort(int arg0) throws HibernateException {
		return _scrollableResults.getShort(arg0);
	}

	/**
	 * @see org.hibernate.ScrollableResults#getString(int)
	 */
	public String getString(int arg0) throws HibernateException {
		return _scrollableResults.getString(arg0);
	}

	/**
	 * @see org.hibernate.ScrollableResults#getText(int)
	 */
	public String getText(int arg0) throws HibernateException {
		return _scrollableResults.getString(arg0);
	}

	/**
	 * @see org.hibernate.ScrollableResults#getTimeZone(int)
	 */
	public TimeZone getTimeZone(int arg0) throws HibernateException {
		return _scrollableResults.getTimeZone(arg0);
	}

	/**
	 * @see org.hibernate.ScrollableResults#getType(int)
	 */
	public Type getType(int arg0) {
		return _scrollableResults.getType(arg0);
	}

	/* ###### getters / setters ###### */
	public void setScrollableResults(ScrollableResults scrollableResults) {
		_scrollableResults = scrollableResults;
		reset();
	}

	public void setValidator(ObjectValidator validator) {
		_validator = validator;
	}

	private void reset() {
		_index = new int[INITIAL_CAPACITY];
		_currentRow = -1;
		_size = 0;
		_isComplete = false;
	}
}
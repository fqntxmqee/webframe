/**
 * Copyright (c) 2003 held jointly by the individual authors.
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation; either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; with out even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library;  if not, write to the Free Software Foundation,
 * Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307  USA.
 *
 * > http://www.gnu.org/copyleft/lesser.html
 * > http://www.opensource.org/licenses/lgpl-license.php
 */
package org.webframe.web.page.adapter.jdbc.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

/**
 * The default connection creator, it uses the simplest way to create connection and clear the resources.
 *
 * @author Stepan Marek
 * @version $Revision: 1.3 $ $Date: 2006/03/29 19:47:49 $
 */
public class StandardConnectionCreator implements ConnectionCreator
{
   private DataSource dataSource;

   private int transactionIsolation = 0;

   public StandardConnectionCreator()
   {}

   public StandardConnectionCreator(DataSource dataSource)
   {
      this.dataSource = dataSource;
   }

   public Connection createConnection() throws SQLException
   {
      Connection connection = dataSource.getConnection();
      switch (transactionIsolation)
      {
         case Connection.TRANSACTION_READ_UNCOMMITTED: 
         case Connection.TRANSACTION_READ_COMMITTED:
         case Connection.TRANSACTION_REPEATABLE_READ:
         case Connection.TRANSACTION_SERIALIZABLE:
            connection.setTransactionIsolation(transactionIsolation);
            connection.setAutoCommit(false);
            break;
      }
      return connection;
   }

   public void close(ResultSet result, PreparedStatement statement, Connection connection)
   {
      JdbcUtil.close(result, statement, connection);
   }

   public DataSource getDataSource()
   {
      return dataSource;
   }

   public void setDataSource(DataSource dataSource)
   {
      this.dataSource = dataSource;
   }
   public void setTransactionIsolation(int transactionIsolation)
   {
      this.transactionIsolation = transactionIsolation;
   }
}
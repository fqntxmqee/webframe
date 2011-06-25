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
 * This is a helper class to handle different behaviour of connection eg. in transactional/nontransactional environment.
 *
 * @author Stepan Marek
 * @version $Revision: 1.2 $ $Date: 2005/10/20 16:37:49 $
 */
public interface ConnectionCreator
{

   /**
    * Creates a new connection.
    *
    * @return
    * @throws SQLException
    */
   Connection createConnection() throws SQLException;

   /**
    * Close all opened resources.
    *
    * @param result
    * @param statement
    * @param connection
    */
   void close(ResultSet result, PreparedStatement statement, Connection connection);

   /**
    * @return Returns the dataSource.
    */
   DataSource getDataSource();

   /**
    * @param dataSource The dataSource to set.
    */
   void setDataSource(DataSource dataSource);
}
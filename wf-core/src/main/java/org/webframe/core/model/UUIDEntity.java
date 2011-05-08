
package org.webframe.core.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.GenericGenerator;

/**
 * uuid主键策略
 * 
 * @author <a href="mailto:guoqing.huang@foxmail.com>huangguoqing</a>
 * @version $Id: codetemplates.xml,v 1.3 2009/05/05 02:30:07 huangguoqing Exp $ Create: 2010-12-24
 *          下午03:22:48
 */
@MappedSuperclass
public abstract class UUIDEntity extends BaseEntity {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= -7296880883046805908L;

	@Override
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "ID_", length = 32, updatable = false, insertable = false)
	public String getId() {
		return super.getId();
	}
}

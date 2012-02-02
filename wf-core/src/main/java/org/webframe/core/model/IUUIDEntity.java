/*
 * wf-core
 * Created on 2012-2-2-上午09:36:34
 */

package org.webframe.core.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;

/**
 * UUID主键策略实体类接口
 * 
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @since 2012-2-2 上午09:36:34
 * @version
 */
public interface IUUIDEntity {

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "ID_", length = 32, updatable = false, insertable = false)
	public String getId();
}

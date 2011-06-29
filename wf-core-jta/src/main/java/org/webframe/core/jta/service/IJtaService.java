/*
 * wf-core-jta
 * Created on 2011-6-29-下午04:23:45
 */

package org.webframe.core.jta.service;

import org.webframe.core.jta.exception.JtaServiceException;
import org.webframe.core.model.BaseEntity;
import org.webframe.core.service.IBaseService;

/**
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @version $Id: codetemplates.xml,v 1.1 2009/09/07 08:48:12 Exp $ Create: 2011-6-29 下午04:23:45
 */
public interface IJtaService extends IBaseService {

	void jtaCreateTable(BaseEntity entity) throws JtaServiceException;
}

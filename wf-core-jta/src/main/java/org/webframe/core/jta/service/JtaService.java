/*
 * wf-core-jta
 * Created on 2011-6-29-下午04:24:07
 */

package org.webframe.core.jta.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.webframe.core.exception.ServiceException;
import org.webframe.core.jta.dao.IJtaDao;
import org.webframe.core.jta.exception.JtaServiceException;
import org.webframe.core.model.BaseEntity;
import org.webframe.core.service.BaseService;

/**
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @version $Id: codetemplates.xml,v 1.1 2009/09/07 08:48:12 Exp $ Create: 2011-6-29 下午04:24:07
 */
@Service
public class JtaService extends BaseService implements IJtaService {

	@Autowired
	private IJtaDao	jtaDao;

	@Override
	public void jtaCreateTable(BaseEntity entity) throws JtaServiceException {
		try {
			save(entity);
		} catch (ServiceException e) {
			throw new JtaServiceException(e.getMessage());
		}
		jtaDao.createTable(null, null, null);
	}
}

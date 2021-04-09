package com.incture.ptw.services;

import javax.transaction.Transactional;

import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.incture.ptw.dao.GetJsaDetailsDao;

@Service
@Transactional
public class GetJsaDetailsService {

	@Autowired
	private GetJsaDetailsDao getJsaDetailsDao;

	public void downloadDataService() {
		

		try {
			getJsaDetailsDao.downloaadData();

		} catch (Exception e) {

			System.out.println(e.getMessage());

		}

		

	}

}

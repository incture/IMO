package com.incture.iopptw.services;

import java.util.List;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;

import com.incture.iopptw.entities.TestDo;
import com.incture.iopptw.repositories.TestDao;

@Service
public class TestService {
	@Autowired
	private TestDao testDao;
	public List<TestDo> getAllData() {
		return testDao.getAllData();
	}
	
	public String saveData(int id){
		return testDao.saveData(id);
	}

}
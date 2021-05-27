package com.incture.iopptw.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.incture.iopptw.entities.TestDo;
import com.incture.iopptw.repositories.TestRepository;

@Service
public class TestService {
	@Autowired
	private TestRepository testRepository;
	public List<TestDo> getAllData() {
		return testRepository.findAll();
	}

}

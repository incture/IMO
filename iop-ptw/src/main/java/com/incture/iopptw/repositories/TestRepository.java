package com.incture.iopptw.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.incture.iopptw.entities.TestDo;

public interface TestRepository extends JpaRepository<TestDo, Integer> {

}

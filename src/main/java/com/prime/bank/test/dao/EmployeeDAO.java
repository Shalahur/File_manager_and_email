package com.prime.bank.test.dao;

import com.prime.bank.test.model.Employee;
import com.prime.bank.test.model.Job;

import java.util.List;

public interface EmployeeDAO {

    List<Employee> getAllEmployeeBYJobId(String jobId);
}

package com.prime.bank.test.dao.impl;

import com.prime.bank.test.dao.EmployeeDAO;
import com.prime.bank.test.dao.JobDAO;
import com.prime.bank.test.model.Employee;
import com.prime.bank.test.model.EmployeeMapper;
import com.prime.bank.test.model.Job;
import com.prime.bank.test.model.JobMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EmployeeDAOImpl implements EmployeeDAO {

    @Autowired
    JdbcTemplate jdbcTemplate;

    private final String SQL_FIND_EMPLOYEES = "select * from EMPLOYEES where JOB_ID = ?";

    @Override
    public List<Employee> getAllEmployeeBYJobId(String jobId) {
        return jdbcTemplate.query(SQL_FIND_EMPLOYEES,new Object[]{jobId}, new EmployeeMapper());
    }
}
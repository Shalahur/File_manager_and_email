package com.prime.bank.test.dao.impl;

import com.prime.bank.test.dao.JobDAO;
import com.prime.bank.test.model.Job;
import com.prime.bank.test.model.JobMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JobDAOImpl implements JobDAO {

    @Autowired
    JdbcTemplate jdbcTemplate;

    private final String SQL_FIND_JOB = "select * from jobs where JOB_ID = ?";
    private final String SQL_INSERT_JOB = "insert into jobs(JOB_ID, JOB_TITLE, MIN_SALARY, MAX_SALARY) values(?,?,?,?)";
    private final String SQL_GET_ALL = "select * from jobs";

    @Override
    public Job getJobById(String id) {
        return jdbcTemplate.queryForObject(SQL_FIND_JOB, new Object[]{id}, new JobMapper());
    }

    @Override
    public boolean createJob(Job job) {
        return jdbcTemplate.update(SQL_INSERT_JOB, job.getJobId(), job.getJobTitle(), job.getMinSalary(),
                job.getMaxSalary()) > 0;
    }

    @Override
    public List<Job> getAllJob() {
        return jdbcTemplate.query(SQL_GET_ALL, new JobMapper());
    }
}
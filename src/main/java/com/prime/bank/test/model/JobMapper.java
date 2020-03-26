package com.prime.bank.test.model;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class JobMapper implements RowMapper<Job> {
    @Override
    public Job mapRow(ResultSet resultSet, int i) throws SQLException {
        return Job.builder()
                .jobId(resultSet.getString("JOB_ID"))
                .jobTitle(resultSet.getString("JOB_TITLE"))
                .maxSalary(resultSet.getInt("MIN_SALARY"))
                .minSalary(resultSet.getInt("MAX_SALARY"))
                .build();

    }
}

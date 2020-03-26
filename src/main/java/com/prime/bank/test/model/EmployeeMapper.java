package com.prime.bank.test.model;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class EmployeeMapper implements RowMapper<Employee> {
    @Override
    public Employee mapRow(ResultSet resultSet, int i) throws SQLException {
        return Employee.builder()
                .employeeId(resultSet.getInt("EMPLOYEE_ID"))
                .firstName(resultSet.getString("FIRST_NAME"))
                .lastName(resultSet.getString("LAST_NAME"))
                .email(resultSet.getString("EMAIL"))

                .build();

    }
}
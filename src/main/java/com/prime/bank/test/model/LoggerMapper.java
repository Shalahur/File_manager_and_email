package com.prime.bank.test.model;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class LoggerMapper implements RowMapper<Logger> {
    @Override
    public Logger mapRow(ResultSet resultSet, int i) throws SQLException {
        return Logger.builder()
                .id(resultSet.getInt("id"))
                .description(resultSet.getString("description"))
                .logTime(resultSet.getTimestamp("log_time"))
                .build();

    }
}

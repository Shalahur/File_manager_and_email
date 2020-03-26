package com.prime.bank.test.dao.impl;

import com.prime.bank.test.dao.LoggerDAO;
import com.prime.bank.test.model.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class LoggerDAOImpl implements LoggerDAO {

    @Autowired
    JdbcTemplate jdbcTemplate;

    private final String SQL_INSERT_LOG = "insert into logger(id, description, log_time) values(?,?,?)";

    @Override
    public boolean createLog(Logger log) {
        return jdbcTemplate.update(SQL_INSERT_LOG, log.getId(), log.getDescription(), log.getLogTime()) > 0;
    }
}
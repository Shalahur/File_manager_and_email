package com.prime.bank.test.dao;

import com.prime.bank.test.model.Job;

import java.util.List;

public interface JobDAO {
    Job getJobById(String id);

    boolean createJob(Job job);

    List<Job> getAllJob();

}

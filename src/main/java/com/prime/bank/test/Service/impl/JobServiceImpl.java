package com.prime.bank.test.Service.impl;

import com.prime.bank.test.Service.JobService;
import com.prime.bank.test.dao.JobDAO;
import com.prime.bank.test.model.Job;
import com.prime.bank.test.util.JasperManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class JobServiceImpl implements JobService {

    private static Logger logger = Logger.getLogger(JobService.class);

    @Autowired
    JobDAO jobDAO;

    @Autowired
    JasperManager jasperManager;

    @Override
    public void createJobs(Map<Integer, Map<String, Object>> jobs) {
        for (Integer row : jobs.keySet()) {
            Map<String, Object> data = jobs.get(row);

            Job job = Job.builder()
                    .jobId(data.get("jobId").toString())
                    .jobTitle(data.get("jobTitle").toString())
                    .maxSalary((Integer) data.get("maxSalary"))
                    .minSalary((Integer) data.get("minSalary"))
                    .build();
            jobDAO.createJob(job);
        }
    }

    @Override
    public List<List<String>> prepareData() {
        List<Job> jobs = jobDAO.getAllJob();
        List<List<String>> data = new ArrayList<>();
        jobDAO.getAllJob().forEach(job -> {
            List<String> jobDetail = new ArrayList<>();
            jobDetail.add(job.getJobId());
            jobDetail.add(job.getJobTitle());
            jobDetail.add(job.getMinSalary().toString());
            jobDetail.add(job.getMaxSalary().toString());
            data.add(jobDetail);
        });
        return data;
    }

    @Override
    public List<String> prepareHeader() {
        List<String> header = new ArrayList<>();
        header.add("Job Id");
        header.add("Job Title");
        header.add("Min salary");
        header.add("Max Salary");
        return header;
    }

    @Override
    public void generatePdf() {

        List<Job> data = jobDAO.getAllJob();
        List<String> subReports = new ArrayList<>();

        try {
            jasperManager.generatePdf("Job", "Job.pdf", subReports, new JRBeanCollectionDataSource(data));
        } catch (Exception e) {
            logger.error("Unable to generate pdf" + e);
        }
    }
}

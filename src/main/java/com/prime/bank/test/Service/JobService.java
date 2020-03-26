package com.prime.bank.test.Service;

import java.util.List;
import java.util.Map;

public interface JobService {
    void createJobs(Map<Integer, Map<String, Object>> jobs);

    List<List<String>> prepareData();

    List<String> prepareHeader();

    void generatePdf();
}

package com.prime.bank.test.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ExcelManagerUtil {

    private static Logger logger = Logger.getLogger(ExcelManagerUtil.class);

    public Map<Integer, Map<String, Object>> readUploadDocuments(InputStream in) {
        Map<Integer, Map<String, Object>> documents = new TreeMap<>();
        List<Integer> failedRows = new ArrayList<>();
        try {
            final ExcelReader decoder = ExcelReader.init(in);
            decoder.selectSheet(0).forEachRow((index) -> {
                if (index != 0) {
                    try {
                        Map<String, Object> data = new TreeMap<>();
                        data.put("jobId", decoder.getStringValueOfCell(index, 0));
                        data.put("jobTitle",  decoder.getStringValueOfCell(index, 1));
                        data.put("maxSalary", decoder.getIntegerValueOfCell(index, 2));
                        data.put("minSalary", decoder.getIntegerValueOfCell(index, 3));
                        documents.put(index, data);
                    } catch (Exception e) {
                        failedRows.add(index);
                    }
                }
            });
        } catch (Exception e) {
            logger.error("Unable read excel document.", e);
        }

        if (!failedRows.isEmpty()) {
            logger.error("Unable parse rows :" + StringUtils.join(failedRows, ","));
        }
        return documents;
    }
}

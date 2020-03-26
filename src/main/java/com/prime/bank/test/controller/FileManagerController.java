package com.prime.bank.test.controller;

import com.prime.bank.test.Service.JobService;
import com.prime.bank.test.Service.MailService;
import com.prime.bank.test.dao.EmployeeDAO;
import com.prime.bank.test.dao.JobDAO;
import com.prime.bank.test.model.Employee;
import com.prime.bank.test.util.Constants;
import com.prime.bank.test.util.ExcelManagerUtil;
import com.prime.bank.test.util.ExcelWriter;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
public class FileManagerController {
    private static Logger logger = Logger.getLogger(FileManagerController.class);

    @Autowired
    JobService jobService;

    @Autowired
    JobDAO jobDAO;

    @Autowired
    ExcelManagerUtil excelManagerUtil;

    @Autowired
    MailService mailService;

    @Autowired
    EmployeeDAO employeeDAO;

    @GetMapping(value = "/")
    public String home() {
        return "home/index";
    }

    @GetMapping(value = "/import")
    public String importFile() {
        return "home/import-file";
    }

    @PostMapping("/import")
    public String uploadPersonalLibraryDocuments(@RequestParam(name = "fileAttachment", required = false) final MultipartFile fileAttachment) {
        try {
            Map<Integer, Map<String, Object>> jobs = excelManagerUtil.readUploadDocuments(fileAttachment.getInputStream());
            jobService.createJobs(jobs);
        } catch (Exception ex) {
            logger.error("Unable Import document.", ex);
        }
        return "home/import-file";
    }

    @GetMapping(value = "/export")
    public String export() {
        return "home/export-file";
    }

    @PostMapping(value = "/export")
    public String export(@RequestParam(name = "excel", required = false) final boolean isExcel,
                         @RequestParam(name = "csv", required = false) final boolean isCsv,
                         @RequestParam(name = "pdf", required = false) final boolean isPdf) {

        List<String> headers = jobService.prepareHeader();
        List<List<String>> data = jobService.prepareData();
        Map<String, Boolean> fileType = new HashMap();

        try {
            final ExcelWriter writer = new ExcelWriter();

            if (isPdf) {
                fileType.put("isPdf", true);
                jobService.generatePdf();
            }

            if (isCsv) {
                fileType.put("isCsv", true);
                writer.setFileName("Job" + ".csv");
                writer.setSheetName("Jobs");
                writer.writeXLSXFile(data, headers);
            }

            if (isExcel) {
                fileType.put("isExcel", true);
                writer.setFileName("Job" + ".xlsx");
                writer.setSheetName("Jobs");
                writer.writeXLSXFile(data, headers);
            }

            if (isCsv || isExcel || isPdf) {
                List<Employee> employees =  employeeDAO.getAllEmployeeBYJobId("IT_PROG");
                employees.forEach(employee ->{
                    mailService.sendMessageWithAttachment(employee.getEmail(), "Job List", "Hi there,\n this is job list.", Constants.DOWNLOAD_FOLDER_LOCATION, fileType);
                });
            }

        } catch (IOException e) {
            logger.error("Unable export document and send email.", e);
        }
        return "home/export-file";
    }
}

package com.prime.bank.test.util;


import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.fill.JRSwapFileVirtualizer;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfReportConfiguration;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;


public class JasperManager {
    private static Logger logger = Logger.getLogger(ExcelWriter.class);

    private ByteArrayOutputStream exportAsPdf(String filename,
                                              JRBeanCollectionDataSource dataSource,
                                              HashMap<String, Object> params) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            writeStreamAsPdf(filename, dataSource, params, baos);
        } catch (Exception ex) {
            logger.error("Unable to export", ex);
        }
        return baos;
    }


    private void writeStreamAsPdf(String filename,
                                  JRBeanCollectionDataSource dataSource,
                                  HashMap<String, Object> params,
                                  ByteArrayOutputStream baos) throws JRException {
        JRPdfExporter exporter = new JRPdfExporter();
        exporter.setConfiguration(new SimplePdfReportConfiguration());
        exporter.setExporterInput(exporterInput(filename, dataSource, params));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(baos));
        exporter.exportReport();
    }

    private SimpleExporterInput exporterInput(String filename,
                                              JRBeanCollectionDataSource dataSource,
                                              HashMap<String, Object> params) throws JRException {
        return new SimpleExporterInput(JasperFillManager.fillReport(jasperReport(filename), params, dataSource));
    }

    public JasperReport jasperReport(String filename) throws JRException {
        File binary = new File(jasperBinary(filename));

        if (!binary.exists()) {
            JasperCompileManager.compileReportToFile(jasperSource(filename), binary.getPath());
        }
        return (JasperReport) JRLoader.loadObjectFromFile(binary.getPath());
    }

    private JasperDesign jasperSource(String filename) throws JRException {
        return JRXmlLoader.load(loadResource("report/" + filename.concat(Constants.DOT).concat("jrxml")));
    }

    private String jasperBinary(String filename) {
        File jasperFolder = new File(Constants.JASPER_FOLDER_LOCATION);
        if (!jasperFolder.exists()) {
            jasperFolder.mkdir();
        }
        return new StringBuilder()
                .append(Constants.JASPER_FOLDER_LOCATION)
                .append(File.separator)
                .append(filename)
                .append(Constants.DOT)
                .append("jasper")
                .toString();
    }

    public String jasperSubReportBinaryPath() {
        File jasperFolder = new File(Constants.JASPER_FOLDER_LOCATION);
        if (!jasperFolder.exists()) {
            jasperFolder.mkdir();
        }
        return new StringBuilder()
                .append(Constants.JASPER_FOLDER_LOCATION)
                .append(File.separator)
                .toString();
    }


    private InputStream loadResource(String resource) {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        return loader.getResourceAsStream(resource);
    }

    public File writeAsPdf(String fileName,
                           String exportFileName,
                           HashMap<String, Object> params,
                           List<String> subReports,
                           JRBeanCollectionDataSource dataSource) {
        File file = new File(exportFileName);
        try {
            if (Objects.nonNull(subReports)) {
                for (String report : subReports) {
                    jasperReport(report);
                }
            }

            try (ByteArrayOutputStream baos = exportAsPdf(fileName, dataSource, params)) {
                try (FileOutputStream fos = new FileOutputStream(file)) {
                    baos.writeTo(fos);
                } catch (Exception ioe) {
                    logger.error("Unable to write", ioe);
                }
            }
        } catch (Exception e) {
            logger.error("Unable to process jasper", e);
        }

        cleanJRSwapFileVirtualizer(params);
        return file;
    }

    public <T> Boolean generatePdf(String jasperFileName,
                                   String fileName,
                                   List<String> subReportNames,
                                   JRBeanCollectionDataSource dataSource) {

        File downloadFolder = new File(Constants.DOWNLOAD_FOLDER_LOCATION);
        if (!downloadFolder.exists()) {
            downloadFolder.mkdir();
        }

        HashMap<String, Object> params = new HashMap<>();
        params.put("SUBREPORT_DIR", jasperSubReportBinaryPath());

        try {
            if (subReportNames != null && !subReportNames.isEmpty()) {
                for (String report : subReportNames) {
                    jasperReport(report);
                }
            }

            try (ByteArrayOutputStream baos = exportAsPdf(jasperFileName, dataSource, params)) {
                File file = new File(Constants.DOWNLOAD_FOLDER_LOCATION +
                        File.separator + fileName);
                FileUtils.writeByteArrayToFile(file, baos.toByteArray());
            } catch (Exception ex) {
                logger.error("Unable to export", ex);
            }
        } catch (Exception e) {
            logger.error("Unable to process jasper", e);
            return Boolean.FALSE;
        }

        return Boolean.TRUE;
    }

    private void cleanJRSwapFileVirtualizer(HashMap<String, Object> params) {
        if (params.containsKey(JRParameter.REPORT_VIRTUALIZER)) {
            JRSwapFileVirtualizer jrSwapFileVirtualizer =
                    (JRSwapFileVirtualizer) params.get(JRParameter.REPORT_VIRTUALIZER);
            jrSwapFileVirtualizer.cleanup();
        }
    }
}

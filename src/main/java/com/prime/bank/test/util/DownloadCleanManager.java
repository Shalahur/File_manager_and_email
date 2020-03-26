package com.prime.bank.test.util;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.File;


public class DownloadCleanManager {
    private static Logger logger = Logger.getLogger(DownloadCleanManager.class);

    @Scheduled(fixedDelay = 120000)
    public void cleanFolder() {
        try {
            FileUtils.cleanDirectory(new File(Constants.DOWNLOAD_FOLDER_LOCATION));
        } catch (Exception ex) {
            logger.error("Unable to clean directory" + ex);
        }
    }
}

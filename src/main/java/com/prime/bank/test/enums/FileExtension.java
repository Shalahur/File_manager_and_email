package com.prime.bank.test.enums;

import lombok.Getter;
import org.apache.commons.io.FilenameUtils;

import java.util.Objects;

@Getter
public enum FileExtension {

    PDF(1, "pdf", "application/pdf", false),
    XLSX(2, "xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", false),
    CSV(3, "csv", "text/csv", false);

    private Integer id;
    private String name;
    private String mimeType;
    private Boolean convert;

    FileExtension(Integer id, String name, String mimeType, Boolean convert) {
        this.id = id;
        this.name = name;
        this.mimeType = mimeType;
        this.convert = convert;
    }

    public static FileExtension getById(Integer id) {
        for (FileExtension type : FileExtension.values()) {
            if (Objects.equals(type.getId(), id)) {
                return type;
            }
        }
        return null;
    }

    public static String getMimeType(String fileName) {
        for (FileExtension type : FileExtension.values()) {
            if (type.name.equalsIgnoreCase(FilenameUtils.getExtension(fileName))) {
                return type.mimeType;
            }
        }
        return "application/octet-stream";
    }

    public static FileExtension getByName(String str) {
        for (FileExtension type : FileExtension.values()) {
            if (type.getName().equalsIgnoreCase(str)) {
                return type;
            }
        }
        return null;
    }
}

package com.redhat.qe.sekuli.common.remote;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Base64;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.xml.bind.DatatypeConverter;

import org.apache.commons.io.IOUtils;

import com.google.common.io.Files;
import com.redhat.qe.sekuli.common.exceptions.MissingMandatoryFieldsException;
import com.redhat.qe.sekuli.common.model.FileBase64;

/**
 * @author Jeeva Kandasamy (jkandasa)
 */

public class RemoteFile {

    public FileBase64 read(String filePath) throws IOException {
        try (InputStream inputStream = new FileInputStream(filePath);
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            IOUtils.copy(inputStream, outputStream);
            FileBase64 fileBase64 = new FileBase64();
            fileBase64.setPath(filePath);
            fileBase64.setBase64String(Base64.getEncoder().encodeToString(outputStream.toByteArray()));
            return fileBase64;
        }
    }

    public String save(FileBase64 fileBase64) throws IOException {
        if (!fileBase64.isValid()) {
            throw new MissingMandatoryFieldsException("Missing mandatory parameters!");
        }
        File target = new File(fileBase64.getPath());
        byte[] data = Base64.getDecoder().decode(fileBase64.getBase64String());
        try (InputStream inputStream = new ByteArrayInputStream(data);
                OutputStream outputStream = new FileOutputStream(target)) {
            IOUtils.copy(inputStream, outputStream);
        }
        return target.getCanonicalPath();
    }

    public String saveZip(String base64Zip) throws IOException {
        byte[] zipBytes = DatatypeConverter.parseBase64Binary(base64Zip);
        File outputFolder = Files.createTempDir();
        unZipIt(new ByteArrayInputStream(zipBytes), outputFolder);
        return outputFolder.getCanonicalPath();
    }

    private static void unZipIt(InputStream inputStream, File outputFolder) throws IOException {
        try (ZipInputStream zipInputStream = new ZipInputStream(inputStream)) {
            ZipEntry zipEntry;
            while (null != (zipEntry = zipInputStream.getNextEntry())) {
                String fileName = zipEntry.getName();
                File outFile = new File(outputFolder, fileName);
                if (zipEntry.isDirectory()) {
                    outFile.mkdir();
                } else {
                    outFile.getParentFile().mkdirs();
                    try (FileOutputStream fileOutputStream = new FileOutputStream(outFile)) {
                        IOUtils.copy(zipInputStream, fileOutputStream);
                    }
                }
            }
        }
    }
}
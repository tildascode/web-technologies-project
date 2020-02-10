package com.webproject.services;

import com.webproject.form.PresentationForm;
import com.webproject.models.Presentation;
import com.webproject.repositories.PresentationRepository;

import java.io.*;
import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.zeroturnaround.zip.ZipUtil;

@Service
public class PresentationService {

    private static final int RESULTS_PER_PAGE = 10;

    @Autowired
    PresentationRepository presentationRepository;

    public Page<Presentation> findAll(int pageIndex, String tag) {
        if (tag != null) {
            return findAllForTag(tag, pageIndex);
        } else {
            return findAll(pageIndex);
        }
    }

    public Page<Presentation> findAll(int pageIndex) {
        return presentationRepository.findAll(PageRequest.of(pageIndex, RESULTS_PER_PAGE));
    }

    public Page<Presentation> findAllForTag(String tag, int pageIndex) {
        return presentationRepository.findByTagsContaining(tag, PageRequest.of(pageIndex, RESULTS_PER_PAGE));
    }

    public Page<Presentation> findAllForUser(String userId, int pageIndex) {
        //all for user id
        return presentationRepository.findAll(PageRequest.of(pageIndex, RESULTS_PER_PAGE));
    }

    public Set<String> getAllDistinctTags() {
        Set<String> tags = new HashSet<>();
        List<Presentation> presentations = presentationRepository.findAll();
        presentations.stream().forEach(p -> tags.addAll(Arrays.asList(p.getTags().split(","))));
        return tags;
    }

    public void exportAll() {
        ZipUtil
                .pack(new File("src/main/resources/static/files"), new File("src/main/resources/static/presentations.zip"));
    }

    public File decompressZipToDestination(MultipartFile zipFile) throws IOException, ZipException {
        File destDir = createTempDirectory();
        byte[] buffer = new byte[1024];
        ZipInputStream zis = new ZipInputStream(zipFile.getInputStream());
        ZipEntry zipEntry = zis.getNextEntry();
        while (zipEntry != null) {
            File newFile = newFile(destDir, zipEntry);
            FileOutputStream fos = new FileOutputStream(newFile);
            int len;
            while ((len = zis.read(buffer)) > 0) {
                fos.write(buffer, 0, len);
            }
            fos.close();
            zipEntry = zis.getNextEntry();
        }
        zis.closeEntry();
        zis.close();
        return destDir;
    }

    private static File createTempDirectory() throws IOException {
        return Files.createTempDirectory("temp_folder").toFile();
    }

    public static File newFile(File destinationDir, ZipEntry zipEntry) throws IOException {
        File destFile = new File(destinationDir, zipEntry.getName());

        String destDirPath = destinationDir.getCanonicalPath();
        String destFilePath = destFile.getCanonicalPath();

        if (!destFilePath.startsWith(destDirPath + File.separator)) {
            throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
        }

        return destFile;
    }
}

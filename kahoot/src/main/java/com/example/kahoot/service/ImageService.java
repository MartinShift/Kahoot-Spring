package com.example.kahoot.service;

import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@ConditionalOnProperty(value = "mock.azure.blobstorage.service.enabled", havingValue = "false", matchIfMissing = true)
public class ImageService {

    @Value("${azure.storage.connection-string}")
    private String connectionString;

    @Value("${azure.storage.container-name}")
    private String containerName;

    private final CloudBlobContainer cloudBlobContainer;

    public String save(MultipartFile multipartFile) {
        URI uri;
        String multipartName = multipartFile.getName().replaceAll("[\n|\r|\t]", "_");

        try {
            String extension = FilenameUtils.getExtension(multipartFile.getOriginalFilename());
            String fileName = String.join(".", UUID.randomUUID().toString(), extension);
            CloudBlockBlob blob = cloudBlobContainer.getBlockBlobReference(fileName);
            blob.upload(multipartFile.getInputStream(), -1);
            uri = blob.getUri();
        } catch (URISyntaxException | StorageException | IOException e) {
            throw new RuntimeException();
        }
        return Optional.ofNullable(uri).orElseThrow(RuntimeException::new).toString();
    }
}
package com.gotrid.trid.marshmello.services;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class AzureFileService {

    private final BlobContainerClient containerClient;

    public AzureFileService(BlobContainerClient containerClient) {
        this.containerClient = containerClient;
    }

    public String uploadFile(String path, MultipartFile file) throws IOException {
        BlobClient blobClient = containerClient.getBlobClient(path);
        blobClient.upload(file.getInputStream(), file.getSize(), true);
        return blobClient.getBlobUrl();
    }

    public byte[] getFile(String path) throws IOException {
        BlobClient blobClient = containerClient.getBlobClient(path);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        blobClient.download(outputStream);
        return outputStream.toByteArray();
    }
}

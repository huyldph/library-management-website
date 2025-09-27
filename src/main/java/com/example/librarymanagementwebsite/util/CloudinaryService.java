package com.example.librarymanagementwebsite.util;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryService {

    private final Cloudinary cloudinary;

    public CloudinaryService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    public String upload(byte[] fileBytes, String fileName) throws IOException {
        Map<String, Object> uploadResult = cloudinary.uploader().upload(fileBytes,
                ObjectUtils.asMap(
                        "public_id", "barcodes/" + fileName,
                        "resource_type", "image"
                ));

        return uploadResult.get("secure_url").toString();
    }
}

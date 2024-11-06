package org.example.api_project.services;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.util.IOUtils;
import org.example.api_project.dto.DownloadFileS3Response;
import org.example.api_project.dto.ResponseInfo;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Service
public class S3Service {
    public S3Service() {
        BasicAWSCredentials credentials = new BasicAWSCredentials(ID, SECRET_KEY);
        s3 = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withEndpointConfiguration(
                        new AmazonS3ClientBuilder.EndpointConfiguration(
                                "storage.yandexcloud.net", "ru-central1"
                        )
                )
                .build();
    }

    public ResponseInfo<DownloadFileS3Response> downloadFile(String path) {
        String[] buckets = path.split("/");
        if (buckets.length >= 4) {
            String bucket = buckets[3];
            String objectName = buckets[4];

            S3Object object = s3.getObject(new GetObjectRequest(bucket, objectName));

            byte[] targetArray;
            try {
                targetArray = IOUtils.toByteArray(object.getObjectContent());
            } catch (IOException e) {
                return new ResponseInfo<>(HttpStatus.INTERNAL_SERVER_ERROR,
                        Optional.empty(),
                        "Error in processing message");
            }

            String content = new String(targetArray, StandardCharsets.UTF_8);

            return new ResponseInfo<>(HttpStatus.OK,
                    Optional.of(new DownloadFileS3Response(content)),
                    OK_MESSAGE);
        } else {
            return new ResponseInfo<>(HttpStatus.BAD_REQUEST,
                    Optional.empty(),
                    "Invalid URL");
        }
    }

    // private final static String ID = "---";
    // private final static String SECRET_KEY = "---";
    private final static String OK_MESSAGE = "OK";

    private final AmazonS3 s3;
}

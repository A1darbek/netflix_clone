package com.ayderbek.netflxSpring.netflix_clone.service;

import com.amazonaws.services.cloudfront.CloudFrontUrlSigner;
import com.amazonaws.services.cloudfront.util.SignerUtils;
import com.ayderbek.netflxSpring.netflix_clone.domain.Video;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.io.*;
import java.util.Calendar;
import java.util.Date;


@Service
@RequiredArgsConstructor
public class VideoStreamingService {
    private final VideoService videoService;
    @Value("${cloudfront.privateKeyPath}")
    private String privateKeyPath;

    public String getSignedCloudFrontUrl(Long videoId) throws Exception {
        // Retrieve the video
        Video video = videoService.getById(videoId);

        // Get the S3 object key from the video
        String s3ObjectKey = video.getKey();

        // Key pair ID from the CloudFront console
        String keyPairId = "APKA43UPJH7H766E6EMC";

        // Set up the expiration date of the signed URL (valid for 24 hours)
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, 24);
        Date expirationDate = cal.getTime();

        // Load the private key file
//        byte[] derPrivateKey = Files.readAllBytes(Paths.get("C:/Keys/pk-APKA43UPJH7H766E6EMC-pkcs8.pem"));
//        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
//        PrivateKey privateKey = getPrivateKey("C:/Keys/pk-APKA43UPJH7H766E6EMC-pkcs8.pem");
        File privateKeyFile = new File(privateKeyPath);
        // The domain of your CloudFront distribution
        String distributionDomain = "d2tidpfltjr45g.cloudfront.net";

        // Generate the signed URL
        String signedUrlCannedPolicy = CloudFrontUrlSigner.getSignedURLWithCannedPolicy(
                SignerUtils.Protocol.http, distributionDomain, privateKeyFile, s3ObjectKey, keyPairId, expirationDate);

        // Return the signed URL
        return signedUrlCannedPolicy;
    }
}

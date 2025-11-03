package com.github.an001code.blog.utils;

import com.aliyun.oss.ClientBuilderConfiguration;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.common.auth.CredentialsProviderFactory;
import com.aliyun.oss.common.auth.EnvironmentVariableCredentialsProvider;
import com.aliyun.oss.common.comm.SignVersion;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import com.aliyuncs.exceptions.ClientException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

/**
 * 阿里云 OSS 工具类
 */

@Component
public class AliOSSUtils {

    private static final Logger logger = LoggerFactory.getLogger(AliOSSUtils.class);

    // 建议从配置文件中读取
    @Value("${aliyun.oss.endpoint}")
    private String endpoint ;
    @Value("${aliyun.oss.bucketName}")
    private String bucketName;
    @Value("${aliyun.oss.region}")
    private String region = "cn-beijing";
    private EnvironmentVariableCredentialsProvider credentialsProvider;

    public AliOSSUtils() throws ClientException {
        try {
            this.credentialsProvider = CredentialsProviderFactory.newEnvironmentVariableCredentialsProvider();
        } catch (ClientException e) {
            logger.error("Failed to initialize OSS credentials provider", e);
            throw e;
        }
    }

    /**
     * 实现上传图片到OSS
     */
    public String upload(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File cannot be null or empty");
        }

        // 生成唯一文件名
        String originalFilename = file.getOriginalFilename();
        String fileExtension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        String fileName = UUID.randomUUID().toString() + fileExtension;

        OSS ossClient = null;
        InputStream inputStream = null;

        try {
            // 创建OSS客户端配置
            ClientBuilderConfiguration clientConfig = new ClientBuilderConfiguration();
            clientConfig.setSignatureVersion(SignVersion.V4);

            // 创建OSS客户端
            ossClient = OSSClientBuilder.create()
                    .endpoint(endpoint)
                    .credentialsProvider(credentialsProvider)
                    .clientConfiguration(clientConfig)
                    .region(region)
                    .build();

            inputStream = file.getInputStream();

            // 创建上传请求
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, fileName, inputStream);

            // 执行上传
            PutObjectResult result = ossClient.putObject(putObjectRequest);
            logger.info("File uploaded successfully: {}", fileName);

            // 构建访问URL
            String url = buildFileUrl(fileName);
            return url;

        } catch (OSSException oe) {
            logger.error("OSS Exception occurred: Error Code: {}, Error Message: {}, Request ID: {}, Host ID: {}",
                    oe.getErrorCode(), oe.getErrorMessage(), oe.getRequestId(), oe.getHostId());
            throw new IOException("OSS operation failed: " + oe.getErrorMessage(), oe);
        } catch (com.aliyun.oss.ClientException ce) {
            logger.error("OSS Client Exception occurred: {}", ce.getMessage());
            throw new IOException("OSS client error: " + ce.getMessage(), ce);
        } finally {
            // 关闭输入流
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    logger.warn("Failed to close input stream", e);
                }
            }
            // 关闭OSS客户端 - 只在这里关闭一次
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }

    /**
     * 构建文件访问URL
     */
    private String buildFileUrl(String fileName) {
        // 更健壮的URL构建方式
        String protocol = endpoint.startsWith("https") ? "https" : "http";
        String domain = endpoint.replaceAll("^https?://", "");
        return String.format("%s://%s.%s/%s", protocol, bucketName, domain, fileName);
    }

    // 建议添加配置的setter方法，便于从配置文件注入
    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public void setRegion(String region) {
        this.region = region;
    }
}

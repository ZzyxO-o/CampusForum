package cn.zuo.service;

import org.springframework.web.multipart.MultipartFile;

public interface VectorService {
    void uploadDocument(MultipartFile file) throws Exception;
}

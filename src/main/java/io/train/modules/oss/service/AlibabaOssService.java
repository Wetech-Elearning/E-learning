package io.train.modules.oss.service;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.List;

public interface AlibabaOssService {
    String upload(File file, String fileHost, HttpSession httpSession);

    String delete(String fileKey,String fileHost);

    List<String> getObjectList(String bucketName, String fileHost);
}

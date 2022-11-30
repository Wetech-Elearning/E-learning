package io.train.common.service.impl;

import io.train.common.excel.ExcelLogs;
import io.train.common.excel.ExcelUtil;
import io.train.common.service.ExcelService;
import io.train.common.utils.R;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * excel接口实现类
 * @author: 李亚杰
 * @version: 1.0
 * @date: 2021/9/7 10:32
 */
@Service
public class ExcelServiceImpl implements ExcelService {

    @Value("${data.template.path}")
    String templatePath;

    @Override
    public Collection<Map> importExcel(MultipartFile file, HttpServletRequest request) {
        Collection<Map> excelData = null;
        try {
            String fileName = templatePath + File.separator + file.getOriginalFilename();
            File newFile = new File(fileName);
            file.transferTo(newFile);
            InputStream inputStream= new FileInputStream(newFile);
            ExcelLogs logs =new ExcelLogs();
            excelData = ExcelUtil.importExcel(Map.class, inputStream, "yyyy/MM/dd HH:mm:ss", logs , 0);
        }catch (Exception e){
            e.printStackTrace();
        }
        return excelData;
    }

    @Override
    public OutputStream exportExcel(List<Map<String,Object>> list, Map<String,String> headMap, OutputStream outputStream) {
        try{
            ExcelUtil.exportExcel(headMap, list, outputStream );
        }catch (Exception e){
            e.printStackTrace();
        }
        return outputStream;
    }

    @Override
    public OutputStream downExcelTemplate(OutputStream outputStream, String templateName) {
        String filePath = templatePath + File.separator + templateName;
        FileInputStream inputStream = null;
        try{
            BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(filePath));
            byte[] buf = new byte[1024];
            int length;
            while ((length = bufferedInputStream.read(buf)) > 0) {
                outputStream.write(buf, 0, length);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            IOUtils.closeQuietly(inputStream);
        }
        return outputStream;
    }
}

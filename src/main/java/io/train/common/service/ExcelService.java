package io.train.common.service;

import io.train.common.utils.R;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.OutputStream;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * excel接口
 * @author: 李亚杰
 * @version: 1.0
 * @date: 2021/9/7 10:31
 */
public interface ExcelService {
    /**
     * 导入excel
     * @author: liyajie
     * @date: 2021/9/7 10:33
     * @param file-文件
     * @param request-请求参数
     * @return com.ancient.common.utils.R
     * @exception:
     * @update:
     * @updatePerson:
     **/
    Collection<Map> importExcel(MultipartFile file, HttpServletRequest request);

    /**
     * 导出excel
     * @author: liyajie
     * @date: 2021/9/7 11:07
     * @param list-导出的数据
     * @param headMap-表头(list中使用LinkedHashMap)
     * @return com.ancient.common.utils.R
     * @exception:
     * @update:
     * @updatePerson:
     **/
    OutputStream exportExcel(List<Map<String, Object>> list, Map<String, String> headMap, OutputStream outputStream);

    /**
     * 下载模板
     * @author: liyajie
     * @date: 2022/3/4 16:02
     * @param templateName
     * @return void
     * @exception:
     * @update:
     * @updatePerson:
     **/
    OutputStream downExcelTemplate(OutputStream outputStream, String templateName);
}

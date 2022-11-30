package io.train.modules.business.administrator.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.train.common.utils.PageUtils;
import io.train.modules.business.administrator.entity.AdministratorsEntity;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2021-07-05 10:12:46
 */
public interface AdministratorsService extends IService<AdministratorsEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 删除
     * @author: liyajie
     * @date: 2021/8/30 0:14
     * @param uuids
     * @return void
     * @exception:
     * @update:
     * @updatePerson:
     **/
    void removeByIds(List<String> uuids);
    
    /**
     * 导出管理员列表
     * @author: liyajie 
     * @date: 2022/7/1 17:56
     * @param response
     * @param fileName
     * @return void
     * @exception:
     * @update:
     * @updatePerson:
     **/
    void exportExcel(HttpServletResponse response, String fileName);
}


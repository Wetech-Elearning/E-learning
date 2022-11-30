package io.train.modules.business.classes.service.impl;

import io.train.modules.business.classes.dao.ClassesCourseDao;
import io.train.modules.business.classes.entity.ClassesCourseEntity;
import io.train.modules.business.classes.service.ClassesCourseService;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.train.common.utils.PageUtils;
import io.train.common.utils.Query;


@Service("classesCourseService")
public class ClassesCourseServiceImpl extends ServiceImpl<ClassesCourseDao, ClassesCourseEntity> implements ClassesCourseService {

	
	
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        QueryWrapper<ClassesCourseEntity> queryWrapper = new QueryWrapper<ClassesCourseEntity>();
        if(null != params.get("className") && !"".equals(params.get("className").toString())){
            queryWrapper.like("class_name",params.get("className"));
        }
        IPage<ClassesCourseEntity> page = this.page(
                new Query<ClassesCourseEntity>().getPage(params),
                queryWrapper
        );
        return new PageUtils(page);
    }

	@Override
	public List<ClassesCourseEntity> listAll(Map<String, Object> params) {
		String classId=params.containsKey("classId")?""+params.get("classId"):"";
		return this.list(new QueryWrapper<ClassesCourseEntity>().eq(StringUtils.isNotBlank(classId),"class_id", classId));
	}

}
package io.train.modules.business.question.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import io.train.common.utils.DateUtils;
import io.train.common.utils.PageUtils;
import io.train.common.utils.Query;
import io.train.modules.business.coursetype.entity.CourseTypeEntity;
import io.train.modules.business.coursetype.service.CourseTypeService;
import io.train.modules.business.question.dao.TranslateQuestionDao;
import io.train.modules.business.question.entity.TranslateQuestionEntity;
import io.train.modules.business.question.service.TranslateQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

@Service("translateQuestionService")
public class TranslateQuestionServiceImpl extends ServiceImpl<TranslateQuestionDao, TranslateQuestionEntity> implements TranslateQuestionService {

    @Autowired
    CourseTypeService courseTypeService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        QueryWrapper<TranslateQuestionEntity> queryWrapper = new QueryWrapper<TranslateQuestionEntity>();
        queryWrapper.eq("delete_flag","0");
        queryWrapper.orderByDesc("create_date");
        IPage<TranslateQuestionEntity> page = this.page(
                new Query<TranslateQuestionEntity>().getPage(params),
                queryWrapper
        );
        List<TranslateQuestionEntity> translateQuestionEntityList = new ArrayList<TranslateQuestionEntity>();
        page.getRecords().stream().forEach((TranslateQuestionEntity translateQuestionEntity) -> {
            //将课程id翻译成课程名称
            CourseTypeEntity courseTypeEntity = courseTypeService.getById(translateQuestionEntity.getSubject());
            translateQuestionEntity.setSubject(courseTypeEntity.getCourseTypeName());
            translateQuestionEntityList.add(translateQuestionEntity);
        });
        page.setRecords(translateQuestionEntityList);

        return new PageUtils(page);
    }

    @Override
    public void removeByIds(List<String> uuids) {
        UpdateWrapper<TranslateQuestionEntity> updateWrapper = new UpdateWrapper<TranslateQuestionEntity>();
        updateWrapper.set("delete_flag", DateUtils.format(new Date(),DateUtils.DATE_TIME_PATTERN2));
        updateWrapper.in("uuid",uuids);
        this.baseMapper.update(null,updateWrapper);
    }

    @Override
    public List<TranslateQuestionEntity> getAllQuestionsBySubject(String subject) {
        QueryWrapper<TranslateQuestionEntity> queryWrapper = new QueryWrapper<TranslateQuestionEntity>();
        queryWrapper.eq("delete_flag",0);
        queryWrapper.eq("subject",subject);
        return this.baseMapper.selectList(queryWrapper);
    }
}

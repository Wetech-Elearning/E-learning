package io.train.modules.business.question.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import io.train.common.utils.DateUtils;
import io.train.common.utils.PageUtils;
import io.train.common.utils.Query;
import io.train.modules.business.coursetype.entity.CourseTypeEntity;
import io.train.modules.business.coursetype.service.CourseTypeService;
import io.train.modules.business.question.dao.DiscussionQuestionDao;
import io.train.modules.business.question.entity.DiscussionQuestionEntity;
import io.train.modules.business.question.service.DiscussionQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;


@Service("discussionQuestionService")
public class DiscussionQuestionServiceImpl extends ServiceImpl<DiscussionQuestionDao, DiscussionQuestionEntity> implements DiscussionQuestionService {

    @Autowired
    CourseTypeService courseTypeService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        QueryWrapper<DiscussionQuestionEntity> queryWrapper = new QueryWrapper<DiscussionQuestionEntity>();
        queryWrapper.eq("delete_flag",0);
        if(null != params.get("question") && !"".equals(params.get("question").toString())){
            queryWrapper.like("question",params.get("question"));
        }
        queryWrapper.orderByDesc("create_date");
        IPage<DiscussionQuestionEntity> page = this.page(
                new Query<DiscussionQuestionEntity>().getPage(params),
                queryWrapper
        );
        List<DiscussionQuestionEntity> discussionQuestionEntityList = new ArrayList<DiscussionQuestionEntity>();
        page.getRecords().stream().forEach((DiscussionQuestionEntity discussionQuestionEntity) -> {
            //将课程id翻译成课程名称
            CourseTypeEntity courseTypeEntity = courseTypeService.getById(discussionQuestionEntity.getSubject());
            discussionQuestionEntity.setSubject(courseTypeEntity.getCourseTypeName());
            discussionQuestionEntityList.add(discussionQuestionEntity);
        });
        page.setRecords(discussionQuestionEntityList);
        return new PageUtils(page);
    }

    @Override
    public void removeByIds(List<String> uuids) {
        UpdateWrapper<DiscussionQuestionEntity> updateWrapper = new UpdateWrapper<DiscussionQuestionEntity>();
        updateWrapper.set("delete_flag", DateUtils.format(new Date(),DateUtils.DATE_TIME_PATTERN2));
        updateWrapper.in("uuid",uuids);
        this.baseMapper.update(null,updateWrapper);
    }

    @Override
    public List<DiscussionQuestionEntity> getAllQuestionsBySubject(String subject) {
        QueryWrapper<DiscussionQuestionEntity> queryWrapper = new QueryWrapper<DiscussionQuestionEntity>();
        queryWrapper.eq("delete_flag",0);
        queryWrapper.eq("subject",subject);
        return this.baseMapper.selectList(queryWrapper);
    }
}

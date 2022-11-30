package io.train.modules.business.rate.controller;

import java.util.List;

import io.train.common.utils.R;
import io.train.modules.business.rate.entity.CourseRateEntity;
import io.train.modules.business.rate.service.CourseRateService;
import io.train.modules.business.rate.vo.CourseRateVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 课程评分相关api
 * @author liyajie
 * @date 2021-09-07 22:47:05
 */
@RestController
@RequestMapping("generator/courserate")
public class CourseRateController {
    @Autowired
    private CourseRateService courseRateService;

    /**
     * 信息
     */
    @RequestMapping("/info/{courseId}")
    public R info(@PathVariable("courseId") Long courseId){
        List<CourseRateEntity> courseRateEntityList = courseRateService.getRateByCourseId(courseId);
        CourseRateVO courseRateVO = new CourseRateVO();
        if(null != courseRateEntityList && courseRateEntityList.size() > 0){
            Integer rate = 0;
            for (CourseRateEntity courseRateEntity :courseRateEntityList) {
                rate += courseRateEntity.getRate();
            }
            courseRateVO.setNum(courseRateEntityList.size());
            courseRateVO.setRate(rate/courseRateEntityList.size());
        }else{
            courseRateVO.setRate(0);
            courseRateVO.setNum(0);
        }
        return R.ok().put("data", courseRateVO);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody CourseRateEntity courseRate){
		courseRateService.saveRate(courseRate);
        return R.ok();
    }
}

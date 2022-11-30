package io.train.modules.business.rate.vo;

import lombok.Data;

/**
 * 课程评分vo
 * @author: 李亚杰
 * @version: 1.0
 * @date: 2021/9/7 23:00
 */
@Data
public class CourseRateVO {
    /**
     * 评分
     **/
    private Integer rate;

    /**
     * 评分的人数
     **/
    private Integer num;
}

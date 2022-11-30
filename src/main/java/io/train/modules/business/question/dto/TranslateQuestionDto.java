package io.train.modules.business.question.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * 翻译题dto
 * @author: 李亚杰
 * @version: 1.0
 * @date: 2021/9/14 1:02
 */
@Data
@AllArgsConstructor
public class TranslateQuestionDto implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 考试课程
     */
    private String subject;
    /**
     * 翻译类型
     */
    private String type;
    /**
     * 试题数量
     */
    private Integer count;
    /**
     * 分数
     */
    private Integer score;
    /**
     * 难度等级
     */
    private String level;

}

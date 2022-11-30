package io.train.modules.business.questionnaire.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 问卷调查详情dto
 * @author: 李亚杰
 * @version: 1.0
 * @date: 2021/10/25 1:31
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionnaireDto implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 试卷id
     */
    private String questionnaireId;
    /**
     * 创建人
     */
    private String creater;
    /**
     * 创建时间
     */
    private Date createDate;
    /**
     * 试题id
     */
    private List<QuestionDto> questionIds = new ArrayList<QuestionDto>();
}

package io.train.modules.business.questionnaire.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 问卷调查详情删除dto
 * @author: 李亚杰
 * @version: 1.0
 * @date: 2021/10/25 1:31
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionnaireIdsDto implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 问卷id
     */
    private String questionnaireId;

    /**
     * 试题id
     */
    private List<String> questionIds = new ArrayList<String>();
}

package io.train.modules.business.questionnaire.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @TODO
 * @author: 李亚杰
 * @version: 1.0
 * @date: 2021/10/25 6:15
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionnaireRecordItem implements Serializable {
    private static final long serialVersionUID = 1L;

    private String questionnaireId;

    private String questionnaireQuestionId;

    private String questionnaireQuestionAnswer;
}

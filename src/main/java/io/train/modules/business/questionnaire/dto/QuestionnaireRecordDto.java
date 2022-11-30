package io.train.modules.business.questionnaire.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 调查问卷回答记录
 * @author: 李亚杰
 * @version: 1.0
 * @date: 2021/10/25 6:12
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionnaireRecordDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String userId;

    private List<QuestionnaireRecordItem> questionnaireRecords = new ArrayList<QuestionnaireRecordItem>();
}

package io.train.modules.business.paperquestion.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 试卷和题目关联的dto
 * @author: 李亚杰
 * @version: 1.0
 * @date: 2021/9/11 16:39
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaperQuestionDto implements Serializable{
    private static final long serialVersionUID = 1L;
    /**
     * 试卷id
     */
    private String paperId;
    /**
     * 试题id
     */
    private List<QuestionDto> questionIds = new ArrayList<QuestionDto>();

}

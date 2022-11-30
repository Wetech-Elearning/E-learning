package io.train.modules.business.paperquestion.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 试题dto
 * @author: 李亚杰
 * @version: 1.0
 * @date: 2021/10/18 6:00
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String type;

    private String key;

    private String label;
    
    private int num;
    
    private String subject;
}

package io.train.modules.business.translate.dto;

import io.train.modules.business.translate.entity.TranslateEntity;
import lombok.Data;

import java.util.List;

/**
 * @TODO
 * @author: 李亚杰
 * @version: 1.0
 * @date: 2021/8/31 0:53
 */
@Data
public class TranslateDto {
    private List<TranslateEntity> translateEntityList;

    private String relatedCourseId;
}

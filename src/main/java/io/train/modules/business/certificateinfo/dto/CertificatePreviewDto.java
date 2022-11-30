package io.train.modules.business.certificateinfo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 证书预览接口dto
 * @author:liyajie
 * @createTime:2022/6/27 17:51
 * @version:1.0
 */
@Data
@AllArgsConstructor
public class CertificatePreviewDto {

    /**
     * 证书id
     */
    private String uuid;
    /**
     * 学员id
     */
    private String userId;
}

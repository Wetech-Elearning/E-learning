package io.train.modules.business.paperquestion.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.train.common.utils.PageUtils;
import io.train.modules.business.paperquestion.dto.PaperQuestionDto;
import io.train.modules.business.paperquestion.entity.PaperQuestionEntity;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2021-07-05 10:52:42
 */
public interface PaperQuestionService extends IService<PaperQuestionEntity> {

    PageUtils queryPage(Map<String, Object> params);
    
    List<PaperQuestionEntity> queryAll(Map<String, Object> params);

    /**
     * 根据试卷id查询该试卷下的所有试题
     * @author: liyajie
     * @date: 2021/9/12 12:35
     * @param paperId
     * @return java.util.List<io.train.modules.business.common.paperquestion.entity.PaperQuestionEntity>
     * @exception:
     * @update:
     * @updatePerson:
     **/
    List<PaperQuestionEntity> getByPaperId(String paperId);

    /**
     * 根据试题id查询
     * @author: liyajie
     * @date: 2021/9/12 12:36
     * @param questionId
     * @return io.train.modules.business.common.paperquestion.entity.PaperQuestionEntity
     * @exception:
     * @update:
     * @updatePerson:
     **/
    PaperQuestionEntity getByQuestionId(String questionId);

    /**
     * 根据试题id和试卷id查询
     * @author: liyajie
     * @date: 2021/9/12 12:36
     * @param questionId
     * @return io.train.modules.business.common.paperquestion.entity.PaperQuestionEntity
     * @exception:
     * @update:
     * @updatePerson:
     **/
    PaperQuestionEntity getByQuestionIdAndPaperId(String questionId,String paperId);

    /**
     * 根据试题id删除
     * @author: liyajie
     * @date: 2021/9/12 22:20
     * @param questionIds
     * @return void
     * @exception:
     * @update:
     * @updatePerson:
     **/
    void deleteByQuestionIds(String[] questionIds);
    
    /**
     * 按要求随机添加试卷
     * @author: liyajie 
     * @date: 2022/8/8 15:49
     * @param 
     * @return void
     * @exception:
     * @update:
     * @updatePerson:
     **/
    void randomAddQuestions(PaperQuestionDto paperQuestionDto);
}


package io.train.modules.business.questionnaire.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import io.train.common.utils.PageUtils;
import io.train.common.utils.R;
import io.train.modules.business.lecturer.entity.LecturerEntity;
import io.train.modules.business.lecturer.service.LecturerService;
import io.train.modules.business.questionnaire.dto.QuestionnaireIdsDto;
import io.train.modules.business.questionnaire.entity.QuestionnaireDetailEntity;
import io.train.modules.business.questionnaire.entity.QuestionnaireEntity;
import io.train.modules.business.questionnaire.entity.QuestionnaireRelationEntity;
import io.train.modules.business.questionnaire.service.QuestionnaireDetailService;
import io.train.modules.business.questionnaire.service.QuestionnaireRelationService;
import io.train.modules.business.questionnaire.service.QuestionnaireService;
import io.train.modules.business.student.entity.StudentEntity;
import io.train.modules.business.student.service.StudentService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;



/**
 *
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2021-10-24 04:53:55
 */
@RestController
@RequestMapping("generator/questionnaire")
public class QuestionnaireController {
    @Autowired
    private QuestionnaireService questionnaireService;

    @Autowired
    private QuestionnaireDetailService questionnaireDetailService;

    @Autowired
    private QuestionnaireRelationService questionnaireRelationService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private LecturerService lecturerService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("business:questionnaire:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = questionnaireService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{uuid}")
    @RequiresPermissions("business:questionnaire:info")
    public R info(@PathVariable("uuid") Long uuid){
		QuestionnaireEntity questionnaire = questionnaireService.getById(uuid);

        return R.ok().put("questionnaire", questionnaire);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("business:questionnaire:save")
    public R save(@RequestBody QuestionnaireEntity questionnaire){
		questionnaireService.save(questionnaire);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("business:questionnaire:update")
    public R update(@RequestBody QuestionnaireEntity questionnaire){
		questionnaireService.updateById(questionnaire);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("business:questionnaire:delete")
    public R delete(@RequestBody Long[] uuids){
		questionnaireService.removeByIds(Arrays.asList(uuids));

        return R.ok();
    }

    /**
     * 发布调查问卷
     * @author: liyajie
     * @date: 2021/10/24 23:45
     * @param uuid
     * @return io.train.common.utils.R
     * @exception:
     * @update:
     * @updatePerson:
     **/
    @RequestMapping("/release/{uuid}")
    //@RequiresPermissions("business:questionnaire:release")
    public R release(@PathVariable("uuid") String uuid){
        QuestionnaireEntity questionnaireEntity = questionnaireService.getById(uuid);
        if(null != questionnaireEntity && questionnaireEntity.getStatus() == 1){
            return R.error("该问卷调查已经发布过了，请选择其他问卷");
        }
        //给学生发送问卷调查
        List<StudentEntity> studentEntityList = studentService.list();
        if(null != studentEntityList && studentEntityList.size() > 0){
            for (StudentEntity studentEntity:studentEntityList) {
                QuestionnaireRelationEntity questionnaireRelationEntity = new QuestionnaireRelationEntity();
                questionnaireRelationEntity.setQuestionnaireId(uuid);
                questionnaireRelationEntity.setAcceptUserId(String.valueOf(studentEntity.getUuid()));
                questionnaireRelationEntity.setDeleteFlag("0");
                questionnaireRelationEntity.setStatus(0);
                questionnaireRelationService.save(questionnaireRelationEntity);
            }
        }
        //给讲师发送问卷调查
        List<LecturerEntity> lecturerEntityList = lecturerService.list();
        if(null != lecturerEntityList && lecturerEntityList.size() > 0){
            for (LecturerEntity lecturerEntity:lecturerEntityList) {
                QuestionnaireRelationEntity questionnaireRelationEntity = new QuestionnaireRelationEntity();
                questionnaireRelationEntity.setQuestionnaireId(uuid);
                questionnaireRelationEntity.setAcceptUserId(String.valueOf(lecturerEntity.getUuid()));
                questionnaireRelationEntity.setDeleteFlag("0");
                questionnaireRelationEntity.setStatus(0);
                questionnaireRelationService.save(questionnaireRelationEntity);
            }
        }
        //更新问卷状态
        questionnaireService.updateStatus(1, uuid);
        return R.ok();
    }

    /**
     * 根据问卷id查询该问卷下的题目
     * @author: liyajie
     * @date: 2021/10/25 2:22
     * @param uuid
     * @return io.train.common.utils.R
     * @exception:
     * @update:
     * @updatePerson:
     **/
    @RequestMapping("/getRightQuestionnaire/{uuid}")
    @RequiresPermissions("business:questionnaire:list")
    public R getRightQuestionnaireById(@PathVariable("uuid") String uuid){
        List<QuestionnaireDetailEntity> questionnaireDetailEntityList = questionnaireDetailService.getQuestionnaireById(uuid);
        return R.ok().put("data",questionnaireDetailEntityList);
    }

    /**
     * 根据题目id删除题目
     * @author: liyajie
     * @date: 2021/10/25 2:22
     * @param questionnaireIdsDto
     * @return io.train.common.utils.R
     * @exception:
     * @update:
     * @updatePerson:
     **/

    @RequestMapping("/deleteByQuestionIds")
    @RequiresPermissions("business:questionnaire:delete")
    public R deleteByQuestionIds(@RequestBody QuestionnaireIdsDto questionnaireIdsDto){
        List<String> questionIds = questionnaireIdsDto.getQuestionIds();
        questionnaireDetailService.deleteByQuestionIds(questionIds,questionnaireIdsDto.getQuestionnaireId());
        return R.ok();
    }


}

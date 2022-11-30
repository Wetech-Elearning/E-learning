package io.train.modules.business.paperquestion.controller;

import java.util.*;
import java.util.stream.Collectors;

import io.train.modules.business.lecturer.entity.LecturerEntity;
import io.train.modules.business.lecturer.service.LecturerService;
import io.train.modules.business.paper.entity.ExamPaperInfosEntity;
import io.train.modules.business.paper.service.ExamPaperInfosService;
import io.train.modules.business.paperquestion.dto.PaperQuestionDto;
import io.train.modules.business.paperquestion.dto.QuestionDto;
import io.train.modules.business.paperquestion.entity.PaperQuestionEntity;
import io.train.modules.business.paperquestion.service.PaperQuestionService;
import io.train.modules.business.question.entity.*;
import io.train.modules.business.relation.entity.UserExamRecordEntity;
import io.train.modules.business.relation.service.UserExamInfoService;
import io.train.modules.business.relation.service.UserExamRecordService;
import io.train.modules.business.question.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import io.train.common.utils.PageUtils;
import io.train.common.utils.R;

/**
 * 
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2021-07-05 10:52:42
 */
@RestController
@RequestMapping("generator/paperQuestion")
public class PaperQuestionController {
    @Autowired
    private PaperQuestionService paperQuestionService;

    @Autowired
    private ExamPaperInfosService examPaperInfosService;

    @Autowired
    private FillQuestionService fillQuestionService;

    @Autowired
    private JudgeQuestionService judgeQuestionService;

    @Autowired
    private MultiQuestionService multiQuestionService;

    @Autowired
    private DiscussionQuestionService discussionQuestionService;

    @Autowired
    private TranslateQuestionService translateQuestionService;

    @Autowired
    LecturerService lecturerService;
    
    @Autowired
    UserExamInfoService userExamInfoService;
    
    @Autowired
    UserExamRecordService userExamRecordService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("business:paperQuestion:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = paperQuestionService.queryPage(params);

        return R.ok().put("page", page);
    }
    
    @RequestMapping("/listall")
    public R listAll(@RequestParam Map<String, Object> params){
    	List<PaperQuestionEntity> page = paperQuestionService.queryAll(params);
        return R.ok().put("data", page);
    }

    /**
     * 信息
     */
    @RequestMapping("/info")
    //@RequiresPermissions("business:paperQuestion:info")
    public R info(@RequestParam Map<String, Object> params){
        String uuid = String.valueOf(params.get("uuid"));
        String userId = String.valueOf(params.get("userId"));
        String isPractice = String.valueOf(params.get("isPractice"));
        String pageName = String.valueOf(params.get("pageName"));
        Integer examNum = userExamInfoService.getMaxNum(uuid,userId);
        Map<String,Object> map = new HashMap<String,Object>();
        List<MultiQuestionEntity> radioQuestionEntityList = new ArrayList<MultiQuestionEntity>();
        List<MultiQuestionEntity> multiQuestionEntityList = new ArrayList<MultiQuestionEntity>();
        List<FillQuestionEntity> fillQuestionEntityList = new ArrayList<FillQuestionEntity>();
        List<JudgeQuestionEntity> judgeQuestionEntityList = new ArrayList<JudgeQuestionEntity>();
        List<DiscussionQuestionEntity> discussionQuestionEntityList = new ArrayList<DiscussionQuestionEntity>();
        List<TranslateQuestionEntity> translateQuestionEntityList = new ArrayList<TranslateQuestionEntity>();
		List<PaperQuestionEntity> questionEntityList = paperQuestionService.getByPaperId(uuid);
        for (PaperQuestionEntity paperQuestionEntity : questionEntityList) {
            if(paperQuestionEntity.getQuestionType().equals("单选题")){
                MultiQuestionEntity multiQuestionEntity = multiQuestionService.getById(paperQuestionEntity.getQuestionId());
                if("true".equals(isPractice)){
                    UserExamRecordEntity userExamRecordEntity = userExamRecordService.getCurrentUserAnswers(uuid,multiQuestionEntity.getUuid(),userId,examNum);
                    if(null != userExamRecordEntity){
                        multiQuestionEntity.setUserAnswer(userExamRecordEntity.getQuestionAnswer());
                    }
                }
                radioQuestionEntityList.add(multiQuestionEntity);
            }
            if(paperQuestionEntity.getQuestionType().equals("多选题")){
                MultiQuestionEntity multiQuestionEntity = multiQuestionService.getById(paperQuestionEntity.getQuestionId());
                if("true".equals(isPractice)){
                    UserExamRecordEntity userExamRecordEntity = userExamRecordService.getCurrentUserAnswers(uuid,multiQuestionEntity.getUuid(),userId,examNum);
                    if(null != userExamRecordEntity){
                        multiQuestionEntity.setUserAnswer(userExamRecordEntity.getQuestionAnswer());
                    }
                }
                multiQuestionEntityList.add(multiQuestionEntity);
            }
            if(paperQuestionEntity.getQuestionType().equals("判断题")){
                JudgeQuestionEntity judgeQuestionEntity = judgeQuestionService.getById(paperQuestionEntity.getQuestionId());
                if("true".equals(isPractice)){
                    UserExamRecordEntity userExamRecordEntity = userExamRecordService.getCurrentUserAnswers(uuid,judgeQuestionEntity.getUuid(),userId,examNum);
                    if(null != userExamRecordEntity){
                        judgeQuestionEntity.setUserAnswer(userExamRecordEntity.getQuestionAnswer());
                    }
                }
                judgeQuestionEntityList.add(judgeQuestionEntity);
            }
            if(paperQuestionEntity.getQuestionType().equals("填空题")){
                FillQuestionEntity fillQuestionEntity = fillQuestionService.getById(paperQuestionEntity.getQuestionId());
                if("true".equals(isPractice)){
                    UserExamRecordEntity userExamRecordEntity = userExamRecordService.getCurrentUserAnswers(uuid,fillQuestionEntity.getUuid(),userId,examNum);
                    if(null != userExamRecordEntity){
                        fillQuestionEntity.setUserAnswer(userExamRecordEntity.getQuestionAnswer());
                    }
                }
                fillQuestionEntityList.add(fillQuestionEntity);
            }
            if(paperQuestionEntity.getQuestionType().equals("问答题")){
                DiscussionQuestionEntity discussionQuestionEntity = discussionQuestionService.getById(paperQuestionEntity.getQuestionId());
                if("true".equals(isPractice)){
                    UserExamRecordEntity userExamRecordEntity = userExamRecordService.getCurrentUserAnswers(uuid,discussionQuestionEntity.getUuid(),userId,examNum);
                    if(null != userExamRecordEntity){
                        discussionQuestionEntity.setUserAnswer(userExamRecordEntity.getQuestionAnswer());
                    }
                }
                discussionQuestionEntityList.add(discussionQuestionEntity);
            }
            if(paperQuestionEntity.getQuestionType().equals("翻译题")){
                TranslateQuestionEntity translateQuestionEntity = translateQuestionService.getById(paperQuestionEntity.getQuestionId());
                if("true".equals(isPractice)){
                    UserExamRecordEntity userExamRecordEntity = userExamRecordService.getCurrentUserAnswers(uuid,translateQuestionEntity.getUuid(),userId,examNum);
                    if(null != userExamRecordEntity){
                        translateQuestionEntity.setUserAnswer(userExamRecordEntity.getQuestionAnswer());
                    }
                }
                translateQuestionEntityList.add(translateQuestionEntity);
            }
        }
        map.put("radioQuestionEntityList",radioQuestionEntityList);
        map.put("multiQuestionEntityList",multiQuestionEntityList);
        map.put("judgeQuestionEntityList",judgeQuestionEntityList);
        map.put("fillQuestionEntityList",fillQuestionEntityList);
        map.put("discussionQuestionEntityList",discussionQuestionEntityList);
        map.put("translateQuestionEntityList",translateQuestionEntityList);
        ExamPaperInfosEntity examPaperInfos = examPaperInfosService.getById(uuid);
        //转换讲师名称
        LecturerEntity lecturerEntity = lecturerService.getByAccount(examPaperInfos.getCreater());
        if(null != lecturerEntity){
            examPaperInfos.setCreater(lecturerEntity.getSurname() + lecturerEntity.getLecturerName());
        }
        examPaperInfos.setExamPaperDetail(map);
        //当前考试次数
        if("answer".equals(pageName)){
            if(!"true".equals(isPractice)){
                examPaperInfos.setExamNum(examNum + 1);
            }else{
                examPaperInfos.setExamNum(examNum);
            }
        }
        return R.ok().put("examPaperInfos", examPaperInfos);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("business:paperQuestion:add")
    public R save(@RequestBody PaperQuestionDto paperQuestionDto){
        List<QuestionDto> questionList = paperQuestionDto.getQuestionIds();
        for (QuestionDto question : questionList) {
            PaperQuestionEntity paperQuestion = paperQuestionService.getByQuestionIdAndPaperId(question.getKey(),paperQuestionDto.getPaperId());
            if(null != paperQuestion){
                continue;
            }
            PaperQuestionEntity paperQuestionEntity = new PaperQuestionEntity();
            paperQuestionEntity.setPaperId(paperQuestionDto.getPaperId());
            paperQuestionEntity.setQuestionId(question.getKey());
            paperQuestionEntity.setQuestionType(question.getType());
            paperQuestionEntity.setQuestion(question.getLabel());
            paperQuestionService.save(paperQuestionEntity);
        }
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("business:paperQuestion:update")
    public R update(@RequestBody PaperQuestionEntity examInfo){
        paperQuestionService.updateById(examInfo);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("business:paperQuestion:delete")
    public R delete(@RequestBody String[] uuids){
        paperQuestionService.removeByIds(Arrays.asList(uuids));
        return R.ok();
    }

    /**
     * 根据试题id删除
     */
    @RequestMapping("/deleteByQuestionIds")
    //@RequiresPermissions("business:paperQuestion:delete")
    public R deleteByQuestionIds(@RequestBody String[] questionIds){
        paperQuestionService.deleteByQuestionIds(questionIds);
        return R.ok();
    }

    /**
     * 信息
     */
    @RequestMapping("/getRightQuestions/{uuid}")
    //@RequiresPermissions("business:paperQuestion:info")
    public R getRightQuestions(@PathVariable("uuid") String uuid){
        List<PaperQuestionEntity> questionEntityList = paperQuestionService.getByPaperId(uuid);
        List<String> data = questionEntityList.stream().map(paperQuestionEntity->paperQuestionEntity.getQuestionId()).collect(Collectors.toList());
        return R.ok().put("data", data);
    }

    /**
     * 按要求随机添加试卷
     * @author: liyajie 
     * @date: 2022/8/8 15:43
     * @param paperQuestionDto
     * @return io.train.common.utils.R
     * @exception:
     * @update:
     * @updatePerson:
     **/
    @RequestMapping("/randomAddQuestions")
    public R randomAddQuestions(@RequestBody PaperQuestionDto paperQuestionDto){
        paperQuestionService.randomAddQuestions(paperQuestionDto);
        return R.ok();
    }
    
}

package io.train.modules.business.paper.controller;

import java.util.*;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.train.modules.business.course.entity.CourseEntity;
import io.train.modules.business.course.service.CourseService;
import io.train.modules.business.lecturer.entity.LecturerEntity;
import io.train.modules.business.lecturer.service.LecturerService;
import io.train.modules.business.paper.entity.ExamPaperInfosEntity;
import io.train.modules.business.paper.service.ExamPaperInfosService;
import io.train.modules.business.question.service.*;
import io.train.modules.business.record.entity.StudentStudyRecordEntity;
import io.train.modules.business.record.service.StudentStudyRecordService;
import io.train.modules.business.relation.entity.UserExamRecordEntity;
import io.train.modules.business.relation.service.UserExamInfoService;
import io.train.modules.business.relation.service.UserExamRecordService;
import io.train.modules.business.question.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.train.common.utils.PageUtils;
import io.train.common.utils.R;



/**
 * 
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2021-07-05 10:51:50
 */
@RestController
@RequestMapping("generator/exampaperinfos")
public class ExamPaperInfosController {
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
    private CourseService courseService;

    @Autowired
    private StudentStudyRecordService studentStudyRecordService;

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
    //@RequiresPermissions("business:exampaperinfos:exampaperinfos:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = examPaperInfosService.queryPage(params);

        return R.ok().put("page", page);
    }
    
    @RequestMapping("/listall")
    //@RequiresPermissions("business:exampaperinfos:exampaperinfos:list")
    public R listAll(@RequestParam Map<String, Object> params){
    	List<ExamPaperInfosEntity> data = examPaperInfosService.queryAll(params);
        return R.ok().put("data", data);
    }
    
    @RequestMapping("/listByUser")
    public R listByUser(@RequestParam Map<String, Object> params){
    	String userId = ""+params.get("userId");
        String examPaperName = "";
        if(null != params.get("examPaperName") && !"".equals(params.get("examPaperName").toString())){
            examPaperName = "%" + params.get("examPaperName") + "%";
        }
        int start = -1;
    	int limit =0;
    	if(params.containsKey("page") && params.containsKey("limit")) {
    		int startpage = new Integer(""+params.get("page"));
    		limit = new Integer(""+params.get("limit"));
    		start = (startpage-1)*limit;
    	}
    	List<ExamPaperInfosEntity> data = examPaperInfosService.listByUser(userId,examPaperName,start,limit);
    	int total = examPaperInfosService.listByUserCount(userId,examPaperName);
    	return R.ok().put("data", data).put("total", total);
    }
    
    @RequestMapping("/listByStudent")
    public R listByStudent(@RequestParam Map<String, Object> params){
    	String userId = ""+params.get("userId");
    	String examPaperName = "";
    	int start = -1;
    	int limit =0;
    	if(params.containsKey("page") && params.containsKey("limit")) {
    		int startpage = new Integer(""+params.get("page"));
    		limit = new Integer(""+params.get("limit"));
    		start = (startpage-1)*limit;
    	}
    	List<ExamPaperInfosEntity> data = examPaperInfosService.listByUser(userId,examPaperName,start,limit);
    	int total = examPaperInfosService.listByUserCount(userId,examPaperName);
    	return R.ok().put("data", data).put("total", total);
    }
    
    /**
     * 根据班级查询考试任务
     * @author: liyajie 
     * @date: 2022/6/13 17:46
     * @param params
     * @return io.train.common.utils.R
     * @exception:
     * @update:
     * @updatePerson:
     **/
    @RequestMapping("/listByClassId")
    public R listByClassId(@RequestParam Map<String, Object> params){
    	String classId = ""+params.get("classId");
    	String userId = ""+params.get("userId");
    	List<ExamPaperInfosEntity> data = examPaperInfosService.listByClassId(classId);
    	if(null != data && data.size() > 0){
            for (ExamPaperInfosEntity examPaperInfosEntity : data) {
                Integer scores = 0;
                Integer examNum = userExamInfoService.getMaxExamNumByPaperIdAndUserId(examPaperInfosEntity.getUuid(),userId);
                List<UserExamRecordEntity> userExamRecordEntityList = userExamRecordService.getCurrentUserPapteAnswers(examPaperInfosEntity.getUuid(),userId,examNum);
                for (UserExamRecordEntity userExamRecordEntity : userExamRecordEntityList) {
                    scores += Integer.parseInt(userExamRecordEntity.getScore());
                }
                examPaperInfosEntity.setAnswerScore(scores);
            }
        }
    	return R.ok().put("data", data);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{uuid}")
    //@RequiresPermissions("business:exampaperinfos:exampaperinfos:info")
    public R info(@PathVariable("uuid") String uuid){
        Map map = new HashMap();
		ExamPaperInfosEntity examPaperInfos = examPaperInfosService.getById(uuid);
        //转换讲师名称
        LecturerEntity lecturerEntity = lecturerService.getByAccount(examPaperInfos.getCreater());
        if(null != lecturerEntity){
            examPaperInfos.setCreater(lecturerEntity.getSurname() + lecturerEntity.getLecturerName());
        }
        //选择题
        List<MultiQuestionEntity> multiQuestionEntityList = multiQuestionService.getListByQuestionId(uuid);
        List<MultiQuestionEntity> multiQuestionList = new ArrayList<MultiQuestionEntity>();
        List<MultiQuestionEntity> radioQuestionList = new ArrayList<MultiQuestionEntity>();
        for (MultiQuestionEntity multiQuestionEntity : multiQuestionEntityList) {
            //单选
            if("radio".equals(multiQuestionEntity.getQuestionType())){
                radioQuestionList.add(multiQuestionEntity);
            }
            //多选
            if("multi".equals(multiQuestionEntity.getQuestionType())){
                multiQuestionList.add(multiQuestionEntity);
            }
        }
        map.put("multiQuestionEntityList",multiQuestionList);
        map.put("radioQuestionEntityList",radioQuestionList);
        //填空题
        List<FillQuestionEntity> fillQuestionEntityList = fillQuestionService.getListByQuestionId(uuid);
        map.put("fillQuestionEntityList",fillQuestionEntityList);
        //判断题
        List<JudgeQuestionEntity> judgeQuestionEntityList = judgeQuestionService.getListByQuestionId(uuid);
        map.put("judgeQuestionEntityList",judgeQuestionEntityList);
        examPaperInfos.setExamPaperDetail(map);
        return R.ok().put("examPaperInfos", examPaperInfos);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("business:exampaperinfos:exampaperinfos:save")
    public R save(@RequestBody ExamPaperInfosEntity examPaperInfos){
		examPaperInfosService.save(examPaperInfos);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("business:exampaperinfos:exampaperinfos:update")
    public R update(@RequestBody ExamPaperInfosEntity examPaperInfos){
		examPaperInfosService.updateById(examPaperInfos);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("business:exampaperinfos:exampaperinfos:delete")
    public R delete(@RequestBody String[] uuids){
		examPaperInfosService.removeByIds(Arrays.asList(uuids));

        return R.ok();
    }

    /**
     * 初始化试卷枚举
     * @author: liyajie
     * @date: 2021/7/28 23:41
     * @param
     * @return io.train.common.utils.R
     * @exception:
     * @update:
     * @updatePerson:
     */
    @RequestMapping("/initPaperOptions")
    public R initPaperOptions(){
        QueryWrapper queryWrapper = new QueryWrapper<ExamPaperInfosEntity>();
        List<ExamPaperInfosEntity> examPaperInfosEntityList =  examPaperInfosService.initPaperOptions();
        return R.ok().put("data",examPaperInfosEntityList);
    }

    /**
     * 发布/取消发布试卷
     * @author: liyajie
     * @date: 2021/7/28 23:41
     * @param
     * @return io.train.common.utils.R
     * @exception:
     * @update:
     * @updatePerson:
     */
    @RequestMapping("/updateStatus")
    public R updateStatus(@RequestParam Map<String, Object> params){
        String uuid = String.valueOf(params.get("uuid"));
        String status = String.valueOf(params.get("status"));
        examPaperInfosService.updateStatus(uuid,status);
        return R.ok();
    }

    /**
     * @Description //TODO
     * @author: liyajie
     * @date: 2021/9/8 2:13
     * @param params
     * @return io.train.common.utils.R
     * @exception:
     * @update:
     * @updatePerson:
     **/
    @RequestMapping("/getAllQuestionsBySubject")
    public R getAllQuestionsBySubject(@RequestParam Map<String, Object> params){
        String subject = String.valueOf(params.get("subject"));
        Map map = new HashMap();
        //选择题
        List<MultiQuestionEntity> multiQuestionEntityList = multiQuestionService.getAllQuestionsBySubject(subject);
        List<MultiQuestionEntity> multiQuestionList = new ArrayList<MultiQuestionEntity>();
        List<MultiQuestionEntity> radioQuestionList = new ArrayList<MultiQuestionEntity>();
        for (MultiQuestionEntity multiQuestionEntity : multiQuestionEntityList) {
            //单选
            if("radio".equals(multiQuestionEntity.getQuestionType())){
                radioQuestionList.add(multiQuestionEntity);
            }
            //多选
            if("multi".equals(multiQuestionEntity.getQuestionType())){
                multiQuestionList.add(multiQuestionEntity);
            }
        }
        map.put("multiQuestionEntityList",multiQuestionList);
        map.put("radioQuestionEntityList",radioQuestionList);
        //填空题
        List<FillQuestionEntity> fillQuestionEntityList = fillQuestionService.getAllQuestionsBySubject(subject);
        map.put("fillQuestionEntityList",fillQuestionEntityList);
        //判断题
        List<JudgeQuestionEntity> judgeQuestionEntityList = judgeQuestionService.getAllQuestionsBySubject(subject);
        map.put("judgeQuestionEntityList",judgeQuestionEntityList);
        //问答题
        List<DiscussionQuestionEntity> discussionQuestionEntityList =  discussionQuestionService.getAllQuestionsBySubject(subject);
        map.put("discussionQuestionEntityList",discussionQuestionEntityList);
        //中日文翻译题
        List<TranslateQuestionEntity> translateQuestionEntityList =  translateQuestionService.getAllQuestionsBySubject(subject);
        map.put("translateQuestionEntityList",translateQuestionEntityList);
        return R.ok().put("data",map);
    }

    /**
     * 查下试卷是否可以进行考试（试卷关联的课程学习完才可以考试）
     * @author: liyajie
     * @date: 2021/9/26 21:45
     * @param params
     * @return io.train.common.utils.R
     * @exception:
     * @update:
     * @updatePerson:
     **/
    @RequestMapping("/isExam")
    public R info(@RequestParam Map<String, Object> params) {
        Boolean examFlag = true;
        String uuid = String.valueOf(params.get("uuid"));
        String userId = String.valueOf(params.get("userId"));
        //查下课程id下的课时信息
        List<CourseEntity> courseEntityList = courseService.getCourseEntityByCourseTypeAndStatus(uuid,"876ff807b5c84374b101be71ba7efd3f");
        if(null != courseEntityList && courseEntityList.size() > 0){
            for (CourseEntity courseEntity : courseEntityList) {
                StudentStudyRecordEntity studentStudyRecordEntity = studentStudyRecordService.getRecordByUserIdAndCourseId(userId,courseEntity.getUuid());
                if(null != studentStudyRecordEntity){
                    Integer isFiished = studentStudyRecordEntity.getIsFinished();
                    if(0 == isFiished){
                        examFlag = false;
                        break;
                    }
                }else{
                    examFlag = false;
                    break;
                }
            }
        }

        return R.ok().put("data",examFlag);
    }
}

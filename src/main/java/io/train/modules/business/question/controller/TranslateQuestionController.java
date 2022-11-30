package io.train.modules.business.question.controller;

import java.util.*;

import io.train.common.utils.PageUtils;
import io.train.common.utils.R;
import io.train.modules.business.course.entity.CourseEntity;
import io.train.modules.business.course.service.CourseService;
import io.train.modules.business.paperquestion.entity.PaperQuestionEntity;
import io.train.modules.business.paperquestion.service.PaperQuestionService;
import io.train.modules.business.question.dto.TranslateQuestionDto;
import io.train.modules.business.question.entity.TranslateQuestionEntity;
import io.train.modules.business.question.service.TranslateQuestionService;
import io.train.modules.business.translate.entity.TranslateEntity;
import io.train.modules.business.translate.service.TranslateService;
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
 * @date 2021-09-12 18:37:56
 */
@RestController
@RequestMapping("generator/translatequestion")
public class TranslateQuestionController {
    @Autowired
    private TranslateQuestionService translateQuestionService;

    @Autowired
    private TranslateService translateService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private PaperQuestionService paperQuestionService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("business:translatequestion:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = translateQuestionService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{uuid}")
    @RequiresPermissions("business:translatequestion:info")
    public R info(@PathVariable("uuid") Long uuid){
		TranslateQuestionEntity translateQuestion = translateQuestionService.getById(uuid);

        return R.ok().put("translateQuestion", translateQuestion);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("business:translatequestion:add")
    public R save(@RequestBody TranslateQuestionEntity translateQuestion){
		translateQuestionService.save(translateQuestion);

        return R.ok();
    }

    /**
     * 自动保存
     */
    @RequestMapping("/autoSave")
    @RequiresPermissions("business:translatequestion:add")
    public R autoSave(@RequestBody TranslateQuestionDto translateQuestionDto){
        List<CourseEntity> courseEntityList = courseService.getCourseEntityByCourseTypeIdAndCourseType(translateQuestionDto.getSubject(),"translate");
        String zh = "";
        String jpa = "";
        //该二级课程包下的所有的试题
        List<TranslateEntity> allTranslateEntityList = new ArrayList<TranslateEntity>();
        if(null != courseEntityList && courseEntityList.size() > 0){
            for (CourseEntity courseEntity : courseEntityList) {
                List<TranslateEntity> translateEntityList = translateService.getTranslateEntityByRelatedCourseId(String.valueOf(courseEntity.getUuid()));
                for (TranslateEntity translateEntity : translateEntityList) {
                    allTranslateEntityList.add(translateEntity);
                }
            }
        }
        //该二级课程包下已经存在的试题
        List<TranslateQuestionEntity> translateQuestionEntityList = translateQuestionService.getAllQuestionsBySubject(translateQuestionDto.getSubject());
        List<TranslateEntity> unaddedTranslateEntityList = new ArrayList<TranslateEntity>();
        if(null != translateQuestionEntityList && translateQuestionEntityList.size() > 0){
            //从所有的试题中排除已经存在的试题
            for (TranslateEntity translateEntity : allTranslateEntityList) {
                boolean flag = true;
                for(TranslateQuestionEntity translateQuestionEntity : translateQuestionEntityList){
                    if(translateEntity.getChinese().equals(translateQuestionEntity.getQuestion())
                            || translateEntity.getChinese().equals(translateQuestionEntity.getAnswer())
                            || translateEntity.getJapanese().equals(translateQuestionEntity.getQuestion())
                            || translateEntity.getJapanese().equals(translateQuestionEntity.getAnswer())){
                        flag = false;
                    }
                }
                if(flag){
                    unaddedTranslateEntityList.add(translateEntity);
                }
            }
        }else{
            unaddedTranslateEntityList.addAll(allTranslateEntityList);
        }
        if(unaddedTranslateEntityList.size() > 0){
            Random random = new Random();
            if("1".equals(translateQuestionDto.getType())){
                for(int i=0;i<translateQuestionDto.getCount();i++){
                    int index = 0;
                    if(unaddedTranslateEntityList.size() > 1){
                        index = random.nextInt(unaddedTranslateEntityList.size()-1)%(unaddedTranslateEntityList.size());
                    }
                    TranslateQuestionEntity translateQuestion = new TranslateQuestionEntity();
                    translateQuestion.setSubject(translateQuestionDto.getSubject());
                    translateQuestion.setType("1");
                    translateQuestion.setQuestion(unaddedTranslateEntityList.get(index).getChinese());
                    translateQuestion.setAnswer(unaddedTranslateEntityList.get(index).getJapanese());
                    translateQuestion.setAnalysis(unaddedTranslateEntityList.get(index).getChinese() + "对应的日文是：" + unaddedTranslateEntityList.get(index).getJapanese());
                    translateQuestion.setLevel(translateQuestionDto.getLevel());
                    translateQuestion.setScore(translateQuestionDto.getScore());
                    translateQuestion.setDeleteFlag("0");
                    translateQuestion.setCreateDate(new Date());
                    translateQuestionService.save(translateQuestion);
                    unaddedTranslateEntityList.remove(unaddedTranslateEntityList.get(index));
                }
            }
            if("2".equals(translateQuestionDto.getType())){
                for(int i=0;i<translateQuestionDto.getCount();i++){
                    int index = 0;
                    if(unaddedTranslateEntityList.size() > 1){
                        index = random.nextInt(unaddedTranslateEntityList.size()-1)%(unaddedTranslateEntityList.size());
                    }
                    TranslateQuestionEntity translateQuestion = new TranslateQuestionEntity();
                    translateQuestion.setSubject(translateQuestionDto.getSubject());
                    translateQuestion.setType("2");
                    translateQuestion.setQuestion(unaddedTranslateEntityList.get(index).getJapanese());
                    translateQuestion.setAnswer(unaddedTranslateEntityList.get(index).getChinese());
                    translateQuestion.setAnalysis(unaddedTranslateEntityList.get(index).getJapanese() + "对应的中文是：" + unaddedTranslateEntityList.get(index).getChinese());
                    translateQuestion.setLevel(translateQuestionDto.getLevel());
                    translateQuestion.setScore(translateQuestionDto.getScore());
                    translateQuestion.setDeleteFlag("0");
                    translateQuestion.setCreateDate(new Date());
                    translateQuestionService.save(translateQuestion);
                    unaddedTranslateEntityList.remove(unaddedTranslateEntityList.get(index));
                }
            }
        }else{
            return R.error("该课程下没有新的试题了");
        }
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("business:translatequestion:update")
    public R update(@RequestBody TranslateQuestionEntity translateQuestion){
		translateQuestionService.updateById(translateQuestion);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("business:translatequestion:delete")
    public R delete(@RequestBody String[] uuids){
        boolean deleteFlag = true;
        for (String uuid : uuids) {
            PaperQuestionEntity paperQuestionEntity = paperQuestionService.getByQuestionId(uuid);
            if(null != paperQuestionEntity){
                deleteFlag =false;
                break;
            }
        }
        if(deleteFlag){
            translateQuestionService.removeByIds(Arrays.asList(uuids));
        }else {
            return R.error("该试题已被添加到试卷无法删除");
        }

        return R.ok();
    }

}

package io.train.modules.business.paperquestion.service.impl;

import io.train.modules.business.paperquestion.dao.PaperQuestionDao;
import io.train.modules.business.paperquestion.dto.PaperQuestionDto;
import io.train.modules.business.paperquestion.dto.QuestionDto;
import io.train.modules.business.paperquestion.entity.PaperQuestionEntity;
import io.train.modules.business.paperquestion.service.PaperQuestionService;
import io.train.modules.business.question.service.*;
import io.train.modules.business.question.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.train.common.utils.PageUtils;
import io.train.common.utils.Query;


@Service("paperQuestionService")
public class PaperQuestionServiceImpl extends ServiceImpl<PaperQuestionDao, PaperQuestionEntity> implements PaperQuestionService {

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
    private PaperQuestionService paperQuestionService;
    
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PaperQuestionEntity> page = this.page(
                new Query<PaperQuestionEntity>().getPage(params),
                new QueryWrapper<PaperQuestionEntity>()
        );

        return new PageUtils(page);
    }

	@Override
	public List<PaperQuestionEntity> queryAll(Map<String, Object> params) {
		return this.list(new QueryWrapper<PaperQuestionEntity>());
	}

    @Override
    public List<PaperQuestionEntity> getByPaperId(String paperId) {
        QueryWrapper<PaperQuestionEntity> queryWrapper = new QueryWrapper<PaperQuestionEntity>();
        queryWrapper.eq("paper_id",paperId);
        return this.baseMapper.selectList(queryWrapper);
    }

    @Override
    public PaperQuestionEntity getByQuestionId(String questionId) {
        QueryWrapper<PaperQuestionEntity> queryWrapper = new QueryWrapper<PaperQuestionEntity>();
        queryWrapper.eq("question_id",questionId);
        return this.baseMapper.selectOne(queryWrapper);
    }

    @Override
    public PaperQuestionEntity getByQuestionIdAndPaperId(String questionId, String paperId) {
        QueryWrapper<PaperQuestionEntity> queryWrapper = new QueryWrapper<PaperQuestionEntity>();
        queryWrapper.eq("question_id",questionId);
        queryWrapper.eq("paper_id",paperId);
        return this.baseMapper.selectOne(queryWrapper);
    }

    @Override
    public void deleteByQuestionIds(String[] questionIds) {
        QueryWrapper<PaperQuestionEntity> queryWrapper = new QueryWrapper<PaperQuestionEntity>();
        queryWrapper.in("question_id",questionIds);
        this.baseMapper.delete(queryWrapper);
    }

    @Override
    public void randomAddQuestions(PaperQuestionDto paperQuestionDto) {
        System.out.println(paperQuestionDto);
        String paperId = paperQuestionDto.getPaperId();
        for (QuestionDto questionDto : paperQuestionDto.getQuestionIds()) {
            if("单选题".equals(questionDto.getType())){
                List<MultiQuestionEntity> radioList = multiQuestionService.getAllQuestionsBySubjectAndType(questionDto.getSubject(),"radio");
                List<Integer> indexList = randomValues(questionDto.getNum(), radioList.size());
                for (Integer index : indexList) {
                    MultiQuestionEntity multiQuestionEntity = radioList.get(index);
                    savePaperQuestion(paperId,"单选题",multiQuestionEntity.getUuid(),multiQuestionEntity.getQuestion());
                }
            }
            if("多选题".equals(questionDto.getType())){
                List<MultiQuestionEntity> multiList = multiQuestionService.getAllQuestionsBySubjectAndType(questionDto.getSubject(),"multi");
                List<Integer> indexList = randomValues(questionDto.getNum(), multiList.size());
                for (Integer index : indexList) {
                    MultiQuestionEntity multiQuestionEntity = multiList.get(index);
                    savePaperQuestion(paperId,"多选题",multiQuestionEntity.getUuid(),multiQuestionEntity.getQuestion());
                }
            }
            if("填空题".equals(questionDto.getType())){
                List<FillQuestionEntity> fillList = fillQuestionService.getAllQuestionsBySubject(questionDto.getSubject());
                List<Integer> indexList = randomValues(questionDto.getNum(), fillList.size());
                for (Integer index : indexList) {
                    FillQuestionEntity fillQuestionEntity = fillList.get(index);
                    savePaperQuestion(paperId,"填空题",fillQuestionEntity.getUuid(),fillQuestionEntity.getQuestion());
                }
            }
            if("判断题".equals(questionDto.getType())){
                List<JudgeQuestionEntity> judgeList = judgeQuestionService.getAllQuestionsBySubject(questionDto.getSubject());
                List<Integer> indexList = randomValues(questionDto.getNum(), judgeList.size());
                for (Integer index : indexList) {
                    JudgeQuestionEntity judgeQuestionEntity = judgeList.get(index);
                    savePaperQuestion(paperId,"判断题",judgeQuestionEntity.getUuid(),judgeQuestionEntity.getQuestion());
                }
            }
            if("问答题".equals(questionDto.getType())){
                List<DiscussionQuestionEntity> discussionList = discussionQuestionService.getAllQuestionsBySubject(questionDto.getSubject());
                List<Integer> indexList = randomValues(questionDto.getNum(), discussionList.size());
                for (Integer index : indexList) {
                    DiscussionQuestionEntity discussionQuestionEntity = discussionList.get(index);
                    savePaperQuestion(paperId,"问答题",discussionQuestionEntity.getUuid(),discussionQuestionEntity.getQuestion());
                }
            }
            if("翻译题".equals(questionDto.getType())){
                List<TranslateQuestionEntity> translateList = translateQuestionService.getAllQuestionsBySubject(questionDto.getSubject());
                List<Integer> indexList = randomValues(questionDto.getNum(), translateList.size());
                for (Integer index : indexList) {
                    TranslateQuestionEntity translateQuestionEntity = translateList.get(index);
                    savePaperQuestion(paperId,"翻译题",translateQuestionEntity.getUuid(),translateQuestionEntity.getQuestion());
                }
            }
        }
    }

    /**
     * 随机取值
     * @param length 取值个数
     * @param total 数组总长度
     * @return
     */
    public List<Integer> randomValues(int length, int total){
        List<Integer> list = new ArrayList<Integer>();
        for (int i = 0; i < total; i++) {
            list.add(i);
        }
        Set<Integer>  randomSource = new HashSet<Integer>(list);
        //重复数据防越界
        if(randomSource.size()<=length){
            return new ArrayList<Integer>(randomSource);
        }
        List<Integer> randomResult=new ArrayList<>(randomSource);
        Set<Integer> randomList=new HashSet<Integer>();
        Random random =new Random();
        while (randomList.size()<length){
            randomList.add(randomResult.get(random.nextInt(randomResult.size())));
        }
        return new ArrayList<>(randomList);
    }
    
    private void savePaperQuestion(String paperId, String questionType, String questionId, String question){
        PaperQuestionEntity paperQuestionEntity = new PaperQuestionEntity();
        paperQuestionEntity.setPaperId(paperId);
        paperQuestionEntity.setQuestionType(questionType);
        paperQuestionEntity.setQuestionId(questionId);
        paperQuestionEntity.setQuestion(question);
        paperQuestionService.save(paperQuestionEntity);
    }
}
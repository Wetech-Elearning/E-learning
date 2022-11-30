package io.train.modules.business.comment.dao;

import io.train.modules.business.comment.entity.CommentEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2021-07-05 10:49:41
 */
@Mapper
public interface CommentDao extends BaseMapper<CommentEntity> {
	
}

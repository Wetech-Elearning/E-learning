package io.train.modules.sys.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.train.modules.sys.entity.SysDictEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 数据字典信息表
 * 
 * @author Tony
 * @email
 * @date 2019-07-23 15:55:11
 */
@Mapper
public interface SysDictDao extends BaseMapper<SysDictEntity> {

}

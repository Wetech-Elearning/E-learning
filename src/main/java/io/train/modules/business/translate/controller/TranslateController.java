package io.train.modules.business.translate.controller;

import java.util.Arrays;
import java.util.Map;

import io.train.common.utils.PageUtils;
import io.train.common.utils.R;
import io.train.modules.business.translate.dto.TranslateDto;
import io.train.modules.business.translate.entity.TranslateEntity;
import io.train.modules.business.translate.service.TranslateService;
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
 * @date 2021-08-30 23:55:05
 */
@RestController
@RequestMapping("generator/translate")
public class TranslateController {
    @Autowired
    private TranslateService translateService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("generator:translate:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = translateService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{uuid}")
    //@RequiresPermissions("generator:translate:info")
    public R info(@PathVariable("uuid") Long uuid){
		TranslateEntity translate = translateService.getById(uuid);

        return R.ok().put("translate", translate);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("generator:translate:save")
    public R save(@RequestBody TranslateDto translateDto){
        if(null != translateDto.getTranslateEntityList() && translateDto.getTranslateEntityList().size() > 0){
            for (TranslateEntity translateEntity : translateDto.getTranslateEntityList()) {
                if(null == translateEntity.getUuid()){
                    translateService.save(translateEntity);
                }
            }
        }else{
            translateService.removeByRelatedCourseId(translateDto.getRelatedCourseId());
        }
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
   // @RequiresPermissions("generator:translate:update")
    public R update(@RequestBody TranslateEntity translate){
		translateService.updateById(translate);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("generator:translate:delete")
    public R delete(@RequestBody String[] uuids){
		translateService.removeByIds(Arrays.asList(uuids));

        return R.ok();
    }

}

package com.yi.weal.pipeline;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.yi.weal.config.Config;
import com.yi.weal.model.Weal;
import com.yi.weal.service.WealService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.Map;

/**
 * 定制Pipeline
 * @author YI
 * @date 2018-11-30 14:15:39
 */
@Component
public class ElasticSearchVideoPipeline implements Pipeline {
    private static final Logger log = LoggerFactory.getLogger(ElasticSearchVideoPipeline.class);

    @Autowired
    @Qualifier("weal")
    private WealService wealService;

    @Override
    public void process(ResultItems resultItems, Task task) {
        System.out.println("get page: " + resultItems.getRequest().getUrl());

        Map<String, Object> all = resultItems.getAll();

        Weal weal = BeanUtil.mapToBean(all, Weal.class, true);

        if (StrUtil.isNotBlank(weal.getTitle()) && StrUtil.isNotBlank(weal.getVideo()) && StrUtil.isNotBlank(weal.getCover())){
            weal.setId(Config.nextInc.incrementAndGet());
            log.info("爬虫数据 {}", weal);

            wealService.wealSave(weal);
        }
    }
}

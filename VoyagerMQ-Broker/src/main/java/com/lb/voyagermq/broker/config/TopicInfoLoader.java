package com.lb.voyagermq.broker.config;

import com.lb.voyagermq.broker.cache.CommonCache;
import io.netty.util.internal.StringUtil;

public class TopicInfoLoader {
    private TopicInfo topicInfo;

    public void loadProperties() {
        GlobalProperties globalProperties = CommonCache.getGlobalProperties();
        String basePath = globalProperties.getVoyagerMqHome();
        if (StringUtil.isNullOrEmpty(basePath)) {
            throw new IllegalArgumentException("VOYAGER_MQ_HOME is invalid");
        }

        String topicJsonFilePath = basePath + "broker/config/voyagermq-topic.json";
        topicInfo = new TopicInfo();
    }
}

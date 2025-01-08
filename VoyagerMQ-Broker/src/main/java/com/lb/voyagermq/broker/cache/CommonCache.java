package com.lb.voyagermq.broker.cache;

import com.lb.voyagermq.broker.config.GlobalProperties;
import com.lb.voyagermq.broker.config.TopicInfo;

/**
 * 统一缓存对象
 */
public class CommonCache {
    public static GlobalProperties globalProperties = new GlobalProperties();
    public static TopicInfo topicInfo = new TopicInfo();

    public static GlobalProperties getGlobalProperties() {
        return globalProperties;
    }

    public static void setGlobalProperties(GlobalProperties globalProperties) {
        CommonCache.globalProperties = globalProperties;
    }

    public static TopicInfo getTopicInfo() {
        return topicInfo;
    }

    public static void setTopicInfo(TopicInfo topicInfo) {
        CommonCache.topicInfo = topicInfo;
    }
}

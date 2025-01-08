package com.lb.voyagermq.broker.config;

import com.lb.voyagermq.broker.cache.CommonCache;
import com.lb.voyagermq.broker.constants.BrokerConstants;
import io.netty.util.internal.StringUtil;

public class GlobalPropertiesLoader {

    GlobalProperties globalProperties = new GlobalProperties();

    public void loadProperties() {
        String vayagerMqHome = System.getenv(BrokerConstants.VOYAGER_MQ_HOME);
        if (StringUtil.isNullOrEmpty(vayagerMqHome)) {
            throw new IllegalArgumentException("VOYAGER_MQ_HOME is null");
        }
        globalProperties.setVoyagerMqHome(vayagerMqHome);
        CommonCache.setGlobalProperties(globalProperties);
    }
}

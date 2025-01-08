package com.lb.voyagermq.broker.core;

import java.util.HashMap;
import java.util.Map;

/**
 * 管理多个 MMapFileModel 对象的类。它通过一个 Map（键是 topic 名称，值是 MMapFileModel 实例）来存储和管理文件映射。
 */
public class MMapFileModelManager {
    /**
     * key:主题名称，value:文件的mMap对象
     */
    private Map<String,MMapFileModel> mMapFileModelMap = new HashMap<>();

    public void put(String topic,MMapFileModel mapFileModel) {
        mMapFileModelMap.put(topic,mapFileModel);
    }

    public MMapFileModel get(String topic) {
        return mMapFileModelMap.get(topic);
    }
}

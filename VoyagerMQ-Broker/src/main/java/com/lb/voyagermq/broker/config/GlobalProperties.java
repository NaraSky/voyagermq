package com.lb.voyagermq.broker.config;

public class GlobalProperties {
    /**
     * 读取环境变量中配置的mq存储绝对路径地址
     */
    private String voyagerMqHome;

    public String getVoyagerMqHome() {
        return voyagerMqHome;
    }

    public void setVoyagerMqHome(String voyagerMqHome) {
        this.voyagerMqHome = voyagerMqHome;
    }
}

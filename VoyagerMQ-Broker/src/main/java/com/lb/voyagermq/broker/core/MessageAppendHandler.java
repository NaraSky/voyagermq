package com.lb.voyagermq.broker.core;

import java.io.IOException;

/**
 * MessageAppendHandler 类是消息处理类，负责将消息附加到特定的文件映射中，并提供读取消息的功能。
 * 该类通过内存映射文件（MMap）实现高效的消息存储与读取。
 */
public class MessageAppendHandler {

    // 用于管理不同主题（topic）对应的文件映射模型
    private MMapFileModelManager mMapFileModelManager = new MMapFileModelManager();

    // 文件路径和主题名称
    private static String filePath = "/home/lb/Downloads/VoyagerMQ/broker/store/order_cancel_topic/00000000";
    private static String topicName = "order_cancel_topic";

    /**
     * 构造函数，在创建 MessageAppendHandler 实例时，初始化文件映射。
     * 调用 prepareMMapLoading 方法来加载文件映射，并将其与主题关联。
     *
     * @throws IOException 如果在文件映射加载过程中发生 IO 异常
     */
    public MessageAppendHandler() throws IOException {
        this.prepareMMapLoading(); // 初始化文件映射
    }

    /**
     * 初始化文件映射的方法。
     * 该方法创建一个 MMapFileModel 实例，并将指定文件的部分（从偏移量 0 开始，映射 1MB 内存）映射到内存。
     * 然后将文件映射模型与主题名称（topicName）关联，并存储到 MMapFileModelManager 中。
     *
     * @throws IOException 如果文件映射加载过程中发生错误
     */
    private void prepareMMapLoading() throws IOException {
        // 创建 MMapFileModel 实例
        MMapFileModel mapFileModel = new MMapFileModel();
        // 加载文件并映射到内存，从文件的第一个字节开始，映射 1MB 的内存区域
        mapFileModel.loadFileInMMap(filePath, 0, 1 * 1024 * 1024);
        // 将映射模型与主题名称关联，并存储到管理器中
        mMapFileModelManager.put(topicName, mapFileModel);
    }

    /**
     * 向指定的主题（topic）追加消息内容。
     * 该方法从管理器中获取对应主题的文件映射模型，并将消息内容写入该文件映射。
     * 如果主题无效，则抛出异常。
     *
     * @param topic   主题名称，表示文件映射的标识
     * @param content 要追加的消息内容
     */
    public void appendMsg(String topic, String content) {
        // 从 MMapFileModelManager 中获取指定主题的映射模型
        MMapFileModel mapFileModel = mMapFileModelManager.get(topic);
        if (mapFileModel == null) {
            // 如果找不到映射模型，抛出异常
            throw new RuntimeException("topic is invalid!");
        }
        // 将消息内容转换为字节数组，并写入文件映射中
        mapFileModel.writeContent(content.getBytes());
    }

    /**
     * 读取指定主题的消息内容。
     * 该方法从文件映射中读取消息内容（最多读取 10 个字节），并打印到控制台。
     * 如果主题无效，则抛出异常。
     *
     * @param topic 主题名称，表示文件映射的标识
     */
    public void readMsg(String topic) {
        // 从 MMapFileModelManager 中获取指定主题的映射模型
        MMapFileModel mapFileModel = mMapFileModelManager.get(topic);
        if (mapFileModel == null) {
            // 如果找不到映射模型，抛出异常
            throw new RuntimeException("topic is invalid!");
        }
        // 从文件映射中读取前 10 个字节
        byte[] content = mapFileModel.readContent(0, 10);
        // 将字节数据转换为字符串并打印
        System.out.println(new String(content));
    }

    /**
     * 程序入口，用于测试消息的追加与读取操作。
     * 创建 MessageAppendHandler 实例，向文件映射追加消息内容，然后读取并打印出来。
     *
     * @param args 命令行参数
     * @throws IOException 如果文件映射加载或读写过程中发生 IO 异常
     */
    public static void main(String[] args) throws IOException {
        // 创建 MessageAppendHandler 实例，初始化文件映射
        MessageAppendHandler messageAppendHandler = new MessageAppendHandler();

        // 向指定主题追加一条消息内容
        messageAppendHandler.appendMsg(topicName, "here is content");

        // 读取并打印指定主题的消息内容
        messageAppendHandler.readMsg(topicName);
    }
}

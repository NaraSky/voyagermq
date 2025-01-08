package com.lb.voyagermq.broker.utils;

import com.alibaba.fastjson2.JSON;
import com.lb.voyagermq.broker.model.VoyagerMqTopicModel;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class FileContentReaderUtils {

    public static String readFromFile(String path) {
        try (BufferedReader in = new BufferedReader(new FileReader(path))) {
            StringBuffer stringBuffer = new StringBuffer();
            while (in.ready()) {
                stringBuffer.append(in.readLine());
            }
            return stringBuffer.toString();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        String content = FileContentReaderUtils.readFromFile("broker/config/voyagermq-topic.json");
        System.out.println("content = " + content);
        List<VoyagerMqTopicModel> voyagerMqTopicModels = JSON.parseArray(content, VoyagerMqTopicModel.class);
        System.out.println("voyagerMqTopicModels = " + voyagerMqTopicModels);
    }
}

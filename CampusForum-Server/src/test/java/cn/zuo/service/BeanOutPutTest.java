package cn.zuo.service;

import cn.zuo.record.ChatInfoRecordTest;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.ai.converter.StructuredOutputConverter;

public class BeanOutPutTest {

    public static void main(String[] args) {
        StructuredOutputConverter converter = new BeanOutputConverter(ChatInfoRecordTest.class);
        String format = converter.getFormat();
        System.out.println(format);
    }
}

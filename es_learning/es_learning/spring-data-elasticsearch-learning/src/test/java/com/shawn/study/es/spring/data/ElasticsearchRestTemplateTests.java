package com.shawn.study.es.spring.data;

import com.shawn.study.es.spring.data.service.ElasticsearchTemplateService;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ElasticsearchRestTemplateTests {

    @Autowired private ElasticsearchTemplateService demo;

    public void test_create_index(){
        Map<String, Object> settings = new HashMap<>();
        settings.put("index.number_of_shards", 1);
        settings.put("index.number_of_replicas", 0);

        Document mapping = Document.create();
//        mapping.
//        demo.createIndex(settings);
    }

}

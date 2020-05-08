package com.csye7255.project.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.http.HttpHost;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Service
public class IndexingService {
    
    private static JedisPool jedisPool;
    private static RestHighLevelClient restClient;
    
    public JedisPool getJedisPool() {
    	jedisPool = new JedisPool("localhost", 6379); 
    	return jedisPool;
    } 

    public RestHighLevelClient restHighLevelClient() {
    	restClient =  new RestHighLevelClient(RestClient.builder(new HttpHost("localhost", 9200)).setRequestConfigCallback(requestConfigBuilder ->
        requestConfigBuilder
                .setConnectTimeout(5000)
                .setSocketTimeout(5000))
        .setMaxRetryTimeoutMillis(60000));
    	return restClient;

    } 
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    static String IndexQueue = "RedisIndexQueue";
    static String BackupQueue = "BackUpQueue";

    @EventListener(ApplicationReadyEvent.class)
    public void indexing() {
        while(true) {
            Jedis jedis = jedisPool.getResource();

            byte[] bytes = jedis.rpoplpush(IndexQueue.getBytes(), BackupQueue.getBytes());
            if (bytes != null && bytes.length !=0) {
                try {
                    JSONObject jo = new JSONObject(new String(bytes));
                    String objectType = jo.getString("objectType");
                    String objectId = jo.getString("objectId");
                    restClient.index(new IndexRequest("plan_index", objectType, objectId).source(bytes, XContentType.JSON));
                    logger.info("Index: plan_index, "+"Type="+objectType+" , Id="+objectId+" added to ElasticSearch.");
                } catch (IOException e) {
                    logger.error("ElasticSearch add index error:"+e);
                }
                String message = new String(bytes, StandardCharsets.UTF_8);
                logger.info("Message get: "+message);
            }
            jedis.close();
        }
    }
}

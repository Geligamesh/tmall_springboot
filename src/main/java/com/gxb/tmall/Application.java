package com.gxb.tmall;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableCaching
@EnableElasticsearchRepositories(basePackages = "com.gxb.tmall.es")
@EnableJpaRepositories(basePackages = {"com.gxb.tmall.dao","com.gxb.tmall.pojo"})
public class Application {

    // static {
    //     PortUtils.checkPort(6379, "Redis服务器", true);
    // }
    // static {
    //     // PortUtils.checkPort(9300, "ElasticSearch服务端", true);
    //     // PortUtils.checkPort(5601, "Kibana工具",true);
    // }
    public static void main(String[] args) {
        SpringApplication.run(Application.class);
    }
}

package com.how2j.tmall;

import com.how2j.tmall.util.PortUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
// @EnableCaching
@EnableElasticsearchRepositories(basePackages = "com.how2j.tmall.es")
@EnableJpaRepositories(basePackages = {"com.how2j.tmall.dao","com.how2j.tmall.pojo"})
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

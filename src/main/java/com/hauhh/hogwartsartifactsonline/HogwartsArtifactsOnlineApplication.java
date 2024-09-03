package com.hauhh.hogwartsartifactsonline;

import com.hauhh.hogwartsartifactsonline.system.utils.IDUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class HogwartsArtifactsOnlineApplication {

    public static void main(String[] args) {
        SpringApplication.run(HogwartsArtifactsOnlineApplication.class, args);
    }

    @Bean
    public IDUtil IDUtil(){
        return new IDUtil(1,1);
    }

}

package com.convict.configuration;

import com.convict.model.WebPath;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.util.ResourceUtils;

import java.io.File;

/**
 * @Author Convict
 * @Date 2019/3/13 19:55
 */
@SpringBootConfiguration
public class WebPathConfiguration {

    @Bean
    public WebPath webPath() {

        WebPath webPath = null;
        try {
            File filePath = new File(ResourceUtils.getURL("classpath:").getPath());
            String AbsolutePath = filePath.getAbsolutePath().replace("%20", " ").replace("\\", "\\\\");
            webPath = new WebPath(AbsolutePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return webPath;
    }
}

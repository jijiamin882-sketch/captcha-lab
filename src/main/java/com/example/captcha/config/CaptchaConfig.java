package com.example.captcha.config;

import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
public class CaptchaConfig {

    @Bean
    public Producer captchaProducer() {
        Properties props = new Properties();
        props.put("kaptcha.textproducer.char.length", "5");
        props.put("kaptcha.textproducer.char.string", "ABCDEFGHJKLMNPQRSTUVWXYZ23456789");
        props.put("kaptcha.image.width", "200");
        props.put("kaptcha.image.height", "60");
        props.put("kaptcha.noise.impl", "com.google.code.kaptcha.impl.NoNoise");

        Config config = new Config(props);
        return config.getProducerImpl();
    }
}


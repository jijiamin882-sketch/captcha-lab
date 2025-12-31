package com.example.captcha.controller;

import com.google.code.kaptcha.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Files;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/captcha")
public class CaptchaController {

    @Autowired
    private Producer producer;

    private final Map<String, String> cache = new ConcurrentHashMap<>();

    @GetMapping("/new")
    public Map<String, String> newCaptcha() throws Exception {
        String text = producer.createText();
        String id = UUID.randomUUID().toString();

        BufferedImage image = producer.createImage(text);
        File file = new File("/tmp/" + id + ".png");
        ImageIO.write(image, "png", file);

        cache.put(id, text);

        return Map.of(
                "captchaId", id,
                "imageUrl", "/captcha/image/" + id
        );
    }

    @GetMapping("/image/{id}")
    public ResponseEntity<byte[]> getImage(@PathVariable String id) throws Exception {
        File file = new File("/tmp/" + id + ".png");
        byte[] image = Files.readAllBytes(file.toPath());

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(image);
    }

    @PostMapping("/verify")
    public String verify(@RequestBody Map<String, String> body) {
        String id = body.get("captchaId");
        String code = body.get("captchaCode");

        if (cache.containsKey(id) && cache.get(id).equalsIgnoreCase(code)) {
            cache.remove(id); // invalidate
            return "SUCCESS";
        }
        return "FAIL";
    }
}

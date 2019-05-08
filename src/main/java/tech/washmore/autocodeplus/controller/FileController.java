package tech.washmore.autocodeplus.controller;

import com.google.common.collect.ImmutableMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@RestController
public class FileController {

    @PostMapping("/file/parse")
    public Map<String, String> process(@RequestParam("file") MultipartFile file, @RequestParam String type) {
        try {
            return ImmutableMap.of("type", type, "name", file.getOriginalFilename(), "content", new String(file.getBytes(), StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

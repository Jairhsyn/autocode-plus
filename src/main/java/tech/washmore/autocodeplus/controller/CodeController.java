package tech.washmore.autocodeplus.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import tech.washmore.autocodeplus.model.SysConfig;
import tech.washmore.autocodeplus.model.param.AllConfig;
import tech.washmore.autocodeplus.service.CodeService;

@RestController
public class CodeController {
    @Autowired
    private CodeService codeService;

    @PostMapping("/code/process")
    public boolean process(@RequestBody AllConfig config) {
        return codeService.process(config);
    }
}

package tech.washmore.autocodeplus.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import tech.washmore.autocodeplus.common.db.DbConfig;
import tech.washmore.autocodeplus.model.param.Db;

@RestController
public class DbConfigController {
    @Autowired
    private DbConfig dbConfig;

    @PostMapping("/db/init")
    public Db initDb(@RequestBody Db db) {
        return dbConfig.initConfig(db);
    }
}

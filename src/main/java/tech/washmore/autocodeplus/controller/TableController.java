package tech.washmore.autocodeplus.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.washmore.autocodeplus.common.db.DbUtil;

import java.sql.SQLException;
import java.util.List;

@RestController
public class TableController {
    @Autowired
    private DbUtil dbUtil;

    @GetMapping("/table/all")
    public List<JSONObject> allTables() throws SQLException {
        return dbUtil.queryList("show table status;", JSONObject.class);
    }


}

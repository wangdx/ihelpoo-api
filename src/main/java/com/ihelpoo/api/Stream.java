package com.ihelpoo.api;

import com.ihelpoo.api.dao.JdbcRecordSayDao;
import com.ihelpoo.api.model.RecordSay;
import com.ihelpoo.api.model.RecordSayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@RequestMapping("/api")
@Controller
public class Stream {


    @Autowired
    JdbcRecordSayDao recordSayDao;

    @RequestMapping(value = {"/get"}, method = RequestMethod.GET)
    public
    @ResponseBody
    RecordSayList savePayment() {

        RecordSayList recordSays = recordSayDao.getRecordSays(1);

        return recordSays;

    }

    @RequestMapping(value = {"/getOne"})
    public
    @ResponseBody
    RecordSay savePayment1() {

        RecordSay recordSays = recordSayDao.getRecordSay(1);

        return recordSays;

    }

    @RequestMapping(value = {"/put"}, method = {RequestMethod.PUT})
    public
    @ResponseBody
    String b() {
        return "put";

    }

    @RequestMapping(value = {"/delete"}, method = {RequestMethod.DELETE})
    public
    @ResponseBody
    String c() {
        return "delete";

    }

    @RequestMapping(value = {"/post"}, method = {RequestMethod.POST})
    public
    @ResponseBody
    String d() {
        return "post";

    }

}
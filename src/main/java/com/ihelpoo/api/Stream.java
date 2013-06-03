package com.ihelpoo.api;

import com.ihelpoo.api.dao.JdbcRecordSayDao;
import com.ihelpoo.api.model.RecordSay;
import com.ihelpoo.api.model.RecordSayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * stream.
 *
 * @author dongxu.wang@acm.org
 * @version 1
 */

@RequestMapping("/api")
@Controller
public class Stream {


    /**
     * record say dao.
     */
    @Autowired
    private JdbcRecordSayDao recordSayDao;

    /**
     * get.
     *
     * @return list
     */
    @RequestMapping(value = {"/get"}, method = RequestMethod.GET)
    @ResponseBody
    public RecordSayList getTenElements() {

        final RecordSayList recordSays = recordSayDao.getRecordSays(1);

        System.out.println("get lots of things");
        return recordSays;

    }

    /**
     * get.
     *
     * @return one
     */
    @RequestMapping(value = {"/getOne"})
    @ResponseBody
    public RecordSay getOnlyOneElement() {

        final RecordSay recordSays = recordSayDao.getRecordSay(1);
        System.out.println("---get only one thing--");
        return recordSays;

    }

    /**
     * put.
     *
     * @return body
     */
    @RequestMapping(value = {"/put"}, method = {RequestMethod.PUT})
    @ResponseBody
    public String b() {
        return "put";

    }

    /**
     * delete.
     *
     * @return body
     */
    @RequestMapping(value = {"/delete"}, method = {RequestMethod.DELETE})
    @ResponseBody
    public String c() {
        return "delete";

    }

    /**
     * method.
     *
     * @return body
     */
    @RequestMapping(value = {"/post"}, method = {RequestMethod.POST})
    @ResponseBody
    public String d() {
        return "post";

    }

}
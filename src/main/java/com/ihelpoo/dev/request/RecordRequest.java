package com.ihelpoo.dev.request;

import com.ihelpoo.dev.response.RecordResponse;
import com.ihelpoo.dev.response.StatusMessageResult;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author: echowdx@gmail.com
 */
@Controller
public final class RecordRequest {

    @RequestMapping(value = "/records", method = RequestMethod.DELETE, produces = "application/xml")
    @ResponseBody
    public RecordResponse records(){
        StatusMessageResult result = new StatusMessageResult();
        result.status = "200";
        result.message = "success";
        RecordResponse resp = new RecordResponse();
        resp.result = result;
        return resp;
    }
}

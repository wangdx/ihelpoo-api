package com.ihelpoo.api;

import com.ihelpoo.api.model.SchoolResult;
import com.ihelpoo.api.service.SchoolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author: echowdx@gmail.com
 */
@Controller
public class School {


    @Autowired
    SchoolService schoolService;

    @RequestMapping(value = "/schools.xml", method = RequestMethod.GET, produces = "application/xml")
    @ResponseBody
    public SchoolResult schoolsXML() {
        return schoolService.fetchAllSchools();
    }

    @RequestMapping(value = "/schools.json", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public SchoolResult schoolsJSON() {
        return schoolsXML();
    }

    @RequestMapping(value = "/schools", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public SchoolResult schools() {
        return schoolsJSON();
    }
}

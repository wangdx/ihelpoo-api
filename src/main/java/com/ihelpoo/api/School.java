package com.ihelpoo.api;

import com.ihelpoo.api.model.AcademyResult;
import com.ihelpoo.api.model.DormResult;
import com.ihelpoo.api.model.MajorResult;
import com.ihelpoo.api.model.SchoolResult;
import com.ihelpoo.api.service.SchoolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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

    @RequestMapping(value = "/academies.xml", method = RequestMethod.GET, produces = "application/xml")
    @ResponseBody
    public AcademyResult academiesXML(@RequestParam(value = "school_id", required = false) Integer schoolId) {
        return schoolService.fetchAllAcademys(schoolId);
    }

    @RequestMapping(value = "/academies.json", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public AcademyResult academiesJSON(@RequestParam(value = "school_id", required = false) Integer schoolId) {
        return academiesXML(schoolId);
    }

    @RequestMapping(value = "/academies", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public AcademyResult academies(@RequestParam(value = "school_id", required = false) Integer schoolId) {
        return academiesXML(schoolId);
    }

    @RequestMapping(value = "/majors.xml", method = RequestMethod.GET, produces = "application/xml")
    @ResponseBody
    public MajorResult majorsXML(
            @RequestParam(value = "school_id", required = false) Integer schoolId,
            @RequestParam(value = "academy_id", required = false) Integer academyId
    ) {
        return schoolService.fetchAllMajors(schoolId, academyId);
    }

    @RequestMapping(value = "/majors.json", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public MajorResult majorsJSON(
            @RequestParam(value = "school_id", required = false) Integer schoolId,
            @RequestParam(value = "academy_id", required = false) Integer academyId
    ) {
        return majorsXML(schoolId, academyId);
    }

    @RequestMapping(value = "/majors", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public MajorResult majors(
            @RequestParam(value = "school_id", required = false) Integer schoolId,
            @RequestParam(value = "academy_id", required = false) Integer academyId
    ) {
        return majorsXML(schoolId, academyId);
    }

    @RequestMapping(value = "/dorms.xml", method = RequestMethod.GET, produces = "application/xml")
    @ResponseBody
    public DormResult dormsXML(@RequestParam(value = "school_id", required = false) Integer schoolId) {
        return schoolService.fetchAllDorms(schoolId);
    }

    @RequestMapping(value = "/dorms.json", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public DormResult dormsJSON(@RequestParam(value = "school_id", required = false) Integer schoolId) {
        return dormsXML(schoolId);
    }

    @RequestMapping(value = "/dorms", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public DormResult dorms(@RequestParam(value = "school_id", required = false) Integer schoolId) {
        return dormsXML(schoolId);
    }
}

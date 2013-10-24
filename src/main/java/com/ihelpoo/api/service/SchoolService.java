package com.ihelpoo.api.service;

import com.ihelpoo.api.dao.UserDao;
import com.ihelpoo.api.model.SchoolResult;
import com.ihelpoo.api.model.obj.Result;
import com.ihelpoo.api.model.entity.ISchoolInfoEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: echowdx@gmail.com
 */
@Service
public class SchoolService {
    @Autowired
    UserDao userDao;
    public SchoolResult fetchAllSchools() {
        List<ISchoolInfoEntity> schoolInfoEntities = userDao.fetchAllSchools();
        SchoolResult schoolResult = new SchoolResult();
        Result result = new Result();
        result.setErrorCode("1");
        result.setErrorMessage("获取成功");
        List<SchoolResult.School> schools = new ArrayList<SchoolResult.School>();
        for(ISchoolInfoEntity schoolInfoEntity:schoolInfoEntities){
            SchoolResult.School school = new SchoolResult.School();
            school.city_op = schoolInfoEntity.getCityOp();
            school.domain = schoolInfoEntity.getDomain();
            school.domain_main = schoolInfoEntity.getDomainMain();
            school.id = schoolInfoEntity.getId();
            school.initial = schoolInfoEntity.getInitial();
            school.schoolName = schoolInfoEntity.getSchool();
            school.status = schoolInfoEntity.getStatus();
            school.time = schoolInfoEntity.getTime();
            schools.add(school);
        }
        schoolResult.result   = result;
        schoolResult.schools = new SchoolResult.Schools(schools);
        return schoolResult;
    }
}

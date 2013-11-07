package com.ihelpoo.api.service;

import com.ihelpoo.api.dao.UserDao;
import com.ihelpoo.api.model.AcademyResult;
import com.ihelpoo.api.model.DormResult;
import com.ihelpoo.api.model.MajorResult;
import com.ihelpoo.api.model.SchoolResult;
import com.ihelpoo.api.model.entity.IOpAcademyEntity;
import com.ihelpoo.api.model.entity.IOpDormitoryEntity;
import com.ihelpoo.api.model.entity.IOpSpecialtyEntity;
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
        for (ISchoolInfoEntity schoolInfoEntity : schoolInfoEntities) {
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
        schoolResult.result = result;
        schoolResult.schools = new SchoolResult.Schools(schools);
        return schoolResult;
    }


    public AcademyResult fetchAllAcademys(Integer schoolId) {
        List<IOpAcademyEntity> academyEntities = userDao.fetchAllAcademys(schoolId);
        AcademyResult academyResult = new AcademyResult();
        Result result = new Result();
        result.setErrorCode("1");
        result.setErrorMessage("获取成功");
        List<AcademyResult.Academy> academies = new ArrayList<AcademyResult.Academy>();
        for (IOpAcademyEntity entity : academyEntities) {
            AcademyResult.Academy academy = new AcademyResult.Academy();
            academy.id = entity.getId();
            academy.academyName = entity.getName();
            academy.schoolId = entity.getSchool();
            academies.add(academy);
        }
        academyResult.result = result;
        academyResult.academys = new AcademyResult.Academys(academies);
        return academyResult;
    }


    public MajorResult fetchAllMajors(Integer schoolId, Integer academyId) {
        List<IOpSpecialtyEntity> specialtyEntities = userDao.fetchAllSpecialties(schoolId, academyId);
        MajorResult majorResult = new MajorResult();
        Result result = new Result();
        result.setErrorCode("1");
        result.setErrorMessage("获取成功");
        List<MajorResult.Major> majors = new ArrayList<MajorResult.Major>();
        for (IOpSpecialtyEntity entity : specialtyEntities) {
            MajorResult.Major major = new MajorResult.Major();
            major.id = entity.getId();
            major.academyId = entity.getAcademy();
            major.majorName = entity.getName();
            major.schoolId = entity.getSchool();
            majors.add(major);
        }
        majorResult.result = result;
        majorResult.majors = new MajorResult.Majors(majors);
        return majorResult;
    }

    public DormResult fetchAllDorms(Integer schoolId) {
        List<IOpDormitoryEntity> dormEntities = userDao.fetchAllDorms(schoolId);
        DormResult dormResult = new DormResult();
        Result result = new Result();
        result.setErrorCode("1");
        result.setErrorMessage("获取成功");
        List<DormResult.Dorm> dorms = new ArrayList<DormResult.Dorm>();
        for (IOpDormitoryEntity entity : dormEntities) {
            DormResult.Dorm dorm = new DormResult.Dorm();
            dorm.id = entity.getId();
            dorm.dormName = entity.getName();
            dorm.dormType = entity.getType();
            dorm.schoolId = entity.getSchool();
            dorms.add(dorm);
        }
        dormResult.result = result;
        dormResult.dorms = new DormResult.Dorms(dorms);
        return dormResult;
    }

}

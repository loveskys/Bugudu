package com.lyh.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lyh.entity.activity.ActivityVo;
import com.lyh.entity.user.*;

import java.util.List;

/**
 * @author lyh
 * @since 2024-07-05
 */
public interface IStudentUserService extends IService<StudentUser> {

    StudentUser getByUserName(String userName, String pwd);

    void saveUserLog(String userId, String userName, String sysTag, String remark);

    StudentUser wxLogin(WxUserLoginDto dto);

    StudentUser register(StudentRegistDto dto);

    StudentInfoVo getInfoDetail(StudentDetailDto dto);

    StudentUser saveOrUpdate(StudentSaveDto dto);

    boolean updateCategory(String id,String category);

    void updateUserAuthenById(String authId, String authCont, String authStatus, String userId);

    //根据用户昵称模糊查询列表
    List<StudentUserVo> getByNameList(String name);
}

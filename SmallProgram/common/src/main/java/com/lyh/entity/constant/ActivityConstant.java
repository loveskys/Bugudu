package com.lyh.entity.constant;

//活动通用字段
public class ActivityConstant {

    //活动发布状态：0申请中，1通过，2人工不通过
    public static final String activity_originate_status_ing = "0";
    public static final String activity_originate_status_pass = "1";
    public static final String activity_originate_status_nopass = "2";


    //学生认证审核状态：0未认证，1审核中，2已认证，3认证不通过
    public static final String student_authen_no = "0";
    public static final String student_authen_ing = "1";
    public static final String student_authen_pass = "2";
    public static final String student_authen_nopass = "3";



    //微信图片安全检测状态: 0未检测,1检测中,2通过,3不通过
    public static final String wx_pic_check_no = "0";     //未检测
    public static final String wx_pic_check_ing = "1";    //检测中
    public static final String wx_pic_check_pass = "2";   //通过
    public static final String wx_pic_check_nopass = "3"; //不通过







    //活动报名状态：0申请中，1成功，2不通过，3取消报名
    public static final String activity_apply_status_applying = "0";
    public static final String activity_apply_status_pass = "1";
    public static final String activity_apply_status_nopass = "2";
    public static final String activity_apply_status_cancel = "3";


    //是否发布人，0否，1是
    public static final String unIssue = "0";
    public static final String IsIssue = "1";


    //活动是否被下架，0否，1是
    public static final String unDelete = "0";
    public static final String IsDelete = "1";

    //是否首页推荐，0否，1是
    public static final String unRecommend = "0";
    public static final String IsRecommend = "1";



    //性别，0未知，1男2女
    public static final String unknownSex = "0";
    public static final String man = "1";
    public static final String girl = "2";


    //微信安全校验图片结果 检测结果：0检测中，1不可以，2可以
    public static final String pci_checking = "0";
    public static final String pci_checkno = "1";
    public static final String pci_checkok = "2";

    //活动图片类型：0图片，1公众号
//    public static final String image_type_url = "1";
}

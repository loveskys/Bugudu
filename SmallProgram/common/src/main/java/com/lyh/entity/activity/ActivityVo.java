package com.lyh.entity.activity;

import lombok.Data;
import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
public class ActivityVo implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String id;
    private String theme;
    private String intro;
    private String category;
    private String image;
    private String wxUrl;
//    private String imageType;

    //活动开始时间
    private String activityStart;
    private String activityEnd;

    //活动是否删除：0未删，1已删除
    private String isDelete;
    //活动是否过期：0未过期，1已过期
    private String isPast = "0";

    //发布人id
    private String issue;
    private String contactType;
    private String contact;
    private String contactImage;
    //活动申请状态：0申请中，1通过，2不通过
    private String auditStatus;
    private String auditTime;
    private String auditCont;
    private String createTime;
    private String createBy;
    private String updateTime;
    private String updateBy;

    //有值代表收藏，没有代表没收藏
    private String isCollect;

    //是否首页推荐：0否1是
    private String recommend;

    //是否上轮播图：0否1是
    private String swiper;

    //微信图片安全检测状态: 0未检测,1检测中,2通过,3不通过
    private String wxImgCheck;

    private List<ActivityApplyPerson> applyPeople;
    private ActivityLocation location;
    private ActivityCondit condit;
    private String[] conditArray;
    private List<DetailApplyPersonVO> enrolledDetails;
    private String enrolledRatio;
    private String atFull;  //0未满，1满员

    private String itSme;     //0不是我发布的，1是我发布的
    private String isApply;   //0不可报名，1可以报名
    private String isCancelApply; //0不可取消报名，1可以取消报名


    //活动地址名 深圳市民中心百果园
    private String locationName;
    //具体地址 广东省深圳市福田区福中三路
    private String locationAddress;
    //手写补充地址
    private String address;
    //经度  114.05956
    private String longitude;
    //纬度  22.54286
    private String latitude;
    private String peopleNum;
    private String conditSex;
    private String conditGrade;
    private String conditOther;


    private String nickName;
    private String userName;
    private String wxNum;
    private String headUrl;
    private String phone;

}

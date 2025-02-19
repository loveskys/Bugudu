package com.lyh.entity.activity.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lyh.entity.ComResponse;
import com.lyh.entity.activity.ActivityCondit;
import com.lyh.entity.activity.ActivityLocation;
import lombok.Data;
import org.springframework.util.StringUtils;
import java.io.Serial;
import java.io.Serializable;

/**
 * 活动发布dto
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ActivityDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String id;
    private String theme;
    private String intro;
    private String category;
    private String image;
    private String wxUrl;
//    private String imageType;

    //活动时间
    private String startDate;
    private String startTime;
    private String endDate;
    private String endTime;

    //联系方式 1微信号，2手机号，3群二维码
    private String contactType;
    private String contact;
    private String contact_image;

    //活动经纬度信息
    private ActivityLocation location;

        //活动要求限制
    private ActivityCondit condit;

    //活动是否删除：0未删，1已删除
    private String isDelete;

    //活动发起人
    private String issue;

    //活动申请状态：0申请中，1通过，2不通过
    private String auditStatus;
    //活动申请通过时间
    private String auditTime;
    //活动申请状态返回内容
    private String auditCont;


    private String createTime;
    private String createBy;
    private String updateTime;
    private String updateBy;


    public ComResponse<?> validate() {
        if (!StringUtils.hasText(theme)) {
            return new ComResponse<>().error("请填写活动名称");
        }
        if (!StringUtils.hasText(intro)) {
            return new ComResponse<>().error("请填写活动描述");
        }
        if (!StringUtils.hasText(category)) {
            return new ComResponse<>().error("请选择活动类别");
        }
        if (!StringUtils.hasText(image)) {
            return new ComResponse<>().error("请上传活动首图");
        }
        if (!StringUtils.hasText(condit.getPeopleNum())) {
            return new ComResponse<>().error("请填写活动人数");
        }
        if (!StringUtils.hasText(String.valueOf(location.getLongitude()))) {
            return new ComResponse<>().error("请选择活动地址");
        }
        if (!StringUtils.hasText(startDate) && !StringUtils.hasText(startTime)) {
            return new ComResponse<>().error("请填写活动开始时间");
        }
        if (!StringUtils.hasText(endDate) && !StringUtils.hasText(endTime)) {
            return new ComResponse<>().error("请填写活动结束时间");
        }
        return null;
    }
}

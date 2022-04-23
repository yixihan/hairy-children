package com.wq.pojo;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author wq
 * @since 2022-02-05
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="Adopt对象", description="")
public class Adopt implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "领养 id")
    @TableId(value = "adopt_id", type = IdType.AUTO)
    private Long adoptId;

    @ApiModelProperty(value = "申请人 id")
    private Long userId;

    @ApiModelProperty(value = "申请人用户名")
    @TableField(exist = false)
    private String userName;

    @ApiModelProperty(value = "申请人头像")
    @TableField(exist = false)
    private String userAvatar;

    @ApiModelProperty(value = "文章 id")
    private Long titleId;

    @ApiModelProperty(value = "申请理由")
    private String adoptReason;

    @ApiModelProperty(value = "申请人当前所在城市")
    private String adoptUserAddress;

    @ApiModelProperty(value = "申请人联系方式")
    private String adoptUserPhone;

    @ApiModelProperty(value = "养宠理念")
    private String adoptConcept;

    @ApiModelProperty(value = "接动物方式")
    private String adoptWay;

    @ApiModelProperty(value = "申请人年龄")
    private Integer adoptUserAge;

    @ApiModelProperty(value = "图片存储路径")
    private String adoptDir;

    @ApiModelProperty(value = "图片 url")
    private String imgsDir;

    @ApiModelProperty(value = "图片 url, 前端读取")
    @TableField(exist = false)
    private String[] imgs;

    @ApiModelProperty(value = "是否接受定期回访, 0 : 否, 1 : 是")
    private Integer isReturnVisit;

    @ApiModelProperty(value = "是否接受定期反馈领养信息, 0 : 否, 1 : 是")
    private Integer isFeedback;

    @ApiModelProperty(value = "是否成功, 0 : 未成功, 1 : 成功")
    private Integer isSuccess;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date gmtCreate;

    @ApiModelProperty(value = "修改时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date gmtModified;

    @ApiModelProperty(value = "乐观锁")
    @Version
    private Integer version;

    @ApiModelProperty(value = "逻辑删除")
    @TableLogic
    private Integer isDeleted;


}

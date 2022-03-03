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
@ApiModel(value="Clue对象", description="")
public class Clue implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "线索 id")
    @TableId(value = "clue_id", type = IdType.AUTO)
    private Long clueId;

    @ApiModelProperty(value = "提供者 id")
    private Long userId;

    @ApiModelProperty(value = "文章 id")
    private Long titleId;

    @ApiModelProperty(value = "线索内容")
    private String clueContent;

    @ApiModelProperty(value = "图片存储路径")
    private String clueDir;

    @ApiModelProperty(value = "图片 url")
    private String imgsDir;

    @ApiModelProperty(value = "图片 url, 前端读取")
    @TableField(exist = false)
    private String[] imgs;

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

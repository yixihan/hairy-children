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
@ApiModel(value="CollectionTitle对象", description="")
public class CollectionTitle implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键 id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "收藏夹 id")
    private Long collectionId;

    @ApiModelProperty(value = "文章 id")
    private Long titleId;

    @ApiModelProperty(value = "文章 名")
    @TableField(exist = false)
    private String titleName;

    @ApiModelProperty(value = "文章 预览图")
    @TableField(exist = false)
    private String titleAvatar;

    @ApiModelProperty(value = "文章 正文")
    @TableField(exist = false)
    private String titleContent;

    @ApiModelProperty(value = "文章作者 id")
    @TableField(exist = false)
    private Long titleAuthorId;

    @ApiModelProperty(value = "文章作者 用户名")
    @TableField(exist = false)
    private String titleAuthorName;

    @ApiModelProperty(value = "文章作者 头像")
    @TableField(exist = false)
    private String titleAuthorAvatar;

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

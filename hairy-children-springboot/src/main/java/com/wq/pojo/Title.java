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
import java.util.List;

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
@ApiModel(value = "Title对象", description = "")
public class Title implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "文章 id")
    @TableId(value = "title_id", type = IdType.AUTO)
    private Long titleId;

    @ApiModelProperty(value = "作者 id")
    private Long userId;

    @ApiModelProperty(value = "作者用户名")
    @TableField(exist = false)
    private String userName;

    @ApiModelProperty(value = "作者头像")
    @TableField(exist = false)
    private String userAvatar;

    @ApiModelProperty(value = "作者 城市")
    private String userAddress;

    @ApiModelProperty(value = "文章类型, 1 : 领养, 2 : 寻宠")
    private Integer titleType;

    @ApiModelProperty(value = "文章标题")
    private String titleName;

    @ApiModelProperty(value = "文章预览图")
    private String titleImg;

    @ApiModelProperty(value = "文章正文")
    private String titleContent;

    @ApiModelProperty(value = "文章图片路径")
    private String titleDir;

    @ApiModelProperty(value = "收藏数")
    private Integer collectionCount;

    @ApiModelProperty(value = "评论数")
    private Integer commentCount;

    @ApiModelProperty(value = "点赞数")
    private Integer likeCount;

    @ApiModelProperty(value = "是否完成, 0 : 未完成, 1 : 完成")
    private Integer isFinish;

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

    @ApiModelProperty(value = "申请列表")
    @TableField(exist = false)
    private List<Adopt> adoptList;

    @ApiModelProperty(value = "线索列表")
    @TableField(exist = false)
    private List<Clue> clueList;


}

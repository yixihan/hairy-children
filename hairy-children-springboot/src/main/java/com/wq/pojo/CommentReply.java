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
@ApiModel(value="CommentReply对象", description="")
public class CommentReply implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "子评论 id")
    @TableId(value = "reply_id", type = IdType.AUTO)
    private Long replyId;

    @ApiModelProperty(value = "父评论 id")
    private Long rootId;

    @ApiModelProperty(value = "回复的子评论 id")
    private Long replyCommentId;

    @ApiModelProperty(value = "回复的子评论 用户 id")
    private Long replyUserId;

    @ApiModelProperty(value = "回复的子评论用户名")
    @TableField(exist = false)
    private String replyUserName;

    @ApiModelProperty(value = "回复的子评论用户头像")
    @TableField(exist = false)
    private String replyUserAvatar;

    @ApiModelProperty(value = "评论者 id")
    private Long userId;

    @ApiModelProperty(value = "评论者用户名")
    @TableField(exist = false)
    private String userName;

    @ApiModelProperty(value = "评论者头像")
    @TableField(exist = false)
    private String userAvatar;

    @ApiModelProperty(value = "评论内容")
    private String content;

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

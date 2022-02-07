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
@ApiModel(value="CommentRoot对象", description="")
public class CommentRoot implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "父评论 id")
    @TableId(value = "root_id", type = IdType.AUTO)
    private Long rootId;

    @ApiModelProperty(value = "文章 id")
    private Long answerId;

    @ApiModelProperty(value = "评论者 id")
    private Long userId;

    @ApiModelProperty(value = "评论内容")
    private String content;

    @ApiModelProperty(value = "点赞数")
    private Integer likeCount;

    @ApiModelProperty(value = "回复数")
    private Integer replyCount;

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

    @ApiModelProperty(value = "子评论")
    @TableField(exist = false)
    private List<CommentReply> commentReplyList;


}

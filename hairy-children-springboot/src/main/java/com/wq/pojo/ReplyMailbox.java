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
 * @since 2022-02-13
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="ReplyMailbox对象", description="")
public class ReplyMailbox implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键 id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "文章 id")
    private Long titleId;

    @ApiModelProperty(value = "父评论 id")
    private Long rootId;

    @ApiModelProperty(value = "子评论 id")
    private Long replyId;

    @ApiModelProperty(value = "评论内容")
    private String replyContent;

    @ApiModelProperty(value = "发送者 id")
    private Long sendUserId;

    @ApiModelProperty(value = "接受者 id")
    private Long receiveUserId;

    @ApiModelProperty(value = "是否已读, 0 : 未读, 1 : 已读")
    private Integer isRead;

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

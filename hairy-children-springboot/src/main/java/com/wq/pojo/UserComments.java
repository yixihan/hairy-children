package com.wq.pojo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author : yixihan
 * @create : 2022-02-07-15:16
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserComments implements Serializable {

    @ApiModelProperty(value = "用户 id")
    private Long userId;

    @ApiModelProperty(value = "用户名")
    private String userName;

    @ApiModelProperty(value = "用户头像")
    private String userAvatar;

    @ApiModelProperty(value = "回答(文章) id")
    private Long answerId;

    @ApiModelProperty(value = "评论 id")
    private Long commentId;

    @ApiModelProperty(value = "父评论 id")
    private Long rootId;

    @ApiModelProperty(value = "父评论 用户id")
    private Long rootUserId;

    @ApiModelProperty(value = "父评论 用户名")
    private String rootUserName;

    @ApiModelProperty(value = "父评论 用户头像")
    private String rootUserAvatar;

    @ApiModelProperty(value = "父评论 内容")
    private String replyContent;

    @ApiModelProperty(value = "评论 内容")
    private String content;

    @ApiModelProperty(value = "点赞 数")
    private Integer likeCount;

    @ApiModelProperty(value = "回复 数")
    private Integer replyCount;

    @ApiModelProperty(value = "评论 类型(父评论 : 1, 子评论 : 2)")
    private Integer type;

    @ApiModelProperty(value = "评论 时间")
    private Date gmtCreate;
}

package com.wq.service.impl;

import com.wq.pojo.CommentReply;
import com.wq.mapper.CommentReplyMapper;
import com.wq.service.CommentReplyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wq
 * @since 2022-02-05
 */
@Service("commentReplyService")
public class CommentReplyServiceImpl extends ServiceImpl<CommentReplyMapper, CommentReply> implements CommentReplyService {

}

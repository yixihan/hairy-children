package com.wq.service.impl;

import com.wq.mapper.CommentRootMapper;
import com.wq.pojo.CommentReply;
import com.wq.pojo.CommentRoot;
import com.wq.service.CommentRootService;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author : yixihan
 * @create : 2022-02-07-20:16
 */
class CommentRootServiceImplTest {

    @Resource
    private CommentRootService commentRootService;

    @Resource
    private CommentRootMapper commentRootMapper;

    @Test
    void getSonComments() {
        List<CommentReply> sonComments = commentRootService.getSonComments (1L);
        System.out.println (sonComments.isEmpty ());

        System.out.println (sonComments);
    }

    @Test
    public void getSonCommentsTest1 () {
        List<CommentReply> sonComments = commentRootMapper.getSonComments (1L);
        System.out.println (sonComments.isEmpty ());

        System.out.println (sonComments);

    }

    @Test
    public void getRootCommentsTest1 () {
        List<CommentRoot> rootComments = commentRootMapper.getAllRootCommentsByTitleId (2L);
        System.out.println (rootComments.isEmpty ());

        System.out.println (rootComments);
    }
}
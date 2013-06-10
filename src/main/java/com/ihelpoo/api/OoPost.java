package com.ihelpoo.api;

import com.ihelpoo.api.dao.PostDao;
import com.ihelpoo.api.model.PostList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class OoPost {

    @Autowired
    private
    PostDao postDao;

    @RequestMapping(value = "/posts.xml", method = RequestMethod.GET, produces = "application/xml")
    @ResponseBody
    public PostList getPostList(@RequestParam(value = "pageIndex") int pageIndex,
                                @RequestParam(value = "pageSize") int pageSize,
                                @RequestParam(value = "catalog") int catalog) {
        return postDao.getPostListByTimeLevel("//TODO, with time level", catalog, pageIndex, pageSize);
    }

    @RequestMapping(value = "/posts.json", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public PostList getPostListJson(@RequestParam(value = "pageIndex") int pageIndex,
                                @RequestParam(value = "pageSize") int pageSize,
                                @RequestParam(value = "catalog") int catalog) {
        return postDao.getPostListByTimeLevel("//TODO, with time level", catalog, pageIndex, pageSize);
    }
}

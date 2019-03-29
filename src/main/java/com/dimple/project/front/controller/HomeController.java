package com.dimple.project.front.controller;

import com.dimple.framework.aspectj.lang.annotation.VLog;
import com.dimple.framework.web.controller.BaseController;
import com.dimple.project.blog.blog.domain.Blog;
import com.dimple.project.blog.blog.service.BlogService;
import com.dimple.project.blog.category.service.CategoryService;
import com.dimple.project.blog.tag.domain.Tag;
import com.dimple.project.blog.tag.service.TagService;
import com.dimple.project.front.service.HomeService;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @className: HomeController
 * @description: 前台首页Controller
 * @auther: Dimple
 * @date: 03/27/19
 * @version: 1.0
 */
@Controller
public class HomeController extends BaseController {

    @Autowired
    HomeService homeService;
    @Autowired
    BlogService blogService;
    @Autowired
    CategoryService categoryService;
    @Autowired
    TagService tagService;

    /**
     * 设置前台页面公用的部分代码
     * 均设置Redis缓存
     */
    private void setCommonMessage(Model model) {
        //获取分类下拉项中的分类
        model.addAttribute("categories", categoryService.selectSupportCategoryList());
        //查询所有的标签
        model.addAttribute("tags", tagService.selectTagList(new Tag()));
        //查询最近更新的文章
        model.addAttribute("newestUpdateBlog", blogService.selectNewestUpdateBlog());
        //查询文章排行
        model.addAttribute("blogRanking", blogService.selectBlogRanking());
        //查询推荐博文
        model.addAttribute("supportBlog", blogService.selectSupportBlog());
    }

    /**
     * 首页
     */
    @GetMapping("/")
    @VLog(title = "首页")
    public String index(Model model) {
        setCommonMessage(model);
        model.addAttribute("blogs", homeService.selectFrontNewestBlogList());
        return "front/index";
    }

    /**
     * 关于我
     */
    @VLog(title = "关于我")
    @GetMapping("/about.html")
    public String about(Model model) {
        setCommonMessage(model);
        return "front/about";
    }

    /**
     * 归档
     */
    @VLog(title = "归档")
    @GetMapping("/archives.html")
    public String archives(Model model) {
        setCommonMessage(model);
        return "front/archives";
    }

    @VLog(title = "博客")
    @GetMapping("/article/{blogId}.html")
    public String article(@PathVariable Integer blogId, Model model) {
        setCommonMessage(model);
        model.addAttribute("blog", blogService.selectBlogWithTextAndTagsAndCategoryByBlogId(blogId));
        model.addAttribute("nextBlog", blogService.selectNextBlogById(blogId));
        model.addAttribute("previousBlog", blogService.selectPreviousBlogById(blogId));
        model.addAttribute("randBlogList", blogService.selectRandBlogList());
        return "front/article";
    }

    @VLog(title = "分类")
    @GetMapping("/category/{categoryId}/{pageNum}.html")
    public String category(@PathVariable Integer categoryId, @PathVariable Integer pageNum, Model model) {
        setCommonMessage(model);
        model.addAttribute("category", categoryService.selectCategoryById(categoryId));
        Blog blog = new Blog();
        blog.setCategoryId(categoryId);
        PageHelper.startPage(pageNum, 12, "create_time desc");
        model.addAttribute("blogs", blogService.selectBlogList(blog));
        return "front/category";
    }

    /**
     * 留言
     */
    @VLog(title = "留言")
    @GetMapping("/leaveComment.html")
    public String leaveComment(Model model) {
        setCommonMessage(model);
        return "front/leaveComment";
    }

    /**
     * 版权声明
     */
    @GetMapping("/copyright.html")
    public String copyright(Model model) {
        setCommonMessage(model);
        return "front/other/copyright";
    }

    /**
     * 侵删联系
     */
    @GetMapping("/delete.html")
    public String delete(Model model) {
        setCommonMessage(model);
        return "front/other/delete";
    }

    /**
     * 友链显示
     */
    @GetMapping("/link.html")
    public String link(Model model) {
        setCommonMessage(model);
        return "front/link";
    }
}

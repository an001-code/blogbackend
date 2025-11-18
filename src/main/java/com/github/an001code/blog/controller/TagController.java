package com.github.an001code.blog.controller;

import com.github.an001code.blog.pojo.*;
import com.github.an001code.blog.service.TagService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@Slf4j
public class TagController {
    @Autowired
    TagService tagService;

    /**
     * 标签类别查询
     * @param tagQuery
     * @return
     */
    @GetMapping("/tags/select")
    public Result getTagList(TagQuery tagQuery){
        log.info("进入getTagList");
        PageResult<Tag> tagPage = tagService.getTagList(tagQuery);
        log.info("得到pageBean");
        return Result.success(tagPage);

    }

    /**
     * 根据id查询
     * @param id
     * @return
     */
    @GetMapping("/tags/{id}")
    public Result getById(@PathVariable  Long id){
        log.info("进入getById");
        if(id == null){
            return Result.error("id不能为空");
        }
        Tag tag = tagService.getById(id);
        return Result.success(tag);

    }

    /**
     * 标签添加
     * @param tag
     * @return
     */

    @PostMapping("/tags")
    public Result addTag(@RequestBody Tag tag){
        log.info("进入addTag");
        if(tag.getTagName() == null || tag.getTagName() == ""){
            return Result.error("标签名不能为空");
        }
        int affectRows = tagService.addTag(tag);
        if(affectRows < 1){
            return Result.error("标签已存在");
        }
        else{
            return Result.success();
        }
    }

    /**
     * 标签修改
     * @param tag
     * @return
     */

    @PutMapping("/tags")
    public Result updateTag(@RequestBody Tag tag){
        log.info("进入updateTag");
        if(tag.getTagId() == null){
            return Result.error("id不能为空");
        }
        int affectRows = tagService.updateTag(tag);
        if(affectRows <1 ){
            return Result.error("修改失败");
        }
        else{
            return Result.success();
        }
    }

    @DeleteMapping("/tags/{ids}")
    public Result deleteTag(@PathVariable List<Long> ids){
        log.info("进入deleteTag");
        if(tagService.deleteTag(ids)){
            return Result.success();
        }
        else{
            return Result.error("删除失败");
        }
    }

}

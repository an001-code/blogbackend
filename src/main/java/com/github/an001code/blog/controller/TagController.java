package com.github.an001code.blog.controller;

import com.github.an001code.blog.pojo.Result;
import com.github.an001code.blog.pojo.Tag;
import com.github.an001code.blog.pojo.TagPageBean;
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
     * 标签列表查询
     * @param begin
     * @param end
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/tags/select")
    public Result getTagList(@DateTimeFormat(pattern = "yy-MM-dd") LocalDate begin,
                             @DateTimeFormat(pattern = "yy-MM-dd")LocalDate end,
                             @RequestParam(defaultValue = "1") Integer page,
                             @RequestParam(defaultValue = "10") Integer pageSize){
        log.info("进入getTagList");
        TagPageBean tagPageBean = tagService.getTagList(begin,end,page,pageSize);
        log.info("得到pageBean");
        return Result.success(tagPageBean);

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

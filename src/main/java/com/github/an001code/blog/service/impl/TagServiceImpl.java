package com.github.an001code.blog.service.impl;

import com.github.an001code.blog.mapper.TagMapper;
import com.github.an001code.blog.pojo.*;
import com.github.an001code.blog.service.TagService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
public class TagServiceImpl implements TagService {
    @Autowired
    TagMapper tagMapper;

    @Override
    public PageResult<Tag> getTagList(TagQuery tagQuery) {
        PageHelper.startPage(tagQuery.getPage(),tagQuery.getPageSize());
        List<Tag> tagList = tagMapper.getTagList(tagQuery);
        Page<Tag> p = (Page<Tag>) tagList;
        PageResult<Tag> tagPage = new PageResult<>(p.getTotal(),p.getResult());
        return tagPage;

    }

    @Override
    public Tag getById(Long id) {
        Tag tag = tagMapper.getById(id);
        return tag;
    }

    @Override
    public int addTag(Tag tag) {
         Tag tmp = tagMapper.getByName(tag.getTagName());   //判断是否已经存在同名的标签
         if(tmp != null){
             return 0;
         }
         return tagMapper.insert(tag);
    }

    @Override
    public int updateTag(Tag tag) {
        if(tag.getTagName() != null){      //判断是否已经存在与要更改的标签名字一样的标签
            String tagName = tag.getTagName();
            Tag tmp = tagMapper.getByName(tagName);
            log.info("已存在该内容的标签");
            if(tmp != null){
                return 0;
            }
        }
        int affectRows = tagMapper.update(tag);
        return affectRows;
    }

    @Override
    public boolean deleteTag(List<Long> ids) {
        int affectRows = tagMapper.delete(ids);
        if(affectRows < 1){
            return false;
        }
        return true;
    }

    //增加使用数量
    public void increaseUseCount(Long id){
        tagMapper.increaseUseCount(id);
    }

    //
    public void decreaseUseCount(Long id){
        tagMapper.decreaseUseCount(id);
    }
}

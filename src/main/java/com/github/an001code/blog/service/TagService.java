package com.github.an001code.blog.service;

import com.github.an001code.blog.pojo.PageResult;
import com.github.an001code.blog.pojo.Tag;
import com.github.an001code.blog.pojo.TagQuery;

import java.util.List;

public interface TagService {
    PageResult<Tag> getTagList(TagQuery tagQuery);

    Tag getById(Long id);

    int addTag(Tag tag);

    int updateTag(Tag tag);

    boolean deleteTag(List<Long> ids);
}

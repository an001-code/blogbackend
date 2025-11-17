package com.github.an001code.blog.service;

import com.github.an001code.blog.pojo.Tag;
import com.github.an001code.blog.pojo.TagPageBean;

import java.time.LocalDate;
import java.util.List;

public interface TagService {
    TagPageBean getTagList(LocalDate begin, LocalDate end, Integer page, Integer pageSize);

    Tag getById(Long id);

    int addTag(Tag tag);

    int updateTag(Tag tag);

    boolean deleteTag(List<Long> ids);
}

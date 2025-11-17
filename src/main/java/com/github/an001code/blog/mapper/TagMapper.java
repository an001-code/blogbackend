package com.github.an001code.blog.mapper;

import com.github.an001code.blog.pojo.Tag;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface TagMapper {

    List<Tag> getTagList(LocalDate begin, LocalDate end);

    @Select("select * from tag where tag_id = #{id}")
    Tag getById(Long id);

    @Select("select * from tag where tag_name = #{tagName}")
    Tag getByName(String tagName);


    int insert(Tag tag);

    int update(Tag tag);

    int delete(List<Long> ids);
}

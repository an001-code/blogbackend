package com.github.an001code.blog.mapper;

import com.github.an001code.blog.pojo.Tag;
import com.github.an001code.blog.pojo.TagQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface TagMapper {

    List<Tag> getTagList(TagQuery tagQuery);

    @Select("select * from tag where tag_id = #{id}")
    Tag getById(Long id);

    @Select("select * from tag where tag_name = #{tagName}")
    Tag getByName(String tagName);


    int insert(Tag tag);

    int update(Tag tag);

    int delete(List<Long> ids);
    
    @Update("update tag set use_count = use_count+1 where tag_id = #{id} and status = 1")
    void increaseUseCount(Long id);

    @Update("update tag set use_count = GREATEST(use_count-1,0) where tag_id = #{id} and status = 1")
    void decreaseUseCount(Long id);
}

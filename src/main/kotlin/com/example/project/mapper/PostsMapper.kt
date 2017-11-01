package com.example.project.mapper

import com.example.project.bean.CommentBean
import com.example.project.bean.PostsDetailBean
import com.example.project.bean.PostsListBean
import org.apache.ibatis.annotations.*
import java.sql.Timestamp


@Mapper
interface PostsMapper {
    @Select("select p.id, p.content, p.user_id, l.status " +
            "from posts as p left join `like` as l " +
            "on p.id = l.posts_id " +
            "and l.user_id = #{user_id} " +
            "order by timestamp desc")
    fun getPostsList(@Param("user_id") userId: Int): List<PostsListBean>

    @Select("select count(*) from `like` where posts_id = #{posts_id} and status = 1")
    fun getLikeCount(@Param("posts_id") postsId: Int): Int

    @Insert("insert into posts (content, timestamp, user_id) values (#{content}, #{timestamp}, #{user_id})")
    fun createPost(@Param("content") content: String,
                   @Param("timestamp") timestamp: Timestamp = Timestamp(System.currentTimeMillis()),
                   @Param("user_id") userId: Int): Int

    @Select("select count(*) from `like` where user_id = #{user_id} and posts_id = #{posts_id}")
    fun like(@Param("user_id") userId: Int,
             @Param("posts_id") postsId: Int): Int

    @Insert("insert into `like` (user_id, posts_id, status) values (#{user_id}, #{posts_id}, 1)")
    fun insertLike(@Param("user_id") userId: Int,
                   @Param("posts_id") postsId: Int): Int

    @Update("update `like` set status = 1 where user_id = #{user_id} and posts_id = #{posts_id}")
    fun updateLike(@Param("user_id") userId: Int,
                   @Param("posts_id") postsId: Int): Int

    @Update("update `like` set status = 0 where user_id = #{user_id} and posts_id = #{posts_id}")
    fun updateDislike(@Param("user_id") userId: Int,
                      @Param("posts_id") postsId: Int): Int

    @Select("select p.id, p.content, l.status " +
            "from posts as p, `like` as l " +
            "where l.posts_id = p.id " +
            "and p.id = #{posts_id} " +
            "and l.user_id = #{user_id}")
    fun getPostsDetailWithLike(@Param("posts_id") postsId: Int,
                               @Param("user_id") userId: Int): Map<String, Any>

    @Select("select id, content from posts where id = #{posts_id}")
    fun getPostsDetailWithoutLike(@Param("posts_id") postsId: Int): Map<String, Any>

    @Insert("insert into comment (content, posts_id, user_id) values (#{content}, #{posts_id}, #{user_id})")
    fun createComment(@Param("content") contnet: String,
                      @Param("posts_id") postsId: Int,
                      @Param("user_id") userId: Int): Int

    @Select("select c.content, u.name " +
            "from comment as c, user as u " +
            "where c.user_id = u.id " +
            "and c.posts_id = #{posts_id}")
    fun getComment(@Param("posts_id") postsId: Int): List<Map<String, String>>
}
package com.example.project.controller

import com.example.project.bean.*
import com.example.project.mapper.PostsMapper
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/posts")
@EnableAutoConfiguration
class PostsController(var mapper: PostsMapper) {

    @GetMapping("/list")
    fun getPosts(@RequestParam("user") user: Int, @RequestParam("page") page: Int): List<Map<String, Any>> {
        val list = mapper.getPostsList(user, (page - 1) * 10)
        val postsList = ArrayList<Map<String, Any>>()
        list.forEach {
            val map = HashMap<String, Any>()
            map.put("id", it.id)
            map.put("content", it.content)
            map.put("userId", it.userId)
            map.put("status", it.status ?: 0)
            map.put("like_count", mapper.getLikeCount(it.id))
            postsList.add(map)
        }
        return postsList
    }

    @PostMapping("/create")
    fun createPosts(@RequestParam("content") content: String,
               @RequestParam("user_id") userId: Int): SimpleBean {
        val result = mapper.createPost(content, userId = userId)
        return SimpleBean(if (result == 0) "fail" else "success")
    }

    @PostMapping("/like")
    fun like(@RequestParam("user_id") userId: Int,
             @RequestParam("posts_id") postsId: Int): LikeBean {
        val searchResult = mapper.like(userId, postsId)
        val result = if (searchResult == 0) {
            mapper.insertLike(userId, postsId)
        } else {
            mapper.updateLike(userId, postsId)
        }

        return LikeBean(if (result == 1) "成功" else "失败", 1)
    }

    @PostMapping("/dislike")
    fun dislike(@RequestParam("user_id") userId: Int,
                @RequestParam("posts_id") postsId: Int): LikeBean {
        val result = mapper.updateDislike(userId, postsId)
        return LikeBean(if (result == 1) "成功" else "失败", 0)
    }

    @GetMapping("/detail")
    fun detail(@RequestParam("posts_id") postsId: Int,
               @RequestParam("user_id") userId: Int): Map<String, Any?> {
        val searchResult = mapper.like(userId, postsId)
        if (searchResult == 0) {
            val map = mapper.getPostsDetailWithoutLike(postsId).toMutableMap()
            map.put("status", 0)
            return map
        }
        return mapper.getPostsDetailWithLike(postsId, userId)
    }

    @PostMapping("/comment")
    fun comment(@RequestParam("content") content: String,
                @RequestParam("user_id") userId: Int,
                @RequestParam("posts_id") postsId: Int): SimpleBean {
        val result = mapper.createComment(content, postsId, userId)
        return SimpleBean(if (result == 0) "失败" else "成功")
    }

    @GetMapping("/comment")
    fun getComment(@RequestParam("posts_id") postsId: Int): List<Map<String, String>> {
        return mapper.getComment(postsId)
    }
}
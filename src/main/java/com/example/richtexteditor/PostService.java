package com.example.richtexteditor;

import com.example.richtexteditor.models.Post;
import com.example.richtexteditor.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {
    @Autowired
    private PostRepository postRepository;

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }
    public Post savePost(Post post) {
        return postRepository.save(post);
    }
}


package com.example.richtexteditor.repository;

import com.example.richtexteditor.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}


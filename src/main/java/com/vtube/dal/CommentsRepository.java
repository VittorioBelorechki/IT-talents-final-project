package com.vtube.dal;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vtube.model.Comment;

@Repository
public interface CommentsRepository extends JpaRepository<Comment, Long>{

	Optional<Comment> findById(Integer commentId);

	List<Comment> findAllBySuperCommentId(Long commentId);


	
}
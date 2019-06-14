package com.vtube.dal;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vtube.model.User;
import com.vtube.model.Video;

@Repository
public interface UsersRepository extends JpaRepository<User, Long>{
	Optional<User> findUserByEmail(String email);
	Optional<User> findUserByNickName(String nickName);
	List<User> findAllBywatchedVideos(Video video);
	List<User> findAllByVideosForLater(Video video);
	List<User> findAllByLikedVideos(Video video);
}
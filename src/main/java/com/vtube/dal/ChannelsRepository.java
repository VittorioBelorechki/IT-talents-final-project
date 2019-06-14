package com.vtube.dal;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vtube.model.Channel;
import com.vtube.model.User;

@Repository
public interface ChannelsRepository extends JpaRepository<Channel, Long>{
	
	Optional<Channel> findChannelByOwner(User owner);
}

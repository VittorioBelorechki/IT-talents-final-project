package com.vtube.service;

import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vtube.dal.ChannelsRepository;
import com.vtube.dal.UsersRepository;
import com.vtube.dal.VideosRepository;
import com.vtube.dto.IDTO;
import com.vtube.dto.LoginDTO;
import com.vtube.dto.SignUpDTO;
import com.vtube.dto.SimpleMessageDTO;
import com.vtube.dto.UserDTO;
import com.vtube.dto.VideoDTO;
import com.vtube.exceptions.BadCredentialsException;
import com.vtube.exceptions.ChannelNotFoundException;
import com.vtube.exceptions.EmailExistsException;
import com.vtube.exceptions.InvalidAgeException;
import com.vtube.exceptions.InvalidEmailException;
import com.vtube.exceptions.InvalidNameException;
import com.vtube.exceptions.InvalidPasswordException;
import com.vtube.exceptions.ConflictException;
import com.vtube.exceptions.UserExistsException;
import com.vtube.exceptions.UserNotFoundException;
import com.vtube.exceptions.VideoNotFoundException;
import com.vtube.model.Channel;
import com.vtube.model.User;
import com.vtube.model.Video;
import com.vtube.validations.UserValidation;

import lombok.NonNull;

/**
 * Class to manage database with user related requests.
 */
@Service
public class UserService {
	@Autowired
	private UsersRepository userRepository;

	@Autowired
	private ChannelsRepository channelRepository;

	@Autowired
	private VideosRepository videosRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private UserValidation userValidator;

	@Autowired
	private VideoService videoService;

	private UserDTO createUser(SignUpDTO signUpData) {
		User user = this.modelMapper.map(signUpData, User.class);

		user = this.userRepository.save(user);

		UserDTO userDTO = this.convertFromUserToUserDTO(user);

		return userDTO;
	}
	
	private boolean validateUser (SignUpDTO signUpData)
			throws InvalidNameException, InvalidEmailException, InvalidPasswordException, InvalidAgeException, EmailExistsException, UserExistsException {
		UserValidation userValidator = this.getUserValidator();
		userValidator.confirm(signUpData);
		
		String email = signUpData.getEmail();
		String nickName = signUpData.getNickName();
		
		this.haveSameEmail(email);
		this.haveSameNickName(nickName);
		
		return true;
	}
	
	public UserDTO signUpUser (SignUpDTO signUpData)
			throws InvalidNameException, InvalidEmailException, InvalidPasswordException, InvalidAgeException, EmailExistsException, UserExistsException {
		UserDTO user = null;
		
		if(this.validateUser(signUpData)) {
			//encrypt user password
			signUpData.setPassword(this.encryptPassword(signUpData.getPassword()));
			
			//add user to db and return the proper object to be sent as response
			user = this.createUser(signUpData);
		}
		
		return user;
	}

	public UserDTO convertFromUserToUserDTO(User user) {
		UserDTO userDTO = this.modelMapper.map(user, UserDTO.class);

		Channel channel = null;
		try {
			channel = this.channelRepository.findChannelByOwner(user).get();
		} catch (NoSuchElementException e) {
			// do nothing and channel remains null
		}

		if (channel != null)
			userDTO.setNumberOfSubscribers((long) channel.getUsersSubscribedToChannel().size());

		return userDTO;

	}

	public UserDTO getUserDTOById(Long id) throws UserNotFoundException {
		User user = null;
		try {
			user = this.userRepository.findById(id).get();
		} catch (NoSuchElementException e) {
			throw new UserNotFoundException("No such user");
		}

		UserDTO userDTO = this.modelMapper.map(user, UserDTO.class);
		if (user.getLikedVideos() != null) {
			userDTO.setNumberOfLikedVideos(user.getLikedVideos().size());
		}
		if (user.getOwnedChannel() != null && user.getOwnedChannel().getOwnedVideos() != null) {
			userDTO.setNumberOfOwnVideos(user.getOwnedChannel().getOwnedVideos().size());
		}

		return userDTO;
	}

	public Long login(LoginDTO loginData) throws BadCredentialsException {
		Optional<User> user = this.userRepository.findUserByEmail(loginData.getEmail());
		if (!user.isPresent() || !this.checkPasswordById(loginData.getPassword(), user.get().getPassword())) {
			throw new BadCredentialsException("Wrong username or password");
		}

		return user.get().getId();
	}

	public void haveSameEmail(String email) throws EmailExistsException {
		if (this.userRepository.findUserByEmail(email).isPresent()) {
			throw new EmailExistsException();
		}
	}

	public void haveSameNickName(String nickName) throws UserExistsException {
		if (this.userRepository.findAll().stream().anyMatch(u -> u.getNickName().equals(nickName))) {
			throw new UserExistsException();
		}
	}

	public String encryptPassword(String password) {
		String encryptedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

		return encryptedPassword;
	}

	public boolean checkPasswordById(String password, String encryptedPassword) {
		return BCrypt.checkpw(password, encryptedPassword);
	}

	public UserValidation getUserValidator() {
		return this.userValidator;
	}

	public @NonNull Optional<User> findById(Long id) {
		return this.userRepository.findById(id);
	}

	public User getUserById(Long userId) {
		return this.userRepository.findById(userId).get();
	}

	public List<VideoDTO> getUserWatchedVideos(Long userId) {
		User user = this.userRepository.findById(userId).get();

		List<Video> watchedVideos = user.getWatchedVideos();
		List<VideoDTO> watchedVideosDTO = new LinkedList<VideoDTO>();

		watchedVideos.stream()
				.forEach(video -> watchedVideosDTO.add(this.videoService.convertFromVideoToVideoDTO(video)));

		return watchedVideosDTO;
	}

	public List<VideoDTO> getUserVideosForLater(Long userId) {
		User user = this.userRepository.findById(userId).get();

		List<Video> videosForLater = user.getVideosForLater();
		List<VideoDTO> videosForLaterDTO = new LinkedList<VideoDTO>();

		videosForLater.stream()
				.forEach(video -> videosForLaterDTO.add(this.videoService.convertFromVideoToVideoDTO(video)));

		return videosForLaterDTO;
	}
	
	public List<VideoDTO> getUserLikedVideos(Long userId) throws UserNotFoundException {
		User user = null;
		try {
			user = this.userRepository.findById(userId).get();
		} catch (NoSuchElementException e) {
			throw new UserNotFoundException();
		}

		List<Video> likedVideos = user.getLikedVideos();
		List<VideoDTO> likedVideosDTO = new LinkedList<VideoDTO>();

		likedVideos.stream()
				.forEach(video -> likedVideosDTO.add(this.videoService.convertFromVideoToVideoDTO(video)));

		return likedVideosDTO;
	}

	@Transactional
	public IDTO likeVideo(Long videoId, Long userId) throws VideoNotFoundException {
		Video video = null;
		
		try {
			video = this.videosRepository.findById(videoId).get();
		} catch (NoSuchElementException e) {
			throw new VideoNotFoundException();
		}
		
		User user = this.userRepository.findById(userId).get();
		SimpleMessageDTO message = new SimpleMessageDTO();
		
		//if user've already liked this video
		if(user.getLikedVideos().contains(video)) {
			message.setMessage("Already liked");
			return message;
		}
		
		//if user've already dislike this video
		if(video.getUsersWhoDisLikeThisVideo().contains(user)) {
			video.getUsersWhoDisLikeThisVideo().remove(user);
		}
		video.getUsersWhoLikeThisVideo().add(user);
		this.videosRepository.save(video);
		
		user.getLikedVideos().add(video);
		this.userRepository.save(user);
		
		message.setMessage("Liked");
		
		return message;
	}

	@Transactional
	public IDTO removeVideoLike(Long videoId, Long userId) throws VideoNotFoundException {
		Video video = null;
		
		try {
			video = this.videosRepository.findById(videoId).get();
		} catch (NoSuchElementException e) {
			throw new VideoNotFoundException();
		}
		
		User user = this.userRepository.findById(userId).get();
		SimpleMessageDTO message = new SimpleMessageDTO();
		
		//if user've already liked this video
		if(!user.getLikedVideos().contains(video)) {
			message.setMessage("No like on this video");
			return message;
		}
		
		video.getUsersWhoLikeThisVideo().remove(user);
		this.videosRepository.save(video);
		
		user.getLikedVideos().remove(video);
		this.userRepository.save(user);
		
		message.setMessage("Like removed");
		
		return message;
	}

	@Transactional
	public IDTO dislikeVideo(Long videoId, Long userId) throws VideoNotFoundException {
		Video video = null;
		
		try {
			video = this.videosRepository.findById(videoId).get();
		} catch (NoSuchElementException e) {
			throw new VideoNotFoundException();
		}
		
		User user = this.userRepository.findById(userId).get();
		SimpleMessageDTO message = new SimpleMessageDTO();
		
		//if user've already disliked this video
		if(video.getUsersWhoDisLikeThisVideo().contains(user)) {
			message .setMessage("Already disliked");
			return message;
		}
		
		//if user've already liked this video
		if(video.getUsersWhoLikeThisVideo().contains(user)) {
			video.getUsersWhoLikeThisVideo().remove(user);
			user.getLikedVideos().remove(video);
		}
		
		video.getUsersWhoDisLikeThisVideo().add(user);
		this.videosRepository.save(video);
		
		message.setMessage("Disliked");
		
		return message;
	}
	
	@Transactional
	public IDTO removeVideoDislike(Long videoId, Long userId) throws VideoNotFoundException {
		Video video = null;
		
		try {
			video = this.videosRepository.findById(videoId).get();
		} catch (NoSuchElementException e) {
			throw new VideoNotFoundException();
		}
		
		User user = this.userRepository.findById(userId).get();
		SimpleMessageDTO message = new SimpleMessageDTO();
		
		if(!video.getUsersWhoDisLikeThisVideo().contains(user)) {
			message .setMessage("No dislike on this video");
			return message;
		}
		
		video.getUsersWhoDisLikeThisVideo().remove(user);
		this.videosRepository.save(video);
		
		message.setMessage("Dislike removed");
		
		return message;
	}

	@Transactional
	public IDTO subscribeToChannel(Long userId, Long channelId) throws ChannelNotFoundException, ConflictException {
		Channel channel = null;
		try {
			channel = this.channelRepository.findById(channelId).get();
		} catch (NoSuchElementException e){
			throw new ChannelNotFoundException();
		}
		
		if(channel.getOwner().getId().equals(userId)) {
			throw new ConflictException("Cannot subscribe to your own channel!");
		}
		
		User user = this.userRepository.findById(userId).get();
		
		SimpleMessageDTO message = new SimpleMessageDTO();
		
		if(user.getSubscribedChannels().contains(channel)) {
			user.getSubscribedChannels().remove(channel);
			channel.getUsersSubscribedToChannel().remove(user);
			message.setMessage("Unsubscribed");
		} else {
			user.getSubscribedChannels().add(channel);
			channel.getUsersSubscribedToChannel().add(user);
			message.setMessage("Subscribed");
		}
		
		this.userRepository.save(user);
		this.channelRepository.save(channel);
		
		return message;
	}

	public SimpleMessageDTO watchVideoLater(Long userId, Long videoId) throws VideoNotFoundException {
		Video video = null;
		try {
			video = this.videosRepository.findById(videoId).get();
		} catch (NoSuchElementException e){
			throw new VideoNotFoundException();
		}
		
		SimpleMessageDTO message = new SimpleMessageDTO();
		User user = this.userRepository.findById(userId).get();

		if(!user.getVideosForLater().contains(video)) {
			message.setMessage("Video added to watch later list!");
			user.getVideosForLater().add(video);
		} else {
			message.setMessage("Video removed from watch later list!");
			user.getVideosForLater().remove(video);
		}

		this.userRepository.save(user);
		
		return message;
	}
}

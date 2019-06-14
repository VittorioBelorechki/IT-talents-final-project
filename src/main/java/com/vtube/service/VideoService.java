package com.vtube.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

import javax.transaction.Transactional;

import org.apache.commons.io.FilenameUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.vtube.dal.ChannelsRepository;
import com.vtube.dal.CommentsRepository;
import com.vtube.dal.UsersRepository;
import com.vtube.dal.VideosRepository;
import com.vtube.dto.BigVideoDTO;
import com.vtube.dto.CommentDTO;
import com.vtube.dto.CreatedVideoDTO;
import com.vtube.dto.SmallVideoDTO;
import com.vtube.dto.VideoDTO;
import com.vtube.dto.VideoToSaveDTO;
import com.vtube.exceptions.FileExistsException;
import com.vtube.exceptions.UnsupportedFileFormatException;
import com.vtube.exceptions.VideoNotFoundException;
import com.vtube.model.Channel;
import com.vtube.model.User;
import com.vtube.model.Video;

/**
 * Class to manage database with video related requests.
 */
@Service
public class VideoService {
	private static final String VIDEO_DIR_NAME = "Video";
	private static final String THUMBNAIL_DIR_NAME = "Picture";
	private static final String SUPPORTED_VIDEO_FORMATS = "mp4 avi wmv";
	private static final String SUPPORTED_THUMBNAIL_FORMATS = "jpeg jpg png gif";
	private static final String UPLOAD_DIR = "..\\VTubeFileStorage\\";

	@Autowired
	private VideosRepository videosRepository;
	
	@Autowired
	private UsersRepository usersRepository;
	
	@Autowired
	private CommentsRepository commentsRepository;
	
	@Autowired
	private ChannelsRepository channelsRepository;
	
	@Autowired
	private ModelMapper modelMapper;

	public boolean findById(Long videoId) {
		try {
			videosRepository.findById(videoId).get();
		} catch (NoSuchElementException e) {
			return false;
		}
		return true;
	}

	public Video getVideoById(Long videoId) {
		return this.videosRepository.findById(videoId).get();
	}

	public CreatedVideoDTO uploadVideoData(MultipartFile file, 
			MultipartFile thumbnail, 
			String title,
			String description, 
			Long ownerId, 
			Channel channel)
			throws FileExistsException, VideoNotFoundException, UnsupportedFileFormatException {
		this.checkFileFormat(file, SUPPORTED_VIDEO_FORMATS, "Video");
		this.checkFileFormat(thumbnail, SUPPORTED_THUMBNAIL_FORMATS, "Picture");

		VideoToSaveDTO videoToSave = new VideoToSaveDTO();
		videoToSave.setDescription(description);
		if (title == null || title.trim().equals("")) {
			videoToSave.setTitle(FilenameUtils.removeExtension(file.getOriginalFilename()));
		} else {
			videoToSave.setTitle(title);
		}

		String videoUrl = "";
		String thumbnailUrl = "";

		try {
			videoUrl = this.saveFileToDir(file, VIDEO_DIR_NAME, ownerId);
		} catch (IOException e) {
			throw new VideoNotFoundException();
		}

		try {
			thumbnailUrl = this.saveFileToDir(thumbnail, THUMBNAIL_DIR_NAME, ownerId);
		} catch (IOException e) {
			throw new VideoNotFoundException();
		}

		videoToSave.setUrl(videoUrl);
		videoToSave.setThumbnail(thumbnailUrl);

		Long videoId = this.saveFileToDB(videoToSave, channel);

		CreatedVideoDTO video = new CreatedVideoDTO();
		video.setId(videoId);
		video.setTitle(videoToSave.getTitle());
		video.setDescription(videoToSave.getDescription());
		video.setUrl(videoToSave.getUrl());

		return video;
	}

	public Long saveFileToDB(VideoToSaveDTO videoToSave, Channel channel) {
		Video video = this.modelMapper.map(videoToSave, Video.class);

		video.setDateOfCreation(LocalDate.now());
		video.setOwner(channel);

		Long id = this.videosRepository.save(video).getId();

		return id;
	}

	// save file to directory and returns it's url
	private String saveFileToDir(MultipartFile file, String fileDir, Long ownerId)
			throws IOException, FileExistsException {
		byte[] bytes = file.getBytes();

		File userDir = new File(UPLOAD_DIR, fileDir + ownerId);
		userDir.mkdirs();

		File fileToSave = new File(userDir, file.getOriginalFilename());

		if (fileToSave.exists()) {
			throw new FileExistsException("File with name " + file.getOriginalFilename() + " exists!");
		}

		Path path = Paths.get(fileToSave.getPath());
		Files.write(path, bytes);

		return path.toString();
	}

	private void checkFileFormat(MultipartFile file, String formats, String inputType)
			throws UnsupportedFileFormatException {
		String type = file.getContentType().toLowerCase();
		String[] supportedFormats = formats.split(" ");

		for (int format = 0; format < supportedFormats.length; format++) {
			if (type.contains(supportedFormats[format].toLowerCase())) {
				return;
			}
		}

		throw new UnsupportedFileFormatException(inputType + " not supported");
	}

	public BigVideoDTO getBigVideoDTOById(Long id) throws VideoNotFoundException {
		Video video = null;
		try {
			video = this.videosRepository.findById(id).get();
		} catch (NoSuchElementException e) {
			throw new VideoNotFoundException();
		}
		video.setNumberOfViews(video.getNumberOfViews() + 1);
		this.videosRepository.save(video);
		
		BigVideoDTO bigVideoDTO = this.convertFromVideoToBigVideoDTO(video);
		
		return bigVideoDTO;
	}
	
	public BigVideoDTO getBigVideoDTOById(Long id, Long userId) throws VideoNotFoundException {
		BigVideoDTO bigVideoDTO = this.getBigVideoDTOById(id);
		User user = this.usersRepository.findById(userId).get();
		
		Video video = this.videosRepository.findById(id).get();
		
		if(!user.getWatchedVideos().contains(video)) {
			user.getWatchedVideos().add(video);
			this.usersRepository.save(user);
		} else {
			video.setNumberOfViews(video.getNumberOfViews() - 1);
			this.videosRepository.save(video);
		}
		
		return bigVideoDTO;
	}
	
	public SmallVideoDTO convertFromVideoToSmallVideoDTO(Video parent) {
		SmallVideoDTO video = this.modelMapper.map(parent, SmallVideoDTO.class);

		video.setChannelName(parent.getOwner().getName());
		
		return video;
	}
	
	public VideoDTO convertFromVideoToVideoDTO(Video parent) {
		VideoDTO video = this.modelMapper.map(parent, VideoDTO.class);
		
		video.setChannelName(parent.getOwner().getName());
		video.setChannelId(parent.getOwner().getId());
		
		return video;
	}
	
	public BigVideoDTO convertFromVideoToBigVideoDTO(Video parent) {
		BigVideoDTO video = this.modelMapper.map(parent, BigVideoDTO.class);
		
		video.setChannelName(parent.getOwner().getName());
		video.setChannelId(parent.getOwner().getId());
		if(parent.getUsersWhoLikeThisVideo() != null)
			video.setLikes(parent.getUsersWhoLikeThisVideo().size());
		if(parent.getUsersWhoDisLikeThisVideo() != null)
			video.setDislikes(parent.getUsersWhoDisLikeThisVideo().size());
		video.setComments(this.convertFromCommentsToCommentsDTO(parent));
		
		return video;
	}

	private List<CommentDTO> convertFromCommentsToCommentsDTO(Video parent) {
		if(parent == null) {
			return new LinkedList<CommentDTO>();
		}
		List<CommentDTO> comments = new LinkedList<CommentDTO>();
		parent.getComments()
			.stream()
			.forEach(comment -> {
				CommentDTO tempComment = this.modelMapper.map(comment, CommentDTO.class);
				tempComment.setUserNickName(comment.getAuthor().getNickName());
			
				List<CommentDTO> tempSubComments = new LinkedList<CommentDTO>();
				this.commentsRepository.findAllBySuperCommentId(comment.getId())
					.stream()
					.forEach(tComment -> {
						CommentDTO subComment = this.modelMapper.map(tComment, CommentDTO.class);
						subComment.setUserNickName(tComment.getAuthor().getNickName());
						subComment.setSubComments(new LinkedList<CommentDTO>());
						tempSubComments.add(subComment);
					});
				tempComment.setSubComments(tempSubComments);
				if(comment.getSuperComment() != null) {
					tempComment.setSuperCommentId(comment.getSuperComment().getId());
				}
				comments.add(tempComment);
			});
		
		return comments;
	}

	public List<Video> findAllBySearchString(String search) {
		
		String searchAsRegex = this.convertSearchToRegex(search);
		
		return this.videosRepository.findByTitle(searchAsRegex);
	}
	
	public String convertSearchToRegex(String searchInput) {
		String regexStart = "(?=.*";
		String regexEnd = ")";
		
		return regexStart + searchInput.replaceAll(" ", regexEnd + regexStart) + regexEnd;	
	}

	@Transactional
	public void deleteVideo(Long videoId) {
		Video video = this.videosRepository.findById(videoId).get();
		Channel channel = this.channelsRepository.findById(video.getOwner().getId()).get();
		List<User> usersWhoWatchedVideo = this.usersRepository.findAllBywatchedVideos(video);
		List<User> usersToWatchLater = this.usersRepository.findAllByVideosForLater(video);
		List<User> usersWhoLikeVideo = this.usersRepository.findAllByLikedVideos(video);
		
		usersWhoWatchedVideo.stream().forEach(user -> {	
			user.getWatchedVideos().remove(video);
			this.usersRepository.save(user);
		});
		usersToWatchLater.stream().forEach(user -> {
			user.getVideosForLater().remove(video);
			this.usersRepository.save(user);
		});
		usersWhoLikeVideo.stream().forEach(user -> {
			user.getLikedVideos().remove(video);
			this.usersRepository.save(user);
		});
		
		this.deleteVideosFromDisk(videoId);
		
		this.videosRepository.delete(video);
		
		channel.getOwnedVideos().remove(video);
		this.channelsRepository.save(channel);
	}
	
	public void deleteVideosFromDisk(Long videoId) {
		Video video = this.videosRepository.findById(videoId).get();
		File videoFile = new File(video.getUrl());
		File thumbnailFile = new File(video.getThumbnail());
		
		videoFile.delete();
		thumbnailFile.delete();
	}
	

}

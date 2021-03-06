package com.cpg.pixogramspring.services.Impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cpg.pixogramspring.constants.UserConstants;
import com.cpg.pixogramspring.exceptions.NotFoundException;
import com.cpg.pixogramspring.models.Comment;
import com.cpg.pixogramspring.models.Content;
import com.cpg.pixogramspring.models.User;
import com.cpg.pixogramspring.repositories.ContentRepository;
import com.cpg.pixogramspring.repositories.UserRepository;
import com.cpg.pixogramspring.services.ContentService;


@Service
public class ContentServiceImpl implements ContentService {

	@Autowired
	private ContentRepository contentRepository;
	@Autowired
	private UserRepository userRepository;
//	//Path path= ;
//	private final Path fileStorageLocation = null;

	/**
	 * Uploading images/videos to your account
	 */
	@Override
	public Content uploadFile(MultipartFile file, String caption, int user_id)
			throws IllegalStateException, IOException {
		long append = System.nanoTime();
		String filename = append + "_" + file.getOriginalFilename();
		System.out.println(filename);
		System.out.println(file.getSize());
		String filetype = file.getContentType();
		Optional<User> existingUser = userRepository.findByUserId(user_id);
		Path targetLocation = Paths.get("src/main/resources/static/uploads/"+filename);
		Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);		
		//file.transferTo(new File("E:\\Sprint\\uploads\\" + file.getOriginalFilename()));
		Content content = new Content(caption, filename, filetype);
//		content.setUser(user.get());
		if (existingUser.isPresent()) {
			content.setUser(existingUser.get());
			return contentRepository.save(content);
		} else {
			throw new NotFoundException(UserConstants.userNotExists);
		}
	}
	
//	public String storeFile(MultipartFile file) throws IOException{
//	
//	 String fileName = StringUtils.cleanPath(file.getOriginalFilename());
//	 try {
//		 if(fileName.contains("..")) {
//                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
//                }
//		 String newFileName = System.currentTimeMillis() + "_" + fileName;
//		 Path targetLocation = ((Path) this.fileStorageLocation).resolve(newFileName);
//         Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
//         Content c= new Content(fileName, file.getContentType());
//         contentRepository.save(c);
//         return newFileName;
//	 }
//	 catch(IOException ex) {
//		 throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
//	 }
//}

	/**
	 * Finding a particular image/video
	 */
	@Override
	public Content findContent(int content_id) {
		Optional<Content> content = contentRepository.findById(content_id);
		if (content.isPresent()) {
			return content.get();
		} else {
			throw new NotFoundException(UserConstants.contentNotExists);
		}
	}

	/**
	 * Getting all contents
	 */
	@Override
	public List<Content> allContent() {
		return contentRepository.findAll();
	}

	/**
	 * Deleting a particular content
	 */
	@Override
	public void deleteContent(int content_id) {
		Optional<Content> content = contentRepository.findById(content_id);
		if (!content.isPresent()) {
			throw new NotFoundException(UserConstants.contentNotExists);
		} else {
			contentRepository.deleteById(content_id);
		}
	}

	/**
	 * Adding comments to uploaded content
	 */
	@Override
	public Content addComment(int user_id, int content_id, String comment) {
		Optional<User> existingUser = userRepository.findByUserId(user_id);
		String email = existingUser.get().getEmail();
		Content existingContent = contentRepository.findContentById(content_id);
		Comment message = new Comment(comment, email);
		List<Comment> comments = new ArrayList<>();
		if (existingUser.isPresent()) {
			if ((existingContent != null)) {
				comments = existingContent.getComment();
				comments.add(message);
				existingContent.setComment(comments);
				return contentRepository.save(existingContent);
			}
			throw new NotFoundException(UserConstants.contentNotExists);
		}
		throw new NotFoundException(UserConstants.userNotExists);
	}

	/**
	 * Adding likes to uploaded content
	 */
	@Override
	public Content addLikes(int content_id, int user_id) {
		Content existingContent = contentRepository.findContentById(content_id);
		Optional<User> existingUser = userRepository.findById(user_id);
		int like = existingContent.getLike();
		if (existingUser.isPresent()) {
			if (existingContent != null) {
				++like;
				existingContent.setLike(like);
//				Long count=contentRepository.countLikes(content_id);
//				System.out.println(count);
				return contentRepository.save(existingContent);
			}
			throw new NotFoundException(UserConstants.contentNotExists);
		}
		throw new NotFoundException(UserConstants.userNotExists);
	}

	/**
	 * Adding dislikes to uploaded content
	 */
	@Override
	public Content addDislikes(int content_id, int user_id) {
		Content existingContent = contentRepository.findContentById(content_id);
		Optional<User> existingUser = userRepository.findById(user_id);
		int dislike = existingContent.getDislike();
		if (existingUser.isPresent()) {
			if (existingContent != null) {
				++dislike;
				existingContent.setDislike(dislike);
				return contentRepository.save(existingContent);
			}
			throw new NotFoundException(UserConstants.contentNotExists);
		}
		throw new NotFoundException(UserConstants.userNotExists);
	}

}

package com.baeldung.mongodb.file.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.baeldung.mongodb.file.models.Photo;
import com.baeldung.mongodb.file.models.Video;
import com.baeldung.mongodb.file.services.PhotoService;
import com.baeldung.mongodb.file.services.VideoService;
import com.baeldung.mongodb.file.web.dtos.PhotoWeb;

@Controller
public class IndexController {

	@Autowired
	private VideoService videoService;

	@Autowired
	private PhotoService photoService;

	@GetMapping("/")
	public String getIndex(Model model) throws IllegalStateException, IOException {

		List<Video> videos = videoService.getVideos();
		model.addAttribute("videosweb", videos);

		List<Photo> photos = photoService.getPhotos();
		List<PhotoWeb> photosweb = new ArrayList<PhotoWeb>();
		for (Photo photo : photos) {
			PhotoWeb photoWeb = new PhotoWeb();
			photoWeb.setId(photo.getId());
			photoWeb.setTitle(photo.getTitle());
			photoWeb.setImage(Base64.getEncoder().encodeToString(photo.getImage().getData()));
			photosweb.add(photoWeb);
		}
		model.addAttribute("photosweb", photosweb);
		return "index";
	}

}

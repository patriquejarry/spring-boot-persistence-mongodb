package com.baeldung.mongodb.file.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.baeldung.mongodb.file.models.Video;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.gridfs.GridFSFindIterable;
import com.mongodb.client.gridfs.model.GridFSFile;

@Service
public class VideoService {

	@Autowired
	private GridFsTemplate gridFsTemplate;

	@Autowired
	private GridFsOperations operations;

	public List<Video> getVideos() throws IllegalStateException, IOException {
		List<Video> videos = new ArrayList<>();
		GridFSFindIterable files = gridFsTemplate.find(new Query(Criteria.where("_id").exists(true)));
		for (GridFSFile file : files) {
			videos.add(getVideo(file.getObjectId().toString()));
		}
		return videos;
	}

	public Video getVideo(String id) throws IllegalStateException, IOException {
		GridFSFile file = gridFsTemplate.findOne(new Query(Criteria.where("_id").is(id)));
		Video video = new Video();
		video.setId(file.getObjectId().toString());
		video.setTitle(file.getMetadata().get("title").toString());
		video.setStream(operations.getResource(file).getInputStream());
		return video;
	}

	public String addVideo(String title, MultipartFile file) throws IOException {
		DBObject metaData = new BasicDBObject();
		metaData.put("type", "video");
		metaData.put("title", title);
		ObjectId id = gridFsTemplate.store(file.getInputStream(), file.getName(), file.getContentType(), metaData);
		return id.toString();
	}

}

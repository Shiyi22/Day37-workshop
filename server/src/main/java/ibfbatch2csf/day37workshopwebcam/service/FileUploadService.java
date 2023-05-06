package ibfbatch2csf.day37workshopwebcam.service;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import ibfbatch2csf.day37workshopwebcam.model.Post;
import ibfbatch2csf.day37workshopwebcam.repository.FileUploadRepository;

@Service
public class FileUploadService {

    @Autowired
    private FileUploadRepository fileUploadRepo; 

    public void upload(MultipartFile file, String title, String complain) throws SQLException, IOException {
        this.fileUploadRepo.upload(file, title, complain);
    }
    
    public Optional<Post> getPostById(Integer postId) {
        return this.fileUploadRepo.getPostById(postId); 
    }

    public Integer getStoreCount() {
        return this.fileUploadRepo.getStoreCount(); 
    }
}

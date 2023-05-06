package ibfbatch2csf.day37workshopwebcam.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Base64;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import ibfbatch2csf.day37workshopwebcam.model.Post;
import ibfbatch2csf.day37workshopwebcam.service.FileUploadService;
import ibfbatch2csf.day37workshopwebcam.service.S3Service;
import jakarta.json.Json;
import jakarta.json.JsonObject;

@Controller
public class FileUploadController {

    // PREFIX to read the encoded String 
    private static final String BASE64_PREFIX = "data:image/png;base64,";

    @Autowired
    private S3Service s3Svc; 
    
    @Autowired
    private FileUploadService fileUploadSvc;

    @PostMapping(path="/upload", consumes=MediaType.MULTIPART_FORM_DATA_VALUE, produces=MediaType.APPLICATION_JSON_VALUE) 
    public ResponseEntity<String> upload(@RequestPart MultipartFile file, @RequestPart String title, @RequestPart String complain) {
        
        String key = ""; 
        try {
            key = s3Svc.upload(file);  // to digital ocean 
            this.fileUploadSvc.upload(file, title, complain); // to SQL 
        } catch (IOException | SQLException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage()); 
        }
        JsonObject payload = Json.createObjectBuilder().add("imageKey", key).build(); 
        return ResponseEntity.ok(payload.toString());  
    }

    // this id is the auto-increment id in SQL 
    @GetMapping(path="/get-image/{postId}") 
    public ResponseEntity<String> retrieveImage(@PathVariable Integer postId) {

        Optional<Post> opt = this.fileUploadSvc.getPostById(postId); 
        Post post = opt.get();
        String encodedString = Base64.getEncoder().encodeToString(post.getImage()); 

        // prefix used to tell html that this item is a png -> for decoding 
        JsonObject payload = Json.createObjectBuilder().add("image", BASE64_PREFIX + encodedString).build(); 

        return ResponseEntity.ok().body(payload.toString()); 
    }

    @GetMapping(path="/get-image")
    public ResponseEntity<String> getStoreCount() {
        Integer count = this.fileUploadSvc.getStoreCount();
        
        return ResponseEntity.ok().body(count.toString()); 
    }    
}

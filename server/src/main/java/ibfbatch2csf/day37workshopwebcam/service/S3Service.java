package ibfbatch2csf.day37workshopwebcam.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.UUID;

import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

@Service
public class S3Service {

    @Autowired
    private AmazonS3 s3Client; 

    // @Value("${DO_STORAGE_BUCKETNAME}")
    @Value("do.storage.bucketname")
    private String bucketName; 

    public String upload(MultipartFile file) throws IOException {

        Map<String, String> userData = new HashMap<>(); 
        userData.put("name", "Kenneth"); 
        userData.put("uploadDateTime", LocalDateTime.now().toString());
        userData.put("originalFilename", file.getOriginalFilename());  
        // KENNETH MADE SOME CHANGES HERE 

        // create objectMetadata for put request 
        ObjectMetadata metadata = new ObjectMetadata(); 
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());
        metadata.setUserMetadata(userData); // takes in HashMap / Map 

        // randomly generate key
        String key = UUID.randomUUID().toString().substring(0, 8); 
        StringTokenizer tk = new StringTokenizer(file.getOriginalFilename(), "."); 
        int count = 0;
        String filenameExt = ""; 

        while (tk.hasMoreTokens()) {
            if (count == 1) {
                filenameExt = tk.nextToken(); 
                break; 
            } else { // should increment 
                filenameExt = tk.nextToken(); 
                count++; 
            }
        }
        // image captured is in .blob when it is sent via httpclient -> convert to png 
        if (filenameExt.equals("blob"))
            filenameExt = filenameExt + ".png"; // force upload to be png @ angular side 
        
        PutObjectRequest putRequest = new PutObjectRequest(bucketName, "myobject%s.%s".formatted(key, filenameExt), 
            file.getInputStream(), metadata); 
        putRequest = putRequest.withCannedAcl(CannedAccessControlList.PublicRead);

        s3Client.putObject(putRequest); 
        return "myobject%s.%s".formatted(key, filenameExt); 
    }
    
}

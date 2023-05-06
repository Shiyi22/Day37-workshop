package ibfbatch2csf.day37workshopwebcam.repository;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import ibfbatch2csf.day37workshopwebcam.model.Post;

@Repository
public class FileUploadRepository {

    private static final String INSERT_POST_SQL = "INSERT INTO posts(blobc, title, complain) VALUES (?, ?, ?)";
    private static final String GET_POST_BY_POSTID_SQL = "select id, title, complain, blobc from posts where id = ?";
    private static final String GET_COUNT_SQL = "SELECT COUNT(*) FROM posts"; 
    
    @Autowired
    private DataSource dataSource; 

    @Autowired
    private JdbcTemplate template; 

    // get num of entries (images) stored in sql
    public Integer getStoreCount() {
        return template.queryForObject(GET_COUNT_SQL, Integer.class); 
    }

    // function to upload image 
    public void upload(MultipartFile file, String title, String complain) throws SQLException, IOException {
        template.update(INSERT_POST_SQL, new PreparedStatementSetter() {

            InputStream is = file.getInputStream(); 
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setBinaryStream(1, is, file.getSize());
                ps.setString(2, title);
                ps.setString(3, complain);
                ps.execute();
            }
        }); 

        // Alternative way of using preparedstatement 
        // try (Connection con = dataSource.getConnection(); 
        //     PreparedStatement ps = con.prepareStatement(INSERT_POST_SQL)) {
        //     InputStream is = file.getInputStream(); 
        //     ps.setBinaryStream(1, is, file.getSize());
        //     ps.setString(2, title);
        //     ps.setString(3, complain);
        //     ps.executeUpdate();
        // } 
    }

    // function to retrieve image
    public Optional<Post> getPostById(Integer postId) {

        return template.query(GET_POST_BY_POSTID_SQL, 
            (ResultSet rs) -> {
                if (!rs.next())
                    return Optional.empty();
                final Post post = Post.populate(rs); 
                return Optional.of(post); 
            }, postId); 
    }

        // Post post = template.query(GET_POST_BY_POSTID_SQL, new ResultSetExtractor<Post>() {
        //     @Override
        //     public Post extractData(ResultSet rs) throws SQLException, DataAccessException {
        //         Post post = new Post(); 
        //         while (rs.next()) {
        //             post = Post.populate(rs); 
        //         }
        //         return post;
        //     }
        // }, postId); 
        // if (post == null)
        //     return Optional.empty(); 
        // return Optional.of(post); 
    
}
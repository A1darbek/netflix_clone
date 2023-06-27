package com.ayderbek.netflxSpring.netflix_clone.domain;

import com.ayderbek.netflxSpring.netflix_clone.repos.CategoryRepository;
import com.ayderbek.netflxSpring.netflix_clone.repos.DirectorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.util.List;

@RequiredArgsConstructor
public class VideoRowMapper  implements RowMapper<Video> {
    private final CategoryRepository categoryRepository;

    private final DirectorRepository directorRepository;

    private final JdbcTemplate jdbcTemplate;
    @Override
    public Video mapRow(ResultSet rs, int rowNum) throws SQLException {
        Video video = new Video();
        video.setId(rs.getLong("id"));
        video.setBucketName(rs.getString("bucket_name"));
        video.setDescription(rs.getString("description"));
        video.setDuration(Duration.ofSeconds(rs.getLong("duration")));
        video.setKey(rs.getString("key"));
        video.setReleaseDate(rs.getDate("release_date").toLocalDate());
        video.setTitle(rs.getString("title"));

        Long categoryId = rs.getLong("category_id");
        Category category = categoryRepository.findById(categoryId).orElse(null);
        video.setCategory(category);

        Long directorId = rs.getLong("director_id");
        Director director = directorRepository.findById(directorId).orElse(null);
        video.setDirector(director);

        // Fetch actors from separate table using video's id
        List<Actor> actors = jdbcTemplate.query(
                "SELECT actor.* FROM actor JOIN video_actor ON actor.id = video_actor.actor_id WHERE video_actor.video_id = ?",
                (rs1, rowNum1) -> {
                    Actor actor = new Actor();
                    actor.setId(rs1.getLong("id"));
                    actor.setName(rs1.getString("name"));
                    return actor;
                },
                video.getId()
        );
        video.setActors(actors);
        return video;
    }
}

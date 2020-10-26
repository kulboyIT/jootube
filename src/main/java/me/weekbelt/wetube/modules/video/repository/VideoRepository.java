package me.weekbelt.wetube.modules.video.repository;

import me.weekbelt.wetube.modules.video.Video;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VideoRepository extends JpaRepository<Video, Long>, VideoRepositoryCustom {
     List<Video> findAllByOrderByIdDesc();
}

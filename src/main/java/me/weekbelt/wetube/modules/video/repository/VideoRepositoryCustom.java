package me.weekbelt.wetube.modules.video.repository;

import me.weekbelt.wetube.modules.video.Video;

import java.util.List;
import java.util.Optional;

public interface VideoRepositoryCustom {
    List<Video> findAllByVideoKeyword(String keyword);

    Optional<Video> findWithMemberById(Long id);
}

package me.weekbelt.wetube.modules.video.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import me.weekbelt.wetube.modules.video.Video;

import java.util.List;
import java.util.Optional;

import static me.weekbelt.wetube.modules.member.QMember.member;
import static me.weekbelt.wetube.modules.video.QVideo.*;

@RequiredArgsConstructor
public class VideoRepositoryCustomImpl implements VideoRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Video> findAllByVideoKeyword(String keyword) {
        QueryResults<Video> videoQueryResults = queryFactory
                .selectFrom(video)
                .where(containKeyword(keyword))
                .orderBy(video.createdDate.desc())
                .fetchResults();

        return videoQueryResults.getResults();
    }

    private BooleanExpression containKeyword(String keywordCond) {
        return video.title.containsIgnoreCase(keywordCond);
    }

    @Override
    public Optional<Video> findWithMemberById(Long id) {
        return Optional.ofNullable( queryFactory
                .selectFrom(video)
                .join(video.member, member).fetchJoin()
                .where(video.id.eq(id))
                .fetchOne());
    }
}

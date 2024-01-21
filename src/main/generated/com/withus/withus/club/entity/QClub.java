package com.withus.withus.club.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QClub is a Querydsl query type for Club
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QClub extends EntityPathBase<Club> {

    private static final long serialVersionUID = -1172762218L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QClub club = new QClub("club");

    public final com.withus.withus.global.timestamp.QTimeStamp _super = new com.withus.withus.global.timestamp.QTimeStamp(this);

    public final EnumPath<com.withus.withus.category.entity.ClubCategory> category = createEnum("category", com.withus.withus.category.entity.ClubCategory.class);

    public final ListPath<ClubMember, QClubMember> clubMemberList = this.<ClubMember, QClubMember>createList("clubMemberList", ClubMember.class, QClubMember.class, PathInits.DIRECT2);

    public final StringPath clubTitle = createString("clubTitle");

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final DateTimePath<java.time.LocalDateTime> endTime = createDateTime("endTime", java.time.LocalDateTime.class);

    public final StringPath filename = createString("filename");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath imageURL = createString("imageURL");

    public final BooleanPath isActive = createBoolean("isActive");

    public final NumberPath<Integer> MaxMember = createNumber("MaxMember", Integer.class);

    public final com.withus.withus.member.entity.QMember member;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final ListPath<com.withus.withus.notice.entity.Notice, com.withus.withus.notice.entity.QNotice> noticeList = this.<com.withus.withus.notice.entity.Notice, com.withus.withus.notice.entity.QNotice>createList("noticeList", com.withus.withus.notice.entity.Notice.class, com.withus.withus.notice.entity.QNotice.class, PathInits.DIRECT2);

    public final DateTimePath<java.time.LocalDateTime> startTime = createDateTime("startTime", java.time.LocalDateTime.class);

    public final StringPath username = createString("username");

    public QClub(String variable) {
        this(Club.class, forVariable(variable), INITS);
    }

    public QClub(Path<? extends Club> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QClub(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QClub(PathMetadata metadata, PathInits inits) {
        this(Club.class, metadata, inits);
    }

    public QClub(Class<? extends Club> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new com.withus.withus.member.entity.QMember(forProperty("member")) : null;
    }

}


package com.withus.withus.domain.club.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QReportClub is a Querydsl query type for ReportClub
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReportClub extends EntityPathBase<ReportClub> {

    private static final long serialVersionUID = -1853559142L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QReportClub reportClub = new QReportClub("reportClub");

    public final com.withus.withus.global.timestamp.QTimeStamp _super = new com.withus.withus.global.timestamp.QTimeStamp(this);

    public final QClub club;

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.withus.withus.domain.member.entity.QMember member;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public QReportClub(String variable) {
        this(ReportClub.class, forVariable(variable), INITS);
    }

    public QReportClub(Path<? extends ReportClub> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QReportClub(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QReportClub(PathMetadata metadata, PathInits inits) {
        this(ReportClub.class, metadata, inits);
    }

    public QReportClub(Class<? extends ReportClub> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.club = inits.isInitialized("club") ? new QClub(forProperty("club"), inits.get("club")) : null;
        this.member = inits.isInitialized("member") ? new com.withus.withus.domain.member.entity.QMember(forProperty("member")) : null;
    }

}


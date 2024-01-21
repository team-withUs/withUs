package com.withus.withus.notice.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QReportNotice is a Querydsl query type for ReportNotice
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReportNotice extends EntityPathBase<ReportNotice> {

    private static final long serialVersionUID = 2134309870L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QReportNotice reportNotice = new QReportNotice("reportNotice");

    public final StringPath content = createString("content");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.withus.withus.member.entity.QMember member;

    public final QNotice notice;

    public QReportNotice(String variable) {
        this(ReportNotice.class, forVariable(variable), INITS);
    }

    public QReportNotice(Path<? extends ReportNotice> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QReportNotice(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QReportNotice(PathMetadata metadata, PathInits inits) {
        this(ReportNotice.class, metadata, inits);
    }

    public QReportNotice(Class<? extends ReportNotice> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new com.withus.withus.member.entity.QMember(forProperty("member")) : null;
        this.notice = inits.isInitialized("notice") ? new QNotice(forProperty("notice"), inits.get("notice")) : null;
    }

}


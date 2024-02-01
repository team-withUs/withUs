package com.withus.withus.domain.member.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QReportMember is a Querydsl query type for ReportMember
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReportMember extends EntityPathBase<ReportMember> {

    private static final long serialVersionUID = -430044126L;

    public static final QReportMember reportMember = new QReportMember("reportMember");

    public final StringPath content = createString("content");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> reportedId = createNumber("reportedId", Long.class);

    public final NumberPath<Long> reporterId = createNumber("reporterId", Long.class);

    public QReportMember(String variable) {
        super(ReportMember.class, forVariable(variable));
    }

    public QReportMember(Path<? extends ReportMember> path) {
        super(path.getType(), path.getMetadata());
    }

    public QReportMember(PathMetadata metadata) {
        super(ReportMember.class, metadata);
    }

}


package com.withus.withus.global.timestamp;


import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

// 엔티티의 변화를 감지하고 매핑된 테이블 데이터 조작?
@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class TimeStamp {

    // @CreatedDate => 엔티티가 생성되어 저장될 때 시간이 자동 저장
    // "updatable = false" => 생성일자가 수정되지 않게 막음
    // TempralType.xxxxx 생성일자를 날짜타입으로 매핑할 때 사용?, TIMESTAMP = 날짜와 시간 데이터베이스와 매핑(예: 2024-01-09 12:00:00)
    @CreatedDate
    @Column(updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt;

    @LastModifiedDate // 엔티티가 마지막으로 수정(변경)될 때 시간이 자동 저장
    @Column // 수정은 막으면 안되니 false 안쓴듯?
    @Temporal(TemporalType.TIMESTAMP) // 수정일자 날짜타입 매핑
    private LocalDateTime modifiedAt;

}
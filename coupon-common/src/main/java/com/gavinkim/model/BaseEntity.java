package com.gavinkim.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@Accessors(chain = true)
@MappedSuperclass
@JsonIgnoreProperties(value = {"createdAt","updatedAt"}, allowGetters = true)
@EntityListeners({AuditingEntityListener.class})
public abstract class BaseEntity implements Serializable {

    @CreationTimestamp
    @Column(nullable = false,name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

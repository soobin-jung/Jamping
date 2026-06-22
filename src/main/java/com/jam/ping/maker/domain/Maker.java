package com.jam.ping.maker.domain;

import com.jam.ping.global.domain.CommonEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Maker extends CommonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 100)
    private String nameEng;

    @Column(length = 500)
    private String homepageUrl;

    @Builder
    private Maker(String name, String nameEng, String homepageUrl) {
        this.name = name;
        this.nameEng = nameEng;
        this.homepageUrl = homepageUrl;
    }

    public void update(String name, String nameEng, String homepageUrl) {
        this.name = name;
        this.nameEng = nameEng;
        this.homepageUrl = homepageUrl;
    }
}

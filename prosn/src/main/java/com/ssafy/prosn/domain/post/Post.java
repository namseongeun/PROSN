package com.ssafy.prosn.domain.post;

import com.ssafy.prosn.domain.BaseEntity;
import com.ssafy.prosn.domain.user.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

/**
 * created by seongmin on 2022/07/19
 * updated by seongmin on 2022/07/19
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public abstract class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private Integer views;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    @Column(columnDefinition = "Integer default 0")
    private Integer numOfLikes;

    @Column(columnDefinition = "Integer default 0")
    private Integer numOfDislikes;

    public Post(String title, Integer views, User user) {
        this.title = title;
        this.views = views;
        this.user = user;
    }
}

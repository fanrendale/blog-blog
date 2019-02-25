package com.xjf.springboot.domain;

import com.github.rjeschke.txtmark.Processor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * BLog 实体
 *
 * @author xjf
 * @date 2019/2/25 19:05
 */
@Entity
public class Blog implements Serializable {

    private static final long serialVersionUID = -8643118283263467925L;

    /**
     * 主键ID
     * 自增长策略
     * 用户的唯一标识
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 标题
     * 映射为字段，值不能为空
     */
    @NotEmpty(message = "标题不能为空")
    @Size(min = 2,max = 50)
    @Column(nullable = false,length = 50)
    private String title;

    /**
     * 摘要
     */
    @NotEmpty(message = "摘要不能为空")
    @Size(min = 2,max = 300)
    @Column(nullable = false)
    private String summary;

    /**
     * 内容
     */
    @Lob
    @Basic(fetch = FetchType.LAZY)
    @NotEmpty(message = "内容不能为空")
    @Size(min = 2)
    @Column(nullable = false)
    private String content;

    /**
     * 将md转为html
     * 大对象，映射MySql的Long Text类型
     * 懒加载
     */
    @Lob
    @Basic(fetch = FetchType.LAZY)
    @NotEmpty(message = "内容不能为空")
    @Size(min = 2)
    @Column(nullable = false)
    private String htmlContent;

    /**
     * 博客作者
     */
    @OneToOne(cascade = CascadeType.DETACH,fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    /**
     * 创建时间
     * 有数据库自动创建时间
     */
    @Column(nullable = false)
    @CreationTimestamp
    private Timestamp createTime;

    /**
     * 访问量、阅读量
     */
    @Column(name = "reading")
    private Long reading = 0L;

    /**
     * 评论量
     */
    @Column(name = "comments")
    private Long comments = 0L;

    /**
     * 点赞量
     */
    @Column(name = "likes")
    private Long likes = 0L;

    /**
     * 保护权限的构造方法
     */
    protected Blog(){

    }

    public Blog(@NotEmpty(message = "标题不能为空") @Size(min = 2, max = 50) String title, @NotEmpty(message = "摘要不能为空") @Size(min = 2, max = 300) String summary, @NotEmpty(message = "内容不能为空") @Size(min = 2) String content) {
        this.title = title;
        this.summary = summary;
        this.content = content;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
        //将Markdown转为html
        this.htmlContent = Processor.process(content);
    }

    public String getHtmlContent() {
        return htmlContent;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public Long getReading() {
        return reading;
    }

    public void setReading(Long reading) {
        this.reading = reading;
    }

    public Long getComments() {
        return comments;
    }

    public void setComments(Long comments) {
        this.comments = comments;
    }

    public Long getLikes() {
        return likes;
    }

    public void setLikes(Long likes) {
        this.likes = likes;
    }
}

package com.xyn.appdetial.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/3/29 0029.
 */
public class WorksItem extends BaseData implements Serializable{

    private static final long serialVersionUID = 5579718754563193743L;
    public int id;
    public String name;
    public int likeCount;
    public int viewCount;
    public String createDate;
    public String thumbnailWebPath;
    public String portfolio_detail;
    public String tag;
    public boolean box;

    public String[] vids;

    public String[] getVids() {
        return vids;
    }

    public boolean isLikeAble;
    public boolean isCollectAble;
    public int commentCount;


    public Category category;
    public WorksMember member;

    public String follow_type;//0 无关系 1  已 关注我  2 我已关注 3.互相关注

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getThumbnailWebPath() {
        return thumbnailWebPath;
    }

    public void setThumbnailWebPath(String thumbnailWebPath) {
        this.thumbnailWebPath = thumbnailWebPath;
    }

    public String getPortfolio_detail() {
        return portfolio_detail;
    }

    public void setPortfolio_detail(String portfolio_detail) {
        this.portfolio_detail = portfolio_detail;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public boolean isBox() {
        return box;
    }

    public void setBox(boolean box) {
        this.box = box;
    }

    public void setVids(String[] vids) {
        this.vids = vids;
    }

    public boolean isLikeAble() {
        return isLikeAble;
    }

    public void setIsLikeAble(boolean isLikeAble) {
        this.isLikeAble = isLikeAble;
    }

    public boolean isCollectAble() {
        return isCollectAble;
    }

    public void setIsCollectAble(boolean isCollectAble) {
        this.isCollectAble = isCollectAble;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public WorksMember getMember() {
        return member;
    }

    public void setMember(WorksMember member) {
        this.member = member;
    }

    public String getFollow_type() {
        return follow_type;
    }

    public void setFollow_type(String follow_type) {
        this.follow_type = follow_type;
    }
}

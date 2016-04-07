package com.xyn.appdetial.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/4/7 0007.
 */
public class LibraryInfo extends BaseData implements Serializable{

    private int id;
    private String title;
    private String cover_path;
    private String student_count;
    private int duration;
    private String difficulty;
    private String video_prefix;
    private String payment_type;
    private String collect;

    @Override
    public String toString() {
        return "LibraryInfo{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", cover_path='" + cover_path + '\'' +
                ", student_count='" + student_count + '\'' +
                ", duration=" + duration +
                ", difficulty='" + difficulty + '\'' +
                ", video_prefix='" + video_prefix + '\'' +
                ", payment_type='" + payment_type + '\'' +
                ", collect='" + collect + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCover_path() {
        return cover_path;
    }

    public void setCover_path(String cover_path) {
        this.cover_path = cover_path;
    }

    public String getStudent_count() {
        return student_count;
    }

    public void setStudent_count(String student_count) {
        this.student_count = student_count;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getVideo_prefix() {
        return video_prefix;
    }

    public void setVideo_prefix(String video_prefix) {
        this.video_prefix = video_prefix;
    }

    public String getPayment_type() {
        return payment_type;
    }

    public void setPayment_type(String payment_type) {
        this.payment_type = payment_type;
    }

    public String getCollect() {
        return collect;
    }

    public void setCollect(String collect) {
        this.collect = collect;
    }
}

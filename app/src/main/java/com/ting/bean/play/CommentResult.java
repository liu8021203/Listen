package com.ting.bean.play;

import com.ting.bean.anchor.ListenBookVO;
import com.ting.bean.BaseResult;

import java.util.List;

/**
 * Created by gengjiajia on 15/11/20.
 * 获取评论结果集合
 */
public class CommentResult  extends BaseResult{
    private String[] playerBg;
    private boolean isFavorite;//请求用户是否添加到收藏夹
    private int id;//作品ID;
    private String thumb;//作品封面
    private String title;//作品标题
    private String author;//作者
    private String broadercaster;//主播
    private String category;//分类
    private String tips;//简单描述
    private String lastUpdate;//最新集数
    private String lastUpdateDate;//最新更新时间戳
    private int update_status;
    private int viewd;//人气
    private int price;
    private boolean buyed;
    private boolean reward;
    private List<ListenBookVO> tingshuka;
    //1关闭评论，0打开评论
    private int commentoff;

    public boolean isReward() {
        return reward;
    }

    public void setReward(boolean reward) {
        this.reward = reward;
    }

    private Comment comment;

    public String[] getPlayerBg() {
        return playerBg;
    }

    public void setPlayerBg(String[] playerBg) {
        this.playerBg = playerBg;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setIsFavorite(boolean isFavorite) {
        this.isFavorite = isFavorite;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getBroadercaster() {
        return broadercaster;
    }

    public void setBroadercaster(String broadercaster) {
        this.broadercaster = broadercaster;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(String lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public int getViewd() {
        return viewd;
    }

    public void setViewd(int viewd) {
        this.viewd = viewd;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public boolean isBuyed() {
        return buyed;
    }

    public void setBuyed(boolean buyed) {
        this.buyed = buyed;
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }


    public int getUpdate_status() {
        return update_status;
    }

    public void setUpdate_status(int update_status) {
        this.update_status = update_status;
    }

    public List<ListenBookVO> getTingshuka() {
        return tingshuka;
    }

    public void setTingshuka(List<ListenBookVO> tingshuka) {
        this.tingshuka = tingshuka;
    }

    public int getCommentoff() {
        return commentoff;
    }
}

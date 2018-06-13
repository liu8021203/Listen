package com.ting.bean.play;

import com.ting.bean.BaseResult;
import com.ting.bean.anchor.ListenBookVO;
import com.ting.bean.play.PlayDiverDate;

import java.util.List;

/**
 * Created by gengjiajia on 15/10/8.
 * 书籍播放结果集合
 */
public class PlayResult extends BaseResult {

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
    private int viewd;//人气
    private List<ListenBookVO> tingshuka;
    private PlayDiverDate data;//分集数组
//    private Comment comment;//用户评论
    private int price;
    private boolean buyed;
    private boolean reward;

    public boolean isBuyed() {
        return buyed;
    }

    public void setBuyed(boolean buyed) {
        this.buyed = buyed;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

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

    public PlayDiverDate getData() {
        return data;
    }

    public void setData(PlayDiverDate data) {
        this.data = data;
    }

    public boolean isReward() {
        return reward;
    }

    public void setReward(boolean reward) {
        this.reward = reward;
    }

    public List<ListenBookVO> getTingshuka() {
        return tingshuka;
    }

    public void setTingshuka(List<ListenBookVO> tingshuka) {
        this.tingshuka = tingshuka;
    }
}


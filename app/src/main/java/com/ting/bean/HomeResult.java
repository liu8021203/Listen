package com.ting.bean;

import com.ting.bean.home.BookListVO;
import com.ting.bean.home.HotAnchorVO;
import com.ting.bean.home.SpecialVO;
import com.ting.bean.home.SuperMarketVO;
import com.ting.bean.vo.BestVO;
import com.ting.bean.vo.CategoryVO;
import com.ting.bean.vo.HostVO;
import com.ting.bean.vo.LoopVO;
import com.ting.bean.vo.TeamVO;

import java.util.List;

/**
 * Created by liu on 2017/7/25.
 */

public class HomeResult {
    private List<LoopVO> loop;
    private List<HostVO> hostData;
    private BestVO best;
    private BestVO free;
    private List<TeamVO> team;

    public List<LoopVO> getLoop() {
        return loop;
    }

    public void setLoop(List<LoopVO> loop) {
        this.loop = loop;
    }

    public List<HostVO> getHostData() {
        return hostData;
    }

    public void setHostData(List<HostVO> hostData) {
        this.hostData = hostData;
    }

    public BestVO getBest() {
        return best;
    }

    public void setBest(BestVO best) {
        this.best = best;
    }

    public BestVO getFree() {
        return free;
    }

    public void setFree(BestVO free) {
        this.free = free;
    }

    public List<TeamVO> getTeam() {
        return team;
    }

    public void setTeam(List<TeamVO> team) {
        this.team = team;
    }
}

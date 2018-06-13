package com.ting.play.controller;

import com.ting.base.BaseApplication;
import com.ting.db.DBListenHistory;
import com.ting.db.DBListenHistoryDao;

import java.util.List;

/**
 * Created by liu on 15/11/30.
 */
public class MusicDBController {

    public MusicDBController()
    {
    }


    public void insert(DBListenHistory vo)
    {
        DBListenHistoryDao dao = BaseApplication.getInstance().getDaoSession().getDBListenHistoryDao();
        List<DBListenHistory> temps = (List<DBListenHistory>) dao.queryRaw("where BOOKID = ?", new String[]{vo.getBookid() + ""});
        if(temps != null && temps.size() > 0)
        {
            vo.setId(temps.get(0).getId());
            dao.update(vo);
        }
        else
        {
            dao.insert(vo);
        }
    }

    /**
     * 获取指定书籍的DB数据
     * @param bookid
     * @return
     */
    public DBListenHistory getBookIdData(String bookid)
    {
        DBListenHistoryDao dao = BaseApplication.getInstance().getDaoSession().getDBListenHistoryDao();
        List<DBListenHistory> temps = (List<DBListenHistory>) dao.queryRaw("where BOOKID = ?", new String[]{bookid});
        if(temps != null && temps.size() > 0){
            return temps.get(0);
        }else{
            return null;
        }
    }



    public List<DBListenHistory> getListenHistory()
    {
        List<DBListenHistory> data = BaseApplication.getInstance().getDaoSession().getDBListenHistoryDao().queryBuilder().orderDesc(DBListenHistoryDao.Properties.Date).list();
        return data;
    }

    public void delete()
    {
        DBListenHistoryDao dao = BaseApplication.getInstance().getDaoSession().getDBListenHistoryDao();
        dao.deleteAll();
    }
}

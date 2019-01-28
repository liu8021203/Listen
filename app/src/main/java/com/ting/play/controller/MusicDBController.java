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
        List<DBListenHistory> temps =  dao.queryRaw("where CHAPTER_ID = ?", new String[]{vo.getChapterId()});
        if(temps != null && !temps.isEmpty())
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
        List<DBListenHistory> temps = dao.queryRaw("where BOOK_ID = ? order by SYSTEM_TIME desc", new String[]{bookid});
        if(temps != null && temps.size() > 0){
            return temps.get(0);
        }else{
            return null;
        }
    }


    public DBListenHistory getBookIdAndPositionData(String bookId, String position){
        DBListenHistoryDao dao = BaseApplication.getInstance().getDaoSession().getDBListenHistoryDao();
        List<DBListenHistory> temps = dao.queryRaw("where BOOK_ID = ? and  POSITION = ?", new String[]{bookId, position});
        if(temps != null && !temps.isEmpty()){
            return temps.get(0);
        }else{
            return null;
        }
    }



    /**
     * 获取指定书籍的DB数据
     * @param bookid
     * @return
     */
    public List<DBListenHistory> getBookIdHistory(String bookid)
    {
        DBListenHistoryDao dao = BaseApplication.getInstance().getDaoSession().getDBListenHistoryDao();
        List<DBListenHistory> temps = dao.queryRaw("where BOOK_ID = ?", new String[]{bookid});
        return temps;
    }



    public List<DBListenHistory> getListenHistory()
    {
        List<DBListenHistory> data = BaseApplication.getInstance().getDaoSession().getDBListenHistoryDao().queryBuilder().orderDesc(DBListenHistoryDao.Properties.SystemTime).list();
        return data;
    }

    public void delete()
    {
        DBListenHistoryDao dao = BaseApplication.getInstance().getDaoSession().getDBListenHistoryDao();
        dao.deleteAll();
    }
}

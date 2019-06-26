package com.ting.download;


import com.ting.base.BaseApplication;
import com.ting.common.AppData;
import com.ting.db.DBBook;
import com.ting.db.DBBookDao;
import com.ting.db.DBChapter;
import com.ting.db.DBChapterDao;
import com.ting.db.DBListenHistory;
import com.ting.db.DBListenHistoryDao;
import com.ting.util.UtilFileManage;

import java.util.List;


/**
 * Created by liu on 15/7/28.
 */
public class DownloadController {


    public DownloadController()
    {
    }

    public List<DBChapter> getData()
    {
        DBChapterDao dao = BaseApplication.getInstance().getDaoSession().getDBChapterDao();
        List<DBChapter> data = dao.loadAll();
        return data;
    }

    public void insert(DBChapter result)
    {
        DBBookDao bookDao = BaseApplication.getInstance().getDaoSession().getDBBookDao();
        List<DBBook> dbBooks = bookDao.queryRaw("where BOOK_ID = ?", new String[]{String.valueOf(result.getBookId())});
        if(dbBooks == null || (dbBooks != null && dbBooks.size() == 0)){
            DBBook book = new DBBook();
            book.setBookId(String.valueOf(result.getBookId()));
            book.setBookName(result.getBookTitle());
            book.setBookHost(result.getBookHost());
            book.setBookUrl(result.getBookImage());
            bookDao.insert(book);
        }
        DBChapterDao chapterDao = BaseApplication.getInstance().getDaoSession().getDBChapterDao();
        List<DBChapter> data = chapterDao.queryRaw("where BOOK_ID = ? and CHAPTER_ID = ?", new String[]{String.valueOf(result.getBookId()), String.valueOf(result.getChapterId())});
        if(data != null && data.size() > 0)
        {
            result.setId(data.get(0).getId());
            chapterDao.update(result);
        }
        else {
            chapterDao.insert(result);
        }
    }

    //更新
    public void update(DBChapter result)
    {
        DBChapterDao dao = BaseApplication.getInstance().getDaoSession().getDBChapterDao();
        List<DBChapter> data = dao.queryRaw("where BOOK_ID = ? and CHAPTER_ID = ?", new String[]{String.valueOf(result.getBookId()), String.valueOf(result.getChapterId())});
        if(data != null && data.size() > 0)
        {
            result.setId(data.get(0).getId());
            dao.update(result);
        }
        else {
            dao.insert(result);
        }
    }

    public DBChapter query(String BOOK_ID, String CATE_ID)
    {
        DBChapterDao dao = BaseApplication.getInstance().getDaoSession().getDBChapterDao();
        List<DBChapter> data = dao.queryRaw("where BOOK_ID = ? and CHAPTER_ID = ?", new String[]{BOOK_ID, CATE_ID});
        if(data == null || data.size() == 0)
        {
            return null;
        }
        else
        {
            return data.get(0);
        }
    }

    public static DBChapter queryDBChapter(String bookId, String chapterId){
        DBChapterDao dao = BaseApplication.getInstance().getDaoSession().getDBChapterDao();
        List<DBChapter> data = dao.queryRaw("where BOOK_ID = ? AND CHAPTER_ID = ?", new String[]{bookId, chapterId});
        if(data != null && !data.isEmpty()){
            return data.get(0);
        }else{
            return null;
        }
    }


    public DBChapter queryByBookIdAndPosition(String bookId, String position)
    {
        DBChapterDao dao = BaseApplication.getInstance().getDaoSession().getDBChapterDao();
        List<DBChapter> data = dao.queryRaw("where BOOK_ID = ? and POSITION = ?", new String[]{bookId, position});
        if(data == null || data.size() == 0)
        {
            return null;
        }
        else
        {
            return data.get(0);
        }
    }

    public List<DBChapter> queryData(String BOOK_ID, String STATE)
    {
        DBChapterDao dao = BaseApplication.getInstance().getDaoSession().getDBChapterDao();
        List<DBChapter> data = dao.queryBuilder().orderAsc(DBChapterDao.Properties.Position).where(DBChapterDao.Properties.BookId.eq(BOOK_ID), DBChapterDao.Properties.State.eq(STATE)).list();
        return data;
    }

    public List<DBChapter> queryData(String BOOK_ID)
    {
        DBChapterDao dao = BaseApplication.getInstance().getDaoSession().getDBChapterDao();
        List<DBChapter> data = dao.queryRaw("where BOOK_ID = ?", new String[]{BOOK_ID});
        return data;
    }


//    public List<DBChapter> queryData(String state) {
//        DBChapterDao dao = BaseApplication.getInstance().getDaoSession().getDBChapterDao();
//        return dao.queryRaw("where state = ?", new String[]{state});
//    }


    public void delete(DBChapter vo)
    {
        DBChapterDao dao = BaseApplication.getInstance().getDaoSession().getDBChapterDao();
        dao.delete(vo);
    }

    /**
     * 删除书籍
     */
    public void deleteBook(String bookId)
    {
        UtilFileManage.deleteFolderFile(AppData.FILE_PATH + bookId);
        DBChapterDao dao = BaseApplication.getInstance().getDaoSession().getDBChapterDao();
        dao.queryBuilder().where(DBChapterDao.Properties.BookId.eq(bookId)).buildDelete().executeDeleteWithoutDetachingEntities();
    }

    public void deleteAll()
    {
        DBChapterDao dao = BaseApplication.getInstance().getDaoSession().getDBChapterDao();
        dao.deleteAll();
    }

    /**
     * 查询所有书籍信息
     * @return
     */
    public List<DBBook> queryBook(){
        DBBookDao dao = BaseApplication.getInstance().getDaoSession().getDBBookDao();
        return dao.loadAll();
    }


    public List<DBChapter> getDownloadBook(){
        DBChapterDao dao = BaseApplication.getInstance().getDaoSession().getDBChapterDao();
        return dao.queryRaw("where STATE = ? group by BOOK_ID", new String[]{"4"});
    }


    public static List<DBChapter> queryDataAsc(String BOOK_ID, String STATE)
    {
        DBChapterDao dao = BaseApplication.getInstance().getDaoSession().getDBChapterDao();
        List<DBChapter> data = dao.queryRaw("where BOOK_ID = ? and STATE = ? order by position asc", new String[]{BOOK_ID, STATE});
        return data;
    }

}

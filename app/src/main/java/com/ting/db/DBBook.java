package com.ting.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by liu on 2017/7/25.
 */
@Entity
public class DBBook {
    @Id
    private Long id;

    @NotNull
    private String bookId;
    private String bookName;
    private String bookHost;
    private String bookUrl;
    @Generated(hash = 1954967938)
    public DBBook(Long id, @NotNull String bookId, String bookName, String bookHost,
            String bookUrl) {
        this.id = id;
        this.bookId = bookId;
        this.bookName = bookName;
        this.bookHost = bookHost;
        this.bookUrl = bookUrl;
    }
    @Generated(hash = 173941970)
    public DBBook() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getBookId() {
        return this.bookId;
    }
    public void setBookId(String bookId) {
        this.bookId = bookId;
    }
    public String getBookName() {
        return this.bookName;
    }
    public void setBookName(String bookName) {
        this.bookName = bookName;
    }
    public String getBookHost() {
        return this.bookHost;
    }
    public void setBookHost(String bookHost) {
        this.bookHost = bookHost;
    }
    public String getBookUrl() {
        return this.bookUrl;
    }
    public void setBookUrl(String bookUrl) {
        this.bookUrl = bookUrl;
    }
    

}

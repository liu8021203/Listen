package com.ting.common.http;

import com.ting.bean.AdResult;
import com.ting.bean.AppSearchResult;
import com.ting.bean.AppWxPayResult;
import com.ting.bean.BookListResult;
import com.ting.bean.BookResult;
import com.ting.bean.ChapterResult;
import com.ting.bean.CommentListResult;
import com.ting.bean.ExpenseResult;
import com.ting.bean.HostDetailResult;
import com.ting.bean.MoneyResult;
import com.ting.bean.anchor.AnchorMessResult;
import com.ting.bean.anchor.AnchorResult;
import com.ting.bean.anchor.LiWuResult;
import com.ting.bean.BaseResult;
import com.ting.bean.HomeResult;
import com.ting.bean.apk.ApkResult;
import com.ting.bean.bookrack.RandomRackResult;
import com.ting.bean.home.BookCityResult;
import com.ting.bean.home.CategoryListResult;
import com.ting.bean.home.HomeHotAnchorResult;
import com.ting.bean.home.HomeSpecialResult;
import com.ting.bean.home.HotRecommendResult;
import com.ting.bean.myself.CheckMessageResult;
import com.ting.bean.myself.DouChildrenResult;
import com.ting.bean.myself.GetVIPTingdouResult;
import com.ting.bean.myself.MessageJavaResult;
import com.ting.bean.myself.MyCardResult;
import com.ting.bean.play.ReplyResult;
import com.ting.bean.home.FineRecommendResult;
import com.ting.bean.home.HotAnchorResult;
import com.ting.bean.classfi.ClassIntroduceResult;
import com.ting.bean.classfi.ClassMainResult;
import com.ting.bean.myself.MySeeResult;
import com.ting.bean.play.MessageResult;
import com.ting.bean.play.PayResult;
import com.ting.bean.play.CommentResult;
import com.ting.bean.play.PlayResult;
import com.ting.bean.myself.CollectResult;
import com.ting.bean.UserInfoResult;
import com.ting.bean.search.SearchHotResult;
import com.ting.bean.search.SearchResult;
import com.ting.bean.vo.BookVO;
import com.ting.bean.vo.CardListVO;
import com.ting.bean.vo.CategoryVO;
import com.ting.bean.vo.CommentListVO;
import com.ting.bean.vo.GiftVO;
import com.ting.bean.vo.HostVO;
import com.ting.bean.vo.HotSearchVO;
import com.ting.db.DBChapter;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import retrofit2.http.QueryMap;

/**
 * Created by liu on 2017/6/22.
 */

public interface HttpService {

    @POST("index.php?s=Home/App/getIndexScencs2")
    Observable<BookCityResult> getIndexScencs2();

    @POST("index.php?s=Home/App/getPlayer")
    Observable<CommentResult> getPlayer(@QueryMap Map<String, String> map);


    @GET("index.php?s=Home/App/getPlayer")
    Observable<PlayResult> getPlayerList(@QueryMap Map<String, String> map);

    @GET("index.php?s=Home/App/setPostComment")
    Observable<BaseResult> setPostComment(@QueryMap Map<String, String> map);
    //获取回复数据
    @GET("index.php?s=Home/App/getComment_replay")
    Observable<ReplyResult> getComment_replay(@QueryMap Map<String, String> map);
    //获取分类数据
    @GET("index.php?s=Home/App/getCategorys")
    Observable<ClassMainResult> getCategorys();

    //获取听书卡数据
    @GET("index.php?s=Home/App/mytingshuka")
    Observable<MyCardResult> getCard(@QueryMap Map<String, String> map);

    //我关注的主播
    @GET("index.php?s=Home/App/getMyFriend")
    Observable<MySeeResult> getMyFriend(@QueryMap Map<String, String> map);

    @GET("index.php?s=Home/App/setFocus")
    Observable<BaseResult> setFocus(@QueryMap Map<String, String> map);

    /**
     * 获取分类数据
     * @param map
     * @return
     */
    @GET("index.php?s=Home/App/getCategoryData")
    Observable<ClassIntroduceResult> getCategoryData(@QueryMap Map<String, String> map);

    /**
     * 找回密码获取验证码
     * @param cellphoneNumber
     * @return
     */
    @FormUrlEncoded
    @POST("index.php?s=Home/App/getCellPhoneValidationForget")
    Observable<BaseResult> getCellPhoneValidationForget(@Field("cellphoneNumber") String cellphoneNumber);

    @FormUrlEncoded
    @POST("index.php?s=Home/App/setNewPasswrod")
    Observable<BaseResult> setNewPasswrod(@Field("mobilephone") String mobilephone, @Field("captcha") String captcha, @Field("password") String password);


    /**
     * 注册时获取验证码
     * @param cellphoneNumber
     * @return
     */
    @FormUrlEncoded
    @POST("index.php?s=Home/App/getCellPhoneValidation")
    Observable<BaseResult> getCellPhoneValidation(@Field("cellphoneNumber") String cellphoneNumber);

    /**
     * 注册
     * @param cellphoneNumber
     * @param password
     * @param captcha
     * @return
     */
    @FormUrlEncoded
    @POST("index.php?s=Home/App/getUserRegister")
    Observable<UserInfoResult> getUserRegister(@Field("cellphoneNumber") String cellphoneNumber, @Field("password") String password, @Field("captcha") String captcha);

    @POST("index.php?s=Home/App/getUserLogin")
    Observable<UserInfoResult> getUserLogin(@QueryMap Map<String, String> map);

    @FormUrlEncoded
    @POST("index.php?s=Home/App/openOAuth")
    Observable<UserInfoResult> openOAuth(@Field("type") String type, @Field("nickname") String nickname, @Field("sex") String sex, @Field("uuid") String uuid, @Field("thumb") String thumb);

    @FormUrlEncoded
    @POST("index.php?s=Home/App/getFavorite")
    Observable<CollectResult> getFavorite(@Field("uid") String uid);

    /**
     * 获取礼物数据
     */
    @POST("index.php?s=Home/App/getRewardSymbol")
    Observable<LiWuResult> getRewardSymbol();

    /**
     * 书籍打赏礼物
     * @return
     */
    @FormUrlEncoded
    @POST("index.php?s=Home/App/setForce")
    Observable<BaseResult> setForce(@Field("uid") String uid, @Field("bookid") String bookid, @Field("rewardid") String rewardid);

    /**
     * 购买听书卡
     * @param uid
     * @param tskid
     * @return
     */
    @FormUrlEncoded
    @POST("index.php?s=Home/App/buy_tingshuka")
    Observable<BaseResult> buy_tingshuka(@Field("uid") String uid, @Field("tskid") String tskid);
    /**
     * 收藏书籍
     */
    @FormUrlEncoded
    @POST("index.php?s=Home/App/setFavorite")
    Observable<BaseResult> setFavorite(@Field("uid") String uid, @Field("target_id") String target_id);

    /**
     * 取消收藏
     * @param uid
     * @param target_id
     * @return
     */
    @FormUrlEncoded
    @POST("index.php?s=Home/App/deleteFavorite")
    Observable<BaseResult> deleteFavorite(@Field("uid") String uid, @Field("target_id") String target_id);

    /**
     * 精品推荐
     */
    @POST("index.php?s=Home/App/getRecommend")
    Observable<FineRecommendResult> getRecommend();

    /**
     * 获取主播数据
     */
    @FormUrlEncoded
    @POST("index.php?s=Home/App/getBroadcasterInfo")
    Observable<AnchorMessResult> getBroadcasterInfo(@Field("uid") String uid, @Field("bid") String bid);

    @FormUrlEncoded
    @POST("index.php?s=Home/App/setPostComment")
    Observable<MessageResult> setPostComment(@Field("uid") String uid, @Field("bookID") String bookID, @Field("message") String message);

    /**
     * 购买单集章节
     * @param blistID
     * @param uid
     * @param bookID
     * @return
     */
    @FormUrlEncoded
    @POST("index.php?s=Home/App/buyBook")
    Observable<PayResult> buyBookSingle(@Field("blistID") String blistID, @Field("uid") String uid, @Field("bookID") String bookID);

    /**
     * 购买全书
     * @param uid
     * @param bookID
     * @return
     */
    @FormUrlEncoded
    @POST("index.php?s=Home/App/buyBook")
    Observable<PayResult> buyBookAll(@Field("uid") String uid, @Field("bookID") String bookID);

    /**
     * 获取用户信息
     * @return
     */
    @POST("index.php?s=Home/App/getMyInfo")
    Observable<UserInfoResult> getMyInfo(@QueryMap Map<String, String> map);

    /**
     * 签到
     * @param uid
     * @return
     */
    @FormUrlEncoded
    @POST("index.php?s=Home/App/setDot")
    Observable<BaseResult> setDot(@Field("uid") String uid);


    @POST("listen/api/feedback")
    Observable<BaseResult> feedback(@QueryMap Map<String, String> map);

    /**
     * 获取热门搜索词
     * @return
     */
    @POST("index.php?s=Home/App/getSearchTop")
    Observable<SearchHotResult> getSearchTop();

    @POST("index.php?s=Home/App/getSearchResult")
    Observable<SearchResult> getSearchResult(@QueryMap Map<String, String> map);

    /**
     * 获取热门主播
     * @param map
     * @return
     */
    @POST("index.php?s=Home/App/getHotBroadcaster")
    Observable<HotAnchorResult> getHotBroadcaster(@QueryMap Map<String,String> map);

    /**
     * 获取首页数据
     * @return
     */
    @POST("index.php?s=Home/App/getIndexScencs3")
    Observable<HomeResult> getIndexScencs3();

    @POST("index.php?s=Home/App/getBroadcasterInfo")
    Observable<AnchorMessResult> getBroadcasterInfo(@QueryMap Map<String, String> map);

    /**
     * 给主播送礼物
     * @param map
     * @return
     */
    @POST("index.php?s=Home/App/setGift")
    Observable<BaseResult> setGift(@QueryMap Map<String, String> map);

    /**
     * 首页热门推荐换一换
     * @param map
     * @return
     */
    @POST("index.php?s=Home/App/index_book_supermarket")
    Observable<HotRecommendResult> index_book_supermarket(@QueryMap Map<String, String> map);

    /**
     * 首页热门主播换一换
     * @param map
     * @return
     */
    @POST("index.php?s=Home/App/index_hot_author")
    Observable<HomeHotAnchorResult> index_hot_author(@QueryMap Map<String, String> map);

    /**
     * 设置用户信息
     * @param map
     * @return
     */
    @Multipart
    @POST("index.php?s=Home/App/setUserInfo")
    Observable<UserInfoResult> setUserInfo(@PartMap Map<String, RequestBody> map);


    /**
     * 检测更新
     * @param map
     * @return
     */
    @POST("index.php?s=Home/App/app_update")
    Observable<ApkResult> app_update(@QueryMap Map<String, String> map);

    /**
     * 获取首页专辑数据
     * @param map
     * @return
     */
    @POST("index.php?s=Home/App/get_special_info")
    Observable<HomeSpecialResult> get_special_info(@QueryMap Map<String, String> map);

    /**
     * 启动页广告
     * @return
     */
    @POST("index.php?s=Home/App/start_ad")
    Observable<AdResult> start_ad();

    @POST("index.php?s=Home/App/jifen_mingxi")
    Observable<DouChildrenResult> jifen_mingxi(@QueryMap Map<String, String> map);

    @GET("index.php?s=Home/App/getAllBroadcaster")
    Observable<AnchorResult> getAllBroadcaster(@QueryMap Map<String, String> map);

    /**
     * 猜你喜欢
     * @param map
     * @return
     */
    @GET("index.php?s=Home/App/get_guess_like")
    Observable<RandomRackResult> get_guess_like(@QueryMap Map<String, String> map);


    @POST("index.php?s=Home/App/get_systemmsg_list")
    Observable<MessageJavaResult> get_systemmsg_list(@QueryMap Map<String, String> map);

    @POST("index.php?s=Home/App/check_new_systemmsg")
    Observable<CheckMessageResult> check_new_systemmsg(@QueryMap Map<String, String> map);


    @POST("index.php?s=Home/App/read_systemmsg")
    Observable<BaseResult> read_systemmsg(@QueryMap Map<String, String> map);

    @POST("index.php?s=Home/App/get_vip_fee_tingdou")
    Observable<GetVIPTingdouResult> get_vip_fee_tingdou();

    @POST("index.php?s=Home/App/buy_vip")
    Observable<BaseResult> buy_vip(@QueryMap Map<String, String> map);


    @POST("listen/api/login")
    Observable<BaseResult<UserInfoResult>> login(@QueryMap Map<String, String> map);


    @GET("listen/api/getUserInfo")
    Observable<BaseResult<UserInfoResult>> getUserInfo(@QueryMap Map<String, String> map);


    @GET("listen/api/appHome")
    Observable<BaseResult<HomeResult>> appHome();


    @GET("listen/api/book")
    Observable<BaseResult<BookResult>> book(@QueryMap Map<String, String> map);


    @POST("listen/api/comments")
    Observable<BaseResult<CommentListVO>> comments(@QueryMap Map<String, String> map);

    @GET("listen/api/chapter")
    Observable<BaseResult<ChapterResult>> chapter(@QueryMap Map<String, String> map);

    @GET("listen/api/money")
    Observable<BaseResult<MoneyResult>> money(@QueryMap Map<String, String> map);


    @POST("listen/api/appWxPay")
    Observable<BaseResult<AppWxPayResult>> appWxPay(@QueryMap Map<String, String> map);


    @POST("listen/api/appAliPay")
    Observable<BaseResult<String>> appAliPay(@QueryMap Map<String, String> map);


    @GET("listen/api/expense")
    Observable<BaseResult<ExpenseResult>> expense(@QueryMap Map<String, String> map);

    @POST("listen/api/buyCard")
    Observable<BaseResult> buyCard(@QueryMap Map<String, String> map);


    @GET("listen/api/cardList")
    Observable<BaseResult<List<CardListVO>>> cardList(@QueryMap Map<String, String> map);


    @Multipart
    @POST("listen/api/modifyUserInfo")
    Observable<BaseResult<UserInfoResult>> modifyUserInfo(@PartMap Map<String, RequestBody> map);


    @GET("listen/api/category")
    Observable<BaseResult<List<CategoryVO>>> category();


    @GET("listen/api/categoryList")
    Observable<BaseResult<CategoryListResult>> categoryList(@QueryMap Map<String, String> map);

    @GET("listen/api/hostList")
    Observable<BaseResult<List<HostVO>>> hostList(@QueryMap Map<String, String> map);


    @POST("listen/api/buyChapter")
    Observable<BaseResult<DBChapter>> buyChapter(@QueryMap Map<String, String> map);


    @POST("listen/api/buyBook")
    Observable<BaseResult> buyBook(@QueryMap Map<String, String> map);

    @GET("listen/api/buyBookList")
    Observable<BaseResult<BookListResult>> buyBookList(@QueryMap Map<String, String> map);

    @POST("listen/api/batchBuyChapter")
    Observable<BaseResult> batchBuyChapter(@QueryMap Map<String, String> map);


    @GET("listen/api/queryComments")
    Observable<BaseResult<CommentListResult>> queryComments(@QueryMap Map<String, String> map);

    @POST("listen/api/collect")
    Observable<BaseResult> collect(@QueryMap Map<String, String> map);

    @POST("listen/api/unCollect")
    Observable<BaseResult> unCollect(@QueryMap Map<String, String> map);

    @GET("listen/api/collectList")
    Observable<BaseResult<List<BookVO>>> collectList(@QueryMap Map<String, String> map);


    @GET("listen/api/gift")
    Observable<BaseResult<List<GiftVO>>> gift();

    /**
     * 打赏礼物
     * @param map
     * @return
     */
    @POST("listen/api/sendGift")
    Observable<BaseResult> sendGift(@QueryMap Map<String, String> map);


    /**
     * 主播详情
     * @param map
     * @return
     */
    @GET("listen/api/hostDetail")
    Observable<BaseResult<HostDetailResult>> hostDetail(@QueryMap Map<String, String> map);


    /**
     * 关注主播
     * @param map
     * @return
     */
    @POST("listen/api/focusHost")
    Observable<BaseResult> focusHost(@QueryMap Map<String, String> map);


    /**
     * 取消关注主播
     * @param map
     * @return
     */
    @POST("listen/api/cancleFocusHost")
    Observable<BaseResult> cancleFocusHost(@QueryMap Map<String, String> map);


    /**
     * 热门搜索关键词
     * @return
     */
    @GET("listen/api/hotSearchList")
    Observable<BaseResult<List<HotSearchVO>>> hotSearchList();


    /**
     * 热门搜索关键词
     * @return
     */
    @GET("listen/api/appSearch")
    Observable<BaseResult<AppSearchResult>> appSearch(@QueryMap Map<String, String> map);


    /**
     * 用户关注主播列表
     * @return
     */
    @GET("listen/api/getHostListByUid")
    Observable<BaseResult<List<HostVO>>> getHostListByUid(@QueryMap Map<String, String> map);



    /**
     * 签到
     * @return
     */
    @GET("listen/api/sign")
    Observable<BaseResult> sign(@QueryMap Map<String, String> map);


    /**
     * 是否签到
     * @return
     */
    @GET("listen/api/isSign")
    Observable<BaseResult<Integer>> isSign(@QueryMap Map<String, String> map);


    /**
     * 书架推荐书籍
     * @return
     */
    @GET("listen/api/bookrackRecommend")
    Observable<BaseResult<BookVO>> bookrackRecommend();



    /**
     * 第三方书籍列表
     * @return
     */
    @GET("listen/api/getTeamBooks")
    Observable<BaseResult<List<BookVO>>> getTeamBooks(@QueryMap Map<String, String> map);



    /**
     * 热门推荐列表
     * @return
     */
    @GET("listen/api/hotRecommendList")
    Observable<BaseResult<CategoryListResult>> hotRecommendList(@QueryMap Map<String, String> map);



    /**
     * 获取验证码
     * @return
     */
    @GET("listen/api/sendSms")
    Observable<BaseResult> sendSms(@QueryMap Map<String, String> map);


    /**
     * 注册
     * @return
     */
    @POST("listen/api/register")
    Observable<BaseResult<UserInfoResult>> register(@QueryMap Map<String, String> map);


    /**
     * 注册
     * @return
     */
    @POST("listen/api/findPassword")
    Observable<BaseResult> findPassword(@QueryMap Map<String, String> map);


    /**
     * 版本更新
     * @return
     */
    @GET("listen/api/appVersionUpdate")
    Observable<BaseResult<ApkResult>> appVersionUpdate(@QueryMap Map<String, String> map);


    /**
     * 版本更新
     * @return
     */
    @GET("listen/api/test")
    Observable<BaseResult> test(@QueryMap Map<String, String> map);


    /**
     * 浏览书籍
     * @return
     */
    @POST("listen/api/browseBook")
    Observable<BaseResult> browseBook(@QueryMap Map<String, String> map);

    /**
     * 获取章节信息
     * @return
     */
    @GET("listen/api/getChapterUrl")
    Observable<BaseResult<DBChapter>> getChapterUrl(@QueryMap Map<String, String> map);
}

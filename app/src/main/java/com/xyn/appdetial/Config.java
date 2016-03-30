package com.xyn.appdetial;

import java.io.File;

/**
 * Created by Administrator on 2016/3/29 0029.
 */
public class Config {

    //  temp 当前的收藏数量
    public static void setUserCollectionCount(int userCollectionCount) {
        USER_COLLECTION_COUNT = userCollectionCount;
    }

    public static int USER_COLLECTION_COUNT = 0;


    //   告诉ChildFfag_s 要不要restartLoader
    public static boolean FriendsNeedRefresh = false;
    public static boolean AttetionNeedRefresh = false;
    public static boolean FansNeedRefresh = false;
    public static boolean InVitedNeedRefresh = false;
    public static boolean PrivateChatNeedRefresh = false;

    // 定义follow type


    //	public static final String TYPE_NEW_FRIENDS_UID = "item_new_friends";
//	public static final String GROUP_USERNAME = "item_groups";
    public static final String MESSAGE_ATTR_IS_VOICE_CALL = "is_voice_call";
    public static final String ACCOUNT_REMOVED = "account_removed";


    public static final int REQUEST_PHONE_CONTACT = 401;
    public static final String FOLLOW_TYPE_ALL = "0";
    public static final int FOLLOW_TYPE_FANS = 1; //粉丝
    public static final int FOLLOW_TYPE_ATTENTION = 2;//关注
    public static final int FOLLOW_TYPE_FRIENDS = 3;//好友
    public static final int FOLLOW_TYPE_INVITED = 4;//邀请你的人
    public static final int FOLLOW_TYPE_STRANGER = -1;//todo 陌生人

    // 假定的  类似于第三方的联系人，虽然是陌生人，但又不是完全的陌生人，或者说陌生人也有分类的
    public static final String FOLLOW_TYPE_GROUP_USERNAME = "item_groups";

//  准备加入的分组

//   假定的  类似于陌陌的推荐板块，或说HXdemo的 申请与通知版块
//    public static final int USER_TYPE_ID_NEW_FRIENDS = -1;//不满足互斥条件，作为userid 这也可以吗？？
//    public static final int USER_TYPE_ID_NEW_FUNS = -2;//仍然是userid

    public static final int USER_TYPE_ID__CUSTOM_GROUP = -3;


    public static final String TYPE_NEW_FRIENDS_UID = "new_friends_uid";
    public static final String TYPE_NEW_FUNS_UID = "new_funs_uid";

    public static int UNREAD_FANS_NUM = 0;
    public static int UNREAD_FRIENDS_NUM = 0;
    public static int UNREAD_INVEITED_NUM = 0;
    // 被评论
    public static int UNREAD_ACK_COMMENT = 0;
    public static int UNREAD_NEW_PORTFOLIO = 0;
    //   未读消息标志
    public static int UN_READ_JPUSH_MSG = 0; //通知


//    public static int UN_READ_CHAT_MSG = 0; //私聊
    //   这是总的未读消息数，activity 's bottom icon uses

    //  异常退出 logout

    //    个人资料页面是否刷新的flag
    public static boolean isUserNeedRefresh = false;


    public interface Consts {
        //    String host = "http://zihua.codeedu.net";
        //    String host = "http://10.10.101.60:82";
        //    String host = "http://10.10.101.61:8082";
        //    String host = "http://zihua.com.cn";

        //todo 测试环境地址为dev.zihua.com.cn
        //正式环境为zihua.com.cn
        String host = "http://zihua.com.cn";
        //String host = "http://dev.zihua.com.cn";

        String api_base = host + File.separator + "rest" + File.separator;
        String polyvOfGetThumbnail = "http://v.polyv.net/uc/video/getImage?vid=";
        boolean debug = true;
        //  1是最热， 2是最新，3是推荐
        String[] trend = new String[]{
                "最热",//1
                "最新",//2
                "推荐",//3
        };

        //  1是最热， 2是最新，3是推荐
        String[] trend1 = new String[]{
                "最新",
                "推荐",
                "点赞最多",
                "收藏最多",
                "评论最多",
        };

        String[] copyright = new String[]{
                "禁止匿名转载；禁止商业应用；禁止个人使用", //1
                "禁止匿名转载；禁止商业应用",//2
                "不限制任何用途", //3
                "禁止任何用途",//4
        };


    }
}

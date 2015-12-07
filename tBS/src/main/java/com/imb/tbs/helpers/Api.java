package com.imb.tbs.helpers;

public class Api {
    // ================================================================================
    // Public APIs
    // ================================================================================
    // Google Maps image
    public static final String MAP_API               = "https://maps.googleapis.com/maps/api/staticmap?";
    public static final String ECOMM                 = "http://www.thebodyshop.co.id/";
    public static final String ECOMM_DETAILS         = ECOMM + "p/";
    public static final String QR_CODE               =
            "https://chart.googleapis.com/chart?chs=150x150&cht=qr&chld=L|0&chl=";
    /*
     * Code 39 (c39), Code 128a (c128a), Code 128b (c128b), Code 128c (c128c), 4 of 5 Interleaved
     * (i2of5)
     */
    public static final String BAR_CODE              = "http://www.barcodes4.me/barcode/c128c/$1.png?IsTextDrawn=0";
    // ================================================================================
    // Webservice
    // ================================================================================
    // public static final String BASE_URL_LOCAL = "http://192.168.1.41:8888/ll/";
    // public static final String BASE_URL = "http://192.168.1.41:8888/ll/";
    // public static final String BASE_URL_LOCAL = "http://172.16.121.47:8888/ll/";
    // public static final String BASE_URL = "http://ll.indomegabyte.com/";
    // public static final String BASE_URL = "http://172.16.121.47:8888/ll/";
    //    public static final String BASE_URL_LOCAL        = "http://123.231.241.42:7574/";
    //    public static final String BASE_URL              = "http://123.231.241.42:7574/";
    public static final String BASE_URL_LOCAL        = "http://43.231.128.129/";
    public static final String BASE_URL              = "http://43.231.128.129/";
    public static final String PRODUCT_IMAGE_BY_NAME = BASE_URL + "LLProdImage/getImageJPG/";
    public static final String SETTINGS              = BASE_URL
            + "SettingWeb/Efiwebsetting?cmd=ws&mws=getall";
    public static final String GET_CAROUSEL          =
            BASE_URL
                    + "FeatureWeb/getNews?mode=carousel";
    public static final String GET_CAMPAIGN          = BASE_URL + "FeatureWeb/getNews?mode=offer";
    public static final String GET_NEWS              = BASE_URL + "FeatureWeb/getNews?mode=news";
    public static final String GET_REWARDS           = BASE_URL_LOCAL + "RewardWeb/findMy";
    public static final String GET_PRODUCTS          = BASE_URL + "LLProdWeb/getCategory";
    public static final String GET_PRODUCT_LIST      = BASE_URL + "LLProdWeb/getItemsUsing3rdTag";
    public static final String REGISTER_USER         = BASE_URL_LOCAL + "LL_AccWeb/signup";
    public static final String LOGIN_WITH_CARD       = BASE_URL_LOCAL + "LL_AccWeb/signInWCardnBday";
    public static final String LOGIN_WITH_FB         = BASE_URL_LOCAL + "LL_AccWeb/getByFB";
    public static final String UPDATE_FB             = BASE_URL_LOCAL + "LL_AccWeb/updateFB";
    public static final String UPDATE_USER           = BASE_URL_LOCAL + "LL_AccWeb/update";
    public static final String GET_REVIEW            = BASE_URL_LOCAL + "TestiWeb/get";
    public static final String ADD_REVIEW            = BASE_URL_LOCAL
            + "LL_Web/LL_Testimonial?cmd=ws&mws=addnew&updates=1";
    public static final String ADD_WISHLIST          = BASE_URL_LOCAL
            + "LL_Web/LL_Wishlist?cmd=ws&mws=addnew&updates=1";
    public static final String CHECK_WISHLIST        = BASE_URL_LOCAL + "LL_Web/LL_Wishlist?cmd=ws&mws=getPair";
    public static final String SEARCH_WISHLIST       = BASE_URL_LOCAL + "WishlistWeb/get";
    public static final String DELETE_WISHLIST       = BASE_URL_LOCAL + "LL_Web/LL_Wishlist?cmd=ws&mws=dodelete";
    public static final String GET_STORE             = BASE_URL + "StoreCrawlerweb/langsung";
    public static final String SEARCH_SCAN           = BASE_URL + "LLProdScanner/scan";
    public static final String SEARCH_PRODUCT_BY_ID  =
            BASE_URL
                    + "LLProdWeb/LL_Article_WImage?cmd=ws&mws=getPair";
    public static final String PRODUCT_VARIANT       = BASE_URL + "LLProdWeb/LL_Article_WImage?cmd=ws&mws=getPair";
    public static final String TNC                   =
            BASE_URL
                    + "LL_Web/LL_Tnc?cmd=ws&mws=getPair&page=1&limit=20&orderby=tnc_rank&search=tnc_aktif,1,exact";
    public static final String LIVECHAT              = BASE_URL + "livechat.html";
    public static final String STC_SETTING           = BASE_URL + "LLSettingWeb/getLLSetting";
    public static final String PUSH_NOTIF            = BASE_URL + "PushNotWeb/save";
    public static final String UPDATE_CHECK          = BASE_URL + "SettingWeb/appVersion";
    public static final String OPEN_RATE             = "http://push.indomegabyte.com/PushLoggerWeb/viewed";
}

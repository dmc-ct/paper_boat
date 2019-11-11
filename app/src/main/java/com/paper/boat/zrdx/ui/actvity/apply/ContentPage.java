package com.paper.boat.zrdx.ui.actvity.apply;

/**
 * 页面枚举
 * @author xuexiang
 * @since 2018/12/26 下午2:23
 */
public enum ContentPage {

    新闻(0),
    教育(1),
    体育(2),
    娱乐(3);

    private final int position;

    ContentPage(int pos) {
        position = pos;
    }

    public static ContentPage getPage(int position) {
        return ContentPage.values()[position];
    }

    public static int size() {
        return ContentPage.values().length;
    }

    public static String[] getPageNames() {
        ContentPage[] pages = ContentPage.values();
        String[] pageNames = new String[pages.length];
        for (int i = 0; i < pages.length; i++) {
            pageNames[i] = pages[i].name();
        }
        return pageNames;
    }

    public int getPosition() {
        return position;
    }
}
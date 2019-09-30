package com.paper.boat.zrdx.bean.rest;

import com.zl.weilu.saber.annotation.LiveData;
import com.zl.weilu.saber.annotation.LiveDataType;
import com.zl.weilu.saber.api.event.SingleLiveEvent;

/**
 * @Description:
 * @Author: weilu
 * @Time: 2018/7/25 0025 17:53.
 */
public class Single {

//    @LiveData(type = LiveDataType.OTHER, liveDataType = SingleLiveEvent.class)
//    或
//    @LiveData(type = LiveDataType.SINGLE)
//    Integer value;

    @LiveData(type = LiveDataType.OTHER, liveDataType = SingleLiveEvent.class)
    Integer value;
}

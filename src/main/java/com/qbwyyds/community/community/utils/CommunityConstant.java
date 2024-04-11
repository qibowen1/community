package com.qbwyyds.community.community.utils;

public interface CommunityConstant {
    /**
     * 激活成功
     */
    int ACTIVATION_SUCCESS=0;

    /**
     * 重复激活
     */
    int ACTIVATION_REPEAT=1;

    /**
     * 激活失败
     */
    int ACTIVATION_FALLURE=2;

    /**
     * 默认登录超时时间
     */
    int DEFAULT_EXPIRED_SECONDS=3600 * 12;

    /**
     * 记住状态下的超时时间
     */
    int REMEBER_EXPIRED_SECONDS=3600 * 24 * 100;

    /**
     * 评论类型
     */
    int ENTITY_TYPE_POST=1;

    int ENTITY_TYPE_COMMENT=2;

    int ENTITY_TYPE_USER=3;
}

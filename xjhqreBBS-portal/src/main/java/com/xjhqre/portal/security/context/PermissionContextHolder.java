package com.xjhqre.portal.security.context;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import com.xjhqre.common.text.Convert;

/**
 * 权限信息，存入 request 域中
 * 
 * @author xjhqre
 */
public class PermissionContextHolder {

    private static final String PERMISSION_CONTEXT_ATTRIBUTES = "PERMISSION_CONTEXT";

    public static void setContext(String permission) {
        RequestContextHolder.currentRequestAttributes().setAttribute(PERMISSION_CONTEXT_ATTRIBUTES, permission,
            RequestAttributes.SCOPE_REQUEST);
    }

    public static String getContext() {
        return Convert.toStr(RequestContextHolder.currentRequestAttributes().getAttribute(PERMISSION_CONTEXT_ATTRIBUTES,
            RequestAttributes.SCOPE_REQUEST));
    }
}
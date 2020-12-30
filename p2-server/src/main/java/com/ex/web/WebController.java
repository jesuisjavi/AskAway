package com.ex.web;

import com.ex.utilities.CheckAuthUtil;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * WebController with AuthUtil
 */
public class WebController {
    protected CheckAuthUtil checkAuthUtil;

    @Autowired
    public WebController(CheckAuthUtil checkAuthUtil){
        this.checkAuthUtil = checkAuthUtil;
    }
}

package com.hou.mail.component;

import com.mysql.cj.util.StringUtils;
import org.springframework.web.servlet.LocaleResolver;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

/**
 * solve the local message in href of login.html
 */
public class HLocaleResolver implements LocaleResolver {
    @Override
    public Locale resolveLocale(HttpServletRequest httpServletRequest) {
        String language = httpServletRequest.getParameter("lan");
        Locale locale = Locale.getDefault();

        if(!StringUtils.isNullOrEmpty(language)){
            String[] data = language.split("_");
            locale = new Locale(data[0], data[1]);
        }
        return locale;
    }

    @Override
    public void setLocale(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Locale locale) {

    }
}

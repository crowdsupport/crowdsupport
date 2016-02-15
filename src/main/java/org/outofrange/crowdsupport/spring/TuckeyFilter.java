package org.outofrange.crowdsupport.spring;

import org.tuckey.web.filters.urlrewrite.Conf;
import org.tuckey.web.filters.urlrewrite.UrlRewriteFilter;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import java.io.InputStream;
import java.net.URL;

public class TuckeyFilter extends UrlRewriteFilter {
    @Override
    protected void loadUrlRewriter(FilterConfig filterConfig) throws ServletException {
        // we have to load the url ourself, not with the servlet class loaded as tuckey would do it

        final String confPath = "urlrewrite.xml";
        final URL confUrl = getClass().getClassLoader().getResource(confPath);
        final InputStream config = getClass().getClassLoader().getResourceAsStream(confPath);

        Conf conf = new Conf(filterConfig.getServletContext(), config, confPath, confUrl.toString(), false);
        checkConf(conf);
    }
}

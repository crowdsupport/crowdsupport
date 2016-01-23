package org.outofrange.crowdsupport.dto;

import org.outofrange.crowdsupport.spring.Config;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpRequest;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public abstract class LinkBaseDto extends BaseDto {
    private List<Link> links = new ArrayList<>();

    private final boolean autoSelfLink;
    private boolean selfLinkAdded = false;

    @Inject
    private ServletContext servletContext;

    public LinkBaseDto() {
        this(true);
    }

    public LinkBaseDto(boolean autoSelfLink) {
        this.autoSelfLink = autoSelfLink;
    }

    public List<Link> getLinks() {
        if (autoSelfLink && !selfLinkAdded) {
            addSelfLink();
            selfLinkAdded = true;
        }

        return links;
    }

    public void addSelfLink() {
        links.add(new Link(selfLink(), "self"));
    }

    public URI uri() {
        return URI.create(selfLink());
    }

    protected String selfLink() {
        return getWholeContextPath() + "/service/v2" + self();
    }

    protected abstract String self();

    private static HttpServletRequest getCurrentRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        HttpServletRequest servletRequest = ((ServletRequestAttributes) requestAttributes).getRequest();
        return servletRequest;
    }

    private static String getWholeContextPath() {
        HttpRequest httpRequest = new ServletServerHttpRequest(getCurrentRequest());
        UriComponents uriComponents = UriComponentsBuilder.fromHttpRequest(httpRequest).build();
        String scheme = uriComponents.getScheme();
        String host = uriComponents.getHost();
        int port = uriComponents.getPort();

        final StringBuilder sb = new StringBuilder();
        sb.append(scheme).append("://").append(host);


        if (("http".equals(scheme) && port != 80) || ("https".equals(scheme) && port != 443)) {
            sb.append(":").append(port);
        }

        return sb.append(Config.getContextPath()).toString();
    }
}

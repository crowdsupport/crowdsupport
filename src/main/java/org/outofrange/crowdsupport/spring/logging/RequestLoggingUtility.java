package org.outofrange.crowdsupport.spring.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.boot.logging.logback.ColorConverter;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class RequestLoggingUtility extends OncePerRequestFilter implements ServletRequestListener {
    public static final String ID_PROPERTY = "requestId";
    public static final String COLOR_PROPERTY = "requestColor";
    public static final String START_TIME_PROPERTY = "started";

    private static final Logger log = LoggerFactory.getLogger(RequestLoggingUtility.class);
    private static final Random R = new Random();

    private static final String[] COLORS = getColors();

    // start with random color
    private int c = (int) (R.nextFloat() * COLORS.length);

    @SuppressWarnings("unchecked")
    private static String[] getColors() {
        try {
            Field ELEMENTS = ColorConverter.class.getDeclaredField("ELEMENTS");
            ELEMENTS.setAccessible(true);

            final Set<String> keySet = ((Map<String, ?>) ELEMENTS.get(null)).keySet();
            final String[] colorKeys = keySet.toArray(new String[keySet.size()]);

            ELEMENTS.setAccessible(false);

            return colorKeys;
        } catch (NoSuchFieldException | IllegalAccessException e) {
            log.error("Couldn't initialize colors for request logging", e);
            return new String[]{};
        }
    }

    private String getNextColor() {
        return COLORS[c++ % COLORS.length];
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String requestId = Integer.toHexString(R.nextInt());

        MDC.put(ID_PROPERTY, requestId);
        MDC.put(COLOR_PROPERTY, getNextColor());
        MDC.put(START_TIME_PROPERTY, String.valueOf(System.nanoTime()));

        log.trace("Start of request {}", requestId);

        filterChain.doFilter(request, response);
    }

    @Override
    public void requestDestroyed(ServletRequestEvent sre) {
        final String requestId = MDC.get(ID_PROPERTY);
        if(requestId != null) {
            final long nanos = System.nanoTime() - Long.valueOf(MDC.get(START_TIME_PROPERTY));
            final long ms = nanos / 1000000;

            log.trace("End of request {} (took {}ms on server)", requestId, ms);

            MDC.clear();
        }
    }

    @Override
    public void requestInitialized(ServletRequestEvent sre) { }
}

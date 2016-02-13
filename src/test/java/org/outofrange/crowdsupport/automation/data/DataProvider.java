package org.outofrange.crowdsupport.automation.data;

import com.google.common.collect.ClassToInstanceMap;
import com.google.common.collect.MutableClassToInstanceMap;
import org.outofrange.crowdsupport.automation.Cleanable;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class DataProvider implements Cleanable, ApplicationContextAware {
    private final ClassToInstanceMap<Cleanable> PROVIDERS = MutableClassToInstanceMap.create();

    private ApplicationContext ctx;

    public UserDataProvider user() {
        final UserDataProvider existing = PROVIDERS.getInstance(UserDataProvider.class);
        if (existing != null) {
            return existing;
        } else {
            final UserDataProvider newProvider = ctx.getBean(UserDataProvider.class);
            PROVIDERS.putInstance(UserDataProvider.class, newProvider);
            return newProvider;
        }
    }


    @Override
    public void cleanUp() {
        PROVIDERS.forEach((c, i) -> i.cleanUp());
        PROVIDERS.clear();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.ctx = applicationContext;
    }
}

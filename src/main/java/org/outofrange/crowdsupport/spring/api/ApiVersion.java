package org.outofrange.crowdsupport.spring.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation versions a REST interface.
 * <p>
 * The following code demonstrates its behaviour:
 * <pre>
 * // this method will be accessible at "/v1/something" and "/v2/something"
 *{@literal @}RequestMapping(value = "something", method = RequestMethod.GET)
 *{@literal @}ApiVersion("1", "2")
 * public Object doSomething() { ... };
 * </pre>
 *
 * <p/>
 * The annotation can also be applied at type level to version an entire REST controller.
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiVersion {
    String[] value();
}
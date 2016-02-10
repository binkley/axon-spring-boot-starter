package hm.binkley.spring.axon;

import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * {@code EnableAxonQuery} enables Axon configuration for Spring Boot for
 * query (read side) only.
 *
 * @author <a href="mailto:binkley@alumni.rice.edu">B. K. Oxley (binkley)</a>
 * @see AxonQueryConfiguration
 */
@Documented
@Import(AxonQueryConfiguration.class)
@Retention(RUNTIME)
@Target(TYPE)
public @interface EnableAxonQuery {}

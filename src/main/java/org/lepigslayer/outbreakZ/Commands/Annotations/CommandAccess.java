package org.lepigslayer.outbreakZ.Commands.Annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface CommandAccess {
    CommandAccessLevel value() default CommandAccessLevel.ANYONE;
}

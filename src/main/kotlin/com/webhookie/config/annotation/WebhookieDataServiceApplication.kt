package com.webhookie.config.annotation

import org.springframework.data.mongodb.config.EnableReactiveMongoAuditing
import java.lang.annotation.Inherited

/**
 *
 * @author Arthur Kazemi<bidadh@gmail.com>
 * @since 20:13
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Inherited
@WebhookieServiceApplication
@EnableReactiveMongoAuditing
annotation class WebhookieDataServiceApplication

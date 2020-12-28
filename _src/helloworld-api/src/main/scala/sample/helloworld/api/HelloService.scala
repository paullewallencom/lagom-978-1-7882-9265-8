/*
 * Copyright (C) 2016 Lightbend Inc. <http://www.lightbend.com>
 */
package sample.helloworld.api

import akka.{Done, NotUsed}
import com.lightbend.lagom.javadsl.api.{Descriptor, Service, ServiceCall}
import com.lightbend.lagom.javadsl.api.ScalaService._

/**
  * The hello service interface.
  * <p>
  * This describes everything that Lagom needs to know about how to serve and
  * consume the HelloService.
  */
trait HelloService extends Service {
  def hello(id: String): ServiceCall[NotUsed, String]

  /**
    * Example: curl http://localhost:9000/api/hello/Alice
    */
  def useGreeting(id: String): ServiceCall[GreetingMessage, Done]

  /**
    * Example: curl -H "Content-Type: application/json" -X POST -d '{"message":
    * "Hi"}' http://localhost:9000/api/hello/Alice
    */
  def descriptor: Descriptor = {
    // @formatter:off
    named("helloservice").withCalls(
      pathCall("/api/hello/:id", hello _),
      pathCall("/api/hello/:id", useGreeting _)
    ).withAutoAcl(true)
    // @formatter:on
  }
}

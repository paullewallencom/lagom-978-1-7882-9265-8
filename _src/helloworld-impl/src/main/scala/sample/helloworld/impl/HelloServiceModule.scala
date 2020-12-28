/*
 * Copyright (C) 2016 Lightbend Inc. <http://www.lightbend.com>
 */
package sample.helloworld.impl

import com.google.inject.AbstractModule
import com.lightbend.lagom.javadsl.server.ServiceGuiceSupport
import sample.helloworld.api.HelloService

class HelloServiceModule extends AbstractModule with ServiceGuiceSupport {
  protected def configure {
    bindServices(serviceBinding(classOf[HelloService], classOf[HelloServiceImpl]))
  }
}
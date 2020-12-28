/*
 * Copyright (C) 2016 Lightbend Inc. <http://www.lightbend.com>
 */
package sample.helloworld.impl

import javax.inject.Inject

import akka.{Done, NotUsed}
import com.lightbend.lagom.javadsl.api.ServiceCall
import com.lightbend.lagom.javadsl.persistence.{PersistentEntityRef, PersistentEntityRegistry}
import sample.helloworld.api.{GreetingMessage, HelloService}
import sample.helloworld.impl.HelloCommand.{Hello, UseGreetingMessage}

/**
  * Implementation of the HelloService.
  */
class HelloServiceImpl @Inject()(persistentEntityRegistry: PersistentEntityRegistry) extends HelloService {

  persistentEntityRegistry.register(classOf[HelloWorld])

  def hello(id: String): ServiceCall[NotUsed, String] = request => {
    // Look up the hello world entity for the given ID.
    val ref: PersistentEntityRef[HelloCommand] = persistentEntityRegistry.refFor[HelloCommand](classOf[HelloWorld], id)
    // Ask the entity the Hello command.
    ref.ask(new Hello(id, None))
  }

  def useGreeting(id: String): ServiceCall[GreetingMessage, Done] = request => {
    // Look up the hello world entity for the given ID.
    val ref: PersistentEntityRef[HelloCommand] = persistentEntityRegistry.refFor[HelloCommand](classOf[HelloWorld], id)
    // Tell the entity to use the greeting message specified.
    ref.ask(new UseGreetingMessage(request.message))
  }
}
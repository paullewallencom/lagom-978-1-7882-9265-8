/*
 * Copyright (C) 2016 Lightbend Inc. <http://www.lightbend.com>
 */
package sample.helloworld.impl

import com.lightbend.lagom.serialization.Jsonable

trait HelloEvent extends Jsonable

/**
  * This interface defines all the events that the HelloWorld entity supports.
  * <p>
  * By convention, the events should be inner classes of the interface, which
  * makes it simple to get a complete picture of what events an entity has.
  */
object HelloEvent {

  /**
    * An event that represents a change in greeting message.
    */
  case class GreetingMessageChanged (message: String) extends HelloEvent {
    require(message != null, "message must not be null")
  }
}
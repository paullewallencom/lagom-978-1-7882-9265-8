/*
 * Copyright (C) 2016 Lightbend Inc. <http://www.lightbend.com>
 */
package sample.helloworld.impl

import akka.Done
import com.lightbend.lagom.javadsl.persistence.PersistentEntity
import com.lightbend.lagom.serialization.{CompressedJsonable, Jsonable}

sealed trait HelloCommand extends Jsonable

/**
  * This interface defines all the commands that the HelloWorld entity supports.
  *
  * By convention, the commands should be inner classes of the interface, which
  * makes it simple to get a complete picture of what commands an entity
  * supports.
  */
object HelloCommand {

  /**
    * A command to switch the greeting message.
    * <p>
    * It has a reply type of {@link akka.Done}, which is sent back to the caller
    * when all the events emitted by this command are successfully persisted.
    */
  case class UseGreetingMessage(message: String)
    extends HelloCommand with CompressedJsonable with PersistentEntity.ReplyType[Done] {
      require(message != null, "message must not be null")
  }

  /**
    * A command to say hello to someone using the current greeting message.
    * <p>
    * The reply type is String, and will contain the message to say to that
    * person.
    */
  case class Hello(name: String, organization: Option[String])
    extends HelloCommand with PersistentEntity.ReplyType[String] {
      require(name != null, "name must not be null")
      require(organization != null, "organization must not be null")
  }
}
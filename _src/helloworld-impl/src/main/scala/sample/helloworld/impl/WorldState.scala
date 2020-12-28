/*
 * Copyright (C) 2016 Lightbend Inc. <http://www.lightbend.com>
 */
package sample.helloworld.impl

import com.lightbend.lagom.serialization.CompressedJsonable

/**
  * The state for the {@link HelloWorld} entity.
  */
case class WorldState (message: String, timestamp: String) extends CompressedJsonable {
  require(message != null, "message must not null")
  require(timestamp != null, "timestamp must not be null")
}
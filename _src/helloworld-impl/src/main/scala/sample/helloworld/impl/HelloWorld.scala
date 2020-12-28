/*
 * Copyright (C) 2016 Lightbend Inc. <http://www.lightbend.com>
 */
package sample.helloworld.impl

import java.time.LocalDateTime
import java.util.Optional

import akka.Done
import com.lightbend.lagom.javadsl.persistence.PersistentEntity
import sample.helloworld.impl.HelloCommand._
import sample.helloworld.impl.HelloEvent.GreetingMessageChanged

/**
  * This is an event sourced entity. It has a state, {@link WorldState}, which
  * stores what the greeting should be (eg, "Hello").
  * <p>
  * Event sourced entities are interacted with by sending them commands. This
  * entity supports two commands, a {@link UseGreetingMessage} command, which is
  * used to change the greeting, and a {@link Hello} command, which is a read
  * only command which returns a greeting to the name specified by the command.
  * <p>
  * Commands get translated to events, and it's the events that get persisted by
  * the entity. Each event will have an event handler registered for it, and an
  * event handler simply applies an event to the current state. This will be done
  * when the event is first created, and it will also be done when the entity is
  * loaded from the database - each event will be replayed to recreate the state
  * of the entity.
  * <p>
  * This entity defines one event, the {@link GreetingMessageChanged} event,
  * which is emitted when a {@link UseGreetingMessage} command is received.
  */
class HelloWorld extends PersistentEntity[HelloCommand, HelloEvent, WorldState] {
  /**
    * An entity can define different behaviours for different states, but it will
    * always start with an initial behaviour. This entity only has one behaviour.
    */
  def initialBehavior(snapshotState: Optional[WorldState]): Behavior = {

    /*
    * Behaviour is defined using a behaviour builder. The behaviour builder
    * starts with a state, if this entity supports snapshotting (an
    * optimisation that allows the state itself to be persisted to combine many
    * events into one), then the passed in snapshotState may have a value that
    * can be used.
    *
    * Otherwise, the default state is to use the Hello greeting.
    */
    val b: BehaviorBuilder = newBehaviorBuilder(snapshotState.orElse(new WorldState("Hello", LocalDateTime.now.toString)))

    import scala.compat.java8.FunctionConverters._

    /*
     * Command handler for the UseGreetingMessage command.
     */
    b.setCommandHandler[Done, UseGreetingMessage](classOf[HelloCommand.UseGreetingMessage],
      // In response to this command, we want to first persist it as a
      // GreetingMessageChanged event
      asJavaBiFunction((cmd, ctx) => ctx.thenPersist[HelloEvent](
        GreetingMessageChanged(cmd.message),
        // Then once the event is successfully persisted, we respond with done.
        asJavaConsumer((event: HelloEvent) => ctx.reply(Done.getInstance()))
      )
      )
    )

    /*
     * Event handler for the GreetingMessageChanged event.
     */
    b.setEventHandler[HelloEvent.GreetingMessageChanged](classOf[HelloEvent.GreetingMessageChanged],
      asJavaFunction(evt => new WorldState(evt.message, LocalDateTime.now().toString())))

    /*
     * Command handler for the Hello command.
     */
    b.setReadOnlyCommandHandler[String, Hello](classOf[HelloCommand.Hello],
      asJavaBiConsumer((cmd, ctx) => ctx.reply(state.message + ", " + cmd.name + "!")))

    /*
     * We've defined all our behaviour, so build and return it.
     */
    b.build
  }
}
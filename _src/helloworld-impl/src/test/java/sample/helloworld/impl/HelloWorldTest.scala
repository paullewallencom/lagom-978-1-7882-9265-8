package sample.helloworld.impl

import org.junit.Assert.assertEquals
import java.util.Collections
import java.util.Optional
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Test
import com.lightbend.lagom.javadsl.testkit.PersistentEntityTestDriver
import com.lightbend.lagom.javadsl.testkit.PersistentEntityTestDriver.Outcome
import akka.Done
import akka.actor.ActorSystem
import akka.testkit.JavaTestKit
import sample.helloworld.impl.HelloCommand.Hello
import sample.helloworld.impl.HelloCommand.UseGreetingMessage
import sample.helloworld.impl.HelloEvent.GreetingMessageChanged

object HelloWorldTest {
  private[impl] var system: ActorSystem = null

  @BeforeClass def setup {
    system = ActorSystem.create("HelloWorldTest")
  }

  @AfterClass def teardown {
    JavaTestKit.shutdownActorSystem(system)
    system = null
  }
}

class HelloWorldTest {
  @Test def testHelloWorld {
    val driver: PersistentEntityTestDriver[HelloCommand, HelloEvent, WorldState] =
      new PersistentEntityTestDriver[HelloCommand, HelloEvent, WorldState](HelloWorldTest.system, new HelloWorld, "world-1")
    val outcome1: PersistentEntityTestDriver.Outcome[HelloEvent, WorldState] = driver.run(new HelloCommand.Hello("Alice", None))
    assertEquals("Hello, Alice!", outcome1.getReplies.get(0))
    assertEquals(Collections.emptyList, outcome1.issues)
    val outcome2: PersistentEntityTestDriver.Outcome[HelloEvent, WorldState] =
      driver.run(new HelloCommand.UseGreetingMessage("Hi"), new HelloCommand.Hello("Bob", None))
    assertEquals(1, outcome2.events.size)
    assertEquals(new HelloEvent.GreetingMessageChanged("Hi"), outcome2.events.get(0))
    assertEquals("Hi", outcome2.state.message)
    assertEquals(Done.getInstance, outcome2.getReplies.get(0))
    assertEquals("Hi, Bob!", outcome2.getReplies.get(1))
    assertEquals(2, outcome2.getReplies.size)
    assertEquals(Collections.emptyList, outcome2.issues)
  }
}
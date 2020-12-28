package sample.helloworld.api

case class GreetingMessage(message: String) {
  require(message != null, "message must not be null")
}

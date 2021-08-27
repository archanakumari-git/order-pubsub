package com.lightbend.order

import com.lightbend.order.entity.{OrderRequest, OrderResponse}
import net.liftweb.json.{DefaultFormats, parse}
import net.liftweb.json.Serialization.write
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}

import java.util.{Properties, UUID}

class OrderProducer {
  def main(args: Array[String]): Unit = {
    writeToKafka("order-response")
  }

  def writeToKafka(topic: String): Unit = {
    val props: Properties = setKafkaProperties
    val producer = new KafkaProducer[String, String](props)
    //todo : change when integrating with orch
    val orders = generateRandomOrders("")
    val record = new ProducerRecord[String, String](topic, "key", "value")
    producer.send(record)
    producer.close()
  }

  def generateRandomOrders(orderRequestJsonString : String) = {
    implicit val formats: DefaultFormats.type = DefaultFormats
    val orderRequest = parse(orderRequestJsonString).extract[OrderRequest]
    val orderId = UUID.randomUUID().toString
    val order = OrderResponse(orderRequest.requestId,orderRequest.requestId,orderId,true)
    val jsonString = write(order)
    println(jsonString)
    jsonString
  }

  private def setKafkaProperties = {
    val props = new Properties()
    props.put("bootstrap.servers", "localhost:9094")
    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    props
  }
}

package com.lightbend.order.entity

case class OrderResponse(userId: String, requestId: String, orderId: String, status: Boolean)

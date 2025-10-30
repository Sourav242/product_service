package com.sourav.product_service.model

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

@Document("products")
data class Product(
    @Id
    val id: ObjectId = ObjectId.get(),
    val title: String,
    val description: String,
    val price: Double,
    val discountPercentage: Double,
    val rating: Double,
    val stock: Int,
    val images: List<String>,
    val createdAt: Instant = Instant.now(),
    val ownerId: ObjectId
)
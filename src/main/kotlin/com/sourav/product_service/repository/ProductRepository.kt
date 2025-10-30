package com.sourav.product_service.repository

import com.sourav.product_service.model.Product
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository

interface ProductRepository : MongoRepository<Product, ObjectId> {
    fun findByOwnerId(ownerId: ObjectId): List<Product>
    fun findObjectByIdAndOwnerId(id: ObjectId, ownerId: ObjectId): Product
}
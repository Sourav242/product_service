package com.sourav.product_service.controller

import com.sourav.product_service.model.Product
import com.sourav.product_service.repository.ProductRepository
import com.sourav.product_service.utils.Constants.DEMO_OWNER_ID
import org.bson.types.ObjectId
import org.springframework.web.bind.annotation.*
import java.time.Instant

@RestController
@RequestMapping("/products")
class ProductController(
    private val productRepository: ProductRepository
) {

    data class ProductRequest(
        val id: String?,
        val title: String,
        val description: String,
        val price: Double,
        val discountPercentage: Double,
        val rating: Double,
        val stock: Int,
        val images: List<String>
    )

    data class ProductResponse(
        val id: String,
        val title: String,
        val description: String,
        val price: Double,
        val discountPercentage: Double,
        val rating: Double,
        val stock: Int,
        val images: List<String>,
        val createdAt: Instant
    )

    @GetMapping
    fun getAllProducts(): List<ProductResponse> {
        return productRepository.findAll().map {
            it.toResponse()
        }
    }

    @GetMapping(path = ["/my-products"])
    fun getMyProducts(): List<ProductResponse> {
        val ownerId = ObjectId(DEMO_OWNER_ID) // Placeholder ownerId
        return productRepository.findByOwnerId(ownerId).map { it.toResponse() }
    }

    @GetMapping(path = ["/my-products/{id}"])
    fun getMyProducts(
        @PathVariable id: String
    ): ProductResponse {
        val ownerId = ObjectId(DEMO_OWNER_ID) // Placeholder ownerId
        return productRepository.findObjectByIdAndOwnerId(
            ownerId = ownerId,
            id = ObjectId(id)
        ).toResponse()
    }

    @PostMapping
    fun saveProduct(
        @RequestBody body: ProductRequest
    ): ProductResponse {
        val ownerId = ObjectId(DEMO_OWNER_ID) // Placeholder ownerId
        val product = Product(
            id = body.id?.let { ObjectId(it) } ?: ObjectId.get(),
            title = body.title,
            description = body.description,
            price = body.price,
            discountPercentage = body.discountPercentage,
            rating = body.rating,
            stock = body.stock,
            images = body.images,
            ownerId = ownerId
        )
        return productRepository.save(product).toResponse()
    }

    @DeleteMapping(path = ["/{id}"])
    fun deleteAllProducts(
        @PathVariable id: String
    ) {
        val product = productRepository.findById(ObjectId(id)).orElseThrow {
            IllegalArgumentException("Product not found")
        }
        val ownerId = ObjectId(DEMO_OWNER_ID) // Placeholder ownerId
        if (product.ownerId != ownerId) {
            throw IllegalAccessException("You are not authorized to delete this product")
        }
        productRepository.deleteById(ObjectId(id))
    }
}

private fun Product.toResponse() = ProductController.ProductResponse(
    id = this.id.toHexString(),
    title = this.title,
    description = this.description,
    price = this.price,
    discountPercentage = this.discountPercentage,
    rating = this.rating,
    stock = this.stock,
    images = this.images,
    createdAt = this.createdAt
)
package com.gotrid.trid.marshmello.Client;//package com.example.metamall.controllers.Client;
//import com.example.metamall.models.Shops.Product;
//import com.example.metamall.services.ProductService;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//
//
//@RestController
//@RequestMapping("/api/products")
//public class ProductController {
//
//    private final ProductService productService;
//
//    public ProductController(ProductService productService) {
//        this.productService = productService;
//    }
//
//    @PostMapping(value = "/{shopId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public ResponseEntity<Product> createProduct(
//            @PathVariable Long shopId,
//            @RequestParam String userId,
//            @RequestParam String productId,
//            @RequestPart Product product,
//            @RequestPart(required = false) MultipartFile gltfFile,
//            @RequestPart(required = false) MultipartFile binFile,
//            @RequestPart(required = false) MultipartFile iconFile,
//            @RequestPart(required = false) MultipartFile textureFile) throws IOException {
//
//        Product createdProduct = productService.createProductWithAssets(
//                shopId, userId, productId, product, gltfFile, binFile, iconFile, textureFile);
//        return ResponseEntity.ok(createdProduct);
//    }
//
//    @GetMapping("/{productId}/gltf")
//    public ResponseEntity<byte[]> getProductGltf(@PathVariable Long productId) {
//        return ResponseEntity.ok()
//                .contentType(MediaType.APPLICATION_OCTET_STREAM)
//                .body(productService.getProductGltfFile(productId));
//    }
//
//    @DeleteMapping("/{productId}")
//    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId) {
//        productService.deleteProductWithAssets(productId);
//        return ResponseEntity.noContent().build();
//    }
//}

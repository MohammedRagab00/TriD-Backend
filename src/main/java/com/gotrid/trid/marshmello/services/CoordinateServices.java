package com.gotrid.trid.marshmello.services;//package com.example.metamall.services;
//
//
//import com.example.metamall.models.Shops.Coordinate;
//import com.example.metamall.models.Shops.Shops;
//import com.example.metamall.models.Shops.Product;
//import com.example.metamall.repository.CoordinateRepository;
//import com.example.metamall.repository.ShopRepository;
//
//import com.example.metamall.repository.ProductRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//@Service
//public class CoordinateServices {
//
//    @Autowired
//    private CoordinateRepository coordinateRepository;
//
//    @Autowired
//    private ShopRepository shopsRepository;
//
//    @Autowired
//    private ProductRepository productRepository;
//
//    public Coordinate saveCoordinate(Long shopId, Long productId,
//                                     float xPos, float yPos, float zPos,
//                                     float xScale, float yScale, float zScale,
//                                     float xRot, float yRot, float zRot) throws Exception {
//
//        Shops shop = shopsRepository.findById(shopId)
//                .orElseThrow(() -> new Exception("Shop not found"));
//
//        Product product = productRepository.findById(productId)
//                .orElseThrow(() -> new Exception("Product not found"));
//
//        Coordinate coordinate = new Coordinate();
//        coordinate.setShop(shop);
//        coordinate.setProduct(product);
//
//        coordinate.setXPos(xPos);
//        coordinate.setYPos(yPos);
//        coordinate.setZPos(zPos);
//
//        coordinate.setXScale(xScale);
//        coordinate.setYScale(yScale);
//        coordinate.setZScale(zScale);
//
//        coordinate.setXRot(xRot);
//        coordinate.setYRot(yRot);
//        coordinate.setZRot(zRot);
//
//        return coordinateRepository.save(coordinate);
//    }
//}
//

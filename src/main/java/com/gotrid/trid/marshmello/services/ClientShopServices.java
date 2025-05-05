package com.gotrid.trid.marshmello.services;//package com.example.metamall.services;
//
//import com.example.metamall.exception.ResourceNotFoundException;
//import com.example.metamall.models.Entities.User;
//import com.example.metamall.models.Shops.ClientShop;
//import com.example.metamall.models.Shops.ClientShopId;
//import com.example.metamall.models.Shops.Shops;
//import com.example.metamall.repository.ClientShopRepository;
//import com.example.metamall.repository.ShopRepository;
//import com.example.metamall.repository.UserRepository;
//import jakarta.transaction.Transactional;
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.domain.Pageable;
//import org.springframework.stereotype.Service;
//
//import java.nio.file.AccessDeniedException;
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//@Transactional
//public class ClientShopServices {
//    private final ClientShopRepository clientShopRepository;
//    private final UserRepository userRepository;
//    private final ShopRepository shopRepository;
//
//    public void grantAccess(Long ownerId, Long shopId, Long userId, String accessLevel) {
//        validateAccess(ownerId, shopId, userId);
//
//        ClientShop clientShop = new ClientShop();
//        clientShop.setUser(userRepository.getReferenceById(userId));
//        clientShop.setShop(shopRepository.getReferenceById(shopId));
//        clientShop.setAccessLevel(accessLevel);
//        clientShop.setId(new ClientShopId(userId, shopId));
//
//        clientShopRepository.save(clientShop);
//    }
//
//    public void updateAccess(Long ownerId, Long shopId, Long userId, String newAccessLevel) {
//        validateAccess(ownerId, shopId, userId);
//
//        clientShopRepository.updateAccessLevel(userId, shopId, newAccessLevel);
//    }
//
//    public void revokeAccess(Long ownerId, Long shopId, Long userId) {
//        if (!shopRepository.existsByIdAndOwnerId(shopId, ownerId)) {
//            throw new AccessDeniedException("Only shop owner can revoke access");
//        }
//
//        clientShopRepository.deleteByUserIdAndShopId(userId, shopId);
//    }
//
//    public List<Shops> getAccessibleShops(Long userId) {
//        return clientShopRepository.findShopsByUserId(userId, Pageable.unpaged()).getContent();
//    }
//
//    public List<User> getShopUsers(Long ownerId, Long shopId) {
//        if (!shopRepository.existsByOwnerIdAndName(shopId, ownerId)) {
//            throw new AccessDeniedException("Only shop owner can view access list");
//        }
//
//        return clientShopRepository.findUsersByShopId(shopId, Pageable.unpaged()).getContent();
//    }
//
//    public boolean hasAccess(Long userId, Long shopId, String requiredAccessLevel) {
//        return clientShopRepository.existsByUserIdAndShopIdAndAccessLevel(
//                userId, shopId, requiredAccessLevel);
//    }
//
//    private void validateAccess(Long ownerId, Long shopId, Long userId) throws AccessDeniedException {
//        if (!shopRepository.existsByOwnerIdAndName(shopId, ownerId)) {
//            throw new AccessDeniedException("Only shop owner can grant access");
//        }
//
//        if (ownerId.equals(userId)) {
//            throw new IllegalArgumentException("Cannot grant access to owner");
//        }
//
//        if (!userRepository.existsById(userId)) {
//            throw new ResourceNotFoundException("User not found");
//        }
//    }
//}

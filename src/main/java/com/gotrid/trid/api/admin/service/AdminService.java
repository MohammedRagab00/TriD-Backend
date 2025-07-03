package com.gotrid.trid.api.admin.service;

import com.gotrid.trid.api.admin.dto.DashboardStatsDto;
import com.gotrid.trid.api.admin.dto.RecentOrderDto;
import com.gotrid.trid.api.admin.dto.UserSearchResponse;
import com.gotrid.trid.common.response.PageResponse;
import com.gotrid.trid.core.order.repository.OrderRepository;
import com.gotrid.trid.core.user.mapper.UserMapper;
import com.gotrid.trid.core.user.model.Role;
import com.gotrid.trid.core.user.model.Users;
import com.gotrid.trid.core.user.repository.RoleRepository;
import com.gotrid.trid.core.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Service
public class AdminService implements IAdminService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final OrderRepository orderRepository;
    private final RoleRepository roleRepository;

    @Override
    @Cacheable(value = "users", key = "#email + '-' + #page + '-' + #size")
    public PageResponse<UserSearchResponse> searchUserByEmail(String email, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<Users> usersPage = userRepository.findByEmailContainingIgnoreCase(email, pageable);

        List<UserSearchResponse> userSearchResponses = usersPage.map(userMapper::toSearchResponse).getContent();

        return new PageResponse<>(
                userSearchResponses,
                usersPage.getNumber(),
                usersPage.getSize(),
                usersPage.getTotalElements(),
                usersPage.getTotalPages(),
                usersPage.isFirst(),
                usersPage.isLast()
        );
    }

    @Override
    @Transactional
    public UserSearchResponse updateUserRoles(Integer id, Set<String> roleNames) {
        Users user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + id));

        Set<Role> roles = roleRepository.findByNameIn(roleNames);
        if (roles.size() != roleNames.size()) {
            Set<String> invalidRoles = roleNames.stream()
                    .filter(name -> roles.stream().noneMatch(role -> role.getName().equals(name)))
                    .collect(Collectors.toSet());
            throw new IllegalArgumentException("Invalid roles: " + invalidRoles);
        }

        user.setRoles(roles);
        Users savedUser = userRepository.save(user);
        return userMapper.toSearchResponse(savedUser);
    }

    @Override
    public DashboardStatsDto getStats() {
        long totalUsers = userRepository.count();
        long totalOrders = orderRepository.count();

        BigDecimal totalRevenue = orderRepository.sumTotalAmount();
        BigDecimal netProfit = totalRevenue.multiply(new BigDecimal("0.2")); //* example: assuming 20% profit margin

        return new DashboardStatsDto(totalUsers, totalOrders, totalRevenue, netProfit);
    }

    @Override
    public List<RecentOrderDto> getRecentOrders() {
        return orderRepository.getRecentOrders(PageRequest.of(0, 10, Sort.by("createdDate").descending()));
    }
}
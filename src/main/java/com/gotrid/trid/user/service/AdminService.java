package com.gotrid.trid.user.service;

import com.gotrid.trid.infrastructure.common.PageResponse;
import com.gotrid.trid.user.domain.Users;
import com.gotrid.trid.user.dto.UserSearchResponse;
import com.gotrid.trid.user.mapper.UserMapper;
import com.gotrid.trid.user.model.Role;
import com.gotrid.trid.user.repository.RoleRepository;
import com.gotrid.trid.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Service
public class AdminService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;

    public PageResponse<UserSearchResponse> searchUserByEmail(String email, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<Users> usersPage = userRepository.findByEmailContainingIgnoreCase(email, pageable);
        List<UserSearchResponse> userSearchResponses = usersPage.stream()
                .map(userMapper::toSearchResponse)
                .toList();

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
}

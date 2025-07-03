package com.gotrid.trid.api.admin.service;

import com.gotrid.trid.api.admin.dto.DashboardStatsDto;
import com.gotrid.trid.api.admin.dto.RecentOrderDto;
import com.gotrid.trid.api.admin.dto.UserSearchResponse;
import com.gotrid.trid.common.response.PageResponse;

import java.util.List;
import java.util.Set;

public interface IAdminService {
    PageResponse<UserSearchResponse> searchUserByEmail(String email, int page, int size);

    UserSearchResponse updateUserRoles(Integer id, Set<String> roleNames);

    DashboardStatsDto getStats();

    //? Why?
    List<RecentOrderDto> getRecentOrders();
}

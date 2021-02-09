package uz.bdm.HrTesting.service;

import uz.bdm.HrTesting.domain.UserDetail;

public interface UserDetailService {
    UserDetail findByUserId(Long id);
}

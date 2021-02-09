package uz.bdm.HrTesting.service.Impl;

import org.springframework.stereotype.Service;
import uz.bdm.HrTesting.domain.UserDetail;
import uz.bdm.HrTesting.ropository.UserDetailRepository;
import uz.bdm.HrTesting.service.UserDetailService;

@Service
public class UserDetailServiceImpl implements UserDetailService {

    private final UserDetailRepository userDetailRepository;

    public UserDetailServiceImpl(UserDetailRepository userDetailRepository) {
        this.userDetailRepository = userDetailRepository;
    }

    @Override
    public UserDetail findByUserId(Long id) {
        return userDetailRepository.findByUserId(id).orElse(null);
    }
}

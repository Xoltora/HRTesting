package uz.bdm.HrTesting.service.Impl;

import org.springframework.stereotype.Service;
import uz.bdm.HrTesting.constants.AuthoritiesConstants;
import uz.bdm.HrTesting.domain.Recruiter;
import uz.bdm.HrTesting.domain.Role;
import uz.bdm.HrTesting.dto.ResponseData;
import uz.bdm.HrTesting.ropository.RoleRepository;
import uz.bdm.HrTesting.service.RoleService;

import java.util.Arrays;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public ResponseData getRolesList() {
        ResponseData result = new ResponseData();

        result.setAccept(true);
        result.setData(Arrays.asList(
                AuthoritiesConstants.ADMIN,
                AuthoritiesConstants.Moderator
        ));

        return result;
    }


    @Override
    public ResponseData findByName(String name) {
        ResponseData result = new ResponseData();

        Role role = roleRepository.findByName(name).orElse(null);
//TODO message to'g'irlash kere
        if (role == null) {
            result.setAccept(false);
            result.setMessage("Role не найден Name = " + name);
        } else {
            result.setAccept(true);
            result.setData(role);
        }
        return result;
    }
}

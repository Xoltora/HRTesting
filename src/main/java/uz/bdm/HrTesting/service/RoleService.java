package uz.bdm.HrTesting.service;

import uz.bdm.HrTesting.dto.ResponseData;

public interface RoleService {

    ResponseData getRolesList();

    ResponseData findByName(String name);
}

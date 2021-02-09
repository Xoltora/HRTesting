package uz.bdm.HrTesting.service.Impl;

import org.springframework.stereotype.Service;
import uz.bdm.HrTesting.domain.Recruiter;
import uz.bdm.HrTesting.dto.RecruiterDto;
import uz.bdm.HrTesting.dto.ResponseData;
import uz.bdm.HrTesting.ropository.RecruiterRepository;
import uz.bdm.HrTesting.service.RecruiterService;

import javax.transaction.Transactional;

@Service
public class RecruiterServiceImpl implements RecruiterService {

    private final RecruiterRepository recruiterRepository;

    public RecruiterServiceImpl(RecruiterRepository recruiterRepository) {
        this.recruiterRepository = recruiterRepository;
    }

    @Override
    @Transactional
    public ResponseData save(RecruiterDto recruiterDto) {
        ResponseData result = new ResponseData();

        Recruiter recruiter = recruiterDto.mapToEntity();

        try {
            Recruiter save = recruiterRepository.save(recruiter);
            result.setAccept(true);
            result.setMessage("Рекрутер успешно создан !");
            result.setData(save.mapToDto());
        } catch (Exception e) {
            e.printStackTrace();
            result.setAccept(false);
            result.setMessage("Error saving data");
        }

        return result;
    }

    @Override
    public ResponseData update(RecruiterDto recruiterDto) {

        ResponseData result = new ResponseData();

        Recruiter recruiter = recruiterRepository.findById(recruiterDto.getId()).orElse(null);

        if (recruiter == null) {
            result.setAccept(false);
            result.setMessage("Рекрутер не найден ID = " + recruiterDto.getId());
            return result;
        }

        try {

            recruiterDto.setCreated(recruiter.getCreated());
            Recruiter save = recruiterRepository.save(recruiterDto.mapToEntity());

            result.setAccept(true);
            result.setMessage("Рекрутер успешно обновлен !");
            result.setData(save.mapToDto());
        } catch (Exception e) {
            e.printStackTrace();
            result.setAccept(false);
            result.setMessage("Error saving data");
        }

        return result;
    }

    @Override
    public ResponseData findById(Long id) {
        ResponseData result = new ResponseData();

        Recruiter recruiter = recruiterRepository.findById(id).orElse(null);

        if (recruiter == null) {
            result.setAccept(false);
            result.setMessage("Рекрутер не найден ID = " + id);
        } else {
            result.setAccept(true);
            result.setData(recruiter.mapToDto());
        }
        return result;
    }

    @Override
//    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public ResponseData deleteById(Long id) {
        ResponseData result = new ResponseData();

        try {
            recruiterRepository.deleteById(id);
            result.setAccept(true);
            result.setMessage("Рекрутер успешно удалён !");
            result.setData(id);

        } catch (Exception e) {
            e.printStackTrace();
            result.setAccept(false);
            result.setMessage("Error delete data");
        }

        return result;
    }

    @Override
    public ResponseData findAll() {
        ResponseData result = new ResponseData();

        try {
            result.setAccept(true);
            result.setData(recruiterRepository.findAll());
        } catch (Exception e) {
            e.printStackTrace();
            result.setAccept(false);
            result.setMessage("Error get list ");
        }

        return result;
    }
}

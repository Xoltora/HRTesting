package uz.bdm.HrTesting.bootstrap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import uz.bdm.HrTesting.constants.AuthoritiesConstants;
import uz.bdm.HrTesting.domain.*;
import uz.bdm.HrTesting.enums.AnswerType;
import uz.bdm.HrTesting.ropository.*;


import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {

    @Value("${spring.datasource.initialization-mode}")
    private String initialMode;

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final DepartmentRepository departmentRepository;
    private final RecruiterRepository recruiterRepository;
    private final TestRepository testRepository;
    private final TestSettingRepository testSettingRepository;
    private final QuestionRepository questionRepository;
    private final SelectableAnswerRepository selectableAnswerRepository;

    public DataLoader(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder, DepartmentRepository departmentRepository, RecruiterRepository recruiterRepository, TestRepository testRepository, TestSettingRepository testSettingRepository, QuestionRepository questionRepository, SelectableAnswerRepository selectableAnswerRepository) {
        this.roleRepository = roleRepository;

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.departmentRepository = departmentRepository;
        this.recruiterRepository = recruiterRepository;
        this.testRepository = testRepository;
        this.testSettingRepository = testSettingRepository;
        this.questionRepository = questionRepository;
        this.selectableAnswerRepository = selectableAnswerRepository;
    }

    @Override
    public void run(String... args) {
        if (initialMode.equals("always")) {
            List<Role> roles = roleRepository.saveAll(Arrays.asList(
                    new Role(AuthoritiesConstants.ADMIN),
                    new Role(AuthoritiesConstants.Moderator),
                    new Role(AuthoritiesConstants.CANDIDATE)
            ));

            userRepository.save(
                    new User("Begzod",
                            passwordEncoder.encode("Begzod"),
                            "Baratov Begzod",
                            true,
                            new HashSet<>(Arrays.asList(roles.get(0), roles.get(1))),
                            false)

            );

            Department saveDepartment = departmentRepository.save(new Department(
                    "Yurdik"
            ));

            Recruiter saveRecruiter = recruiterRepository.save(new Recruiter(
                    "A A A"
            ));

            Test test = new  Test();
            test.setName("Test1");
            test.setDepartment(saveDepartment);
            test.setVersion(1);
            Test saveTest1 = testRepository.save(test);

            TestSetting testSetting = new TestSetting();
            testSetting.setTime(10);
            testSetting.setTest(saveTest1);
            testSetting.setTitle("Test title ");
            testSetting.setDescription("Test description description description description description description");
            testSetting.setNumberOfAttempts(1);

            TestSetting saveTestSetting = testSettingRepository.save(testSetting);


            Question question = new Question();
            question.setText("5+5");
            question.setImageName("1m8rie6aprd8si2ckdtl3nj0jpeg");
            question.setImagePath("/2021/2");
            question.setAnswerType(AnswerType.ONE_CORRECT);
            question.setTest(saveTest1);

            Question saveQuestion = questionRepository.save(question);

            selectableAnswerRepository.saveAll(
                    Arrays.asList(
                    new SelectableAnswer(saveQuestion,"9",false,false),
                    new SelectableAnswer(saveQuestion,"10",true,false),
                    new SelectableAnswer(saveQuestion,"11",false,false)
                    ));


            Test test1 = new  Test();
            test1.setName("Test1");
            test1.setDepartment(saveDepartment);
            test1.setVersion(1);
            Test saveTest2 = testRepository.save(test1);

            TestSetting testSetting1 = new TestSetting();
            testSetting1.setTime(10);
            testSetting1.setTest(saveTest2);
            testSetting1.setTitle("Test title ");
            testSetting1.setDescription("Test description description description description description description");
            testSetting1.setNumberOfAttempts(1);

            TestSetting saveTestSetting1 = testSettingRepository.save(testSetting1);


            Question question1 = new Question();
            question1.setText("10+10");
            question1.setAnswerType(AnswerType.MULTIPLE_CORRECT);
            question1.setTest(saveTest2);

            Question saveQuestion1 = questionRepository.save(question1);

            selectableAnswerRepository.saveAll(
                    Arrays.asList(
                            new SelectableAnswer(saveQuestion1,"20",true,false),
                            new SelectableAnswer(saveQuestion1,"10",false,false),
                            new SelectableAnswer(saveQuestion1,"11+9",true,false)
                    ));



        }

    }
}

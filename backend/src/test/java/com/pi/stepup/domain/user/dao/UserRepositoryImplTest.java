package com.pi.stepup.domain.user.dao;

import static org.assertj.core.api.Assertions.assertThat;

import com.pi.stepup.domain.user.domain.Country;
import com.pi.stepup.domain.user.domain.User;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class UserRepositoryImplTest {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private UserRepository userRepository;

    private final String[] testCountryCodes = new String[]{"en", "ko", "jp", "ch"};
    private final String TEST_EMAIL = "test@test.com";
    private final String TEST_ID = "testId";
    private final String TEST_NICKNAME = "testNickname";
    private final LocalDate TEST_BIRTH = LocalDate.now();

    private User sampleUser;
    private List<Country> sampleCountries;

    @BeforeEach
    void setCountriesAndUserData() {
        em.createQuery("DELETE FROM Country c").executeUpdate();
        em.createQuery("DELETE FROM User u").executeUpdate();
        setCountriesData();
        setUserData();
    }

    @DisplayName("국가 정보 목록을 조회했을 때 전체 데이터가 반환된다.")
    @Test
    void findAllCountriesTest() {
        // when
        List<Country> findCountries = userRepository.findAllCountries();

        // then
        assertThat(findCountries.size()).isEqualTo(sampleCountries.size());
        for (int i = 0; i < sampleCountries.size(); i++) {
            assertThat(findCountries.get(i)).isEqualTo(sampleCountries.get(i));
        }
    }

    @DisplayName("국가 정보 pk로 조회했을 때 해당 데이터가 반환된다.")
    @Test
    void findOneCountryTest() {
        // given
        int zeroIndex = 0;

        // when, then
        assertThat(userRepository.findOneCountry(sampleCountries.get(zeroIndex).getCountryId()))
            .isEqualTo(sampleCountries.get(zeroIndex));
    }

    @DisplayName("이메일 기준으로 조회했을 때 해당 데이터가 반환된다.")
    @Test
    void findByEmailTest() {
        // when
        User findUser = userRepository.findByEmail(TEST_EMAIL).orElse(User.builder().build());

        // then
        assertThat(findUser).isEqualTo(sampleUser);
    }

    @DisplayName("닉네임 기준으로 조회했을 때 해당 데이터가 반환된다.")
    @Test
    void findByNicknameTest() {
        // when
        User findUser = userRepository.findByNickname(TEST_NICKNAME).orElse(User.builder().build());

        //then
        assertThat(findUser).isEqualTo(sampleUser);
    }

    @DisplayName("아이디 기준으로 조회했을 때 해당 데이터가 반환된다.")
    @Test
    void findByIdTest() {
        // when
        User findUser = userRepository.findById(TEST_ID).orElse(User.builder().build());

        // then
        assertThat(findUser).isEqualTo(sampleUser);
    }

    @DisplayName("사용자가 삭제될 경우, DB에서 조회했을 때 null이 반환된다.")
    @Test
    void deleteTest() {
        // given
        Long userId = sampleUser.getUserId();

        // when
        userRepository.delete(sampleUser);

        // then
        assertThat(em.find(User.class, userId)).isNull();
    }

    @DisplayName("사용자 정보를 삽입할 경우 pk로 조회했을 때 동일한 데이터가 조회된다.")
    @Test
    void insertTest() {
        // given
        int countryIndex = 0;
        User user = User.builder()
            .id(TEST_ID)
            .email(TEST_EMAIL)
            .country(sampleCountries.get(countryIndex))
            .nickname(TEST_NICKNAME)
            .birth(TEST_BIRTH)
            .point(0)
            .build();

        // when
        userRepository.insert(user);

        // then
        assertThat(user).isEqualTo(em.find(User.class, user.getUserId()));
    }

    @DisplayName("이메일과 생일 기준 조회에 성공한다.")
    @Test
    void findByEmailAndBirthTest() {
        // when
        User findUser = userRepository.findByEmailAndBirth(TEST_EMAIL, TEST_BIRTH).get();

        // then
        assertThat(findUser).isEqualTo(sampleUser);
    }

    @DisplayName("아이디와 이메일 기준 조회에 성공한다.")
    @Test
    void findByIdAndEmailTest() {
        // when
        User findUser = userRepository.findByIdAndEmail(TEST_ID, TEST_EMAIL).get();

        // then
        assertThat(findUser).isEqualTo(sampleUser);
    }

    private void setCountriesData() {
        sampleCountries = Arrays.stream(testCountryCodes)
            .map(c -> Country.builder().code(c).build())
            .collect(Collectors.toList());

        sampleCountries.forEach(em::persist);
    }

    private void setUserData() {
        sampleUser = User.builder()
            .id(TEST_ID)
            .email(TEST_EMAIL)
            .country(sampleCountries.get(0))
            .nickname(TEST_NICKNAME)
            .birth(TEST_BIRTH)
            .point(0)
            .build();
        em.persist(sampleUser);
    }
}
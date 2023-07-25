package com.pi.stepup.domain.user.dao;

import static org.assertj.core.api.Assertions.assertThat;

import com.pi.stepup.domain.user.domain.Country;
import com.pi.stepup.domain.user.domain.User;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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


    @DisplayName("국가 정보 목록을 조회했을 때 전체 데이터가 반환된다.")
    @Test
    void findAllCountriesTest() {
        // given : 국가 목록 생성
        List<Country> countries = makeSampleCountries();
        insertCountries(countries);
        int numOfCountries = 4;

        // when
        List<Country> findCountries = userRepository.findAllCountries();

        // then
        assertThat(findCountries.size()).isEqualTo(numOfCountries);
        for (int i = 0; i < countries.size(); i++) {
            assertThat(findCountries.get(i)).isEqualTo(countries.get(i));
        }
    }

    @DisplayName("국가 정보 pk로 조회했을 때 해당 데이터가 반환된다.")
    @Test
    void findOneCountryTest() {
        // given
        Country country = Country.builder()
            .code("ko")
            .build();

        em.persist(country);

        // when
        Country findCountry = userRepository.findOneCountry(country.getCountryId());

        // then
        assertThat(findCountry).isEqualTo(country);
    }

    @DisplayName("이메일 기준으로 조회했을 때 해당 데이터가 반환된다.")
    @Test
    void findByEmailTest() {
        // given
        User user = setUserSample();

        // when
        User findUser = userRepository.findByEmail(TEST_EMAIL).orElse(User.builder().build());

        // then
        assertThat(findUser).isEqualTo(user);
    }

    @DisplayName("닉네임 기준으로 조회했을 때 해당 데이터가 반환된다.")
    @Test
    void findByNicknameTest() {
        // given
        User user = setUserSample();

        // when
        User findUser = userRepository.findByNickname(TEST_NICKNAME).orElse(User.builder().build());

        //then
        assertThat(user).isEqualTo(findUser);
    }

    @DisplayName("아이디 기준으로 조회했을 때 해당 데이터가 반환된다.")
    @Test
    void findByIdTest() {
        // given
        User user = setUserSample();

        // when
        User findUser = userRepository.findById(TEST_ID).orElse(User.builder().build());

        // then
        assertThat(findUser).isEqualTo(user);
    }

    private User setUserSample() {
        Country country = Country.builder()
            .code(testCountryCodes[0])
            .build();
        em.persist(country);

        User user = User.builder()
            .id(TEST_ID)
            .email(TEST_EMAIL)
            .country(country)
            .nickname(TEST_NICKNAME)
            .point(0)
            .build();
        em.persist(user);

        return user;
    }

    private List<Country> makeSampleCountries() {
        return Arrays.stream(testCountryCodes)
            .map(c -> Country.builder().code(c).build())
            .collect(Collectors.toList());
    }

    private void insertCountries(List<Country> countries) {
        countries.forEach(em::persist);
    }
}
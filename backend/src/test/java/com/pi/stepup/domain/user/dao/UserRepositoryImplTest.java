package com.pi.stepup.domain.user.dao;

import static org.assertj.core.api.Assertions.assertThat;

import com.pi.stepup.domain.user.domain.Country;
import com.pi.stepup.domain.user.domain.User;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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

    @DisplayName("국가 정보 목록 조회 테스트")
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

    @DisplayName("이메일 기준 조회 테스트")
    @Test
    void findByEmail() {
        // given
        String testCountryCodes = "ko";
        String testEmail = "test@test.com";

        Country country = Country.builder()
            .code(testCountryCodes)
            .build();
        em.persist(country);

        User user = User.builder()
            .email(testEmail)
            .country(country)
            .point(0)
            .build();
        em.persist(user);

        // when
        User findUser = userRepository.findByEmail(testEmail);

        // then
        assertThat(findUser).isEqualTo(user);
    }

    private List<Country> makeSampleCountries() {
        return new ArrayList<>(Arrays.asList(
            Country.builder().code("en").build(),
            Country.builder().code("ko").build(),
            Country.builder().code("jp").build(),
            Country.builder().code("ch").build()
        ));
    }

    private void insertCountries(List<Country> countries) {
        countries.forEach(em::persist);
    }
}
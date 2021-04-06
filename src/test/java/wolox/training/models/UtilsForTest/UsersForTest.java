package wolox.training.models.UtilsForTest;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import wolox.training.models.User;

public class UsersForTest {

    public List<User> usersList() {
        User user1 = new User();
        user1.setName("Alison");
        user1.setUsername("Alison");
        user1.setBirthdate(LocalDate.of(1997, 05, 23));
        user1.setPassword("1234");

        User user2 = new User();
        user2.setName("Harry");
        user2.setUsername("Harry");
        user2.setBirthdate(LocalDate.of(1980, 7, 31));
        user2.setPassword("expectoPatronum");

        User user3 = new User();
        user3.setName("Hermione");
        user3.setUsername("Hermione");
        user3.setBirthdate(LocalDate.of(1979, 9, 19));
        user3.setPassword("library");

        User user4 = new User();
        user4.setName("Ron");
        user4.setUsername("Ron");
        user4.setBirthdate(LocalDate.of(1980, 3, 1));
        user4.setPassword("Spiders");

        User user5 = new User();
        user5.setName("Luna");
        user5.setUsername("Luna");
        user5.setBirthdate(LocalDate.of(1981, 2, 13));
        user5.setPassword("Nargles");

        return Collections.unmodifiableList(Arrays.asList(user1, user2, user3, user4, user5));

    }

}

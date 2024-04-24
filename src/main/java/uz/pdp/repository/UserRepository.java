package uz.pdp.repository;

import com.google.gson.reflect.TypeToken;
import lombok.Getter;
import lombok.NonNull;
import uz.pdp.model.User;
import uz.pdp.service.FileHelper;
import uz.pdp.utils.FileUrls;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserRepository implements BaseRepository<User, Long> {
    @Getter
    private static final UserRepository instance = new UserRepository();
    private UserRepository() {}

    @Override
    public Boolean save(User user) {
        List<User> users = getAllUsersFromFile();
        users.add(user);
        setAllUsersFromFile(users);
        return Boolean.TRUE;
    }

    @Override
    public Boolean update(Long id, User oldUser) {
        List<User> users = getAllUsersFromFile();
        for (User user : users) {
            if (user.getChatId().equals(id)) {
                user.setUserState(oldUser.getUserState());
                setAllUsersFromFile(users);
                break;
            }
        }
        return true;
    }

    @Override
    public List<User> findAll() {
        return null;
    }

    @Override
    public Optional<User> findById(Long id) {
        List<User> users = getAllUsersFromFile();
        for (User user : users) {
            if (user.getChatId().equals(id))
                return Optional.of(user);
        }
        return Optional.empty();
    }

    @NonNull
    private List<User> getAllUsersFromFile() {
        List<User> users = FileHelper.load(FileUrls.USER_URL, new TypeToken<List<User>>(){}.getType());
        return users == null ? new ArrayList<>() : users;
    }
    private void setAllUsersFromFile(List<User> users) {
        FileHelper.write(FileUrls.USER_URL, users);
    }
}

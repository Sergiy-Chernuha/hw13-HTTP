package global.goit;

import global.goit.objectvalues.Post;
import global.goit.objectvalues.Todo;
import global.goit.objectvalues.User;

import java.io.IOException;
import java.util.List;

public class Main {


    public static void main(String[] args) throws IOException {
        final String URL = "https://jsonplaceholder.typicode.com/users";
        final User INITUSER = new User();
        final Post INITPOST = new Post();
        int userIndex;

//___________1.1
        User newUser = HTTPUtil.getObjectFromUrlByIndex(URL, 3, INITUSER);
        User user = HTTPUtil.createNewObject(URL, newUser);
        System.out.println(user);
//___________1.2
        String newJson = HTTPUtil.updateObject(URL, 5);
        System.out.println(newJson);
//___________1.3
        int deleteStatusCode = HTTPUtil.deleteObject(URL, 5);
        System.out.println(deleteStatusCode);
//___________1.4
        List<User> users = HTTPUtil.getListObjectsFromUrl(URL, INITUSER);
        System.out.println(users);
//___________1.5
        userIndex = 5;
        User objectFromUrlById = HTTPUtil.getObjectFromUrlByIndex(URL, userIndex, INITUSER);
        System.out.println(objectFromUrlById);
//___________1.6
        String userName = "Antonette";
        List<User> objectFromUrlByName = HTTPUtil.getObjectFromUrlByName(URL, userName, INITUSER);
        System.out.println(objectFromUrlByName);
//____________2.1
        userIndex = 10;
        HTTPUtil.createCommentsFileFromLastPostNUser(URL, userIndex, INITPOST);
//_____________2.2
        userIndex = 5;
        List<Todo> allOpenTodosForNUser = HTTPUtil.getAllOpenTodosForNUser(URL, userIndex);
        System.out.println(allOpenTodosForNUser);
    }
}

package global.goit;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import global.goit.objectvalues.Post;
import global.goit.objectvalues.Todo;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.*;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class HTTPUtil {

    public static <T> T createNewObject(String url, T inputObject) throws IOException {
        String newJson = new Gson().toJson(inputObject);

        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept", "application/json");
        connection.setDoOutput(true);

        OutputStream os = connection.getOutputStream();
        os.write(newJson.getBytes());
        os.flush();
        os.close();

        StringBuilder response = new StringBuilder();
        if (connection.getResponseCode() == HttpURLConnection.HTTP_CREATED) {
            Scanner scanner = new Scanner(connection.getInputStream());

            while (scanner.hasNext()) {
                response.append(scanner.nextLine());
            }

        } else {
            System.out.println("POST request not worked");
        }

        return (T) new Gson().fromJson(response.toString(), inputObject.getClass());
    }

    public static <T> String updateObject(String url, int index) throws IOException {
        String newJson = getJson(url + "/" + index);

        Connection.Response response = Jsoup.connect(url + "/" + index)
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .followRedirects(true)
                .ignoreHttpErrors(true)
                .ignoreContentType(true)
                .method(Connection.Method.PUT)
                .requestBody(newJson)
                .execute();

        return response.body();
    }

    public static int deleteObject(String url, int index) throws IOException {
        Connection.Response response = Jsoup.connect(url + "/" + index)
                .ignoreContentType(true)
                .method(Connection.Method.DELETE)
                .execute();

        return response.statusCode();
    }

    public static <T> List<T> getListObjectsFromUrl(String url, T parseToClass) throws IOException {
        String json = getJson(url);

        return parseJsonToListT(parseToClass, json);
    }

    public static <T> T getObjectFromUrlByIndex(String url, int index, T parseToClass) throws IOException {
        String json = getJson(url + "/" + index);

        return (T) new Gson().fromJson(json, parseToClass.getClass());
    }

    public static <T> List<T> getObjectFromUrlByName(String url, String name, T parseToClass) throws IOException {
        String json = getJson(url + "?username=" + name);

        return parseJsonToListT(parseToClass, json);
    }

    public static void createCommentsFileFromLastPostNUser(String url, int index, Post parseToClass) throws IOException {
        String postsJson = getJson(url + "/" + index + "/posts");
        List<Post> posts = parseJsonToListT(parseToClass, postsJson);
        int lostPostId = posts.get(posts.size()-1).getId();
        String commentsJson = getJson("https://jsonplaceholder.typicode.com/posts/" + lostPostId + "/comments");
        writeResult(commentsJson, index, lostPostId);
    }

    private static void writeResult(String result, int userId, int postNumber) throws IOException {
        FileWriter writer = new FileWriter("user-" + userId + "-post-" + postNumber + "-comments.json");
        writer.write(result);
        writer.flush();
        writer.close();
    }

    public static List<Todo> getAllOpenTodosForNUser(String url, int index) throws IOException {
        String todosJson = getJson(url + "/" + index + "/todos");
        List<Todo> posts = parseJsonToListT(new Todo(), todosJson);

        return posts.stream().filter(x -> !x.isCompleted()).collect(Collectors.toList());
    }

    private static String getJson(String url) throws IOException {
        return Jsoup.connect(url)
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .followRedirects(true)
                .ignoreHttpErrors(true)
                .ignoreContentType(true)
                .method(Connection.Method.GET)
                .execute()
                .body();
    }

    private static <T> List<T> parseJsonToListT(T parseToClass, String json) {
        Type type = TypeToken.getParameterized(List.class, parseToClass.getClass()).getType();

        return new Gson().fromJson(json, type);
    }
}
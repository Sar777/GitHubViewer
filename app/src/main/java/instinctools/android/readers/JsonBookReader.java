package instinctools.android.readers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import instinctools.android.Book;

/**
 * Created by orion on 16.12.16.
 */

public class JsonBookReader implements IReader<String, List<Book>> {
    @Override
    public List<Book> read(String json) {
        List<Book> objects = new ArrayList<>();
        try {
            JSONObject obj = new JSONObject(json);
            JSONArray dataArray = obj.getJSONArray("data");
            for (int i = 0; i < dataArray.length(); ++i) {
                Book book = new Book();
                book.setTitle(dataArray.getJSONObject(i).getString("title"));
                book.setDescription(dataArray.getJSONObject(i).getString("description"));
                book.setImage(dataArray.getJSONObject(i).getString("image_url"));
                objects.add(book);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return objects;
    }
}

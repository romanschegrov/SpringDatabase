package interfaces;

import java.util.List;

/**
 * Created by ramon on 10.10.2016.
 */
public interface MusicDao {
    void create();
    int insert(Music music);
    void insert(List<Music> list);
    void update(Music music);
    void delete(int id);
    Music getById(int id);
    List<Music> getByName(String name);
    List<Music> getByAuthor(String author);
}

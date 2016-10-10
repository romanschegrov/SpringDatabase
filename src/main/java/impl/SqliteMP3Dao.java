package impl;

import interfaces.Music;
import interfaces.MusicDao;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.List;

/**
 * Created by ramon on 10.10.2016.
 */
public class SqliteMP3Dao implements MusicDao {

    private JdbcTemplate jdbcTemplate;

    public SqliteMP3Dao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public void create() {
        jdbcTemplate.execute("CREATE TABLE mp3(" +
                "    id INTEGER PRIMARY KEY NOT NULL,\n" +
                "    name TEXT NOT NULL,\n" +
                "    author TEXT NOT NULL\n" +
                ");");
    }

    @Override
    public void insert(Music music) {
        String sql = "INSERT INTO mp3(name, author) VALUES (?, ?)";
        jdbcTemplate.update(sql, music.getName(), music.getAuthor());
    }

    @Override
    public void insert(List<Music> list) {
        list.forEach(m -> insert(m));
    }

    @Override
    public void update(Music music) {
        String sql = "UPDATE mp3 SET name = ?, author = ? WHERE id = ?";
        jdbcTemplate.update(sql, music.getName(), music.getAuthor(), music.getId());
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM mp3 WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public Music getById(int id) {
        return null; //jdbcTemplate.query("SELECT * FROM mp3 WHERE id = ?");
    }

    @Override
    public List<Music> getByName(String name) {
        return jdbcTemplate.queryForList("SELECT * FROM mp3 WHERE name LIKE %'?'%", new Object[]{name}, Music.class);
    }

    @Override
    public List<Music> getByAuthor(String author) {
        return null;
    }
}

package impl;

import interfaces.Music;
import interfaces.MusicDao;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ramon on 10.10.2016.
 */
public class SqliteMP3Dao implements MusicDao {

    private JdbcTemplate jdbcTemplate;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public SqliteMP3Dao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
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
    public int insert(Music music) {
        String sql = "INSERT INTO mp3(name, author) VALUES (:name, :author)";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("name", music.getName());
        params.addValue("author", music.getAuthor());

        KeyHolder keyHolder = new GeneratedKeyHolder();

        int update = namedParameterJdbcTemplate.update(sql, params, keyHolder);
        System.out.println("update = " + update);

        int value = keyHolder.getKey().intValue();
        System.out.println("value = " + value);
        return value;
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
        String sql = "SELECT * FROM mp3 WHERE id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);
        return namedParameterJdbcTemplate.queryForObject(sql, params, MP3.class);
    }

    @Override
    public List<Music> getByName(String name) {
        String sql = "SELECT * FROM mp3 WHERE name LIKE :name";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("name", "%" + name + "%");
        return namedParameterJdbcTemplate.query(sql, params, new MusicRowMapper());
    }

    @Override
    public List<Music> getByAuthor(String author) {
        String sql = "SELECT * FROM mp3 WHERE author LIKE :author";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("author", author);
        return namedParameterJdbcTemplate.query(sql, params, new MusicRowMapper());
    }

    public int getMP3Count(){
        String sql = "SELECT count(*) FROM mp3";
        return jdbcTemplate.queryForObject(sql,Integer.class);
    }

    public Map<String, Integer> getGroup(){
        String sql = "SELECT author, count(*) as count FROM mp3 GROUP BY author";
        return namedParameterJdbcTemplate.query(sql, new ResultSetExtractor<Map<String, Integer>>() {
            @Override
            public Map<String, Integer> extractData(ResultSet rs) throws SQLException, DataAccessException {
                Map<String, Integer> map = new HashMap<>();
                while (rs.next()){
                    map.put(rs.getString("author"), rs.getInt(2));
                }
                return map;
            }
        });
    }

    private class MusicRowMapper implements RowMapper<Music>{
        @Override
        public MP3 mapRow(ResultSet resultSet, int i) throws SQLException {
            MP3 mp3 = new MP3();
            mp3.setId(resultSet.getInt("id"));
            mp3.setName(resultSet.getString("name"));
            mp3.setAuthor(resultSet.getString("author"));
            return mp3;
        }
    }
}

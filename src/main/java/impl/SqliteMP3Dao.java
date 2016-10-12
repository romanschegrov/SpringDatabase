package impl;

import interfaces.Music;
import interfaces.MusicDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ramon on 10.10.2016.
 */
@Component("sqliteMP3Dao")
public class SqliteMP3Dao implements MusicDao {

    private JdbcTemplate jdbcTemplate;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private SimpleJdbcInsert jdbcInsert;
    private DataSource dataSource;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        this.jdbcInsert = new SimpleJdbcInsert(dataSource).withTableName("mp3").usingColumns("name","author");
    }

    //    public SqliteMP3Dao(DataSource dataSource) {
//        this.jdbcTemplate = new JdbcTemplate(dataSource);
//        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
//        this.jdbcInsert = new SimpleJdbcInsert(dataSource).withTableName("mp3").usingColumns("name","author");
//    }

    @Override
    public void create() {
        jdbcTemplate.execute("CREATE TABLE mp3(" +
                "    id INTEGER PRIMARY KEY NOT NULL,\n" +
                "    name TEXT NOT NULL,\n" +
                "    author TEXT NOT NULL\n" +
                ");");
    }

    @Override
    public void insertWithSimpleJdbcInsert(Music music){
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("name", music.getName());
        params.addValue("author", music.getAuthor());
        jdbcInsert.execute(params);
    }

    @Override
    @Transactional
    public int insert(Music music) {
        String sqlAuthor = "INSERT INTO author(name) VALUES (:author_name);";

        MapSqlParameterSource params = new MapSqlParameterSource("author_name", music.getAuthor().getName());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(sqlAuthor, params, keyHolder);
        int author_id = keyHolder.getKey().intValue();

        String sqlMP3 = "INSERT INTO mp3(name, author_id2) VALUES (:name, :author_id)";
        MapSqlParameterSource paramsAuthor = new MapSqlParameterSource();
        paramsAuthor.addValue("name", music.getName());
        paramsAuthor.addValue("author_id", author_id);

        return namedParameterJdbcTemplate.update(sqlMP3, paramsAuthor);
    }

    @Override
    public void insert(List<Music> list) {
        list.forEach(m -> insert(m));
    }

    @Override
    public int[] insertBatchWithSimpleJdbcInsert(List<Music> list){
        Map<String,Music> batch = new HashMap<>();
        for (Music music : list) batch.put(Integer.toString(music.getId()), music);
        int[] ints = jdbcInsert.executeBatch(batch);
        return ints;
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

    public Author getAuthorById(int id) {
        String sql = "SELECT * FROM author WHERE id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource("id", id);
        return namedParameterJdbcTemplate.queryForObject(sql, params, Author.class);
    }

    private class MusicRowMapper implements RowMapper<Music>{
        @Override
        public MP3 mapRow(ResultSet resultSet, int i) throws SQLException {
            MP3 mp3 = new MP3();
            mp3.setId(resultSet.getInt("id"));
            mp3.setName(resultSet.getString("name"));

            int author_id = resultSet.getInt("author_id");
            Author author = getAuthorById(author_id);
            mp3.setAuthor(author);
            return mp3;
        }
    }
}

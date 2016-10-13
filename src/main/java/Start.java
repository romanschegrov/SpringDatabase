import impl.Author;
import impl.MP3;
import impl.SqliteMP3Dao;
import impl.TestBean;
import interfaces.Music;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import java.util.*;

/**
 * Created by ramon on 10.10.2016.
 */
public class Start {

    static SqliteMP3Dao sqliteMP3Dao;

    public static void main(String[] args) {
        ConfigurableApplicationContext context = new GenericXmlApplicationContext("context.xml");
//        TestBean testBean = context.getBean("testBean", TestBean.class);
//        System.out.println(testBean);
        sqliteMP3Dao = context.getBean("sqliteMP3Dao",SqliteMP3Dao.class);
//        createMP3Table()
        insertOne();
//        insertList();
//        getByNameAndUpdateAuthor("Лондон");
//        System.out.println(Arrays.asList(insertBatchWithSimpleJdbcInsert()));
        context.close();
    }

    static void createMP3Table(){
        sqliteMP3Dao.create();
    }

    static void insertOne(){
        MP3 mp3 = new MP3();
        mp3.setName("Лондон");
        Author author = new Author();
        author.setName("Григорий Лепс");
        mp3.setAuthor(author);
        sqliteMP3Dao.insert(mp3);
    }

    static void insertList(){
        List<Music> list = new ArrayList<>();

        MP3 mp3 = new MP3();
        mp3.setName("Обернитесь");
        Author g = new Author();
        g.setName("Григорий Лепс");
        mp3.setAuthor(g);
        list.add(mp3);

        mp3 = new MP3();
        mp3.setName("Девченки");
        Author r = new Author();
        r.setName("Руки вверх!");
        mp3.setAuthor(r);
        list.add(mp3);

        sqliteMP3Dao.insert(list);
    }

    static void insertWithSimpleJdbcInsert(){
        MP3 mp3 = new MP3();
        mp3.setName("Хороший день");
        Author v = new Author();
        v.setName("Вера Брежнева");
        mp3.setAuthor(v);
        sqliteMP3Dao.insertWithSimpleJdbcInsert(mp3);
    }

    static int[] insertBatchWithSimpleJdbcInsert(){
        List<Music> list = new ArrayList<>();

        MP3 mp3 = new MP3();
        mp3.setName("Обернитесь");
        Author g = new Author();
        g.setName("Григорий Лепс");
        mp3.setAuthor(g);
        list.add(mp3);

        mp3 = new MP3();
        mp3.setName("Девченки");
        Author r = new Author();
        r.setName("Руки вверх!");
        mp3.setAuthor(r);
        list.add(mp3);

        return sqliteMP3Dao.insertBatchWithSimpleJdbcInsert(list);
    }

    static void getByNameAndUpdateAuthor(String name){
        List<Music> list = sqliteMP3Dao.getByName(name);
        for (Music music : list){
            MP3 mp3 = new MP3();
            mp3.setId(music.getId());
            mp3.setName(music.getName());

            Author author = music.getAuthor();
            author.setName(author.getName() + "!");
            mp3.setAuthor(author);
            sqliteMP3Dao.update(mp3);
        }
    }
}

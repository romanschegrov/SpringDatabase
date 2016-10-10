import impl.MP3;
import impl.SqliteMP3Dao;
import interfaces.Music;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ramon on 10.10.2016.
 */
public class Start {
    public static void main(String[] args) {
        try (ConfigurableApplicationContext context = new GenericXmlApplicationContext("context.xml")) {
            SqliteMP3Dao sqliteMP3Dao = context.getBean("sqliteMP3Dao", SqliteMP3Dao.class);

//            sqliteMP3Dao.create();

            MP3 mp3 = null;
//            mp3 = new MP3();
//            mp3.setName("Григорий Лепс Трек№222");
//            mp3.setAuthor("Григорий Лепс");
//
//            sqliteMP3Dao.insert(mp3);
//
//            List<Music> list = new ArrayList<>();
//
//            mp3 = new MP3();
//            mp3.setName("Григорий Лепс Трек№2");
//            mp3.setAuthor("Григорий Лепс");
//            list.add(mp3);
//
//            mp3 = new MP3();
//            mp3.setName("Руки вверх! Трек№2");
//            mp3.setAuthor("Руки вверх!");
//            list.add(mp3);
//
//            sqliteMP3Dao.insert(list);

            mp3 = new MP3();
            mp3.setName("Григорий Лепс Трек№222");
            mp3.setAuthor("Григорий Лепс!!!");
            sqliteMP3Dao.update(mp3);
        }
    }
}

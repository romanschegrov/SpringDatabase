import impl.MP3;
import impl.SqliteMP3Dao;
import interfaces.Music;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by ramon on 10.10.2016.
 */
public class Start {
    public static void main(String[] args) {
        try (ConfigurableApplicationContext context = new GenericXmlApplicationContext("context.xml")) {
            SqliteMP3Dao sqliteMP3Dao = context.getBean("sqliteMP3Dao", SqliteMP3Dao.class);

//            sqliteMP3Dao.create();

//            MP3 mp3 = null;

//            mp3 = new MP3();
//            mp3.setName("Григорий Лепс Трек№222");
//            mp3.setAuthor("Григорий Лепс");
//
//            System.out.println(sqliteMP3Dao.insert(mp3));

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

//            getByNameAndUpdateAuthor(sqliteMP3Dao, "Трек№2");
//            sqliteMP3Dao.delete(942891);
//            System.out.println(sqliteMP3Dao.getMP3Count());

            Map<String, Integer> map = sqliteMP3Dao.getGroup();
            for (Map.Entry<String, Integer> entry : map.entrySet()){
                System.out.println(entry.getKey() + " - " + entry.getValue());
            }
        }
    }

    static void getByNameAndUpdateAuthor(SqliteMP3Dao dao, String name){
        List<Music> list = dao.getByName(name);
        for (Music music : list){
            MP3 mp3 = new MP3();
            mp3.setId(music.getId());
            mp3.setName(music.getName());
            mp3.setAuthor(music.getAuthor() + "!");
            dao.update(mp3);
        }
    }

    static void getByIdAndDelete(SqliteMP3Dao dao, int id){}
}

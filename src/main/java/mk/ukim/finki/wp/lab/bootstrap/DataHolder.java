package mk.ukim.finki.wp.lab.bootstrap;

import jakarta.annotation.PostConstruct;
import mk.ukim.finki.wp.lab.model.Album;
import mk.ukim.finki.wp.lab.model.Artist;
import mk.ukim.finki.wp.lab.model.Song;
import mk.ukim.finki.wp.lab.model.User;
import mk.ukim.finki.wp.lab.repository.jpa.AlbumRepository;
import org.springframework.stereotype.Component;
import mk.ukim.finki.wp.lab.repository.jpa.UserRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class DataHolder {

    public static List<Artist> lista_artisti = new ArrayList<>();
    public static List<Song> lista_pesni = new ArrayList<>();

    public static List<User> users = null;

    public static List<Album> lista_albumi = new ArrayList<>();

    private final AlbumRepository albumRepository;
    private final UserRepository userRepository;

    public DataHolder(AlbumRepository albumRepository, UserRepository userRepository) {
        this.albumRepository = albumRepository;
        this.userRepository = userRepository;
    }

    @PostConstruct
    public void init() {
        lista_artisti.add(new Artist (11L, "Marshall", "Mathers", "Last Project: 2024"));
        lista_artisti.add(new Artist (135L, "Lil", "Uzi", "Last Project: 2024"));
        lista_artisti.add(new Artist (119L, "Don", "Toliver", "Last Project: 2024"));
        lista_artisti.add(new Artist (999L, "Roddy", "Ricch", "Last Project: 2023"));
        lista_artisti.add(new Artist (16L, "Travis", "Scott", "Last Project: 2023"));

        lista_albumi.add(new Album(10L, "Taylor", "rap", "2010"));
        lista_albumi.add(new Album(20L, "Taylor1", "rap", "2010"));
        lista_albumi.add(new Album(30L, "Taylor2", "rap", "2010"));
        lista_albumi.add(new Album(40L, "Taylor3", "rap", "2010"));
        lista_albumi.add(new Album(50L, "Taylor4", "rap", "2010"));

        lista_pesni.add(new Song("011", "Stan", "Rap", 2000, lista_albumi.get(0)));
        lista_pesni.add(new Song("135", "Patience", "RnB", 2023, lista_albumi.get(1)));
        lista_pesni.add(new Song("119", "4x4", "RnB", 2024, lista_albumi.get(2)));
        lista_pesni.add(new Song("999", "The Box", "Hip Hop", 2019, lista_albumi.get(3)));
        lista_pesni.add(new Song("016", "I KNOW?", "Hip Hop", 2023, lista_albumi.get(4)));

        if (albumRepository.count() == 0) {
            // Create albums (or you can fetch these from a list)
            Album album1 = new Album(10L, "Taylor", "rap", "2010");
            Album album2 = new Album(20L, "Taylor1", "rap", "2010");
            Album album3 = new Album(30L, "Taylor2", "rap", "2010");
            Album album4 = new Album(40L, "Taylor3", "rap", "2010");
            Album album5 = new Album(50L, "Taylor4", "rap", "2010");

            // Save albums to the database
            albumRepository.saveAll(Arrays.asList(album1, album2, album3, album4, album5));
            //System.out.println("Albums added to the database!");
        }

        users = new ArrayList<>();
        if (this.userRepository.count() == 0) {
            users.add(new User("jana.stoichkova", "ea", "Jana", "Stoichkova"));
            this.userRepository.saveAll(users);
        }

    }

}

package mk.ukim.finki.wp.lab.web.controller;

import mk.ukim.finki.wp.lab.bootstrap.DataHolder;
import mk.ukim.finki.wp.lab.model.Album;
import mk.ukim.finki.wp.lab.model.Song;
import mk.ukim.finki.wp.lab.service.SongService;
import mk.ukim.finki.wp.lab.service.AlbumService;
import mk.ukim.finki.wp.lab.repository.jpa.SongRepository;
import mk.ukim.finki.wp.lab.repository.jpa.AlbumRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;



@Controller
public class SongController {

    private final SongService songService;
    private final AlbumService albumService;
    private final SongRepository songRepository;
    private final AlbumRepository albumRepository;

    public SongController(SongService songService, AlbumService albumService, SongRepository songRepository, AlbumRepository albumRepository) {
        this.songService = songService;
        this.albumService = albumService;
        this.songRepository = songRepository;
        this.albumRepository = albumRepository;
    }

    //Display all songs (listSongs.html)
    @GetMapping("/songs")
    public String getSongsPage(Model model){
        List<Song> songList = songService.listSongs();
        List<Album> albumList = albumRepository.findAll();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));

        boolean isUser = authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_USER"));

        model.addAttribute("songList", songList);
        model.addAttribute("albumList", albumList);
        model.addAttribute("bodyContent", "listSongs");
        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("isUser", isUser || isAdmin);

        return "master-template";  // Display the list of songs
    }

    @GetMapping("/songs/add")
    public String saveSong(Model model) {

        Song song = new Song();
        model.addAttribute("song", song);
        model.addAttribute("albumList", albumRepository.findAll());
        model.addAttribute("bodyContent", "add-song");

        return "master-template";
    }

    //Add a new song
    @PostMapping("/songs/add")
    public String saveSong(@RequestParam String title,
                           @RequestParam String trackId,
                           @RequestParam String genre,
                           @RequestParam Integer releaseYear,
                           @RequestParam Long album_id) {


        Album album = albumRepository.findById(album_id).orElse(null);

        if (album != null) {
            Song song = new Song(trackId, title, genre, releaseYear, album);

            songRepository.save(song);  // This will persist the song in the database
            return "redirect:/songs";
        }

        return "redirect:/songs";
    }

    // Edit song details (show prefilled form for editing)
    @GetMapping("/songs/edit/{songId}")
    public String editSongForm(@PathVariable Long songId, Model model){

        Song song = songRepository.findById(songId).orElse(null);

        if (song != null) {
            model.addAttribute("song", song);
            model.addAttribute("albumList", albumRepository.findAll());
            model.addAttribute("bodyContent", "add-song");

            return "master-template";
        }

        return "redirect:/songs";
    }

    //Update the song after editing
    @PostMapping("/songs/edit/{songId}")
    public String updateSong(@PathVariable Long songId,
                             @RequestParam String title,
                             @RequestParam String trackId,
                             @RequestParam String genre,
                             @RequestParam Integer releaseYear,
                             @RequestParam Long album_id) {

        Song song = songRepository.findById(songId).orElse(null);

        if (song != null) {
            // Fetch the album from the database using album_id
            Album album = albumRepository.findById(album_id).orElse(null);

            if (album != null) {
                // Update the song details
                song.setTitle(title);
                song.setTrackId(trackId);
                song.setGenre(genre);
                song.setReleaseYear(releaseYear);
                song.setAlbum(album);

                songRepository.save(song);  // Save the updated song
                return "redirect:/songs";  // Redirect to the list of songs
            }
        }

        return "redirect:/songs";  // Redirect to the list of songs
    }

    // Delete a song
    @GetMapping("/songs/delete/{id}")
    public String deleteSong(@PathVariable Long id){
        //DataHolder.lista_pesni.removeIf(s -> s.getId().equals(id));
        songRepository.deleteById(id);
        return "redirect:/songs";
    }

    // Add song page (for adding a new song, you can display a form here)
    @GetMapping("/songs/add-form")
    public String getAddSongPage(Model model){

        model.addAttribute("albumList", albumRepository.findAll());
        model.addAttribute("bodyContent", "add-song");

        return "master-template";
    }
}

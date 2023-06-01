package com.driver;

import java.util.*;

import org.springframework.stereotype.Service;

@Service
public class SpotifyService {

    //Auto-wire will not work in this case, no need to change this and add autowire

    SpotifyRepository dao = new SpotifyRepository();

    public User createUser(String name, String mobile){

        return dao.createUser(name, mobile);

    }

    public Artist createArtist(String name) {
        return dao.createArtist(name);

    }

    public Album createAlbum(String title, String artistName) {

       return dao.createAlbum(title, artistName);

    }

    public Song createSong(String title, String albumName, int length) throws Exception {

        return dao.createSong(title,albumName,length);

    }

    public Playlist createPlaylistOnLength(String mobile, String title, int length) throws Exception {
       return dao.createPlaylistOnLength(mobile,title,length);

    }

    public Playlist createPlaylistOnName(String mobile, String title, List<String> songTitles) throws Exception {
        return dao.createPlaylistOnName(mobile,title,songTitles);

    }

    public Playlist findPlaylist(String mobile, String playlistTitle) throws Exception {
        return dao.findPlaylist(mobile,playlistTitle);

    }

    public Song likeSong(String mobile, String songTitle) throws Exception {
        return dao.likeSong(mobile,songTitle);

    }

    public String mostPopularArtist() {
        return dao.mostPopularArtist();

    }

    public String mostPopularSong() {
        return dao.mostPopularSong();

    }
}

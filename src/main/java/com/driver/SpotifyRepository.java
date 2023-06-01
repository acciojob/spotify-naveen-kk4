package com.driver;

import java.util.*;

import org.springframework.stereotype.Repository;

@Repository
public class SpotifyRepository {
    public HashMap<Artist, List<Album>> artistAlbumMap;
    public HashMap<Album, List<Song>> albumSongMap;
    public HashMap<Playlist, List<Song>> playlistSongMap;
    public HashMap<Playlist, List<User>> playlistListenerMap;
    public HashMap<User, Playlist> creatorPlaylistMap;
    public HashMap<User, List<Playlist>> userPlaylistMap;
    public HashMap<Song, List<User>> songLikeMap;

    public List<User> users;
    public List<Song> songs;
    public List<Playlist> playlists;
    public List<Album> albums;
    public List<Artist> artists;

    public SpotifyRepository(){
        //To avoid hitting apis multiple times, initialize all the hashmaps here with some dummy data
        artistAlbumMap = new HashMap<>();
        albumSongMap = new HashMap<>();
        playlistSongMap = new HashMap<>();
        playlistListenerMap = new HashMap<>();
        creatorPlaylistMap = new HashMap<>();
        userPlaylistMap = new HashMap<>();
        songLikeMap = new HashMap<>();

        users = new ArrayList<>();
        songs = new ArrayList<>();
        playlists = new ArrayList<>();
        albums = new ArrayList<>();
        artists = new ArrayList<>();
    }

    public User createUser(String name, String mobile) {
        User user = new User(name,mobile);
        users.add(user);
        return user;
    }

    public Artist createArtist(String name) {
        Artist artist = new Artist(name);
        artists.add(artist);
        return artist;
    }

    public Album createAlbum(String title, String artistName) {
        Artist temp = null;
        for(Artist artist : artists){
            if(artist.getName().equals(artistName)) {
                temp = artist;
                break;
            }
        }
        if(Objects.isNull(temp))temp=this.createArtist(artistName);
        Album album = new Album(title);
        albums.add(album);
        List<Album> albumss = artistAlbumMap.getOrDefault(temp,new ArrayList<>());
        albumss.add(album);
        artistAlbumMap.put(temp,albumss);
        return album;
    }

    public Song createSong(String title, String albumName, int length) throws Exception{
        Album temp = null;
        boolean found = false;
        for(Album album : albums){
            if(album.getTitle().equals(albumName)){
                temp = album;
                found = true;
                break;
            }
        }
        throw new Exception("Album does not Exist");
        /*Song song = new Song(title,length);
        songs.add(song);
        List<Song> songss = albumSongMap.getOrDefault(temp,new ArrayList<>());
        songss.add(song);
        albumSongMap.put(temp,songss);
        return song;*/
    }

    public Playlist createPlaylistOnLength(String mobile, String title, int length) throws Exception {
        User temp = null;
        for(User user : users ){
            if(user.getMobile().equals(mobile)){
                temp=user;
                break;
            }
        }
        if(Objects.isNull(temp))throw new Exception("User does not exist");
        Playlist playlist = new Playlist(title);
        playlists.add(playlist);
        creatorPlaylistMap.put(temp,playlist);
        List<Playlist> playlistss = userPlaylistMap.getOrDefault(temp,new ArrayList<>());
        playlistss.add(playlist);
        userPlaylistMap.put(temp,playlistss);
        playlistListenerMap.put(playlist,List.of(temp)) ;
        for (Song song : songs){
            if(song.getLength()==length){
                List<Song> songss = playlistSongMap.getOrDefault(playlist,new ArrayList<>());
                songss.add(song);
                playlistSongMap.put(playlist,songss);
            }
        }
        return playlist;


    }

    public Playlist createPlaylistOnName(String mobile, String title, List<String> songTitles) throws Exception {
        User temp = null;
        for(User user : users ){
            if(user.getMobile().equals(mobile)){
                temp=user;
                break;
            }
        }
        if(Objects.isNull(temp))throw new Exception("User does not exist");
        Playlist playlist = new Playlist(title);
        playlists.add(playlist);
        creatorPlaylistMap.put(temp,playlist);
        List<Playlist> playlistss = userPlaylistMap.getOrDefault(temp,new ArrayList<>());
        playlistss.add(playlist);
        userPlaylistMap.put(temp,playlistss);
        List<User> userss = new ArrayList<>();
        userss.add(temp);
        playlistListenerMap.put(playlist,userss) ;
        for (Song song : songs){
            if(songTitles.contains(song.getTitle())){
                List<Song> songss = playlistSongMap.getOrDefault(playlist,new ArrayList<>());
                songss.add(song);
                playlistSongMap.put(playlist,songss);
            }
        }
        return playlist;

    }

    public Playlist findPlaylist(String mobile, String playlistTitle) throws Exception {
        Playlist temp1 = null;
        User temp2 = null;
        for(Playlist playlist : playlists){
            if(playlist.getTitle().equals(playlistTitle)){
                temp1 = playlist;
                break;
            }
        }
        for(User user : users){
            if(user.getMobile().equals(mobile)){
                temp2 = user;
                break;
            }
        }
        if(Objects.isNull(temp1))throw new Exception("Playlist does not exist");
        if(Objects.isNull(temp2))throw new Exception("User does not exist");


        if(creatorPlaylistMap.containsKey(temp2) && creatorPlaylistMap.get(temp2).equals(temp1))return temp1;
        if(playlistListenerMap.containsKey(temp1) && playlistListenerMap.get(temp1).contains(temp2))return temp1;
        List<User> users = playlistListenerMap.getOrDefault(temp1,new ArrayList<>());
        users.add(temp2);
        playlistListenerMap.put(temp1,users);
        List<Playlist> playlists = userPlaylistMap.getOrDefault(temp2,new ArrayList<>());
        playlists.add(temp1);
        userPlaylistMap.put(temp2,playlists);
        return temp1;


    }

    public Song likeSong(String mobile, String songTitle) throws Exception {
        User temp1 = null;
        Song temp2 = null;
        for(User user : users){
            if(user.getMobile().equals(mobile)){
                temp1 = user;
                break;
            }
        }
        for(Song song : songs){
            if(song.getTitle().equals(songTitle)){
                temp2 = song;
                break;
            }
        }
        if(Objects.isNull(temp1))throw new Exception("User does not exist");
        if(Objects.isNull(temp2))throw new Exception("Song does not exist");
        List<User> users = songLikeMap.getOrDefault(temp2,new ArrayList<>());
        for(User user : users){
            if(user.getMobile().equals(temp1.getMobile()) && user.getName().equals(temp1.getName()))return temp2;
        }
        users.add(temp1);
        songLikeMap.put(temp2,users);
        temp2.setLikes(temp2.getLikes()+1);

        List<Album> list = new ArrayList<>();
        for(Album album : albumSongMap.keySet()){
            if(albumSongMap.get(album).contains(temp2)){
                list.add(album);
            }
        }
        for(Album album : list){
            for(Artist artist : artistAlbumMap.keySet()){
                if(artistAlbumMap.get(artist).contains(album)){
                    artist.setLikes(artist.getLikes()+1);
                }
            }
        }
       return temp2;


    }

    public String mostPopularArtist() {
        int max = -1;
        String ans = null;
        for(Artist artist : artists){
            if(artist.getLikes()>max){
                max = artist.getLikes();
                ans = artist.getName();
            }
        }
        return ans;
    }

    public String mostPopularSong() {
        int max = -1;
        String ans = null;
        for(Song song : songs){
            if(song.getLikes()>max){
                max = song.getLikes();
                ans = song.getTitle();
            }
        }
        return ans;
    }


}

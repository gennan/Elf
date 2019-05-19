package com.example.elf.config;

/**
 * 接口
 */
public class Api {
    //更新 GET 返回版本号
    public static final String getLastVersion = "http://elf.egos.hosigus.com/getLastVersion.php";
    //心情歌单 GET 可选参数:HAPPY,UNHAPPY,CLAM,EXCITING
    //example：http://elf.egos.hosigus.com/getSongListID.php?type=EXCITING
    public static final String getSongListID = "http://elf.egos.hosigus.com/getSongListID.php?type=";
    //推荐歌单 GET 返回推荐歌单的id
    public static final String getRecommendID = "http://elf.egos.hosigus.com/getRecommendID.php";


    //歌单信息 参数：id
    public static final String getSongListDetail = "http://elf.egos.hosigus.com/music/playlist/detail?id=";
    //歌曲文件 将歌曲id写在前后缀之间即可
    public static final String getSongFilePr = "http://music.163.com/song/media/outer/url?id=";
    public static final String getSongFileSu = ".mp3";
    //获取歌词
    public static final String getLyric = "http://elf.egos.hosigus.com/music/lyric?id=";
}

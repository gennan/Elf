package com.example.elf.bean;

import java.util.List;

public class SongBean{

    /**
     * code : 200
     * playlist : {"tracks":[{"name":"歌曲名字","id":424060342,"ar":[{"id":0,"name":"作者名字"}],"al":{"id":3358040,"name":"所在专辑名字","picUrl":"http://p2.music.126.net/7VNhKpCYVz3UWAATV_HstA==/109951163828297300.jpg"}},"..."],"trackIds":[{"id":424060342},"..."],"coverImgUrl":"http://p1.music.126.net/05xtHVg_0G3fFWVjJciexA==/109951163897711683.jpg","description":"歌单描述...","name":"歌单名字"}
     */

    private int code;
    private PlaylistBean playlist;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public PlaylistBean getPlaylist() {
        return playlist;
    }

    public void setPlaylist(PlaylistBean playlist) {
        this.playlist = playlist;
    }

    public static class PlaylistBean {
        /**
         * tracks : [{"name":"歌曲名字","id":424060342,"ar":[{"id":0,"name":"作者名字"}],"al":{"id":3358040,"name":"所在专辑名字","picUrl":"http://p2.music.126.net/7VNhKpCYVz3UWAATV_HstA==/109951163828297300.jpg"}},"..."]
         * trackIds : [{"id":424060342},"..."]
         * coverImgUrl : http://p1.music.126.net/05xtHVg_0G3fFWVjJciexA==/109951163897711683.jpg
         * description : 歌单描述...
         * name : 歌单名字
         */

        private String coverImgUrl;
        private String description;
        private String name;
        private List<TracksBean> tracks;
        private List<TrackIdsBean> trackIds;

        public String getCoverImgUrl() {
            return coverImgUrl;
        }

        public void setCoverImgUrl(String coverImgUrl) {
            this.coverImgUrl = coverImgUrl;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<TracksBean> getTracks() {
            return tracks;
        }

        public void setTracks(List<TracksBean> tracks) {
            this.tracks = tracks;
        }

        public List<TrackIdsBean> getTrackIds() {
            return trackIds;
        }

        public void setTrackIds(List<TrackIdsBean> trackIds) {
            this.trackIds = trackIds;
        }

        public static class TracksBean {
            /**
             * name : 歌曲名字
             * id : 424060342
             * ar : [{"id":0,"name":"作者名字"}]
             * al : {"id":3358040,"name":"所在专辑名字","picUrl":"http://p2.music.126.net/7VNhKpCYVz3UWAATV_HstA==/109951163828297300.jpg"}
             */

            private String name;
            private int id;
            private AlBean al;
            private List<ArBean> ar;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public AlBean getAl() {
                return al;
            }

            public void setAl(AlBean al) {
                this.al = al;
            }

            public List<ArBean> getAr() {
                return ar;
            }

            public void setAr(List<ArBean> ar) {
                this.ar = ar;
            }

            public static class AlBean {
                /**
                 * id : 3358040
                 * name : 所在专辑名字
                 * picUrl : http://p2.music.126.net/7VNhKpCYVz3UWAATV_HstA==/109951163828297300.jpg
                 */

                private int id;
                private String name;
                private String picUrl;

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getPicUrl() {
                    return picUrl;
                }

                public void setPicUrl(String picUrl) {
                    this.picUrl = picUrl;
                }
            }

            public static class ArBean {
                /**
                 * id : 0
                 * name : 作者名字
                 */

                private int id;
                private String name;

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }
            }
        }

        public static class TrackIdsBean {
            /**
             * id : 424060342
             */

            private int id;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }
        }
    }
}
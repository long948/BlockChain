package com.block.xjfkchain.data;

public class LoginEntity {
    /**
     * user : {"union_uid":2292,"openid":"13800138000","role":"member","type":"username","id":593}
     * token : eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwOlwvXC9pb3QudjAzNTcuY29tXC9hcGlcL3JlZ2lzdGVyIiwiaWF0IjoxNTkxNzk3MzMxLCJleHAiOjE1OTMwOTMzMzEsIm5iZiI6MTU5MTc5NzMzMSwianRpIjoic1p2QnpVZE1FcG56ZkFWaCIsInN1YiI6NTkzLCJwcnYiOiI5MWY5NjZiYTZiYzdiZGY5MTcyZWIzZDBlOWVmZTA5MmExMjRmMDAwIn0.j4oQc5audNKK5vEbXAPub2dRxZW3ldQq2cStzk_nrW0
     */

    public UserBean user;
    public String token;

    public static class UserBean {
        /**
         * union_uid : 2292
         * openid : 13800138000
         * role : member
         * type : username
         * id : 593
         */

        public int union_uid;
        public String openid;
        public String role;
        public String type;
        public long id;
    }
}

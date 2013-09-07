package com.ihelpoo.api.service;

import com.ihelpoo.api.model.UserActiveResult;
import com.ihelpoo.api.model.WordResult;
import com.ihelpoo.api.model.base.Active;
import com.ihelpoo.api.model.base.Actives;
import com.ihelpoo.api.model.base.Notice;
import com.ihelpoo.api.model.base.ObjectReply;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: dongxu.wang@acm.org
 */
@Service
public class WordService {
    public UserActiveResult pullBy(int uid, int catalog, int schoolId, int pageIndex, int pageSize) {


        long t = System.currentTimeMillis();
        Notice notice = new Notice.Builder()
                .talk(0)
                .system(0)
                .comment(0)
                .at(0)
                .build();
//        WordResult.Blob blob = new WordResult.Blob("name","content");

        UserActiveResult.User user = new UserActiveResult.User.Builder()
                .avatar("http://static.oschina.net/uploads/user/0/12_100.jpg?t=" + System.currentTimeMillis())
                .academy("文学与传媒学院")
                .foer("12")
                .foing("21")
                .gossip("狮子女")
                .major("汉语言文学")
                .nickname("孤独不苦")
                .rank("6")
                .type("大四")
                .uid(123456)
                .build();
//        WordResult.Word word = new WordResult.Word.Builder()
//                .appclient(1)
//                .id(24968)
//                .portrait("http://ihelpoo-public.stor.sinaapp.com/useralbum/10116/101161364749925_m.jpg?t=" + t)
//                .author("陈源杉")
//                .authorid(10116)
//                .catalog(4)
//                .objecttype(3)
//                .objectcatalog(0)
//                .objecttitle("孤独")
//                .url("")
//                .objectID(136727)
//                .objectreply(blob)
//                .message("还没断网哦！得瑟一个……千万别刚得瑟完就断了啊万别刚得瑟完就断了啊万别刚得瑟完就断了啊万别刚得瑟完就断了啊万别刚得瑟完就断了啊万别刚得瑟完就断了啊")
//                .commentCount(2)
//                .pubDate("2012-06-14 22:00:22")
//                .tweetimage("http://ihelpoo-public.stor.sinaapp.com/useralbum/12781/thumb_recordsay1368613690.jpg?t=")
//                .build();
        Actives actives = new Actives();
        ObjectReply or = new ObjectReply("滔哥", "今天听歌《星月神话》我自豪且肯定的告诉老婆@走在路上 是@曾沙 唱的，现在在跪CPU。。。。");
        Active active0 = new Active.Builder()
                .sid(24968)
                .avatar("http://ihelpoo-public.stor.sinaapp.com/useralbum/10116/101161364749925_m.jpg?t=" + t)
                .name("陈源杉")
                .uid(10116)
                .catalog(4)
                .setObjecttype(3)
                .setObjectcatalog(0)
                .setObjecttitle("孤独")
                .by(3)
                .setUrl("")
                .setObjectID(136727)
                .content("还没断网哦！得瑟一个……千万别刚得瑟完就断了啊万别刚得瑟完就断了啊万别刚得瑟完就断了啊万别刚得瑟完就断了啊万别刚得瑟完就断了啊万别刚得瑟完就断了啊")
                .commentCount(0)
                .date("2012-06-14 22:00:22")
                .image("http://ihelpoo-public.stor.sinaapp.com/useralbum/12781/thumb_recordsay1368613690.jpg?t=" + t)
                .academy("[汉语言文学1]")
                .type("大三")
                .rank("2")
                .gossip("处女女")
                .diffusionCount(2)
                .online(0)
                .setObjectreply(or)
                .build();
        Active active1 = new Active.Builder()
                .sid(24968)
                .avatar("http://ihelpoo-public.stor.sinaapp.com/useralbum/10116/101161364749925_m.jpg?t=" + t)
                .name("陈源杉")
                .uid(10116)
                .catalog(4)
                .setObjecttype(3)
                .setObjectcatalog(0)
                .setObjecttitle("孤独")
                .by(3)
                .rank("2")
                .setUrl("")
                .setObjectID(136727)
                .content("还没断网哦！得瑟一个……千万别刚得瑟完就断了啊万别刚得瑟完就断了啊万别刚得瑟完就断了啊万别刚得瑟完就断了啊万别刚得瑟完就断了啊万别刚得瑟完就断了啊")
                .commentCount(0)
                .date("2012-06-14 22:00:22")
                .image("http://ihelpoo-public.stor.sinaapp.com/useralbum/12781/thumb_recordsay1368613690.jpg?t=" + t)
                .academy("[汉语言文学1]")
                .type("大三")
                .gossip("处女女")
                .diffusionCount(2)
                .online(0)
                .build();
        Active active2 = new Active.Builder()
                .sid(24968)
                .rank("2")
                .avatar("http://ihelpoo-public.stor.sinaapp.com/useralbum/10116/101161364749925_m.jpg?t=" + t)
                .name("李云龙")
                .uid(10116)
                .catalog(4)
                .setObjecttype(3)
                .setObjectcatalog(0)
                .setObjecttitle("TEST")
                .by(2)
                .setUrl("")
                .setObjectID(136727)
                .content("还没断网哦！得瑟一个……千万别刚得瑟完就断了啊…… ")
                .commentCount(3)
                .date("2013-06-14 22:00:22")
                .academy("[汉语言文学]")
                .type("大四")
                .gossip("狮子女")
                .diffusionCount(4)
                .online(1)
                .setObjectreply(or)
                .build();
        Active active3 = new Active.Builder()
                .sid(24968)
                .avatar("http://ihelpoo-public.stor.sinaapp.com/useralbum/10116/101161364749925_m.jpg?t=" + t)
                .name("李云龙")
                .rank("2")
                .uid(10116)
                .catalog(4)
                .setObjecttype(3)
                .setObjectcatalog(0)
                .setObjecttitle("TEST")
                .by(3)
                .setUrl("")
                .setObjectID(136727)
                .content("还没断网哦！得瑟一个……千万别刚得瑟完就断了啊…… ")
                .commentCount(3)
                .date("2013-06-14 22:00:22")
                .academy("[汉语言文学]")
                .type("大四")
                .gossip("狮子男")
                .diffusionCount(4)
                .online(1)
                .build();
        Active active4 = new Active.Builder()
                .sid(24968)
                .avatar("http://ihelpoo-public.stor.sinaapp.com/useralbum/10116/101161364749925_m.jpg?t=" + t)
                .name("李云龙")
                .uid(10116)
                .rank("4")
                .catalog(4)
                .setObjecttype(3)
                .setObjectcatalog(0)
                .setObjecttitle("TEST")
                .by(3)
                .setUrl("")
                .setObjectID(136727)
                .content("还没断网哦！得瑟一个……千万别刚得瑟完就断了啊…… ")
                .commentCount(3)
                .date("2013-06-14 22:00:22")
                .academy("[汉语言文学]")
                .type("大四")
                .gossip("狮子男")
                .diffusionCount(4)
                .online(1)
                .build();
        List<Active> activeList = new ArrayList<Active>();
        activeList.add(active0);
        activeList.add(active1);
        activeList.add(active2);
        activeList.add(active3);
        activeList.add(active4);
        actives.setActive(activeList);
        UserActiveResult uar = new UserActiveResult();
        uar.setNotice(notice);
        uar.setPagesize(20);
        uar.setActives(actives);
        uar.setUser(user);
        return uar;
    }


}

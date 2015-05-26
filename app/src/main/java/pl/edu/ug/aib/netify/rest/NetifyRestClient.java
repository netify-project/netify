package pl.edu.ug.aib.netify.rest;

import org.androidannotations.annotations.rest.Delete;
import org.androidannotations.annotations.rest.Get;
import org.androidannotations.annotations.rest.Post;
import org.androidannotations.annotations.rest.RequiresHeader;
import org.androidannotations.annotations.rest.Rest;
import org.androidannotations.api.rest.RestClientHeaders;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import pl.edu.ug.aib.netify.data.EmailAndPassword;
import pl.edu.ug.aib.netify.data.FriendDataList;
import pl.edu.ug.aib.netify.data.GroupData;
import pl.edu.ug.aib.netify.data.GroupDataList;
import pl.edu.ug.aib.netify.data.IdData;
import pl.edu.ug.aib.netify.data.MemberGroupData;
import pl.edu.ug.aib.netify.data.MemberGroupDataList;
import pl.edu.ug.aib.netify.data.SongData;
import pl.edu.ug.aib.netify.data.SongDataList;
import pl.edu.ug.aib.netify.data.User;
import pl.edu.ug.aib.netify.data.UserList;
import pl.edu.ug.aib.netify.data.UserLogout;
import pl.edu.ug.aib.netify.data.UserRegistrationData;

@Rest(rootUrl = "https://dsp-netify.cloud.dreamfactory.com:443/rest", converters = { MappingJackson2HttpMessageConverter.class })
@RequiresHeader({"X-Dreamfactory-Application-Name"})
public interface NetifyRestClient extends RestClientHeaders{

    //SONG_DATA
    @Get("/db/songdata?filter={filter}")
    SongDataList getSongsByGroupId(String filter);

    @Post("/db/songdata")
    @RequiresHeader({"X-Dreamfactory-Session-Token","X-Dreamfactory-Application-Name" })
    IdData addSongToGraph(SongData songData);

    //GROUP_DATA
    @Get("/db/groupdata?ids={ids}")
    GroupDataList getGroupsById(String ids);

    @Get("/db/groupdata?filter={filter}")
    GroupDataList getGroupsByQuery(String filter);

    @Post("/db/groupdata")
    @RequiresHeader({"X-Dreamfactory-Session-Token","X-Dreamfactory-Application-Name" })
    IdData addGroup(GroupData groupData);

    //MEMBER_GROUP_DATA
    @Get("/db/membergroupdata?filter={filter}")
    MemberGroupDataList getMemberGroupsByUserId(String filter);

    @Post("/db/membergroupdata")
    @RequiresHeader({"X-Dreamfactory-Session-Token","X-Dreamfactory-Application-Name" })
    IdData addMemberGroupData(MemberGroupData memberGroupData);

    //FRIENDDATA
    @Get("/db/frienddata?filter={filter}")
    FriendDataList getFriendDataByUserId(String filter);


    //USER
    @Get("/system/user?ids={ids}")
    @RequiresHeader({"X-Dreamfactory-Session-Token","X-Dreamfactory-Application-Name" })
    UserList getUsersById(String ids);

    @Get("/system/user?filter={filter}")
    @RequiresHeader({"X-Dreamfactory-Session-Token","X-Dreamfactory-Application-Name" })
    UserList getUsersByQuery(String filter);

    //AUTH
    @Post("/user/session")
    User login(EmailAndPassword emailAndPassword);

    @Delete("/user/session")
    UserLogout logout();

    @Post("/user/register/?login=true")
    User register(UserRegistrationData userData);

    @Get("/user/session")
    User getSession();

}

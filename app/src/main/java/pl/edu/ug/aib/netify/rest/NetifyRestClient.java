package pl.edu.ug.aib.netify.rest;

import org.androidannotations.annotations.rest.Delete;
import org.androidannotations.annotations.rest.Get;
import org.androidannotations.annotations.rest.Post;
import org.androidannotations.annotations.rest.RequiresHeader;
import org.androidannotations.annotations.rest.Rest;
import org.androidannotations.api.rest.RestClientHeaders;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import pl.edu.ug.aib.netify.data.EmailAndPassword;
import pl.edu.ug.aib.netify.data.SongData;
import pl.edu.ug.aib.netify.data.SongDataList;
import pl.edu.ug.aib.netify.data.User;
import pl.edu.ug.aib.netify.data.UserLogout;
import pl.edu.ug.aib.netify.data.UserRegistrationData;

@Rest(rootUrl = "https://dsp-netify.cloud.dreamfactory.com:443/rest", converters = { MappingJackson2HttpMessageConverter.class })
@RequiresHeader({"X-Dreamfactory-Application-Name"})
public interface NetifyRestClient extends RestClientHeaders{

    @Get("/db/songdata")
    SongDataList getSongs();

    @Post("/db/songdata")
    @RequiresHeader({"X-Dreamfactory-Session-Token","X-Dreamfactory-Application-Name" })
    SongData addSongToGraph(SongData songData);

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

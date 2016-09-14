package bms.artifactory.restapi;

import bms.artifactory.model.Builds;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.multipart.FormDataMultiPart;
import com.sun.jersey.multipart.file.FileDataBodyPart;
import org.apache.log4j.Logger;
import org.junit.Test;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
/**
 * Created by chjagann on 3/21/2016.
 */

public class ArtifactoryApiTest {

    private static Logger log = Logger.getLogger(ArtifactoryApiTest.class.getSimpleName());
    private static String user;
    private static String password;
    private static String url;

    static {
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(System.getProperty("user.dir").replace('\\', '/')+"/config.properties");
        } catch (FileNotFoundException e) {
            log.error("Properties file not found");
        }
        Properties properties = new Properties();

        if(inputStream!=null){
            // load the inputStream using the Properties
            try {
                properties.load(inputStream);

            } catch (IOException e) {
                e.printStackTrace();
            }

            user = properties.getProperty("user");
            password = properties.getProperty("password");
            url = properties.getProperty("artifactory-url");
        }
        try{
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private WebResource resource;

    private URI getBaseURI() {
        return UriBuilder.fromUri(url).build();
    }

    private WebResource getService() {
        ClientConfig config = new DefaultClientConfig();
        Client client = Client.create(config);
        client.addFilter(new HTTPBasicAuthFilter(user, password));
        return client.resource(getBaseURI());
    }


    /**
     * getBuilds - all builds info in artifactory instance
     * @throws JsonParseException
     * @throws JsonMappingException
     * @throws IOException
     */
    @Test
    public void getBuilds() throws JsonParseException, JsonMappingException, IOException {
        WebResource service = getService();

        log.info("verify that Artifactory is running.");
        log.info( service.path("api").path("system").path("ping").accept(MediaType.TEXT_PLAIN).get(ClientResponse.class).toString() + "\n");

        log.info("Build Information");
        WebResource.Builder wb = service.path("api").path("build").accept(MediaType.APPLICATION_JSON);
        ClientResponse response = wb.get(ClientResponse.class);

        if(response.getStatus()==200) {
            String allBuilds = wb.get(String.class).toString();
            ObjectMapper mapper = new ObjectMapper();
            Builds builds = mapper.readValue(allBuilds, Builds.class);
            assertTrue(builds.getBuilds().length > 0);
        }

    }

    /**
     * doArtifactDeploy
     * @throws IOException
     * @throws URISyntaxException
     */
    @Test
    public void doArtifactDeploy() throws IOException, URISyntaxException {

        String filepath = System.getProperty("user.dir").replace('\\', '/')+"/src/main/resources/commons-httpclient.jar";
        final File fileToUpload = new File(filepath);

        if(fileToUpload.exists()) {
            WebResource service = getService();
            WebResource wr = service.path(url+"/bmstesting-snapshot").path("commons-httpclient.jar");

            final FormDataMultiPart multiPart = new FormDataMultiPart();
            if (fileToUpload != null)
            {
                multiPart.bodyPart(new FileDataBodyPart("file", fileToUpload,MediaType.APPLICATION_OCTET_STREAM_TYPE));
            }

            log.info("doArtifactDeploy"+wr.getURI());
            final ClientResponse clientResp = wr.type(MediaType.MULTIPART_FORM_DATA_TYPE).put(ClientResponse.class,multiPart);
            log.info("Response: " + clientResp.getClientResponseStatus() + " StatusCode" + clientResp.getStatus());

        }
    }

    /**
     * Retrieve Artifact
    */
   @Test
    public void retrieveArtifact() throws JsonParseException, JsonMappingException, IOException{

        Client client = Client.create();
        WebResource wr = client.resource(url+"/bms-repo/apache-httpclient/commons-httpclient/3.1/commons-httpclient-3.1.jar");
        WebResource.Builder wb=wr.accept("application/octet-stream");
        client.addFilter(new HTTPBasicAuthFilter("admin", "password"));
        log.info("URI " + wr.getURI());
        ClientResponse response= wr.get(ClientResponse.class);

       assertEquals(200, response.getStatus());
        if(response.getStatus()==200){
            InputStream in = response.getEntityInputStream();
            assertTrue(in!=null);
            log.info("Artifact downloaded");
            if (in != null) {
               // File f = new File("C:\\RTP\\commons-httpclient.jar");

                //@TODO copy the in stream to the file f
                FileOutputStream out = new FileOutputStream(System.getProperty("user.dir")+"/commons-httpclient.jar");
                InputStream is = (InputStream)response.getEntityInputStream();
                int len = 0;
                byte[] buffer = new byte[4096];
                while((len = is.read(buffer)) != -1) {
                    out.write(buffer, 0, len);
                }
                out.flush();
                out.close();
                is.close();

                //System.out.println("Result size:" + f.length() + " written to " + f.getPath());
            }
        }
    }


    /**
     * Get Storage Information
     * Currently api response is not properly formatted, cannot map to java object with proper structure data.
     * Not triggered for test
     */
   //@Test
    public void getStorageInfo() throws IOException {

        WebResource service = getService();
        log.info("Build Storage Info");
       WebResource.Builder wb = service.path("api").path("storageinfo").accept(MediaType.APPLICATION_JSON);
       ClientResponse response = service.path("api").path("storageinfo").accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
       //log.info(storageInfo);

       assertEquals(200, response.getStatus());
       if(response.getStatus()==200){
           ObjectMapper mapper = new ObjectMapper();

           //***** Currently api response is not properly formatted, cannot map to java object with proper structure data.*****//

           /*
           StorageInfo storageInfo1 = mapper.readValue(wb.get(String.class).toString(), StorageInfo.class);

           if(storageInfo1 != null){
               assertTrue(storageInfo1.getStorageSummary()!=null);
           }*/
       }


    }

    /**
     * Copy Artifact to Destination
     */
    @Test
    public void copyArtifact(){

        Client c = Client.create();
        c.addFilter(new HTTPBasicAuthFilter(user, password));
        WebResource resource = c.resource(url+"/copy/ABR-AnyRes-release-repo?to=/bms-maven-test11-release");
        ClientResponse response = resource.post(ClientResponse.class);
        log.info(resource.getURI());

        assertEquals(200, response.getStatus());
        if(response.getStatus() ==  200){
           log.info(resource.get(String.class).toString());
        }

    }

    /**
     * Replication Status
     */
    @Test
    public void getReplicationStatus(){
        WebResource service = getService();
        log.info("Get Replication Status");
        WebResource.Builder wb = service.path("api").path("replication").path("bmstesting-snapshot")
                .accept(MediaType.APPLICATION_JSON);
        assertEquals(200,wb.get(ClientResponse.class).getStatus());
        log.info("getReplicationStatus " + wb.get(String.class).toString());
    }


    @Test
    public void deleteFile(){
        WebResource service = getService();
        log.info("Deletes the file");
        WebResource wr = service.path("bmstesting-snapshot").path("commons-httpclient.jar");

        log.info(wr.getURI());
        ClientResponse response = wr.delete(ClientResponse.class);

        assertEquals(204,response.getStatus());
    }

}

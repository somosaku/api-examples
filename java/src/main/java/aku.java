import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class aku {

    private static String URL_TOKEN = "https://devpanel.aku.mx/api/getToken";
    private static String URL_USERS = "https://devpanel.aku.mx/api/updateUsers";
    private static String API_KEY = "my_api_key";
    private static String API_SECRET = "my_api_secret";

    private static String obtainToken() {
        Algorithm algorithm = Algorithm.HMAC256(API_SECRET);
        String generatedToken = JWT.create()
                .withClaim("api_key", API_KEY)
                .sign(algorithm);
        String access_token = String.format("{ \"company\":\"empresa_chida\",\"access_code\":\"%s\"}", generatedToken);
        String access_token_base64 = Base64.getEncoder().encodeToString(access_token.getBytes());
        JSONObject response = sendRequest(URL_TOKEN, access_token_base64, "");
        String token = response.getString("token");
        return token;
    }

    private static void updateInfo(String token) {
        String data = "[ { \"name\":\"Maria Eugenia\",\"surname\":\"Fernandez Vazquez\",\"phone\":\"5512312300\",\"email\":\"maria@empresa.mx\",\"net_salary\": 10000.0 },"
                +
                "{ \"name\":\"Juan Carlos\",\"surname\":\"Rodriguez Mora\",\"phone\":\"2278978900\",\"email\":\"juan@empresa.mx\",\"net_salary\": 10000.0 } ]";
        Algorithm algorithm = Algorithm.HMAC256(API_SECRET);
        String generatedJWT = JWT.create()
                .withClaim("data", data)
                .sign(algorithm);
        JSONObject response = sendRequest(URL_USERS, token, generatedJWT);
        System.out.println(response);
    }

    private static JSONObject sendRequest(String endpoint, String access_code, String data) {
        HttpClient httpClient = HttpClientBuilder.create().build();
        try {
            // Request
            HttpPost request = new HttpPost(endpoint);
            request.addHeader("X-Access-Token", access_code);
            StringEntity params = new StringEntity(data);
            request.setEntity(params);
            HttpResponse response = httpClient.execute(request);

            // Response
            HttpEntity entity = response.getEntity();
            String json = EntityUtils.toString(entity, StandardCharsets.UTF_8);
            JSONObject parsedresponse = new JSONObject(json);
            return parsedresponse;

        } catch (Exception ex) {
            System.out.print(ex.toString());
        }
        return new JSONObject();
    }

    public static void main(String[] args) {
        String token = obtainToken();
        updateInfo(token);
    }

}

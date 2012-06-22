/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package twitterrmi;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import twitter4j.*;
import twitter4j.auth.AccessToken;
import twitter4j.auth.BasicAuthorization;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

/**
 *
 * @author alan
 */
public class TwitterImplementado {

    private String nome;
    private Twitter twitter;
    // token obtido previamente para usuario do twitter 
    private String token = "317250090-E7I0K3t7gzRyMPWCQkwpi5QAjUBtXXYqeH12yGCA";
    private String segredoToken = "gc0QDM9joUugwGqFRY4bvkILOyK8pJbuqbCipN9ik";
    private long idCredencial = 317250090;
    // chaves da aplicacao atributos estáticos -App Twiiter
    private final static String Consumer__Key = "GStEIcVNNtJVZtNQZV2fw";
    private final static String Consumer_Secret = "r9e3Limkx4WPVSsN1IqINsk7mZ1fuRulJOx1WQRs";

    public TwitterImplementado(String nome) {
        this.nome = nome;
        this.obtemInstanciaTwitter();
    }

    private void obtemInstanciaTwitter() {
        this.twitter = new TwitterFactory().getInstance();
    }

    public TwitterImplementado() {
        this.obtemInstanciaTwitter();
    }

    /**
     * @return the Consumer__Key
     */
    public String getConsumer__Key() {
        return Consumer__Key;
    }

    /**
     * @return the Consumer_Secret
     */
    public String getConsumer_Secret() {
        return Consumer_Secret;
    }

    /**
     * @return the nome
     */
    public String getNome() {
        return nome;
    }

    /**
     * @param nome the nome to set
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * @return the token
     */
    public String getToken() {
        return token;
    }

    /**
     * @param token the token to set
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * @return the segredoToken
     */
    public String getSegredoToken() {
        return segredoToken;
    }

    /**
     * @param segredoToken the segredoToken to set
     */
    public void setSegredoToken(String segredoToken) {
        this.segredoToken = segredoToken;
    }

    /**
     * @return the idCredencial
     */
    public long getIdCredencial() {
        return idCredencial;
    }

    /**
     * @param idCredencial the idCredencial to set
     */
    public void setIdCredencial(long idCredencial) {
        this.idCredencial = idCredencial;
    }

    private void setTwitterAcessToken() {
        AccessToken accessToken = new AccessToken(getToken(), getSegredoToken());
        twitter.setOAuthAccessToken(accessToken);
    }

    private void setTwitterConsumerkeyAndSecret() {
        twitter.setOAuthConsumer(TwitterImplementado.Consumer__Key, TwitterImplementado.Consumer_Secret);
    }

    public void publicaTweetXAuth() {
        ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();

        configurationBuilder.setOAuthConsumerKey(this.Consumer__Key);
        configurationBuilder.setOAuthConsumerSecret(this.Consumer_Secret);
        Configuration configuration = configurationBuilder.build();

        Twitter twitter = new TwitterFactory(configuration).getInstance(new BasicAuthorization("rcarochainfufg",
                "TwoAvgIv4"));

        AccessToken token;
        try {
            token = twitter.getOAuthAccessToken();
            System.out.println("Access Token " + token);

            String name = token.getScreenName();
            System.out.println("Screen Name" + name);

            // PrintWriter out = response.getWriter();
            System.out.println(token);

        } catch (TwitterException e) {
            System.out.println("** EXCECAO no recebimento do token de autenticacao");
            e.printStackTrace();
        }
    }

    public void modificaStatusOAuth(String novoStatus) throws TwitterException {
        setTwitterConsumerkeyAndSecret();
        setTwitterAcessToken();
        Status status = twitter.updateStatus(novoStatus);
        System.out.println("Successfully updated the status to [" + status.getText() + "].");

    }

    /*
     * Token obtido para acesso a conta <xxx> preencher apos execucao id
     * credencial = ?? token = ?? segredoToken = ??
     */
    public void obtemTokenAutenticacao() throws Exception {

        setTwitterConsumerkeyAndSecret();
        RequestToken requestToken = twitter.getOAuthRequestToken();
        AccessToken accessToken = null;
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        while (null == accessToken) {
            System.out.println("Open the following URL and grant access to your account:");
            System.out.println(requestToken.getAuthorizationURL());
            System.out.print("Enter the PIN(if aviailable) or just hit enter.[PIN]:");
            String pin = br.readLine();
            try {
                if (pin.length() > 0) {
                    accessToken = twitter.getOAuthAccessToken(requestToken, pin);
                } else {
                    accessToken = twitter.getOAuthAccessToken();
                }
            } catch (TwitterException te) {
                if (401 == te.getStatusCode()) {
                    System.out.println("Unable to get the access token.");
                } else {
                    te.printStackTrace();
                }
            }
        }
        // persist to the accessToken for future reference.
        System.out.println("id credencial = "
                + twitter.verifyCredentials().getId());
        System.out.println("token         = " + accessToken.getToken());
        System.out.println("segredoToken  = " + accessToken.getTokenSecret());
        System.exit(0);

        /*
         * Podemos trabalhar de forma que permita o acesso por outra conta.
         */
        this.setToken(accessToken.getToken());
        this.setSegredoToken(accessToken.getTokenSecret());
        this.setIdCredencial(accessToken.getUserId());
    }

    public ArrayList<String> pesquisa(String pesquisa) {
        Query query = new Query(pesquisa);
        QueryResult result;
        ArrayList<String> resultado = null;
        try {
            result = twitter.search(query);
            resultado = retornaTwitters(result);
        } catch (TwitterException e) {
            e.printStackTrace();
        }
        return resultado;
    }

    public ArrayList<String> recuperaStatus() {
        ResponseList<Status> listStatus = null;
//        this.setTwitterConsumerkeyAndSecret();
//        this.setTwitterAcessToken();
        try {
            listStatus = twitter.getHomeTimeline();
        } catch (TwitterException e) {
        }
        return retornaListStatus(listStatus);
    }

    private ArrayList<String> retornaListStatus(List<Status> listStatus) {
        ArrayList<String> ListStringStatus = null;
        for (Status status : listStatus) {
            ListStringStatus.add(status.getId() + " - " + status.getCreatedAt().toString() + " - " + status.getText());
//            ListStringStatus.add(status.getId() + " - " + status.getCreatedAt().toString() + "- " +"LAT: " +status.getGeoLocation().getLongitude() +" LONG: " +status.getGeoLocation().getLatitude() + " - " + status.getPlace() +" - " + status.getText());
        }

        return ListStringStatus;
    }

    public static void main(String[] args) {

        List<Status> statuses = null;

        try {
            ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();

            configurationBuilder.setOAuthConsumerKey(TwitterImplementado.Consumer__Key);
            configurationBuilder.setOAuthConsumerSecret(TwitterImplementado.Consumer_Secret);
            Configuration configuration = configurationBuilder.build();

            Twitter twitter = new TwitterFactory(configuration).getInstance(new BasicAuthorization("alanbarbosa_gyn",
                    "alan100691"));
            int page = 1;
            do {
                statuses = twitter.getHomeTimeline();
                for (Status status : statuses) {
                    System.out.println("@" + status.getUser().getScreenName() + " - " + status.getText());
                }
                page++;
            } while (statuses.size() > 0 && page < 10);
            System.out.println("done.");
            System.exit(0);

        } catch (TwitterException te) {
            te.printStackTrace();
            System.out.println("Failed to get favorites: " + te.getMessage());
        }
    }

    private ArrayList<String> retornaTwitters(QueryResult result) {
        ArrayList<String> resultado = new ArrayList<String>();
        for (Tweet tweet : result.getTweets()) {
            resultado.add((tweet.getFromUser() + "@"
                    + tweet.getGeoLocation() + " :" + tweet.getText()));
        }
        return resultado;
    }
}

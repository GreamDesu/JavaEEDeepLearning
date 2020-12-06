package br.com.example.javaee.chat;

import br.com.example.javaee.chat.model.ChatMessage;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.logging.Logger;

public class ChatState {
    private final Logger log = Logger.getLogger(getClass().getName());
    private String chatId;
    private List<ChatMessage> messages = new ArrayList<>();

    public ChatState(String chatId) {
        this.chatId = chatId;
    }

    public String getChatId() {
        return chatId;
    }

    public void addMessage(ChatMessage message) throws IOException {

        this.messages.add(message);
        log.info(doPost("https://apis.paralleldots.com/v4/keywords"));

//        URL url = new URL ("https://apis.paralleldots.com/v4/keywords");
//        Map<String,Object> params = new LinkedHashMap<>();
//        params.put("text", this.messages);
//        params.put("api_key", "U3xH7qzhaCmzmhXaHanOlPJP2EsmafizqlQAnUdkyh8");
//
//        StringBuilder postData = new StringBuilder();
//        for (Map.Entry<String,Object> param : params.entrySet()) {
//            if (postData.length() != 0) postData.append('&');
//            postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
//            postData.append('=');
//            postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
//        }
//        byte[] postDataBytes = postData.toString().getBytes(StandardCharsets.UTF_8);
//
//        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
//        conn.setRequestMethod("POST");
//        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
//        conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
//        conn.setDoOutput(true);
//        conn.getOutputStream().write(postDataBytes);
//
//        Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
//
//        for (int c; (c = in.read()) >= 0;)
//            log.info(String.valueOf((char)c));

    }

    public String doPost(String url) throws UnsupportedEncodingException {
        String postData = URLEncoder.encode("text", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(this.messages), "UTF-8");
        postData += "&" + URLEncoder.encode("api_key", "UTF-8") + "=" + URLEncoder.encode("U3xH7qzhaCmzmhXaHanOlPJP2EsmafizqlQAnUdkyh8", "UTF-8");
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // build connection
            URLConnection conn = realUrl.openConnection();
            // set request properties
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
            // enable output and input
            conn.setDoOutput(true);
            conn.setDoInput(true);
            out = new PrintWriter(conn.getOutputStream());
            // send POST DATA
            out.print(postData);
            out.flush();
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += "/n" + line;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }

    public List<ChatMessage> getMessages() {
        return Collections.unmodifiableList(messages);
    }
}

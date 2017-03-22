/**
 * Created by Хускар on 21.03.2017.
 */
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.*;
import  org.elasticsearch.transport.* ;
import org.elasticsearch.client.transport.*;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;
import java.net.UnknownHostException;

import static org.elasticsearch.index.query.QueryBuilders.*;
public class Main {
    static TransportClient  client;
    public static void main(String[] args){
                try {
                      client = new PreBuiltTransportClient(Settings.EMPTY)
            //Client client=TransportClient.
                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300));
                   // .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("host2"), 9300));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

// on shutdown


      //  SearchResponse response = client.prepareSearch(String.valueOf(QueryBuilders.matchAllQuery()))
         //       .setQuery()
           //     .get();
    }}
//client.close();
/**
 * Created by Хускар on 21.03.2017.
 */
import org.apache.lucene.queryparser.flexible.core.builders.QueryBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.filters.FiltersAggregator;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import  org.elasticsearch.transport.* ;
import org.elasticsearch.client.transport.*;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.elasticsearch.index.query.QueryBuilders.*;
public class Main {


    static TransportClient  client;
    public static void main(String[] args){
        Map<String, Object> res=new HashMap<String, Object>();;
                try {
                      client = new PreBuiltTransportClient(Settings.EMPTY)
            //Client client=TransportClient.
                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300));
                   // .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("host2"), 9300));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        //System.out.println("22");
// on shutdown
        //SearchResponse response = client.prepareSearch("packetbeat-2017.03.12").setTypes("DNS").setQuery().get();
        //SearchResponse response = client.prepareSearch(String.valueOf(QueryBuilders.matchAllQuery()))
          //      .setQuery();
        SearchResponse response = client.prepareSearch("packetbeat-2017.03.12")
                .setQuery(QueryBuilders.rangeQuery("@timestamp").gte("now-12d").lt("now-10d"))
               //.addAggregation(AggregationBuilders.terms("dns.question.type").subAggregation(AggregationBuilders.count("dns.question.type"))).get();
                //.addAggregation(QueryBuilders.termsQuery("dns.question.type","",)).get();
               // .addAggregation(AggregationBuilders.terms("dns.question.type").subAggregation(AggregationBuilders.cardinality("agg").field("dns.question.type"))).get();
                .addAggregation(AggregationBuilders
                        .terms("q").field("dns.question.type")).execute().actionGet();
                Terms dns_question_type=response.getAggregations().get("q");
                for (Terms.Bucket entry : dns_question_type.getBuckets()) {
                                 res.put(entry.getKeyAsString(),entry.getDocCount());
                    System.out.println(entry.getKeyAsString()+entry.getDocCount());
                }
                                 //entry.getKey();      // Term
                                 //entry.getDocCount(); // Doc count
                //.setQuery(QueryBuilders.rangeQuery("@timestamp").gte("1489303962532").lt("1489344462532").format("epoch_millis")).get();
                //.addAggregation(AggregationBuilders.terms("dns.question.type").field("dns.question.type").subAggregation()).get();

                //.addAggregation(QueryBuilders.termsQuery("dns.question.type",AggregationBuilders.count("desk"))).get();
        System.out.println("33");
      //  SearchResponse response = client.prepareSearch(String.valueOf(QueryBuilders.matchAllQuery()))
         //       .setQuery()
           //     .get();
        SearchHit[] hits = response.getHits().getHits();
       Map<String, Object> result = hits[0].sourceAsMap();
        System.out.println("44");
        System.out.print(result.get(0));
    }}//.setQuery(String.valueOf(QueryBuilders.rangeQuery("@timestamp").gte("1489303962532").lt("1489344462532").format("epoch_millis")))
//client.close();